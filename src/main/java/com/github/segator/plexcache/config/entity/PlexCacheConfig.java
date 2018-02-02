/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.config.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author isaac
 */
@Configuration
@ConfigurationProperties
public class PlexCacheConfig {

    //Fuse configuration
    private String mountpoint = "O:\\";

    //Metadata configuration
    private String metadataType = "memory";

    //Storage configuration
    private String storageType = "cache";
    private String storageReadOnlyPath = "local";
    private String storageBackendPath = "D:\\";
    private String storageCachePath = "D:\\temp";

    public String getMountpoint() {
        return mountpoint;
    }

    public void setMountpoint(String mountpoint) {
        this.mountpoint = mountpoint;
    }

    public String getMetadataType() {
        return metadataType;
    }

    public void setMetadataType(String metadataType) {
        this.metadataType = metadataType;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getStorageReadOnlyPath() {
        return storageReadOnlyPath;
    }

    public void setStorageReadOnlyPath(String storageReadOnlyPath) {
        this.storageReadOnlyPath = storageReadOnlyPath;
    }

    public String getStorageBackendPath() {
        return storageBackendPath;
    }

    public void setStorageBackendPath(String storagebackendPath) {
        this.storageBackendPath = storagebackendPath;
    }

    public String getStorageCachePath() {
        return storageCachePath;
    }

    public void setStorageCachePath(String storageCachePath) {
        this.storageCachePath = storageCachePath;
    }

}
