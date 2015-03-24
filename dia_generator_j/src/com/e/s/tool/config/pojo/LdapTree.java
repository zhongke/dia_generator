package com.e.s.tool.config.pojo;

import java.util.LinkedList;
import java.util.List;



public class LdapTree {

    /**
     * 
     */
    private static final long serialVersionUID = 852670006354184487L;

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
            int position = child.getNodeName().indexOf(',');
            String parentDn = child.getNodeName().substring(position + 1, child.getNodeName().length());
            for (int j = 0; j < nodes.size(); ++j) {

                Node parent = nodes.get(j);
                if (parent.getNodeName().split(":")[1].equals(parentDn)) {
                    child.setParent(parent);
                    System.out.println("child   : " + child.getNodeName());
                    System.out.println("parent  : " + parent.getNodeName());
                    System.out.println();
                    break;

                }

            }

        }

    }



}
