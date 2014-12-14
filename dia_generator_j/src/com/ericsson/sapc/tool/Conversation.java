package com.ericsson.sapc.tool;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Conversation {
    List<Event> events = new LinkedList<Event>();
    Set<String> nodeSet = new HashSet<String>();
    List<String> nodeList = new LinkedList<String>();
}
