package com.ericsson.sapc.tool;

public class Node {
    private String nodeName;
    private int nodeSeqence;
    private String nodeMsgType;
    private String eventType;
    private String nodeMsgDirection;
    private String release;
    private String requestType;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getNodeSeqence() {
        return nodeSeqence;
    }

    public void setNodeSeqence(int nodeSeqence) {
        this.nodeSeqence = nodeSeqence;
    }

    public String getNodeMsgType() {
        return nodeMsgType;
    }

    public void setNodeMsgType(String nodeMsgType) {
        this.nodeMsgType = nodeMsgType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getNodeMsgDirection() {
        return nodeMsgDirection;
    }

    public void setNodeMsgDirection(String nodeMsgDirection) {
        this.nodeMsgDirection = nodeMsgDirection;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

}
