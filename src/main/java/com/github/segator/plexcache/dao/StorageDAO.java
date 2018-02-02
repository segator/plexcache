/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.dao;

import com.github.segator.plexcache.entity.FileFuse;
import com.github.segator.plexcache.exception.FileStorageException;
import com.github.segator.plexcache.system.StartableService;
import com.github.segator.plexcache.system.StopableService;
import java.nio.ByteBuffer;


/**
 *
 * @author isaac
 */
public interface StorageDAO extends StartableService,StopableService {

    public byte[] read(FileFuse file, long readOffset, int size) throws FileStorageException;

    public void write(FileFuse file, byte[] buffer, long writeOffset) throws FileStorageException;

    public void truncate(FileFuse file, long size) throws FileStorageException;   
    
}
