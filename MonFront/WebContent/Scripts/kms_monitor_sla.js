function Sla_Init() {

    Sla_PopulateSlaSelectBox();
    Sla_PopulatePeriodTimeInterval();

    $("#bnSlaAddPeriod").click(function () {
        Sla_SetLastStatus("");
        if ($("#sbSla :selected").val() != "_newsla") {
            $("#hidSlaPeriodUpdateMode").val("_new");
            Sla_ClearSlaPeriodForm(true);
            $("#sbSlaPeriods").val("");
            $("#divSlaPeriodEdit").slideDown(50);
        }
    });

    $("#bnSlaSavePeriod").click(function () {
        Sla_UpdatePeriod(function () {
            Sla_ClearSlaPeriodForm(true);
            Sla_PopulateSlaPeriod();
        });
    });

    $("#bnSlaSave").click(function () {
        Sla_Update(function () {
            Sla_ClearSlaForm();
            Sla_PopulateSlaSelectBox();
            Sla_ClearSlaPeriodForm(true);
        });
    });

    $("#sbSla").change(function () {
        Sla_SetLastStatus("");
        if ($("#sbSla :selected").val() != "_newsla") {
            Sla_FillSlaForm();
        } else {
            Sla_ClearSlaForm();
        }
        Sla_ClearSlaPeriodForm(true);
    });

    $("#sbSlaPeriods").change(function () {
        Sla_SetLastStatus("");
        $("#hidSlaPeriodUpdateMode").val("_update");
        Sla_ClearSlaPeriodForm(false);
        $("#divSlaPeriodEdit").slideDown(50);
        Sla_FillSlaPeriodForm();
    });

    $("#bnSlaDeletePeriod").click(function () {
        
        if ($("#sbSlaPeriods :selected").val() != undefined) {
            var reply = confirm("This will delete selected period!");
            if (reply == true) {
                Sla_DeleteSlaPeriod(function () {
                    Sla_ClearSlaPeriodForm(true);
                    Sla_PopulateSlaPeriod();
                });
            }
        }
        
    });

    $("#bnDeleteSla").click(function () {
        if ($("#sbSla :selected").val() != "_newsla") {
            var reply = confirm("This will delete selected SLA!");
            if (reply == true) {
                Sla_DeleteSla(function () {
                    Sla_ClearSlaPeriodForm(true);
                    Sla_ClearSlaForm();
                    Sla_PopulateSlaSelectBox();
                });
            }
        }
    });

    $("#bnSlaCancel").click(function () {
        Sla_ClearSlaPeriodForm(true);
        Sla_ClearSlaForm();
        Sla_SetLastStatus("");
        $("#sbSla").val("_newsla");
    });

    $("#bnSlaPeriodCancel").click(function () {
        Sla_ClearSlaPeriodForm(true);
        Sla_SetLastStatus("");
        $("#sbSlaPeriods").val("");
    });

    $("#tbSlaPeriodSpecificDate").datepicker({ dateFormat: "yy-m-d", firstDay: 1 });

}

function Sla_PopulatePeriodTimeInterval() {
    for (var hour = 0; hour < 24; hour++) {
        var hourTxt = "";
        if (hour < 10) hourTxt = "0";
        hourTxt = hourTxt + hour + ":00:00";

        $("#sbTimeIntFrom").append(new Option(hourTxt, hourTxt, false, false));
        $("#sbTimeIntTo").append(new Option(hourTxt, hourTxt, false, false));
    }
}

function Sla_PopulateSlaSelectBox() {
    $.getJSON(CONST_BASEURL + "sla", function (data) {
        selectItems = { "items": [] };
        selectItems.items.push({ "value": "_newsla", "text": "Create new SLA", "default": true });
        jQuery.each(data.data, function (i) {
            selectItems.items.push({ "value": data.data[i].id, "text": data.data[i].name, "default": false });
        });
        PopulateSelectBox($("#sbSla"), selectItems);
    });
}

function Sla_PopulateSlaPeriod() {

    var slaId = $("#sbSla :selected").val();

    $.getJSON(CONST_BASEURL + "sla/" + slaId + "/period", function (data) {
        selectItems = { "items": [] };
        jQuery.each(data.data, function (i) {
            selectItems.items.push({ "value": data.data[i].id, "text": data.data[i].name, "default": false });
        });
        PopulateSelectBox($("#sbSlaPeriods"), selectItems);
    });
}

function Sla_ClearSlaForm() {
    $("#tbSlaName").val("");
    $("#cbSlaInspire").attr("checked", false);
    $("#cbSlaMeasureFirstByte").attr("checked", false);
    $("#sbSlaPeriods").empty();
}

