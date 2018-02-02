/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.controller;

import com.github.segator.plexcache.dao.impl.MemoryMetadataDAO;
import com.github.segator.plexcache.entity.cache.StorageCacheStats;
import com.github.segator.plexcache.exception.FileMetadataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author isaac
 */
@RestController
public class RestAPI {

    @Autowired 
    private MemoryMetadataDAO memoryMetadata;
    @RequestMapping("/ping")
    public String about() {
        return "asdsad";
    }
    
    @RequestMapping("/cache/metadata/refresh")
    public void refreshMetadata() throws FileMetadataException{
        memoryMetadata.reloadMetadata();
    }
    
    @RequestMapping("/cache/storage/stats")
    public StorageCacheStats getStorageCacheStats(){
        return null;
    }

}
