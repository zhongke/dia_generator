package com.ericsson.sapc.tool;

import java.util.List;

import com.ericsson.sapc.tool.ConstantType.EVENT_FLOW;
import com.ericsson.sapc.tool.ConstantType.EVENT_TYPE;
import com.ericsson.sapc.tool.ConstantType.MSG_TYPE;
import com.ericsson.sapc.tool.ConstantType.REQUEST_TYPE;


public class DiagramHanlder {
    public static String BEGIN      = "*       ";
    public static String BLANK      = "                  ";
    public static String HEADER     = " ________ ";
    public static String MIDDLE     = "|        |";
    public static String BOTTOM     = "|________|";
    public static String EMPTY_RIGHT= " ---------------------------";
    public static String EMPTY_LEFT = "--------------------------- ";
    public static String LEFT       = " <--------------- ";
    public static String RIGHT      = " ---------------> ";

    enum COMMON {
        FIRST, MIDDLE, LAST
    }

    public static void showCommonLine(COMMON type, List<String> nodeList) {

        // Take the SAPC node into the second position
        StringBuffer lineMessage = new StringBuffer();
        lineMessage.append(DiagramHanlder.BEGIN);

        for (int i = 0; i < nodeList.size(); ++i) {

            if (DiagramHanlder.COMMON.FIRST == type) {
                lineMessage.append(DiagramHanlder.HEADER);
            } else if (DiagramHanlder.COMMON.MIDDLE == type) {
                lineMessage.append(DiagramHanlder.MIDDLE);
            } else if (DiagramHanlder.COMMON.LAST == type) {
                lineMessage.append(DiagramHanlder.BOTTOM);
            }

            if (i != nodeList.size() - 1) {
                lineMessage.append(DiagramHanlder.BLANK);
            }
        }

        System.out.println(lineMessage);
    }

    public static void showMessageLine(Event event, List<String> nodeList) {

        StringBuffer lineMessage = new StringBuffer();
        StringBuffer lineArrow = new StringBuffer();
        lineMessage.append(DiagramHanlder.BEGIN);
        lineArrow.append(DiagramHanlder.BEGIN);

        // Get node position
        boolean needBlank = true;
        int position = nodeList.indexOf(event.getNodeName());
        event.setNodePosition(position);

        for (int i = 0; i < nodeList.size(); ++i) {

            if (i != 2 || position != 2) {
                lineMessage.append(DiagramHanlder.MIDDLE);
                lineArrow.append(DiagramHanlder.MIDDLE);
                needBlank = true;
            }

            if (i == position) {
                // Check the message type
                if (i >= 2) {
                    lineMessage.delete(lineMessage.lastIndexOf(DiagramHanlder.BLANK), lineMessage.length());
                    lineArrow.delete(lineArrow.lastIndexOf(DiagramHanlder.BLANK), lineArrow.length());
                    lineArrow.delete(
                            lineArrow.indexOf(DiagramHanlder.BLANK) + DiagramHanlder.BLANK.length() + DiagramHanlder.MIDDLE.length(),
                            lineArrow.length());
                    assembleMessage(lineMessage, lineArrow, event);
                    lineMessage.append(DiagramHanlder.MIDDLE);
                    lineArrow.append(DiagramHanlder.MIDDLE);
                } else {
                    assembleMessage(lineMessage, lineArrow, event);
                    needBlank = false;
                }
            }

            if (i < (nodeList.size() - 1) && needBlank) {
                lineMessage.append(DiagramHanlder.BLANK);
                lineArrow.append(DiagramHanlder.BLANK);
            }
            needBlank = true;
        }

        System.out.println(lineMessage.toString());
        System.out.println(lineArrow.toString());
    }

