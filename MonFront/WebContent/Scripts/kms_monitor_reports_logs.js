
var ReportlogsContainer = '#divLogsPlaceholder';
var ReportlogsThreshold = 0;
var ReportlogParams		= {};

$(document).ready(function() {
    //$.ajaxSetup({
    //    cache: false,
    //    contentType: "application/json; charset=UTF-8"
    //});
	
	$("#bnDisplayReport").click(function () {
		$(ReportlogsContainer).empty();
		
		ReportlogParams = {
			'groupid':		$("#sbReportGroup :selected").val(),
			'mindate':		$("#tbPeriodFrom").val(),
			'maxdate':		$("#tbPeriodTo").val(),
			'requestid':	$("#sbReportRequest :selected").val(),
			'exportid':		$("#sbExport :selected").text(),
			'slatext':		$("#sbReportSLA :selected").val(),
			'slaval':		$("#sbReportSLA :selected").val(),
			'useagg':		$("#cbUseAgg").is(":checked")
		};
	
		ReportlogsLoadRequests();
    });
	
});


function ReportlogsGetInput(key) 
{
	var result = '';
	key = key.toLowerCase();
	
	if (typeof ReportlogParams[key] != "undefined") {
		result = ReportlogParams[key];
	}
	
	return result;
}

function ReportlogsToggleRequest(id)
{
    var target = $('#request-' + id + '-agglogs');

    if (target.is(":visible")) {
        target.slideUp();
    }

	target.empty();
	
    ReportlogsLoadAggLogs(id, target);
    $(target).slideDown();
}


function ReportlogsToggleAggLog(id, logid)
{
    var target = $('#request-' + id + '-agglogs-'+ logid +'-logs');

    if (target.is(":visible")) {
        target.slideUp();
    }

    target.empty();

    ReportlogsLoadLog(id, logid, target);
    target.slideDown();
}


/*************************************************************/
/*************************************************************/


/**************  Loading and building functions  *************/



function ReportlogsLoadRequests()
{
    var requests = ReportlogsGetRequests();
	
	$.each(requests, function(index, request)
    {
        // Build request container
        var rcontainer = $('<div/>', {
            id:     'request-' + request.id,
            class:  'request',
            text:   '#' + request.id + ': ' + request.name // + ' (' + request.status +')'
        }).appendTo(ReportlogsContainer);

        // When clicked, it must toggle the request details
        rcontainer.bind('click', function(event) {
            ReportlogsToggleRequest(request.id);
        });

        // Add a status to request container
        var rstatus = $('<span/>', {
            id:     'request-' + request.id + '-status',
            class:  'request-status pull-right',
            text:   'Unavailability' //request.status
        }).appendTo(rcontainer);

        // Add container for the agglog to request container
        $('<div/>', {
            id:     'request-' + request.id + '-agglogs',
            class:  'request-agglogs',
            text:   ''
        }).appendTo(rcontainer);

        $('.request-agglogs').hide();
    });
}


function ReportlogsLoadAggLogs(id, target)
{
    var items = ReportlogsGetAggLogs(id);

    $.each(items, function(index, item)
    {
		//var level = parseInt(item.h24Unavailability * 100);
		var level = parseInt(parseFloat(item.unavailability) * 100);
		var status = 'bad'; 
		var date_id = item.date.replace(' ', '-').replace(':', '-');
		
		if (level <= ReportlogsThreshold) {
			status = 'good';
			
			// Break as we don't want to show lines below threshold
			return true;
		}
		
        // Selectable dates goes into this
        var agglog = $('<div/>', {
            id:     target.attr('id') + '-' + date_id,
            class:  'request-agglog ' + status,
            text:   item.date.substr(0, 10)
        }).appendTo(target);

        // When clicked, it must toggle the content
        agglog.bind('click', function(event) {
            event.stopPropagation();
            ReportlogsToggleAggLog(id, date_id);
        });
		
		// Set percentage status
		var agglogstatus = $('<div/>', {
            id:     target.attr('id') + '-' + date_id + '-status',
            class:  'request-agglog-status',
            text:   (level / 100) + '%'
        }).appendTo(agglog);
		
        // Container to hold the actual loglines
        var agglogcontainer = $('<div/>', {
            id:     target.attr('id') + '-' + date_id + '-logs',
            class:  'request-agglog-logs',
            text:   ''
        }).appendTo(agglog);
    });
}


