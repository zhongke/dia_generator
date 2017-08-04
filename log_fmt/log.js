// TODO:
// Add a filter for doamin
// Load all the domain in the set for later filtering in a list

// Global variable to store column status show or hide
var g_column_status = {
    index        : true,
    timestamp    : true,
    nodeName     : true,
    logLine      : true,
    domain       : true,
    cpu          : true,
    procName     : true,
    vpid         : true,
    vtid         : true,
    codeFileName : true,
    func         : true,
    codeLine     : true,
    message      : true
};

function initTable() {
    // Load the json file and the information step bu step
    // 1) load traffic message
    var items = [];
    var header = "<tr id='header'>"
               +      "<th id='index' class='index'>NUM</th>"
               +      "<th id='timestamp' class='timestamp'>TIME</th>"
               +      "<th id='nodeName' class='nodeName'>NODE</th>"
               +      "<th id='logLine' class='logLine'>LINE</th>"
               +      "<th id='domain' class='domain'>DOMAIN</th>"
               +      "<th id='cpu' class='cpu'>CPU</th>"
               +      "<th id='procName' class='procName'>_PROC_</th>"
               +      "<th id='vpid' class='vpid'>vPID</th>"
               +      "<th id='vtid' class='vtid'>vTID</th>"
               +      "<th id='codeFileName' class='codeFileName'>FILE</th>"
               +      "<th id='function' class='function'>FUNC</th>"
               +      "<th id='codeLine' class='codeLine'>LINE</th>"
               +      "<th id='message' class='message'>MSG</th>"
               + "</tr>";


    items.push(header);
    var traffic_list  = [];
    for (var traffic_index = 0; traffic_index < data.length; traffic_index++) {
        // Add the traffic line into the table
        if (data[traffic_index].detail.message.includes('DIAMETER-MESSAGE')) {
            log = "<tr id=" + data[traffic_index].index + " class='traffic'>"
                +      "<td class='index'>"        + data[traffic_index].index               + "</td>"
                +      "<td class='timestamp'>"    + data[traffic_index].timestamp           + "</td>"
                +      "<td class='nodeName'>"     + data[traffic_index].nodeName            + "</td>"
                +      "<td class='logLine'>"      + data[traffic_index].logLine             + "</td>"
                +      "<td class='domain'>"       + data[traffic_index].domain              + "</td>"
                +      "<td class='cpu'>"          + data[traffic_index].cpu                 + "</td>"
                +      "<td class='procName'>"     + data[traffic_index].procInfo.procName   + "</td>"
                +      "<td class='vpid'>"         + data[traffic_index].procInfo.vpid       + "</td>"
                +      "<td class='vtid'>"         + data[traffic_index].procInfo.vtid       + "</td>"
                +      "<td class='codeFileName'>" + data[traffic_index].detail.codeFileName + "</td>"
                +      "<td class='function'>"     + data[traffic_index].detail.function     + "</td>"
                +      "<td class='codeLine'>"     + data[traffic_index].detail.codeLine     + "</td>"
                +      "<td class='message'>"      + data[traffic_index].detail.message      + "</td>"
                + "</tr>";

            items.push(log);
            traffic_list.push(traffic_index);
        }
    }

    $( "<table/>", {
        html: items.join( "" )
    }).appendTo( "body" );

    // Load the function here
    hideColumn();
    showContext(traffic_list);

}

// Check the status for the column status the display or hide
// .............................................................................
function hideColumn()
{
    $('th').click(function() {
        var tr_class = $(this).attr('class');
        $('.'+ tr_class).css('display', 'none');
        if (tr_class == 'index') {
            g_column_status.index = false;
        } else if (tr_class == 'timestamp') {
            g_column_status.timestamp = false;
        } else if (tr_class == 'nodeName') {
            g_column_status.nodeName = false;
        } else if (tr_class == 'logLine') {
            g_column_status.logLine = false;
        } else if (tr_class == 'domain') {
            g_column_status.domain = false;
        } else if (tr_class == 'cpu') {
            g_column_status.cpu = false;
        } else if (tr_class == 'procName') {
            g_column_status.procName = false;
        } else if (tr_class == 'vpid') {
            g_column_status.vpid = false;
        } else if (tr_class == 'vtid') {
            g_column_status.vtid = false;
        } else if (tr_class == 'codeFileName') {
            g_column_status.codeFileName = false;
        } else if (tr_class == 'function') {
            g_column_status.func = false;
        } else if (tr_class == 'codeLine') {
            g_column_status.codeLine = false;
        } else if (tr_class == 'message') {
            g_column_status.message = false;
        }
    });
}

