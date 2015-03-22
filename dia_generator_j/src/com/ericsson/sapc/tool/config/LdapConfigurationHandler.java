package com.ericsson.sapc.tool.config;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ericsson.sapc.tool.config.pojo.Policy;
import com.ericsson.sapc.tool.config.pojo.PolicyLocator;
import com.ericsson.sapc.tool.config.pojo.Rule;

public class LdapConfigurationHandler implements ConfigurationHandler {
    private static String PATTERN_DN_CONTEXT    = "dn:[ ]*EPC-ContextName=";
    private static String PATTERN_DN_RESOURCE   = "";
    private static String PATTERN_DN_SUBJECT    = "";
    private static String PATTERN_DN_POLICY     = "dn:[ ]*EPC-PolicyId=";
    private static String PATTERN_DN_RULE       = "dn:[ ]*EPC-RuleId=";

    private static String PATTERN_EPC_GLOBAL    = "EPC-GlobalPolicyLocators";
    private static String PATTERN_EPC_POLICY_IDS= "EPC-PolicyIds:";
    private static String PATTERN_EPC_RULES     = "EPC-Rules:";
    
    private static String PATTERN_COMBINING_ALG = "EPC-RuleCombiningAlgorithm";
    private static String PATTERN_CONDITION     = "EPC-ConditionFormula:";
    private static String PATTERN_OUTPUT        = "EPC-OutputAttributes:Permit:";
    
    private static int COLUMN_LENTH_CONTEXT     = 15;
    private static int COLUMN_LENTH_POLICY      = 20;

    private enum COLUMN_TYPE {
        CONTEXT, POLICY
    }


    private static List<PolicyLocator> policies = new ArrayList<PolicyLocator>();

