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
import com.e.s.tool.config.pojo.SubscriberGroup;

public class SubscriberGroupHanlder extends TableFormatter implements ConfigurationHandler {

    private static String PATTERN_DN_SUB_GROUP = "dn:EPC-SubscriberGroupId=";

    private LdapTree tree;
    private ConfigurationData configurationData;
    Map<Integer, String> headerMap = new HashMap<Integer, String>();

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
     * Show all the elements following the order of headers
     */
    private void showConfiguration() {

        showHeader();

        StringBuffer buffer = null;
        StringBuffer tmpBuffer = new StringBuffer();
        StringBuffer serviceBuffer = new StringBuffer();
        String column = null;

        List<StringBuffer> lineBufferList = null;
        /*
         * ToDo: How to show the cell aligned with header if there is some column absent for the
         * current group
         */
        for (SubscriberGroup group : configurationData.getSubscriberGroups()) {
            lineBufferList = new ArrayList<StringBuffer>();
            buffer = new StringBuffer();
            tmpBuffer = new StringBuffer();
            buffer.append("| ");
            tmpBuffer.append(buffer);
            if (null != group.getSubscriberGroupId()) {
                buffer.append(getCell(group.getSubscriberGroupId(), COLUMN_TYPE.CONTEXT));
                
                // Add new empty column to tmpBuffer in order to compose the whole line later.
                tmpBuffer.append(getCell(null, COLUMN_TYPE.CONTEXT));
            }

            if (null != group.getDescription()) {
                buffer.append(getCell(group.getDescription(), COLUMN_TYPE.CONTEXT));
                tmpBuffer.append(getCell(null, COLUMN_TYPE.CONTEXT));
            } else {
                addPlaceholderForRow(tmpBuffer, 1);
                addPlaceholderForRow(buffer, 1);
            }
            
            // subscribed services
            if (group.getSubscribedServiceIds().size() > 0) {
                for (int i = 0; i < group.getSubscribedServiceIds().size(); ++i) {
                    column = getCell(group.getSubscribedServiceIds().get(i), COLUMN_TYPE.CONTEXT);
                    if (i == 0) {
                        buffer.append(column);
                        // tmpBuffer.append(getColumn(null, COLUMN_TYPE.CONTEXT));
                    } else {
                        serviceBuffer = new StringBuffer();
                        serviceBuffer.append(tmpBuffer);
                        serviceBuffer.append(column);
                        lineBufferList.add(serviceBuffer);
                    }

                }
            } else {
                addPlaceholderForRow(buffer, 2);
                if (lineBufferList.size() > 0) {
                    addPlaceholderForRow(tmpBuffer, 2);
                }
            }

            // black list
            int lineBufferSize = lineBufferList.size();
            int currentSize = group.getBlacklistServiceIds().size();
            if (currentSize > 0) {
                for (int i = 0; i < currentSize; ++i) {
                    column = getCell(group.getBlacklistServiceIds().get(i), COLUMN_TYPE.CONTEXT);
                    if (i == 0) {
                        tmpBuffer.append(getCell(null, COLUMN_TYPE.CONTEXT));
                        buffer.append(column);
                    } else {
                        if (0 == lineBufferSize) {
                                StringBuffer blackBuffer = new StringBuffer();

                                // The tmpBuffer did not add more column follow the later column
                                blackBuffer.append(tmpBuffer);
                                lineBufferList.add(blackBuffer.append(column));

                        } else {
                            for (int j = 0; j < lineBufferSize; ++j) {
                                if (i == j + 1) {
                                    lineBufferList.get(j).append(column);
                                } else if (i > lineBufferSize) {
                                    StringBuffer blackBuffer = new StringBuffer();

                                    // The tmpBuffer did not add more column follow the later column
                                    blackBuffer.append(tmpBuffer);
                                    lineBufferList.add(blackBuffer.append(column));
                                    break;
                                }
                            }
                        }
                    }

                }

                // Please think about if the previous column is more items than current column
                if (lineBufferSize >= currentSize) {
                    for (int k = currentSize - 1; k < lineBufferSize; ++k) {
                        lineBufferList.get(k).append(getCell(null, COLUMN_TYPE.CONTEXT));
                    }
                }
            } else {
                addPlaceholderForRow(buffer, 3);
                addPlaceholderForRow(tmpBuffer, 3);
                for (StringBuffer sb : lineBufferList) {
                    addPlaceholderForRow(sb, 3);
                }
            }

            // event trigger
            lineBufferSize = lineBufferList.size();
            currentSize = group.getEventTriggers().size();
            if (currentSize > 0) {
                for (int i = 0; i < currentSize; ++i) {
                    column = getCell(group.getEventTriggers().get(i).toString(), COLUMN_TYPE.CONTEXT);
                    if (i == 0) {
                        buffer.append(column);
                        tmpBuffer.append(getCell(null, COLUMN_TYPE.CONTEXT));
                    } else {
                        if (0 == lineBufferSize) {
                            StringBuffer blackBuffer = new StringBuffer();

                            // The tmpBuffer did not add more column follow the later column
                            blackBuffer.append(tmpBuffer);
                            lineBufferList.add(blackBuffer.append(column));
                        } else {
                                
                            for (int j = 0; j < lineBufferSize; ++j) {
                                if (i == j + 1) {
                                    lineBufferList.get(j).append(column);
                                } else if (i > lineBufferSize) {
                                    StringBuffer blackBuffer = new StringBuffer();
                                    blackBuffer.append(tmpBuffer);
                                    lineBufferList.add(blackBuffer.append(column));
                                    break;
                                }
                            }
                        }
                    }

                }
                
                // Please think about if the previous column is more items than current column
                if (lineBufferSize >= currentSize) {
                    for (int k = currentSize - 1; k < lineBufferSize; ++k) {
                        lineBufferList.get(k).append(getCell(null, COLUMN_TYPE.CONTEXT));
                    }
                }
            } else {
                addPlaceholderForRow(buffer, 4);
                addPlaceholderForRow(tmpBuffer, 4);
                for (StringBuffer sb : lineBufferList) {
                    addPlaceholderForRow(sb, 4);
                }
            }

            lineBufferSize = lineBufferList.size();
            currentSize = group.getNotificationData().size();

            if (currentSize > 0) {
                // Notification
                for (int i = 0; i < currentSize; ++i) {
                    column = getCell(group.getNotificationData().get(i).toString(), COLUMN_TYPE.CONTEXT);
                    if (i == 0) {
                        buffer.append(column);
                        tmpBuffer.append(getCell(null, COLUMN_TYPE.CONTEXT));
                    } else {

                        if (0 == lineBufferSize) {
                            StringBuffer blackBuffer = new StringBuffer();
                            // The tmpBuffer did not add more column follow the later column
                                    blackBuffer.append(tmpBuffer);
                                    lineBufferList.add(blackBuffer.append(column));
                        } else {
                            for (int j = 0; j < lineBufferSize; ++j) {

                                if (i == j + 1) {
                                    lineBufferList.get(j).append(column);
                                } else if (i > lineBufferSize) {
                                    StringBuffer blackBuffer = new StringBuffer();
                                    blackBuffer.append(tmpBuffer);
                                    lineBufferList.add(blackBuffer.append(column));
                                    break;
                                }
                            }
                        }
                    }

                }

                // Please think about if the previous column is more items than current column
                if (lineBufferSize >= currentSize) {
                    for (int k = currentSize - 1; k < lineBufferSize; ++k) {
                        lineBufferList.get(k).append(getCell(null, COLUMN_TYPE.CONTEXT));
                    }
                }
            } else {
                addPlaceholderForRow(buffer, 5);
                addPlaceholderForRow(tmpBuffer, 5);
                for (StringBuffer sb : lineBufferList) {
                    addPlaceholderForRow(sb, 5);
                }
            }


            System.out.println(HEADER + buffer.toString());
            for (StringBuffer sb : lineBufferList) {
                System.out.println(HEADER + sb.toString());
            }
            showLine();
        }


    }

