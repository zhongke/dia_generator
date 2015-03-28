package com.e.s.tool.config;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;

import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;
import com.e.s.tool.config.pojo.Policy;
import com.e.s.tool.config.pojo.PolicyLocator;
import com.e.s.tool.config.pojo.Rule;
import com.e.s.tool.config.pojo.Subscriber;
import com.e.s.tool.config.pojo.SubscriberGroup;

public class LdapConfigurationHandler implements ConfigurationHandler {

    private static String PATTERN_DN_CONTEXT        = "dn:EPC-ContextName";
    private static String PATTERN_DN_POLICY         = "dn:EPC-PolicyId=";
    private static String PATTERN_DN_RULE           = "dn:EPC-RuleId=";

    private static String PATTERN_DN_SUB            = "dn:EPC-SubscriberId=";
    private static String PATTERN_DN_SUB_QUALIFY    = "dn:EPC-Name=EPC-SubscriberQualification,EPC-SubscriberId=";
    private static String PATTERN_DN_SUB_GROUP      = "dn:EPC-SubscriberGroupId=";
    private static String PATTERN_DN_FAMILY         = "dn:EPC-FamilyId=";
    private static String PATTERN_DN_SERVICE        = "dn:EPC-ServiceId=";
    private static String PATTERN_DN_PCC_RULE       = "dn:EPC-PccRuleName=EPC-PccRule,EPC-ServiceId=";
    private static String PATTERN_DN_SERVICE_QUALIFY= "dn:EPC-ServiceQualificationName=EPC-ServiceQualification,EPC-ServiceId=";

    private static String PATTERN_EPC_GLOBAL        = "EPC-GlobalPolicyLocators";
    private static String PATTERN_EPC_POLICY_IDS    = "EPC-PolicyIds";
    private static String PATTERN_EPC_RULES         = "EPC-Rules";

    private static String PATTERN_COMBINING_ALG     = "EPC-RuleCombiningAlgorithm";
    private static String PATTERN_CONDITION         = "EPC-ConditionFormula";
    private static String PATTERN_OUTPUT            = "EPC-OutputAttributes";
    private static String PATTERN_PERMIT            = ":Permit:";


    private static String PATTERN_DN_TRIGGER        = "dn:EPC-BearerEventsNotificationName=EPC-BearerEventConfig,EPC-AccessPolicyChargingControlName=EPC-AccessPolicyChargingControl,EPC-ConfigContainerName=EPC-EpcConfigCont,applicationName=EPC-EpcNode,nodeName=jambala";
    
    private static String PATTERN_TRIGGER           = "EPC-EventTriggers";


    private static int COLUMN_LENTH_CONTEXT = 15;
    private static int COLUMN_LENTH_POLICY  = 20;

    private LdapTree tree = null;
    private ConfigurationData configurationData;

    public LdapConfigurationHandler() {
        tree = new LdapTree();
        configurationData = new ConfigurationData();
    }



    private enum COLUMN_TYPE {
        CONTEXT, POLICY
    }


    static {
    }

    @Override
    public void getConfiguration(String fileName) {
        constructLdapTree(fileName);

        /*
         * getPolicyConfiguration
         * - Context
         * - Resource
         * - Subject
         * - Policy
         * - Rule
         * 
         */
        getPolicyConfiguartion();

        showPolicyConfiguration(configurationData.getPolicyLocators());

        /*
         * getSubscriberConfiguration
         * - Subscriber
         * - SubscriberQualification
         * 
         */
        getSubscriberConfiguration();

        /*
         * getSubscriberGroupConfiguration 
         * - SubscriberGroup
         * 
         */
        getSuscriberGroupConfiguration();

        /*
         * getServiceConfiguration
         * - Service
         * - PccRule
         * - ServiceQualification
         * 
         */
        getServiceConfiguration();

        /*
         * getEventTrigger
         */



    }



