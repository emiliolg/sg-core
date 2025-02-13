package tekgenesis.console;

form EndpointsForm "Endpoints"
 on_load init
{
    endpoints : table(10), sortable {
        url "Url" : display;
        mode "Mode" : display;
        available "Available":Boolean,display;
    };
}
