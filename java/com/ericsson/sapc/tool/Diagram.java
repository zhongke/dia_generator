package com.ericsson.sapc.tool;

import java.util.List;

import com.ericsson.sapc.tool.ConstantType.EVENT_TYPE;
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
	public static void showMessageLine(Event event, List<String> nodeList) {
		StringBuffer lineMessage = new StringBuffer();
		StringBuffer lineArrow = new StringBuffer();
		lineMessage.append(Diagram.BEGIN);
		lineArrow.append(Diagram.BEGIN);
		
		int sequence = event.getNodeSeqence();
		// Get node position
		boolean needBlank = true;
		int position = nodeList.indexOf(event.getNodeName());

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
					assembleMessage(lineMessage, lineArrow, sequence, event);
					lineMessage.append(Diagram.MIDDLE);
					lineArrow.append(Diagram.MIDDLE);
				} else {
					assembleMessage(lineMessage, lineArrow, sequence, event);
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
		for (int i = 0; i < nodeList.size(); ++i) {
			String nodeName = nodeList.get(i).toString();
			lineHeader.append(nodeName);
			// fill more blank to the line until the Diagram.MIDDLE
			for ( int j = 0; j < HEADER.length() - nodeName.length(); ++j ) {
				lineHeader.append(" ");
			}
			if (i < nodeList.size() - 1) {
				lineHeader.append(Diagram.BLANK);
			}
		}
		System.out.println(lineHeader.toString());
	}
	
	private static void assembleMessage(StringBuffer lineMessage, StringBuffer lineArrow, int sequence, Event node) {
		String eventType = node.getEventType();
		StringBuffer msg = new StringBuffer();

		msg.append(" (");
		msg.append(sequence + 1);
		msg.append(") ");
		if (EVENT_TYPE.SX_CCR_EVENT.toString().equals(eventType)
				|| EVENT_TYPE.GX_CCR_EVENT.toString().equals(eventType)
				|| EVENT_TYPE.GXA_CCR_EVENT.toString().equals(eventType)
		) {
			if (node.getRequestType().equals(REQUEST_TYPE.INITIAL_REQUEST.toString())) {
				msg.append(MSG_TYPE.CCR_I);
			} else
			if (node.getRequestType().equals(REQUEST_TYPE.UPDATE_REQUEST.toString())) {
				msg.append(MSG_TYPE.CCR_U);
			} else
			if (node.getRequestType().equals(REQUEST_TYPE.TERMINATION_REQUEST.toString())) {
				msg.append(MSG_TYPE.CCR_U);
			}
			lineMessage.append(msg.toString());
			lineArrow.append(LEFT);
		} else
		if (EVENT_TYPE.SX_RAR_EVENT.toString().equals(eventType)
				|| EVENT_TYPE.GX_RAR_EVENT.toString().equals(eventType)
				|| EVENT_TYPE.GXA_RAR_EVENT.toString().equals(eventType)
		) {
			msg.append(MSG_TYPE.RAR);
			lineMessage.append(msg.toString());
			lineArrow.append(RIGHT);
		}
		
		
		// fill more blank to the line until the Diagram.MIDDLE
		for ( int i = 0; i < BLANK.length() - msg.length(); ++i ) {
			lineMessage.append(" ");
		}
	}
	
}
