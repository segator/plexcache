/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.config;

import com.github.segator.plexcache.config.entity.PlexCacheConfig;
import com.github.segator.plexcache.factory.MetadataDAOFactory;
import com.github.segator.plexcache.factory.StorageDAOFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author isaac
 */
@Configuration
public class FactoriesConfiguration {

    @Autowired
    PlexCacheConfig config;

//    @Autowired
//    @Bean
//    public MetadataDAOFactory metadataDAOFactory() {
//        MetadataDAOFactory factory = new MetadataDAOFactory();
//        factory.setMetadataType(config.getMetadataType());
//        return factory;
//    }
//
//    @Autowired
//    @Bean
//    public StorageDAOFactory storageDAOFactory() {
//        StorageDAOFactory factory = new StorageDAOFactory();
//        factory.setStorageType(config.getStorageType());
//        return factory;
//    }
}
