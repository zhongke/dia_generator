package com.e.s.tool;

import com.e.s.tool.ConstantType.EVENT_FLOW;
import com.e.s.tool.ConstantType.EVENT_TYPE;
import com.e.s.tool.ConstantType.NODE_TYPE;
import com.e.s.tool.ConstantType.REQUEST_TYPE;


public class MessageHandler {

    private String BLANK            = "*";
    private String SENT             = "*          -> ";
    private String RECEIVED         = "*          <- ";
    private String HEADER           = "*             -";
    private String START            = "*       ";
    private String FROM             = " message is sent from ";
    private String TO               = " to ";
    private String REQUEST_TYPE     = " Request-Type         : ";
    private String RESULT_CODE      = " Result-Code          : 2001 (SUCCESS)";

    public void showMessageLine(Event event) {


        // Use the counter to track the event are both Request and Answer or not
        int messageCount;

        if (EVENT_FLOW.BOTH != event.getEventFlow()) {
            messageCount = 1;
        } else {
            messageCount = 2;
        }

        // Use deep copy to set the eventFlow for every event
        Event currentEvent = (Event) event.clone();

        do {

            if (EVENT_FLOW.BOTH == event.getEventFlow()) {
                if (messageCount == 2) {
                    currentEvent.setEventFlow(EVENT_FLOW.REQUEST);
                } else {
                    currentEvent.setEventFlow(EVENT_FLOW.ANSWER);
                }
            }

            StringBuffer sequenceMessage = new StringBuffer();
            StringBuffer directionMessage = new StringBuffer();
            StringBuffer detailMessage = new StringBuffer();



            getSequenceMessage(currentEvent, sequenceMessage);
            getDirectionMessage(currentEvent, directionMessage);

            // prepare detail message
            detailMessage.append(HEADER);

            EVENT_TYPE eventType = event.getEventType();

            // Get eventType info
            switch (eventType) {
                case SX_CCR_EVENT:
                case GX_CCR_EVENT:
                case GXA_CCR_EVENT:

                    // Get requestType
                    REQUEST_TYPE requestType = event.getRequestType();

                    if (EVENT_FLOW.REQUEST == currentEvent.getEventFlow()) {
                        detailMessage.append(REQUEST_TYPE);
                        detailMessage.append(requestType);
                    }

                    switch (requestType) {
                        case INITIAL_REQUEST:

                            sequenceMessage.append("initialization:");

                            break;

                        case UPDATE_REQUEST:

                            sequenceMessage.append("update:");

                            break;

                        case TERMINATION_REQUEST:

                            sequenceMessage.append("termination:");

                            break;
                    }

                    break;

                case SX_RAR_EVENT:
                case GX_RAR_EVENT:
                case GXA_RAR_EVENT:

                    sequenceMessage.append("re-authorization:");

                    break;

                case RX_ASR_EVENT:
                case RX_RAR_EVENT:

                    sequenceMessage.append("bearer release notification:");

                    break;

                case RX_AAR_EVENT:

                    sequenceMessage.append("initialization:");

                    break;

                case RX_STR_EVENT:
                case SY_STR_EVENT:
                case SY_3GPP_STR_EVENT:

                    sequenceMessage.append("termination:");

                    break;

                case SY_SLR_EVENT:
                case SY_3GPP_SLR_EVENT:

                    sequenceMessage.append("initialization:");

                    break;

                case SY_SNR_EVENT:
                case SY_3GPP_SNR_EVENT:

                    sequenceMessage.append("Notification:");

                    break;

            }
            if (EVENT_FLOW.ANSWER == currentEvent.getEventFlow()) {
                detailMessage.append(RESULT_CODE);
            }

            if ((EVENT_FLOW.BOTH == event.getEventFlow() && (messageCount == 2))
                    || (EVENT_FLOW.BOTH != event.getEventFlow() && (messageCount == 1))) {
                System.out.println(sequenceMessage.toString());
            }

            System.out.println(directionMessage.toString());
            System.out.println(detailMessage.toString());
            System.out.println(BLANK);

            --messageCount;

        } while (messageCount > 0);
    }

    private void getSequenceMessage(Event event, StringBuffer sequenceMessage) {
        sequenceMessage.append(START);
        sequenceMessage.append(event.getEventSeqence());
        sequenceMessage.append(") ");


        // Get Protocol
        EVENT_TYPE eventType = event.getEventType();
        sequenceMessage.append(eventType.toString().split("_")[0]);

        sequenceMessage.append(" session ");
    }

    private void getDirectionMessage(Event event, StringBuffer directionMessage) {
        String eventTypeStr = "";
        String fromNode = "";
        String toNode = "";

        if (event.getNodeName().equals(NODE_TYPE.OCS3GPP.toString())
                || event.getNodeName().equals(NODE_TYPE.OCS3GPP2.toString())) {
            eventTypeStr = event.getEventType().toString().split("_")[2];
        } else {
            eventTypeStr = event.getEventType().toString().split("_")[1];
        }


        if ((EVENT_FLOW.REQUEST == event.getEventFlow() && (!event.isSapcInitialized()))
                || (EVENT_FLOW.ANSWER == event.getEventFlow() && (event.isSapcInitialized()))) {
            directionMessage.append(SENT);
            fromNode = event.getNodeName();
            toNode = NODE_TYPE.SAPC.toString();

        } else {
            directionMessage.append(RECEIVED);
            fromNode = NODE_TYPE.SAPC.toString();
            toNode = event.getNodeName();

        }

        if (EVENT_FLOW.REQUEST == event.getEventFlow()) {
            directionMessage.append(eventTypeStr);
        } else {
            directionMessage.append(eventTypeStr.substring(0, eventTypeStr.length() - 1)).append("A");

        }

        directionMessage.append(FROM);
        directionMessage.append(fromNode);

        directionMessage.append(TO);
        directionMessage.append(toNode);

    }

    /**
     * It could be a big class to handle specific message based on different events.
     * 
     * @param event
     * @param detailMessage
     */
    private void getDetailSpecificMessage(Event event, StringBuffer detailMessage) {

    }
}
