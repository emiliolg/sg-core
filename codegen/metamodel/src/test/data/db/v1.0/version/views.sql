-- SQL for Schema VIEWS --

create table QName(VIEWS, A) (
	ID                                Identity         not null,
	NAME                              nvarchar(256)    not null,
	DESC                              nvarchar(256)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_A                   primary key (ID)
);;

create table QName(VIEWS, E) (
	R_ID                              int              not null,
	DESC                              nvarchar(256)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_E                   primary key (R_ID)
);;

create table QName(VIEWS, _METADATA) (
	VERSION                           nvarchar(24)     not null,
	SHA                               nvarchar(128)    not null,
	SHA_OVL                           nvarchar(128),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

create table QName(VIEWS, R) (
	ID                                Identity         not null,
	N                                 nvarchar(256)    not null,
	D                                 nvarchar(256)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_R                   primary key (ID)
);;

create view QName(VIEWS, V) as
	 select * from TableName(VIEWS,A) ;;

create index IndexName(VIEWS, R_N_IDXT)
	on QName(VIEWS, R) (N) IndexTableSpace;;

-- if NeedsCreateSequence

create sequence QName(VIEWS, A_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