    private void addPlaceholderForRow(StringBuffer tmpBuffer, int key) {
        Set<Entry<Integer, String>> entrySet = headerMap.entrySet();
        for (Entry<Integer, String> entry : entrySet) {
            if (key == entry.getKey() && !entry.getValue().equals("")) {
                tmpBuffer.append(getCell(null, COLUMN_TYPE.CONTEXT));
                break;
            }

        }
    }



    private void showHeader() {
        showLine();

        StringBuffer buffer = new StringBuffer();

        buffer.append("| ");

        /*
         * Think about more than one subscriber with different attributes
         * Define the order for the header list
         */
        for (SubscriberGroup group : configurationData.getSubscriberGroups()) {
            int order = 0;
            if (null != group.getSubscriberGroupId()) {
                headerMap.put(order++, getCell("GROUP_ID", COLUMN_TYPE.CONTEXT));
            } else {
                // Add a place holder for this cell
                addCellPlaceholder(headerMap, order++);
            }

            if (null != group.getDescription()) {
                headerMap.put(order++, getCell("DESCRIPTION", COLUMN_TYPE.CONTEXT));
            } else {
                addCellPlaceholder(headerMap, order++);
            }

            if (group.getSubscribedServiceIds().size() > 0) {
                headerMap.put(order++, getCell("SUBSCRIBED", COLUMN_TYPE.CONTEXT));
            } else {
                addCellPlaceholder(headerMap, order++);
            }


            if (group.getBlacklistServiceIds().size() > 0) {
                headerMap.put(order++, getCell("BLACKLIST", COLUMN_TYPE.CONTEXT));
            } else {
                addCellPlaceholder(headerMap, order++);
            }

            if (group.getEventTriggers().size() > 0) {
                headerMap.put(order++, getCell("EVENT_TRIGGER", COLUMN_TYPE.CONTEXT));
            } else {
                addCellPlaceholder(headerMap, order++);
            }

            if (group.getNotificationData().size() > 0) {
                headerMap.put(order++, getCell("NOTIFICATION", COLUMN_TYPE.CONTEXT));
            } else {
                addCellPlaceholder(headerMap, order++);
            }


        }

        Set<Entry<Integer, String>> entrySet = headerMap.entrySet();
        for (Entry<Integer, String> entry : entrySet) {
            int sequence = 0;
            while (!(sequence > entrySet.size())) {
                if (sequence == entry.getKey().intValue()) {
                    if (!entry.getValue().equals("")) {
                        buffer.append(entry.getValue());
                        break;
                    }
                }
                ++sequence;
            }
        }

        System.out.println(HEADER + buffer.toString());
        showLine();

    }


    @Override
    public void getConfiguration(String fileName) {
        // TODO Auto-generated method stub

    }

}