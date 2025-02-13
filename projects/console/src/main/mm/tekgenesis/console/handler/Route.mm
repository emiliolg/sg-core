package tekgenesis.console.handler;

handler ConsoleHandler on_route "/sg" {
    "/console" : Html, console;
    "/transfer/": Void, transfer, method post, body SessionTransfer;
    "/transfer/login": Void, transferLogin,  method post;
}

type ConsoleMenuItem {
    fqn : String(256);
    label : String(64);
    icon : String(32);
    badgeNumber : Int, optional;
}

type SessionTransfer {
    machine: String(256);
    token : String(256);
    targetUrl : String (1024);
}