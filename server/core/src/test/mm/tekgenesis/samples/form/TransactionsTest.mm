package tekgenesis.samples.form;

entity TransactionalEntity
    primary_key key
{
    key : String;
}

form TransactionalForm
    entity TransactionalEntity
{
    header {
        message(entity), col 12;
    };

    key;

    fail : Boolean; // Throw exception on action
    error : Boolean; // Return error on action

    button : button, on_click createEntity;

    footer {
        save : button(save);
        cancel : button(cancel);
        delete : button(delete);
    };
}