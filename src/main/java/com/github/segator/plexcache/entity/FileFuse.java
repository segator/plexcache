/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.entity;

/**
 *
 * @author isaac
 */
public class FileFuse extends FileAbstractFuse {

    private FileFuse hardLink;

    public FileFuse getHardLink() {
        return hardLink;
    }

    public void setHardLink(FileFuse hardLink) {
        this.hardLink = hardLink;
    }

}
