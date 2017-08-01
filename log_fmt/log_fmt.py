# This script is going to format SAPC trace with more readable and elegant format
# in html files.
#
# All the tasks are going to be devided into 2 parts:
# 1) Analyze SAPC trace files
#    - Split every line of log with different fields.
#    - Extract the information into a list
#      - Keep the Task selection part as original
#
# 2) Generate a beautiful html file with some javascript action and css decoration
#    - General idea the whole log is a big table which includes all the info
#      - The header is field name
#      - Every line of row is log message with different style based on CSS style
#        - Highlight the DIAMETER-MESSSAGE line and folded related log in the default mode
#          - Keep the task selection info folded in a seperate format for reference
#          - Keep the diaMsg info folded in a seperate format for reference
#      - Add predifined action based on javascript for EASY of USE purpose (TBD)
#        - Hide the filtered logs that you don't want to see.
#        - ONLY show the keyword related logs and scoped by "DIAMETER-MESSSAGE" logs
#        - ONLY show the keyword related logs and 10 or more PREVIOUS and NEXT related context
#          and scoped by "DIAMETER-MESSSAGE" logs
#        - ONLY show the domain your cared about
#        - ONLY show the column your cared about

# [EXAMPLE]
# ...............................................................................
# [03:22:10.644005909] [PL-4] [3540] com_ericsson_sapc_traffic_pcc_helpers:InfoSubscriberEvent: { cpu_id = 3 }, { procname = "pcrf-proc", vpid = 7037, vtid = 7129 }, { file = "PcrfIncomingHandler.cc", function = "handleGxCcr", line = 250, msg = "DIAMETER-MESSAGE: CCR received. SessionId: tc_04_01_handover_not_affect_all_service;ggsnNodeHostname.nodeHostRealm.com;2;602175, IpAddr: , Protocol: Gx, RequestType: Modify, APN: .", adminId = "346000000001", trafficId = "" }
# ...............................................................................
#     [03:22:10.644005909]
#     [PL-4]
#     [3540]
#     com_ericsson_sapc_traffic_pcc_helpers:InfoSubscriberEvent:
#     { cpu_id = 3 },
#     { procname = "pcrf-proc", vpid = 7037, vtid = 7129 },
#     {file = "PcrfIncomingHandler.cc", function = "handleGxCcr", line = 250, msg = "DIAMETER-MESSAGE: CCR received. SessionId: tc_04_01_handover_not_affect_all_service;ggsnNodeHostname.nodeHostRealm.com;2;602175, IpAddr: , Protocol: Gx, RequestType: Modify, APN: .", adminId = "346000000001", trafficId = " }
#
#
# ...............................................................................
# [03:22:10.649808844] [PL-4] [4490] com_ericsson_sapc_util_dbn:DebugEvent: { cpu_id = 2 }, { procname = "pcrf-proc", vpid = 7037, vtid = 7164 }, { file = "DbnCache.cc", function = "findDoa", line = 36, msg = "DoaHolder with key:'133.138.47.210@defaultApnId@346000000001' is not empty" }
# ...............................................................................
#     [03:22:10.649808844]
#     [PL-4]
#     [4490]
#     com_ericsson_sapc_util_dbn:DebugEvent:
#     { cpu_id = 2 },
#     { procname = "pcrf-proc", vpid = 7037, vtid = 7164 },
#     { file = "DbnCache.cc", function = "findDoa", line = 36, msg = "DoaHolder with key:'133.138.47.210@defaultApnId@346000000001' is not empty" }
# ------------------------------------------------------------------------------
#
#!/bin/python
#
# SOLUTION
# ------------------------------------------------------------------------------
# 1) Read file from input
# 2) Process every line of log
#    IF line includes domain("com_ericsson_sapc") which means that is not the diaMsg or Task Selection
#      a. split line by regexp "{}"
#      b. get first 4 parts as common information
#      c. get last 3 parts as detailed info
# ------------------------------------------------------------------------------

