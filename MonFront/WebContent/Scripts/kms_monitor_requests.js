// Load request data from server and bind to datatable
function InitializeDatagridRequests(updateQuickLinks, onCompletedCallback) {

    var groupId = $("#tbRequest_Group :selected").val();

    $("#divRequestsGrid").empty();
    $("#divRequestsGrid").append("<table id='tbRequestsGrid'><thead><tr><th>Id</th><th>Name</th><th>Service method</th><th>Url</th><th></th><th></th></tr></thead><tbody></tbody></table>");

    $.getJSON(CONST_BASEURL + "jobs/" + groupId + "/queries", function (getRequestData) {

    	// Also update request quicklinks
    	if (updateQuickLinks == true) {
    		Request_CreateQuickLinks(getRequestData);
    	}
    	
        var gridData = [];

        jQuery.each(getRequestData.data, function (i) {
            gridData.push({
                "id": getRequestData.data[i].id,
                "name": getRequestData.data[i].name,
                "serviceMethod": getRequestData.data[i].serviceMethod,
                "url": getRequestData.data[i].url
            });
        });

        var oTable = $("#tbRequestsGrid").dataTable(
            {
                "aaData": gridData,
                "aoColumns": [
                    { "mDataProp": "id" },
                    { "mDataProp": "name" },
                    { "mDataProp": "serviceMethod" },
                    { "mDataProp": "url" },
                    { "mDataProp": "id" },
                    { "mDataProp": "id" }
                ],
                "aoColumnDefs": [
                    {
                        "aTargets": [4],
                        "bSortable": false,
                        "fnCreatedCell": function (nTd, sData, oData, iRow, iCol) {

                            var delBut = $("<button/>", {
                                text: "Show",
                                click: function () {
                            		RequestEdit_ShowForm_Edit(groupId, sData);
                                }
                            });

                            $(nTd).text("");
                            $(nTd).append(delBut);
                        }
                    },
                    {
                        "aTargets": [5],
                        "bSortable": false,
                        "fnCreatedCell": function (nTd, sData, oData, iRow, iCol) {

                            var delBut = $("<button/>", {
                                text: "Delete",
                                click: function () {
                                    var reply = confirm("This will delete selected request!");
                                    if (reply == true) {
                                        RequestEdit_Delete(sData);
                                    }
                                }
                            });

                            $(nTd).text("");
                            $(nTd).append(delBut);
                        }
                    }
                ]
            });
        
        if (onCompletedCallback != undefined) {
        	onCompletedCallback();
        }
        
    });
}

// Merge individual parameters from datastructure received from the server into an URL parameter string.
function CreateURLFromParamList(getRequestParamData) {
    var params = "";
    jQuery.each(getRequestParamData, function (i) {
        if (params != "") { params = params + "&"; }
        params = params + getRequestParamData[i].name + "=" + getRequestParamData[i].value;
    });
    return params;
}

