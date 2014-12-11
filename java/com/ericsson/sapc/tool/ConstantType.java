package com.ericsson.sapc.tool;

public class ConstantType {

	public enum NODE_TYPE {
		EMA,
		SAPC,
		GGSN,
		GGSN2,
		SGSN_MME,
		BBERF,
		AF,
		AF2,
		OCS,
		OCS2,
		OCS3GPP,
		OCS3GPP2
	}

	public enum EVENT_TYPE {
				
		GX_CCR_EVENT("t_gx_ccr_event"),
		GX_RAR_EVENT,
		GXA_CCR_EVENT("t_gxa_ccr_event"),
		GXA_RAR_EVENT,
		SX_CCR_EVENT("t_sx_ccr_event"),
		SX_RAR_EVENT,
		RX_AAR_EVENT,
		RX_ASR_EVENT,
		RX_RAR_EVENT,
		RX_STR_EVENT,
		SY_SLR_EVENT,
		SY_SNR_EVENT,
		SY_STR_EVENT;
		
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
		CCR_U("CCA-I"),
		CCA_U("CCA-I"),
		CCR_T("CCA-I"),
		CCA_T("CCA-I"),
		RAR("RAR"),
		RAA("RAA"),
		SLR("SLR"),
		SLA("SLA"),
		SNR("SNR"),
		SNA("SNA"),
		STR("STR"),
		STA("STA"),
		AAR("AAR"),
		AAA("AAR"),
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

	public enum MSG_DIRECTION {
		LEFT,
		RIGHT
	}
}
