package tekgenesis.showcase;

entity Synchronous
    described_by name
    auditable
    unique name
{
    name : String(40);
}

form SynchronousForm
    entity Synchronous
{
    header { message(title); };

    id, internal, optional;
    name, default now();

    click "Click" : button, on_click clicked;
    sync "Sync" : button, on_click clicked, synchronous;

    actionError "Error" : button, on_click error;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}


form SynchronousList "Synchronous List"
{
    header {
        message(title), col 12;
    };

    synchronouses    : Synchronous, table, on_change saveSynchronous, on_load loadSynchronouses {
        "Id"   : id, display;
        "Name" : name;
        creationTime;
    };

    horizontal, style "margin-top-20" {
        button(add_row), disable when forbidden(create), style "margin-right-5";
        button(remove_row), disable when forbidden(delete), on_click removeSynchronous;
    };
}

form DisabledForm {

    header { message(title); };

    first: dropdown, split {
        mainAction "Main Action": label, on_click mainAction;
        secondaryAction "Secondary Action" : label, on_click secondaryAction, disable;
    };

    click "Click" : button, on_click clicked;
    someAction "Some Action": label, on_click mainAction;
}
