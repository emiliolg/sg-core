alter  table QName(MAIL, ATTACHMENT)
	SetDefault(MAIL_QUEUE_ID, 0);;

alter  table QName(MAIL, ATTACHMENT)
	SetDefault(SEQ_ID, 0);;

alter  table QName(MAIL, MAIL_QUEUE)
	SetDefault(FROM, EmptyString);;

alter  table QName(MAIL, MAIL_QUEUE)
	SetDefault(TO, EmptyString);;

alter  table QName(MAIL, MAIL_QUEUE)
	SetDefault(REPLY_TO, EmptyString);;

alter  table QName(MAIL, MAIL_QUEUE)
	SetDefault(CC, EmptyString);;

alter  table QName(MAIL, MAIL_QUEUE)
	SetDefault(BCC, EmptyString);;

alter  table QName(MAIL, MAIL_QUEUE)
	SetDefault(SUBJECT, EmptyString);;

alter  table QName(MAIL, MAIL_QUEUE)
	SetDefault(BODY, EmptyString);;

alter  table QName(MAIL, MAIL_QUEUE)
	SetDefault(SENT, False);;

alter  table QName(MAIL, MAIL_QUEUE)
	SetDefault(MAIN_PROP, EmptyString);;

alter  table QName(MAIL, MAIL_QUEUE)
	SetDefault(RETRY, 0);;

alter  table QName(MAIL, MAIL_QUEUE)
	SetDefault(STATUS, 'PENDING');;

