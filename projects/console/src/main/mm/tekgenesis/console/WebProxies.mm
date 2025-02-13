package tekgenesis.console;

form WebProxiesForm on_load loadProxies{
    proxies "Proxies" : table  {
        url : String, internal;
        webProxy "Proxy" : String, display;
        backEnds "Backends": Int, display;
        sent "Sent Messages": Int , display;
        received "Received Messages": Int , display;
        details "Details": button, on_click details, icon info;

    };

    detailsDialog: dialog {
        backends "Backends" : table, placeholder "No registered backends"  {
            name "Name" : String, display;
            connections "Connections" : Int, display;
            buffers "Req. Buffers" : Int, display;
            sentMsgs "Sent Msgs"   : Int, display;
            receivedMsgs "Received Msgs"   : Int, display;
        };

        pendindRequests "Pending Requests" : table(10), content_style "table-condensed", placeholder "No pending request"  {
            client "Client" : String, display;
            method "Method" : String, display;
            request "Resquest" : String, display, link request, target_blank;
        };

    };
}

final type ProxyStatus {
    url      : String;
    backends : ProxyBackend*;
    pendingRequests : ProxyPendingRequest*;
}

final type ProxyBackend {
    name : String;
    connections : Int;
    requiredBuffers : Int;
    sentMessages : Int;
    receivedMessages : Int;
}

final type ProxyPendingRequest {
    client : String;
    method : String;
    request : String;
}