// Format request edit form. This function can be executed at any given time to enable or disable form elements
// according to form data and related business rules. It is called at the time of initialization and every time data is 
// changed that is assosiated with business rules.
function RequestEdit_Format() {

    var selectedHttpMethodId;
    if ($("#hidRequestEdit_RequestHttpMethodId").val() != "") {
        selectedHttpMethodId = $("#hidRequestEdit_RequestHttpMethodId").val();
        $("#hidRequestEdit_RequestHttpMethodId").val("");
    } else {
        selectedHttpMethodId = $("#sbRequestEdit_HttpMethod :selected").val();
    }
    if (selectedHttpMethodId == undefined || selectedHttpMethodId == "") {
        selectedHttpMethodId = $("#hidRequestEdit_GroupHttpMethodId").val();
    }

    var selectedServiceTypeId;
    if ($("#hidRequestEdit_RequestServiceTypeId").val() != "") {
        selectedServiceTypeId = $("#hidRequestEdit_RequestServiceTypeId").val();
        $("#hidRequestEdit_RequestServiceTypeId").val("");
    } else {
        selectedServiceTypeId = $("#sbRequestEdit_ServiceType :selected").val();
    }
    if (selectedServiceTypeId == undefined) {
        selectedServiceTypeId = KMSMonitor_ServiceTypes.items[0].value;
    }

    var selectedServiceMethodId;
    if ($("#hidRequestEdit_RequestServiceMethodId").val() != "") {
        selectedServiceMethodId = $("#hidRequestEdit_RequestServiceMethodId").val();
        $("#hidRequestEdit_RequestServiceMethodId").val("");
    } else {
        selectedServiceMethodId = $("#sbRequestEdit_ServiceMethod :selected").val();
    }
    if (selectedServiceMethodId == undefined) {
        selectedServiceMethodId = KMSMonitor_ServiceMethods.items[0].value;
    };
   
    // Populate service methods. After the selectbox has been populated then selectedServiceMethodId 
    // is re-initialized because previous selectedServiceMethodId could have been invalidated because
    // of business rules applied inside the populate-function.
    RequestEdit_PopulateServiceMethod(selectedServiceTypeId, selectedHttpMethodId, selectedServiceMethodId);
    var selectedServiceMethodId = $("#sbRequestEdit_ServiceMethod :selected").val();
    $("#divRequestEdit_ServiceType").show();
    $("#divRequestEdit_HttpMethod").show();

    RequestEdit_PopulateServiceType(selectedServiceTypeId);
    RequestEdit_PopulateHttpMethod(selectedHttpMethodId);

    // If Service method is "SOAP 1.1" then show "SOAP action URL" textbox
    if ($("#sbRequestEdit_ServiceMethod :selected").val() == 9) {
        $("#divRequestEdit_SOAPActionUrl").show();
    } else {
        $("#tbRequestEdit_SOAPActionUrl").val("");
        $("#divRequestEdit_SOAPActionUrl").hide();
    }
    // FTP
    // alert($("#sbRequestEdit_ServiceMethod :selected").val());
    if ($("#sbRequestEdit_ServiceMethod :selected").val() == 13) {
    	$("#sbRequestEdit_HttpMethod").hide();
    	$("#sbRequestEditlabel").hide();
    	$("#divRequestEdit_Login").show();
    	
    }else{
    	$("#sbRequestEdit_HttpMethod").show();
    	$("#sbRequestEditlabel").show();
    	$("#divRequestEdit_Login").hide();
    }
    
}

