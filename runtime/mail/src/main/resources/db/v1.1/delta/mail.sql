
delete from  QName(MAIL, ATTACHMENT);;
delete from  QName(MAIL, MAIL_QUEUE);;

alter  table QName(MAIL, MAIL_QUEUE)
	AddColumn(RETRY                             int             not null);;

alter  table QName(MAIL, MAIL_QUEUE)
	AddColumn(STATUS                            nvarchar(50)     not null);;

