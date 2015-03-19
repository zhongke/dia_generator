package com.ericsson.sapc.tool.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LdapConfigurationHandler implements ConfigurationHandler {
    private static String PATTERN_DN_CONTEXT = "dn:EPC-ContextName=";
    private static String PATTERN_DN_RESOURCE   = "";
    private static String PATTERN_DN_SUBJECT    = "";
    
    private static String PATTERN_POLICY_IDS = "EPC-PolicyIds:";


    @Override
    public void getConfiguration(String fileName) {
        // TODO Auto-generated method stub
        BufferedReader bufferedReader = null;

        try {

            bufferedReader = new BufferedReader(new FileReader(fileName));
            Matcher matcher_context = null;
            Pattern pattern_context = Pattern.compile(PATTERN_DN_CONTEXT);

            String line = "";

            while (bufferedReader.ready()) {
                line = bufferedReader.readLine();

                if (null != line && !("".equals(line.trim())) && !(line.trim().startsWith("#"))) {

                    matcher_context = pattern_context.matcher(line);

                    if (matcher_context.find()) {

                        // String str_context = matcher_context.group(0);
                        System.out.println(line);
                    }



                }


            }

        } catch (IOException e) {
            System.out.println("File does not exist");
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void getContext() {

    }

}
