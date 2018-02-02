/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.entity;

import java.nio.file.Path;
import java.util.Objects;

/**
 *
 * @author isaac
 */
public class FileAbstractFuse {

    private String name;
    private Path absolutePath;
    private FileAbstractAttributes attributes;

    public FileAbstractAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(FileAbstractAttributes attributes) {
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Path getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(Path absolutePath) {
        this.absolutePath = absolutePath;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.absolutePath);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileAbstractFuse other = (FileAbstractFuse) obj;
        if (!Objects.equals(this.absolutePath, other.absolutePath)) {
            return false;
        }
        return true;
    }
}
