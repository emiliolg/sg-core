-- SQL for Schema TODO --

create table QName(TODO, LIST) (
	ID                                Identity         not null,
	NAME                              nvarchar(256)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_LIST                primary key (ID)
);;

create table QName(TODO, TASK) (
	ID                                Identity         not null,
	NAME                              nvarchar(100)    not null,
	DUE_DATE                          date             not null,
	COMPLETED                         boolean          not null,
	LIST_ID                           int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_TASK                primary key (ID)
);;

create table QName(TODO, _METADATA) (
	VERSION                           nvarchar(24)     not null,
	SHA                               nvarchar(128)    not null,
	SHA_OVL                           nvarchar(128),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

-- if NeedsCreateSequence

create sequence QName(TODO, LIST_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(TODO, TASK_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

alter table QName(TODO, TASK) add constraint LIST_TASK_FK
	foreign key (LIST_ID)
	references QName(TODO, LIST) (ID);;

