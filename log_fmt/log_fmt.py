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
#      - Add predifined action based on javascript for EASY of USE purpose by filter(TBD)
#        - Hide the filtered logs that you don't want to see.
#        - ONLY show the keyword related logs and scoped by "DIAMETER-MESSSAGE" logs
#        - ONLY show the keyword related logs and 10 or more PREVIOUS and NEXT related context
#          and scoped by "DIAMETER-MESSSAGE" logs
#        - ONLY show the domain, proc your cared about
#        - ONLY show the column your cared about
# 3) Generate a sequence diagram.
# 4) Do some anlysis for every request handling, time consuming compared with design base.
#    So that the risk can be detected as early as possible
# 5) Display every function time consuming from 'Enter' to 'Exit'
#    - Such as max time consuming process, thread, etc.
#    - Which task consume most part of TPS
#
# Another solution:
# 1) Parse all the log into Python object data model
# 2) Convert Python object into JSON format as a log.json file
# 3) Load the JSON file from browser and redering the data as any format as you want
#
# TODO:
# ------------------------------------------------------------------------------
# 1) Add filter contorller
#    - domain
# 2) Fix table header
# 3) Drag the header to adjust the width
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
# 3) Set <tr> CSS class as domain
# 4) Set <td> CSS class as header
# 5) Add click event on function 'traceExecutionSummary' show or hide related info
# 6) Add click event on function 'otpdiaHandleRequest' + 'Diameter dump' show or hide related info
# 7) Add click event on function 'otpdiaHandleRequest' + 'Received incoming request' show or hide related info
# 8) Add click event on function 'handleMessage' + 'Decoded' show or hide related info
# ------------------------------------------------------------------------------
import os, sys, shutil, os.path, json
import re
import LogDetail


# Get info from input
#.......................................................................'
# TODO: Ignore the unicode error before read the log file in diaMsg <Frame-IP-Address>
# Solution: Process the info in shell firstly with sed or awk
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

    logDetail.timestamp = contents[0].strip('[]')
    logDetail.nodeName  = contents[1].strip('[]')
    logDetail.logLine   = contents[2].strip('[]')

    # Ignore the prefix 'com_ericsson_sapc_'
    logDetail.domain    = (contents[3].split(':')[0])[18:]


# The entry function to parse log list
#.......................................................................'
def parse_log(file_info: list, logDetailList: list) -> None:
    index = 0

    # Define a list in order to pass this parameter to getDetail function
    # There are 4 status:
    # - 'NotFound' means at the begining of log handling
    # - 'Found'    means found the function at the begining of log
    # - 'Lost'     means the founction was over before the begining of traffic
    # - 'End'      means the function as at the end of traffic

    logStatus = ['NotFound']
    for line in file_info:
        contents = []
        # initialize a LogDetail object
        logDetail = LogDetail.LogDetail()
        # logDetail.logStatus = logStatus

        # Handle normal log line with domain info
        if 'com_ericsson_sapc' in line:
            # Split line by regexp
            for item in re.split('{|}', line):
                # Ignore the elements like ', '
                if (', ' != item):
                    contents.append(item)

            # if current line function is "PcrfVerificationPip", that will be ignored
            if (get_detail(contents[3], logDetail, logStatus) == 'End'):
                break;

            # Set first 4 object members
            get_first_4_parts(contents[0], logDetail)

            get_cpu(contents[1], logDetail)
            get_proc(contents[2], logDetail)

        else:
            # Handle the remaining part if there is no domain info included like dia
            # message and task selection info
            for item in re.split('\[|\]', line):
                if (' ' != item):
                    contents.append(item)

            logDetail.timestamp = contents[1].strip('[]')
            logDetail.nodeName  = contents[2].strip('[]')
            logDetail.logLine   = contents[3].strip('[]')

            # TODO: differentiate the dia message and task selection info with
            #       2 special domain for later filtering

            # Get the remaining info by the substr start from index 34 to the end of the line
            # Keep the 'space' for the diaMsg in the original format [&nbsp;]
            logDetail.detail['message'] = str(line[34:]).replace(' ', '&nbsp;')

        if (logStatus[0] == 'Lost'):
            logDetail.index = index
            index += 1
            logDetailList.append(logDetail)


#.......................................................................'
def get_cpu(log_cpu: str, logDetail: LogDetail) -> None:
    logDetail.cpu = log_cpu.split('=')[1].strip(' ')


#.......................................................................'
def get_proc(log_proc: str, logDetail: LogDetail) -> None:

    procInfo = re.split(',|=', log_proc)
    logDetail.procInfo['procName'] = procInfo[1].strip(' "')
    logDetail.procInfo['vpid']     = procInfo[3].strip(' ')
    logDetail.procInfo['vtid']     = procInfo[5].strip(' ')


#.......................................................................'
def get_detail(detail: str, logDetail: LogDetail, logStatus: list) -> str:

    # TODO: ignore the last part of trace which the fuciton is "PcrfVerificationPip"
    codeInfo = detail.split('=')

    logDetail.detail['funct'] = codeInfo[2].split('"')[1]

    if (logDetail.detail['funct'] != 'PcrfVerificationPip'):
        if(logStatus[0] == 'Found' or logStatus[0] == 'Lost'):
            logDetail.detail['fileName']     = codeInfo[1].split('"')[1]
            logDetail.detail['codeLine']     = (codeInfo[3].split(',')[0]).strip(' ')
            # get the index of substr('msg') + ('msg = "')
            logDetail.detail['message']      = detail[(detail.find('msg') + 7):]
            if (logStatus[0] == 'Found'):
                logStatus[0] = 'Lost'

        else:
            pass

    else:
        if (logStatus[0] == 'NotFound'):
            logStatus[0] = 'Found'
        elif (logStatus[0] == 'Lost'):
            logStatus[0] = 'End'


    return logStatus[0];


# PART 1 : Parse log file
print('#-######################################################################')
print('# PART 1 : Parse log file')
print('#-######################################################################')
# Initialize a final data structure and put all the log info into this data structure
logDetailList = list()

file_info = read_from_log_file(sys.argv[1])

parse_log(file_info, logDetailList)
print('------------------------------------------------------------------------')
print()
# Convert python object data into JSON
# print(json.dumps(logDetailList, default=lambda o: o.__dict__, sort_keys=True, indent=4))
json_file = 'log.json'
if (os.path.isfile(json_file)):
    print('[INFO]: json file exist, remove it')
    os.remove(json_file)

with open(json_file, 'a') as log_json:
    # add prefix to define a variable as JSON object
    log_json.write('var data =')
    # log_json.write(json.dumps(logDetailList, default=lambda o: o.__dict__, sort_keys=True, indent=4))
    log_json.write(json.dumps(logDetailList, default=lambda o: o.__dict__))
# for log in logDetailList:
#     print(log)
print('------------------------------------------------------------------------')
