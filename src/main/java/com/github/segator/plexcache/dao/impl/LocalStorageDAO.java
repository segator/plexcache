/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.dao.impl;

import com.github.segator.plexcache.config.entity.PlexCacheConfig;
import com.github.segator.plexcache.constants.StorageErrorCodeEnum;
import com.github.segator.plexcache.dao.StorageDAO;
import com.github.segator.plexcache.entity.FileFuse;
import com.github.segator.plexcache.exception.FileStorageException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author isaac
 */
@Component
public class LocalStorageDAO implements StorageDAO {

    @Autowired
    private PlexCacheConfig configuration;

    private Path getLocalPath(Path relativePath) {
        return getLocalPath(relativePath.toString());
    }

    private Path getLocalPath(String relativePath) {
        return Paths.get(configuration.getStorageBackendPath(), relativePath);
    }

    @Override
    public byte[] read(FileFuse file, long readOffset, int size) throws FileStorageException {
        try {

            Path localPath = getLocalPath(file.getAbsolutePath());
            FileInputStream fis = new FileInputStream(localPath.toFile());
            if (readOffset > file.getAttributes().getSize()) {
                throw new FileStorageException(StorageErrorCodeEnum.INVALID_OFFSET);
            }
            int bytesToRead = (int) Math.min(file.getAttributes().getSize() - readOffset, size);
            byte[] buffer = new byte[bytesToRead];
            fis.skip(readOffset);
            int readedBytes = fis.read(buffer);
            if(readedBytes!=bytesToRead){
                System.out.println("asd");
            }
            return buffer;

        } catch (IOException ex) {
            throw new FileStorageException(StorageErrorCodeEnum.IO_ERROR, ex);
        }
    }

    @Override
    public void write(FileFuse file, byte[] buffer, long writeOffset) throws FileStorageException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void truncate(FileFuse file, long size) throws FileStorageException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
