/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.factory;

import com.github.segator.plexcache.dao.StorageDAO;
import com.github.segator.plexcache.dao.impl.LocalCacheStorageDAO;
import com.github.segator.plexcache.dao.impl.LocalStorageDAO;

/**
 *
 * @author isaac
 */
public class StorageDAOFactory extends AbstractAutowiringFactoryBean<StorageDAO> {

    private String storageType;

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    @Override
    public Class<?> getObjectType() {
        return StorageDAO.class;
    }

    @Override
    protected StorageDAO doCreateInstance() {
        if (getStorageType().equals("local")) {
            return new LocalStorageDAO();
        } else if (getStorageType().equals("cache")) {
            return new LocalCacheStorageDAO();
        }
        return null;
    }

}