    @Override
    public void getConfiguration(String fileName) {
        LineNumberReader lineNumberReader = null;

        try {

            lineNumberReader = new LineNumberReader(new FileReader(fileName));
            getPolicyConfiguartion(fileName, lineNumberReader);

            /*
             * getSubscriberConfiguration
             * getSubscriberGroupConfiguration 
             * getServiceConfiguration
             * 
             */

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



    private void getPolicyConfiguartion(String fileName, LineNumberReader lineNumberReader) throws IOException {
        PolicyLocator policy;

        Matcher matcher_context = null;
        Pattern pattern_context = Pattern.compile(PATTERN_DN_CONTEXT);

        String line = "";

        while (lineNumberReader.ready()) {
            line = lineNumberReader.readLine();


            if (checkLine(line)) {

                matcher_context = pattern_context.matcher(line);

                if (matcher_context.find()) {
                    policy = new PolicyLocator();

                    getContextInfo(line, policy);

                    getPolicyDn(lineNumberReader.getLineNumber(), fileName, policy);

                    policies.add(policy);


                    System.out.println();

                }

            }

        }

        showPolicyConfiguration(policies);

    }



    private void getContextInfo(String line, PolicyLocator policyLocator) {
        String context = line.split(",")[0].trim().split("=")[1].trim();
        String resource = line.split(",")[1].trim().split("=")[1].trim();
        String subject = line.split(",")[2].trim().split("=")[1].trim();

        policyLocator.setContext(context);
        policyLocator.setResource(resource);

        if (PATTERN_EPC_GLOBAL.equals(subject)) {
            policyLocator.setSubject("Global");
        } else {
            policyLocator.setSubject(subject);
        }


    }


    private void getPolicyDn(int lineNumber, String fileName, PolicyLocator policyLocator) throws IOException {
        LineNumberReader lineNumberReader;
        lineNumberReader = new LineNumberReader(new FileReader(fileName));
        Policy policy;


        int counter = 0;
        Matcher matcher_policyId = null;
        Pattern pattern_policyId = Pattern.compile(PATTERN_EPC_POLICY_IDS);

        while (lineNumberReader.ready()) {

            String line = lineNumberReader.readLine();
            if (counter > lineNumber) {
                if (checkLine(line)) {
                    matcher_policyId = pattern_policyId.matcher(line);

                    // Think about one or more policies
                    if (matcher_policyId.find()) {
                        policy = new Policy();
                        policy.setPolicyId(line.split(":")[2].trim());
                        getPolicyInfo(fileName, policy);
                        policyLocator.getPolicies().add(policy);
                    }

                } else {
                    break;
                }

            }
            ++counter;

        }

    }



    private void getPolicyInfo(String fileName, Policy policy) throws IOException {

        LineNumberReader lineNumberReader;
        lineNumberReader = new LineNumberReader(new FileReader(fileName));
        Matcher matcher_policy = null;
        String regex = PATTERN_DN_POLICY + policy.getPolicyId();
        Pattern pattern_policy = Pattern.compile(regex);

        while (lineNumberReader.ready()) {

            String line = lineNumberReader.readLine();
            if (checkLine(line)) {
                matcher_policy = pattern_policy.matcher(line);

                // Think about one or more policies
                if (matcher_policy.find()) {
                    getPolicyDetailInfo(lineNumberReader.getLineNumber(), fileName, policy);
                }
            }

        }

    }

    private void getPolicyDetailInfo(int lineNumber, String fileName, Policy policy) throws IOException {
        LineNumberReader lineNumberReader;
        lineNumberReader = new LineNumberReader(new FileReader(fileName));
        Rule rule;


        int counter = 0;
        Matcher matcher_RuleId = null;
        Matcher matcher_RuleAlg = null;
        Pattern pattern_RuleId = Pattern.compile(PATTERN_EPC_RULES);
        Pattern pattern_RuleAlg = Pattern.compile(PATTERN_COMBINING_ALG);

        while (lineNumberReader.ready()) {

            String line = lineNumberReader.readLine();
            if (counter > lineNumber) {
                if (checkLine(line)) {
                    matcher_RuleId = pattern_RuleId.matcher(line);
                    matcher_RuleAlg = pattern_RuleAlg.matcher(line);

                    // Think about one or more rules
                    if (matcher_RuleId.find()) {
                        rule = new Rule();

                        rule.setRuleId(line.split(":")[2].trim());
                        getRuleInfo(fileName, rule);

                        policy.getRules().add(rule);
                    } else if (matcher_RuleAlg.find()) {
                        policy.setCombineAlgorithm(line.split(":")[1].trim());
                    }

                } else {
                    break;
                }

            }
            ++counter;

        }

        
    }



    private void getRuleInfo(String fileName, Rule rule) throws IOException {
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(fileName));
        Matcher matcher_policy = null;
        String regex = PATTERN_DN_RULE + rule.getRuleId();
        Pattern pattern_policy = Pattern.compile(regex);

        while (lineNumberReader.ready()) {

            String line = lineNumberReader.readLine();
            if (checkLine(line)) {
                matcher_policy = pattern_policy.matcher(line);

                // Think about one or more policies
                if (matcher_policy.find()) {
                    getRuleDetailInfo(lineNumberReader, fileName, rule);
                }
            }

        }

    }



    private void getRuleDetailInfo(LineNumberReader lineNumberReader, String fileName, Rule rule) throws IOException {
        int lineNumber = lineNumberReader.getLineNumber();
        lineNumberReader = new LineNumberReader(new FileReader(fileName));

        int counter = 0;
        Matcher matcher_condition = null;
        Matcher matcher_output = null;
        Pattern pattern_condition = Pattern.compile(PATTERN_CONDITION);
        Pattern pattern_output = Pattern.compile(PATTERN_OUTPUT);

        while (lineNumberReader.ready()) {

            String line = lineNumberReader.readLine();

            if (counter > lineNumber) {
                if (checkLine(line)) {
                    matcher_condition = pattern_condition.matcher(line);
                    matcher_output = pattern_output.matcher(line);

                    // Think about one or more output
                    if (matcher_condition.find()) {
                        rule.setCondition(line.split(":")[1].trim());
                    } else if (matcher_output.find()) {
                        rule.getOutputs().add(line.substring(PATTERN_OUTPUT.length(), line.length()).trim());
                    }

                } else {
                    break;
                }

            }
            ++counter;

        }

    }



    private boolean checkLine(String line) {
        return (null != line) && !("".equals(line.trim())) && !(line.trim().startsWith("#"));
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

}
