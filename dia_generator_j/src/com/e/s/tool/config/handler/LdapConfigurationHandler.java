package com.e.s.tool.config.handler;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import com.e.s.tool.config.pojo.ConfigurationData;
import com.e.s.tool.config.pojo.LdapTree;
import com.e.s.tool.config.pojo.Node;

public class LdapConfigurationHandler implements ConfigurationHandler {

    private static String PATTERN_DN_FAMILY = "dn:EPC-FamilyId=";

    private static String PATTERN_DN_TRIGGER =
            "dn:EPC-BearerEventsNotificationName=EPC-BearerEventConfig,EPC-AccessPolicyChargingControlName=EPC-AccessPolicyChargingControl,EPC-ConfigContainerName=EPC-EpcConfigCont,applicationName=EPC-EpcNode,nodeName=jambala";

    private static String PATTERN_TRIGGER = "EPC-EventTriggers";

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
        showLdapTree();

        /*
         * getPolicyConfiguration - Context - Resource - Subject - Policy - Rule
         *
         */
        new PolicyHandler(tree, configurationData).getConfiguration();

        /*
         * getSubscriberConfiguration - Subscriber - SubscriberQualification
         *
         */
        new SubscriberHandler(tree, configurationData).getConfiguration();

        /*
         * getSubscriberGroupConfiguration - SubscriberGroup
         *
         */
        new SubscriberGroupHandler(tree, configurationData).getConfiguration();

        /*
         * getServiceConfiguration - Service - PccRule - ServiceQualification
         *
         */
        new ServiceHandler(tree, configurationData).getConfiguration();

        /*
         * getEventTrigger
         */



    }

    private void showLdapTree() {
        for (Node node : tree.getNodes()) {
            System.out.println("------------------------------------");
            System.out.println(node.getDn());
            for (String attr : node.getAttributes()) {
                System.out.println(attr);
            }
            System.out.println("------------------------------------");
        }
    }



    private void constructLdapTree(String fileName) {
        // Construct Tree model
        LineNumberReader lineNumberReader = null;


        try {

            lineNumberReader = new LineNumberReader(new FileReader(fileName));

            String line;
            String cleanLine;
            StringBuffer sbDn = new StringBuffer();
            StringBuffer sbAttr = new StringBuffer();
            int newAttrCounter = 0;

            boolean isSameDn = false;
            boolean isSameAttribute = false;
            String currentLine = null;

            Node node = new Node();

            while (lineNumberReader.ready()) {
                line = lineNumberReader.readLine();
                // System.out.println(line);

                if (checkLine(line)) {
                    cleanLine = cleanWhiteSpace(line);

                    if (cleanWhiteSpace(line).startsWith("dn:")) {
                        sbDn = new StringBuffer();
                        sbDn.append(cleanLine);
                        isSameDn = true;
                        isSameAttribute = false;

                    } else if (line.startsWith(" ")) {
                        if (isSameDn) {
                            sbDn.append(cleanLine);
                            isSameAttribute = false;
                        } else if (isSameAttribute) {
                            sbAttr.append(cleanLine);
                            isSameAttribute = true;
                            newAttrCounter++;
                        }

                    } else {
                        if (0 == newAttrCounter) {
                            sbAttr = new StringBuffer();
                            sbAttr.append(cleanLine);
                            if (null != currentLine) {
                                node.getAttributes().add(currentLine);
                                currentLine = null;

                            }

                            // System.out.println(cleanLine);
                            isSameAttribute = true;

                        } else if (1 <= newAttrCounter) {
                            node.getAttributes().add(sbAttr.toString());
                            // System.out.println(sbAttr.toString());

                            newAttrCounter = -1;
                            isSameAttribute = true;
                            currentLine = cleanLine;
                            sbAttr = new StringBuffer();
                            sbAttr.append(currentLine);
                        }

                        isSameDn = false;
                        isSameAttribute = true;


                        newAttrCounter++;
                    }

                } else if (null == line || line.trim().equals("")) {
                    if (null != sbDn) {

                        node.setNodeDn(sbDn.toString());
                        tree.setParent(tree.getNodes());
                        tree.getNodes().add(node);
                    }
                    if (null != currentLine) {
                        node.getAttributes().add(sbAttr.toString());
                    }

                    node = new Node();
                    isSameDn = false;
                    isSameAttribute = false;
                    newAttrCounter = 0;
                    currentLine = null;
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
