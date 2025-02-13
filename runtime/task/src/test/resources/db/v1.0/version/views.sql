-- SQL for Schema VIEWS --

-- if NeedsCreateSequence

create sequence QName(VIEWS, A_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(VIEWS, A) (
	ID                                Serial(1,A_SEQ)                           not null,
	NAME                              nvarchar(255)    default EmptyString      not null,
	DESC                              nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_A                   primary key (ID)
);;

create table QName(VIEWS, E) (
	R_ID                              int              default 0                not null,
	DESC                              nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_E                   primary key (R_ID)
);;

create table QName(VIEWS, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

create table QName(VIEWS, R) (
	ID                                int              default 0                not null,
	N                                 nvarchar(255)    default EmptyString      not null,
	D                                 nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_R                   primary key (ID)
);;

create view QName(VIEWS, V) as
	 select * from TableName(VIEWS,A) ;;

CommentOnView  QName(VIEWS, V) is 'select * from TableName(VIEWS,A)';;

create index IndexName(VIEWS, R_N_IDXT)
	on QName(VIEWS, R) (N) IndexTableSpace;;

-- if NeedsSerialComment
comment on column QName(VIEWS,A).ID                        is 'Serial(1,A_SEQ)';;
-- end

