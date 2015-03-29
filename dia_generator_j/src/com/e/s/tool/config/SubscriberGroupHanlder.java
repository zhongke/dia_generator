package com.e.s.tool.config;

import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;
import com.e.s.tool.config.pojo.Subscriber;
import com.e.s.tool.config.pojo.SubscriberGroup;

public class SubscriberGroupHanlder implements ConfigurationHandler {

    private static String PATTERN_DN_SUB_GROUP = "dn:EPC-SubscriberGroupId=";


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

        for (SubscriberGroup group : configurationData.getSubscriberGroups()) {
            System.out.println(group);
        }
        showConfiguration();

    }

    private void showConfiguration() {

    }
    @Override
    public void getConfiguration(String fileName) {
        // TODO Auto-generated method stub

    }

}