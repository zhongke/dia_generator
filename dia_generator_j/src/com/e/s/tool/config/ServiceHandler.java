package com.e.s.tool.config;

import java.util.ArrayList;
import java.util.List;

import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;
import com.e.s.tool.config.pojo.Service;

public class ServiceHandler implements ConfigurationHandler {
    private static String PATTERN_DN_SERVICE        = "dn:EPC-ServiceId=";
    private static String PATTERN_DN_PCC_RULE       = "dn:EPC-PccRuleName=EPC-PccRule,EPC-ServiceId=";
    private static String PATTERN_DN_SERVICE_QUALIFY= "dn:EPC-ServiceQualificationName=EPC-ServiceQualification,EPC-ServiceId=";

    private static int COLUMN_LENTH_CONTEXT = 15;
    private static int COLUMN_LENTH_POLICY = 20;

    private static String HEADER = "*       ";
    private enum COLUMN_TYPE {
        CONTEXT, POLICY
    }

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
                                service.setPccRuleType(new Integer(attribute.split(":")[1]).intValue());
                            } else if (attributeName.equals(Service.PATTERN_EPC_FLOW_DESCRIPTIONS)) {
                                service.getFlowDescriptions().add(
                                        attribute.substring(attribute.indexOf(':') + 1, attribute.length()));
                            } else if (attributeName.equals(Service.PATTERN_EPC_PRECEDENCE)) {
                                service.setPrecedence(new Integer(attribute.split(":")[1]).intValue());
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

        StringBuffer buffer = null;

        for (Service service : configurationData.getServices()) {
            buffer = new StringBuffer();
            buffer.append("| ");
            if (null != service.getServiceId()) {
                buffer.append(getColumn(service.getServiceId(), COLUMN_TYPE.CONTEXT));
            }

            // description
            if (null != service.getDescription()) {
                buffer.append(getColumn(service.getDescription(), COLUMN_TYPE.CONTEXT));
            }



            // pccRuleId
            if (null != service.getPccRuleId()) {
                buffer.append(getColumn(service.getPccRuleId(), COLUMN_TYPE.CONTEXT));
            }


            // pccRuleIdv6
            if (null != service.getPccRuleIdv6()) {
                buffer.append(getColumn(service.getPccRuleIdv6(), COLUMN_TYPE.CONTEXT));
            }

            // pccRuleType
            buffer.append(getColumn(new Integer(service.getPccRuleType()).toString(), COLUMN_TYPE.CONTEXT));

            // mascServiceId
            if (null != service.getMascServiceId()) {
                buffer.append(getColumn(service.getMascServiceId(), COLUMN_TYPE.CONTEXT));
            }


            // precedence
            buffer.append(getColumn(new Integer(service.getPrecedence()).toString(), COLUMN_TYPE.CONTEXT));

            // flow description
            for (String flowDescription : service.getFlowDescriptions()) {
                buffer.append(getColumn(flowDescription, COLUMN_TYPE.POLICY));
            }


            // qualification data
            for (String qualification : service.getServiceQulificationData()) {
                buffer.append(getColumn(qualification, COLUMN_TYPE.POLICY));
            }
            System.out.println(HEADER + buffer.toString());
        }


    }



    private void showHeader() {
        showLine();

        StringBuffer buffer = new StringBuffer();

        buffer.append("| ");

        List<String> headers = new ArrayList<String>();

        for (Service service : configurationData.getServices()) {
            if (null != service.getServiceId()) {
                headers.add(getColumn("SERVICE_ID", COLUMN_TYPE.CONTEXT));
            }

            if (null != service.getDescription()) {
                headers.add(getColumn("DESCRIPTION", COLUMN_TYPE.CONTEXT));
            }


            if (null != service.getPccRuleId()) {
                headers.add(getColumn("PCC_RULE_ID", COLUMN_TYPE.CONTEXT));
            }

            if (null != service.getPccRuleIdv6()) {
                headers.add(getColumn("PCC_RULE_IDv6", COLUMN_TYPE.CONTEXT));
            }

            // How to handle dynamic service who has no Pcc rule type
            headers.add(getColumn("PCC_RULE_TYPE", COLUMN_TYPE.CONTEXT));
            headers.add(getColumn("PRECEDENCE", COLUMN_TYPE.CONTEXT));

            // masc serviceId
            if (null != service.getMascServiceId()) {
                headers.add(getColumn("MASC_SERVICE_ID", COLUMN_TYPE.CONTEXT));
            }


            if (service.getFlowDescriptions().size() > 0) {
                headers.add(getColumn("FLOW_DESCRIPTION", COLUMN_TYPE.CONTEXT));
            }

            if (service.getServiceQulificationData().size() > 0) {
                headers.add(getColumn("QUALIFICATION", COLUMN_TYPE.POLICY));
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

        System.out.println(HEADER + buffer.toString());
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

        System.out.println(HEADER + bf.toString());
    }



    @Override
    public void getConfiguration(String fileName) {
        // TODO Auto-generated method stub

    }

}