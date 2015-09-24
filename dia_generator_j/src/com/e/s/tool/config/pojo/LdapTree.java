package com.e.s.tool.config.pojo;

import java.util.LinkedList;
import java.util.List;



public class LdapTree {


    private List<Node> nodes;

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public LdapTree() {
        nodes = new LinkedList<Node>();
    }

    public void setParent(List<Node> nodes) {

        for (int i = 0; i < nodes.size(); ++i) {

            Node child = nodes.get(i);
            String ChildDn = child.getDn();
            int position = ChildDn.indexOf(',');
            String parentDn = ChildDn.substring(position + 1, ChildDn.length());

            for (int j = 0; j < nodes.size(); ++j) {

                Node parent = nodes.get(j);
                if (!"".equals(parent.getDn()) && parent.getDn().split(":")[1].equals(parentDn)) {
                    child.setParent(parent);
                    break;

                }

            }

        }

    }



}
