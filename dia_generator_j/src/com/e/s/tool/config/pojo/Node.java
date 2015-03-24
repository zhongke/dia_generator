/*
 * $HeadURL: $ $Id: $ Copyright (c) 2012 by Ericsson, all rights reserved.
 */

package com.e.s.tool.config.pojo;

public class Node {
    /** Revision of the class */
    public static final String _REV_ID_ = "$Revision: $";

    private int index = 0;

    private boolean hasParent = true;

    private String parentName = "";

    private int level;

    private String nodeName;

    private boolean isDirectory;

    private boolean isLeaf;

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public boolean isHasParent() {
        return hasParent;
    }

    public void setHasParent(boolean hasParent) {
        this.hasParent = hasParent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
