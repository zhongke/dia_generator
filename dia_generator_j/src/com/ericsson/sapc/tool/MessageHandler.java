package com.ericsson.sapc.tool;

import com.ericsson.sapc.tool.ConstantType.EVENT_FLOW;
import com.ericsson.sapc.tool.ConstantType.EVENT_TYPE;
import com.ericsson.sapc.tool.ConstantType.NODE_TYPE;
import com.ericsson.sapc.tool.ConstantType.REQUEST_TYPE;


public class MessageHandler {

    private String BLANK    = "*";
    private String IN       = "*          -> ";
    private String OUT      = "*          <- ";
    private String HEADER   = "*             -";
    private String START    = "*       ";
    private String FROM     = " message is sent from ";
    private String TO       = " to ";
    private String REQUEST_TYPE = " Request-Type         : ";
    private String RESULT_CODE  = " Result-Code          : 2001 (SUCCESS)";

    public void showMessageLine(Event event) {


        // Use the counter to track the event are both Request and Answer or not
        int messageCount = 2;
        
        if (EVENT_FLOW.BOTH != event.getEventFlow()) {
            messageCount = 1;
        } else {
            messageCount = 2;
        }
        
        Event currentEvent = (Event)event.clone();
        
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
            // prepare sequence message


            EVENT_TYPE eventType = event.getEventType();

            getSequenceMessage(currentEvent, sequenceMessage, detailMessage);


            getDirectionMessage(currentEvent, directionMessage);

            // prepare detail message
            detailMessage.append(HEADER);

            // Get eventType info
            switch (eventType) {
                case SX_CCR_EVENT:
                case GX_CCR_EVENT:
                case GXA_CCR_EVENT:

                    // Get requestType
                    REQUEST_TYPE requestType = event.getRequestType();

                    detailMessage.append(REQUEST_TYPE);
                    detailMessage.append(requestType);
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
                    // getDirectionMessage(event, directionMessage);

                    break;

                case RX_ASR_EVENT:
                case RX_RAR_EVENT:

                    sequenceMessage.append("bearer release notification:");
                    // getDirectionMessage(event, directionMessage);

                    break;

                case RX_AAR_EVENT:

                    sequenceMessage.append("initialization:");
                    // getDirectionMessage(event, directionMessage);

                    break;

                case RX_STR_EVENT:
                case SY_STR_EVENT:
                case SY_3GPP_STR_EVENT:

                    sequenceMessage.append("termination:");
                    // getDirectionMessage(event, directionMessage);

                    break;

                case SY_SLR_EVENT:
                case SY_3GPP_SLR_EVENT:

                    sequenceMessage.append("initialization:");
                    // getDirectionMessage(event, directionMessage);

                    break;

                case SY_SNR_EVENT:
                case SY_3GPP_SNR_EVENT:

                    sequenceMessage.append("Notification:");
                    // getDirectionMessage(event, directionMessage);

                    break;

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

    private void getSequenceMessage(Event event, StringBuffer sequenceMessage, StringBuffer detailMessage) {
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

        if (event.getNodeName().equals(NODE_TYPE.OCS3GPP) || event.getNodeName().equals(NODE_TYPE.OCS3GPP2)) {
            eventTypeStr = event.getEventType().toString().split("_")[2];
        } else {
            eventTypeStr = event.getEventType().toString().split("_")[1];
        }


        if (EVENT_FLOW.REQUEST == event.getEventFlow()) {
            directionMessage.append(IN);
            directionMessage.append(eventTypeStr);
        } else {
            directionMessage.append(OUT);
            directionMessage.append(eventTypeStr.substring(0, eventTypeStr.length() - 1)).append("A");

        }

        directionMessage.append(FROM);

        if (EVENT_FLOW.REQUEST == event.getEventFlow()) {
            directionMessage.append(event.getNodeName());
        } else {

            directionMessage.append(NODE_TYPE.SAPC);
        }

        directionMessage.append(TO);

        if (EVENT_FLOW.REQUEST == event.getEventFlow()) {
            directionMessage.append(NODE_TYPE.SAPC);
        } else {

            directionMessage.append(event.getNodeName());
        }
    }

}
