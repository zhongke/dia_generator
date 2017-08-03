
// Check the status for the column status the display or hide

function initTable() {


    // Check the status for the column status the display or hide
    $('th').click(function() {
        $('.'+ $(this).attr('class')).css('display', 'none');
    });

    var show_context = false;
    // Add click event on ervery class 'traffic' display or hide
    $('tr').click(function() {
        var traffic_context_class = '.'+ ($(this).attr('class')).split(" ").pop() + '_context';

        if (show_context) {
            $(traffic_context_class).css('display', 'none');
            show_context = false;
        } else {
            // $(traffic_context_class).css('display', 'block');
            $(traffic_context_class).css('display', 'table-row');
            show_context = true;
        }
    });

}
