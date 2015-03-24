package com.e.s.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.e.s.tool.ConstantType.EVENT_FLOW;
import com.e.s.tool.ConstantType.EVENT_TYPE;

public class BufferMgr {
    private static String PATTERN_EVENT = "t_[3,a-z,/_]*_event";
    private static String PATTERN_FUNCTION = "f_runEvent(";
    private static String PATTERN_EVENT_FLOW = "eventFlow";

    // Initiate list to store all the events in the test case,
    // Include one direction (REQUEST/ANSWER) or Bi-direction (REQUEST and ANSWER)
    private List<Event> events = new LinkedList<Event>();

    // Get the all the distinguished nodes with correct order in the test case
    private List<String> nodeList = new LinkedList<String>();

    public void readInputFromFile(String fileName) {

        BufferedReader bufferedReader = null;

        try {

            bufferedReader = new BufferedReader(new FileReader(fileName));
            Matcher matcher_event = null;
            Pattern pattern_event = Pattern.compile(PATTERN_EVENT);

            String line = "";
            Event event = new Event();

            // Default value is 1 for the first message
            int sequenceNumber = 1;

            while (bufferedReader.ready()) {
                line = bufferedReader.readLine();

                if (!event.isSameFlow()) {
                    event = new Event();
                }

                if (null != line && !("".equals(line)) && !(line.trim().startsWith("\\/\\/"))
                        && !(line.contains("vl_diaCerEvent")) && !(line.contains("t_dia_cer_event_server"))) {

                    // Get event list from input by reqex
                    matcher_event = pattern_event.matcher(line);

                    if (matcher_event.find()) {

                        String str_event = matcher_event.group(0);
                        if ((null != str_event) && (!"".equals(str_event))) {
                            // Remove event prefix in order to covert to enum directly
                            event.setEventType(str_event.toUpperCase().substring(2, str_event.length()));

                            event.setSameFlow(true);
                            setCcrEventSpeicialInfo(line, event);
                            getSapcInitilazed(event);
                        }


                    } else if (line.contains(PATTERN_EVENT_FLOW)) {

                        // Get event flow if it has
                        event.setSameFlow(true);
                        setFlow(line, event);

                        if (event.getEventFlow() == EVENT_FLOW.ANSWER) {

                            // Set eventType and requestType from the previous event x - 2
                            Event linkedEvent = events.get(events.size() - 2);
                            event.setEventType(linkedEvent.getEventType().toString());
                            event.setRequestType(linkedEvent.getRequestType().toString());

                        }

                        // Get detailed information from input
                        // requestedData, expectedData, expectedResult
                    } else if (line.contains("requestData")) {

                        System.out.println(line);
                        getRequestData(event, line);

                    } else if (line.contains(PATTERN_FUNCTION)) {

                        // Get node list from input
                        sequenceNumber = getEventSquence(line, event, sequenceNumber);

                    }
                }
            }

            // Get node list from events
            for (Event e : events) {

                if (!nodeList.contains(e.getNodeName())) {
                    nodeList.add(e.getNodeName());
                }

            }

            // Add SAPC node into the second position
            nodeList.add(1, "SAPC");

        } catch (IOException e) {
            System.out.println("File does not exist");
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void getRequestData(Event event, String line) {
        RequestDataHandler requestDataHandler = new RequestDataHandler();
        requestDataHandler.getRequestData(event, line);

    }


    private int getEventSquence(String line, Event event, int sequenceNumber) {
        String nodeName = line.split("\\]")[1].split("\\[")[1];
        event.setNodeName(nodeName);

        if (null == event.getEventFlow()) {
            event.setEventFlow(EVENT_FLOW.BOTH);
        }

        event.setSameFlow(false);

        if (event.isAnswer()) {

            event.setAnswer(false);
            event.setEventSeqence(sequenceNumber - 2);

            --sequenceNumber;

        } else {
            event.setEventSeqence(sequenceNumber);
        }

        // Create a map to contain the node list and node message
        events.add(event);

        return ++sequenceNumber;
    }

    private void setFlow(String line, Event event) {

        String eventFlow = line.split("=")[1].split(";")[0].trim();

        if (EVENT_FLOW.REQUEST.toString().equals(eventFlow)) {

            event.setEventFlow(EVENT_FLOW.REQUEST);
            event.setAnswer(false);

        } else if (EVENT_FLOW.ANSWER.toString().equals(eventFlow)) {

            event.setEventFlow(EVENT_FLOW.ANSWER);
            event.setAnswer(true);

        }

    }

    private void setCcrEventSpeicialInfo(String line, Event event) {

        String release;
        String requestType;
        EVENT_TYPE eventType = event.getEventType();
        release = line.split(",")[2];
        event.setRelease(release);

        // Get the request type if the event is ccr event from Gx, Gxa, Sx
        if (EVENT_TYPE.GX_CCR_EVENT == eventType || EVENT_TYPE.GXA_CCR_EVENT == eventType
                || EVENT_TYPE.SX_CCR_EVENT == eventType) {

            requestType = line.split(",")[3].split("\\)")[0].trim();
            event.setRequestType(requestType);

        }

    }


    private static void getSapcInitilazed(Event event) {
        EVENT_TYPE eventType = event.getEventType();
        if (null != eventType) {

            switch (eventType) {

                case SX_CCR_EVENT:
                case GX_CCR_EVENT:
                case GXA_CCR_EVENT:
                case SY_3GPP_SNR_EVENT:
                case SY_SNR_EVENT:
                case SY_3GPP_STR_EVENT:
                case SY_STR_EVENT:
                case RX_STR_EVENT:
                case RX_AAR_EVENT:

                    event.setSapcInitialized(false);

                    break;

                case SX_RAR_EVENT:
                case GX_RAR_EVENT:
                case GXA_RAR_EVENT:
                case RX_RAR_EVENT:
                case SY_3GPP_SLR_EVENT:
                case SY_SLR_EVENT:
                case RX_ASR_EVENT:

                    event.setSapcInitialized(true);

                    break;
            }

        }
    }

    public void showDiagramFromBuffer() {

        DiagramHanlder.showHeaderLine(nodeList);
        DiagramHanlder.showCommonLine(DiagramHanlder.COMMON.FIRST, nodeList);
        DiagramHanlder.showCommonLine(DiagramHanlder.COMMON.MIDDLE, nodeList);

        Iterator<Event> iter = events.iterator();
        Event event = null;

        while (iter.hasNext()) {

            event = (Event) iter.next().clone();
            EVENT_FLOW eventFlow = event.getEventFlow();

            if (EVENT_FLOW.BOTH == eventFlow) {

                event.setEventFlow(EVENT_FLOW.REQUEST);
                DiagramHanlder.showMessageLine(event, nodeList);
                event.setEventFlow(EVENT_FLOW.ANSWER);
                DiagramHanlder.showMessageLine(event, nodeList);

            } else {

                DiagramHanlder.showMessageLine(event, nodeList);

            }

            DiagramHanlder.showCommonLine(DiagramHanlder.COMMON.MIDDLE, nodeList);

        }

        DiagramHanlder.showCommonLine(DiagramHanlder.COMMON.MIDDLE, nodeList);
        DiagramHanlder.showCommonLine(DiagramHanlder.COMMON.LAST, nodeList);

    }

    public void showMessageFromBuffer() {

        Iterator<Event> iter = events.iterator();
        Event event = null;

        while (iter.hasNext()) {

            event = (Event) iter.next();

            MessageHandler messageHandler = new MessageHandler();
            messageHandler.showMessageLine(event);

        }

    }

}
