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
import com.e.s.tool.config.pojo.Service;

public class ServiceHandler extends TableFormatter<String> implements ConfigurationHandler {
    private static String PATTERN_DN_SERVICE = "dn:EPC-ServiceId=";
    private static String PATTERN_DN_PCC_RULE = "dn:EPC-PccRuleName=EPC-PccRule,EPC-ServiceId=";
    private static String PATTERN_DN_SERVICE_QUALIFY =
            "dn:EPC-ServiceQualificationName=EPC-ServiceQualification,EPC-ServiceId=";

    Map<Integer, String> headerMap = new HashMap<Integer, String>();

    List<Map<Integer, String>> attributeLineList;

    public LdapTree tree;
    public ConfigurationData configurationData;

    public ServiceHandler(LdapTree tree, ConfigurationData configurationData) {
        this.tree = tree;
        this.configurationData = configurationData;
    }

    @Override
    public void getConfiguration() {
        Service service = null;
        String attributeName = "";
        String serviceId = "";
        Node node = null;


        for (int i = 0; i < tree.getNodes().size(); ++i) {
            node = tree.getNodes().get(i);
            if (node.getDn().startsWith(PATTERN_DN_SERVICE)) {
                service = new Service();

                serviceId = node.getDn().split(",")[0].split("=")[1];
                service.setServiceId(serviceId);

                for (String attribute : node.getAttributes()) {
                    attributeName = attribute.split(":")[0];
                    if (attributeName.equals(Service.PATTERN_EPC_SERVICE_DESCRIPTION)) {
                        service.setDescription(attribute.split(":")[1]);
                    } else if (attributeName.equals(Service.PATTERN_EPC_MASC_SERVICE_ID)) {
                        service.setMascServiceId(attribute.split(":")[1]);
                    }
                }

                for (int j = i; j < tree.getNodes().size(); ++j) {
                    node = tree.getNodes().get(j);

                    if (node.getDn().startsWith(PATTERN_DN_SERVICE_QUALIFY + serviceId)) {
                        for (String attribute : node.getAttributes()) {
                            attributeName = attribute.split(":")[0];
                            if (attributeName.equals(Service.PATTERN_EPC_SERVICE_QUALIFICATION_DATA)) {
                                service.getServiceQulificationData().add(
                                        attribute.substring(attribute.indexOf(':') + 1, attribute.length()));
                            }
                        }
                    } else if (node.getDn().startsWith(PATTERN_DN_PCC_RULE + serviceId)) {
                        for (String attribute : node.getAttributes()) {
                            attributeName = attribute.split(":")[0];

                            if (attributeName.equals(Service.PATTERN_EPC_PCC_RULE_ID)) {
                                service.setPccRuleId(attribute.split(":")[1]);
                            } else if (attributeName.equals(Service.PATTERN_EPC_PCC_RULE_ID_V6)) {
                                service.setPccRuleIdv6(attribute.split(":")[1]);
                            } else if (attributeName.equals(Service.PATTERN_EPC_RULE_TYPE)) {
                                service.setPccRuleType(attribute.split(":")[1]);
                            } else if (attributeName.equals(Service.PATTERN_EPC_FLOW_DESCRIPTIONS)) {
                                service.getFlowDescriptions().add(
                                        attribute.substring(attribute.indexOf(':') + 1, attribute.length()));
                            } else if (attributeName.equals(Service.PATTERN_EPC_PRECEDENCE)) {
                                service.setPrecedence(attribute.split(":")[1]);
                            }
                            attributeName = attribute.split(":")[0];
                        }
                    }
                }
                configurationData.getServices().add(service);

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

        for (Service service : configurationData.getServices()) {
            attributeLineList = new ArrayList<Map<Integer, String>>();
            for (int i = 0; i <= getMaxSizeOfElement(service); ++i) {
                int order = 0;
                attributeMap = new HashMap<Integer, String>();
                if (getMaxSizeOfElement(service) > 0) {
                    if (i == getMaxSizeOfElement(service)) {
                        break;
                    }
                }
                if (null != service.getServiceId() && (i == 0)) {
                    attributeMap.put(order++, service.getServiceId());
                } else {
                    attributeMap.put(order++, null);
                }

                // description
                if (null != service.getDescription() && (i == 0)) {
                    attributeMap.put(order++, service.getDescription());
                } else {
                    attributeMap.put(order++, null);
                }



                // pccRuleId
                if (null != service.getPccRuleId() && (i == 0)) {
                    attributeMap.put(order++, service.getPccRuleId());
                } else {
                    attributeMap.put(order++, null);
                }

                // pccRuleIdv6
                if (null != service.getPccRuleIdv6() && (i == 0)) {
                    attributeMap.put(order++, service.getPccRuleIdv6());
                } else {
                    attributeMap.put(order++, null);
                }

                // pccRuleType
                if (null != service.getPccRuleType() && (i == 0)) {
                    attributeMap.put(order++, service.getPccRuleType());
                } else {
                    attributeMap.put(order++, null);
                }

                // mascServiceId
                if (null != service.getMascServiceId() && (i == 0)) {
                    attributeMap.put(order++, service.getMascServiceId());
                } else {
                    attributeMap.put(order++, null);
                }


                // precedence
                if (null != service.getPrecedence() && (i == 0)) {
                    attributeMap.put(order++, service.getPrecedence());

                } else {
                    attributeMap.put(order++, null);
                }

                // flow description
                getAttribute(attributeMap, order++, service.getFlowDescriptions(), i);


                // qualification data
                getAttribute(attributeMap, order++, service.getServiceQulificationData(), i);


                attributeLineList.add(attributeMap);

                Set<Entry<Integer, String>> attributeSet = attributeMap.entrySet();
                for (Entry<Integer, String> attributeEntry : attributeSet) {
                    // System.out.println(attributeEntry.getKey() + " : " +
                    // attributeEntry.getValue());
                }

            }

            showService();
        }


    }



    private void showService() {
        for (Map<Integer, String> attributeMap : attributeLineList) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("| ");
            Set<Entry<Integer, String>> attributeSet = attributeMap.entrySet();
            Set<Entry<Integer, String>> headerSet = headerMap.entrySet();

            int sequence = 0;
            for (Entry<Integer, String> headerEntry : headerSet) {
                for (Entry<Integer, String> attributeEntry : attributeSet) {

                    if ((sequence == headerEntry.getKey()) && (sequence == attributeEntry.getKey())) {
//                        System.out.println(sequence);
//                        System.out.println(headerEntry.getKey() + " : " + headerEntry.getValue());
//                        System.out.println(attributeEntry.getKey() + " : " + attributeEntry.getValue());
//                        System.out.println();
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


    private void showHeader() {
        showLine();

        StringBuffer buffer = new StringBuffer();

        buffer.append("| ");


        for (Service service : configurationData.getServices()) {
            int order = 0;
            int index = 0;

            // service Id
            index = order++;
            if (null != service.getServiceId()) {
                headerMap.put(index, Service.attributeList.get(index));
            } else {
                if (isNull(headerMap, index)) {
                    headerMap.put(index, null);
                }
            }

            // description
            index = order++;
            if (null != service.getDescription()) {
                headerMap.put(index, Service.attributeList.get(index));
            } else {
                if (isNull(headerMap, index)) {
                    headerMap.put(index, null);
                }
            }

            // PccRuleId
            index = order++;
            if (null != service.getPccRuleId()) {
                headerMap.put(index, Service.attributeList.get(index));
            } else {
                if (isNull(headerMap, index)) {
                    headerMap.put(index, null);
                };
            }

            // PccRuleIdv6
            index = order++;
            if (null != service.getPccRuleIdv6()) {
                headerMap.put(index, Service.attributeList.get(index));
            } else {
                headerMap.put(index, null);
            }

            // PccRuleType
            // How to handle dynamic service who has no Pcc rule type
            index = order++;
            if (null != service.getPccRuleType()) {
                headerMap.put(index, Service.attributeList.get(index));
            } else {
                if (isNull(headerMap, index)) {
                    headerMap.put(index, null);
                }
            }

            // Masc serviceId
            index = order++;
            if (null != service.getMascServiceId()) {
                headerMap.put(index, Service.attributeList.get(index));
            } else {
                    headerMap.put(index, null);
            }

            // Precedence
            index = order++;
            if (null != service.getPrecedence()) {
                headerMap.put(index, Service.attributeList.get(index));
            } else {
                if (isNull(headerMap, index)) {
                    headerMap.put(index, null);
                }
            }

            // FlowDescription
            index = order++;
            if (service.getFlowDescriptions().size() > 0) {
                headerMap.put(index, Service.attributeList.get(index));
            } else {
                if (isNull(headerMap, index)) {
                    headerMap.put(index, null);
                }
            }

            // qualification
            index = order++;
            if (service.getServiceQulificationData().size() > 0) {
                headerMap.put(index, Service.attributeList.get(index));
            } else {
                if (isNull(headerMap, index)) {
                    headerMap.put(index, null);
                }
            }

        }

        // Get a ordered list without duplicate element

        Set<Entry<Integer, String>> entrySet = headerMap.entrySet();
        for (Entry<Integer, String> entry : entrySet) {
            int sequence = 0;
            while (!(sequence > entrySet.size())) {
                if (sequence == entry.getKey().intValue()) {
                    if (null != entry.getValue()) {
                        System.out.println(entry.getKey() + " : " + entry.getValue());
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
