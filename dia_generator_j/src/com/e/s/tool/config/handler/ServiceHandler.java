package com.e.s.tool.config.handler;

import java.util.List;

import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;
import com.e.s.tool.config.pojo.Service;

public class ServiceHandler extends AbstractConfigurationHandler<Service> {
    private static String PATTERN_DN_SERVICE = "dn:EPC-ServiceId=";
    private static String PATTERN_DN_PCC_RULE = "dn:EPC-PccRuleName=EPC-PccRule,EPC-ServiceId=";
    private static String PATTERN_DN_SERVICE_QUALIFY =
            "dn:EPC-ServiceQualificationName=EPC-ServiceQualification,EPC-ServiceId=";

    List<String[]> attributeLineList;

    public LdapTree tree;
    public ConfigurationData configurationData;

    public ServiceHandler(LdapTree tree, ConfigurationData configurationData) {
        this.tree = tree;
        this.configurationData = configurationData;
    }

    @Override
    public void getConfiguration() {
        boolean existed = false;
        Service service = null;
        String attributeName = "";
        String serviceId = "";
        Node node = null;


        for (int i = 0; i < tree.getNodes().size(); ++i) {
            node = tree.getNodes().get(i);
            if (node.getDn().startsWith(PATTERN_DN_SERVICE)) {
                existed = true;
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
                                service.getServiceQulificationData()
                                        .add(attribute.substring(attribute.indexOf(':') + 1, attribute.length()));
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
                                service.getFlowDescriptions()
                                        .add(attribute.substring(attribute.indexOf(':') + 1, attribute.length()));
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

        if (existed) {
            System.out.print("*       + ");
            System.out.print("SERVICE");
            System.out.println(" +");

            showConfiguration(service, configurationData.getServices());

            System.out.println("*");
        }

    }



    @Override
    public void getConfiguration(String fileName) {
        // TODO Auto-generated method stub

    }

}
