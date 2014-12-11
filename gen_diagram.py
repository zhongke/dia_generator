#!/usr/bin/python

# MarkDown format for comments
# Generate diagram for traffic flow in the test case for test spec


import fileinput


print( "import ttcn file ..." )
file = open( "/Users/kevinzhong/ttcn/script/tc_05_01_02_SessionReauth_By_PRA_status_Update.ttcn" )

isReadable = True
while isReadable :
    data = file.readline()
    if data != "":
        print( data )
    else:
        isReadable = False

file.seek( 0 )
print( file.readlines() )

file.close()


# xxx
