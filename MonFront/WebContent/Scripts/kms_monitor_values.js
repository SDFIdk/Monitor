// Constants
var CONST_USR = "admin";
var CONST_PWD = "adm";
var CONST_BASEURL = "http://localhost:8083/Monitor/";
//"http://kmsload49.kmsext.dk:8080/Monitor/";

var KMSMonitor_ServiceTypes = {
    "items": [
        { "value": 0, "text": "ALL", "default": true, "serviceMethods": [9, 10, 11, 12,13] },
        { "value": 1, "text": "WMS", "default": false, "serviceMethods": [1, 2] },
        { "value": 2, "text": "WFS", "default": false, "serviceMethods": [1, 3] },
        { "value": 4, "text": "WMTS", "default": false, "serviceMethods": [1, 5] },
        { "value": 5, "text": "CSW", "default": false, "serviceMethods": [1, 4, 6] },
        { "value": 6, "text": "SOS", "default": false, "serviceMethods": [1, 8] },
        { "value": 7, "text": "WCS", "default": false, "serviceMethods": [1, 7] }
    ]
};

var KMSMonitor_HttpMethods = {
    "items": [
        { "value": 1, "text": "GET", "default": true },
        { "value": 2, "text": "POST", "default": false }
    ]
};

var KMSMonitor_ServiceMethods = {
    "items": [
        { "value": 1, "text": "GetCapabilities", "default": false, "httpMethod": null, "serviceType": true },
        { "value": 2, "text": "GetMap", "default": false, "httpMethod": null, "serviceType": true },
        { "value": 3, "text": "GetFeature", "default": false, "httpMethod": null, "serviceType": true },
        { "value": 4, "text": "GetRecordById", "default": false, "httpMethod": null, "serviceType": true },
        { "value": 5, "text": "GetTile", "default": false, "httpMethod": null, "serviceType": true },
        { "value": 6, "text": "GetRecords", "default": false, "httpMethod": null, "serviceType": true },
        { "value": 7, "text": "GetCoverage", "default": false, "httpMethod": null, "serviceType": true },
        { "value": 8, "text": "DescribeSensor", "default": false, "httpMethod": null, "serviceType": true },
        { "value": 9, "text": "SOAP 1.1", "default": false, "httpMethod": 2, "serviceType": false },
        { "value": 10, "text": "SOAP 1.2", "default": false, "httpMethod": 2, "serviceType": false },
        { "value": 11, "text": "HTTP POST", "default": false, "httpMethod": 2, "serviceType": false },
        { "value": 12, "text": "HTTP GET", "default": false, "httpMethod": 1, "serviceType": false },
        { "value": 13, "text": "FTP", "default": false, "httpMethod": 1, "serviceType": false }
    ]
};

var KMSMonitor_Periods = {
    "items": [
        { "value": 0, "text": "Enter period...", "default": true },
        { "value": 1, "text": "Today", "default": false },
        { "value": 2, "text": "Yesterday", "default": false },
        { "value": 3, "text": "Last week", "default": false },
        { "value": 4, "text": "This month", "default": false },
        { "value": 5, "text": "Past six months", "default": false },
        { "value": 6, "text": "Past year", "default": false },
        { "value": 7, "text": "All", "default": false }
    ]
};

var KMSMonitor_Export = {
    "items": [
        { "value": 0, "text": "default_csv", "default": true },
        { "value": 1, "text": "stats_csv", "default": false },
        { "value": 2, "text": "default_html", "default": false },
        { "value": 3, "text": "default_xml", "default": false }
    ]
};

function GetServiceMethodNameFromId(id) {
    return $.grep(KMSMonitor_ServiceMethods.items, function (e) { return e.value == id; })[0].text;
}

function GetServiceMethodIdFromName(name) {
    return $.grep(KMSMonitor_ServiceMethods.items, function (e) { return e.text == name; })[0].value;
}

function GetHttpMethodNameFromId(id) {
    return $.grep(KMSMonitor_HttpMethods.items, function (e) { return e.value == id; })[0].text;
}

function GetHttpMethodIdFromName(name) {
    return $.grep(KMSMonitor_HttpMethods.items, function (e) { return e.text == name; })[0].value;
}

function GetServiceTypeNameFromId(id) {
    return $.grep(KMSMonitor_ServiceTypes.items, function (e) { return e.value == id; })[0].text;
}

function GetServiceTypeIdFromName(name) {
    return $.grep(KMSMonitor_ServiceTypes.items, function (e) { return e.text == name; })[0].value;
}