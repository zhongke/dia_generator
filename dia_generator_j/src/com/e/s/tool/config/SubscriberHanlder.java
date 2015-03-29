package com.e.s.tool.config;

import java.util.ArrayList;
import java.util.List;

import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;
import com.e.s.tool.config.pojo.Subscriber;

public class SubscriberHanlder implements ConfigurationHandler {

    private static String PATTERN_DN_SUB = "dn:EPC-SubscriberId=";
    private static String PATTERN_DN_SUB_QUALIFY = "dn:EPC-Name=EPC-SubscriberQualification,EPC-SubscriberId=";


    private static int COLUMN_LENTH_CONTEXT = 15;
    private static int COLUMN_LENTH_POLICY  = 20;


    private enum COLUMN_TYPE {
        CONTEXT, POLICY
    }

    private LdapTree tree;
    private ConfigurationData configurationData;

    public SubscriberHanlder(LdapTree tree, ConfigurationData configurationData) {
        this.tree = tree;
        this.configurationData = configurationData;
    }


    /*
     * TODO: How to set different attributes by reflection?
     */
    @Override
    public void getConfiguration() {
        Subscriber subscriber = null;
        String attributeName = "";
        String subscriberId = "";
        Node node = null;


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
                        subscriber.setEnableMasc(attribute.split(":")[1].equals("true") ? true : false);
                    } else if (attributeName.equals(Subscriber.PATTERN_EPC_EVENT_TRIGGERS)) {
                        subscriber.getEventTriggers().add(new Integer(attribute.split(":")[1]));
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

        showConfiguration();


    }

    /*
     * TODO How to show all the elements following the order of headers?
     */
    private void showConfiguration() {

        showHeader();

        StringBuffer buffer = null;


        for (Subscriber sub : configurationData.getSubscribers()) {
            buffer = new StringBuffer();
            buffer.append("| ");
            if (null != sub.getSubscriberId()) {
            buffer.append(getColumn(sub.getSubscriberId(), COLUMN_TYPE.CONTEXT));
            }

            // subscribed services
            for (String serviceId : sub.getSubscribedServiceIds()) {
                buffer.append(getColumn(serviceId, COLUMN_TYPE.CONTEXT));
            }



            // group
            for (String group : sub.getSubscriberGroupIds()) {
                buffer.append(getColumn(group, COLUMN_TYPE.CONTEXT));
            }


            // traffic
            for (String traffic : sub.getTrafficIds()) {
                buffer.append(getColumn(traffic, COLUMN_TYPE.CONTEXT));
            }

            // black list
            for (String blackServiceId : sub.getBlacklistServiceIds()) {
                buffer.append(getColumn(blackServiceId, COLUMN_TYPE.CONTEXT));
            }

            // event trigger
            for (int eventTrigger : sub.getEventTriggers()) {
                buffer.append(getColumn(new Integer(eventTrigger).toString(), COLUMN_TYPE.CONTEXT));
            }

            // family
            if (null != sub.getFamilyId()) {
                buffer.append(getColumn(sub.getFamilyId(), COLUMN_TYPE.CONTEXT));
            }

            // enable masc
            buffer.append(getColumn(new Boolean(sub.isEnableMasc()).toString(), COLUMN_TYPE.CONTEXT));

            // qualification data
            for (String qualification : sub.getSubscriberQualificationData()) {
                buffer.append(getColumn(qualification, COLUMN_TYPE.POLICY));
            }

            // notification
            for (String notification : sub.getNotificationData()) {
                buffer.append(getColumn(notification, COLUMN_TYPE.POLICY));
            }

            // operator specific
            for (String operatorSpecificInfo : sub.getOperatorSpecificInfoList()) {
                buffer.append(getColumn(operatorSpecificInfo, COLUMN_TYPE.POLICY));
            }

        }

        System.out.println(buffer.toString());
    }



    private void showHeader() {
        showLine();

        StringBuffer buffer = new StringBuffer();

        buffer.append("| ");

        List<String> headers = new ArrayList<String>();


        for (Subscriber sub : configurationData.getSubscribers()) {
            if (null != sub.getSubscriberId()) {
                headers.add(getColumn("SUBSCRIBER_ID", COLUMN_TYPE.CONTEXT));
            }

            if (sub.getSubscribedServiceIds().size() > 0) {
                headers.add(getColumn("SUBSCRIBED_SERVICE", COLUMN_TYPE.CONTEXT));
            }


            if (sub.getSubscriberGroupIds().size() > 0) {
                headers.add(getColumn("GROUP_ID", COLUMN_TYPE.CONTEXT));
            }

            if (sub.getTrafficIds().size() > 0) {
                headers.add(getColumn("TRAFFIC_ID", COLUMN_TYPE.CONTEXT));
            }


            if (sub.getBlacklistServiceIds().size() > 0) {
                headers.add(getColumn("BLACKLIST", COLUMN_TYPE.CONTEXT));
            }

            if (sub.getEventTriggers().size() > 0) {
                headers.add(getColumn("EVENT_TRIGGER", COLUMN_TYPE.CONTEXT));
            }


            if (null != sub.getFamilyId()) {
                headers.add(getColumn("FAMILY_ID", COLUMN_TYPE.CONTEXT));
            }

            headers.add(getColumn("ENABLE_MASC", COLUMN_TYPE.CONTEXT));

            if (sub.getSubscriberQualificationData().size() > 0) {
                headers.add(getColumn("QUALIFICATION", COLUMN_TYPE.POLICY));
            }

            if (sub.getNotificationData().size() > 0) {
                headers.add(getColumn("NOTIFICATION", COLUMN_TYPE.POLICY));
            }
            if (sub.getOperatorSpecificInfoList().size() > 0) {
                headers.add(getColumn("OPERATOR_SPECIFIC", COLUMN_TYPE.POLICY));
            }

        }
        // Get a ordered list without duplicate element

        List<String> tmp = new ArrayList<String>();

        for (String header : headers) {
            if (!tmp.contains(header)) {
                tmp.add(header);
                buffer.append(header);
            }
        }

        System.out.println(buffer.toString());
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

        System.out.println(bf.toString());
    }


    @Override
    public void getConfiguration(String fileName) {
        // TODO Auto-generated method stub

    }


}