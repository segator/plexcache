/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.service;

import com.github.segator.plexcache.config.entity.PlexCacheConfig;
import com.github.segator.plexcache.entity.DirectoryFuse;
import com.github.segator.plexcache.entity.FileAbstractAttributes;
import com.github.segator.plexcache.entity.FileAbstractFuse;
import com.github.segator.plexcache.entity.FileFuse;
import com.github.segator.plexcache.entity.FileSystemStats;
import com.github.segator.plexcache.exception.FileMetadataException;
import com.github.segator.plexcache.exception.FileStorageException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jnr.ffi.Platform;
import static jnr.ffi.Platform.OS.WINDOWS;
import jnr.ffi.Pointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.FuseContext;
import ru.serce.jnrfuse.struct.FuseFileInfo;
import ru.serce.jnrfuse.struct.Statvfs;

/**
 *
 * @author isaac
 */
@Service
public class JNRFileSystemService {

    @Autowired
    FileSystemService fileSystem;

    @Autowired
    PlexCacheConfig configuration;

    public int getAttributes(String path, FileStat stat, FuseContext context) {
        try {
            FileAbstractFuse file = fileSystem.getFileFromPath(path);
            if (file == null) {
                return -ErrorCodes.ENOENT();
            }

            modifyFileStat(stat, context, file);
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -ErrorCodes.ECONNABORTED();
        }
    }

    private void modifyFileStat(FileStat stat, FuseContext context, FileAbstractFuse fileAbstract) {
        FileAbstractAttributes attributes = fileAbstract.getAttributes();

        if (fileAbstract.getClass().equals(FileFuse.class)) {
            stat.st_mode.set(FileStat.S_IFREG | attributes.getPermissions());
            stat.st_size.set(attributes.getSize());
        } else if (fileAbstract.getClass().equals(DirectoryFuse.class)) {
            stat.st_mode.set(FileStat.S_IFDIR | attributes.getPermissions());
        }
        if (Platform.getNativePlatform().getOS() == WINDOWS) {
            stat.st_uid.set(context.uid.get());
            stat.st_gid.set(context.gid.get());
            stat.st_birthtime.tv_nsec.set(attributes.getCreationTime().getTime() * 1000000);
            stat.st_ctim.tv_nsec.set(attributes.getCreationTime().getTime() * 1000000);
            stat.st_mtim.tv_nsec.set(attributes.getModificationTime().getTime() * 1000000);
        } else {
            stat.st_uid.set(attributes.getUid());
            stat.st_gid.set(attributes.getGid());
            stat.st_birthtime.tv_nsec.set(attributes.getCreationTime().getTime());
            stat.st_ctim.tv_nsec.set(attributes.getCreationTime().getTime());
            stat.st_mtim.tv_nsec.set(attributes.getModificationTime().getTime());
        }
    }

    public int read(String path, Pointer fuseBuffer, int size, long offset, FuseFileInfo fi) {
        try {
            FileAbstractFuse file = fileSystem.getFileFromPath(path);
            if (file == null) {
                return -ErrorCodes.ENOENT();
            } else if (file.getClass().equals(DirectoryFuse.class)) {
                return -ErrorCodes.EISDIR();
            }

            byte[] fileBuffer = fileSystem.readFile((FileFuse) file, size, offset);
            fuseBuffer.put(0, fileBuffer, 0, fileBuffer.length);
            return fileBuffer.length;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -ErrorCodes.ECONNABORTED();
        }
    }

    public int readDir(String path, Pointer buf, FuseFillDir filter, long offset, FuseFileInfo fi, FuseContext context) {
        try {
            FileAbstractFuse file = fileSystem.getFileFromPath(path);
            if (file == null) {
                return -ErrorCodes.ENOENT();
            } else if (file.getClass().equals(FileFuse.class)) {
                return -ErrorCodes.ENOTDIR();
            }
            DirectoryFuse directory = (DirectoryFuse) file;
            filter.apply(buf, ".", null, 0);
            filter.apply(buf, "..", null, 0);
            for (FileAbstractFuse childFile : directory.getChilds()) {
                //FileStat childStat = FileStat.of(buf);
                //modifyFileStat(childStat, context, childFile);
                filter.apply(buf, childFile.getName(), null, 0);
            }
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -ErrorCodes.ECONNABORTED();
        }
    }

    public int fileSystemStats(String path, Statvfs stbuf) {
        try {
            FileSystemStats fsStats = fileSystem.getFileSystemStats();
            // statfs needs to be implemented on Windows in order to allow for copying
            // data from other devices because winfsp calculates the volume size based
            // on the statvfs call.
            // see https://github.com/billziss-gh/winfsp/blob/14e6b402fe3360fdebcc78868de8df27622b565f/src/dll/fuse/fuse_intf.c#L654
            if ("/".equals(path)) {
                stbuf.f_frsize.set(1);
                stbuf.f_blocks.set(fsStats.getTotalSize());
                stbuf.f_bfree.set(fsStats.getTotalSize() - fsStats.getUsedSize());  // free blocks in fs
                stbuf.f_bavail.set(fsStats.getTotalSize() - fsStats.getUsedSize());
            }
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -ErrorCodes.ECONNABORTED();
        }
    }

    public void initializeFileSystem() throws Exception {
        fileSystem.initializeFileSystem();
    }

    public void stopFileSystem() throws Exception {
        fileSystem.stopFileSystem();
    }

}
