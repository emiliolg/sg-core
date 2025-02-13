-- SQL for Schema SG --

-- if NeedsCreateSequence

create sequence QName(SG, TASK_EXECUTION_LOG_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(SG, TASK_ENTRY) (
	NAME                              nvarchar(256)    default EmptyString      not null,
	STATUS                            nvarchar(50)     default 'NOT_SCHEDULED'  not null,
	CRON_EXPRESSION                   nvarchar(256)    default EmptyString      not null,
	DUE_TIME                          datetime(0)      default CurrentTime      not null,
	EXPIRATION_TIME                   datetime(0),
	MEMBER                            nvarchar(256)    default EmptyString      not null,
	MDC                               nvarchar(40)     default n'INVALID'       not null,
	DATA                              clob             default EmptyString      not null,
	DATA_TIME                         datetime(3),
	CURRENT_LOG_ID                    int,
	ERROR_MESSAGE                     nvarchar(1000)   default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TASK_ENTRY          primary key (NAME)
);;

create table QName(SG, TASK_EXECUTION_LOG) (
	ID                                Serial(1,TASK_EXECUTION_LOG_SEQ)          not null,
	NAME                              nvarchar(256)    default EmptyString      not null,
	MDC                               nvarchar(40)     default EmptyString      not null,
	START_TIME                        datetime(3)      default CurrentTime      not null,
	END_TIME                          datetime(3),
	MEMBER                            nvarchar(256)    default EmptyString      not null,
	DATA                              clob             default EmptyString      not null,
	DATA_TIME                         datetime(3),
	STATUS                            nvarchar(50)     default 'NOT_SCHEDULED'  not null,
	ERROR_MESSAGE                     nvarchar(1000)   default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TASK_EXECUTION_LOG  primary key (ID)
);;

create table QName(SG, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

create index IndexName(SG, TASK_EXECUTION_LOG_NAME_IDXT)
	on QName(SG, TASK_EXECUTION_LOG) (NAME,START_TIME) IndexTableSpace;;

create index IndexName(SG, TASK_EXECUTION_LOG_6D3F07_IDXT)
	on QName(SG, TASK_EXECUTION_LOG) (START_TIME) IndexTableSpace;;

alter table QName(SG, TASK_ENTRY) add constraint CURRENT_LOG_TASK_ENTRY_FK
	foreign key (CURRENT_LOG_ID)
	references QName(SG, TASK_EXECUTION_LOG) (ID);;

-- if NeedsSerialComment
comment on column QName(SG,TASK_EXECUTION_LOG).ID          is 'Serial(1,TASK_EXECUTION_LOG_SEQ)';;
-- end

