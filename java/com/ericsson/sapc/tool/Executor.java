package com.ericsson.sapc.tool;

public class Executor {
	
	public static String FILENAME = "";

	public static void main(String[] args) {
		BufferMgr buffMgr = new BufferMgr();
		buffMgr.readInputFromFile(FILENAME);
		
		buffMgr.showDiagramFromBuffer();

	}

}