function ReportlogsLoadLog(id, logid, target)
{
    var items = ReportlogsGetLog(id, logid);
	var i = 0;
	
    $.each(items, function(index, item)
    {
        // Write actual loglines
        var parent = $('<div/>', {
            id:     target.attr('id') + '-log',
            class:  'request-agglog-logs-log',
            text:   '',	// item.start + ' -> ' + item.end + ' -> [' + item.length.h +':'+ item.length.m + ':' + item.length.s +'] ->'+ item.message
        }).appendTo(target);
		
		$('<div/>', {
			id:     parent.attr('id') + '-min',
            class:  'request-agglog-logs-log-min',
            text:   item.start
        }).appendTo(parent);
		
		$('<div/>', {
			id:     parent.attr('id') + '-max',
            class:  'request-agglog-logs-log-max',
            text:   item.end
        }).appendTo(parent);
		
		$('<div/>', {
			id:     parent.attr('id') + '-length',
            class:  'request-agglog-logs-log-length',
            text:   item.length.h +':'+ item.length.m + ':' + item.length.s
        }).appendTo(parent);
		
		$('<div/>', {
			id:     parent.attr('id') + '-message',
            class:  'request-agglog-logs-log-message',
            text:   item.message
        }).appendTo(parent);
    });
}


/*************************************************************/
/*************************************************************/


/*****************  Get and Parse functions  *****************/


function ReportlogsGetRequests()
{
    var result = false;

    $.ajax({
        url: CONST_BASEURL + "jobs/" + ReportlogsGetInput("groupid") + "/queries",
        cache: false,
        async: false
        //dataType: 'text'
    })
        .fail(function( jqXHR, textStatus ) {
            alert( "Could not load requests: " + textStatus );
        })
        .done(function( json ) {
            result = json.data;
        })
        , "text";

    return result;
}


function ReportlogsGetAggLogs(id)
{
    var result = false;
	var usedSla = ReportlogsGetInput("slaval");
	var slaParam = (usedSla != "_nosla" ? "&useSla="+usedSla : "");
	
	$.ajax({
		url: CONST_BASEURL + "jobs/" + ReportlogsGetInput("groupid") + "/queries/" + id + "/aggLogs?minDate=" + ReportlogsGetInput("mindate") + "&maxDate=" + ReportlogsGetInput("maxdate") + "&servicelog=true" + slaParam,
        cache: false,
        async: false
        //dataType: 'text'
    })
        .fail(function( jqXHR, textStatus ) {
            alert( "Could not load aggLog: " + textStatus );
        })
        .done(function( json ) {
            result = json.data;
        })
        , "text";

    return result;
}


function ReportlogsGetLog(id, logid)
{
    var results = false;

    $.ajax({
		url: CONST_BASEURL + "jobs/" + ReportlogsGetInput("groupid") + "/queries/" + id + "/logs?minDate=" + logid + "&maxDate=" + logid,
        cache: false,
        async: false
        //dataType: 'text'
    })
        .fail(function( jqXHR, textStatus ) {
            alert( "Could not load log: " + textStatus );
        })
        .done(function( json ) {
            results = json.data;
        })
        , "text";

	var incidents	= [];
	var current		= {
					'code':		'AVAILABLE',
					'time':		'0',
					'mesg':		''
	};
	
	results.reverse();
	var len = results.length;
	
	$.each(results, function(index, response)
	{
		if (current.code == 'UNAVAILABLE' && (response.statusCode == 'AVAILABLE' || len - 1 == index))
		{
			// end incident registration
			var event = {
				'code':		current.code,
				'start':	current.time,
				'message':	current.mesg,
				'end':		ReportlogsGetTime(response.time),
				'length':	0
			};
			
			// Calculate event length
			event.length = ReportlogsGetTimeDiff(event.start, event.end);
			
			// Reset status
			current.code = 'AVAILABLE';
			current.time = '0';
			current.mesg = '';
			
			incidents.push(event);
		}
		else if (response.statusCode == 'UNAVAILABLE')
		{
			// Register incident
			current.code = response.statusCode;
			current.mesg = response.message;
			
			if (current.time == '0')
			{
				current.time = ReportlogsGetTime(response.time);
			}
		}
	});
	
	return incidents;
}


/*************************************************************/
/*************************************************************/


/************************  Helpers  *************************/


function ReportlogsGetTime(string)
{
	return string.substr(11,8);
}


function ReportlogsGetTimeDiff(start, end) 
{
	// Calculate time diff (will be in milliseconds)
	var date1 = new Date(2000, 0, 1,  start.substr(0, 2), start.substr(3, 2), start.substr(6, 2));
	var date2 = new Date(2000, 0, 1, end.substr(0,2), end.substr(3, 2), end.substr(6, 2));
	if (date2 < date1) {
		date2.setDate(date2.getDate() + 1);
	}
	var length = date2 - date1;
	
	var msec = length;
	var hh = Math.floor(msec / 1000 / 60 / 60);
	msec -= hh * 1000 * 60 * 60;
	var mm = Math.floor(msec / 1000 / 60);
	msec -= mm * 1000 * 60;
	var ss = Math.floor(msec / 1000);
	msec -= ss * 1000;
	
	return {
			'time':	(length / 1000),
			'h':	ReportlogsFormatDecimal(hh),
			'm':	ReportlogsFormatDecimal(mm),
			's':	ReportlogsFormatDecimal(ss)
	};
}


function ReportlogsFormatDecimal(n) {
    return n > 9 ? "" + n: "0" + n;
}
