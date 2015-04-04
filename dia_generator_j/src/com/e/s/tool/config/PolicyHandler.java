package com.e.s.tool.config;

import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;
import com.e.s.tool.config.pojo.Policy;
import com.e.s.tool.config.pojo.PolicyLocator;
import com.e.s.tool.config.pojo.Rule;

public class PolicyHandler implements ConfigurationHandler {
    private static String PATTERN_DN_CONTEXT        = "dn:EPC-ContextName";
    private static String PATTERN_DN_POLICY         = "dn:EPC-PolicyId=";
    private static String PATTERN_DN_RULE           = "dn:EPC-RuleId=";

    private static String PATTERN_EPC_GLOBAL        = "EPC-GlobalPolicyLocators";
    private static String PATTERN_EPC_POLICY_IDS    = "EPC-PolicyIds";
    private static String PATTERN_EPC_RULES         = "EPC-Rules";

    private static String PATTERN_COMBINING_ALG     = "EPC-RuleCombiningAlgorithm";
    private static String PATTERN_CONDITION         = "EPC-ConditionFormula";
    private static String PATTERN_OUTPUT            = "EPC-OutputAttributes";
    private static String PATTERN_PERMIT            = ":Permit:";
 
    private static String HEADER = "*       ";

    private enum COLUMN_TYPE {
        CONTEXT, POLICY
    }


    private static int COLUMN_LENTH_CONTEXT = 15;
    private static int COLUMN_LENTH_POLICY = 20;

    private LdapTree tree;
    private ConfigurationData configurationData;

    public PolicyHandler(LdapTree tree, ConfigurationData configurationData) {
        this.tree = tree;
        this.configurationData = configurationData;
    }


    /*
     * TODO: How to handle the dn in the several lines 
     * TODO: How to handle the conditionFormula in the several lines
     */
    @Override
    public void getConfiguration() {
        PolicyLocator policyLocator = null;

        for (Node node : tree.getNodes()) {

            if (PATTERN_DN_CONTEXT.equals(node.getDn().split(",")[0].split("=")[0])) {

                policyLocator = new PolicyLocator();

                getContextInfo(node, policyLocator);

                getPolicyDn(node, policyLocator);

                configurationData.getPolicyLocators().add(policyLocator);


            }

        }
        showPolicyConfiguration();

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



    private void showPolicyConfiguration() {

        showHeader();
        StringBuffer buffer = null;
        StringBuffer policyBuffer = null;
        StringBuffer ruleBuffer = null;
        StringBuffer tempBuffer = null;

        Policy policy = null;
        Rule rule = null;

        for (PolicyLocator policyLocator : configurationData.getPolicyLocators()) {
            buffer = new StringBuffer();
            policyBuffer = new StringBuffer();
            ruleBuffer = new StringBuffer();

            buffer.append("| ");

            buffer.append(getColumn(policyLocator.getContext(), COLUMN_TYPE.CONTEXT));
            buffer.append(getColumn(policyLocator.getResource(), COLUMN_TYPE.CONTEXT));
            buffer.append(getColumn(policyLocator.getSubject(), COLUMN_TYPE.CONTEXT));

            // Policy

            for (int i = 0; i < policyLocator.getPolicies().size(); ++i) {
                tempBuffer = new StringBuffer();
                policy = policyLocator.getPolicies().get(i);
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
                    rule = policy.getRules().get(j);
                    tempBuffer.append(getColumn(rule.getRuleId(), COLUMN_TYPE.POLICY));
                    if (i == 0 && j == 0) {
                        buffer.append(tempBuffer);
                        buffer.append(getColumn(rule.getCondition(), COLUMN_TYPE.POLICY));

                    } else if (i > 0 || j == 0) {
                        policyBuffer.append(tempBuffer);
                        policyBuffer.append(getColumn(rule.getCondition(), COLUMN_TYPE.POLICY));
                        buffer = policyBuffer;

                    } else {
                        int length = 0;
                        while (length < (COLUMN_LENTH_CONTEXT * 2 + COLUMN_LENTH_POLICY * 2 + 5)) {
                            ruleBuffer.append(" ");
                            ++length;

                        }
                        ruleBuffer.append(tempBuffer);
                        ruleBuffer.append(getColumn(rule.getCondition(), COLUMN_TYPE.POLICY));
                        buffer = ruleBuffer;

                    }

                    System.out.println(HEADER + buffer.toString());
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
                System.out.println(HEADER + buffer.toString());
            }
        }
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