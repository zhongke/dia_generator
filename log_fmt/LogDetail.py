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
#         - codeFileName
#         - function
#         - codeLine
#         - message

class LogDetail:


    def __init__(self) -> None:
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
        self.detail['codeFileName'] = ''
        self.detail['function']     = ''
        self.detail['codeLine']     = ''
        self.detail['message']      = ''



    def __repr__(self) -> str:
        return 'timestamp     : ['+ self.timestamp                   \
             + ']\nnodeName      : ['+ self.nodeName                 \
             + ']\nlogLine       : ['+ str(self.logLine)             \
             + ']\ndomain        : ['+ self.domain                   \
             + ']\ncpu           : ['+ str(self.cpu)                 \
             + ']\nprocName      : ['+ self.procInfo['procName']     \
             + ']\nvpid          : ['+ self.procInfo['vpid']         \
             + ']\nvtid          : ['+ self.procInfo['vtid']         \
             + ']\ncodeFileName  : ['+ self.detail['codeFileName']   \
             + ']\nfunction      : ['+ self.detail['function']       \
             + ']\ncodeLine      : ['+ str(self.detail['codeLine'])  \
             + ']\nmessage       : ['+ self.detail['message']        \
             + ']'
