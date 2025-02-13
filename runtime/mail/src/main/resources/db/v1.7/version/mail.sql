-- SQL for Schema MAIL --

-- if NeedsCreateSequence

create sequence QName(MAIL, MAIL_QUEUE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(MAIL, ATTACHMENT) (
	MAIL_QUEUE_ID                     int                                       not null,
	SEQ_ID                            int              default 0                not null,
	RESOURCE_ID                       nvarchar(128)                             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ATTACHMENT          primary key (MAIL_QUEUE_ID,SEQ_ID)
);;

create table QName(MAIL, MAIL_QUEUE) (
	ID                                Serial(1,MAIL_QUEUE_SEQ)                  not null,
	FROM                              nvarchar(128)    default EmptyString      not null,
	TO                                nvarchar(1024)   default EmptyString      not null,
	REPLY_TO                          nvarchar(1024)   default EmptyString      not null,
	CC                                nvarchar(1024)   default EmptyString      not null,
	BCC                               nvarchar(1024)   default EmptyString      not null,
	DATE                              datetime(0)      default CurrentTime      not null,
	SUBJECT                           nvarchar(256)    default EmptyString      not null,
	BODY                              clob             default EmptyString      not null,
	SENT                              boolean          default False CheckBoolConstraint(MAIL_QUEUE_SENT_B, SENT) not null,
	MAIN_PROP                         nvarchar(50)     default EmptyString      not null,
	RETRY                             int              default 0                not null,
	STATUS                            nvarchar(50)     default 'PENDING'        not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_MAIL_QUEUE          primary key (ID)
);;

create table QName(MAIL, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

create index IndexName(MAIL, MAIL_QUEUE_FROM_IDXT)
	on QName(MAIL, MAIL_QUEUE) (FROM,TO,DATE) IndexTableSpace;;

alter table QName(MAIL, ATTACHMENT) add constraint MAIL_QUEUE_ATTACHMENT_FK
	foreign key (MAIL_QUEUE_ID)
	references QName(MAIL, MAIL_QUEUE) (ID);;

-- if NeedsSerialComment
comment on column QName(MAIL,MAIL_QUEUE).ID                is 'Serial(1,MAIL_QUEUE_SEQ)';;
-- end