function RequestEdit_Fill(groupId, requestId, completeCallback) {

    $.getJSON(CONST_BASEURL + "jobs/" + groupId + "/queries/" + requestId, function (getRequestData) {

        // First set hidden group fields from the group that this request belongs to
        RequestEdit_SetHiddenGroupFields(groupId, function () {

            var serviceMethodId = $.grep(KMSMonitor_ServiceMethods.items, function (e) { return e.text == getRequestData.data.serviceMethod; })[0].value;
            var httpMethodId = undefined;
            if (!ValIsNothing(getRequestData.data.queryMethod)) {
                var httpMethodId = $.grep(KMSMonitor_HttpMethods.items, function (e) { return e.text == getRequestData.data.queryMethod; })[0].value;
            }

            // When setting of hidden fields completes then continue filling the form
            $("#tbRequestEdit_Group").val($("#tbRequest_Group :selected").text());
            $("#tbRequestEdit_Name").val(getRequestData.data.name);
            $("#tbRequestEdit_Url").val(getRequestData.data.url);

            var fixedParam = false;
            if (getRequestData.data.params.length == 1) {
                if (getRequestData.data.params[0].name.toLowerCase() == "soapenvelope" ||
                    getRequestData.data.params[0].name.toLowerCase() == "cswparam" ||
                    getRequestData.data.params[0].name.toLowerCase() == "ftpfolder")
                {
                    $("#tbRequestEdit_Parameters").val(getRequestData.data.params[0].value);
                    fixedParam = true;
                }
            }

            if (!fixedParam) {
                $("#tbRequestEdit_Parameters").val(CreateURLFromParamList(getRequestData.data.params));
            }

            // Set hidden values for use with select boxes that needs to be populated first
            $("#hidRequestEdit_RequestHttpMethodId").val(httpMethodId);
            if (!ValIsNothing(getRequestData.data.queryServiceType)) {
                $("#hidRequestEdit_RequestServiceTypeId").val(GetServiceTypeIdFromName(getRequestData.data.queryServiceType));
            }
            $("#hidRequestEdit_RequestServiceMethodId").val(serviceMethodId);

            // Set validation settings
            $("#cbRequestEdit_UseSizeValidation").attr("checked", getRequestData.data.queryValidationSettings.useSizeValidation);
            $("#tbRequestEdit_NormSize").val(getRequestData.data.queryValidationSettings.normSize);
            $("#sbRequestEdit_NormSizeTolerance").val(getRequestData.data.queryValidationSettings.normSizeTolerance);
            $("#cbRequestEdit_UseTimeValidation").attr("checked", getRequestData.data.queryValidationSettings.useTimeValidation);
            $("#tbRequestEdit_normTime").val(getRequestData.data.queryValidationSettings.normTime);
            
            if (getRequestData.data.queryValidationSettings.useXpathValidation == true) {
            	$("input:radio[name=rbRequestEdit_ResponseVal]")[1].checked = true;
            	$("#cbRequestEdit_xpathExpression").val(getRequestData.data.queryValidationSettings.xpathExpression);
            	$("#tbRequestEdit_ExpectedResult").val(getRequestData.data.queryValidationSettings.expectedXpathOutput);
            } else if (getRequestData.data.queryValidationSettings.useTextValidation == true) {
            	$("input:radio[name=rbRequestEdit_ResponseVal]")[2].checked = true;
            	$("#tbRequestEdit_ExpectedResult").val(getRequestData.data.queryValidationSettings.expectedTextMatch);
            } else {
            	$("input:radio[name=rbRequestEdit_ResponseVal]")[0].checked = true;
            }
            
            $("#tbRequestEdit_login").val(getRequestData.data.login);
            $("#tbRequestEdit_password").val(getRequestData.data.password);

            if (completeCallback != undefined) {
                completeCallback();
            }
        });

    });

}

function RequestEdit_PopulateServiceType(selectedServiceTypeId) {
    $("#sbRequestEdit_ServiceType").empty();
    $.each(KMSMonitor_ServiceTypes.items, function (i) {

        var serviceType = KMSMonitor_ServiceTypes.items[i];
        var selected = false;
        var serviceTypeText = serviceType.text;
        
    	if (selectedServiceTypeId != undefined && serviceType.value == selectedServiceTypeId) { selected = true; }
    	$("#sbRequestEdit_ServiceType").append(new Option(serviceTypeText, serviceType.value, selected, selected));

    });
}

// Populate data to http method select box on request edit form
// Parameters:
// selectedHttpMethodId: Default selected http method. Not required.
function RequestEdit_PopulateHttpMethod(selectedHttpMethodId) {
    $("#sbRequestEdit_HttpMethod").empty();
    $.each(KMSMonitor_HttpMethods.items, function (i) {

        var httpMethod = KMSMonitor_HttpMethods.items[i];
        var selected = false;

        if (selectedHttpMethodId != undefined && httpMethod.value == selectedHttpMethodId) { selected = true; }

        $("#sbRequestEdit_HttpMethod").append(new Option(httpMethod.text, httpMethod.value, selected, selected));
    });
}

