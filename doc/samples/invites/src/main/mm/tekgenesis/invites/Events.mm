package tekgenesis.invites;

entity EventModel{
    host "Host": String;
    hostMail "Host Mail": String;
    event "Event Name ": String;
    time "Event Time": DateTime;
    guests "Guests": entity Guest*{
        name "Name": String;
        mail "E-mail": String;
    };
}

form InviteGenerator : EventModel
{
    id,internal,optional;
    host;
    hostMail;
    event;
    time;
    guests, table{
        name;
        mail;
    };
    horizontal, style "margin-top-20" {
        button(add_row), style "margin-right-5";
        button(remove_row);
    };

    generateInviteButton "generate invite": button, on_click generateInvite;

}

menu EventMenu{
InviteGenerator;
}
