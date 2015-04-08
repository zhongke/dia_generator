package com.e.s.tool.config;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class TableFormatter {

    private static int COLUMN_LENTH_CONTEXT = 15;
    private static int COLUMN_LENTH_POLICY = 20;

    public enum COLUMN_TYPE {
        CONTEXT, POLICY
    }

    public static String HEADER = "*       ";


    public void addCellPlaceholder(Map<Integer, String> headerMap, int key) {
        Set<Entry<Integer, String>> entrySet = headerMap.entrySet();
        for (Entry<Integer, String> entry : entrySet) {
            if (entry.getKey() == key) {
                if (entry.getValue().equals("")) {
                    headerMap.put(key, getCell(null, COLUMN_TYPE.CONTEXT));
                }
            }
        }
    }


    public String getCell(String resource, COLUMN_TYPE type) {

        int length = 0;
        int count = 0;

        if (COLUMN_TYPE.CONTEXT == type) {
            length = COLUMN_LENTH_CONTEXT;
        } else if (COLUMN_TYPE.POLICY == type) {
            length = COLUMN_LENTH_POLICY;
        }


        StringBuffer buffer = new StringBuffer();
        if (null != resource) {
            buffer.append(resource);
            count = resource.length();
        }

        for (int i = count; i < length; ++i) {
            buffer.append(" ");
        }

        buffer.append("| ");

        return buffer.toString();
    }

    public void showLine() {
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < COLUMN_LENTH_CONTEXT * 3 + COLUMN_LENTH_POLICY * 4 + 15; ++i) {
            bf.append("-");

        }

        System.out.println(HEADER + bf.toString());
    }
}
