/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.dao;

import com.github.segator.plexcache.system.StopableService;
import com.github.segator.plexcache.system.StartableService;
import com.github.segator.plexcache.exception.FileMetadataException;
import com.github.segator.plexcache.entity.DirectoryFuse;
import com.github.segator.plexcache.entity.FileAbstractFuse;
import com.github.segator.plexcache.entity.FileAbstractAttributes;
import com.github.segator.plexcache.entity.FileSystemStats;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author isaac
 */
public interface MetadataDAO extends StartableService,StopableService {

    public FileAbstractAttributes getAttributes(FileAbstractFuse fileAbstract) throws FileMetadataException;

    public void setAttributes(FileAbstractFuse fileAbstract) throws FileMetadataException;

    public FileAbstractAttributes rename(FileAbstractFuse fileAbstract, String newName) throws FileMetadataException;

    public DirectoryFuse mkdir(DirectoryFuse parentDirectory, String name) throws FileMetadataException;

    public void rmdir(DirectoryFuse fileAbstract) throws FileMetadataException;

    public FileSystemStats getStats() throws FileMetadataException;

    public FileAbstractFuse searchFileByPath(String path) throws FileMetadataException;

}
