#Mail Report

A simple project that sends invitation to a list of guest for an event.
The project provides a way to load the guest list, and then uses reporting and mailing utilities to send an invitation to each attendee.


## Basic App

This sample mm provides a simple entity representing the event, with it's name, date and guests, and a sample form to create it, and add guests. It also provides the ability to send invites for this event to all guests.

In this case, we are building a single page pdf. 
We start by creating the builder for the header block that contains a table, with a single row, and two cells, for invitation text and date, with custom font face and size.
After this we create a second block, that will only contain an external image, aligned and scaled. 
We also have a third block, in this case empty, to be added at the bottom of the page.

Finally we compose all three parts, leaving margins between them, and later provide them to the DocumentBuilder, to generate the Document.

@inline-code(doc/samples/invites/src/main/mm/tekgenesis/invites/Events.mm)


## Generate PDF Report/Invitation
The Java code to generate the invitation is shown bellow, it uses tekgenesis.report.fo to generate the report, see [Reporting](../Reporting.html) for further references. 

@inline-code(doc/samples/invites/src/main/java/tekgenesis/invites/InviteGenerator.java#createInvitePdf)

## Send by mail to all guests

Java code to send invite as attachment by mail. see [Mail](../mail.html) for futher references.
In this case the attachment file is being upload to the resource handler to be later attached. 
Mail configuration is being initialized, so they can later be used when sending the mail.
The Mail is later built, with the sender, the list of recipients, the body and header, and the attachment.
Finally the mail is sent using the configuration created earlier.

@inline-code(doc/samples/invites/src/main/java/tekgenesis/invites/InviteGenerator.java#sendInviteMail)
