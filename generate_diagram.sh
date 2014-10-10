#!/bin/bash
# set -x

# The purpose of this script is trying to generate a diagram for the test spec of test case.

# Implementation Plan:
# 1. Get the info from test case
#   - Node list interact with SAPC (SGSN, SASN, AF, OCS, APS, EBM, EMA, BBERF, SGSN-MME, etc)
#   - Message type (CCR/A, RAR/A, AAR/A, SLR/A, STR/A, Login/logout, Accouting_Start/Stop, etc)
#
# 2. Display the Diagram:
#   - Node Name
#   - Message with number
#   - Message line with direction
#   - Key AVPs with message
#   - Close the diagram
#
# 3. Version:
#   - 0.1 => Get the information from the ttcn file
#       + Only Node list
#       + Show the digram based on the number fo nodes
#   2014-09-20
#   - 0.2 => Get the sequence number of the procedure
#       + Sequence number is ready
#   2014-09-21
#   - 0.3 => Get the message type
#       + Add CCR/A, RAR/A message in the diagram
#       + BBERF, SGSN_MME, GGSN, AF
#
#   - 0.4 => Get the input from first parameter
#       + $1
#
#   - 0.5 => Avoid node with multiple interfaces
#       + awk by 2 steps
#
#   - 0.6 => Combine the message direction based on node sequence
#       + done
#       + optimmize
#
#   Mon Sep 29 22:23:41 CST 2014
#   - 0.7 => Add AF support
#       + message: AAR/A, STR/A, ASR/A, RAR/A
#   Thu Oct  2 17:09:08 CEST 2014
#
#   - 0.8 => Add EMA support
#       | Profile change by OAM
#       | Reauthorization by ToD
#       | test case: ./generate_diagram.sh ~/Downloads/ts_43_Gxa/ts_43_Gxa/ch_02_Gxa_Support/sch_02_03_IpCan_Session_Reauthorization_By_Oam/tc_02_03_01_IpCanSession_Update_By_Adding_Services/tc_02_03_01_IpCanSession_Update_By_Adding_Services.ttcn
#
#   - 0.8.1 => Refactor the code
#   - 0.8.2 => Add detailed messge info in the diagram below
#       | Reuse the current implementation
#
#   - 0.8.3 => Add configuration part of test case
#       | Go through the ldif and extract the dn:CotextName=, to retrieve the resource, suject, policy and rule
#
#   - 0.9 => Add multiple sessions with same type of node
#
#   - 0.9.1 => Add usage function to improve Ease of Use
#       | configure the corrent order in the beginning
#       | Use node from GGSN, GGSN2
#
#   - 0.9.2 => How to control the flow by node sequence smmothly?
#
#   - 0.9.3 => How to differentiate multiple sessions in the same node?
#


# Get the inpurt from ttcn file
TTCN_FILE=$1

# Node definition
EMA="EMA"
SAPC="SAPC"
GGSN="GGSN"
GGSN2="GGSN2"
SGSN_MME="SGSN_MME"
BBERF="BBERF"
AF="AF"
AF2="AF2"
OCS="OCS"
OCS2="OCS2"
OCS3GPP="OCS3GPP"
OCS3GPP2="OCS3GPP2"

# Event definition
GX_CCR_EVENT="t_gx_ccr_event"
GX_RAR_EVENT="t_gx_rar_event"

GXA_CCR_EVENT="t_gxa_ccr_event"
GXA_RAR_EVENT="t_gxa_rar_event"

SX_CCR_EVENT="t_sx_ccr_event"
SX_RAR_EVENT="t_sx_rar_event"

RX_AAR_EVENT="t_rx_aar_event"
RX_ASR_EVENT="t_rx_asr_event"
RX_STR_EVENT="t_rx_str_event"

# Message definition
CCR_I="CCR-I"
CCA_I="CCA-I"
CCR_U="CCR-U"
CCA_U="CCA-U"
CCR_T="CCR-T"
CCA_T="CCA-T"
RAR="RAR"
RAA="RAA"
SLR="SLR"
SLA="SLA"
STR="STR"
STA="STA"
AAR="AAR"
AAA="AAA"
ASR="ASR"
ASA="ASA"

# Request type
REQUEST="request"
RESPONSE="response"


# Message direction
LEFT=" <--------------- "
RIGHT=" ---------------> "

# Message type
INITIAL_REQUEST="INITIAL_REQUEST"
UPDATE_REQUEST="UPDATE_REQUEST"
TERMINATION_REQUEST="TERMINATION_REQUEST"

