/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.exception;

import com.github.segator.plexcache.constants.StorageErrorCodeEnum;


/**
 *
 * @author isaac
 */
public class FileStorageException extends Exception {

    private final StorageErrorCodeEnum error;
    private Throwable internalThrowable;

    public FileStorageException(StorageErrorCodeEnum error) {
        this.error = error;
    }

    public FileStorageException(StorageErrorCodeEnum error, Throwable internalThrowable) {
        this(error);
        this.internalThrowable = internalThrowable;
    }

    public StorageErrorCodeEnum getError() {
        return error;
    }

    public Throwable getInternalThrowable() {
        return internalThrowable;
    }

}
