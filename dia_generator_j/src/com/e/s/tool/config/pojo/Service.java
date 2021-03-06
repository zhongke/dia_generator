package com.e.s.tool.config.pojo;

import java.util.ArrayList;
import java.util.List;

public class Service extends DataObject {
    /*
     * EPC-ServiceId EPC-ServiceQualificationName EPC-PccRuleName
     */

    public static String PATTERN_EPC_SERVICE_DESCRIPTION = "EPC-ServiceDescription";
    public static String PATTERN_EPC_MASC_SERVICE_ID = "EPC-MascServiceId";

    public static String PATTERN_EPC_PCC_RULE_ID = "EPC-PccRuleId";
    public static String PATTERN_EPC_PCC_RULE_ID_V6 = "EPC-PccRuleIdv6";
    public static String PATTERN_EPC_RULE_TYPE = "EPC-RuleType";
    public static String PATTERN_EPC_FLOW_DESCRIPTIONS = "EPC-FlowDescriptions";
    public static String PATTERN_EPC_PRECEDENCE = "EPC-Precedence";

    public static String PATTERN_EPC_SERVICE_QUALIFICATION_DATA = "EPC-ServiceQualificationData";

    public static String PATTERN_EPC_MASC_PACKAGE_NAMES = "EPC-MascPackageNames";

    private static List<String> attributeList;

    static {
        attributeList = new ArrayList<String>();
        attributeList.add("serviceId:SERVICE_ID");
        // attributeList.add("description:DESCRIPTION");
        attributeList.add("pccRuleId:PCC_RULE_ID");
        attributeList.add("pccRuleIdv6:PCC_RULE_IDv6");
        attributeList.add("pccRuleType:PCC_RULE_TYPE");
        attributeList.add("mascServiceId:MASC_SERVICE_ID");
        attributeList.add("precedence:PRECEDENCE");
        attributeList.add("flowDescriptions:FLOW_DESCRIPTION");
        attributeList.add("serviceQulificationData:QUALIFICATION");
    }

    private String serviceId;
    private String description;
    private String pccRuleId;
    private String pccRuleIdv6;
    private String pccRuleType;
    private String mascServiceId;
    private String precedence;

    private List<String> flowDescriptions;
    private List<String> serviceQulificationData;



    public Service() {


        this.flowDescriptions = new ArrayList<String>();
        this.serviceQulificationData = new ArrayList<String>();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMascServiceId() {
        return mascServiceId;
    }

    public void setMascServiceId(String mascServiceId) {
        this.mascServiceId = mascServiceId;
    }


    public String getPccRuleId() {
        return pccRuleId;
    }

    public void setPccRuleId(String pccRuleId) {
        this.pccRuleId = pccRuleId;
    }

    public String getPccRuleIdv6() {
        return pccRuleIdv6;
    }

    public void setPccRuleIdv6(String pccRuleIdv6) {
        this.pccRuleIdv6 = pccRuleIdv6;
    }

    public String getPccRuleType() {
        return pccRuleType;
    }

    public void setPccRuleType(String pccRuleType) {
        this.pccRuleType = pccRuleType;
    }

    public List<String> getFlowDescriptions() {
        return flowDescriptions;
    }

    public void setFlowDescriptions(List<String> flowDescriptions) {
        this.flowDescriptions = flowDescriptions;
    }

    public String getPrecedence() {
        return precedence;
    }

    public void setPrecedence(String precedence) {
        this.precedence = precedence;
    }

    public List<String> getServiceQulificationData() {
        return serviceQulificationData;
    }

    public void setServiceQulificationData(List<String> serviceQulificationData) {
        this.serviceQulificationData = serviceQulificationData;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();


        buffer.append("Service [serviceId=");
        buffer.append(serviceId);
        buffer.append(", description=");
        buffer.append(description);
        buffer.append(", mascServiceId=");
        buffer.append(mascServiceId);
        buffer.append(", pccRuleId=");
        buffer.append(pccRuleId);
        buffer.append(", pccRuleIdv6=");
        buffer.append(pccRuleIdv6);
        buffer.append(", pccRuleType=");
        buffer.append(pccRuleType);

        for (String flowDescription : flowDescriptions) {

            buffer.append(", flowDescription=");
            buffer.append(flowDescription);
        }
        buffer.append(", precedence=");
        buffer.append(precedence);

        for (String serviceQulification : serviceQulificationData) {

            buffer.append(", serviceQulificationData=");
            buffer.append(serviceQulification);
        }
        buffer.append("]");

        return buffer.toString();
    }

    public List<String> getAttributeList() {
        return attributeList;
    }

}
