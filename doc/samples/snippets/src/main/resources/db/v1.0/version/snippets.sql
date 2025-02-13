-- SQL for Schema SNIPPETS --

-- if NeedsCreateSequence

create sequence QName(SNIPPETS, ADDRESS_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SNIPPETS, EXTERNAL_ADDRESS_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SNIPPETS, QUANTIFIED_CACHE_ENTITY_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SNIPPETS, RESOURCE_HOLDER_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SNIPPETS, SIMPLE_CACHE_ENTITY_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SNIPPETS, SIMPLE_ENTITY_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(SNIPPETS, ADDRESS) (
	ID                                Serial(1,ADDRESS_SEQ)                     not null,
	PERSON_SSN                        int                                       not null,
	STREET                            nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ADDRESS             primary key (ID)
);;

create table QName(SNIPPETS, ALL_CACHE_ENTITY) (
	SIMPLE_ID                         nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ALL_CACHE_ENTITY    primary key (SIMPLE_ID)
);;

create table QName(SNIPPETS, BASIC_ENTITY) (
	ATTRIBUTE                         nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_BASIC_ENTITY        primary key (ATTRIBUTE)
);;

create table QName(SNIPPETS, CAR) (
	ID                                nvarchar(255)    default EmptyString      not null,
	MAKE                              nvarchar(255)    default EmptyString      not null,
	MODEL                             nvarchar(255)    default EmptyString      not null,
	YEAR                              nvarchar(255)    default EmptyString      not null,
	ENGINE                            nvarchar(255)    default EmptyString      not null,
	PRICE                             nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CAR                 primary key (ID)
);;

create table QName(SNIPPETS, EXTERNAL_ADDRESS) (
	ID                                Serial(1,EXTERNAL_ADDRESS_SEQ)            not null,
	STREET                            nvarchar(255)    default EmptyString      not null,
	PERSON_SSN                        int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_EXTERNAL_ADDRESS    primary key (ID)
);;

create table QName(SNIPPETS, EXTERNAL_MULTIPLE_PERSON) (
	SSN                               int              default 0                not null,
	FIRST                             nvarchar(30)     default EmptyString      not null,
	LAST                              nvarchar(30)     default EmptyString      not null,
	BIRTH                             date             default CurrentDate      not null,
	SEX                               nvarchar(50)     default 'FEMALE'         not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_EXTERNAL_MULTIPLE_PERSON primary key (SSN)
);;

create table QName(SNIPPETS, INNER_MULTIPLE_PERSON) (
	SSN                               int              default 0                not null,
	FIRST                             nvarchar(30)     default EmptyString      not null,
	LAST                              nvarchar(30)     default EmptyString      not null,
	BIRTH                             date             default CurrentDate      not null,
	SEX                               nvarchar(50)     default 'FEMALE'         not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_INNER_MULTIPLE_PERSON primary key (SSN)
);;

create table QName(SNIPPETS, MULTIPLE_INNER_ADDRESS) (
	INNER_MULTIPLE_PERSON_SSN         int                                       not null,
	SEQ_ID                            int              default 0                not null,
	STREET                            nvarchar(255)    default EmptyString      not null,
	ADDRESS_ID                        nvarchar(255)    default EmptyString      not null,
	ZIP_CODE                          nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_MULTIPLE_INNER_ADDRESS primary key (INNER_MULTIPLE_PERSON_SSN,SEQ_ID)
);;

create table QName(SNIPPETS, PERSON) (
	SSN                               int              default 0                not null,
	FIRST_NAME                        nvarchar(30)     default EmptyString      not null,
	LAST_NAME                         nvarchar(30)     default EmptyString      not null,
	BIRTHDAY                          date             default CurrentDate      not null,
	SEX                               nvarchar(50)     default 'FEMALE'         not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PERSON              primary key (SSN)
);;

create table QName(SNIPPETS, QUANTIFIED_CACHE_ENTITY) (
	ID                                Serial(1,QUANTIFIED_CACHE_ENTITY_SEQ)     not null,
	SIMPLE_ID                         nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_QUANTIFIED_CACHE_ENTITY primary key (ID)
);;

create table QName(SNIPPETS, RESOURCE_HOLDER) (
	ID                                Serial(1,RESOURCE_HOLDER_SEQ)             not null,
	R                                 nvarchar(128)                             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_RESOURCE_HOLDER     primary key (ID)
);;

create table QName(SNIPPETS, SIMPLE_CACHE_ENTITY) (
	ID                                Serial(1,SIMPLE_CACHE_ENTITY_SEQ)         not null,
	SIMPLE_ID                         nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SIMPLE_CACHE_ENTITY primary key (ID)
);;

create table QName(SNIPPETS, SIMPLE_ENTITY) (
	ID                                Serial(1,SIMPLE_ENTITY_SEQ)               not null,
	ROW                               int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SIMPLE_ENTITY       primary key (ID)
);;

create table QName(SNIPPETS, SIMPLE_PERSON) (
	SSN                               int              default 0                not null,
	FIRST                             nvarchar(30)     default EmptyString      not null,
	LAST                              nvarchar(30)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SIMPLE_PERSON       primary key (SSN)
);;

create table QName(SNIPPETS, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

alter table QName(SNIPPETS, ADDRESS) add constraint PERSON_ADDRESS_FK
	foreign key (PERSON_SSN)
	references QName(SNIPPETS, PERSON) (SSN);;

alter table QName(SNIPPETS, EXTERNAL_ADDRESS) add constraint PERSON_EXTERNAL_ADDRESS_FK
	foreign key (PERSON_SSN)
	references QName(SNIPPETS, EXTERNAL_MULTIPLE_PERSON) (SSN);;

alter table QName(SNIPPETS, MULTIPLE_INNER_ADDRESS) add constraint INNER_MULTIPLE_PERSO_67E5B1_FK
	foreign key (INNER_MULTIPLE_PERSON_SSN)
	references QName(SNIPPETS, INNER_MULTIPLE_PERSON) (SSN);;

-- if NeedsSerialComment
comment on column QName(SNIPPETS,ADDRESS).ID               is 'Serial(1,ADDRESS_SEQ)';;
comment on column QName(SNIPPETS,EXTERNAL_ADDRESS).ID      is 'Serial(1,EXTERNAL_ADDRESS_SEQ)';;
comment on column QName(SNIPPETS,QUANTIFIED_CACHE_ENTITY).ID is 'Serial(1,QUANTIFIED_CACHE_ENTITY_SEQ)';;
comment on column QName(SNIPPETS,RESOURCE_HOLDER).ID       is 'Serial(1,RESOURCE_HOLDER_SEQ)';;
comment on column QName(SNIPPETS,SIMPLE_CACHE_ENTITY).ID   is 'Serial(1,SIMPLE_CACHE_ENTITY_SEQ)';;
comment on column QName(SNIPPETS,SIMPLE_ENTITY).ID         is 'Serial(1,SIMPLE_ENTITY_SEQ)';;
-- end

