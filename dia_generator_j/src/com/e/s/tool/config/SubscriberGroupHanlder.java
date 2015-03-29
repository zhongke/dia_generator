package com.e.s.tool.config;

import java.util.ArrayList;
import java.util.List;

import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;
import com.e.s.tool.config.pojo.Subscriber;
import com.e.s.tool.config.pojo.SubscriberGroup;

public class SubscriberGroupHanlder implements ConfigurationHandler {

    private static String PATTERN_DN_SUB_GROUP = "dn:EPC-SubscriberGroupId=";

    private static int COLUMN_LENTH_CONTEXT = 15;
    private static int COLUMN_LENTH_POLICY = 20;


    private enum COLUMN_TYPE {
        CONTEXT, POLICY
    }

    private static String HEADER = "*       ";

    private LdapTree tree;
    private ConfigurationData configurationData;

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
                    if (attributeName.equals(Subscriber.PATTERN_EPC_TRAFFIC_IDS)) {
                        subscriberGroup.setDescription(attribute.split(":")[1]);
                    } else if (attributeName.equals(SubscriberGroup.PATTERN_EPC_SUBSCRIBED_SERVICES)) {
                        subscriberGroup.getSubscribedServiceIds().add(attribute.split(":")[1]);
                    } else if (attributeName.equals(SubscriberGroup.PATTERN_EPC_BLACKLIST_SERVICES)) {
                        subscriberGroup.getBlacklistServiceIds().add(attribute.split(":")[1]);
                    } else if (attributeName.equals(SubscriberGroup.PATTERN_EPC_NOTIFICATION_DATA)) {
                        subscriberGroup.getNotificationData().add(attribute.split(":")[1]);
                    } else if (attributeName.equals(SubscriberGroup.PATTERN_EPC_EVENT_TRIGGERS)) {
                        subscriberGroup.getEventTriggers().add(new Integer(attribute.split(":")[1]));
                    }
                }

                configurationData.getSubscriberGroups().add(subscriberGroup);
            }

        }

        showConfiguration();

    }

    /*
     * TODO How to show all the elements following the order of headers?
     */
    private void showConfiguration() {

        showHeader();

        StringBuffer buffer = null;



        for (SubscriberGroup group : configurationData.getSubscriberGroups()) {
            buffer = new StringBuffer();
            buffer.append("| ");
            if (null != group.getSubscriberGroupId()) {
                buffer.append(getColumn(group.getSubscriberGroupId(), COLUMN_TYPE.CONTEXT));
            }

            if (null != group.getDescription()) {
                buffer.append(getColumn(group.getDescription(), COLUMN_TYPE.CONTEXT));
            }

            // subscribed services
            for (String serviceId : group.getSubscribedServiceIds()) {
                buffer.append(getColumn(serviceId, COLUMN_TYPE.CONTEXT));
            }


            // black list
            for (String blackServiceId : group.getBlacklistServiceIds()) {
                buffer.append(getColumn(blackServiceId, COLUMN_TYPE.CONTEXT));
            }

            // event trigger
            for (int eventTrigger : group.getEventTriggers()) {
                buffer.append(getColumn(new Integer(eventTrigger).toString(), COLUMN_TYPE.CONTEXT));
            }


            // notification
            for (String notification : group.getNotificationData()) {
                buffer.append(getColumn(notification, COLUMN_TYPE.POLICY));
            }


        }


        System.out.println(HEADER + buffer.toString());

    }



    private void showHeader() {
        showLine();

        StringBuffer buffer = new StringBuffer();

        buffer.append("| ");

        List<String> headers = new ArrayList<String>();

        /*
         * Think about more than one subscriber with different attributes
         */
        for (SubscriberGroup group : configurationData.getSubscriberGroups()) {
            if (null != group.getSubscriberGroupId()) {
                headers.add(getColumn("GROUP_ID", COLUMN_TYPE.CONTEXT));
            }

            if (null != group.getDescription()) {
                headers.add(getColumn("DESCRIPTION", COLUMN_TYPE.CONTEXT));
            }

            if (group.getSubscribedServiceIds().size() > 0) {
                headers.add(getColumn("SUBSCRIBED_SERVICE", COLUMN_TYPE.CONTEXT));
            }


            if (group.getBlacklistServiceIds().size() > 0) {
                headers.add(getColumn("BLACKLIST", COLUMN_TYPE.CONTEXT));
            }

            if (group.getEventTriggers().size() > 0) {
                headers.add(getColumn("EVENT_TRIGGER", COLUMN_TYPE.CONTEXT));
            }

            if (group.getNotificationData().size() > 0) {
                headers.add(getColumn("NOTIFICATION", COLUMN_TYPE.POLICY));
            }


        }
        // Get an ordered list without duplicate element

        List<String> tmp = new ArrayList<String>();

        for (String header : headers) {
            if (!tmp.contains(header)) {
                tmp.add(header);
                buffer.append(header);
            }
        }

        System.out.println(HEADER + buffer.toString());
        showLine();

    }


    private String getColumn(String resource, COLUMN_TYPE type) {

        int length = 0;

        if (COLUMN_TYPE.CONTEXT == type) {
            length = COLUMN_LENTH_CONTEXT;
        } else if (COLUMN_TYPE.POLICY == type) {
            length = COLUMN_LENTH_POLICY;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(resource);

        for (int i = resource.length(); i < length; ++i) {
            buffer.append(" ");
        }

        buffer.append("| ");

        return buffer.toString();
    }

    private void showLine() {
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < COLUMN_LENTH_CONTEXT * 3 + COLUMN_LENTH_POLICY * 4 + 15; ++i) {
            bf.append("-");

        }

        System.out.println(HEADER + bf.toString());
    }



    @Override
    public void getConfiguration(String fileName) {
        // TODO Auto-generated method stub

    }

}