#!/bin/bash
#set -x

# The purpose of this script is to generate the configuration infomation from LDIF file
# Include:
#   - Policy info:
#       | Context
#       | Resource
#       | Subject
#       | Policy
#       | Rule
#           | Formula
#           | OutputAttribute
#
#   - Subscriber info:
#       | SubscriberId
#       | groups
#       | subscribedServices
#       | blacklistServices
#       | notificationData
#       | eventTriggers
#       | SubscrberQulificationData
#           | ...
#   - group info:
#       | GroupId
#       | subscribedServices
#       | blacklistServices
#       | notificationData
#       | SubscrberQulificationData
#           | ...
#
#Tue Oct 14 23:00:46 CST 2014
#   0.1 Get the all the common info from Context, subsriber and subscriber group
# #   0.2 Get all the info by loop for the context and subscriber/subscribrgroup info
#       | How to handle the 2 lines of result by grep



LDIF_FILE=$1

# dn definition
CONTEXT="EPC-ContextName="
RESOURCE="EPC-SubjectResourceId="
POLICY="EPC-PolicyId="
RULE="EPC-RuleId="
FORMULA="EPC-ConditionFormula:"
OUTPUT_ATTRIBUTES="EPC-OutputAttributes:"
SUBSCRIBER="EPC-SubscriberId="
SUBSCRIBER_GROUP="EPC-SubscriberGroupId="

# CONTEXT_INFO=(`grep -n -e $CONTEXT $LDIF_FILE | awk 'BEGIN { FS="[:=,]" } { print $1,  $4, $6, $8 }'`)
CONTEXT_INFO=(`grep -n -e $CONTEXT $LDIF_FILE`)
echo CONTEXT_INFO[0]
echo ${CONTEXT_INFO[0]}
echo 
echo CONTEXT_INFO[1]
echo ${CONTEXT_INFO[1]}
echo 



SUBSCRIBER_INFO=(`grep -n -e $SUBSCRIBER $LDIF_FILE | awk 'BEGIN { FS="[:=,]" } { print $1, $4 }'`)
SUBSCRIBER_GROUP_INFO=(`grep -n -e $SUBSCRIBER_GROUP $LDIF_FILE | awk 'BEGIN { FS="[:=,]" } { print $1, $4 }'`)
#echo
#echo SUBSCRIBER_INFO
#echo $SUBSCRIBER_INFO
#echo
#echo SUBSCRIBER_GROUP_INFO
#echo $SUBSCRIBER_GROUP_INFO
#echo

show_subscriber_info () {
    echo
    echo SUBSCRIBER_LINE
    SUBSCRIBER_LINE=${SUBSCRIBER_INFO[0]}
    echo $SUBSCRIBER_LINE
    echo
    SUBSCRIBERED_SERVICES=`cat $LDIF_FILE | sed -n ''"${SUBSCRIBER_LINE}"',/^$/p' | sed -n '/EPC-SubscriberedServices/p' | awk 'BEGIN { FS="[:]" } {print $2}'`
    echo SUBSCRIBERED_SERVICES
    echo $SUBSCRIBERED_SERVICES
    SUBSCRIBERED_GROUPIDs=`cat $LDIF_FILE | sed -n ''"${SUBSCRIBER_LINE}"',/^$/p' | sed -n '/EPC-GroupIds/p' | awk 'BEGIN { FS="[:]" } {print $2}'`
    echo SUBSCRIBERED_GROUPIDs
    echo $SUBSCRIBERED_GROUPIDs
}

show_subscriberGroup_info () {
    echo
    echo SUBSCRIBER_GROUP_LINE
    SUBSCRIBER_GROUP_LINE=${SUBSCRIBER_GROUP_INFO[0]}
    echo $SUBSCRIBER_GROUP_LINE
    echo
    SUBSCRIBERED_SERVICES=`cat $LDIF_FILE | sed -n ''"${SUBSCRIBER_GROUP_LINE}"',/^$/p' | sed -n '/EPC-SubscribedServices/p' | awk 'BEGIN { FS="[:]" } {print $2}'`
    echo SUBSCRIBERED_SERVICES
    echo $SUBSCRIBERED_SERVICES
    echo
}

show_policy_info () {

    echo CONTEXT_LINE
    CONTEXT_LINE=${CONTEXT_INFO[0]}
    echo $CONTEXT_LINE
    echo

    echo CONTEXT
    CONTEXT=${CONTEXT_INFO[1]}
    echo $CONTEXT
    echo

    echo RESOURCE
    RESOURCE=${CONTEXT_INFO[2]}
    echo $RESOURCE
    echo

    echo SUBJECT
    SUBJECT=${CONTEXT_INFO[3]}
    echo $SUBJECT

    #cat $LDIF_FILE | sed -n '$LINE,/^$/p'
    # Get policies one by one with sequence
    # cat $LDIF_FILE | sed -n ''"${LINE}"',/^$/p' | sed -n '/EPC-PolicyIds/p'
    POLICY_VALUE=`cat $LDIF_FILE | sed -n ''"${CONTEXT_LINE}"',/^$/p' | sed -n '/EPC-PolicyIds/p' | awk 'BEGIN { FS="[:]" } {print $3}'`

    echo
    echo POLICY_VALUE
    echo $POLICY_VALUE

    POLICY_LINE=(`grep -n -e ${POLICY}${POLICY_VALUE} $LDIF_FILE | awk 'BEGIN { FS="[:]" } { print $1 }'`)
    # POLICY_LINE=`grep -n -e $POLICY$POLICY_VALUE $LDIF_FILE | awk`
    echo
    echo POLICY_LINE
    echo $POLICY_LINE

    # RULE_VALUE=`cat $LDIF_FILE | sed -n '/EPC-PolicyIds/p' | awk 'BEGIN { FS="[:]" } {print $3}'`
    RULE_VALUE=`cat $LDIF_FILE | sed -n ''"${POLICY_LINE}"',/^$/p' | sed -n '/EPC-Rules/p' | awk 'BEGIN { FS="[:]" } {print $3}'`
    echo
    echo RULE_VALUE

    RULE_LINE=(`grep -n -e ${RULE}${RULE_VALUE} $LDIF_FILE | awk 'BEGIN { FS="[:]" } { print $1 }'`)
    # POLICY_LINE=`grep -n -e $POLICY$POLICY_VALUE $LDIF_FILE | awk`
    echo
    echo RULE_LINE
    echo $RULE_LINE

    # FORMULA_VALUE=`cat $LDIF_FILE | sed -n ''"${RULE_LINE}"',/^$/p' | sed -n '/'"${FORMULA}"'/p' | awk 'BEGIN { FS="[:]" } {print $3}'`
    FORMULA_VALUE=`cat $LDIF_FILE | sed -n ''"${RULE_LINE}"',/^$/p' | sed -n '/EPC-Condition/p' | awk 'BEGIN { FS="[:]" } {print $2}'`
    echo
    echo FORMULA_VALUE
    echo $FORMULA_VALUE


}



#show_policy_info
#show_subscriber_info
#show_subscriberGroup_info



