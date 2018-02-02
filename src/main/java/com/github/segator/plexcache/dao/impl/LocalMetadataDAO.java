/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.dao.impl;

import com.github.segator.plexcache.config.entity.PlexCacheConfig;
import com.github.segator.plexcache.constants.MetadataErrorCodeEnum;
import com.github.segator.plexcache.dao.MetadataDAO;
import com.github.segator.plexcache.entity.DirectoryFuse;
import com.github.segator.plexcache.entity.FileAbstractAttributes;
import com.github.segator.plexcache.entity.FileAbstractFuse;
import com.github.segator.plexcache.entity.FileFuse;
import com.github.segator.plexcache.entity.FileSystemStats;
import com.github.segator.plexcache.exception.FileMetadataException;
import com.github.segator.plexcache.service.JNRFileSystemService;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.Date;

import jnr.ffi.Platform;
import static jnr.ffi.Platform.OS.WINDOWS;
import static jnr.ffi.Platform.OS.LINUX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author isaac
 */
@Component
public class LocalMetadataDAO implements MetadataDAO {

    @Autowired
    public PlexCacheConfig configuration;

    @Autowired
    JNRFileSystemService sadsad;

    @Override
    public FileAbstractAttributes getAttributes(FileAbstractFuse fileAbstract) throws FileMetadataException {
        try {
            FileAbstractAttributes fileAttributes = null;
            if (Platform.getNativePlatform().getOS() == WINDOWS) {
                fileAttributes = getAttributesWindows(fileAbstract);
            } else if (Platform.getNativePlatform().getOS() == LINUX) {
                fileAttributes = getAttributesLinux(fileAbstract);
            }
            return fileAttributes;
        } catch (IOException ex) {
            throw new FileMetadataException(MetadataErrorCodeEnum.IO_ERROR, ex);
        }
    }

    @Override
    public void setAttributes(FileAbstractFuse fileAbstract) throws FileMetadataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FileAbstractAttributes rename(FileAbstractFuse fileAbstract, String newName) throws FileMetadataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DirectoryFuse mkdir(DirectoryFuse parentDirectory, String name) throws FileMetadataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void rmdir(DirectoryFuse fileAbstract) throws FileMetadataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Path getLocalPath(Path relativePath) {
        return getLocalPath(relativePath.toString());
    }

    private Path getLocalPath(String relativePath) {
        return Paths.get(configuration.getStorageBackendPath(), relativePath);
    }

    private Path removeLocalPath(Path fullPath) {
        return Paths.get(fullPath.toString().substring(Paths.get(configuration.getStorageBackendPath()).toString().length() - 1));
    }

    @Override
    public FileSystemStats getStats() throws FileMetadataException {
        try {
            FileStore fsStore = Files.getFileStore(getLocalPath(File.separator));
            FileSystemStats fsStats = new FileSystemStats();
            fsStats.setTotalSize(fsStore.getTotalSpace());
            fsStats.setUsedSize(fsStore.getTotalSpace() - fsStore.getUsableSpace());
            return fsStats;
        } catch (IOException ex) {
            throw new FileMetadataException(MetadataErrorCodeEnum.IO_ERROR, ex);
        }
    }

    @Override
    public FileAbstractFuse searchFileByPath(String path) throws FileMetadataException {
        FileAbstractFuse fileFuse = null;
        Path filePath = getLocalPath(path);
        File localFile = filePath.toFile();
        if (!localFile.exists()) {
            return null;
        }
        if (localFile.isDirectory()) {
            fileFuse = new DirectoryFuse();
            if (localFile.listFiles() != null) {
                for (File childFile : localFile.listFiles()) {
                    FileAbstractFuse childFileAbstract = null;
                    if (childFile.isFile()) {
                        childFileAbstract = new FileFuse();
                    } else if (childFile.isDirectory()) {
                        childFileAbstract = new DirectoryFuse();
                    }
                    childFileAbstract.setAbsolutePath(removeLocalPath(childFile.toPath()));
                    childFileAbstract.setName(childFile.getName().equals("") ? File.separator : childFile.getName());
                    childFileAbstract.setAttributes(getAttributes(childFileAbstract));
                    ((DirectoryFuse) fileFuse).getChilds().add(childFileAbstract);
                }
            }
        } else if (localFile.isFile()) {
            fileFuse = new FileFuse();
        } else {
            throw new FileMetadataException(MetadataErrorCodeEnum.UNKNOW_OBJECT_TYPE);
        }
        fileFuse.setAbsolutePath(removeLocalPath(filePath));
        fileFuse.setName(localFile.getName().equals("") ? File.separator : localFile.getName());
        fileFuse.setAttributes(getAttributes(fileFuse));
        return fileFuse;
    }

    private FileAbstractAttributes getAttributesWindows(FileAbstractFuse fileAbstract) throws IOException {
        DosFileAttributes attributes = Files.readAttributes(getLocalPath(fileAbstract.getAbsolutePath()), DosFileAttributes.class);
        FileAbstractAttributes fileAttributes = new FileAbstractAttributes();
        fileAttributes.setCreationTime(new Date(attributes.creationTime().toMillis()));
        //fileAttributes.setGid(0);
        fileAttributes.setModificationTime(new Date(attributes.lastModifiedTime().toMillis()));
        fileAttributes.setPermissions(0777);
        if (fileAbstract.getClass().equals(FileFuse.class)) {
            fileAttributes.setSize(attributes.size());
        }
        //fileAttributes.setUid(0);
        return fileAttributes;

    }

    private FileAbstractAttributes getAttributesLinux(FileAbstractFuse fileAbstract) throws IOException {
        Path localPath = getLocalPath(fileAbstract.getAbsolutePath());
        PosixFileAttributes attributes = Files.readAttributes(localPath, PosixFileAttributes.class);
        FileAbstractAttributes fileAttributes = new FileAbstractAttributes();
        fileAttributes.setCreationTime(new Date(attributes.creationTime().toMillis()));
        fileAttributes.setGid((int) Files.getAttribute(localPath, "unix:gid"));
        fileAttributes.setModificationTime(new Date(attributes.lastModifiedTime().toMillis()));
        fileAttributes.setPermissions((int) Files.getAttribute(localPath, "unix:mode"));
        fileAttributes.setSize(attributes.size());
        fileAttributes.setUid((int) Files.getAttribute(localPath, "unix:uid"));
        return fileAttributes;
    }

    @Override
    public void startService() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stopService() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
