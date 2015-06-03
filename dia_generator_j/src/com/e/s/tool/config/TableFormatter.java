package com.e.s.tool.config;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.e.s.tool.config.pojo.Service;
import com.e.s.tool.config.pojo.Subscriber;


public class TableFormatter<T> {

    private static int COLUMN_LENTH_CONTEXT = 15;
    private static int COLUMN_LENTH_POLICY = 20;

    public enum COLUMN_TYPE {
        CONTEXT, POLICY
    }

    public static String PREFIX = "*       ";

    public int getMaxSizeOfElement(T object) {
        return 0;

    }

    /*
     * public int getMaxSizeOfElement(SubscriberGroup group) {
     * 
     * int size = group.getSubscribedServiceIds().size();
     * 
     * if (size < group.getBlacklistServiceIds().size()) { size =
     * group.getBlacklistServiceIds().size(); }
     * 
     * if (size < group.getEventTriggers().size()) { size = group.getEventTriggers().size(); }
     * 
     * if (size < group.getNotificationData().size()) { size = group.getNotificationData().size(); }
     * 
     * return size; }
     */


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



    public void getAttribute(List<T> attributeList, List<T> attributeValues, int i) {
        int currrentSize = attributeValues.size();
        if (currrentSize > 0) {
            if (i <= currrentSize - 1) {
                attributeList.add(attributeValues.get(i));
            } else {
                attributeList.add(null);
            }
        } else {
            attributeList.add(null);
        }
    }


    public void getAttribute(String[] attributeList, int index, List<?> attributeValues, int i) {
        int currrentSize = attributeValues.size();
        if (currrentSize > 0) {
            if (i <= currrentSize - 1) {
                attributeList[index] = (String) (attributeValues.get(i));
            } else {
                attributeList[index] = null;
            }
        } else {
            attributeList[index] = null;
        }
    }

    public void showObject(List<List<String>> attributeLineList, List<String> headerList) {
        for (List<String> attributeList : attributeLineList) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("| ");

            for (int i = 0; i < attributeList.size(); ++i) {
                if (null != attributeList.get(i)) {
                    buffer.append(getCell(attributeList.get(i), COLUMN_TYPE.CONTEXT));
                } else {
                    if (null != headerList.get(i)) {
                        buffer.append(getCell(null, COLUMN_TYPE.CONTEXT));
                    }
                }
            }

            System.out.println(PREFIX + buffer.toString());
        }
        showLine();

    }

    public void showObject(List<String[]> attributeLineList, String[] headerList) {
        for (String[] attributeList : attributeLineList) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("| ");

            for (int i = 0; i < attributeList.length; ++i) {
                if (null != attributeList[i]) {
                    buffer.append(getCell(attributeList[i], COLUMN_TYPE.CONTEXT));
                } else {
                    if (null != headerList[i]) {
                        buffer.append(getCell(null, COLUMN_TYPE.CONTEXT));
                    }
                }
            }

            System.out.println(PREFIX + buffer.toString());
        }
        showLine();

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