    // * SGSN_MME SAPC GGSN
    public static void showHeaderLine(List<String> nodeList) {

        StringBuffer lineHeader = new StringBuffer();
        lineHeader.append(DiagramHanlder.BEGIN);
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
                lineHeader.append(DiagramHanlder.BLANK);
            }

        }

        System.out.println(lineHeader.toString());

    }

    private static void assembleMessage(StringBuffer lineMessage, StringBuffer lineArrow, Event event) {

        EVENT_TYPE eventType = event.getEventType();
        StringBuffer msg = new StringBuffer();
        EVENT_FLOW eventFlow = event.getEventFlow();

        msg.append("  (");
        msg.append(event.getEventSeqence());
        msg.append(") ");


        setEventTypeInfo(event, eventType, msg, eventFlow);

        lineMessage.append(msg.toString());
        showFlow(lineArrow, event, eventFlow);

        // fill more blank to the line until the Diagram.MIDDLE
        for (int i = 0; i < BLANK.length() - msg.length(); ++i) {
            lineMessage.append(" ");
        }
    }

    private static void setEventTypeInfo(Event event, EVENT_TYPE eventType, StringBuffer msg, EVENT_FLOW flow) {
        if (null != eventType) {

            switch (eventType) {

                case SX_CCR_EVENT:
                case GX_CCR_EVENT:
                case GXA_CCR_EVENT:

                    REQUEST_TYPE requestType = event.getRequestType();
                    event.setSapcInitialized(false);

                    if (REQUEST_TYPE.INITIAL_REQUEST == requestType) {
                        if (EVENT_FLOW.REQUEST == flow) {
                            msg.append(MSG_TYPE.CCR_I);
                        } else {
                            msg.append(MSG_TYPE.CCA_I);
                        }
                    } else if (REQUEST_TYPE.UPDATE_REQUEST == requestType) {
                        if (EVENT_FLOW.REQUEST == flow) {
                            msg.append(MSG_TYPE.CCR_U);
                        } else {
                            msg.append(MSG_TYPE.CCA_U);
                        }
                    } else if (REQUEST_TYPE.TERMINATION_REQUEST == requestType) {
                        if (EVENT_FLOW.REQUEST == flow) {
                            msg.append(MSG_TYPE.CCR_T);
                        } else {
                            msg.append(MSG_TYPE.CCA_T);
                        }
                    }

                    break;

                case SX_RAR_EVENT:
                case GX_RAR_EVENT:
                case GXA_RAR_EVENT:
                case RX_RAR_EVENT:

                    event.setSapcInitialized(true);

                    if (EVENT_FLOW.REQUEST == flow) {
                        msg.append(MSG_TYPE.RAR);
                    } else {
                        msg.append(MSG_TYPE.RAA);
                    }

                    break;

                case SY_3GPP_SLR_EVENT:
                case SY_SLR_EVENT:

                    event.setSapcInitialized(true);
                    if (EVENT_FLOW.REQUEST == flow) {
                        msg.append(MSG_TYPE.SLR);
                    } else {
                        msg.append(MSG_TYPE.SLA);
                    }

                    break;

                case SY_3GPP_SNR_EVENT:
                case SY_SNR_EVENT:

                    event.setSapcInitialized(false);
                    if (EVENT_FLOW.REQUEST == flow) {
                        msg.append(MSG_TYPE.SNR);
                    } else {
                        msg.append(MSG_TYPE.SNA);
                    }

                    break;


                case SY_3GPP_STR_EVENT:
                case SY_STR_EVENT:
                case RX_STR_EVENT:

                    event.setSapcInitialized(false);
                    if (EVENT_FLOW.REQUEST == flow) {
                        msg.append(MSG_TYPE.STR);
                    } else {
                        msg.append(MSG_TYPE.STA);
                    }

                    break;

                case RX_AAR_EVENT:

                    event.setSapcInitialized(false);
                    if (EVENT_FLOW.REQUEST == flow) {
                        msg.append(MSG_TYPE.AAR);
                    } else {
                        msg.append(MSG_TYPE.AAA);
                    }

                    break;
                    
                case RX_ASR_EVENT:
                    
                    event.setSapcInitialized(true);
                    if (EVENT_FLOW.REQUEST == flow) {
                        msg.append(MSG_TYPE.ASR);
                    } else {
                        msg.append(MSG_TYPE.ASA);
                    }
                    
                    break;
            }

        }
    }

    private static void showFlow(StringBuffer lineArrow, Event event, EVENT_FLOW flow) {
        int position = event.getNodePosition();

        if (position < 1) {
            if (!event.isSapcInitialized()) {
                if (EVENT_FLOW.REQUEST == flow) {
                    lineArrow.append(RIGHT);
                } else {
                    lineArrow.append(LEFT);
                }
            } else {
                if (EVENT_FLOW.REQUEST == flow) {
                    lineArrow.append(LEFT);
                } else {
                    lineArrow.append(RIGHT);
                }
            }

        } else if (position <= 2) {

            if (!event.isSapcInitialized()) {
                if (EVENT_FLOW.REQUEST == flow) {
                    lineArrow.append(LEFT);
                } else {
                    lineArrow.append(RIGHT);
                }
            } else {
                if (EVENT_FLOW.REQUEST == flow) {
                    lineArrow.append(RIGHT);
                } else {
                    lineArrow.append(LEFT);
                }

            }

        } else {

            if (!event.isSapcInitialized()) {
                if (EVENT_FLOW.REQUEST == flow) {
                    lineArrow.append(LEFT.substring(0, LEFT.length() - 1));
                    lineArrow.append("-");
                    for (int i = 0; i < position - 2; ++i) {
                        lineArrow.append(DiagramHanlder.EMPTY_LEFT);
                    }
                } else {
                    for (int i = 0; i < position - 2; ++i) {
                        lineArrow.append(DiagramHanlder.EMPTY_RIGHT);
                    }
                    lineArrow.append("-");
                    lineArrow.append(RIGHT.substring(1, LEFT.length()));
                }
            } else {
                if (EVENT_FLOW.REQUEST == flow) {
                    for (int i = 0; i < position - 2; ++i) {
                        lineArrow.append(DiagramHanlder.EMPTY_RIGHT);
                    }
                    lineArrow.append("-");
                    lineArrow.append(RIGHT.substring(1, LEFT.length()));
                } else {
                    lineArrow.append(LEFT.substring(0, LEFT.length() - 1));
                    lineArrow.append("-");
                    for (int i = 0; i < position - 2; ++i) {
                        lineArrow.append(DiagramHanlder.EMPTY_LEFT);
                    }
                }

            }
        }

    }
}