import os, sys
import re
import LogDetail


# Get info from input
#.......................................................................'
def read_from_log_file(log_file) -> list:
    contents = []
    with open(log_file) as log:
        for line in log:
            contents.append(line)
    return contents


# Get first 4 parts by spliting with SPACE
#.......................................................................'
def get_first_4_parts(log_common: str, logDetail: LogDetail) -> None:
    contents = log_common.split(' ')
    print('Trying to iterate the log file and get related info... ')

    logDetail.timestamp = contents[0].strip('[]')
    logDetail.nodeName  = contents[1].strip('[]')
    logDetail.logLine   = contents[2].strip('[]')
    logDetail.domain    = contents[3]


# The entry function to parse log list
#.......................................................................'
def parse_log(file_info: list, logDetailList: list) -> None:
    for line in file_info:
        contents = []
        # initialize a LogDetail object
        logDetail = LogDetail.LogDetail()

        # Handle normal log line with domain info
        if 'com_ericsson_sapc' in line:
            # Split line by regexp
            for item in re.split('{|}', line):
                # Ignore the elements like ', '
                if (', ' != item):
                    contents.append(item)
                    print(item)

            print('#.......................................................................')
            # Set first 4 object members
            get_first_4_parts(contents[0], logDetail)

            get_cpu(contents[1], logDetail)
            get_proc(contents[2], logDetail)
            get_detail(contents[3], logDetail)

        else:
            # Handle the remaining part if there is no domain info included like dia
            # message and task selection info
            for item in re.split('\[|\]', line):
                # TODO: Handle the remaining parts as a common message in this status
                if (' ' != item):
                    contents.append(item)

            logDetail.timestamp = contents[1].strip('[]')
            logDetail.nodeName  = contents[2].strip('[]')
            logDetail.logLine   = contents[3].strip('[]')

            # Get the remaining info by the substr start from index 34 to the end of the line
            logDetail.domain    = line[34:]

        logDetailList.append(logDetail)


#.......................................................................'
def get_cpu(log_cpu: str, logDetail: LogDetail) -> None:
    print("handle log_cpu: ", log_cpu)
    logDetail.cpu = log_cpu.split('=')[1].strip(' ')


#.......................................................................'
def get_proc(log_proc: str, logDetail: LogDetail) -> None:
    print("handle log_proc: ", log_proc)

    procInfo = re.split(',|=', log_proc)
    logDetail.procInfo['procName'] = procInfo[1].strip(' "')
    logDetail.procInfo['vpid']     = procInfo[3].strip(' ')
    logDetail.procInfo['vtid']     = procInfo[5].strip(' ')


#.......................................................................'
def get_detail(detail: str, logDetail: LogDetail) -> None:
    print("handle log_detail: ", detail)

    codeInfo = re.split(',|=', detail)

    logDetail.detail['codeFileName'] = codeInfo[1].strip(' "')
    logDetail.detail['function']     = codeInfo[3].strip(' "')
    logDetail.detail['codeLine']     = codeInfo[5].strip(' ')
    logDetail.detail['message']      = codeInfo[7].strip(' "')


#.......................................................................'
def render_html(logDetailList: list):
    pass


# PART 1 : Parse log file
print('#-######################################################################')
print('# PART 1 : Parse log file')
print('#-######################################################################')
# Initialize a final data structure and put all the log info into this data structure
logDetailList = list()

file_info = read_from_log_file(sys.argv[1])

print('........................................................................')
#print(file_info)
#print('........................................................................')
parse_log(file_info, logDetailList)
print()
print()

print('------------------------------------------------------------------------')
for log in logDetailList:
    print(log)
print('------------------------------------------------------------------------')

# PART 2 : Generate log.html
print('........................................................................')
print('#-######################################################################')
print('# PART 2 : Generate log.html')
print('#-######################################################################')
render_html(logDetailList)
