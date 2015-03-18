package com.ericsson.sapc.tool;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * This class read property file into a map(properties),
 * 
 * iterate map(properties) to get the matched key with line,
 * 
 * put the value into a new map(avps) se key which belong to the current event,
 * 
 * get the value from the line into map(avps) as values.
 * 
 * Finally iterate the map(avps) to composite the whole detail message with correct format
 * 
 * @author Kevin Zhong
 *
 */
public class RequestDataHandler {
    private Map<String, String> avps = new HashMap<String, String>();
    public static Properties properties = new Properties();

    static {

        InputStream in = Object.class.getResourceAsStream("/avps.properties");
        try {
            properties.load(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getRequestData(Event event, String line) {

        String value = "";
        Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Object, Object> entry = (Entry<Object, Object>) it.next();
            if (line.contains(entry.getKey().toString())) {
                value = line.split(":=")[1].trim();
                avps.put(entry.getValue().toString(), (value.substring(0, value.length() - 1)));
            }

            for (Entry<String, String> avp : avps.entrySet()) {

                System.out.println(avp.getKey() + " : " + avp.getValue());
            }
        }

    }


}
