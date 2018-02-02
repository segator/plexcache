/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.dao.impl;

import com.github.segator.plexcache.config.entity.PlexCacheConfig;
import com.github.segator.plexcache.dao.StorageDAO;
import com.github.segator.plexcache.entity.FileFuse;
import com.github.segator.plexcache.entity.cache.FileCachedBlock;
import com.github.segator.plexcache.entity.cache.FileFuseCache;
import com.github.segator.plexcache.exception.FileStorageException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author isaac
 */
@Component
public class LocalCacheStorageDAO implements StorageDAO{
    
    @Autowired
    private LocalStorageDAO localStorageDAO;
    
    @Autowired
    private PlexCacheConfig config;
    
    Map<String,FileFuseCache> filefuseCacheMap;

    @Override
    public byte[] read(FileFuse file, long readOffset, int size) throws FileStorageException {
        List<FileCachedBlock> blocks= getCachedBlocks(file,readOffset,size);
        return localStorageDAO.read(file, readOffset, size);
    }

    @Override
    public void write(FileFuse file, byte[] buffer, long writeOffset) throws FileStorageException {
        localStorageDAO.write(file, buffer, writeOffset);
    }

    @Override
    public void truncate(FileFuse file, long size) throws FileStorageException {
        localStorageDAO.truncate(file, size);
    }

    private List<FileCachedBlock> getCachedBlocks(FileFuse file, long readOffset, int size) {
        FileFuseCache fileCache = getFileFuseCache(file.getAbsolutePath().toString());
        return null;
    }

    @Override
    public void startService() throws Exception {
        filefuseCacheMap=new HashMap();
    }

    @Override
    public void stopService() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private FileFuseCache getFileFuseCache(String path) {
        FileFuseCache fileFusecCache =  filefuseCacheMap.get(path);
        if(fileFusecCache==null){
            
        }
    }

}