// .............................................................................
function showContext(traffic_list)
{
    for (var i = 0; i < traffic_list.length; i++) {
        // Use a key value pair to store the current element status sperately
        var context_exist = {};
        context_exist[traffic_list[i]] = false;
        $('#' + traffic_list[i]).click(function(){
            var id = $(this).attr('id');

            if (context_exist[id]) {
                removeContext(id);
                context_exist[id] = false;
            } else {
                insertContext(id, $(this));
                context_exist[id] = true;
            }
        });
    }
}


// .............................................................................
function removeContext(id)
{
    for (var traffic_index = parseInt(id) + 1; traffic_index < data.length; traffic_index++) {
        if (! data[traffic_index].detail.message.includes('DIAMETER-MESSAGE')) {
            $('table tr#' + traffic_index).remove();
        } else {
            break;
        }
    }
}

// .............................................................................
function insertContext(id, trafficObject)
{
    context = [];
    for (var traffic_index = parseInt(id) + 1; traffic_index < data.length; traffic_index++) {
        // Add the traffic line into the table
        if (! data[traffic_index].detail.message.includes('DIAMETER-MESSAGE')) {
            log = "<tr id=" + data[traffic_index].index + ">";

            if (g_column_status.index)
                log +=      "<td class='index'>"        + data[traffic_index].index               + "</td>";
            if (g_column_status.timestamp)
                log +=      "<td class='timestamp'>"    + data[traffic_index].timestamp           + "</td>";
            if (g_column_status.nodeName)
                log +=      "<td class='nodeName'>"     + data[traffic_index].nodeName            + "</td>";
            if (g_column_status.logLine)
                log +=      "<td class='logLine'>"      + data[traffic_index].logLine             + "</td>";
            if (g_column_status.domain)
                log +=      "<td class='domain'>"       + data[traffic_index].domain              + "</td>";
            if (g_column_status.cpu)
                log +=      "<td class='cpu'>"          + data[traffic_index].cpu                 + "</td>";
            if (g_column_status.procName)
                log +=      "<td class='procName'>"     + data[traffic_index].procInfo.procName   + "</td>";
            if (g_column_status.vpid)
                log +=      "<td class='vpid'>"         + data[traffic_index].procInfo.vpid       + "</td>";
            if (g_column_status.vtid)
                log +=      "<td class='vtid'>"         + data[traffic_index].procInfo.vtid       + "</td>";
            if (g_column_status.codeFileName)
                log +=      "<td class='codeFileName'>" + data[traffic_index].detail.codeFileName + "</td>";
            if (g_column_status.func)
                log +=      "<td class='function'>"     + data[traffic_index].detail.function     + "</td>";
            if (g_column_status.codeLine)
                log +=      "<td class='codeLine'>"     + data[traffic_index].detail.codeLine     + "</td>";
            if (g_column_status.message)
                log +=      "<td class='message'>"      + data[traffic_index].detail.message      + "</td>";

            log += "</tr>";

            context.push(log);
        } else {
            break;
        }
    }
    for (var i = context.length; i > 0; i--) {
        $(context.pop()).insertAfter($(trafficObject).closest('tr'));
    }
}
/*
    var show_context = false;
    // Add click event on ervery class 'traffic' display or hide
    $('tr').click(function() {
        // Ignore the header line
        // If the current line was clicked then insert the related context into the below
        if ($(this).attr('id') != 0) {
            var traffic_context_class = '.'+ ($(this).attr('class')).split(" ").pop() + '_context';

            if (show_context) {
                $(traffic_context_class).css('display', 'none');
                show_context = false;
            } else {
                $(traffic_context_class).css('display', 'table-row');
                show_context = true;
            }
        }
    });
*/
