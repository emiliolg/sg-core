package tekgenesis.mail;

enum MailStatus
{
	PENDING;
	SUCCESS;
	RETRY;
	FAILED;
}

entity MailQueue "Mail Queue"
    index from, to,date
    index date, status
{
	from "From" : String(128);
	to "To" : String(1024);
	replyTo "Reply To": String(1024);
	cc "Cc" : String(1024);
	bcc "Bcc" : String(1024);
	date "Date" : DateTime;
	subject "Subject" : String(256);
	body "Message" : String(100000);
	sent : Boolean, default false;
	mainProp:String(50);
	retry: Int;
	status: MailStatus;
	attachments : entity Attachment* described_by resourceId {
	    resourceId : Resource;
	};
}

