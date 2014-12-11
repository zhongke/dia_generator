package com.ericsson.sapc.tool;

import java.util.List;

import com.ericsson.sapc.tool.ConstantType.EVENT_TYPE;
import com.ericsson.sapc.tool.ConstantType.MSG_FLOW;
import com.ericsson.sapc.tool.ConstantType.MSG_TYPE;
import com.ericsson.sapc.tool.ConstantType.REQUEST_TYPE;


//*         SGSN_MME                     SAPC                        GGSN
//*        ________                    ________                    ________
//*       |        |                  |        |                  |        |
//*       |        |                  |        |                  |        |
//*       |        |                  |        |                  |        |
//*       |        | (1) CCR-I        |        |                  |        |
//*       |        | ---------------> |        |                  |        |	
//*       |        |                  |        |                  |        |
//*       |________|                  |________|                  |________|

public class Diagram {
	public static String BEGIN	= "*       ";
	public static String BLANK	= "                  ";
	public static String HEADER	= " ________ ";
	public static String MIDDLE = "|        |";
	public static String BOTTOM	= "|________|";
	public static String LEFT	= " <--------------- ";
	public static String RIGHT	= " ---------------> ";
	
	enum COMMON {
		FIRST,
		MIDDLE,
		LAST
	}

	
	//*        ________                    ________                    ________
	//*       |        |                  |        |                  |        |
	//*       |________|                  |________|                  |________|
	public static void showCommonLine(COMMON type, List<String> nodeList) {
		// Take the SAPC node into the second position
		StringBuffer lineMessage = new StringBuffer();
		lineMessage.append(Diagram.BEGIN);

		for (int i = 0; i < nodeList.size(); ++i) {
			if (Diagram.COMMON.FIRST == type) {
				lineMessage.append(Diagram.HEADER);
			} else
			if (Diagram.COMMON.MIDDLE == type) {
				lineMessage.append(Diagram.MIDDLE);
			} else
			if (Diagram.COMMON.LAST == type) {
				lineMessage.append(Diagram.BOTTOM);
			}
			if (i != nodeList.size() - 1) {
				lineMessage.append(Diagram.BLANK);
			}
		}
		System.out.println(lineMessage);
	}
	
	//*       |        | (1) CCR-I        |        |                  |        |
	//*       |        | ---------------> |        |                  |        |
	public static void showMessageLine(Event event, List<String> nodeList, MSG_FLOW flow) {
		StringBuffer lineMessage = new StringBuffer();
		StringBuffer lineArrow = new StringBuffer();
		lineMessage.append(Diagram.BEGIN);
		lineArrow.append(Diagram.BEGIN);
		
		// Get node position
		boolean needBlank = true;
		int position = nodeList.indexOf(event.getNodeName());
		event.setNodePosition(position);

		for (int i = 0; i < nodeList.size(); ++i) {
			if ( i != 2 || position != 2) {
				lineMessage.append(Diagram.MIDDLE);
				lineArrow.append(Diagram.MIDDLE);
				needBlank = true;
			}
			if (i == position) {
				// Check the message type
				if (i == 2) {
					lineMessage.delete(lineMessage.lastIndexOf(Diagram.BLANK), lineMessage.length());
					lineArrow.delete(lineArrow.lastIndexOf(Diagram.BLANK), lineArrow.length());
					assembleMessage(lineMessage, lineArrow, event);
					lineMessage.append(Diagram.MIDDLE);
					lineArrow.append(Diagram.MIDDLE);
				} else {
					assembleMessage(lineMessage, lineArrow, event);
					needBlank = false;
				}
			}
			
			if (i < nodeList.size() - 1 && needBlank) {
				lineMessage.append(Diagram.BLANK);
				lineArrow.append(Diagram.BLANK);
			}
			needBlank = true;
		}
		System.out.println(lineMessage.toString());
		System.out.println(lineArrow.toString());
	}
	
//	*         SGSN_MME                     SAPC                        GGSN
	public static void showHeaderLine(List<String> nodeList) {
		StringBuffer lineHeader = new StringBuffer();
		lineHeader.append(Diagram.BEGIN);
		String nodeName = null;
		for (int i = 0; i < nodeList.size(); ++i) {
			nodeName = nodeList.get(i).toString();
			// fill more blank to the line until the Diagram.MIDDLE
			for ( int j = 0; j < (HEADER.length() - nodeName.length()) / 2; ++j ) {
				lineHeader.append(" ");
			}
			lineHeader.append(nodeName);
			for ( int j = 0; j < (HEADER.length() - nodeName.length()) / 2; ++j ) {
				lineHeader.append(" ");
			}
			if (i < nodeList.size() - 1) {
				lineHeader.append(Diagram.BLANK);
			}
		}
		System.out.println(lineHeader.toString());
	}
	
