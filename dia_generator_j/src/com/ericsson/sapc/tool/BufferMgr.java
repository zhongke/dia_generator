package com.ericsson.sapc.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ericsson.sapc.tool.ConstantType.EVENT_TYPE;
import com.ericsson.sapc.tool.ConstantType.MSG_FLOW;

public class BufferMgr {
    // private static String PATTERN_EVENT = "t_[a-z]*_[a-z]*_event";
    private static String PATTERN_EVENT = "t_[3,a-z,/_]*_event";
    // private static String PATTERN_EVENT_SY = "t_[a-z]*_[a-z]*_[a-z]*_event";
    private static String PATTERN_FUNCTION = "f_runEvent";
    private static String PATTERN_EVENT_FLOW = "eventFlow";

    public void readInputFromFile(String fileName, Conversation conv) {

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = null;
            Event event = new Event();
            boolean isSameFlow = true;
            int sequenceNumber = 0;
            Matcher matcher_event = null;
            Pattern pattern_event = Pattern.compile(PATTERN_EVENT);
            while (bufferedReader.ready()) {
                line = bufferedReader.readLine();
                event.setNodeSeqence(sequenceNumber);
                if (!isSameFlow) {
                    event = new Event();
                }
                if (null != line && !"".equals(line)) {
                    // Get event list from input by reqex
                    matcher_event = pattern_event.matcher(line);
                    if (matcher_event.find()) {
                        event.setEventType(matcher_event.group(0));
                        getEventInfo(line, event);
                        isSameFlow = true;
                    } else if (line.contains(PATTERN_EVENT_FLOW)) {
                        // Get event flow if has
                        String eventFlow = line.split("=")[1].split(";")[0].trim();
                        event.setEventFlow(eventFlow);
                        isSameFlow = true;
                    } else if (line.contains(PATTERN_FUNCTION)) {
                        // Get node list from input
                        String nodeName = line.split("\\]")[1].split("\\[")[1];
                        event.setNodeName(nodeName);
                        conv.nodeSet.add(nodeName);

                        // Create a map to contain the node list and node
                        // message
                        conv.events.add(event);
                        isSameFlow = false;
                        ++sequenceNumber;
                    }
                }
            }
            // Put this info into a list

            // Get node list from events
            for (Event e : conv.events) {
                if (!conv.nodeList.contains(e.getNodeName())) {
                    conv.nodeList.add(e.getNodeName());
                }
            }

            // Add SAPC node into the second position
            conv.nodeList.add(1, "SAPC");
            for (int i = 0; i < conv.nodeList.size(); ++i) {
                System.out.println(conv.nodeList.get(i));
            }

            System.out.println("----------------------------------------");

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

    private void getEventInfo(String line, Event event) {
        String release;
        String requestType;
        String eventType = event.getEventType();
        release = line.split(",")[2];
        event.setRelease(release);
        // Get the request type if the event is ccr event from Gx, Gxa, Sx
        if (EVENT_TYPE.GX_CCR_EVENT.toString().equals(eventType)
                || EVENT_TYPE.GXA_CCR_EVENT.toString().equals(eventType)
                || EVENT_TYPE.SX_CCR_EVENT.toString().equals(eventType)) {
            requestType = line.split(",")[3].split("\\)")[0].trim();
            event.setRequestType(requestType);
        }
    }

}