// Populate data to service method selectbox on request edit form
// Parameters:
// groupServiceTypeId: Id of service type defined on the group to which this request belong.
// requestHttpMethodId: Id of http method from the request. Used to determine the allowed service methods if none are specified on the service type. Not required.
// selectedServiceMethodId: Id of service method that should be selected by default. Not required.
function RequestEdit_PopulateServiceMethod(serviceTypeId, requestHttpMethodId, selectedServiceMethodId) {

    if (selectedServiceMethodId == undefined) { selectedServiceMethodId == 0; };

    $("#sbRequestEdit_ServiceMethod").empty();

    // Get specified service type JSON object
    var serviceType = $.grep(KMSMonitor_ServiceTypes.items, function (e) { return e.value == serviceTypeId; })[0];

    if (serviceType.serviceMethods.length > 0) {

        // If allowed service methods were specified for the service type, then use these service methods, but only if service methods are 
    	// allowed for specified HTTP method on the request.

        $.each(serviceType.serviceMethods, function (i) {
            var serviceMethod = $.grep(KMSMonitor_ServiceMethods.items, function (e) { return e.value == serviceType.serviceMethods[i]; })[0];

            var selected = false;
            if (serviceMethod.value == selectedServiceMethodId) { selected = true; }

            var allowedByHttpMethod = true;
            if (requestHttpMethodId != undefined){
            	if (serviceMethod.httpMethod != null && serviceMethod.httpMethod != requestHttpMethodId){
            		allowedByHttpMethod = false;
            	}
            }
            if (allowedByHttpMethod == true) {
            	$("#sbRequestEdit_ServiceMethod").append(new Option(serviceMethod.text, serviceMethod.value, selected, selected));
            };
  
        });
    } else {

        // If no allowed service methods were specified for the service type, then use all service methods allowed for 
        // specified HTTP method on the request.

        if (requestHttpMethodId != undefined) { // If at this time, no requestHttpMethodId has been specified then the service method list will be left empty.

            $.each(KMSMonitor_ServiceMethods.items, function (i) {
                var serviceMethod = KMSMonitor_ServiceMethods.items[i];

                var selected = false;
                if (serviceMethod.value == selectedServiceMethodId) { selected = true; }

                if (serviceMethod.httpMethod == requestHttpMethodId || serviceMethod.httpMethod == null) {
                    $("#sbRequestEdit_ServiceMethod").append(new Option(serviceMethod.text, serviceMethod.value, selected, selected));
                }
            });
        }
    }
}

function RequestEdit_HideForm() {
    $("#divRequestEditx").slideUp(100, function () {
        $("#bnRequestEditSave").hide();
        $("#bnRequestEditClose").hide();
        $("#divRequestQuickLinks").find("*").css({"font-weight":"normal"}); // remove bold style from quicklinks
    });
}

function RequestEdit_ShowForm() {
    $("#divRequestEditx").slideDown(100, function () {
    	$("#bnRequestEditSave").show();
        $("#bnRequestEditClose").show();
    });
}

// Open edit form in update mode
// Do all data retrieval and formatting stuff
function RequestEdit_ShowForm_Edit(groupId, requestId) {
	RequestEdit_ClearForm();
    
    $("#hidRequestEdit_Mode").val("_update");
    $("#hidRequestEdit_RequestId").val(requestId);

    RequestEdit_ShowForm();

    RequestEdit_Fill(groupId, requestId, function () {
        RequestEdit_Format();
    });

    Request_SetLastStatus("");
    
    // Set bold-style on quick link
   $("#tbQL" + requestId).css({"font-weight":"bold"});
}

function RequestEdit_SetHiddenGroupFields(groupId, onCompleteCallback) {

    var service = "jobs/" + groupId;
    AjaxGet(service, function (result) {
        var groupServiceType;
        if (result.success == true) {
            $("#hidRequestEdit_GroupId").val(groupId);
            $("#hidRequestEdit_GroupHttpMethodId").val(
                $.grep(KMSMonitor_HttpMethods.items, function (e) { return e.text == result.data.httpMethod; })[0].value);
            $("#hidRequestEdit_GroupServiceTypeId").val(
                $.grep(KMSMonitor_ServiceTypes.items, function (e) { return e.text == result.data.serviceType; })[0].value);
        }
        onCompleteCallback();
    });
}

