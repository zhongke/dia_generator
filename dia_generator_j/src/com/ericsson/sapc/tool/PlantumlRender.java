package com.ericsson.sapc.tool;

public class PlantumlRender implements Render {

	public void showDiagramFromBuffer(Conversation conv) {
		for (Event e : conv.events) {
			if (e.getEventFlow() == null)
			{
				System.out.println(e.getNodeName() + " <- " + "SAPC:"
						+ e.getEventType());
				System.out.println(e.getNodeName() + " -> " + "SAPC:"
						+ e.getEventType());
			}
			else if (e.getEventFlow().equals("REQUEST")) {
				System.out.println(e.getNodeName() + " -> " + "SAPC:"
						+ e.getEventType());
			} else if (e.getEventFlow().equals("ANSWER")) {
				System.out.println(e.getNodeName() + " <- " + "SAPC:"
						+ e.getEventType());
			}
		}
	}
	
}
