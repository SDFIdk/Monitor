

// Fills select-box with data from JSON object
function PopulateSelectBox(selectBox, values) {
    $(selectBox).empty();
    jQuery.each(values.items, function (i) {
        selectBox.append(new Option(values.items[i].text, values.items[i].value, values.items[i].default, values.items[i].default));
    });
};

function AjaxPost(service, data, onSuccessCallback, onErrorCallback, contentType) {

    var url = CONST_BASEURL + service;
    var dataText;

    if (contentType == undefined) {
        // Assume JSON
        contentType = "application/json; charset=UTF-8";
        dataText = JSON.stringify(data);
    } else {
        dataText = data;
    }

    $.ajax({
        type: "POST",
        dataType: "json",
        contentType: contentType,
        url: url,
        username: CONST_USR,
        password: CONST_PWD,
        cache: false,
        data: dataText,
        success: onSuccessCallback,
        error: onErrorCallback
    });
}

function AjaxPut(service, data, onSuccessCallback, onErrorCallback) {

    var url = CONST_BASEURL + service;

    $.ajax({
        type: "PUT",
        dataType: "json",
        contentType: "application/json; charset=UTF-8",
        url: url,
        username: CONST_USR,
        password: CONST_PWD,
        cache: false,
        data: JSON.stringify(data),
        success: onSuccessCallback,
        error: onErrorCallback
    });
}

function AjaxDelete(service, onSuccessCallback, onErrorCallback) {

    var url = CONST_BASEURL + service;

    $.ajax({
        type: "DELETE",
        dataType: "json",
        contentType: "application/json; charset=UTF-8",
        url: url,
        username: CONST_USR,
        password: CONST_PWD,
        cache: false,
        success: onSuccessCallback,
        error: onErrorCallback
    });
}

function AjaxGet(service, onSuccessCallback, onErrorCallback, doAuth) {

    var url = CONST_BASEURL + service;
    var usr;
    var pwd;

    if (doAuth == true) {
        usr = CONST_USR;
        pwd = CONST_PWD;
    }
    else {
        usr = null;
        pwd = null;
    }

    $.ajax({
        type: "GET",
        dataType: "json",
        contentType: "application/json; charset=UTF-8",
        url: url,
        dataType: "json",
        username: CONST_USR,
        password: CONST_PWD,
        cache: false,
        success: onSuccessCallback,
        error: onErrorCallback
    });

}

function ClearElements(div) {

    $(div).children('input').each(function () {
        if ($(this).is(':checkbox')) {
            $(this).attr("checked", false);
        }
        if ($(this).is(':text')) {
            $(this).val("");
        }
    });
}

function ValIsNothing(value) {
    if (value == undefined || value == null || value == "") {
        return true;
    } else {
        return false;
    }
}

function DoAuth(loginname, password) {
    if (loginname == CONST_USR && password == CONST_PWD) {
        $.cookie("kmsmon_loginname", loginname);
        $.cookie("kmsmon_password", password);
        return true;
    } else {
        return false;
    }
}

function CheckAut() {
    if ($.cookie("kmsmon_loginname") == CONST_USR && $.cookie("kmsmon_password") == CONST_PWD) {
        return true;
    } else {
        return false;
    }
}

function FormatNumberLength(num, length) {
    var r = "" + num;
    while (r.length < length) {
        r = "0" + r;
    }
    return r;
}