function Request_AddEventHandlers() {

    $("#tbRequest_Group").change(function () {
            RequestEdit_ClearForm();
            RequestEdit_HideForm();
            InitializeDatagridRequests(true);
            Request_SetLastStatus("");
    });

    $("#bnRequestEditSave").click(function () {
        RequestEdit_Create(function () {
            RequestEdit_ClearForm();
            RequestEdit_HideForm();
            InitializeDatagridRequests(true);
        });
    });

    $("#bnRequestEditSaveOnly").click(function () {
        RequestEdit_Create(function () {
        	var groupId = $("#hidRequestEdit_GroupId").val();
        	var requestId = $("#hidRequestEdit_RequestId").val();
            InitializeDatagridRequests(true, function() {
            	RequestEdit_ShowForm_Edit(groupId, requestId);
            });
         
        });
    });
    
    $("#bnRequestEditClose").click(function () {
        RequestEdit_ClearForm();
        RequestEdit_HideForm();
        Request_SetLastStatus("");
    });


    $("#bnRequestNew").click(function () {

        if ($("#tbRequest_Group :selected").val() != "_select") {
            RequestEdit_ClearForm();

            $("#hidRequestEdit_Mode").val("_create");

            RequestEdit_ShowForm();

            RequestEdit_SetHiddenGroupFields(
                $("#tbRequest_Group :selected").val(),
                function () {
                    RequestEdit_Format();
                });

            $("#tbRequestEdit_Group").val($("#tbRequest_Group :selected").text());
            $("#divRequestQuickLinks").find("*").css({"font-weight":"normal"});
        }
    });
}

function RequestEdit_AddEventHandlers() {

    $("#sbRequestEdit_HttpMethod").change(function () {
        $("#hidRequestEdit_GroupHttpMethodId").val("");
        RequestEdit_Format();
    });

    $("#sbRequestEdit_ServiceMethod").change(function () {
        RequestEdit_Format();
    });

    $("#sbRequestEdit_ServiceType").change(function () {
        RequestEdit_Format();
    });
    
    $("#bnRequestEdit_RunTest").click(function () {

    	// Save changes and run test simulation.
    	
    	RequestEdit_Create(function() {
    		
    		// Refresh form with modified data
    		var groupId = $("#hidRequestEdit_GroupId").val();
        	var requestId = $("#hidRequestEdit_RequestId").val();
            InitializeDatagridRequests(true, function() {
            	RequestEdit_ShowForm_Edit(groupId, requestId);
            });
            
    		// Disable test button to prevent multiple simultanious test runs
            $("#bnRequestEdit_RunTest").attr("disabled", "disabled");
    		
            // Do the test and enable test button afterwards
    		RequestEdit_RunTest(function () {
    			$("#bnRequestEdit_RunTest").removeAttr("disabled");
    		});
    		
    	});
    });
        
}

function RequestEdit_RunTest(onSuccessCallback) {
    var groupId = $("#hidRequestEdit_GroupId").val();
    var requestId = $("#hidRequestEdit_RequestId").val();

    AjaxGet("jobs/" + groupId + "/queries/" + requestId + "/preview", function (result) {
    	if(!result.success)
    	{
    		alert("Testen kunne ikke gennemføres pga. fejl med query paramterne eller fordi servicen ikke er tilgængelig!");
    		return;
    	}
        var responseUrl = CONST_BASEURL + "image/preview/" + result.data.queryID + "?contenttype=" + result.data.content_type;
        if (result.data.content_type && result.data.content_type.substring(0, 6) == "image/") {
            $("#divResponse").hide();
            $("#divMap").show();
            $("#imgRequestEdit_Result").attr("src", responseUrl);
        } else {
            $("#divResponse").show();
            $("#divMap").hide();
            $("#txtRequestEdit_ResultResponse").attr("href", responseUrl);
        }
        $("#tbRequestEdit_normTime_Result").val(parseFloat(result.data.time * 1000).toFixed(0));
        $("#tbRequestEdit_normSize_Result").val(result.data.size);
        $("#tbRequestEdit_xpathExpression_Result").val(result.data.xpath_result);
        
        if (onSuccessCallback) {
        	onSuccessCallback();
        }
    },
    undefined, false);
}

