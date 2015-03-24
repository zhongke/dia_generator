package com.e.s.tool.config.pojo;

import java.util.HashMap;
import java.util.Map;

public class LeafNode extends Node {

    private Map<String, String> attributes;

    public LeafNode(Map<String, String> attributes) {
        super();
        this.attributes = new HashMap<String, String>();
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

}
