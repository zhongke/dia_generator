package com.e.s.tool.config.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.e.s.tool.config.TableFormatter;
import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;
import com.e.s.tool.config.pojo.SubscriberGroup;

public class SubscriberGroupHanlder extends TableFormatter<String> implements ConfigurationHandler {

    private static String PATTERN_DN_SUB_GROUP = "dn:EPC-SubscriberGroupId=";

    private LdapTree tree;
    private ConfigurationData configurationData;


    private static String[] headerList = null;

    static {
        headerList = new String[SubscriberGroup.attributeList.size()];
        for (int i = 0; i < SubscriberGroup.attributeList.size(); ++i) {
            headerList[i] = null;
        }

    }

    List<List<String>> attributeLineList;

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
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }

        List<String> attributeList = null;

        for (SubscriberGroup group : configurationData.getSubscriberGroups()) {
            attributeLineList = new ArrayList<List<String>>();
            // Iterate every group by the maximum size of its elements.
            for (int i = 0; i <= getMaxSizeOfElement(group); ++i) {
                attributeList = new ArrayList<String>();

                if (getMaxSizeOfElement(group) > 0) {
                    if (i == getMaxSizeOfElement(group)) {
                        break;
                    }
                }

                // group Id
                if (null != group.getSubscriberGroupId() && (i == 0)) {
                    attributeList.add(group.getSubscriberGroupId());
                } else {
                    attributeList.add(null);
                }

                // description
                if (null != group.getDescription() && (i == 0)) {
                    attributeList.add(group.getDescription());
                } else {
                    attributeList.add(null);
                }

                // subscribed service
                getAttribute(attributeList, group.getSubscribedServiceIds(), i);

                // blacklist service
                getAttribute(attributeList, group.getBlacklistServiceIds(), i);

                // event trigger
                getAttribute(attributeList, group.getEventTriggers(), i);

                // notification
                getAttribute(attributeList, group.getNotificationData(), i);


                attributeLineList.add(attributeList);
            }



            showObject(attributeLineList, headerList);

        }
    }


    private void showHeader() throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        showLine();

        StringBuffer buffer = new StringBuffer();

        buffer.append("| ");

        /*
         * Think about more than one subscriber with different attributes Define the order for the
         * header list
         */


        for (int i = 0; i < configurationData.getSubscriberGroups().size(); i++) {
            SubscriberGroup group = configurationData.getSubscriberGroups().get(i);

            Class<?> clazz = Class.forName("com.e.s.tool.config.pojo.SubscriberGroup");
            Method[] methods = clazz.getDeclaredMethods();
            String methodName = null;

            for (int j = 0; j < methods.length; ++j) {
                methodName = methods[j].getName();

                if (methodName.startsWith("get")) {
                    for (int k = 0; k < SubscriberGroup.attributeList.size(); k++) {
                        String attr = SubscriberGroup.attributeList.get(k).split(":")[0].toLowerCase();
                        String attrName = methodName.toLowerCase().substring(3, methodName.length());

                        if (attr.equals(attrName)) {
                            Object result = methods[j].invoke(group, (Object[]) null);
                            Class<?> returnClass = methods[j].getReturnType();
                            boolean headerExisted = false;
                            if (returnClass.equals(List.class) && (((List<?>) result).size() > 0)) {
                                headerExisted = true;
                            } else if (returnClass.equals(String.class) && (result != null)) {
                                headerExisted = true;
                            }

                            if (headerExisted) {
                                headerList[k] = SubscriberGroup.attributeList.get(k).split(":")[1];
                            }
                        }
                    }
                }
            }
        }

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
