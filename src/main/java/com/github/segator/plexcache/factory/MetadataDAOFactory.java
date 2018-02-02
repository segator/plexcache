/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.factory;

import com.github.segator.plexcache.dao.MetadataDAO;
import com.github.segator.plexcache.dao.impl.LocalMetadataDAO;
import com.github.segator.plexcache.dao.impl.MemoryMetadataDAO;

/**
 *
 * @author isaac
 */
public class MetadataDAOFactory extends AbstractAutowiringFactoryBean<MetadataDAO>  {

    private String metadataType;

    public String getMetadataType() {
        return metadataType;
    }

    public void setMetadataType(String metadataType) {
        this.metadataType = metadataType;
    }


    @Override
    protected MetadataDAO doCreateInstance() {
                if (getMetadataType().equals("memory")) {
            return new MemoryMetadataDAO();
        } else if (getMetadataType().equals("local")) {
            return new LocalMetadataDAO();
        }
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return MetadataDAO.class;
    }



}
