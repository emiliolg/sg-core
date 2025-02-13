-- SQL for Schema TASK_TEST --

-- if NeedsCreateSequence

create sequence QName(TASK_TEST, ISOLATED_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(TASK_TEST, ISOLATED) (
	ID                                Serial(1,ISOLATED_SEQ)                    not null,
	COUNT                             int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ISOLATED            primary key (ID)
);;

create table QName(TASK_TEST, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

-- if NeedsSerialComment
comment on column QName(TASK_TEST,ISOLATED).ID             is 'Serial(1,ISOLATED_SEQ)';;
-- end

