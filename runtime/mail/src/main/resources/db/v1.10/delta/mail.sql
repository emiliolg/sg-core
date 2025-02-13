alter  table QName(MAIL, ATTACHMENT)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(MAIL, MAIL_QUEUE)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