// Create new or update existing request
function RequestEdit_Create(onCompletedCallback) {

    mode = $("#hidRequestEdit_Mode").val();

    var groupId = $("#hidRequestEdit_GroupId").val();
    var requestId = $("#hidRequestEdit_RequestId").val();

    postData = { "data": {} };

    if (mode == "_create") {
        postData.data.name = $("#tbRequestEdit_Name").val();
    }

    if ($("#tbRequestEdit_SOAPActionUrl").val() != "") {
        postData.data.soapUrl = $("#tbRequestEdit_SOAPActionUrl").val();
    } else {
        postData.data.soapUrl = "";
    }

    if ($("#sbRequestEdit_ServiceMethod :selected").val() != undefined) {
        postData.data.serviceMethod = $("#sbRequestEdit_ServiceMethod :selected").text();
    } else {
        postData.data.serviceMethod = null;
    }

    if ($("#sbRequestEdit_HttpMethod :selected").val() != undefined) {
        postData.data.queryMethod = $("#sbRequestEdit_HttpMethod :selected").text();
    } else {
        postData.data.queryMethod = GetHttpMethodNameFromId($("#hidRequestEdit_GroupHttpMethodId").val());
    }

    if ($("#sbRequestEdit_ServiceType :selected").val() != undefined) {
        postData.data.queryServiceType = $("#sbRequestEdit_ServiceType :selected").text();
    } else {
        postData.data.queryServiceType = GetServiceTypeNameFromId($("#hidRequestEdit_GroupServiceTypeId").val());
    }
    
    postData.data.url = $("#tbRequestEdit_Url").val();
    
    postData.data.login = $("#tbRequestEdit_login").val();
    postData.data.password = $("#tbRequestEdit_password").val();
    
  
    
    if (mode == "_create") {
        AjaxPost("jobs/" + groupId + "/queries", postData,
            function (result) {
                if (result.success == true) {
                    requestId = result.data.id;
                    $("#hidRequestEdit_RequestId").val(requestId); 
                    RequestEdit_UpdateValSettings(mode, groupId, requestId, onCompletedCallback);
                } else {
                    Request_SetLastStatus("Request '" + $("#tbRequestEdit_Name").val() + "' failed to be created in group '" + $("#tbRequest_Group :selected").text() + "'. Error from server was: '" + result.message + "'.");
                    onCompletedCallback();
                }
            },
            function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            });
    } else if (mode == "_update") {
        AjaxPut("jobs/" + groupId + "/queries/" + requestId, postData,
            function (result) {
                if (result.success == true) {
                    RequestEdit_UpdateValSettings(mode, groupId, requestId, onCompletedCallback);
                } else {
                    Request_SetLastStatus("Request '" + $("#tbRequestEdit_Name").val() + "' failed to be updated. Error from server was: '" + result.message + "'.");
                    onCompletedCallback();
                }
            },
            function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            });
    }
}

