package com.e.s.tool.config.handler;

import java.util.ArrayList;
import java.util.List;

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

    List<String> headerList = new ArrayList<String>();

    List<List<String>> attributeLineList;

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

        List<String> attributeList = null;

        for (Service service : configurationData.getServices()) {
            attributeLineList = new ArrayList<List<String>>();

            for (int i = 0; i <= getMaxSizeOfElement(service); ++i) {
                attributeList = new ArrayList<String>();

                if (getMaxSizeOfElement(service) > 0) {
                    if (i == getMaxSizeOfElement(service)) {
                        break;
                    }
                }


                if (null != service.getServiceId() && (i == 0)) {
                    attributeList.add(service.getServiceId());
                } else {
                    attributeList.add(null);
                }

                // description
                if (null != service.getDescription() && (i == 0)) {
                    attributeList.add(service.getDescription());
                } else {
                    attributeList.add(null);
                }



                // pccRuleId
                if (null != service.getPccRuleId() && (i == 0)) {
                    attributeList.add(service.getPccRuleId());
                } else {
                    attributeList.add(null);
                }

                // pccRuleIdv6
                if (null != service.getPccRuleIdv6() && (i == 0)) {
                    attributeList.add(service.getPccRuleIdv6());
                } else {
                    attributeList.add(null);
                }

                // pccRuleType
                if (null != service.getPccRuleType() && (i == 0)) {
                    attributeList.add(service.getPccRuleType());
                } else {
                    attributeList.add(null);
                }

                // mascServiceId
                if (null != service.getMascServiceId() && (i == 0)) {
                    attributeList.add(service.getMascServiceId());
                } else {
                    attributeList.add(null);
                }


                // precedence
                if (null != service.getPrecedence() && (i == 0)) {
                    attributeList.add(service.getPrecedence());
                } else {
                    attributeList.add(null);
                }

                // flow description
                getAttribute(attributeList, service.getFlowDescriptions(), i);


                // qualification data
                getAttribute(attributeList, service.getServiceQulificationData(), i);

                attributeLineList.add(attributeList);


            }

            showObject(attributeLineList, headerList);
        }


    }



    private void showHeader() {
        showLine();

        StringBuffer buffer = new StringBuffer();

        buffer.append("| ");


        for (int i = 0; i < configurationData.getServices().size(); i++) {
            Service service = configurationData.getServices().get(i);
            int order = 0;
            int index = 0;

            // service Id
            index = order++;
            if (null != service.getServiceId()) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Service.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            // description
            index = order++;
            if (null != service.getDescription()) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Service.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            // PccRuleId
            index = order++;
            if (null != service.getPccRuleId()) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Service.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            // PccRuleIdv6
            index = order++;
            if (null != service.getPccRuleIdv6()) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Service.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            // PccRuleType
            // How to handle dynamic service who has no Pcc rule type
            index = order++;
            if (null != service.getPccRuleType()) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Service.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            // Masc serviceId
            index = order++;
            if (null != service.getMascServiceId()) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Service.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            // Precedence
            index = order++;
            if (null != service.getPrecedence()) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Service.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            // FlowDescription
            index = order++;
            if (service.getFlowDescriptions().size() > 0) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Service.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
                }
            }

            // qualification
            index = order++;
            if (service.getServiceQulificationData().size() > 0) {
                if (i == 0 || (i > 0 && null == headerList.get(index))) {
                    headerList.add(Service.attributeList.get(index));
                }
            } else {
                // Don't override the status after previous header already exist
                if (headerList.size() == index) {
                    headerList.add(null);
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
