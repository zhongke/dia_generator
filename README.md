dia_generator
=============

generate a diagram based on input file


 The purpose of this script is trying to generate a diagram for the test spec of test case.

 Implementation Plan:
 1. Get the info from test case
   - Node list interact with SAPC (SGSN, SASN, AF, OCS, APS, EBM, EMA, BBERF, SGSN-MME, etc)
   - Message type (CCR/A, RAR/A, AAR/A, SLR/A, STR/A, Login/logout, Accouting_Start/Stop, etc)

 2. Display the Diagram:
   - Node Name
   - Message with number
   - Message line with direction
   - Key AVPs with message
   - Close the diagram

 3. Version:
   - 0.1 => Get the information from the ttcn file
       + Only Node list
       + Show the digram based on the number fo nodes
   2014-09-20
   - 0.2 => Get the sequence number of the procedure
       + Sequence number is ready
   2014-09-21
   - 0.3 => Get the message type
       + Add CCR/A, RAR/A message in the diagram
       + BBERF, SGSN_MME, GGSN, AF

   - 0.4 => Get the input from first parameter
       + $1

   - 0.5 => Avoid node with multiple interfaces
       + awk by 2 steps

   - 0.6 => Combine the message direction based on node sequence
       + done
       + optimmize

   Mon Sep 29 22:23:41 CST 2014
   - 0.7 => Add AF support
       + message: AAR/A, STR/A, ASR/A, RAR/A
 Thu Oct  2 17:09:08 CEST 2014

   - 0.8 => Add EMA support
       | Profile change by OAM
       | Reauthorization by ToD
       | test case: ./generate_diagram.sh ~/Downloads/ts_43_Gxa/ts_43_Gxa/ch_02_Gxa_Support/sch_02_03_IpCan_Session_Reauthorization_By_Oam/tc_02_03_01_IpCanSession_Update_By_Adding_Services/tc_02_03_01_IpCanSession_Update_By_Adding_Services.ttcn

   - 0.8.1 => Refactor the code
       + Abstract the common part for show_message function

   - 0.8.2 => Add detailed messge info in the diagram below
       + Reuse the current implementation
 Fri Oct 10 22:21:51 CST 2014

   - 0.8.3 => Add configuration part of test case
       | Go through the ldif and extract the dn:CotextName=, to retrieve the resource, suject, policy and rule

   - 0.9 => Add multiple sessions with same type of node

   - 0.9.1 => Add usage function to improve Ease of Use
       | configure the corrent order in the beginning
       | Use node from GGSN, GGSN2

   - 0.9.2 => How to control the flow by node sequence smmothly?

   - 0.9.3 => How to differentiate multiple sessions in the same node?

