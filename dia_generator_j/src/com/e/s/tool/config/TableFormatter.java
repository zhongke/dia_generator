package com.e.s.tool.config;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.e.s.tool.config.pojo.Service;
import com.e.s.tool.config.pojo.Subscriber;
import com.e.s.tool.config.pojo.SubscriberGroup;


public class TableFormatter<T> {

    private static int COLUMN_LENTH_CONTEXT = 15;
    private static int COLUMN_LENTH_POLICY = 20;

    public enum COLUMN_TYPE {
        CONTEXT, POLICY
    }

    public static String PREFIX = "*       ";

    public int getMaxSizeOfElement(SubscriberGroup group) {

        int size = group.getSubscribedServiceIds().size();

        if (size < group.getBlacklistServiceIds().size()) {
            size = group.getBlacklistServiceIds().size();
        }

        if (size < group.getEventTriggers().size()) {
            size = group.getEventTriggers().size();
        }

        if (size < group.getNotificationData().size()) {
            size = group.getNotificationData().size();
        }

        return size;
    }



    public int getMaxSizeOfElement(Subscriber sub) {

        int size = sub.getBlacklistServiceIds().size();

        if (size < sub.getEventTriggers().size()) {
            size = sub.getEventTriggers().size();
        }

        if (size < sub.getNotificationData().size()) {
            size = sub.getNotificationData().size();
        }

        if (size < sub.getOperatorSpecificInfoList().size()) {
            size = sub.getOperatorSpecificInfoList().size();
        }

        if (size < sub.getSubscribedServiceIds().size()) {
            size = sub.getSubscribedServiceIds().size();
        }

        if (size < sub.getSubscriberGroupIds().size()) {
            size = sub.getSubscriberGroupIds().size();
        }

        if (size < sub.getSubscriberQualificationData().size()) {
            size = sub.getSubscriberQualificationData().size();
        }

        if (size < sub.getTrafficIds().size()) {
            size = sub.getTrafficIds().size();
        }

        return size;
    }

    public int getMaxSizeOfElement(Service service) {

        int size = service.getFlowDescriptions().size();

        if (size < service.getServiceQulificationData().size()) {
            size = service.getServiceQulificationData().size();
        }

        return size;
    }

    public boolean isNull(Map<Integer, String> headerMap, int index) {
        boolean isNull = false;
        Set<Entry<Integer, String>> entrySet = headerMap.entrySet();
        for (Entry<Integer, String> entry : entrySet) {
            if (index == entry.getKey().intValue()) {
                if (null == entry.getValue() || entry.getValue().trim().equals("")) {
                    isNull = true;
                } else {
                    isNull = false;
                }
            }
        }

        return isNull;

    }

    public void getAttribute(Map<Integer, T> attributeMap, int order, List<T> attributeList, int i) {
        int currrentSize = attributeList.size();
        if (currrentSize > 0) {
            if (i <= currrentSize - 1) {
                attributeMap.put(order, attributeList.get(i));
            } else {
                attributeMap.put(order, null);
            }
        } else {
            attributeMap.put(order, null);
        }
    }

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

        System.out.println(PREFIX + bf.toString());
    }
}
