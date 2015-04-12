package com.e.s.tool.config.handler;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;

public class LdapConfigurationHandler implements ConfigurationHandler {

    private static String PATTERN_DN_FAMILY         = "dn:EPC-FamilyId=";

    private static String PATTERN_DN_TRIGGER        = "dn:EPC-BearerEventsNotificationName=EPC-BearerEventConfig,EPC-AccessPolicyChargingControlName=EPC-AccessPolicyChargingControl,EPC-ConfigContainerName=EPC-EpcConfigCont,applicationName=EPC-EpcNode,nodeName=jambala";
    
    private static String PATTERN_TRIGGER           = "EPC-EventTriggers";

    private static String HEADER = "*       + ";
    private LdapTree tree;
    private ConfigurationData configurationData;

    public LdapConfigurationHandler() {
        tree = new LdapTree();
        configurationData = new ConfigurationData();

    }

    @Override
    public void getConfiguration(String fileName) {

        /*
         * Construct LdapTree model from ldif file
         */
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
//        System.out.print(HEADER);
//        System.out.print("POLICY");
//        System.out.println(" +");
//        new PolicyHandler(tree, configurationData).getConfiguration();
//        System.out.println("*");

        /*
         * getSubscriberConfiguration
         * - Subscriber
         * - SubscriberQualification
         * 
         */
        System.out.print(HEADER);
        System.out.print("SUBSCRIBER");
        System.out.println(" +");
        new SubscriberHanlder(tree, configurationData).getConfiguration();
        System.out.println("*");
        /*
         * getSubscriberGroupConfiguration 
         * - SubscriberGroup
         * 
         */
        System.out.print(HEADER);
        System.out.print("GROUP");
        System.out.println(" +");
        new SubscriberGroupHanlder(tree, configurationData).getConfiguration();
        System.out.println("*");
        /*
         * getServiceConfiguration
         * - Service
         * - PccRule
         * - ServiceQualification
         * 
         */
        System.out.print(HEADER);
        System.out.print("SERVICE");
        System.out.println(" +");
        new ServiceHandler(tree, configurationData).getConfiguration();
        System.out.println("*");
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

    @Override
    public void getConfiguration() {
        // TODO Auto-generated method stub

    }

}
