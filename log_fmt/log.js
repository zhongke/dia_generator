// TODO:
// Add a filter for doamin
// Load all the domain in the set for later filtering in a list
// .............................................................................
var g_filter = {
    index     : [],
    timestamp : [],
    // nodeName  : ['PL-3', 'PL-4'],
    nodeName  : [],
    logLine   : [],
    // domain    : ['traffic_pcc_tasks', 'traffic_pcc_db_session'],
    domain    : [],
    // cpu       : ['1', '2', '3'],
    cpu       : [],
    // TODO:
    // procName  : ['pcrf-proc'],
    procName  : [],
    // vpid      : ['1964', '31514'],
    vpid      : [],
    // vtid      : ['12750', '12772'],
    vtid      : [],
    // TODO:
    // fileName  : ['RxRetrieveAndMarkNetlocTask.cc'],
    fileName  : [],
    // funct     : ['onIncomingRequest', 'TasksContext'],
    funct     : [],
    codeLine  : [],
    message   : []
};

// Global variable to store column status show or hide
// .............................................................................
var g_column_status = {
    index     : true, timestamp : true, nodeName  : true,
    logLine   : true, domain    : true, cpu       : true,
    procName  : true, vpid      : true, vtid      : true,
    fileName  : true, funct     : true, codeLine  : true,
    message   : true
};


// Global variable to store traffic releted context status show or hide
// .............................................................................
var g_context_exist = {};

// Global variable to store matched field status for filtering
// .............................................................................
var g_matched_field = {};
// .............................................................................
function initTable() {
    // Load the json file and the information step bu step
    // 1) load traffic message
    var items = [];
    var header = "<tr id='header'>"
               +      "<th id='index'     class='index'>       NUM</th>"
               +      "<th id='timestamp' class='timestamp'>  TIME</th>"
               +      "<th id='nodeName'  class='nodeName'>   NODE</th>"
               +      "<th id='logLine'   class='logLine'>    LINE</th>"
               +      "<th id='domain'    class='domain'>   DOMAIN</th>"
               +      "<th id='cpu'       class='cpu'>         CPU</th>"
               +      "<th id='procName'  class='procName'> _PROC_</th>"
               +      "<th id='vpid'      class='vpid'>       vPID</th>"
               +      "<th id='vtid'      class='vtid'>       vTID</th>"
               +      "<th id='fileName'  class='fileName'>   FILE</th>"
               +      "<th id='funct'     class='funct'>      FUNC</th>"
               +      "<th id='codeLine'  class='codeLine'>   LINE</th>"
               +      "<th id='message'   class='message'>    MSG</th>"
               + "</tr>";


    items.push(header);
    var traffic_list  = [];
    for (var traffic_index = 0; traffic_index < data.length; ++traffic_index) {
        var log_info = data[traffic_index];
        // Add the traffic line into the table
        if (log_info.detail.message.includes('DIAMETER-MESSAGE')) {
            log = "<tr id=" + log_info.index + " class='traffic'>";
            log += buildTd(log_info, 'index');
            log += buildTd(log_info, 'timestamp');
            log += buildTd(log_info, 'nodeName');
            log += buildTd(log_info, 'logLine');
            log += buildTd(log_info, 'domain');
            log += buildTd(log_info, 'cpu');
            log += buildTd(log_info, 'procName');
            log += buildTd(log_info, 'vpid');
            log += buildTd(log_info, 'vtid');
            log += buildTd(log_info, 'fileName');
            log += buildTd(log_info, 'funct');
            log += buildTd(log_info, 'codeLine');
            log += buildTd(log_info, 'message');
            log += "</tr>";

            items.push(log);
            traffic_list.push(traffic_index);
        }
    }

    for (var i = 0; i < traffic_list.length; ++i) {
        // Use a key value pair to store the current element status sperately
        g_context_exist[traffic_list[i]] = false;
    }

    $( "<table/>", {
        html: items.join( "" )
    }).appendTo( "body" );

    // Load the function here
    hideColumn();
    showContext(traffic_list);

    // Test doFiltering
    doFiltering();

}

// Check the status for the column status the display or hide
// .............................................................................
function hideColumn()
{
    $('th').click(function() {
        var tr_class = $(this).attr('class');
        $('.'+ tr_class).css('display', 'none');

        switch(tr_class) {
            case 'index'    : g_column_status.index     = false; break;
            case 'timestamp': g_column_status.timestamp = false; break;
            case 'nodeName' : g_column_status.nodeName  = false; break;
            case 'logLine'  : g_column_status.logLine   = false; break;
            case 'domain'   : g_column_status.domain    = false; break;
            case 'cpu'      : g_column_status.cpu       = false; break;
            case 'procName' : g_column_status.procName  = false; break;
            case 'vpid'     : g_column_status.vpid      = false; break;
            case 'vtid'     : g_column_status.vtid      = false; break;
            case 'fileName' : g_column_status.fileName  = false; break;
            case 'funct'    : g_column_status.funct     = false; break;
            case 'codeLine' : g_column_status.codeLine  = false; break;
            case 'message'  : g_column_status.message   = false; break;
        }
    });
}