# Get the Node list from the ttcn file
NODE_LIST=(`less $TTCN_FILE | grep "f_setup_nodeInterfacesData" | awk 'BEGIN { FS="[{}]" } { print $3 }' | awk 'BEGIN {FS=","} { print $2 }'`)
NODE_LIST_SIZE=${#NODE_LIST[@]}
EVENT_LIST=(`less $TTCN_FILE | grep "f_runEvent" | awk 'BEGIN { FS="[],[]" } { print $5 }'`)
EVENT_TYPE_LIST=(`less $TTCN_FILE | grep -e 't_[a-z]*_[a-z]*_event' | awk 'BEGIN { FS="[),(]" } { print $2 }'`)
EVENT_REQUEST_LIST=(`less $TTCN_FILE | grep -e 't_[a-z]*_[a-z]*_event' | awk 'BEGIN { FS="[),(]" } { print $6 }'`)


# Show the header of the digram based on the number of nodes
show_header() {
    echo -n "*         "

    for (( i = 0; i < $NODE_LIST_SIZE; i++ ))
    do
        node=${NODE_LIST[$i]}
        if [ $node != $EMA ]
        then
            echo -n "$node"

            # Add node SAPC after first node
            if [ $i -eq 1 ]
            then
                echo -n "                        "
                echo -n "$SAPC"
            fi

            # Chage the line after final node
            if [ $i -lt `expr $NODE_LIST_SIZE - 1` ]
            then
                echo -n "                        "
            else
                echo
            fi
        fi
    done
}




# Show the first, common, last line
show_commmon_line() {
    echo  -n "*       "
    flag=0;
    while [ $flag -lt $NODE_LIST_SIZE ]
    do
        if [ "$1" == "begin" ]
        then
            echo -n " ________ "
        elif [ "$1" == "common" ]
        then
            echo -n "|        |"
        elif [ "$1" == "end" ]
        then
            echo -n "|________|"
        fi

        if [ $flag -lt `expr $NODE_LIST_SIZE - 1` ]
        then
            echo -n "                  "
        else
            echo
        fi

        let 'flag++'
    done
}



show_direction () {
    # Combine the start part with comment
    echo  -n "*       "

    # show node based on the number of nodes
    # show_direction $node_seq $event $request
    for (( j = 0; j < $NODE_LIST_SIZE; j++ )) 
    do
        echo -n "|        |"

        if [ $1 -ne $j ]
        then
            if [ $j -lt `expr $NODE_LIST_SIZE - 1` ]
            then
                echo -n "                  "
            else
                echo
            fi
        elif [ $1 -eq 0  ]
        then
            if [ $2 == "$GXA_CCR_EVENT" -o $2 == "$SX_CCR_EVENT" -o $2 == "$GX_CCR_EVENT" ]
            then
                if [ $3 == "$REQUEST" ]
                then
                    echo -n "$RIGHT"
                else
                    echo -n "$LEFT"
                fi

            elif [ $2 == "$GXA_RAR_EVENT" -o $2 == "$SX_RAR_EVENT" -o $2 == "$GX_RAR_EVENT" ]
            then
                if [ $3 == "$REQUEST" ]
                then
                    echo -n "$LEFT"
                else
                    echo -n "$RIGHT"
                fi
            fi
        elif [ $1 -eq 1  ]
        then
            if [ $2 == "$GX_CCR_EVENT" -o $2 == "$RX_AAR_EVENT" -o $2 == "$RX_STR_EVENT" ]
            then
                if [ $3 == "$REQUEST" ]
                then
                    echo -n "$LEFT"
                else
                    echo -n "$RIGHT"
                fi
            elif [ $2 == "$GX_RAR_EVENT" -o $2 == "$RX_RAR_EVENT"  -o $2 == "$RX_ASR_EVENT" ]
            then
                if [ $3 == "$REQUEST" ]
                then
                    echo -n "$RIGHT"
                else
                    echo -n "$LEFT"
                fi
            fi
        fi

    done

}



# ---------------------------------------------------------
# Show message type in the diagram
# ---------------------------------------------------------
show_message_type () {
    # Combine the start part with comment
    echo  -n "*       "
    # show node based on the number of nodes
    # show_message $node_seq $event $request $request_type $msg_seq
    for (( j = 0; j < $NODE_LIST_SIZE; j++ ))
    do
        echo -n "|        |"

        if [ $1 -ne $j ]
        then
            if [ $j -lt `expr $NODE_LIST_SIZE - 1` ]
            then
                echo -n "                  "
            else
                echo
            fi
        elif [ $1 -eq 0  ]
        then

            echo -n " ($4) "
            if [ $2 == "$GXA_CCR_EVENT" -o $2 == "$SX_CCR_EVENT" -o $2 == "$GX_CCR_EVENT" ]
            then
                if [ $3 == "$REQUEST" ]
                then
                    if [ $5 == "$INITIAL_REQUEST" ]
                    then
                        echo -n "$CCR_I"
                    elif [ $5 == "$UPDATE_REQUEST" ]
                    then
                        echo -n "$CCR_U"
                    elif [ $5 == "$TERMINATION_REQUEST" ]
                    then
                        echo -n "$CCR_T"
                    fi

                elif [ $3 == "$RESPONSE" ]
                then
                    if [ $5 == "$INITIAL_REQUEST" ]
                    then
                        echo -n "$CCA_I"
                    elif [ $5 == "$UPDATE_REQUEST" ]
                    then
                        echo -n "$CCA_U"
                    elif [ $5 == "$TERMINATION_REQUEST" ]
                    then
                        echo -n "$CCA_T"
                    fi
                fi
                if [ $4 -lt 10 ]
                then
                    echo -n "        "
                else
                    echo -n "       "
                fi
            elif [ $2 == "$GXA_RAR_EVENT" -o $2 == "$SX_RAR_EVENT" -o $2 == "$GX_RAR_EVENT" ]
            then
                if [ $3 == "$REQUEST" ]
                then
                    echo -n "$RAR"
                else
                    echo -n "$RAA"
                fi
                if [ $4 -lt 10 ]
                then
                    echo -n "          "
                else
                    echo -n "         "
                fi
            fi
        elif [ $1 -eq 1  ]
        then
            echo -n " ($4) "
            if [ $2 == "$GX_CCR_EVENT" ]
            then

                if [ $3 == "$REQUEST" ]
                then
                    if [ $5 == "$INITIAL_REQUEST" ]
                    then
                        echo -n "$CCR_I"
                    elif [ $5 == "$UPDATE_REQUEST" ]
                    then
                        echo -n "$CCR_U"
                    elif [ $5 == "$TERMINATION_REQUEST" ]
                    then
                        echo -n "$CCR_T"
                    fi
                else
                     if [ $5 == "$INITIAL_REQUEST" ]
                    then
                        echo -n "$CCA_I"
                    elif [ $5 == "$UPDATE_REQUEST" ]
                    then
                        echo -n "$CCA_U"
                    elif [ $5 == "$TERMINATION_REQUEST" ]
                    then
                        echo -n "$CCA_T"
                    fi
                fi
                if [ $4 -lt 10 ]
                then
                    echo -n "        "
                else
                    echo -n "       "
                fi
            else
                if [ $2 == "$GX_RAR_EVENT" ]
                then
                    if [ $3 == "$REQUEST" ]
                    then
                        echo -n "$RAR"
                    else
                        echo -n "$RAA"
                    fi
                elif [ $2 == "$RX_AAR_EVENT" ]
                then
                    if [ $3 == "$REQUEST" ]
                    then
                        echo -n "$AAR"
                    else
                        echo -n "$AAA"
                    fi
                elif [ $2 == "$RX_STR_EVENT" ]
                then
                    if [ $3 == "$REQUEST" ]
                    then
                        echo -n "$STR"
                    else
                        echo -n "$STA"
                    fi
                elif [ $2 == "$RX_RAR_EVENT" ]
                then
                    if [ $3 == "$REQUEST" ]
                    then
                        echo -n "$RAR"
                    else
                        echo -n "$RAA"
                    fi
                elif [ $2 == "$RX_ASR_EVENT" ]
                then
                    if [ $3 == "$REQUEST" ]
                    then
                        echo -n "$ASR"
                    else
                        echo -n "$ASA"
                    fi
                fi

                if [ $4 -lt 10 ]
                then
                    echo -n "          "
                else
                    echo -n "         "
                fi
            fi
        fi 
    done

}

# ---------------------------------------------------------
# Show message info below the diagram
# show_message_type $event $count $request
# ---------------------------------------------------------
show_message_detail () {

    count=1
    seq=0
    for (( i = 0; i < ${#EVENT_LIST[@]}; i++ ))
    do
        node=${EVENT_LIST[$i]}
        event=${EVENT_TYPE_LIST[$i]}
        request=${EVENT_REQUEST_LIST[$seq]}
        req=""
        session=`echo $event | awk 'BEGIN {FS="[_]"} {print $2} '`

        echo  -n "*       $count) "

        case $event in
        $GXA_CCR_EVENT|$SX_CCR_EVENT|$GX_CCR_EVENT)

            case $request in
            $INITIAL_REQUEST)
                req="Initialize"
            ;;
            $UPDATE_REQUEST)
                req="Update"
            ;;
            $TERMINATION_REQUEST)
                req="Terminate"
            ;;
            esac
            echo "$req $session Session"
            echo -n "*           -> "
            echo "CCR message is sent from $node to SAPC"
            echo "*              - cc_request_type          : $request"
            echo "*"
            echo -n "*           <- "
            echo "CCA message is sent from SAPC to $node"
            echo "*              - Result-Code              : 2001 (SUCCESS)"


        ;;
        $GXA_RAR_EVENT|$SX_RAR_EVENT|$GX_RAR_EVENT)
            seq=`expr $seq - 1`
            echo "$session Re-authorization"
            echo  -n "*           <- "

            echo "RAR message is sent from SAPC to $node"
            echo "*               - CR-Install              :"
            echo "*"
            echo -n "*           -> "
            echo "RAA message is sent from $node to SAPC"
            echo "*              - Result-Code              : 2001 (SUCCESS)"
        ;;

        esac

        echo "*"

        let 'count++'
        let 'seq++'
    done

}



