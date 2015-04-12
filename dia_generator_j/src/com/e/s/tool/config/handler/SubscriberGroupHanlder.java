package com.e.s.tool.config.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.e.s.tool.config.TableFormatter;
import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;
import com.e.s.tool.config.pojo.SubscriberGroup;

public class SubscriberGroupHanlder extends TableFormatter<String> implements ConfigurationHandler {

    private static String PATTERN_DN_SUB_GROUP = "dn:EPC-SubscriberGroupId=";

    private LdapTree tree;
    private ConfigurationData configurationData;


    Map<Integer, String> headerMap = new HashMap<Integer, String>();

    List<Map<Integer, String>> attributeLineList;

    public SubscriberGroupHanlder(LdapTree tree, ConfigurationData configurationData) {
        this.tree = tree;
        this.configurationData = configurationData;
    }

    @Override
    public void getConfiguration() {
        SubscriberGroup subscriberGroup = null;
        String attributeName = "";

        for (Node node : tree.getNodes()) {
            if (node.getDn().startsWith(PATTERN_DN_SUB_GROUP)) {
                subscriberGroup = new SubscriberGroup();
                subscriberGroup.setSubscriberGroupId(node.getDn().split(",")[0].split("=")[1]);
                for (String attribute : node.getAttributes()) {
                    attributeName = attribute.split(":")[0];
                    if (attributeName.equals(SubscriberGroup.PATTERN_EPC_SUBSCRIBER_GROUP_DESCRIPTION)) {
                        subscriberGroup.setDescription(attribute.split(":")[1]);
                    } else if (attributeName.equals(SubscriberGroup.PATTERN_EPC_SUBSCRIBED_SERVICES)) {
                        subscriberGroup.getSubscribedServiceIds().add(attribute.split(":")[1]);
                    } else if (attributeName.equals(SubscriberGroup.PATTERN_EPC_BLACKLIST_SERVICES)) {
                        subscriberGroup.getBlacklistServiceIds().add(attribute.split(":")[1]);
                    } else if (attributeName.equals(SubscriberGroup.PATTERN_EPC_NOTIFICATION_DATA)) {
                        subscriberGroup.getNotificationData().add(attribute.split(":")[1]);
                    } else if (attributeName.equals(SubscriberGroup.PATTERN_EPC_EVENT_TRIGGERS)) {
                        subscriberGroup.getEventTriggers().add(attribute.split(":")[1]);
                    }
                }

                configurationData.getSubscriberGroups().add(subscriberGroup);
            }

        }

        showConfiguration();

    }

    /*
     * Show all the elements following the order of headers
     */
    private void showConfiguration() {
        try {
            showHeader();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Map<Integer, String> attributeMap = null;

        for (SubscriberGroup group : configurationData.getSubscriberGroups()) {
            attributeLineList = new ArrayList<Map<Integer, String>>();
            // Iterate every group by the maximum size of its elements.
            for (int i = 0; i < getMaxSizeOfElement(group); ++i) {
                int order = 0;
                attributeMap = new HashMap<Integer, String>();
               
                // group Id
                if (null != group.getSubscriberGroupId() && (i == 0)) {
                    attributeMap.put(order++, group.getSubscriberGroupId());
                } else {
                    attributeMap.put(order++, null);
                }
                
                // description 
                if (null != group.getDescription() && (i == 0)) {
                    attributeMap.put(order++, group.getDescription());
                } else {
                    attributeMap.put(order++, null);
                }
                
                // subscribed service
                getAttribute(attributeMap, order++, group.getSubscribedServiceIds(), i);

                // blacklist service
                getAttribute(attributeMap, order++, group.getBlacklistServiceIds(), i);

                // event trigger
                getAttribute(attributeMap, order++, group.getEventTriggers(), i);

                // notification
                getAttribute(attributeMap, order++, group.getNotificationData(), i);

               
                attributeLineList.add(attributeMap);
            }



            showGroups();

        }
    }

    private void showGroups() {
        for (Map<Integer, String> attributeMap : attributeLineList) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("| ");
            Set<Entry<Integer, String>> attributeSet = attributeMap.entrySet();
            Set<Entry<Integer, String>> headerSet = headerMap.entrySet();

            int sequence = 0;
            for (Entry<Integer, String> headerEntry : headerSet) {

                for (Entry<Integer, String> attributeEntry : attributeSet) {
                    if ((sequence == headerEntry.getKey()) && (sequence == attributeEntry.getKey())) {
                        if (null == headerEntry.getValue()) {
                            break;
                        }

                        if (null != attributeEntry.getValue()) {
                            buffer.append(getCell(attributeEntry.getValue(), COLUMN_TYPE.CONTEXT));
                        } else {
                            buffer.append(getCell(null, COLUMN_TYPE.CONTEXT));
                        }
                    }

                            }
                ++sequence;
            }
            System.out.println(PREFIX + buffer.toString());
        }
        showLine();

    }

    private void showHeader() throws ClassNotFoundException {
        showLine();

        StringBuffer buffer = new StringBuffer();

        buffer.append("| ");

        /*
         * Think about more than one subscriber with different attributes Define the order for the
         * header list
         */
        for (SubscriberGroup group : configurationData.getSubscriberGroups()) {
            int order = 0;
            int index = 0;
/*
            Class clazz = Class.forName("com.e.s.tool.config.pojo.SubscriberGroup");
            Method[] methods = clazz.getDeclaredMethods();

            System.out.println("-----------------------");
            for (int i = 0; i < methods.length; ++i) {
                System.out.println(methods[i].getName());
            }
            System.out.println("-----------------------");
*/

            index = order++;
            if (null != group.getSubscriberGroupId()) {
                headerMap.put(index, SubscriberGroup.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

            index = order++;
            if (null != group.getDescription()) {
                headerMap.put(index, SubscriberGroup.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

            index = order++;
            if (group.getSubscribedServiceIds().size() > 0) {
                headerMap.put(index, SubscriberGroup.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }
            index = order++;

            if (group.getBlacklistServiceIds().size() > 0) {
                headerMap.put(index, SubscriberGroup.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }
            index = order++;

            if (group.getEventTriggers().size() > 0) {
                headerMap.put(index, SubscriberGroup.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

            index = order++;
            if (group.getNotificationData().size() > 0) {
                headerMap.put(index, SubscriberGroup.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }
        }

        Set<Entry<Integer, String>> entrySet = headerMap.entrySet();
        for (Entry<Integer, String> entry : entrySet) {
            int sequence = 0;
            while (!(sequence > entrySet.size())) {
                if (sequence == entry.getKey().intValue()) {
                    if (null != entry.getValue()) {
                        buffer.append(getCell(entry.getValue(), COLUMN_TYPE.CONTEXT));
                        break;
                    }
                }
                ++sequence;
            }
        }

        System.out.println(PREFIX + buffer.toString());
        showLine();

    }



    @Override
    public void getConfiguration(String fileName) {
        // TODO Auto-generated method stub

    }

}