/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.dao.impl;

import com.github.segator.plexcache.constants.MetadataErrorCodeEnum;
import com.github.segator.plexcache.dao.MetadataDAO;
import com.github.segator.plexcache.entity.DirectoryFuse;
import com.github.segator.plexcache.entity.FileAbstractAttributes;
import com.github.segator.plexcache.entity.FileAbstractFuse;
import com.github.segator.plexcache.entity.FileSystemStats;
import com.github.segator.plexcache.exception.FileMetadataException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author isaac
 */
@Component
public class MemoryMetadataDAO implements MetadataDAO {

    @Autowired
    private LocalMetadataDAO localMetadataDAO;

    Map<String, Set<FileAbstractFuse>> metadata;
    Map<String, Set<FileAbstractFuse>> metadataCalculated;

    FileSystemStats fileSystemStats;

    @Override
    public FileAbstractAttributes getAttributes(FileAbstractFuse fileAbstract) throws FileMetadataException {
        synchronized (this) {
            return getFileAbstractFuseByName(getMetadataFileList(fileAbstract.getAbsolutePath().getParent().toString()), fileAbstract.getName()).getAttributes();
        }
    }

    @Override
    public FileSystemStats getStats() throws FileMetadataException {
        synchronized (this) {
            return fileSystemStats;
        }
    }

    @Override
    public FileAbstractFuse searchFileByPath(String path) throws FileMetadataException {
        synchronized (this) {
            Set<FileAbstractFuse> filesOnDirectory = null;
            Path pathPath = Paths.get(path);
            filesOnDirectory = metadata.get(pathPath.getParent() != null ? pathPath.getParent().toString() : "");
            if (filesOnDirectory == null) {
                throw new FileMetadataException(MetadataErrorCodeEnum.FILE_NOT_EXIST);
            }
            return getFileAbstractFuseByName(filesOnDirectory, pathPath.getFileName() != null ? pathPath.getFileName().toString() : File.separator);
        }
    }

    @Override
    public void startService() throws Exception {
        synchronized (this) {
            reloadMetadata();
        }
    }

    public void reloadMetadata() throws FileMetadataException {
        System.out.println("Loading On Memory all backend structure:START");
        metadataCalculated = new HashMap();
        cacheDirectoryBackendOnMemory(File.separator);
        fileSystemStats = localMetadataDAO.getStats();
        metadata = metadataCalculated;
        metadataCalculated = new HashMap();
        System.out.println("Loading On Memory all backend structures:FINISH");
    }

    @Override
    public void stopService() throws Exception {
        
    }

    private FileAbstractFuse getFileAbstractFuseByName(Set<FileAbstractFuse> files, String name) throws FileMetadataException {
        try {
            return files.stream().filter(p -> p.getName().equals(name)).findFirst().get();
        } catch (NoSuchElementException nsee) {
            throw new FileMetadataException(MetadataErrorCodeEnum.FILE_NOT_EXIST);
        }
    }

    private void cacheDirectoryBackendOnMemory(String directory) throws FileMetadataException {
        DirectoryFuse rootDirectory = (DirectoryFuse) localMetadataDAO.searchFileByPath(directory);
        Path directoryParentPath = rootDirectory.getAbsolutePath().getParent();
        getMetadataFileList(directoryParentPath != null ? directoryParentPath.toString() : "").add(rootDirectory);
        Set<FileAbstractFuse> files = getMetadataFileList(directory);
        for (FileAbstractFuse backendAbstractFile : rootDirectory.getChilds()) {
            if (backendAbstractFile.getClass().equals(DirectoryFuse.class)) {
                cacheDirectoryBackendOnMemory(backendAbstractFile.getAbsolutePath().toString());
            } else {
                files.add(backendAbstractFile);
            }
        }

    }

    private Set<FileAbstractFuse> getMetadataFileList(String directory) {
        Set<FileAbstractFuse> files = metadataCalculated.get(directory);
        if (files == null) {
            files = new HashSet();
            metadataCalculated.put(directory, files);
        }
        return files;
    }

    @Override
    public void setAttributes(FileAbstractFuse fileAbstract) throws FileMetadataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FileAbstractAttributes rename(FileAbstractFuse fileAbstract, String newName) throws FileMetadataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DirectoryFuse mkdir(DirectoryFuse parentDirectory, String name) throws FileMetadataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void rmdir(DirectoryFuse fileAbstract) throws FileMetadataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
