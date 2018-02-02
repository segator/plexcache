/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.entity.cache;

import com.github.segator.plexcache.entity.FileFuse;
import java.util.Map;

/**
 *
 * @author isaac
 */
public class FileFuseCache extends FileFuse {

    Map<Long, FileCachedBlock> cachedBlockMap;

    public Map<Long, FileCachedBlock> getCachedBlocks() {
        return cachedBlockMap;
    }
}
