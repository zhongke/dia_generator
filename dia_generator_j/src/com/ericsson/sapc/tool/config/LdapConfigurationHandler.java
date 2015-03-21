package com.ericsson.sapc.tool.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LdapConfigurationHandler implements ConfigurationHandler {
    private static String PATTERN_DN_CONTEXT    = "dn:[ ]*EPC-ContextName=";
    private static String PATTERN_DN_RESOURCE   = "";
    private static String PATTERN_DN_SUBJECT    = "";
    private static String PATTERN_DN_POLICY     = "dn:[ ]*EPC-PolicyId=";
    private static String PATTERN_DN_RULE       = "dn:[ ]*EPC-RuleId=";

    private static String PATTERN_POLICY_IDS    = "EPC-PolicyIds:";
    private static String PATTERN_RULE_IDS      = "EPC-Rules:";
    
    private static String PATTERN_CONDITION     = "EPC-ConditionFormula:";
    private static String PATTERN_OUTPUT        = "EPC-OutputAttributes:Permit:";

    // private static String PATTERN_POLICY_IDS = "EPC-PolicyIds:";


    @Override
    public void getConfiguration(String fileName) {
        // TODO Auto-generated method stub
        // BufferedReader bufferedReader = null;
        LineNumberReader lineNumberReader = null;
        BufferedReader bufferedReader = null;

        try {

            lineNumberReader = new LineNumberReader(new FileReader(fileName));
            Matcher matcher_context = null;
            Pattern pattern_context = Pattern.compile(PATTERN_DN_CONTEXT);

            String line = "";

            while (lineNumberReader.ready()) {
                line = lineNumberReader.readLine();


                if (checkLine(line)) {

                    matcher_context = pattern_context.matcher(line);

                    if (matcher_context.find()) {

                        // System.out.println(line);
                        // System.out.println(lineNumberReader.getLineNumber());
                        getContextInfo(line);

                        getPolicyDn(lineNumberReader.getLineNumber(), fileName);

                        System.out.println();

                    }

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



    private void getContextInfo(String line) {
        String context = line.split(",")[0].trim().split("=")[1].trim();
        String resource = line.split(",")[1].trim().split("=")[1].trim();
        String subject = line.split(",")[2].trim().split("=")[1].trim();

        System.out.println("Context: " + context);
        System.out.println("Resource: " + resource);
        System.out.println("subject: " + subject);

    }


    private void getPolicyDn(int lineNumber, String fileName) throws IOException {
        LineNumberReader bufferedReader;
        bufferedReader = new LineNumberReader(new FileReader(fileName));


        int counter = 0;
        Matcher matcher_policyId = null;
        Pattern pattern_policyId = Pattern.compile(PATTERN_POLICY_IDS);

        while (bufferedReader.ready()) {

            String line = bufferedReader.readLine();
            if (counter > lineNumber) {
                if (checkLine(line)) {
                    matcher_policyId = pattern_policyId.matcher(line);

                    // Think about one or more policies
                    if (matcher_policyId.find()) {
                        System.out.println(line);
                        getPolicyInfo(fileName, line.split(":")[2].trim());
                    }

                } else {
                    break;
                }

            }
            ++counter;

        }

    }



    private void getPolicyInfo(String fileName, String policyName) throws IOException {

        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(fileName));
        Matcher matcher_policy = null;
        String regex = PATTERN_DN_POLICY + policyName;
        Pattern pattern_policy = Pattern.compile(regex);

        while (lineNumberReader.ready()) {

            String line = lineNumberReader.readLine();
            if (checkLine(line)) {
                matcher_policy = pattern_policy.matcher(line);

                // Think about one or more policies
                if (matcher_policy.find()) {
                    System.out.println(line);
                    getPolicyDetailInfo(lineNumberReader.getLineNumber(), fileName);
                }
            }

        }

    }

    private void getPolicyDetailInfo(int lineNumber, String fileName) throws IOException {
        LineNumberReader bufferedReader;
        bufferedReader = new LineNumberReader(new FileReader(fileName));


        int counter = 0;
        Matcher matcher_RuleId = null;
        Pattern pattern_RuleId = Pattern.compile(PATTERN_RULE_IDS);

        while (bufferedReader.ready()) {

            String line = bufferedReader.readLine();
            if (counter > lineNumber) {
                if (checkLine(line)) {
                    matcher_RuleId = pattern_RuleId.matcher(line);

                    // Think about one or more policies
                    if (matcher_RuleId.find()) {
                        System.out.println(line);
                        getRuleInfo(fileName, line.split(":")[2].trim());
                    }

                } else {
                    break;
                }

            }
            ++counter;

        }

        
    }



    private void getRuleInfo(String fileName, String ruleName) throws IOException {
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(fileName));
        Matcher matcher_policy = null;
        String regex = PATTERN_DN_RULE + ruleName;
        Pattern pattern_policy = Pattern.compile(regex);

        while (lineNumberReader.ready()) {

            String line = lineNumberReader.readLine();
            if (checkLine(line)) {
                matcher_policy = pattern_policy.matcher(line);

                // Think about one or more policies
                if (matcher_policy.find()) {
                    System.out.println(line);
                    getRuleDetailInfo(lineNumberReader.getLineNumber(), fileName);
                }
            }

        }

    }



    private void getRuleDetailInfo(int lineNumber, String fileName) throws IOException {
        LineNumberReader bufferedReader;
        bufferedReader = new LineNumberReader(new FileReader(fileName));


        int counter = 0;
        Matcher matcher_condition = null;
        Matcher matcher_output = null;
        Pattern pattern_condition = Pattern.compile(PATTERN_CONDITION);
        Pattern pattern_output = Pattern.compile(PATTERN_OUTPUT);

        while (bufferedReader.ready()) {

            String line = bufferedReader.readLine();
            if (counter > lineNumber) {
                if (checkLine(line)) {
                    matcher_condition = pattern_condition.matcher(line);
                    matcher_output = pattern_output.matcher(line);

                    // Think about one or more output
                    if (matcher_condition.find()) {
                        System.out.println(line.split(":")[1].trim());
                    } else if (matcher_output.find()) {
                        System.out.println(line.substring(PATTERN_OUTPUT.length(), line.length()).trim());
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

}
