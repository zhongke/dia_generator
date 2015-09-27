package com.e.s.tool;

import java.util.ArrayList;
import java.util.List;

import com.e.s.tool.config.handler.ConfigurationHandler;
import com.e.s.tool.config.handler.LdapConfigurationHandler;


public class Generator {

    private static List<String> files = new ArrayList<String>();

    private static String DIR = "/Users/Kevinzhong/ttcn/script/";
    private static String file_01 = DIR + "tc_01_UL_Volume_Limit_Change_for_Subscriber_GxR8.ttcn";
    private static String file_02 = DIR + "tc_02_02_01_01_Services_authorized_by_UserLocation.ttcn";
    private static String file_03 = DIR + "tc_02_15_01_BasicUseCase.ttcn";
    private static String file_04 = DIR + "tc_03_01_01_AccumulationSession_and_QosSession_interference.ttcn";
    private static String file_05 = DIR + "tc_03_Surpassed_Subscriber_Volume_Limit_Triggers_Reauth_2SASN_NAT.ttcn";
    private static String file_06 = DIR + "tc_04_02_Reset_Period_2GGSN_gxr9.ttcn";
    private static String file_07 = DIR + "tc_04_FD_Prepaid_and_Postpaid_Shared_Subscriber_Plans_GxR9.ttcn";
    private static String file_08 = DIR + "tc_05_01_02_SessionReauth_By_PRA_status_Update.ttcn";
    private static String file_09 = DIR + "tc_05_threegpp_user_location_info_Based_Policy_for_SAI.ttcn";
    private static String file_10 = DIR + "tc_13_simple.ttcn";
    private static String file_11 = DIR + "tc_14_04_DynamicFlowDirection_Ipv4.ttcn";
    private static String file_12 = DIR + "tc_14_simpleGx_r8.ttcn";
    private static String file_13 = DIR + "tc_authenticationAuthorization_07.ttcn";

    static {
        files.add(file_01);
        files.add(file_02);
        files.add(file_03);
        files.add(file_04);
        files.add(file_05);
        files.add(file_06);
        files.add(file_07);
        files.add(file_08);
        files.add(file_09);
        files.add(file_10);
        files.add(file_11);
        files.add(file_12);
        files.add(file_13);

    }

    public static void main(String[] args) {
        /*
         * for (String file : files) { System.out.println(file);
         * 
         * BufferMgr buffMgr = new BufferMgr(); buffMgr.readInputFromFile(file);
         * 
         * buffMgr.showDiagramFromBuffer(); buffMgr.showMessageFromBuffer(); }
         */

        if (args.length > 0) {

            for (String file : args) {
                 BufferMgr buffMgr = new BufferMgr();
                 buffMgr.readInputFromFile(file + ".ttcn");
                
                 buffMgr.showDiagramFromBuffer();
                 buffMgr.showMessageFromBuffer();

                ConfigurationHandler configurationHandler = new LdapConfigurationHandler();
                configurationHandler.getConfiguration(file + ".ldif");

            }

        } else {
            System.out.println("Please input the fileName!");
        }


    }

}
