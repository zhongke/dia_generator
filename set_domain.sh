#!/bin/bash

tracecc-profile-create Kevin -t "\

com_ericsson_sapc_cached_adapters, 7;\
# com_ericsson_sapc_cba_environment, 7;\
com_ericsson_sapc_cba_platform, 7;\
com_ericsson_sapc_centralcache, 7;\
com_ericsson_sapc_centralcache_loader, 7;\
# com_ericsson_sapc_concurrent_queue, 7;\
# com_ericsson_sapc_dbs_restartmonitor, 7;\
# com_ericsson_sapc_diameterpeermonitor, 7;\
# com_ericsson_sapc_endusernotification, 7;\
# com_ericsson_sapc_entitydata_drivers, 7;\
# com_ericsson_sapc_entitydata_environment, 7;\
# com_ericsson_sapc_entitydata_profile, 7;\
# com_ericsson_sapc_entitydata_qos, 7;\
# com_ericsson_sapc_entitydata_resolvers, 7;\
# com_ericsson_sapc_generic_platform, 7;\
# com_ericsson_sapc_geored, 7;\
# com_ericsson_sapc_geored_dependant, 7;\
# com_ericsson_sapc_geored_imm_config, 7;\
# com_ericsson_sapc_geored_state, 7;\
# com_ericsson_sapc_immadaptation, 7;\
# com_ericsson_sapc_imm_core, 7;\
# com_ericsson_sapc_imm_listener, 7;\
# com_ericsson_sapc_license_adapters, 7;\
# com_ericsson_sapc_license_manager, 7;\
# com_ericsson_sapc_license_monitor, 7;\
com_ericsson_sapc_mobile_sessions, 7;\
# com_ericsson_sapc_nongen_entities_adapters, 7;\
# com_ericsson_sapc_oam_alarms, 7;\
# com_ericsson_sapc_oam_logs, 7;\
# com_ericsson_sapc_oam_measures, 7;\
com_ericsson_sapc_obsolete_sessions_notifier, 7;\
com_ericsson_sapc_parser, 7;\
com_ericsson_sapc_peer_conf_monitor, 7;\
# com_ericsson_sapc_policyengine_core, 7;\
com_ericsson_sapc_policyengine_pip, 7;\
# com_ericsson_sapc_provisioning, 7;\
# com_ericsson_sapc_provisioning_authentication, 7;\
# com_ericsson_sapc_provisioning_verification, 7;\
# com_ericsson_sapc_rmf_manager, 7;\
# com_ericsson_sapc_sec_certificate, 7;\
# com_ericsson_sapc_subscharging_db, 7;\
# com_ericsson_sapc_timetrigger_eventnotifier, 7;\
# com_ericsson_sapc_timetrigger_util, 7;\
# com_ericsson_sapc_tipc, 7;\
# com_ericsson_sapc_traffic_avp_encoders, 7;\
# com_ericsson_sapc_traffic_avp_helpers, 7;\
com_ericsson_sapc_traffic_context, 7;\
com_ericsson_sapc_traffic_db_service, 7;\
com_ericsson_sapc_traffic_db_subscriber, 7;\
com_ericsson_sapc_traffic_helpers, 7;\
com_ericsson_sapc_traffic_legacy, 7;\
com_ericsson_sapc_traffic_message, 7;\
# com_ericsson_sapc_traffic_mobilitypolicy_core, 7;\
# com_ericsson_sapc_traffic_mobilitypolicy_helpers, 7;\
# com_ericsson_sapc_traffic_mobilitypolicy_policy, 7;\
# com_ericsson_sapc_traffic_mobilitypolicy_tasks, 7;\
com_ericsson_sapc_traffic_pcc_context, 7;\
com_ericsson_sapc_traffic_pcc_core, 7;\
com_ericsson_sapc_traffic_pcc_db_session, 7;\
com_ericsson_sapc_traffic_pcc_helpers, 7;\
com_ericsson_sapc_traffic_pcc_policy, 7;\
com_ericsson_sapc_traffic_pcc_queues, 7;\
com_ericsson_sapc_traffic_pcc_reauth, 7;\
# com_ericsson_sapc_traffic_pcc_subscharging_context, 7;\
# com_ericsson_sapc_traffic_pcc_subscharging_core, 7;\
# com_ericsson_sapc_traffic_pcc_subscharging_helpers, 7;\
# com_ericsson_sapc_traffic_pcc_subscharging_tasks, 7;\
com_ericsson_sapc_traffic_pcc_tasks, 7;\
com_ericsson_sapc_traffic_pcc_tasks_selectors, 7;\
com_ericsson_sapc_traffic_policy, 7;\
com_ericsson_sapc_traffic_policy_tags, 7;\
com_ericsson_sapc_traffic_service_rx_adapters, 7;\
com_ericsson_sapc_traffic_tasks, 7;\
com_ericsson_sapc_traffic_task_selectors, 7;\
com_ericsson_sapc_traffic_types, 7;\
# com_ericsson_sapc_usagecontrol_core, 7;\
# com_ericsson_sapc_usagecontrol_db, 7;\
# com_ericsson_sapc_usagecontrol_gx, 7;\
# com_ericsson_sapc_usagecontrol_session_adapter, 7;\
# com_ericsson_sapc_usagecontrol_utils, 7;\
# com_ericsson_sapc_util_charging, 7;\
com_ericsson_sapc_util_common, 7;\
com_ericsson_sapc_util_datatypes, 7;\
com_ericsson_sapc_util_dbn, 7;\
com_ericsson_sapc_util_eventmanager, 7;\
com_ericsson_sapc_util_internalconfig, 7;\
com_ericsson_sapc_util_osimanager, 7;\
com_ericsson_sapc_util_potutility, 7;\
com_ericsson_sapc_util_profile, 7;\
com_ericsson_sapc_util_session, 7;\
com_ericsson_sapc_util_signals, 7;\
com_ericsson_sapc_util_string, 7;\
com_ericsson_sapc_util_task, 7;\
com_ericsson_sapc_util_task_context, 7;\
# com_ericsson_sapc_util_thrift_environment, 7;\
# com_ericsson_sapc_util_thrift_prefork, 7;\
# com_ericsson_sapc_util_timefunctions, 7;\
# com_ericsson_sapc_util_timer_adapter, 7;\
# com_ericsson_sapc_util_validity_collector, 7;\
# com_ericsson_sapc_webservices_soap, 7;\

" -n 128 -s 256
