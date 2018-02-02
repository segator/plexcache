/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.entity.cache;

import java.util.Date;

/**
 *
 * @author isaac
 */
public class FileCachedBlock {

    private Date cachedTime;
    private long accessCount;
    private Date lastAccessTime;
    private long offset;
    private long size;

    public Date getCachedTime() {
        return cachedTime;
    }

    public void setCachedTime(Date cachedTime) {
        this.cachedTime = cachedTime;
    }

    public long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