show_diagram () {
    count=1
    seq=0
    node_seq=0
    has_gxa_sx=0
    for (( i = 0; i < ${#EVENT_LIST[@]}; i++ ))
    do
        node=${EVENT_LIST[$i]}
        event=${EVENT_TYPE_LIST[$i]}
        request=${EVENT_REQUEST_LIST[$seq]}

        #show_commmon_line "common"

        case $node in
        $SGSN_MME|$BBERF)
            node_seq=0
            has_gxa_sx=1
            if [ $event == $GXA_RAR_EVENT -o $event == $SX_RAR_EVENT ]
            then
                seq=`expr $seq - 1`
                show_message_type $node_seq $event $REQUEST  $count
                show_direction $node_seq $event $REQUEST
                #show_commmon_line "common"
                show_message_type $node_seq $event $RESPONSE  $count
                show_direction $node_seq $event $RESPONSE
                #show_commmon_line "common"
            else
                show_message_type $node_seq $event $REQUEST  $count $request
                show_direction $node_seq $event $REQUEST
                #show_commmon_line "common"
                show_message_type $node_seq $event $RESPONSE  $count $request
                show_direction $node_seq $event $RESPONSE
                #show_commmon_line "common"
            fi
        ;;

        $GGSN)
            if [ $has_gxa_sx -eq 1 ]
            then
                node_seq=1
            else
                node_seq=0
            fi

            if [ $event == $GX_RAR_EVENT ]
            then
                seq=`expr $seq - 1`
                show_message_type $node_seq $event $REQUEST $count
                show_direction $node_seq $event $REQUEST
                #show_commmon_line "common"
                show_message_type $node_seq $event $RESPONSE $count
                show_direction $node_seq $event $RESPONSE
                #show_commmon_line "common"
            else
                show_message_type $node_seq $event $REQUEST  $count $request
                show_direction $node_seq $event $REQUEST
                #show_commmon_line "common"
                show_message_type $node_seq $event $RESPONSE  $count $request
                show_direction $node_seq $event $RESPONSE
                #show_commmon_line "common"
            fi
        ;;

        $AF)
            if [ $has_gxa_sx -eq 1 ]
            then
                node_seq=2
            else
                node_seq=1
            fi

            seq=`expr $seq - 1`

            show_message_type $node_seq $event $REQUEST $count
            show_direction $node_seq $event $REQUEST
            #show_commmon_line "common"
            show_message_type $node_seq $event $RESPONSE $count
            show_direction $node_seq $event $RESPONSE
            #show_commmon_line "common"
        ;;

        esac

        let 'count++'
        let 'seq++'
    done

}


show_header

show_commmon_line "begin"
show_commmon_line "common"
show_commmon_line "common"
show_diagram
show_commmon_line "common"
show_commmon_line "common"
show_commmon_line "end"
echo "*"
echo "*"
show_message_detail


