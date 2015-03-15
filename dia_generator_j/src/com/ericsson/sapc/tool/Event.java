package com.ericsson.sapc.tool;

import com.ericsson.sapc.tool.ConstantType.EVENT_TYPE;
import com.ericsson.sapc.tool.ConstantType.EVENT_FLOW;
import com.ericsson.sapc.tool.ConstantType.REQUEST_TYPE;

public class Event {
    private int eventSeqence;
    private int nodePosition;

    private String nodeMsgType;
    private String nodeName;
    private String release;
    // private String requestType;`
    // private String eventFlow;

    private boolean isSapcInitialized;
    private boolean isSameFlow;
    private boolean isAnswer;

    // Define some enum for easy handling
    private EVENT_TYPE eventType;
    private REQUEST_TYPE requestType;
    private EVENT_FLOW eventFlow;



    public EVENT_FLOW getEventFlow() {
        return eventFlow;
    }


    public void setMessageFlow(EVENT_FLOW eventFlow) {
        this.eventFlow = eventFlow;
    }


    public EVENT_TYPE getEventType() {
        return eventType;
    }


    public void setEventType(String eventType) {
        this.eventType = EVENT_TYPE.valueOf(eventType);
    }



    public REQUEST_TYPE getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = REQUEST_TYPE.valueOf(requestType);
    }


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



    public boolean isSapcInitialized() {
        return isSapcInitialized;
    }

    public void setSapcInitialized(boolean isSapcInitialized) {
        this.isSapcInitialized = isSapcInitialized;
    }

}