function Sla_ClearSlaPeriodForm(doCollapse) {

    $("#tbSlaPeriodName").val("");

    $("#sbTimeIntFrom").val("00:00:00");
    $("#sbTimeIntTo").val("00:00:00");

    $("input[name=rbSlaPeriodInclExcl][value=_include]").attr("checked", true);
    
    $("#tbSlaPeriodSpecificDate").val("");
    $("input[name=rbSlaPeriodDate][value=_general]").attr("checked", true);
    
    $("#cbSlaPeriodMon").attr("checked", false);
    $("#cbSlaPeriodTue").attr("checked", false);
    $("#cbSlaPeriodWed").attr("checked", false);
    $("#cbSlaPeriodThu").attr("checked", false);
    $("#cbSlaPeriodFri").attr("checked", false);
    $("#cbSlaPeriodSat").attr("checked", false);
    $("#cbSlaPeriodSun").attr("checked", false);
    $("#cbSlaPeriodHolidays").attr("checked", false);
    
    if (doCollapse == true) {
        $("#divSlaPeriodEdit").slideUp(50);
    }

}

function Sla_FillSlaForm() {

    var slaId = $("#sbSla :selected").val();

    $.getJSON(CONST_BASEURL + "sla/" + slaId, function (data) {
        $("#tbSlaName").val(data.data.name);
        $("#cbSlaInspire").attr("checked", data.data.isExcludeWorst);
        $("#cbSlaMeasureFirstByte").attr("checked", data.data.isMeasureTimeToFirst);
    });

    Sla_PopulateSlaPeriod();
}

function Sla_Update(onCompletedCallback) {
    var updateMode;
    var slaId = $("#sbSla :selected").val();

    var postData = { "data": {} };
    postData.data.name = $("#tbSlaName").val();
    postData.data.isExcludeWorst = $("#cbSlaInspire").is(':checked');
    postData.data.isMeasureTimeToFirst = $("#cbSlaMeasureFirstByte").is(':checked');

    if (slaId == "_newsla") {

        AjaxPost("sla/", postData,
           function (result) {
               if (result.success == true) {
                   Sla_SetLastStatus("SLA '" + $("#tbSlaName").val() + "' successfully created.");
               } else {
                   Sla_SetLastStatus("SLA '" + $("#tbSlaName").val() + "' failed to be created. Error from server was: '" + result.message + "'.");
               }
               if (onCompletedCallback != undefined) {
                   onCompletedCallback();
               }
           },
           function (jqXHR, textStatus, errorThrown) {
               alert(textStatus);
           });

    } else {

        AjaxPut("sla/" + slaId, postData,
            function (result) {
                if (result.success == true) {
                    Sla_SetLastStatus("SLA '" + $("#tbSlaName").val() + "' successfully updated.");
                } else {
                    Sla_SetLastStatus("SLA '" + $("#tbSlaName").val() + "' failed to be updated. Error from server was: '" + result.message + "'.");
                }
                if (onCompletedCallback != undefined) {
                    onCompletedCallback();
                }
            },
            function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            });

    }

}

