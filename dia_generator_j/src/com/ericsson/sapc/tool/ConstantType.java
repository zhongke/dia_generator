package com.ericsson.sapc.tool;

public class ConstantType {

	public enum NODE_TYPE {
		EMA("EMA"),
		SAPC("SAPC"),
		GGSN("GGSN"),
		GGSN2("GGSN2"),
		SGSN_MME("SGSN_MME"),
		BBERF("BBERF"),
		AF("AF"),
		AF2("AF2"),
		OCS("OCS"),
		OCS2("OCS2"),
		OCS3GPP("OCS3GPP"),
		OCS3GPP2("OCS3GPP2");
		

		private String name;
		private NODE_TYPE() {}
		private NODE_TYPE(String name) {
			this.name = name;
		}
		
		public String toString() {
			return this.name;
		}
	}

	public enum EVENT_TYPE {
        // Gx, Gxa, Sx
        GX_CCR_EVENT("GX_CCR_EVENT"),
		GX_RAR_EVENT("GX_RAR_EVENT"),
		GXA_CCR_EVENT("GXA_CCR_EVENT"),
		GXA_RAR_EVENT("GXA_RAR_EVENT"),
		SX_CCR_EVENT("SX_CCR_EVENT"),
		SX_RAR_EVENT("SX_RAR_EVENT"),
        // Rx
        RX_AAR_EVENT("RX_AAR_EVENT"),
		RX_ASR_EVENT("RX_ASR_EVENT"),
		RX_RAR_EVENT("RX_RAR_EVENT"),
		RX_STR_EVENT("RX_STR_EVENT"),
        // Sy
        SY_3GPP_SLR_EVENT("SY_3GPP_SLR_EVENT"),
		SY_3GPP_SNR_EVENT("SY_3GPP_SNR_EVENT"),
		SY_3GPP_STR_EVENT("SY_3GPP_STR_EVENT"),
        // Esy
        SY_SLR_EVENT("SY_SLR_EVENT"),
		SY_SNR_EVENT("SY_SNR_EVENT"),
 SY_STR_EVENT("SY_STR_EVENT");
        // Sys
        // SYS_TIME_EVENT("setSutDateTime_ini"),
        // OAM
        // LDAP_EVENT("");

        private String event;

        private EVENT_TYPE() {}

        private EVENT_TYPE(String event) {
            this.event = event;
        }

        public String toString() {
            return this.event;
        }


	}

	public enum MSG_TYPE {
		CCR_I("CCR-I"),
		CCA_I("CCA-I"),
		CCR_U("CCR-U"),
		CCA_U("CCA-U"),
		CCR_T("CCR-T"),
		CCA_T("CCA-T"),
		RAR("RAR"),
		RAA("RAA"),
		SLR("SLR"),
		SLA("SLA"),
		SNR("SNR"),
		SNA("SNA"),
		STR("STR"),
		STA("STA"),
		AAR("AAR"),
		AAA("AAA"),
		ASR("ASR"),
		ASA("ASA");
		
		private String msg;
		private MSG_TYPE() {}
		private MSG_TYPE(String msg) {
			this.msg = msg;
		}
		
		public String toString() {
			return this.msg;
		}
	}

	public enum REQUEST_TYPE {
        INITIAL_REQUEST("INITIAL_REQUEST"), UPDATE_REQUEST("UPDATE_REQUEST"), TERMINATION_REQUEST("TERMINATION_REQUEST");


        private String request;

        private REQUEST_TYPE() {}

        private REQUEST_TYPE(String request) {
            this.request = request;
        }

        public String toString() {
            return this.request;
        }
	}

	
	public enum EVENT_FLOW {
	    BOTH,
		REQUEST,
		ANSWER
	}
}
