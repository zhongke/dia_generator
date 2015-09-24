package com.e.s.tool.config.handler;

import java.util.List;

import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;
import com.e.s.tool.config.pojo.SubscriberGroup;

public class SubscriberGroupHandler extends AbstractConfigurationHandler<SubscriberGroup> {

    private static String PATTERN_DN_SUB_GROUP = "dn:EPC-SubscriberGroupId=";

    private LdapTree tree;
    private ConfigurationData configurationData;


    List<String[]> attributeLineList;

    public SubscriberGroupHandler(LdapTree tree, ConfigurationData configurationData) {
        this.tree = tree;
        this.configurationData = configurationData;
    }

    @Override
    public void getConfiguration() {
        SubscriberGroup subscriberGroup = null;
        String attributeName = "";
        boolean existed = false;

        for (Node node : tree.getNodes()) {
            if (node.getDn().startsWith(PATTERN_DN_SUB_GROUP)) {
                existed = true;
                subscriberGroup = new SubscriberGroup();
                subscriberGroup.setSubscriberGroupId(node.getDn().split(",")[0].split("=")[1]);
                for (String attribute : node.getAttributes()) {
                    attributeName = attribute.split(":")[0];
                    // if
                    // (attributeName.equals(SubscriberGroup.PATTERN_EPC_SUBSCRIBER_GROUP_DESCRIPTION))
                    // {
                    // subscriberGroup.setDescription(attribute.split(":")[1]);
                    // } else
                    if (attributeName.equals(SubscriberGroup.PATTERN_EPC_SUBSCRIBED_SERVICES)) {
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

        if (existed) {
            System.out.print("*       + ");
            System.out.print("GROUP");
            System.out.println(" +");

            showConfiguration(subscriberGroup, configurationData.getSubscriberGroups());

            System.out.println("*");
        }

    }


    @Override
    public void getConfiguration(String fileName) {
        // TODO Auto-generated method stub

    }

}
