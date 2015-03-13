package com.ericsson.sapc.tool;

import java.util.List;

import com.ericsson.sapc.tool.ConstantType.EVENT_TYPE;
import com.ericsson.sapc.tool.ConstantType.MSG_FLOW;
import com.ericsson.sapc.tool.ConstantType.MSG_TYPE;
import com.ericsson.sapc.tool.ConstantType.REQUEST_TYPE;


public class Diagram {
    public static String BEGIN = "*       ";
    public static String BLANK = "                  ";
    public static String HEADER = " ________ ";
    public static String MIDDLE = "|        |";
    public static String BOTTOM = "|________|";
    public static String EMPTY_RIGHT = " ---------------------------";
    public static String EMPTY_LEFT = "--------------------------- ";
    public static String LEFT = " <--------------- ";
    public static String RIGHT = " ---------------> ";

    enum COMMON {
        FIRST, MIDDLE, LAST
    }

    public static void showCommonLine(COMMON type, List<String> nodeList) {
        // Take the SAPC node into the second position
        StringBuffer lineMessage = new StringBuffer();
        lineMessage.append(Diagram.BEGIN);

        for (int i = 0; i < nodeList.size(); ++i) {
            if (Diagram.COMMON.FIRST == type) {
                lineMessage.append(Diagram.HEADER);
            } else if (Diagram.COMMON.MIDDLE == type) {
                lineMessage.append(Diagram.MIDDLE);
            } else if (Diagram.COMMON.LAST == type) {
                lineMessage.append(Diagram.BOTTOM);
            }
            if (i != nodeList.size() - 1) {
                lineMessage.append(Diagram.BLANK);
            }
        }
        System.out.println(lineMessage);
    }

    public static void showMessageLine(Event event, List<String> nodeList) {
        StringBuffer lineMessage = new StringBuffer();
        StringBuffer lineArrow = new StringBuffer();
        lineMessage.append(Diagram.BEGIN);
        lineArrow.append(Diagram.BEGIN);

        // Get node position
        boolean needBlank = true;
        int position = nodeList.indexOf(event.getNodeName());
        event.setNodePosition(position);

        for (int i = 0; i < nodeList.size(); ++i) {
            if (i != 2 || position != 2) {
                lineMessage.append(Diagram.MIDDLE);
                lineArrow.append(Diagram.MIDDLE);
                needBlank = true;
            }
            if (i == position) {
                // Check the message type
                if (i >= 2) {
                    lineMessage.delete(lineMessage.lastIndexOf(Diagram.BLANK), lineMessage.length());
                    lineArrow.delete(lineArrow.lastIndexOf(Diagram.BLANK), lineArrow.length());
                    lineArrow.delete(
                            lineArrow.indexOf(Diagram.BLANK) + Diagram.BLANK.length() + Diagram.MIDDLE.length(),
                            lineArrow.length());
                    assembleMessage(lineMessage, lineArrow, event);
                    lineMessage.append(Diagram.MIDDLE);
                    lineArrow.append(Diagram.MIDDLE);
                } else {
                    assembleMessage(lineMessage, lineArrow, event);
                    needBlank = false;
                }
            }

            if (i < (nodeList.size() - 1) && needBlank) {
                lineMessage.append(Diagram.BLANK);
                lineArrow.append(Diagram.BLANK);
            }
            needBlank = true;
        }
        System.out.println(lineMessage.toString());
        System.out.println(lineArrow.toString());
    }

