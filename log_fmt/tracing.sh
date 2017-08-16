#!/bin/bash
# set -x

# ..............................................................................
# This script is going to generate a common command to create a trace profile
# based on domain_file
# Java : l4j.com.ericsson.sapc
# C++  : com_ericsson_sapc
# ..............................................................................

# -------------------------------------------------------------
# 1) Check the input parameter with porfile specific name
# -------------------------------------------------------------


if [ $# != 2 ]
then
    echo "ONLY TWO Args are required"
    echo "USAGE: $0 <profile name> <action>"
    echo "e.g. : $0 ezhonke start"
    echo "e.g. : $0 ezhonke save"
    exit 1;
fi


# Create a domain file if needed
# find $SETUP_CBA_DIR -name \com_ericsson_sapc*.h |awk -F'/' '{print $NF}'  | cut -f1 -d"." > ./domain.txt
DOMAIN_FILE="domain.txt"
# Check the domain file exist or not
if [ ! -f  ${DOMAIN_FILE} ]
then
    find $SETUP_CBA_DIR -name \com_ericsson_sapc*.h |awk -F'/' '{print $NF}'  | cut -f1 -d"." > domain.txt
    exit 0;
fi


PROFILE_NAME=$1
ACTION=$2
TRACE_SCRIPT="${AD_FT_DIR}/bat/lib2/eclipse/implementation/commonFunctions/python/tracer.py"
TRACE_CMD_ACTION="${TRACE_SCRIPT} ${PROFILE_NAME} -a ${ACTION}"
TRACE_CMD_FILTER=' -f "'


DOMAIN_LIST=(`cat $DOMAIN_FILE | grep -E ^com`)
DOMAIN_LIST_SIZE=${#DOMAIN_LIST[@]}
DOMAIN_STR=""
DOMAIN_DELIMETER='; '
DOMAIN_END='"'

CMD_DOMAIN=""



# -------------------------------------------------------------
# 3) Concatenate all the available domains with DOMAIN_SUFFIX
# -------------------------------------------------------------
for (( i = 0; i < ${DOMAIN_LIST_SIZE}; i++ ))
do
    # Be careful the regexpression
    # Here is an example to use regexp in if condition
    # if [[ ${DOMAIN_LIST[$i]} == com_* ]]
    DOMAIN_STR+=${DOMAIN_LIST[$i]}

    if [[ ${i} -lt `expr ${DOMAIN_LIST_SIZE} - 1` ]]
    then
        DOMAIN_STR+=${DOMAIN_DELIMETER}
    fi
done

if [[ ${ACTION} == "start" ]]
then
    CMD_DOMAIN=${TRACE_CMD_ACTION}${TRACE_CMD_FILTER}${DOMAIN_STR}${DOMAIN_END}
else
    CMD_DOMAIN=${TRACE_CMD_ACTION}
    # After got the trace, process the character in "Framed-IP-Address"
fi



# -------------------------------------------------------------
# 4) Run the generated command to create a trace profile
# -------------------------------------------------------------
# Be careful that the command cannot be executed directly
eval ${CMD_DOMAIN}

if [[ $? -eq 0 ]]
then
    echo "--------------------------------------------------"
    echo "trace profile:[${PROFILE_NAME}] ${ACTION} successfully!"
    echo "--------------------------------------------------"

    if [[ ${ACTION} == "save" ]]
    then
        LOG_FILE="${PROFILE_NAME}.sort-merge.tracecc.log"
        echo ${LOG_FILE}
        if [[ -f ${LOG_FILE} ]]
        then
            echo "Processing ${LOG_FILE}..."

            # sed -i '/Framed-IP-Address/{n;/Framed-IP-Address/n;d}' ${LOG_FILE}
            sed -i '/Framed-IP-Address/{n;/Framed-IP-Address/n;d}' ${LOG_FILE}

            python3 log_fmt.py ${LOG_FILE}


        else
            echo "${LOG_FILE} doesn't exist!!!"
            return -1;
        fi
    fi
else
    echo "--------------------------------------------------"
    echo "trace profile:[${PROFILE_NAME}] ${ACTION} failed!"
    echo "--------------------------------------------------"
fi

