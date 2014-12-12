package com.ericsson.sapc.tool;

public class Event {
	private String nodeName;
	private int nodeSeqence;
	private String nodeMsgType;
	private String eventType;
	private int nodePosition;
	private String release;
	private String requestType;
	private String eventFlow;
	private boolean isSapcInitialized;


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