function Sla_UpdatePeriod(onCompletedCallback) {

    var slaId = $("#sbSla :selected").val();
    var updateMode = $("#hidSlaPeriodUpdateMode").val();
    
    var slaPeriodId;
    if (updateMode == "_update") {
        slaPeriodId = $("#sbSlaPeriods :selected").val();
    } 
    
    var postData = { "data": {} };
    postData.data.name = $("#tbSlaPeriodName").val();
    postData.data.slaStartTime = $("#sbTimeIntFrom :selected").text();
    postData.data.slaEndTime = $("#sbTimeIntTo :selected").text();

    if ($("input[name=rbSlaPeriodInclExcl]:checked").val() == "_include") {
        postData.data.isInclude = true;
    } else {
        postData.data.isInclude = false;
    }

    if ($("input[name=rbSlaPeriodDate]:checked").val() == "_specific") {
        postData.data.date = $("#tbSlaPeriodSpecificDate").val();
    } else {
        postData.data.date = "";
    }
    
    postData.data.isMonday = $("#cbSlaPeriodMon").is(':checked');
    postData.data.isTuesday = $("#cbSlaPeriodTue").is(':checked');
    postData.data.isWednesday = $("#cbSlaPeriodWed").is(':checked');
    postData.data.isThursday = $("#cbSlaPeriodThu").is(':checked');
    postData.data.isFriday = $("#cbSlaPeriodFri").is(':checked');
    postData.data.isSaturday = $("#cbSlaPeriodSat").is(':checked');
    postData.data.isSunday = $("#cbSlaPeriodSun").is(':checked');
    postData.data.isHolidays = $("#cbSlaPeriodHolidays").is(':checked');
    
    if (updateMode == "_new") {

        AjaxPost("sla/" + slaId + "/period", postData,
            function (result) {
                if (result.success == true) {
                    Sla_SetLastStatus("Period '" + $("#tbSlaPeriodName").val() + "' successfully created.");
                } else {
                    Sla_SetLastStatus("Period data for SLA '" + $("#tbSlaName").val() + "' failed to be created. Error from server was: '" + result.message + "'.");
                }
                if (onCompletedCallback != undefined) {
                    onCompletedCallback();
                }
            },
            function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            });

    } else {

        AjaxPut("sla/" + slaId + "/period/" + slaPeriodId, postData,
            function (result) {
                if (result.success == true) {
                    Sla_SetLastStatus("Period '" + $("#tbSlaPeriodName").val() + "' successfully updated.");
                } else {
                    Sla_SetLastStatus("Period data for SLA '" + $("#tbSlaName").val() + "' failed to be updated. Error from server was: '" + result.message + "'.");
                }
                if (onCompletedCallback != undefined) {
                    onCompletedCallback();
                }
            },
            function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            });
    }
}

function Sla_FillSlaPeriodForm() {

    var slaId = $("#sbSla :selected").val();
    var slaPeriodId = $("#sbSlaPeriods :selected").val();

    $.getJSON(CONST_BASEURL + "sla/" + slaId + "/period/" + slaPeriodId, function (data) {

        $("#tbSlaPeriodName").val(data.data.name);
        $("#sbTimeIntFrom").val(data.data.slaStartTime);
        $("#sbTimeIntTo").val(data.data.slaEndTime);

        if (data.data.isInclude == true) {
            $("input[name=rbSlaPeriodInclExcl][value=_include]").attr("checked", true);
        } else {
            $("input[name=rbSlaPeriodInclExcl][value=_exclude]").attr("checked", true);
        }

        if (ValIsNothing(data.data.date) == false) {
            $("input[name=rbSlaPeriodDate][value=_specific]").attr("checked", true);
            $("#tbSlaPeriodSpecificDate").val(data.data.date);
        } else {
            $("input[name=rbSlaPeriodDate][value=_general]").attr("checked", true);
        }

        $("#cbSlaPeriodMon").attr("checked", data.data.isMonday);
        $("#cbSlaPeriodTue").attr("checked", data.data.isTuesday);
        $("#cbSlaPeriodWed").attr("checked", data.data.isWednesday);
        $("#cbSlaPeriodThu").attr("checked", data.data.isThursday);
        $("#cbSlaPeriodFri").attr("checked", data.data.isFriday);
        $("#cbSlaPeriodSat").attr("checked", data.data.isSaturday);
        $("#cbSlaPeriodSun").attr("checked", data.data.isSunday);
        $("#cbSlaPeriodHolidays").attr("checked", data.data.isHolidays);
        
    });
}

function Sla_SetLastStatus(statusText) {
    $("#divSla_LastAction").text(statusText);
}

function Sla_DeleteSlaPeriod(onCompletedCallback) {

    var slaId = $("#sbSla :selected").val();
    var slaPeriodId = $("#sbSlaPeriods :selected").val();

    AjaxDelete("sla/" + slaId + "/period/" + slaPeriodId,
    function (result) {
        RequestEdit_ClearForm();
        RequestEdit_HideForm();
        Sla_SetLastStatus("Period " + $("#sbSlaPeriods :selected").text() + " was deleted from SLA " + $("#sbSla :selected").text() + ".");

        if (onCompletedCallback != undefined) {
            onCompletedCallback();
        }

    },
    function (jqXHR, textStatus, errorThrown) {
        alert("Failed: " + errorThrown);
    });

}

function Sla_DeleteSla(onCompletedCallback) {

    var slaId = $("#sbSla :selected").val();
    
    AjaxDelete("sla/" + slaId,
    function (result) {
        RequestEdit_ClearForm();
        RequestEdit_HideForm();
        Sla_SetLastStatus("SLA " + $("#sbSla :selected").text() + " was deleted.");

        if (onCompletedCallback != undefined) {
            onCompletedCallback();
        }

    },
    function (jqXHR, textStatus, errorThrown) {
        alert("Failed: " + errorThrown);
    });

}