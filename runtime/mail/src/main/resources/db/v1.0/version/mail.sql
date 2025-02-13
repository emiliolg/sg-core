-- SQL for Schema MAIL --

create table QName(MAIL, ATTACHMENT) (
	MAIL_QUEUE_ID                     int              not null,
	SEQ_ID                            int              not null,
	RESOURCE_ID                       nvarchar(128)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_ATTACHMENT          primary key (MAIL_QUEUE_ID,SEQ_ID)
);;

create table QName(MAIL, MAIL_QUEUE) (
	ID                                Identity         not null,
	FROM                              nvarchar(128)    not null,
	TO                                nvarchar(1024)   not null,
	CC                                nvarchar(1024)   not null,
	BCC                               nvarchar(1024)   not null,
	DATE                              datetime(0)      default CurrentTime not null,
	SUBJECT                           nvarchar(256)    not null,
	BODY                              clob             not null,
	SENT                              boolean          not null,
	MAIN_PROP                         nvarchar(50)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_MAIL_QUEUE          primary key (ID)
);;

create table QName(MAIL, _METADATA) (
	VERSION                           nvarchar(24)     not null,
	SHA                               nvarchar(128)    not null,
	SHA_OVL                           nvarchar(128),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

-- if NeedsCreateSequence

create sequence QName(MAIL, MAIL_QUEUE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

alter table QName(MAIL, ATTACHMENT) add constraint MAIL_QUEUE_ATTACHMENT_FK
	foreign key (MAIL_QUEUE_ID)
	references QName(MAIL, MAIL_QUEUE) (ID);;

