function Holidays_Init() {

	Holidays_PopulateHolidays();
	
	Holidays_AddHandlers();
		
	$("#tbHolidayDate").datepicker({ dateFormat: "yy-m-d", firstDay: 1 });
}

function Holidays_AddHandlers() {
	$("#sbHolidays").change(function () {
		Holidays_FillEditForm(true);
		Holidays_SetLastStatus("");
	});
	
	$("#bnAddHoliday").click(function() {
		Holidays_ClearEditForm(false, true);
		$("#divHolidayEdit").slideDown(50);
		$("#hidHolidayUpdateMode").val("_create");
		Holidays_SetLastStatus("");
	});

	$("#bnDeleteHoliday").click(function(){
		var reply = confirm("This will delete selected holiday!");
        if (reply == true) {
			Holiday_Delete(function(){
				Holidays_ClearEditForm(true, true);
				Holidays_PopulateHolidays();
			});
        }
	});
	
	$("#bnHolidaySave").click(function(){
		Holidays_Save();
	});
	
	$("#bnHolidayCancel").click( function() {
		Holidays_ClearEditForm(true, true);
		Holidays_SetLastStatus("");
	});
}

function Holidays_PopulateHolidays() {

	$.getJSON(CONST_BASEURL + "holidays", function (data) {
        selectItems = { "items": [] };
        jQuery.each(data.data, function (i) {
            selectItems.items.push({ "value": data.data[i].id, "text": data.data[i].name, "default": false });
        });
        PopulateSelectBox($("#sbHolidays"), selectItems);
    });
}

function Holidays_FillEditForm(expand) {
	
	var holidayId = $("#sbHolidays :selected").val();
	$("#hidHolidayUpdateMode").val("_update");
	
	$.getJSON(CONST_BASEURL + "holidays/" + holidayId, function (data) {
		$("#tbHolidayName").val(data.data.name);
		$("#tbHolidayDate").val(data.data.date);
    });
	
	if (expand) {
		$("#divHolidayEdit").slideDown(50);
	}
}

function Holidays_ClearEditForm(collapse, deselect) {
	$("#tbHolidayName").val("");
	$("#tbHolidayDate").val("");
	
	if (collapse) {
		$("#divHolidayEdit").slideUp(50);
	}
	
	if (deselect) {
		$("#sbHolidays :selected").removeAttr("selected");
	}
}

function Holidays_Save(){
	
	var updateMode = $("#hidHolidayUpdateMode").val();
		
	var updData = {"data": {}};
	
	updData.name = $("#tbHolidayName").val();
	updData.date = $("#tbHolidayDate").val();
	
	if (updateMode == "_create") {
		updData.id = $("#sbHolidays :selected").val();
		Holidays_Create(updData, function() {
			Holidays_ClearEditForm(true, true);
			Holidays_PopulateHolidays();
		});
	} else {
		Holidays_Update(updData, function() {
			Holidays_ClearEditForm(true, true);
			Holidays_PopulateHolidays();
		});
	};
	
}

function Holidays_Create(data, successCallback){
	
	AjaxPost("holidays/", data,
	           function (result) {
	               if (result.success == true) {
	                   Holidays_SetLastStatus("Holiday '" + data.name + "' successfully created.");
	                   
	                   if (successCallback != undefined) {
	                	   successCallback();
		               }
		               
	               } else {
	                   Holidays_SetLastStatus("Holiday '" + data.name + "' failed to be created. Error from server was: '" + result.message + "'.");
	               }
	               
	           },
	           function (jqXHR, textStatus, errorThrown) {
	               alert(textStatus);
	           });
}

function Holidays_Update(data, successCallback){
	
	AjaxPut("holidays/" + $("#sbHolidays :selected").val(), data,
	           function (result) {
	               if (result.success == true) {
	                   Holidays_SetLastStatus("Holiday '" + data.name + "' successfully updated.");
	                   
	                   if (successCallback != undefined) {
	                	   successCallback();
		               }
		               
	               } else {
	                   Holidays_SetLastStatus("Holiday '" + data.name + "' failed to be updated. Error from server was: '" + result.message + "'.");
	               }
	               
	           },
	           function (jqXHR, textStatus, errorThrown) {
	               alert(textStatus);
	           });
}

function Holiday_Delete(onCompletedCallback){

	var holidayId = $("#sbHolidays :selected").val();
	var holidayName = $("#sbHolidays :selected").text();
	
    AjaxDelete("holidays/" + holidayId,
    function (result) {
        Holidays_SetLastStatus("Holiday " + holidayName + " was successfully deleted.");

        if (onCompletedCallback != undefined) {
            onCompletedCallback();
        }

    },
    function (jqXHR, textStatus, errorThrown) {
        alert("Failed: " + errorThrown);
    });

}

function Holidays_SetLastStatus(statusText) {
    $("#divHolidays_LastAction").text(statusText);
}
