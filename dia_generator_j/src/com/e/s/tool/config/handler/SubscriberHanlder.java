package com.e.s.tool.config.handler;

import java.util.ArrayList;
import java.util.List;

import com.e.s.tool.config.TableFormatter;
import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;
import com.e.s.tool.config.pojo.Subscriber;

public class SubscriberHanlder extends TableFormatter<String> implements ConfigurationHandler {

    private static String PATTERN_DN_SUB = "dn:EPC-SubscriberId=";
    private static String PATTERN_DN_SUB_QUALIFY = "dn:EPC-Name=EPC-SubscriberQualification,EPC-SubscriberId=";


    List<String> headerList = new ArrayList<String>();

    List<List<String>> attributeLineList;

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

        showConfiguration();


    }

    /*
     * TODO How to show all the elements following the order of headers?
     */
    private void showConfiguration() {

        showHeader();

        List<String> attributeList = null;


        for (Subscriber sub : configurationData.getSubscribers()) {
            attributeLineList = new ArrayList<List<String>>();

            for (int i = 0; i <= getMaxSizeOfElement(sub); ++i) {
                attributeList = new ArrayList<String>();
                if (getMaxSizeOfElement(sub) > 0) {
                    if (i == getMaxSizeOfElement(sub)) {
                        break;
                    }
                }


                if (null != sub.getSubscriberId() && (i == 0)) {
                    attributeList.add(sub.getSubscriberId());
                } else {
                    attributeList.add(null);
                }

                // subscribed services
                getAttribute(attributeList, sub.getSubscribedServiceIds(), i);


                // group
                getAttribute(attributeList, sub.getSubscriberGroupIds(), i);

                // traffic
                getAttribute(attributeList, sub.getTrafficIds(), i);


                // black list
                getAttribute(attributeList, sub.getBlacklistServiceIds(), i);

                // event trigger
                getAttribute(attributeList, sub.getEventTriggers(), i);

                // family
                if (null != sub.getFamilyId() && (i == 0)) {
                    attributeList.add(sub.getFamilyId());
                } else {
                    attributeList.add(null);
                }

                // enable masc
                if (null != sub.isEnableMasc() && (i == 0)) {
                    attributeList.add(sub.isEnableMasc());
                } else {
                    attributeList.add(null);
                }


                // qualification data
                getAttribute(attributeList, sub.getSubscriberQualificationData(), i);

                // notification
                getAttribute(attributeList, sub.getNotificationData(), i);

                // operator specific
                getAttribute(attributeList, sub.getOperatorSpecificInfoList(), i);

                attributeLineList.add(attributeList);
            }
            showObject(attributeLineList, headerList);
        }

    }


    private void showHeader() {
        showLine();

        StringBuffer buffer = new StringBuffer();

        buffer.append("| ");

        for (int i = 0; i < configurationData.getSubscribers().size(); i++) {
            Subscriber subscriber = configurationData.getSubscribers().get(i);
            int order = 0;
            int index = 0;
            index = order++;
            if (null != subscriber.getSubscriberId()) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Subscriber.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            index = order++;
            if (subscriber.getSubscribedServiceIds().size() > 0) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Subscriber.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }


            index = order++;
            if (subscriber.getSubscriberGroupIds().size() > 0) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Subscriber.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            index = order++;
            if (subscriber.getTrafficIds().size() > 0) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Subscriber.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            index = order++;
            if (subscriber.getBlacklistServiceIds().size() > 0) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Subscriber.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            index = order++;
            if (subscriber.getEventTriggers().size() > 0) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Subscriber.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            index = order++;
            if (null != subscriber.getFamilyId()) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Subscriber.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            index = order++;
            if (null != subscriber.isEnableMasc()) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Subscriber.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            index = order++;
            if (subscriber.getSubscriberQualificationData().size() > 0) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Subscriber.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            index = order++;
            if (subscriber.getNotificationData().size() > 0) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Subscriber.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            index = order++;
            if (subscriber.getOperatorSpecificInfoList().size() > 0) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Subscriber.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

        }
        // Get a ordered list without duplicate element

        for (String header : headerList) {
            if (null != header) {
                buffer.append(getCell(header, COLUMN_TYPE.CONTEXT));
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
