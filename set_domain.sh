#!/bin/bash
# set -x

# This script is going to generate a common command to create a trace profile
# based on domain_file

# -------------------------------------------------------------
# 1) Check the input parameter with porfile specific name
# -------------------------------------------------------------

PROFILE_NAME=$1

DOMAIN_FILE="${HOME}/domain_final.txt"
DOMAIN_LIST=(`cat $DOMAIN_FILE | grep -E ^com`)
DOMAIN_LIST_SIZE=${#DOMAIN_LIST[@]}
DOMAIN_STR=""

CMD_PREFIX='tracecc-profile-create ${PROFILE_NAME} -t "'

DOMAIN_SUFFIX=', 7'
DOMAIN_DELIMETER='; '
CMD_SUFFIX='" -n 128 -s 256'

CMD_DOMAIN=""

# Clear the profile firstly if exist
if [[ `tracecc-profile-list` == "${PROFILE_NAME}"  ]]
then
    tracecc-profile-delete ${PROFILE_NAME}
fi

# Concatenate all the available domains with DOMAIN_SUFFIX
for (( i = 0; i < ${DOMAIN_LIST_SIZE}; i++ ))
do
    # Be careful the regexpression
    # Here is an example to use regexp in if condition
    # if [[ ${DOMAIN_LIST[$i]} == com_* ]]
    DOMAIN_STR+=${DOMAIN_LIST[$i]}${DOMAIN_SUFFIX}

    if [[ ${i} -lt `expr ${DOMAIN_LIST_SIZE} - 1` ]]
    then
        DOMAIN_STR+=${DOMAIN_DELIMETER}
    fi
done

CMD_DOMAIN=${CMD_PREFIX}${DOMAIN_STR}${CMD_SUFFIX}

echo "--------------------------------------------------"
echo "-------------- Domain Command --------------------"
echo ${CMD_DOMAIN}
echo "--------------------------------------------------"

# Be careful that the command cannot be executed directly
eval ${CMD_DOMAIN}
