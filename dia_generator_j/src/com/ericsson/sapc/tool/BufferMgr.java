package com.ericsson.sapc.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ericsson.sapc.tool.ConstantType.EVENT_FLOW;
import com.ericsson.sapc.tool.ConstantType.EVENT_TYPE;
import com.ericsson.sapc.tool.ConstantType.REQUEST_TYPE;

public class BufferMgr {
    private static String PATTERN_EVENT = "t_[3,a-z,/_]*_event";
    private static String PATTERN_FUNCTION = "f_runEvent";
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

                if (null != line && !("".equals(line)) && !(line.contains("vl_diaCerEvent"))) {

                    // Get event list from input by reqex
                    matcher_event = pattern_event.matcher(line);

                    if (matcher_event.find()) {

                        String str_event = matcher_event.group(0);
                        // Remove event prefix in order to covert to enum directly
                        event.setEventType(str_event.toUpperCase().substring(2, str_event.length()));

                        event.setSameFlow(true);
                        setCcrEventSpeicialInfo(line, event);

                    } else if (line.contains(PATTERN_EVENT_FLOW)) {

                        // System.out.println(line);
                        // Get event flow if it has
                        event.setSameFlow(true);
                        setEventFlow(line, event);

                    } else if (line.contains(PATTERN_FUNCTION)) {

                        // Get node list from input
                        // System.out.println(line);
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
            for (int i = 0; i < nodeList.size(); ++i) {
                // System.out.println(nodeList.get(i));
            }

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

    private int getEventSquence(String line, Event event, int sequenceNumber) {
        String nodeName = line.split("\\]")[1].split("\\[")[1];
        event.setNodeName(nodeName);

        // Create a map to contain the node list and node message
        events.add(event);
        event.setSameFlow(false);

        if (event.isAnswer()) {

            event.setAnswer(false);
            event.setEventSeqence(sequenceNumber - 2);

            --sequenceNumber;

        } else {
            event.setEventSeqence(sequenceNumber);
        }

        return ++sequenceNumber;
    }

    private void setEventFlow(String line, Event event) {

        String eventFlow = line.split("=")[1].split(";")[0].trim();

        if (EVENT_FLOW.REQUEST.toString().equals(eventFlow)) {

            event.setMessageFlow(EVENT_FLOW.REQUEST);
            event.setAnswer(false);

        } else if (EVENT_FLOW.ANSWER.toString().equals(eventFlow)) {

            event.setMessageFlow(EVENT_FLOW.ANSWER);
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

    public void showDiagramFromBuffer() {

        Diagram.showHeaderLine(nodeList);
        Diagram.showCommonLine(Diagram.COMMON.FIRST, nodeList);
        Diagram.showCommonLine(Diagram.COMMON.MIDDLE, nodeList);

        Iterator<Event> iter = events.iterator();
        EVENT_TYPE eventType = null;
        REQUEST_TYPE requestType = null;

        while (iter.hasNext()) {

            Event event = (Event) iter.next();
            EVENT_FLOW eventFlow = event.getEventFlow();

            if (null == eventFlow) {

                event.setMessageFlow(EVENT_FLOW.REQUEST);
                Diagram.showMessageLine(event, nodeList);
                event.setMessageFlow(EVENT_FLOW.ANSWER);
                Diagram.showMessageLine(event, nodeList);

            } else {

                if (EVENT_FLOW.REQUEST == eventFlow) {
                    event.setMessageFlow(EVENT_FLOW.REQUEST);
                    eventType = event.getEventType();
                    requestType = event.getRequestType();

                } else {

                    // Due to no event initialization for ANSWER event flow
                    // So reuse the REQUEST info above
                    // TODO: How to get the info from the readInputFromFile
                    event.setMessageFlow(EVENT_FLOW.ANSWER);
                    if (null != eventType)
                        event.setEventType(eventType.toString());
                    if (null != requestType)
                        event.setRequestType(requestType.toString());

                }

                Diagram.showMessageLine(event, nodeList);

            }

            Diagram.showCommonLine(Diagram.COMMON.MIDDLE, nodeList);

        }

        Diagram.showCommonLine(Diagram.COMMON.MIDDLE, nodeList);
        Diagram.showCommonLine(Diagram.COMMON.LAST, nodeList);

    }

}
