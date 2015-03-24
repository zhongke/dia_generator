/*
 * $HeadURL: $ $Id: $ Copyright (c) 2012 by Ericsson, all rights reserved.
 */

package com.e.s.tool.config.pojo;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public Node() {
        attributes = new ArrayList<String>();
    }

    /** Revision of the class */
    public static final String _REV_ID_ = "$Revision: $";

    private String nodeName;

    private Node parent;

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    private List<String> attributes;

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }


}
