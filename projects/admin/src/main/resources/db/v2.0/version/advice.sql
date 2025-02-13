-- SQL for Schema ADVICE --

-- if NeedsCreateSequence

create sequence QName(ADVICE, ADVICE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(ADVICE, ADVICE) (
	ID                                Serial(1,ADVICE_SEQ)                      not null,
	DESCRIPTION                       nvarchar(255)    default EmptyString      not null,
	TYPE                              nvarchar(50)     default 'INDEX_MISSING'  not null,
	LEVEL                             nvarchar(50)     default 'INFO'           not null,
	STATE                             nvarchar(50)     default 'NEW'            not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	CREATION_TIME                     datetime(3)      default CurrentTime      not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_ADVICE              primary key (ID)
);;

create table QName(ADVICE, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

create index IndexName(ADVICE, ADVICE_STATE_IDXT)
	on QName(ADVICE, ADVICE) (STATE) IndexTableSpace;;

create index IndexName(ADVICE, ADVICE_CREATION_TIME_IDXT)
	on QName(ADVICE, ADVICE) (CREATION_TIME) IndexTableSpace;;

-- if NeedsSerialComment
comment on column QName(ADVICE,ADVICE).ID                  is 'Serial(1,ADVICE_SEQ)';;
-- end

