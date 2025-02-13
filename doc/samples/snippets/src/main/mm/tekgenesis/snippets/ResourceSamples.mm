package tekgenesis.snippets;

entity ResourceHolder{
r:Resource;
}


form ResourceHolderForm "Resource Holder Form"
    entity ResourceHolder
{
    header {
        message(entity), col 12;
    };
    "Id" : id, internal, optional;
    "R"  : r;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}
menu ResourcesMenu{
ResourceHolderForm;
}


