#!/usr/bin/python

# MarkDown format for comments
# Generate diagram for traffic flow in the test case for test spec


import sys
import fileinput

#print sys.argv[1:][0]


print( "import ttcn file ..." )
file = open( "tc_05_01_02_SessionReauth_By_PRA_status_Update.ttcn" )

while 1==1 :
    data = file.readline()
    if data != "":
        print data,
    else:
        break
