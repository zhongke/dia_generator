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
import com.e.s.tool.config.pojo.Subscriber;

public class SubscriberHanlder extends TableFormatter<String> implements ConfigurationHandler {

    private static String PATTERN_DN_SUB = "dn:EPC-SubscriberId=";
    private static String PATTERN_DN_SUB_QUALIFY = "dn:EPC-Name=EPC-SubscriberQualification,EPC-SubscriberId=";


    Map<Integer, String> headerMap = new HashMap<Integer, String>();

    List<Map<Integer, String>> attributeLineList;

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

        Map<Integer, String> attributeMap = null;


        for (Subscriber sub : configurationData.getSubscribers()) {
            attributeLineList = new ArrayList<Map<Integer, String>>();
            
            for (int i = 0; i <= getMaxSizeOfElement(sub); ++i) {
                int order = 0;
                attributeMap = new HashMap<Integer, String>();
                if (getMaxSizeOfElement(sub) > 0) {
                    if (i == getMaxSizeOfElement(sub)) {
                        break;
                    }
                }
                if (null != sub.getSubscriberId() && (i == 0)) {
                    attributeMap.put(order++, sub.getSubscriberId());
                } else {
                    attributeMap.put(order++, null);
                }

                // subscribed services
                getAttribute(attributeMap, order++, sub.getSubscribedServiceIds(), i);


                // group
                getAttribute(attributeMap, order++, sub.getSubscriberGroupIds(), i);

                // traffic
                getAttribute(attributeMap, order++, sub.getTrafficIds(), i);


                // black list
                getAttribute(attributeMap, order++, sub.getBlacklistServiceIds(), i);

                // event trigger
                getAttribute(attributeMap, order++, sub.getEventTriggers(), i);

                // family
                if (null != sub.getFamilyId() && (i == 0)) {
                    attributeMap.put(order++, sub.getFamilyId());
                } else {
                    attributeMap.put(order++, null);
                }

                // enable masc
                if (null != sub.isEnableMasc() && (i == 0)) {
                    attributeMap.put(order++, sub.isEnableMasc());
                } else {
                    attributeMap.put(order++, null);
                }


                // qualification data
                getAttribute(attributeMap, order++, sub.getSubscriberQualificationData(), i);

                // notification
                getAttribute(attributeMap, order++, sub.getNotificationData(), i);

                // operator specific
                getAttribute(attributeMap, order++, sub.getOperatorSpecificInfoList(), i);

                attributeLineList.add(attributeMap);
            }
            showSubscriber();
        }

    }



    private void showSubscriber() {
        for (Map<Integer, String> attributeMap : attributeLineList) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("| ");
            Set<Entry<Integer, String>> entrySet = attributeMap.entrySet();
            Set<Entry<Integer, String>> headerSet = headerMap.entrySet();

            int sequence = 0;
            for (Entry<Integer, String> headerEntry : headerSet) {
                

                    for (Entry<Integer, String> entry : entrySet) {
                        if ((sequence == headerEntry.getKey()) && (sequence == entry.getKey())) {
                            if (null == headerEntry.getValue()) {
                                break;
                        }
                            if (null != entry.getValue()) {
                                buffer.append(getCell(entry.getValue(), COLUMN_TYPE.CONTEXT));
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


    private void showHeader() {
        showLine();

        StringBuffer buffer = new StringBuffer();

        buffer.append("| ");

        for (Subscriber sub : configurationData.getSubscribers()) {
            int order = 0;
            int index = 0;
            index = order++;
            if (null != sub.getSubscriberId()) {
                headerMap.put(index, Subscriber.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

            index = order++;
            if (sub.getSubscribedServiceIds().size() > 0) {
                headerMap.put(index, Subscriber.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }


            index = order++;
            if (sub.getSubscriberGroupIds().size() > 0) {
                headerMap.put(index, Subscriber.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

            index = order++;
            if (sub.getTrafficIds().size() > 0) {
                headerMap.put(index, Subscriber.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

            index = order++;
            if (sub.getBlacklistServiceIds().size() > 0) {
                headerMap.put(index, Subscriber.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

            index = order++;
            if (sub.getEventTriggers().size() > 0) {
                headerMap.put(index, Subscriber.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

            index = order++;
            if (null != sub.getFamilyId()) {
                headerMap.put(index, Subscriber.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

            index = order++;
            if (null != sub.isEnableMasc()) {
                headerMap.put(index, Subscriber.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

            index = order++;
            if (sub.getSubscriberQualificationData().size() > 0) {
                headerMap.put(index, Subscriber.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

            index = order++;
            if (sub.getNotificationData().size() > 0) {
                headerMap.put(index, Subscriber.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

            index = order++;
            if (sub.getOperatorSpecificInfoList().size() > 0) {
                headerMap.put(index, Subscriber.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

        }
        // Get a ordered list without duplicate element

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