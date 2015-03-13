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
		GX_CCR_EVENT("t_gx_ccr_event"),
		GX_RAR_EVENT("t_gx_rar_event"),
		GXA_CCR_EVENT("t_gxa_ccr_event"),
		GXA_RAR_EVENT("t_gxa_rar_event"),
		SX_CCR_EVENT("t_sx_ccr_event"),
		SX_RAR_EVENT("t_sx_rar_event"),
        // Rx
		RX_AAR_EVENT("t_rx_aar_event"),
		RX_ASR_EVENT("t_rx_asr_event"),
		RX_RAR_EVENT("t_rx_rar_event"),
		RX_STR_EVENT("t_rx_str_event"),
        // Sy
		SY_SLR_EVENT("t_sy_3gpp_slr_event"),
		SY_SNR_EVENT("t_sy_3gpp_snr_event"),
		SY_STR_EVENT("t_sy_3gpp_str_event"),
        // Esy
		ESY_SLR_EVENT("t_sy_slr_event"),
		ESY_SNR_EVENT("t_sy_snr_event"),
 ESY_STR_EVENT("t_sy_str_event"),
        // Sys
        SYS_TIME_EVENT("t_setSutDateTime_ini"),
        // OAM
        LDAP_EVENT("");
		
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
		INITIAL_REQUEST,
		UPDATE_REQUEST,
		TERMINATION_REQUEST
	}

	
	public enum MSG_FLOW {
	    BOTH,
		REQUEST,
		ANSWER
	}
}
