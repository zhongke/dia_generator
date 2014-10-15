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
SUBSCRIBER="dn:EPC-SubscriberId="
SUBSCRIBER_GROUP="EPC-SubscriberGroupId="

# CONTEXT_INFO=(`grep -n -e $CONTEXT $LDIF_FILE | awk 'BEGIN { FS="[:=,]" } { print $1,  $4, $6, $8 }'`)




show_subscriber_info () {
SUBSCRIBER_INFO_LIST=(`grep -n -e $SUBSCRIBER $LDIF_FILE`)
    for (( i = 0; i < ${#SUBSCRIBER_INFO_LIST[@]}; i++ ))
    do
        SUBSCRIBER_INFO=(`echo ${SUBSCRIBER_INFO_LIST[$i]} | awk 'BEGIN { FS="[:=,]" } { print $1, $4 }'`)
        SUBSCRIBER_LINE=${SUBSCRIBER_INFO[0]}
        SUBSCRIBER=${SUBSCRIBER_INFO[1]}
        echo -n "Subscriber : "
        echo $SUBSCRIBER
        SUBSCRIBERED_SERVICES=`cat $LDIF_FILE | sed -n ''"${SUBSCRIBER_LINE}"',/^$/p' | sed -n '/EPC-SubscriberedServices/p' | awk 'BEGIN { FS="[:]" } {print $2}'`
        echo -n "Services   : "
        echo $SUBSCRIBERED_SERVICES
        SUBSCRIBERED_GROUPIDs=`cat $LDIF_FILE | sed -n ''"${SUBSCRIBER_LINE}"',/^$/p' | sed -n '/EPC-GroupIds/p' | awk 'BEGIN { FS="[:]" } {print $2}'`
        echo -n "GroupIds   : "
        echo $SUBSCRIBERED_GROUPIDs
    done
}

show_subscriberGroup_info () {
#SUBSCRIBER_GROUP_INFO_LIST=(`grep -n -e $SUBSCRIBER_GROUP $LDIF_FILE | awk 'BEGIN { FS="[:=,]" } { print $1, $4 }'`)
SUBSCRIBER_GROUP_INFO_LIST=(`grep -n -e $SUBSCRIBER_GROUP $LDIF_FILE`)

#echo
#echo SUBSCRIBER_GROUP_INFO
#echo $SUBSCRIBER_GROUP_INFO
#echo
    for (( i = 0; i < ${#SUBSCRIBER_GROUP_INFO_LIST[@]}; i++ ))
    do
        SUBSCRIBER_GROUP_INFO=(`echo ${SUBSCRIBER_GROUP_INFO_LIST[$i]} | awk  'BEGIN { FS="[:=,]" } { print $1, $4 }'`)
        SUBSCRIBER_GROUP_LINE=${SUBSCRIBER_GROUP_INFO[0]}
        SUBSCRIBER_GROUP=${SUBSCRIBER_GROUP_INFO[1]}
        echo
        echo -n "Group      : "
        echo $SUBSCRIBER_GROUP
        SUBSCRIBERED_SERVICES=`cat $LDIF_FILE | sed -n ''"${SUBSCRIBER_GROUP_LINE}"',/^$/p' | sed -n '/EPC-SubscribedServices/p' | awk 'BEGIN { FS="[:]" } {print $2}'`
        echo -n "Services   : "
        echo $SUBSCRIBERED_SERVICES
        echo
    done
}

show_policy_info () {

    CONTEXT_INFO_LIST=(`grep -n -e $CONTEXT $LDIF_FILE`)

    echo "-------------------------------"

    for (( i = 0; i < ${#CONTEXT_INFO_LIST[@]}; i++ ))
    do

        #echo CONTEXT_INFO_LIST[$i]
        #echo ${CONTEXT_INFO_LIST[$i]}

        CONTEXT_INFO=(`echo ${CONTEXT_INFO_LIST[$i]} | awk 'BEGIN { FS="[:=,]" } { print $1,  $4, $6, $8 }'`)
        CONTEXT_LINE=${CONTEXT_INFO[0]}

        CONTEXT=${CONTEXT_INFO[1]}
        RESOURCE=${CONTEXT_INFO[2]}
        SUBJECT=${CONTEXT_INFO[3]}
        echo "$RESOURCE $CONTEXT Control"
        echo "-------------------------------"

        echo -n "Context    : "
        echo $CONTEXT

        echo -n "Resource   : "
        echo $RESOURCE

        echo -n "Subject    : "
        echo $SUBJECT

        POLICY_VALUE=(`cat $LDIF_FILE | sed -n ''"${CONTEXT_LINE}"',/^$/p' | sed -n '/EPC-PolicyIds/p' | awk 'BEGIN { FS="[:]" } {print $3}'`)

        for (( j = 0; j < ${#POLICY_VALUE[@]}; j++ ))
        do

            echo -n "Policy:$j   : "
            echo ${POLICY_VALUE[$j]}

            POLICY_LINE=(`grep -n -e ${POLICY}${POLICY_VALUE} $LDIF_FILE | awk 'BEGIN { FS="[:]" } { print $1 }'`)

            RULE_VALUE=(`cat $LDIF_FILE | sed -n ''"${POLICY_LINE}"',/^$/p' | sed -n '/EPC-Rules/p' | awk 'BEGIN { FS="[:]" } {print $3}'`)

            for (( k = 0; k < ${#RULE_VALUE[@]}; k++ ))
            do
                echo -n "  Rule:$k   : "
                echo ${RULE_VALUE[$k]}
                RULE_LINE=(`grep -n -e ${RULE}${RULE_VALUE[$k]} $LDIF_FILE | awk 'BEGIN { FS="[:]" } { print $1 }'`)

                FORMULA_VALUE=`cat $LDIF_FILE | sed -n ''"${RULE_LINE}"',/^$/p' | sed -n '/EPC-Condition/p' | awk 'BEGIN { FS="[:]" } {print $2}'`
                echo -n "   Formula : "
                echo $FORMULA_VALUE
            done
        done

        echo "-------------------------------"

    done

}



show_subscriberGroup_info
show_subscriber_info
show_policy_info


