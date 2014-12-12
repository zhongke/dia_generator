package com.ericsson.sapc.tool;

import com.ericsson.sapc.tool.BufferMgr;

public class Generator {
	
//	public static String FILENAME = "C:\\Kevin\\tc_05_01_02_SessionReauth_By_PRA_status_Update.ttcn";
	public static String FILENAME = "C:\\Kevin\\tc_02_15_01_BasicUseCase.ttcn";
//	public static String FILENAME = "/Users/Kevinzhong/Downloads/tc_05_01_02_SessionReauth_By_PRA_status_Update.ttcn";

	public static void main(String[] args) {
		BufferMgr buffMgr = new BufferMgr();
		buffMgr.readInputFromFile(FILENAME);
		
		buffMgr.showDiagramFromBuffer();

	}

}
