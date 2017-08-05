# Here is the log_detail class to record every line of log info
# the structure of the object list below:
# LogDetail
#     - timestamp
#     - nodeName
#     - logLine
#     - domain
#     - cpu
#     - ProcInfo
#         - procName
#         - vpid
#         - vtid
#     - detail
#         - fileName
#         - funct
#         - codeLine
#         - message

class LogDetail:


    def __init__(self) -> None:
        self.index     = 0
        self.timestamp = ''
        self.nodeName  = ''
        self.logLine   = ''
        self.domain    = ''
        self.cpu       = ''

        self.procInfo = {}
        self.procInfo['procName'] = ''
        self.procInfo['vpid']     = ''
        self.procInfo['vtid']     = ''

        self.detail = {}
        self.detail['fileName']   = ''
        self.detail['funct']      = ''
        self.detail['codeLine']   = ''
        self.detail['message']    = ''



    def __repr__(self) -> str:
        return 'index        : ['+ self.index                \
             + ']\ntimestamp : ['+ self.timestamp            \
             + ']\nnodeName  : ['+ self.nodeName             \
             + ']\nlogLine   : ['+ str(self.logLine)         \
             + ']\ndomain    : ['+ self.domain               \
             + ']\ncpu       : ['+ str(self.cpu)             \
             + ']\nprocName  : ['+ self.procInfo['procName'] \
             + ']\nvpid      : ['+ self.procInfo['vpid']     \
             + ']\nvtid      : ['+ self.procInfo['vtid']     \
             + ']\nfileName  : ['+ self.detail['fileName']   \
             + ']\nfunction  : ['+ self.detail['funct']      \
             + ']\ncodeLine  : ['+ self.detail['codeLine']   \
             + ']\nmessage   : ['+ self.detail['message']    \
             + ']'