// .............................................................................
function showContext(traffic_list)
{
    for (var i = 0; i < traffic_list.length; ++i) {
        // Use a key value pair to store the current element status sperately
        // g_context_exist[traffic_list[i]] = false;
        $('#' + traffic_list[i]).click(function(){
            var id = $(this).attr('id');

            if (g_context_exist[id]) {
                removeContext(id);
                g_context_exist[id] = false;
            } else {
                insertContext(id, $(this));
                g_context_exist[id] = true;
            }
        });
    }
}


// .............................................................................
function removeContext(id)
{
    for (var traffic_index = parseInt(id) + 1; traffic_index < data.length; ++traffic_index) {
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
    for (var traffic_index = parseInt(id) + 1; traffic_index < data.length; ++traffic_index) {
        // Add the traffic line into the table
        var log_info = data[traffic_index];
        if (! log_info.detail.message.includes('DIAMETER-MESSAGE')) {
            context.push(buildRow(log_info));
        } else {
            break;
        }
    }
    for (var i = context.length; i > 0; --i) {
        $(context.pop()).insertAfter($(trafficObject).closest('tr'));
    }
}


// TODO: Get the result with the combined conditions 'or' or 'and'
// Set a list to record which field is matched
// Then choose the final result by the 'or' or 'and'
// also need to record which index in the field matched
// Example:
//
// domain = 'A'  && (fileName == 'B1' or fileName == 'B2')
// if condition is 'or', just check one part is enough
// if condition is 'and', all the field need to be check
// if combined 'or' and 'and', the check will be very careful
// Currently pattern: <FIELD && FIELD (ITEM || ITEM)>
// .............................................................................
function doFiltering()
{
    var context = [];

    for (var traffic_index = 0; traffic_index < data.length; ++traffic_index) {
        // Add the traffic line into the table
        var log_info = data[traffic_index];

        if (! log_info.detail.message.includes('DIAMETER-MESSAGE')) {
            filterAllFileds(log_info);
            if (  g_matched_field.index
               && g_matched_field.timestamp
               && g_matched_field.nodeName
               && g_matched_field.logLine
               && g_matched_field.domain
               && g_matched_field.cpu
               && g_matched_field.procName
               && g_matched_field.vpid
               && g_matched_field.vtid
               && g_matched_field.fileName
               && g_matched_field.funct
               && g_matched_field.codeLine
               && g_matched_field.message
            ) {
                // Push to the list based on which traffic conext is shown or not
                // If more than one traffic conext is opened?
                // Handle it one by one
                // 1. Compare the index with first elements which is greater than it.
                // 2. Check this traffic status is shown add it into list, if not, ignore it.
                // 3. util the elements is less than the next elements.

                $.each(g_context_exist, function(key, value){
                    if (log_info.index > key && value == true) {
                        context.push(buildRow(log_info));
                        // break;
                    }
                });

                // context.push(buildRow(log_info));
                // console.log(buildRow(log_info));
            }
        }
    }
    // Add the matched row into html
    $.each(g_context_exist, function(key, value){
        console.log('key :[', key, '] value: [', value, ']');
    });
}

function filterAllFileds(log_info)
{
    g_matched_field = {
        index     : false, timestamp : false, nodeName  : false,
        logLine   : false, domain    : false, cpu       : false,
        procName  : false, vpid      : false, vtid      : false,
        fileName  : false, funct     : false, codeLine  : false,
        message   : false

    };

    // TODO: index need to be suport single number seperate number and a range
    g_matched_field.index = true;

    // TODO: Check timestamp
    g_matched_field.timestamp = true;

    // Check nodeName
    filterField(g_filter.nodeName, log_info.nodeName, 'nodeName');

    // TODO: Check logLine
    g_matched_field.logLine = true;

    // Check domain
    filterField(g_filter.domain, log_info.domain, 'domain');

    // Check cpu
    filterField(g_filter.cpu, log_info.cpu, 'cpu');

    // TODO: Check procName
    // filterField(g_filter.procName, log_info.procInfo.procName, 'procName');

    if (g_filter.procName.length == 0) {
        g_matched_field.procName = true;
    } else {
        for (var i = 0; i < g_filter.procName.length; ++i) {
            if (log_info.procInfo.procName == g_filter.procName[i]) {
                g_matched_field.procName = true;
                row_exist = true;
                break;
            }
        }
    }
    // Check vpid
    filterField(g_filter.vpid, log_info.procInfo.vpid, 'vpid');

    // Check vtid
    filterField(g_filter.vtid, log_info.procInfo.vtid, 'vtid');

    // TODO: Check fileName
    // filterField(g_filter.fileName, log_info.procInfo.fileName, 'fileName');

    if (g_filter.fileName.length == 0) {
        g_matched_field.fileName = true;
    } else {
        for (var i = 0; i < g_filter.fileName.length; ++i) {
            if (log_info.detail.fileName == g_filter.fileName[i]) {
                g_matched_field.fileName = true;
                row_exist = true;
                break;
            }
        }
    }

    // Check funct
    filterField(g_filter.funct, log_info.detail.funct, 'funct');

    // TODO: Check codeLine
    g_matched_field.codeLine = true;

    // TODO: Check message
    g_matched_field.message = true;

}


// .............................................................................
function filterField(filter_field, log_field, field)
{
    if (filter_field.length == 0) {
        setMatched(field);
    } else {
        for (var i = 0; i < filter_field.length; ++i) {
            if (log_field == filter_field[i]) {
                setMatched(field);
                break;
            }
        }
    }
}


// .............................................................................
function setMatched(fieldName)
{
    // alert(fieldName);
    switch(fieldName) {
    case 'nodeName' : g_matched_field.nodeName = true; break;
    case 'domain'   : g_matched_field.domain   = true; break;
    case 'cpu'      : g_matched_field.cpu      = true; break;
    case 'procName' : g_matched_field.porcName = true; break;
    case 'vpid'     : g_matched_field.vpid     = true; break;
    case 'vtid'     : g_matched_field.vtid     = true; break;
    case 'fileName' : g_matched_field.fileName = true; break;
    case 'funct'    : g_matched_field.funct    = true; break;
    //case 'message'  : g_matched_field.message  = true; break;
    }
}


// .............................................................................
function buildRow(log_info)
{
    var log = "<tr id=" + log_info.index + ">";

    if (g_column_status.index)
        log += buildTd(log_info, 'index');
    if (g_column_status.timestamp)
        log += buildTd(log_info, 'timestamp');
    if (g_column_status.nodeName)
        log += buildTd(log_info, 'nodeName');
    if (g_column_status.logLine)
        log += buildTd(log_info, 'logLine');
    if (g_column_status.domain)
        log += buildTd(log_info, 'domain');
    if (g_column_status.cpu)
        log += buildTd(log_info, 'cpu');
    if (g_column_status.procName)
        log += buildTd(log_info, 'procName');
    if (g_column_status.vpid)
        log += buildTd(log_info, 'vpid');
    if (g_column_status.vtid)
        log += buildTd(log_info, 'vtid');
    if (g_column_status.fileName)
        log += buildTd(log_info, 'fileName');
    if (g_column_status.funct)
        log += buildTd(log_info, 'funct');
    if (g_column_status.codeLine)
        log += buildTd(log_info, 'codeLine');
    if (g_column_status.message)
        log += buildTd(log_info, 'message');

    log += "</tr>";

    return log;

}

function buildTd(log_info, fieldName)
{
    var log = '';
    switch(fieldName) {
    case 'index':
        log = "<td class='index'>"     + log_info.index             + "</td>";
        break;
    case 'timestamp':
        log = "<td class='timestamp'>" + log_info.timestamp         + "</td>";
        break;
    case 'nodeName':
        log = "<td class='nodeName'>"  + log_info.nodeName          + "</td>";
        break;
    case 'logLine':
        log = "<td class='logLine'>"   + log_info.logLine           + "</td>";
        break;
    case 'domain':
        log = "<td class='domain'>"    + log_info.domain            + "</td>";
        break;
    case 'cpu':
        log = "<td class='cpu'>"       + log_info.cpu               + "</td>";
        break;
    case 'procName':
        log = "<td class='procName'>"  + log_info.procInfo.procName + "</td>";
        break;
    case 'vpid':
        log = "<td class='vpid'>"      + log_info.procInfo.vpid     + "</td>";
        break;
    case 'vtid':
        log = "<td class='vtid'>"      + log_info.procInfo.vtid     + "</td>";
        break;
    case 'fileName':
        log = "<td class='fileName'>"  + log_info.detail.fileName   + "</td>";
        break;
    case 'funct':
        log = "<td class='funct'>"     + log_info.detail.funct      + "</td>";
        break;
    case 'codeLine':
        log = "<td class='codeLine'>"  + log_info.detail.codeLine   + "</td>";
        break;
    case 'message':
        log = "<td class='message'>"   + log_info.detail.message    + "</td>";
        break;
    }
        return log;
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
