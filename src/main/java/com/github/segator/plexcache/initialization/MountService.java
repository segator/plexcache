/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.initialization;

import com.github.segator.plexcache.fuse.MemoryFS;
import com.github.segator.plexcache.config.entity.PlexCacheConfig;
import com.github.segator.plexcache.dao.MetadataDAO;
import com.github.segator.plexcache.dao.impl.MemoryMetadataDAO;
import com.github.segator.plexcache.fuse.PlexCacheFuse;
import java.nio.file.Paths;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author isaac
 */
@Component
public class MountService {

    @Autowired
    private PlexCacheConfig configuration;

    @Autowired
    //private MemoryFS fileSystem;
    private PlexCacheFuse fileSystem;
    


    private boolean mounted = false;

    @PostConstruct
    public void startFuse() {
        System.out.println("mounting...");
        fileSystem.start();
        fileSystem.mount(Paths.get(configuration.getMountpoint()), false, false);
        mounted = true;

    }

    @PreDestroy()
    public void stopFuse() {
        if (mounted) {
            System.out.println("umounting...");
            fileSystem.umount();
            fileSystem.stop();
        }
    }

}
