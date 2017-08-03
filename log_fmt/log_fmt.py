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

import os, sys, shutil, os.path
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

            # Set first 4 object members
            get_first_4_parts(contents[0], logDetail)

            get_cpu(contents[1], logDetail)
            get_proc(contents[2], logDetail)
            get_detail(contents[3], logDetail)

        else:
            # Handle the remaining part if there is no domain info included like dia
            # message and task selection info
            for item in re.split('\[|\]', line):
                if (' ' != item):
                    contents.append(item)

            logDetail.timestamp = contents[1].strip('[]')
            logDetail.nodeName  = contents[2].strip('[]')
            logDetail.logLine   = contents[3].strip('[]')

            # Get the remaining info by the substr start from index 34 to the end of the line
            # TODO: Keep the 'space' for the diaMsg in the original format
            logDetail.detail['message'] = str(line[34:])

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
def get_detail(detail: str, logDetail: LogDetail) -> None:

    codeInfo = detail.split('=')

    logDetail.detail['codeFileName'] = codeInfo[1].split('"')[1]
    logDetail.detail['function']     = codeInfo[2].split('"')[1]
    logDetail.detail['codeLine']     = (codeInfo[3].split(',')[0]).strip(' ')


    # get the index of substr('msg') + ('msg = "')
    logDetail.detail['message']      = detail[(detail.find('msg') + 7):]


# Copy the template html file and append all the logs info in it
#.......................................................................'
def render_html(logDetailList: list):
    # Copy template file
    # Check the tmplate file exist or not
    template_file = 'log_template.html'
    new_file      = 'log.html'

    if (not os.path.isfile(template_file)):
        print('[ERROR]: Template file does NOT exist, return!!!')
        return

    if (os.path.isfile(new_file)):
        print('[INFO]: Log file exist, remove it')
        os.remove(new_file)

    shutil.copyfile(template_file, new_file)

    message_index = 0
    index = 1
    msg = []
    for log in logDetailList:
        # Set mutliple class tag: <tr> in order to filter logs by request
        # Such as nodeName, domain, cpu, procName, vpid, vtid, codeFileName
        # if the diaMsg and task selection log don't have domain, set domain 'NA'
        domain = 'NA'
        if (log.domain != ''):
            domain = log.domain

        row_class = log.nodeName + ' '                \
                    + domain + ' '                    \
                    + log.cpu + ' '                   \
                    + log.procInfo['procName']+ ' '   \
                    + log.procInfo['vpid']+ ' '       \
                    + log.procInfo['vtid']+ ' '       \
                    + log.detail['codeFileName']+ ' ' \
                    + log.detail['function']

        # Highlight the row with keyword 'DIAMETER-MESSAGE' as another class
        # Add one more class [message_#] to track the 'non_traffic' logs related with
        # current 'traffic' logs then add click event
        if 'DIAMETER-MESSAGE' in log.detail['message']:
            row_class += ' traffic'
            message_index += 1
            row_class += ' message_' + str(message_index)
        else:
            row_class += ' non_traffic'
            row_class += ' message_' + str(message_index) + '_context'

        record =  str('<tr class = "' + row_class  + '">') \
                + str('<td class = "index">')              \
                + str(index)                               \
                + str('</td>')                             \
                + str('<td class = "timestamp">')          \
                + str(log.timestamp)                       \
                + str('</td>')                             \
                + str('<td class = "nodeName">')           \
                + str(log.nodeName)                        \
                + str('</td>')                             \
                + str('<td class = "logLine">')            \
                + str(log.logLine)                         \
                + str('</td>')                             \
                + str('<td class = "domain">')             \
                + str(log.domain)                          \
                + str('</td>')                             \
                + str('<td class = "cpu">')                \
                + str(log.cpu)                             \
                + str('</td>')                             \
                + str('<td class = "procName">')           \
                + str(log.procInfo['procName'])            \
                + str('</td>')                             \
                + str('<td class = "vpid">')               \
                + str(log.procInfo['vpid'])                \
                + str('</td>')                             \
                + str('<td class = "vtid">')               \
                + str(log.procInfo['vtid'])                \
                + str('</td>')                             \
                + str('<td class = "codeFileName">')       \
                + str(log.detail['codeFileName'])          \
                + str('</td>')                             \
                + str('<td class = "function">')           \
                + str(log.detail['function'])              \
                + str('</td>')                             \
                + str('<td class = "codeLine">')           \
                + str(log.detail['codeLine'])              \
                + str('</td>')                             \
                + str('<td class = "message">')            \
                + str('<div>')                             \
                + str(log.detail['message'])               \
                + str('</div>')                            \
                + str('</td>')                             \
                + str('</tr>')

        index += 1
        msg.append(record)


    msg.append('</table> </div> </body> </html>')

    # Append record into the new file
    with open(new_file, 'a') as new_log:
        for line in msg:
            new_log.write(line)




# PART 1 : Parse log file
print('#-######################################################################')
print('# PART 1 : Parse log file')
print('#-######################################################################')
# Initialize a final data structure and put all the log info into this data structure
logDetailList = list()

file_info = read_from_log_file(sys.argv[1])

parse_log(file_info, logDetailList)
print()
print()

print('------------------------------------------------------------------------')
# for log in logDetailList:
#     print(log)
print('------------------------------------------------------------------------')

# PART 2 : Generate log.html
print('........................................................................')
print('#-######################################################################')
print('# PART 2 : Generate log.html')
print('#-######################################################################')
render_html(logDetailList)
