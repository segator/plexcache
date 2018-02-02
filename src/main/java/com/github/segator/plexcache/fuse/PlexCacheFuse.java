package com.github.segator.plexcache.fuse;

import com.github.segator.plexcache.service.JNRFileSystemService;
import jnr.ffi.Pointer;
import jnr.ffi.types.mode_t;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.FuseStubFS;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.FuseFileInfo;
import ru.serce.jnrfuse.struct.Statvfs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlexCacheFuse extends FuseStubFS {

    @Autowired
    JNRFileSystemService jnrFS;

    @Override
    public int create(String path, @mode_t long mode, FuseFileInfo fi) {
        return -ErrorCodes.EROFS();
    }

    @Override
    public int getattr(String path, FileStat stat) {
        return jnrFS.getAttributes(path, stat,getContext());
    }

    @Override
    public int mkdir(String path, @mode_t long mode) {
        return -ErrorCodes.EROFS();
    }

    @Override
    public int read(String path, Pointer buf, @size_t long size, @off_t long offset, FuseFileInfo fi) {
        return jnrFS.read(path, buf,(int) size, offset, fi);
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filter, @off_t long offset, FuseFileInfo fi) {
        return jnrFS.readDir(path, buf, filter, offset, fi,getContext());
    }

    @Override
    public int statfs(String path, Statvfs stbuf) {
        return jnrFS.fileSystemStats(path, stbuf);
    }

    @Override
    public int rename(String path, String newName) {
        return -ErrorCodes.EROFS();
    }

    @Override
    public int rmdir(String path) {
        return -ErrorCodes.EROFS();
    }

    @Override
    public int truncate(String path, long offset) {
        return -ErrorCodes.EROFS();
    }

    @Override
    public int unlink(String path) {
        return -ErrorCodes.EROFS();
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
        return 0;
    }

    @Override
    public int write(String path, Pointer buf, @size_t long size, @off_t long offset, FuseFileInfo fi) {
        return -ErrorCodes.EROFS();
    }


    public void start() throws Exception {
        jnrFS.initializeFileSystem();
    }

    public void stop() throws Exception {
        jnrFS.stopFileSystem();
    }
}