    private void constructLdapTree(String fileName) {
        // Construct Tree model
        LineNumberReader lineNumberReader = null;


        try {

            lineNumberReader = new LineNumberReader(new FileReader(fileName));

            String line = "";

            Node node = new Node();

            while (lineNumberReader.ready()) {
                line = lineNumberReader.readLine();

                if (checkLine(line)) {
                    line = cleanWhiteSpace(line);

                    if (line.startsWith("dn:")) {

                        node.setNodeDn(line);
                        tree.setParent(tree.getNodes());

                        tree.getNodes().add(node);
                    } else {
                        node.getAttributes().add(line);
                    }

                } else if (null == line || line.trim().equals("")) {
                    node = new Node();
                    continue;
                }

            }

        } catch (IOException e) {
            System.out.println("File does not exist");
            e.printStackTrace();
        } finally {
            try {
                if (null != lineNumberReader) {
                    lineNumberReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void getSuscriberGroupConfiguration() {
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

        for (SubscriberGroup group : configurationData.getSubscriberGroups()) {
            System.out.println(group);
        }

    }


    private void getServiceConfiguration() {
        // TODO Auto-generated method stub

    }


    /*
     * TODO: How to set different attributes by reflection?
     */
    private void getSubscriberConfiguration() {
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

        for (Subscriber sub : configurationData.getSubscribers()) {
            System.out.println(sub);
        }

    }

    /*
     * TODO: How to handle the dn in the several lines 
     * TODO: How to handle the conditionFormula in the several lines
     */

    private void getPolicyConfiguartion() {
        PolicyLocator policyLocator = null;

        for (Node node : tree.getNodes()) {

            if (PATTERN_DN_CONTEXT.equals(node.getDn().split(",")[0].split("=")[0])) {

                policyLocator = new PolicyLocator();

                getContextInfo(node, policyLocator);

                getPolicyDn(node, policyLocator);

                configurationData.getPolicyLocators().add(policyLocator);


                System.out.println();

            }

        }

    }



    private void getContextInfo(Node node, PolicyLocator policyLocator) {
        String context = node.getDn().split(":")[1].split(",")[0].trim().split("=")[1].trim();
        String resource = node.getDn().split(":")[1].split(",")[1].trim().split("=")[1].trim();
        String subject = node.getDn().split(":")[1].split(",")[2].trim().split("=")[1].trim();

        policyLocator.setContext(context);
        policyLocator.setResource(resource);

        if (PATTERN_EPC_GLOBAL.equals(subject)) {
            policyLocator.setSubject("Global");
        } else {
            policyLocator.setSubject(subject);
        }

    }


    private void getPolicyDn(Node node, PolicyLocator policyLocator) {

        Policy policy = null;
        String attributeName = "";

        for (String attribute : node.getAttributes()) {
            attributeName = attribute.split(":")[0];
            if (attributeName.equals(PATTERN_EPC_POLICY_IDS)) {

                policy = new Policy();
                policy.setPolicyId(attribute.split(":")[2]);

                getPolicyInfo(policy);

                policyLocator.getPolicies().add(policy);

            }

        }

    }



    private void getPolicyInfo(Policy policy) {
        String regex = PATTERN_DN_POLICY + policy.getPolicyId();
        String attributeName = "";
        Rule rule = null;
        for (Node node : tree.getNodes()) {

            if (regex.equals(node.getDn().split(",")[0])) {

                for (String attribute : node.getAttributes()) {

                    attributeName = attribute.split(":")[0];
                    if (attributeName.equals(PATTERN_EPC_RULES)) {

                        rule = new Rule();

                        rule.setRuleId(attribute.split(":")[2].trim());
                        getRuleInfo(rule);

                        policy.getRules().add(rule);
                    } else if (attributeName.equals(PATTERN_COMBINING_ALG)) {
                        policy.setCombineAlgorithm(attribute.split(":")[1].trim());
                    }
                }
            }
        }
    }


    private void getRuleInfo(Rule rule) {

        String regex = PATTERN_DN_RULE + rule.getRuleId();
        String attributeName = "";
        for (Node node : tree.getNodes()) {

            if (regex.equals(node.getDn().split(",")[0])) {
                for (String attribute : node.getAttributes()) {
                    attributeName = attribute.split(":")[0];
                    if (attributeName.equals(PATTERN_CONDITION)) {

                        rule.setCondition(attribute.split(":")[1].trim());

                    } else if (attributeName.equals(PATTERN_OUTPUT)) {
                        rule.getOutputs().add(
                                attribute.substring(PATTERN_OUTPUT.length() + PATTERN_PERMIT.length(),
                                        attribute.length()));
                    }
                }
            }
        }
    }



    private void showPolicyConfiguration(List<PolicyLocator> policies) {

        showHeader();

        for (PolicyLocator policyLocator : policies) {
            StringBuffer buffer = new StringBuffer();
            StringBuffer policyBuffer = new StringBuffer();
            StringBuffer ruleBuffer = new StringBuffer();

            buffer.append("| ");

            buffer.append(getColumn(policyLocator.getContext(), COLUMN_TYPE.CONTEXT));
            buffer.append(getColumn(policyLocator.getResource(), COLUMN_TYPE.CONTEXT));
            buffer.append(getColumn(policyLocator.getSubject(), COLUMN_TYPE.CONTEXT));

            // Policy

            for (int i = 0; i < policyLocator.getPolicies().size(); ++i) {
                StringBuffer tempBuffer = new StringBuffer();
                Policy policy = policyLocator.getPolicies().get(i);
                tempBuffer.append(getColumn(policy.getPolicyId(), COLUMN_TYPE.POLICY));
                if (0 == i) {
                    buffer.append(tempBuffer);
                } else {
                    int length = 0;
                    while (length < (COLUMN_LENTH_CONTEXT * 3 + 6)) {
                        policyBuffer.append(" ");
                        ++length;

                    }
                    policyBuffer.append(tempBuffer);
                }

                // Rule
                for (int j = 0; j < policy.getRules().size(); ++j) {
                    tempBuffer = new StringBuffer();
                    Rule rule = policy.getRules().get(j);
                    tempBuffer.append(getColumn(rule.getRuleId(), COLUMN_TYPE.POLICY));
                    if (i == 0 && j == 0) {
                        buffer.append(tempBuffer);
                        buffer.append(getColumn(rule.getCondition(), COLUMN_TYPE.POLICY));
                        System.out.println(buffer.toString());

                    } else if (i > 0 || j == 0) {
                        policyBuffer.append(tempBuffer);
                        policyBuffer.append(getColumn(rule.getCondition(), COLUMN_TYPE.POLICY));
                        System.out.println(policyBuffer.toString());

                    } else {
                        int length = 0;
                        while (length < (COLUMN_LENTH_CONTEXT * 2 + COLUMN_LENTH_POLICY * 2 + 5)) {
                            ruleBuffer.append(" ");
                            ++length;

                        }
                        ruleBuffer.append(tempBuffer);
                        ruleBuffer.append(getColumn(rule.getCondition(), COLUMN_TYPE.POLICY));
                        System.out.println(ruleBuffer.toString());

                    }
                    showOutput(rule);
                }
            }
            showLine();
        }
    }



    private void showOutput(Rule rule) {
        StringBuffer buffer;
        if (0 != rule.getOutputs().size()) {
            for (int i = 0; i < rule.getOutputs().size(); ++i) {
                buffer = new StringBuffer();
                int length = 0;
                while (length < (COLUMN_LENTH_CONTEXT * 3 + COLUMN_LENTH_POLICY * 2 + 12)) {
                    buffer.append(" ");
                    ++length;

                }
                buffer.append(getColumn(rule.getOutputs().get(i), COLUMN_TYPE.POLICY));
                System.out.println(buffer.toString());
            }
        }
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


    private void showHeader() {
        showLine();

        StringBuffer buffer = new StringBuffer();

        buffer.append("| ");

        buffer.append(getColumn("CONTEXT", COLUMN_TYPE.CONTEXT));
        buffer.append(getColumn("RESOURCE", COLUMN_TYPE.CONTEXT));
        buffer.append(getColumn("SUBJECT", COLUMN_TYPE.CONTEXT));
        buffer.append(getColumn("POLICY", COLUMN_TYPE.POLICY));
        // buffer.append(getColumn("CombiningAlgrithm", COLUMN_TYPE.POLICY));
        buffer.append(getColumn("RULE", COLUMN_TYPE.POLICY));
        buffer.append(getColumn("CONDITION", COLUMN_TYPE.POLICY));
        buffer.append(getColumn("OUTPUT", COLUMN_TYPE.POLICY));

        System.out.println(buffer.toString());

        showLine();

    }



    private void showLine() {
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < COLUMN_LENTH_CONTEXT * 3 + COLUMN_LENTH_POLICY * 4 + 15; ++i) {
            bf.append("-");

        }

        System.out.println(bf.toString());
    }



    private String cleanWhiteSpace(String line) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < line.length(); ++i) {
            if (line.charAt(i) != ' ') {
                buffer.append(line.charAt(i));
            }
        }
        return buffer.toString();
    }



    private boolean checkLine(String line) {
        return (null != line) && !("".equals(line.trim())) && !(line.trim().startsWith("#"));
    }

}
