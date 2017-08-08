// JSON model analyzer
// Load the log information based on the various conditions
// .............................................................................
// Defintion of all the field strings
// .............................................................................
v_index     = 'index';
v_timestamp = 'timestamp';
v_nodeName  = 'nodeName';
v_logLine   = 'logLine';
v_domain    = 'domain';
v_cpu       = 'cpu';
v_procName  = 'procName';
v_vpid      = 'pid';
v_vtid      = 'tid';
v_fileName  = 'fileName';
v_funct     = 'funct';
v_codeLine  = 'codeLine';
v_message   = 'message';


// TODO:
// Add a filter for doamin
// Load all the domain in the set for later filtering in a list
// .............................................................................
var g_filter = {
    index     : [],
    timestamp : [],
    logLine   : [],
    domain    : ['traffic_pcc_tasks', 'traffic_pcc_db_session'],
    domain    : [],
    cpu       : [],
    // TODO:
    // procName  : ['pcrf-proc'],
    procName  : [],
    vtid      : [],
    // TODO:
    // fileName  : ['RxRetrieveAndMarkNetlocTask.cc'],
    fileName  : [],
    funct     : [],
    codeLine  : [],
    // TODO:
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

// Global varialbe to store all index of traffic log
var g_traffic_list = [];

// Global variable to store matched field status for filtering
// .............................................................................
var g_matched_field = {};
// .............................................................................
function initTable() {
    // Load the json file and the information step bu step
    // 1) load traffic message
    var items = [];
    var header = "<tr id='header'>"
        + "<th id='" + v_index     + "' class='" + v_index     + "'> NUM   </th>"
        + "<th id='" + v_timestamp + "' class='" + v_timestamp + "'> TIME  </th>"
        + "<th id='" + v_nodeName  + "' class='" + v_nodeName  + "'> NODE  </th>"
        + "<th id='" + v_logLine   + "' class='" + v_logLine   + "'> LINE  </th>"
        + "<th id='" + v_domain    + "' class='" + v_domain    + "'> DOMAIN</th>"
        + "<th id='" + v_cpu       + "' class='" + v_cpu       + "'>  CPU  </th>"
        + "<th id='" + v_procName  + "' class='" + v_procName  + "'> _PROC_</th>"
        + "<th id='" + v_vpid      + "' class='" + v_vpid      + "'> vPID  </th>"
        + "<th id='" + v_vtid      + "' class='" + v_vtid      + "'> vTID  </th>"
        + "<th id='" + v_fileName  + "' class='" + v_fileName  + "'> FILE  </th>"
        + "<th id='" + v_funct     + "' class='" + v_funct     + "'> FUNC  </th>"
        + "<th id='" + v_codeLine  + "' class='" + v_codeLine  + "'> LINE  </th>"
        + "<th id='" + v_message   + "' class='" + v_message   + "'>  MSG  </th>"
        + "</tr>";


    items.push(header);
    for (var i = 0; i < data.length; ++i) {
        var log_info = data[i];
        // Add the traffic line into the table
        if (log_info.detail.message.includes('DIAMETER-MESSAGE')) {
            log = "<tr id=" + log_info.index + " class='traffic'>";
            log += buildTd(log_info, v_index);
            log += buildTd(log_info, v_timestamp);
            log += buildTd(log_info, v_nodeName);
            log += buildTd(log_info, v_logLine);
            log += buildTd(log_info, v_domain);
            log += buildTd(log_info, v_cpu);
            log += buildTd(log_info, v_procName);
            log += buildTd(log_info, v_vpid);
            log += buildTd(log_info, v_vtid);
            log += buildTd(log_info, v_fileName);
            log += buildTd(log_info, v_funct);
            log += buildTd(log_info, v_codeLine);
            log += buildTd(log_info, v_message);
            log += "</tr>";

            items.push(log);
            g_traffic_list.push(i);
        }
    }

    for (var i = 0; i < g_traffic_list.length; ++i) {
        // Use a key value pair to store the current element status sperately
        g_context_exist[g_traffic_list[i]] = false;
    }

    $( "<table/>", {
        html: items.join( "" )
    }).appendTo( "body" );

    // Load the function here
    hideColumn();
    showContext(g_traffic_list);

    search();

    // Test doFiltering
    //doFiltering();

}

// Check the status for the column status the display or hide
// .............................................................................
function hideColumn()
{
    $('th').click(function() {
        var tr_class = $(this).attr('class');
        $('.'+ tr_class).css('display', 'none');

        switch(tr_class) {
            case v_index     : g_column_status.index     = false; break;
            case v_timestamp : g_column_status.timestamp = false; break;
            case v_nodeName  : g_column_status.nodeName  = false; break;
            case v_logLine   : g_column_status.logLine   = false; break;
            case v_domain    : g_column_status.domain    = false; break;
            case v_cpu       : g_column_status.cpu       = false; break;
            case v_procName  : g_column_status.procName  = false; break;
            case v_vpid      : g_column_status.vpid      = false; break;
            case v_vtid      : g_column_status.vtid      = false; break;
            case v_fileName  : g_column_status.fileName  = false; break;
            case v_funct     : g_column_status.funct     = false; break;
            case v_codeLine  : g_column_status.codeLine  = false; break;
            case v_message   : g_column_status.message   = false; break;
        }
    });
}


// .............................................................................
function showContext(g_traffic_list)
{
    for (var i = 0; i < g_traffic_list.length; ++i) {
        // Use a key value pair to store the current element status sperately
        $('#' + g_traffic_list[i]).click(function(){
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
    for (var i = parseInt(id) + 1; i < data.length; ++i) {
        if (! data[i].detail.message.includes('DIAMETER-MESSAGE')) {
            $('table tr#' + i).remove();
        } else {
            break;
        }
    }
}

// .............................................................................
function insertContext(id, trafficObject)
{
    context = [];
    for (var i = parseInt(id) + 1; i < data.length; ++i) {
        // Add the traffic line into the table
        var log_info = data[i];
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



// .............................................................................
function search()
{
    // Get all the fields value that you want to filter and put them into
    // global filter object
    $('#filter').click(function(){
        // TODO: before filter the context, remove all the exist cntext firstly

        for (var traffic_idx = 0; traffic_idx < g_traffic_list.length; traffic_idx++) {
            if (g_context_exist[traffic_idx] == true) {
                removeContext(g_traffic_list[traffic_idx]);
            }
        }

        // RESET filter status and set page status to default only traffic message
        // was shwn

        // domain
        var i_domain = $('#f_domain').val();
        // split value by space
        if (i_domain != '') {
            var elements = i_domain.split(',');
            for (var i = 0; i < elements.length; i++) {
                g_filter.domain.push($.trim(elements[i]));
            }
        }

        // fileName
        var i_fileName = $('#f_fileName').val();
        // split value by space
        if (i_fileName != '')
            g_filter.fileName.push(i_fileName);

        // funct
        var i_funct = $('#f_funct').val();
        // split value by space
        if (i_funct != '')
            g_filter.funct.push(i_funct);


        // message
        var i_message = $('#f_message').val();
        // split value by space
        if (i_message != '')
            g_filter.message.push(i_message);

        doFiltering();
    });


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
    var i = data.length - 1;
    for (var traffic_idx = g_traffic_list.length- 1 ; traffic_idx >= 0; traffic_idx--) {

        var traffic_id = g_traffic_list[traffic_idx];
        for (; i >= 0; i--) {
            // Add the traffic line into the table
            var log_info = data[i];

            // Iterate in a reverse order
            // If current index is greater than the traffic index
            // which means this line of log belong to the current traffic context
            // current traffic context is shown
            if (log_info.index >= traffic_id) {
                if (log_info.index == traffic_id) {
                    // Before insert the matched context, remove all the context firstly
                    if (g_context_exist[traffic_id] == true) {
                        removeContext(traffic_idx);
                    } else {
                        // TODO: Set the current traffic context status is shown
                        // if one more click, all the context will be removed.
                        // if click again, the context should like before filtered.
                        // So, it's better keep the filtered context in the global
                        // status.
                        // If no filtered history the status should be empty or false
                        // that could introduce more complex feature to store the
                        // filter HISTORY
                        g_context_exist[traffic_idx] = true;
                    }

                    // Put all the info in the context into html
                    for (var ctx_idx = 0; ctx_idx < context.length; ctx_idx++) {
                        $(context[ctx_idx])
                            .insertAfter($("#"+ traffic_id).closest('tr'));
                    }

                    i = traffic_id - 1;
                    // Reset the list for next traffic context handling
                    context = [];
                    break;
                } else {
                    filterAllFileds(log_info);

                    if(g_matched_field.index    && g_matched_field.timestamp
                    && g_matched_field.nodeName && g_matched_field.logLine
                    && g_matched_field.domain   && g_matched_field.cpu
                    && g_matched_field.procName && g_matched_field.vpid
                    && g_matched_field.vtid     && g_matched_field.fileName
                    && g_matched_field.funct    && g_matched_field.codeLine
                    && g_matched_field.message
                    ) {
                        context.push(buildRow(log_info));
                        // console.log(buildRow(log_info));
                    }
                }
            }
        }
    }
    // Add the matched row into html
    /*
    $.each(g_context_exist, function(key, value){
        console.log('key :[', key, '] value: [', value, ']');
    });
    */
}

function filterAllFileds(log_info)
{
    g_matched_field = {
        index    : false, timestamp : false, nodeName : false,
        logLine  : false, domain    : false, cpu      : false,
        procName : false, vpid      : false, vtid     : false,
        fileName : false, funct     : false, codeLine : false,
        message  : false

    };

    // TODO: index need to be suport single number seperate number and a range
    g_matched_field.index = true;

    // TODO: Check timestamp
    g_matched_field.timestamp = true;

    // Check nodeName
    filterField(g_filter.nodeName, log_info.nodeName, v_nodeName);

    // TODO: Check logLine
    g_matched_field.logLine = true;

    // Check domain
    filterField(g_filter.domain, log_info.domain, v_domain);

    // Check cpu
    filterField(g_filter.cpu, log_info.cpu, v_cpu);

    // TODO: Check procName
    // filterField(g_filter.procName, log_info.procInfo.procName, v_procName);

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
    filterField(g_filter.vpid, log_info.procInfo.vpid, v_vpid);

    // Check vtid
    filterField(g_filter.vtid, log_info.procInfo.vtid, v_vtid);

    // TODO: Check fileName
    // filterField(g_filter.fileName, log_info.procInfo.fileName, v_fileName);

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
    filterField(g_filter.funct, log_info.detail.funct, v_funct);

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
    case v_nodeName : g_matched_field.nodeName = true; break;
    case v_domain   : g_matched_field.domain   = true; break;
    case v_cpu      : g_matched_field.cpu      = true; break;
    case v_procName : g_matched_field.porcName = true; break;
    case v_vpid     : g_matched_field.vpid     = true; break;
    case v_vtid     : g_matched_field.vtid     = true; break;
    case v_fileName : g_matched_field.fileName = true; break;
    case v_funct    : g_matched_field.funct    = true; break;
    //case v_message  : g_matched_field.message  = true; break;
    }
}


// .............................................................................
function buildRow(log_info)
{
    var log = "<tr id=" + log_info.index + ">";

    if (g_column_status.index)
        log += buildTd(log_info, v_index);
    if (g_column_status.timestamp)
        log += buildTd(log_info, v_timestamp);
    if (g_column_status.nodeName)
        log += buildTd(log_info, v_nodeName);
    if (g_column_status.logLine)
        log += buildTd(log_info, v_logLine);
    if (g_column_status.domain)
        log += buildTd(log_info, v_domain);
    if (g_column_status.cpu)
        log += buildTd(log_info, v_cpu);
    if (g_column_status.procName)
        log += buildTd(log_info, v_procName);
    if (g_column_status.vpid)
        log += buildTd(log_info, v_vpid);
    if (g_column_status.vtid)
        log += buildTd(log_info, v_vtid);
    if (g_column_status.fileName)
        log += buildTd(log_info, v_fileName);
    if (g_column_status.funct)
        log += buildTd(log_info, v_funct);
    if (g_column_status.codeLine)
        log += buildTd(log_info, v_codeLine);
    if (g_column_status.message)
        log += buildTd(log_info, v_message);

    log += "</tr>";

    return log;

}

function buildTd(log_info, fieldName)
{
    var log = '';
    switch(fieldName) {
    case v_index:
        log = "<td class='" + v_index     + "'>" + log_info.index             + "</td>";
        break;
    case v_timestamp:
        log = "<td class='" + v_timestamp + "'>" + log_info.timestamp         + "</td>";
        break;
    case v_nodeName:
        log = "<td class='" + v_nodeName  + "'>" + log_info.nodeName          + "</td>";
        break;
    case v_logLine:
        log = "<td class='" + v_logLine   + "'>" + log_info.logLine           + "</td>";
        break;
    case v_domain:
        log = "<td class='" + v_domain    + "'>" + log_info.domain            + "</td>";
        break;
    case v_cpu:
        log = "<td class='" + v_cpu       + "'>" + log_info.cpu               + "</td>";
        break;
    case v_procName:
        log = "<td class='" + v_procName  + "'>" + log_info.procInfo.procName + "</td>";
        break;
    case v_vpid:
        log = "<td class='" + v_vpid      + "'>" + log_info.procInfo.vpid     + "</td>";
        break;
    case v_vtid:
        log = "<td class='" + v_vtid      + "'>" + log_info.procInfo.vtid     + "</td>";
        break;
    case v_fileName:
        log = "<td class='" + v_fileName  + "'>" + log_info.detail.fileName   + "</td>";
        break;
    case v_funct:
        log = "<td class='" + v_funct     + "'>" + log_info.detail.funct      + "</td>";
        break;
    case v_codeLine:
        log = "<td class='" + v_codeLine  + "'>" + log_info.detail.codeLine   + "</td>";
        break;
    case v_message:
        log = "<td class='" + v_message   + "'>" + log_info.detail.message    + "</td>";
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