// Create or update validation settings for a request
function RequestEdit_UpdateValSettings(mode, groupId, requestId, onCompletedCallback) {
    // Persist request validation settings

    var postData = { "data": {} };

    var modeText;
    if (mode == "_create")
    { 
    	modeText = "created"; 
	} 
    else 
    { 
    	modeText = "updated"; 
	}

    postData.data.useSizeValidation = $("#cbRequestEdit_UseSizeValidation").is(':checked');
    postData.data.normSize = $("#tbRequestEdit_NormSize").val();
    postData.data.normSizeTolerance = $("#sbRequestEdit_NormSizeTolerance :selected").val();
    postData.data.useTimeValidation = $("#cbRequestEdit_UseTimeValidation").is(':checked');
    postData.data.normTime = $("#tbRequestEdit_normTime").val();
    
    // Response validation settings
    var rbSelected = $("input[name=rbRequestEdit_ResponseVal]:checked").val();
    postData.data.useXpathValidation = false;
	postData.data.xpathExpression = "";
	postData.data.expectedXpathOutput = "";
	postData.data.useTextValidation = false;
	postData.data.expectedTextMatch = "";
    if (rbSelected == "_xpath"){
    	postData.data.useXpathValidation = true;
        postData.data.xpathExpression = $("#cbRequestEdit_xpathExpression").val();
        postData.data.expectedXpathOutput = $("#tbRequestEdit_ExpectedResult").val();
    } else if (rbSelected == "_text") {
    	postData.data.useTextValidation = true;
    	postData.data.expectedTextMatch =  $("#tbRequestEdit_ExpectedResult").val();
    }
    
    AjaxPut("jobs/" + groupId + "/queries/" + requestId + "/validationSettings", postData,
        function (result) {
            if (result.success == true) {
                RequestEdit_UpdateParams(mode, groupId, requestId, onCompletedCallback);
            } else {
                Request_SetLastStatus("Validation data for request '" + $("#tbRequestEdit_Name").val() + "' failed to be " + modeText + ". Error from server was: '" + result.message + "'.");
                onCompletedCallback();
            }
        },
        function (jqXHR, textStatus, errorThrown) {
            alert(textStatus);
        });
}

function RequestEdit_UpdateParams(mode, groupId, requestId, onCompletedCallback) {

    var modeText;
    if (mode == "_create") { modeText = "created"; } else { modeText = "updated"; }

    var serviceMethodId = $("#sbRequestEdit_ServiceMethod :selected").val();
    var serviceTypeId = $("#sbRequestEdit_ServiceType :selected").val();
    var httpMethodId = $("#sbRequestEdit_HttpMethod :selected").val();
    var groupServiceTypeId = $("#hidRequestEdit_GroupServiceTypeId").val();
    var groupHttpMethodId = $("#hidRequestEdit_GroupHttpMethodId").val();

    var contentType = "application/x-www-form-urlencoded";
    postData = $("#tbRequestEdit_Parameters").val();

    // If service method is SOAP then include the SOAP envelope as a single URL parameter
    if (serviceMethodId == 9 || serviceMethodId == 10) {
        postData = "soapenvelope=" + encodeURIComponent(postData);
    }

    // If service typs is CSW and service method is GetRecords and Http method is POST then encode as special parameter
    if ((serviceTypeId && httpMethodId && 
    	httpMethodId == 2 && serviceTypeId == 5 && serviceMethodId == 6) || // post // csw // GetRecords // && groupServiceTypeId == 8
        ( serviceMethodId == 6 && groupHttpMethodId == 2)) { // csw // post // groupServiceTypeId == 5
        postData = "cswparam=" + encodeURIComponent(postData);
    }
    
    if(serviceMethodId == 13)
    {
    	postData = "ftpfolder="+postData; 
    }

    AjaxPost("jobs/" + groupId + "/queries/" + requestId + "/params", postData,
        function (result) {
            if (result.success == true) {
                Request_SetLastStatus("Request '" + $("#tbRequestEdit_Name").val() + "' successfully " + modeText + ".");
            } else {
                Request_SetLastStatus("Parameter data for request '" + $("#tbRequestEdit_Name").val() + "' failed to be " + modeText + ". Error from server was: '" + result.message + "'.");
            }
            onCompletedCallback();
        },
        function (jqXHR, textStatus, errorThrown) {
            alert(textStatus);
        }, contentType);
}

