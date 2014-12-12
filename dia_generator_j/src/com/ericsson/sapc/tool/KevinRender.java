package com.ericsson.sapc.tool;

import java.util.Iterator;

import com.ericsson.sapc.tool.ConstantType.MSG_FLOW;

public class KevinRender implements Render {
	
	public void showDiagramFromBuffer(Conversation conv) {

		Diagram.showHeaderLine(conv.nodeList);
		Diagram.showCommonLine(Diagram.COMMON.FIRST, conv.nodeList);
		Diagram.showCommonLine(Diagram.COMMON.MIDDLE, conv.nodeList);

		Iterator<Event> iter = conv.events.iterator();
		String eventType = null;
		String requestType = null;
		while (iter.hasNext()) {
			Event event = (Event) iter.next();
			String eventFlow = event.getEventFlow();
			if (null == eventFlow) {
				event.setEventFlow(MSG_FLOW.REQUEST.toString());
				Diagram.showMessageLine(event, conv.nodeList);
				Diagram.showCommonLine(Diagram.COMMON.MIDDLE, conv.nodeList);
				event.setEventFlow(MSG_FLOW.ANSWER.toString());
				Diagram.showMessageLine(event, conv.nodeList);
			} else {
				if (MSG_FLOW.REQUEST.toString().equals(eventFlow)) {
					event.setEventFlow(MSG_FLOW.REQUEST.toString());
					Diagram.showMessageLine(event, conv.nodeList);
					eventType = event.getEventType();
					requestType = event.getRequestType();
				} else {
					// Due to no event initialization for ANSWER event flow
					// So reuse the REQUEST info above
					event.setEventFlow(MSG_FLOW.ANSWER.toString());
					event.setEventType(eventType);
					event.setRequestType(requestType);
					Diagram.showMessageLine(event, conv.nodeList);
				}
				Diagram.showCommonLine(Diagram.COMMON.MIDDLE, conv.nodeList);
			}

		}
		Diagram.showCommonLine(Diagram.COMMON.MIDDLE, conv.nodeList);
		Diagram.showCommonLine(Diagram.COMMON.LAST, conv.nodeList);
	}
	
}
