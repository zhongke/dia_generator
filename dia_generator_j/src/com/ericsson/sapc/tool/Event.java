package com.ericsson.sapc.tool;

import com.ericsson.sapc.tool.ConstantType.EVENT_TYPE;

public class Event {
    private int eventSeqence;
    private int nodePosition;

    private String nodeMsgType;
    private String nodeName;
    private String eventType;
    private String release;
    private String requestType;
    private String eventFlow;

    private boolean isSapcInitialized;
    private boolean isSameFlow;
    private boolean isAnswer;

    private EVENT_TYPE eventEnumType;

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean isAnswer) {
        this.isAnswer = isAnswer;
    }

    public boolean isSameFlow() {
        return isSameFlow;
    }

    public void setSameFlow(boolean isSameFlow) {
        this.isSameFlow = isSameFlow;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getEventSeqence() {
        return eventSeqence;
    }

    public void setEventSeqence(int eventSeqence) {
        this.eventSeqence = eventSeqence;
    }



    public String getNodeMsgType() {
        return nodeMsgType;
    }

    public void setNodeMsgType(String nodeMsgType) {
        this.nodeMsgType = nodeMsgType;
    }

    /*
     * public String getEventType() { return eventType; }
     * 
     * public void setEventType(String eventType) { this.eventType = eventType; }
     */

    public EVENT_TYPE getEventTypeToEnum() {
        return eventEnumType;
    }


    public void setEventTypeToEnum(String eventType) {
        this.eventEnumType = EVENT_TYPE.valueOf(eventType);
    }

    public int getNodePosition() {
        return nodePosition;
    }

    public void setNodePosition(int nodePosition) {
        this.nodePosition = nodePosition;
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

    public String getEventFlow() {
        return eventFlow;
    }

    public void setEventFlow(String eventFlow) {
        this.eventFlow = eventFlow;
    }


    public boolean isSapcInitialized() {
        return isSapcInitialized;
    }

    public void setSapcInitialized(boolean isSapcInitialized) {
        this.isSapcInitialized = isSapcInitialized;
    }

}