	private static void assembleMessage(StringBuffer lineMessage, StringBuffer lineArrow, Event event) {
		String eventType = event.getEventType();
		StringBuffer msg = new StringBuffer();
		String flow = event.getEventFlow();

		msg.append("  (");
		msg.append(event.getNodeSeqence());
		msg.append(") ");
		if (EVENT_TYPE.SX_CCR_EVENT.toString().equals(eventType)
				|| EVENT_TYPE.GX_CCR_EVENT.toString().equals(eventType)
				|| EVENT_TYPE.GXA_CCR_EVENT.toString().equals(eventType)
		) {
			if (event.getRequestType().equals(REQUEST_TYPE.INITIAL_REQUEST.toString())) {
				if (null != flow && MSG_FLOW.REQUEST.toString().equals(flow)) {
					msg.append(MSG_TYPE.CCR_I);
				} else {
					msg.append(MSG_TYPE.CCA_I);
				}
			} else
			if (event.getRequestType().equals(REQUEST_TYPE.UPDATE_REQUEST.toString())) {
				if (null != flow && MSG_FLOW.REQUEST.toString().equals(flow)) {
					msg.append(MSG_TYPE.CCR_U);
				} else {
					msg.append(MSG_TYPE.CCA_U);
				}
			} else
			if (event.getRequestType().equals(REQUEST_TYPE.TERMINATION_REQUEST.toString())) {
				if (null != flow && MSG_FLOW.REQUEST.toString().equals(flow)) {
					msg.append(MSG_TYPE.CCR_T);
				} else {
					msg.append(MSG_TYPE.CCA_T);
				}
			}
			lineMessage.append(msg.toString());
			if (event.getNodePosition() < 1){
				if (null != flow && MSG_FLOW.REQUEST.toString().equals(flow)) {
					lineArrow.append(RIGHT);
				} else {
					lineArrow.append(LEFT);
				}
			} else {
				if (null != flow && MSG_FLOW.REQUEST.toString().equals(flow)) {
					lineArrow.append(LEFT);
				} else {
					lineArrow.append(RIGHT);
				}
				
			}
		} else
		if (EVENT_TYPE.SX_RAR_EVENT.toString().equals(eventType)
		 || EVENT_TYPE.GX_RAR_EVENT.toString().equals(eventType)
		 || EVENT_TYPE.GXA_RAR_EVENT.toString().equals(eventType)
		) {
			msg.append(MSG_TYPE.RAR);
			lineMessage.append(msg.toString());
			if (event.getNodePosition() < 1){
				if (null != flow && MSG_FLOW.REQUEST.toString().equals(flow)) {
					lineArrow.append(LEFT);
				} else {
					lineArrow.append(RIGHT);
				}
			} else {
				
				if (null != flow && MSG_FLOW.REQUEST.toString().equals(flow)) {
					lineArrow.append(RIGHT);
				} else {
					lineArrow.append(LEFT);
				}
				
			}
		}
		
		// fill more blank to the line until the Diagram.MIDDLE
		for ( int i = 0; i < BLANK.length() - msg.length(); ++i ) {
			lineMessage.append(" ");
		}
	}
	
}
