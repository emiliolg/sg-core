-- SQL for Schema ENTITY --

-- if NeedsCreateSequence

create sequence QName(ENTITY, ADDRESS_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(ENTITY, ADDRESS) (
	ID                                Serial(1,ADDRESS_SEQ)                     not null,
	PERSON_SSN                        int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ADDRESS             primary key (ID)
);;

create table QName(ENTITY, PERSON) (
	SSN                               int              default 0                not null,
	FIRST                             nvarchar(30)     default EmptyString      not null,
	LAST                              nvarchar(30)     default EmptyString      not null,
	BIRTH                             date             default CurrentDate      not null,
	SEX                               nvarchar(50)     default 'FEMALE'         not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PERSON              primary key (SSN)
);;

create table QName(ENTITY, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

alter table QName(ENTITY, ADDRESS) add constraint PERSON_ADDRESS_FK
	foreign key (PERSON_SSN)
	references QName(ENTITY, PERSON) (SSN);;

-- if NeedsSerialComment
comment on column QName(ENTITY,ADDRESS).ID                 is 'Serial(1,ADDRESS_SEQ)';;
-- end

