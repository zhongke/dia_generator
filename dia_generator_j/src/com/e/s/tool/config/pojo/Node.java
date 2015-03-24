
package com.e.s.tool.config.pojo;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private String nodeDn;
    private Node parent;

    public Node() {
        attributes = new ArrayList<String>();
    }

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

    public String getDn() {
        return nodeDn;
    }

    public void setNodeDn(String nodeDn) {
        this.nodeDn = nodeDn;
    }


}