function RequestEdit_ClearForm() {
    $("#hidRequestEdit_GroupId").val("");
    $("#hidRequestEdit_GroupServiceTypeId").val("");
    $("#hidRequestEdit_GroupHttpMethodId").val("");
    $("#hidRequestEdit_Mode").val("");
    $("#hidRequestEdit_RequestId").val("");
    $("#hidRequestEdit_RequestHttpMethodId").val("");
    $("#hidRequestEdit_RequestServiceTypeId").val("");
    $("#hidRequestEdit_RequestServiceMethodId").val("");

    $("#tbRequestEdit_Group").val("");
    $("#tbRequestEdit_Name").val("");
    $("#tbRequestEdit_Url").val("");
    $("#tbRequestEdit_Parameters").val("");

    $("#sbRequestEdit_ServiceMethod").empty();

    $("#sbRequestEdit_HttpMethod").empty();
    $("#divRequestEdit_HttpMethod").hide();
    
    $("#tbRequestEdit_login").val("");
    $("#tbRequestEdit_password").val("");
    
    $("#divRequestEdit_SOAPActionUrl").hide();
    $("#tbRequestEdit_SOAPActionUrl").val("");

    $("#sbRequestEdit_ServiceType").empty();
    $("#divRequestEdit_ServiceType").hide();

    // Set validation settings
    $("#cbRequestEdit_UseSizeValidation").attr("checked", false);
    $("#tbRequestEdit_NormSize").val("");
    $("#sbRequestEdit_NormSizeTolerance").val(0);
    $("#cbRequestEdit_UseTimeValidation").attr("checked", false);
    $("#tbRequestEdit_normTime").val("");

    $("input:radio[name=rbRequestEdit_ResponseVal]")[0].checked = true;
    $("#tbRequestEdit_ExpectedResult").val("");
    $("#cbRequestEdit_xpathExpression").val("");
    
    // Clear validation results
    $("#txtRequestEdit_ResultResponse").attr("href", "http://");
    $("#imgRequestEdit_Result").attr("src", "http://");
    $("#tbRequestEdit_normTime_Result").val("");
    $("#tbRequestEdit_normSize_Result").val("");
    $("#tbRequestEdit_xpathExpression_Result").val("");
    $("#divResponse").hide();
    $("#divMap").hide();
}

function RequestEdit_Delete(requestId) {
    var groupId = $("#tbRequest_Group :selected").val();

    AjaxDelete("jobs/" + groupId + "/queries/" + requestId,
        function (result) {
            RequestEdit_ClearForm();
            RequestEdit_HideForm();
            Request_SetLastStatus("Request " + requestId + " was deleted from group " + groupId + ".");
            InitializeDatagridRequests(true);
        },
        function (jqXHR, textStatus, errorThrown) {
            alert("Failed: " + errorThrown);
        });

}

function Request_SetLastStatus(statusText) {

    $("#divRequest_LastAction").text(statusText);
}

function Request_PopulateSelectBoxGroups(groupsSelectBox, afterPopulateCallback) {
    $.getJSON(CONST_BASEURL + "jobs", function (data) {
        resultGroups = { "items": [] };
        resultGroups.items.push({ "value": "_select", "text": "Select group", "default": true });
        jQuery.each(data.data, function (i) {
            resultGroups.items.push({ "value": data.data[i].id, "text": data.data[i].name +" ("+data.data[i].id+")", "default": false });
        });
        PopulateSelectBox(groupsSelectBox, resultGroups);

        // Execute callback function if any provided
        if (afterPopulateCallback != undefined) {
            afterPopulateCallback();
        }
    });
}

// Create links for quick navigation to requests
function Request_CreateQuickLinks(getRequestData) {
		 
	$("#divRequestQuickLinks").empty();
	
	var first = true;
	
	 jQuery.each(getRequestData.data, function (i) {
         
		 var groupId = $("#tbRequest_Group :selected").val();
		 
		 if (first == true) {
			 first = false;
		 } else {
			 $("#divRequestQuickLinks").append(" - ");
		 }
			 
		 $("<a>", {
			 id: "tbQL" + getRequestData.data[i].id, 
			 text: getRequestData.data[i].name,
			 click: function(){
			 			$("#divRequestQuickLinks").find("*").css({"font-weight":"normal"});
			 			RequestEdit_ShowForm_Edit(groupId, getRequestData.data[i].id);
		 			},
 			href: "#"
		 }).appendTo("#divRequestQuickLinks");
	 
     });
	 
	 if (first != true) {$("<br/>&nbsp;<br/>").appendTo("#divRequestQuickLinks");}
}
