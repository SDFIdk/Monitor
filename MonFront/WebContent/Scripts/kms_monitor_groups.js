function PopulateSelectBox_Groups(groupsSelectBox, afterPopulateCallback) {
    $.getJSON(CONST_BASEURL + "jobs", function (data) {
        resultGroups = { "items": [] };
        resultGroups.items.push({ "value": "_new", "text": "Create new group", "default": true });
        jQuery.each(data.data, function (i) {
            resultGroups.items.push({ "value": data.data[i].id, "text": data.data[i].name, "default": false });
        });
        PopulateSelectBox(groupsSelectBox, resultGroups);

        // Execute callback function if any provided
        if (afterPopulateCallback != undefined) {
            afterPopulateCallback();
        }
    });
}

function CreateGroup() {
    var data = CollectGroupInfoFromForm("_create");

    AjaxPost("jobs", data,
        function (result) {
            if (result.success == true) {
                PopulateSelectBox_Groups($("#selGroups"), function () {
                    FillFormWithGroupInfo(result.data);
                    alert("Group sucessfully created!");
                });
            }
            else {
                alert("Error occured during group creation: " + result.message);
            }
        },
        function (jqXHR, textStatus, errorThrown) {
            alert("Unable to call group creation service due to system error: " + errorThrown + " - Status: " + textStatus);
        });
}

function UpdateGroup() {
    var data = CollectGroupInfoFromForm("_update");
    var service = "jobs/" + $("#selGroups :selected").val();

    AjaxPut(service, data,
        function (result) {
            if (result.success == true) {
                FillFormWithGroupInfo(result.data);
                alert("Group " + $("#selGroups :selected").text() + " sucessfully updated!");
            }
            else {
                alert("Error occured during group update: " + result.message);
            }
        },
        function (jqXHR, textStatus, errorThrown) {
            alert("Unable to call group update service due to system error: " + errorThrown + " - Status: " + textStatus);
        });
}

function DeleteGroup() {
    var service = "jobs/" + $("#selGroups :selected").val();

    var reply = confirm("This will delete selected group and all assosiated requests!");
    if (reply == true) {
        AjaxDelete(service,
            function (result) {
                if (result.success == true) {
                    var groupName = $("#selGroups :selected").text();
                    InitGroupForm();
                    alert("Group " + groupName + " sucessfully deleted!");
                }
                else {
                    alert("Error occured during group deletion: " + result.message);
                }
            },
            function (jqXHR, textStatus, errorThrown) {
                alert("Unable to call group deletion service due to system error: " + errorThrown + " - Status: " + textStatus);
            });
    }
}

function GetGroup() {
    var service = "jobs/" + $("#selGroups :selected").val();
    if ($("#selGroups :selected").val() != "_new" && $("#selGroups :selected").val() != undefined) {

        AjaxGet(service,
            function (result) {
                if (result.success == true) {
                    FillFormWithGroupInfo(result.data);
                }
                else {
                    alert("Error occured during group retrieval: " + result.message);
                }
            },
            function (jqXHR, textStatus, errorThrown) {
                alert("Unable to call group retrieval service due to system error: " + errorThrown + " - Status: " + textStatus);
            }, false);
    }
    else {
        InitGroupForm();
    }
}

function FillFormWithGroupInfo(data) {
    {
        $("#selGroups").val(data.id);
        $("#groupId").val(data.id);
        $("#tbGroupsGroupName").val(data.name);
        $("#selGroupsHttpMethod option:contains(" + data.httpMethod + ")").attr('selected', true);
        $("#bnGroupsTestInterval").val(data.testInterval);
        $("#bnGroupsTimeout").val(data.timeout);
        $("#cbGroupsIsAutomatic").attr("checked", data.isAutomatic);
        //$("#cbGroupsAllowsRealTime").attr("checked", data.allowsRealTime);
        //$("#cbGroupsTriggersAlerts").attr("checked", data.triggersAlerts);
        $("#cbGroupsHttpErrors").attr("checked", data.httpErrors);
        //$("#cbGroupsBizErrors").attr("checked", data.bizErrors);
        $("#cbGroupsSaveResponse").attr("checked", data.saveResponse);
        $("#cbRunSimultaneous").attr("checked", data.runSimultaneous);
        $("#cbGroupsUseTestIntervalDown").attr("checked", data.useTestIntervalDown);
        $("#bnGroupsTestIntervalDown").val(data.testIntervalDown);
    }
}

function CollectGroupInfoFromForm(_mode) {
    var data = { "data": {} };

    if (_mode == "_create") {
        data.data["name"] = $("#tbGroupsGroupName").val();
        data.data["slaStartTime"] = "00:00:00";
        data.data["slaEndTime"] = "23:59:59";
        data.data["isPublic"] = true;
        // ONLY
        data.data["serviceType"] = "ALL";
    }

    data.data["httpMethod"] = $("#selGroupsHttpMethod :selected").text();  
    data.data["testInterval"] = $("#bnGroupsTestInterval").val();
    data.data["timeout"] = $("#bnGroupsTimeout").val();
    data.data["isAutomatic"] = $("#cbGroupsIsAutomatic").is(':checked');
    data.data["allowsRealTime"] = true;//$("#cbGroupsAllowsRealTime").is(':checked');
    data.data["triggersAlerts"] = false;//$("#cbGroupsTriggersAlerts").is(':checked');
    data.data["httpErrors"] = $("#cbGroupsHttpErrors").is(':checked');
    data.data["bizErrors"] = false;//$("#cbGroupsBizErrors").is(':checked');
    data.data["saveResponse"] = $("#cbGroupsSaveResponse").is(':checked');
    data.data["useTestIntervalDown"] = $("#cbGroupsUseTestIntervalDown").is(':checked');
    data.data["testIntervalDown"] = $("#bnGroupsTestIntervalDown").val();
    return data;
}

function InitGroupForm() {
    ClearElements("#divGroupsEdit");

    PopulateSelectBox($("#selGroupsHttpMethod"), KMSMonitor_HttpMethods);
    PopulateSelectBox_Groups($("#selGroups"));

    // Default values
    $("#bnGroupsTestInterval").val("3600");
    $("#bnGroupsTimeout").val("30");
    $("#cbGroupsIsAutomatic").attr("checked", true);
    $("#cbGroupsHttpErrors").attr("checked", true);
    $("#cbGroupsUseTestIntervalDown").attr("checked",false);
    $("#bnGroupsTestIntervalDown").val("30");
    
}

function AddGroupEventHandlers() {
    $("#bnCommit").click(function () {
        if ($("#selGroups :selected").val() == "_new") {
            CreateGroup();
        }
        else {
            UpdateGroup();
        }
    });

    $("#bnDeleteGroup").click(function () {
        DeleteGroup();
    });

    $("#selGroups").change(function () {
        GetGroup();
    });
}