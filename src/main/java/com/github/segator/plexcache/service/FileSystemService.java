/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.service;

import com.github.segator.plexcache.constants.MetadataErrorCodeEnum;
import com.github.segator.plexcache.dao.impl.LocalCacheStorageDAO;
import com.github.segator.plexcache.dao.impl.MemoryMetadataDAO;
import com.github.segator.plexcache.entity.FileAbstractFuse;
import com.github.segator.plexcache.entity.FileFuse;
import com.github.segator.plexcache.entity.FileSystemStats;
import com.github.segator.plexcache.exception.FileMetadataException;
import com.github.segator.plexcache.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author isaac
 */
@Service
public class FileSystemService {

    @Autowired
    private MemoryMetadataDAO metadataDAO;

    @Autowired
    private LocalCacheStorageDAO storageDAO;

    public FileAbstractFuse getFileFromPath(String path) throws FileMetadataException {
        try {
            return metadataDAO.searchFileByPath(path);
        } catch (FileMetadataException metadataException) {
            if (metadataException.getError() == MetadataErrorCodeEnum.FILE_NOT_EXIST) {
                return null;
            } else {
                throw metadataException;
            }
        }
    }

    public byte[] readFile(FileFuse file, int size, long offset) throws FileStorageException {
        return storageDAO.read(file, offset, size);
    }

    public FileSystemStats getFileSystemStats() throws FileMetadataException {
        return metadataDAO.getStats();
    }

    public void initializeFileSystem() throws Exception {
        metadataDAO.startService();
    }

    public void stopFileSystem() throws Exception {
        metadataDAO.stopService();
    }
}