    // * SGSN_MME SAPC GGSN
    public static void showHeaderLine(List<String> nodeList) {
        StringBuffer lineHeader = new StringBuffer();
        lineHeader.append(Diagram.BEGIN);
        String nodeName = null;
        for (int i = 0; i < nodeList.size(); ++i) {
            nodeName = nodeList.get(i).toString();
            // fill more blank to the line until the Diagram.MIDDLE
            int blankSpace = HEADER.length() - nodeName.length();
            int nextBlank = 0;
            if (blankSpace % 2 == 0) {
                nextBlank = blankSpace / 2;
            } else {
                nextBlank = blankSpace / 2 + 1;
            }
            for (int j = 0; j < blankSpace / 2; ++j) {
                lineHeader.append(" ");
            }
            lineHeader.append(nodeName);
            for (int j = 0; j < nextBlank; ++j) {
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
        msg.append(event.getEventSeqence());
        msg.append(") ");
        if (EVENT_TYPE.SX_CCR_EVENT.toString().equals(eventType)
                || EVENT_TYPE.GX_CCR_EVENT.toString().equals(eventType)
                || EVENT_TYPE.GXA_CCR_EVENT.toString().equals(eventType)) {
            String requestType = event.getRequestType();
            event.setSapcInitialized(false);
            if (REQUEST_TYPE.INITIAL_REQUEST.toString().equals(requestType)) {
                if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                    msg.append(MSG_TYPE.CCR_I);
                } else {
                    msg.append(MSG_TYPE.CCA_I);
                }
            } else if (REQUEST_TYPE.UPDATE_REQUEST.toString().equals(requestType)) {
                if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                    msg.append(MSG_TYPE.CCR_U);
                } else {
                    msg.append(MSG_TYPE.CCA_U);
                }
            } else if (REQUEST_TYPE.TERMINATION_REQUEST.toString().equals(requestType)) {
                if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                    msg.append(MSG_TYPE.CCR_T);
                } else {
                    msg.append(MSG_TYPE.CCA_T);
                }
            }
        } else if (EVENT_TYPE.SX_RAR_EVENT.toString().equals(eventType)
                || EVENT_TYPE.GX_RAR_EVENT.toString().equals(eventType)
                || EVENT_TYPE.GXA_RAR_EVENT.toString().equals(eventType)
                || EVENT_TYPE.RX_RAR_EVENT.toString().equals(eventType)) {
            event.setSapcInitialized(true);
            if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                msg.append(MSG_TYPE.RAR);
            } else {
                msg.append(MSG_TYPE.RAA);
            }
        } else if (EVENT_TYPE.SY_SLR_EVENT.toString().equals(eventType)
                || EVENT_TYPE.ESY_SLR_EVENT.toString().equals(eventType)) {
            event.setSapcInitialized(true);
            if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                msg.append(MSG_TYPE.SLR);
            } else {
                msg.append(MSG_TYPE.SLA);
            }
        } else if (EVENT_TYPE.SY_SNR_EVENT.toString().equals(eventType)
                || EVENT_TYPE.ESY_SNR_EVENT.toString().equals(eventType)) {
            event.setSapcInitialized(false);
            if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                msg.append(MSG_TYPE.SNR);
            } else {
                msg.append(MSG_TYPE.SNA);
            }
        } else if (EVENT_TYPE.SY_STR_EVENT.toString().equals(eventType)
                || EVENT_TYPE.ESY_STR_EVENT.toString().equals(eventType)
                || EVENT_TYPE.RX_STR_EVENT.toString().equals(eventType)) {
            event.setSapcInitialized(false);
            if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                msg.append(MSG_TYPE.STR);
            } else {
                msg.append(MSG_TYPE.STA);
            }
        } else if (EVENT_TYPE.RX_AAR_EVENT.toString().equals(eventType)) {
            event.setSapcInitialized(false);
            if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                msg.append(MSG_TYPE.AAR);
            } else {
                msg.append(MSG_TYPE.AAA);
            }
        } else if (EVENT_TYPE.RX_ASR_EVENT.toString().equals(eventType)) {
            event.setSapcInitialized(true);
            if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                msg.append(MSG_TYPE.ASR);
            } else {
                msg.append(MSG_TYPE.ASA);
            }
        }



        lineMessage.append(msg.toString());
        showFlow(lineArrow, event, flow);

        // fill more blank to the line until the Diagram.MIDDLE
        for (int i = 0; i < BLANK.length() - msg.length(); ++i) {
            lineMessage.append(" ");
        }
    }

    private static void showFlow(StringBuffer lineArrow, Event event, String flow) {
        int position = event.getNodePosition();
        if (position < 1) {
            if (!event.isSapcInitialized()) {
                if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                    lineArrow.append(RIGHT);
                } else {
                    lineArrow.append(LEFT);
                }
            } else {
                if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                    lineArrow.append(LEFT);
                } else {
                    lineArrow.append(RIGHT);
                }

            }
        } else if (position <= 2) {
            if (!event.isSapcInitialized()) {
                if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                    lineArrow.append(LEFT);
                } else {
                    lineArrow.append(RIGHT);
                }
            } else {
                if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                    lineArrow.append(RIGHT);
                } else {
                    lineArrow.append(LEFT);
                }

            }
        } else {
            if (!event.isSapcInitialized()) {
                if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                    lineArrow.append(LEFT.substring(0, LEFT.length() - 1));
                    lineArrow.append("-");
                    for (int i = 0; i < position - 2; ++i) {
                        lineArrow.append(Diagram.EMPTY_LEFT);
                    }
                } else {
                    for (int i = 0; i < position - 2; ++i) {
                        lineArrow.append(Diagram.EMPTY_RIGHT);
                    }
                    lineArrow.append("-");
                    lineArrow.append(RIGHT.substring(1, LEFT.length()));
                }
            } else {
                if (MSG_FLOW.REQUEST.toString().equals(flow)) {
                    for (int i = 0; i < position - 2; ++i) {
                        lineArrow.append(Diagram.EMPTY_RIGHT);
                    }
                    lineArrow.append("-");
                    lineArrow.append(RIGHT.substring(1, LEFT.length()));
                } else {
                    lineArrow.append(LEFT.substring(0, LEFT.length() - 1));
                    lineArrow.append("-");
                    for (int i = 0; i < position - 2; ++i) {
                        lineArrow.append(Diagram.EMPTY_LEFT);
                    }
                }

            }
        }

    }
}