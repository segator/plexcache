/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.segator.plexcache.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author isaac
 */
public class DirectoryFuse extends FileFuse {
    private final List<FileAbstractFuse> childs=new ArrayList();

    public List<FileAbstractFuse> getChilds() {
        return childs;
    }
}
