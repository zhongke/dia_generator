package com.e.s.tool.config.handler;

import java.util.ArrayList;
import java.util.List;

import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;
import com.e.s.tool.config.pojo.Subscriber;

public class SubscriberHandler extends AbstractConfigurationHandler<Subscriber> {

    private static String PATTERN_DN_SUB = "dn:EPC-SubscriberId=";
    private static String PATTERN_DN_SUB_QUALIFY = "dn:EPC-Name=EPC-SubscriberQualification,EPC-SubscriberId=";


    List<String> headerList = new ArrayList<String>();

    List<List<String>> attributeLineList;

    private LdapTree tree;
    private ConfigurationData configurationData;

    public SubscriberHandler(LdapTree tree, ConfigurationData configurationData) {
        this.tree = tree;
        this.configurationData = configurationData;
    }


    /*
     * TODO: How to set different attributes by reflection?
     */
    @Override
    public void getConfiguration() {
        Subscriber subscriber = null;
        String attributeName;
        String subscriberId;
        Node node;


        for (int i = 0; i < tree.getNodes().size(); ++i) {
            node = tree.getNodes().get(i);
            if (node.getDn().startsWith(PATTERN_DN_SUB)) {
                subscriber = new Subscriber();

                subscriberId = node.getDn().split(",")[0].split("=")[1];
                subscriber.setSubscriberId(subscriberId);

                for (String attribute : node.getAttributes()) {
                    attributeName = attribute.split(":")[0];
                    if (attributeName.equals(Subscriber.PATTERN_EPC_TRAFFIC_IDS)) {
                        subscriber.getTrafficIds().add(attribute.split(":")[1]);
                    } else if (attributeName.equals(Subscriber.PATTERN_EPC_SUBSCRIBED_SERVICES)) {
                        subscriber.getSubscribedServiceIds().add(attribute.split(":")[1]);
                    } else if (attributeName.equals(Subscriber.PATTERN_EPC_BLACKLIST_SERVICES)) {
                        subscriber.getBlacklistServiceIds().add(attribute.split(":")[1]);
                    } else if (attributeName.equals(Subscriber.PATTERN_EPC_OPERATOR_SPECIFIC_INFO)) {
                        subscriber.getOperatorSpecificInfoList().add(attribute.split(":")[1]);
                    } else if (attributeName.equals(Subscriber.PATTERN_EPC_NOTIFICATION_DATA)) {
                        subscriber.getNotificationData().add(attribute.split(":")[1]);
                    } else if (attributeName.equals(Subscriber.PATTERN_EPC_FAMILY_ID)) {
                        subscriber.setFamilyId(attribute.split(":")[1]);
                    } else if (attributeName.equals(Subscriber.PATTERN_EPC_GROUP_IDS)) {
                        subscriber.getSubscriberGroupIds().add(attribute.split(":")[1]);
                    } else if (attributeName.equals(Subscriber.PATTERN_EPC_ENABLE_MASC)) {
                        subscriber.setEnableMasc(attribute.split(":")[1]);
                    } else if (attributeName.equals(Subscriber.PATTERN_EPC_EVENT_TRIGGERS)) {
                        subscriber.getEventTriggers().add(attribute.split(":")[1]);
                    }
                }

                for (int j = i; j < tree.getNodes().size(); ++j) {
                    node = tree.getNodes().get(j);

                    if (node.getDn().startsWith(PATTERN_DN_SUB_QUALIFY + subscriberId)) {
                        for (String attribute : node.getAttributes()) {
                            attributeName = attribute.split(":")[0];
                            if (attributeName.equals(Subscriber.PATTERN_EPC_SUBSCRIBER_QUALIFY_DATA)) {
                                subscriber.getSubscriberQualificationData().add(
                                        attribute.substring(attribute.indexOf(':') + 1, attribute.length()));
                            }
                        }
                    }
                }
                configurationData.getSubscribers().add(subscriber);
            }

        }

        showConfiguration(subscriber, configurationData.getSubscribers());


    }



    @Override
    public void getConfiguration(String fileName) {
        // TODO Auto-generated method stub

    }


}
