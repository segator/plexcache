/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.exception;

import com.github.segator.plexcache.constants.MetadataErrorCodeEnum;


/**
 *
 * @author isaac
 */
public class FileMetadataException extends Exception {

    private final MetadataErrorCodeEnum error;
    private Throwable internalThrowable;

    public FileMetadataException(MetadataErrorCodeEnum error) {
        this.error = error;
    }

    public FileMetadataException(MetadataErrorCodeEnum error, Throwable internalThrowable) {
        this(error);
        this.internalThrowable = internalThrowable;
    }

    public MetadataErrorCodeEnum getError() {
        return error;
    }

    public Throwable getInternalThrowable() {
        return internalThrowable;
    }

}
