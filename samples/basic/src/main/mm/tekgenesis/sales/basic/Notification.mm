package tekgenesis.sales.basic;

form EmailForm "Simple Text Email Form" {

    "Email Provider " : vertical {

        host "Host": String(40), default "smtp.gmail.com";
        port "Port" : Int , default 587;
        username "Username": String(40);
        password "Password" :String(24),password_field;
    };

    "Email " : vertical {

        from "From": String(128),text_field(128);
        to "To" : String(128),text_field(128);
        cc "CC": String(128),text_field(128);
        bcc "BCC" :String(128),text_field(128);
        subject "Subject": String(128),text_field(128);
        body "Body [max 512 characters]": text_area(10, 80);
    };
    footer {
        send "Send": button , on_click sendEmail;
    };

}