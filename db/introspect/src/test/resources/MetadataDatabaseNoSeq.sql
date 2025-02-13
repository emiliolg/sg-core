create table QName(MODEL, ADDRESS) (
	CODE                              nvarchar(64)     not null,
	ROOM                              nvarchar(64),
	STREET                            nvarchar(64),
	UPDATE_TIME                       datetime(3),

	constraint PK_ADDRESS primary key(CODE)
);;

create table QName(MODEL, ADDRESS_PHONE) (
	DOC_TYPE                          int              not null,
	DOC_CODE                          nvarchar(20)     not null,
	ADDRESS_SEQ_ID                    int              not null,
	SEQ_ID                            int              not null,
	PHONE                             int              not null,
	UPDATE_TIME                       datetime(3),

	constraint PK_ADDRESS_PHONE primary key(DOC_TYPE, DOC_CODE, ADDRESS_SEQ_ID, SEQ_ID)
);;

create table QName(MODEL, CATEGORY) (
	CODE                              nvarchar(30)     not null,
	NAME                              nvarchar(30)     not null,
	PARENT_CODE                       nvarchar(30),
	UPDATE_TIME                       datetime(3),

	constraint PK_CATEGORY primary key(CODE),
	constraint NAME_UNQT unique (NAME)
);;

create table QName(MODEL, PARTICIPANT) (
	PID                               int              not null,
	FIRST_NAME                        nvarchar(64)     not null,
	LAST_NAME                         nvarchar(64)     not null,
	ADDRESS_CODE                      nvarchar(64)     not null,
	UPDATE_TIME                       datetime(3),

	constraint PK_PARTICIPANT primary key(PID)
);;

create table QName(MODEL, PERSON) (
	DOC_TYPE                          int              not null,
	DOC_CODE                          nvarchar(20)     not null,
	FIRST_NAME                        nvarchar(64)     not null,
	LAST_NAME                         nvarchar(64)     not null,
	BIRTHDAY                          date             not null,
	SALARY                            decimal(10,2)    not null,
	SEX                               nvarchar(2)      not null,
	UPDATE_TIME                       datetime(3),

	constraint PK_PERSON primary key(DOC_TYPE, DOC_CODE)
);;

create table QName(MODEL, PERSON_ADDRESS) (
	DOC_TYPE                          int              not null,
	DOC_CODE                          nvarchar(20)     not null,
	SEQ_ID                            int              not null,
	STREET                            nvarchar(64)     not null,
	UPDATE_TIME                       datetime(3),

	constraint PK_PERSON_ADDRESS primary key(DOC_TYPE, DOC_CODE, SEQ_ID)
);;

create table QName(MODEL, PRODUCT) (
	CODE                              nvarchar(64)     not null,
	NAME                              nvarchar(64)     not null,
	MAIN_CATEGORY_CODE                nvarchar(64)     not null,
	COLOR                             int              not null,
	OPT_CATEGORY_CODE                 nvarchar(64),
	UPDATE_TIME                       datetime(3),

	constraint PK_PRODUCT primary key(CODE)
);;

create table QName(MODEL, SEQUENCER) (
	ID                                Serial() not null,
	FIRST_NAME                        nvarchar(64)     not null,
	LAST_NAME                         nvarchar(64)     not null,
	UPDATE_TIME                       datetime(3),

	constraint PK_SEQUENCER primary key(ID)
);;

create table QName(MODEL, TYPES) (
	INT1                              int              default 1 not null,
	NUM3                              decimal(3),
	NUM92                             decimal(9,2)     default 2.10,
	REAL1                             double           default 1.1,
	DATE1                             date             default CurrentDate,
	DT0                               datetime(0)      default CurrentTime,
	DT1                               datetime(1),
	DT3                               datetime(3),
	DT6                               datetime(6),
	BOOL                              boolean         default False CheckBoolConstraint(TYPES_BOOL_B, BOOL),
	STR                               nvarchar(10)    default EmptyString,

	constraint PK_TYPES primary key(INT1)
);;

create table QName(MODEL, _METADATA) (
	VERSION                           double           not null,
	SHA                               nvarchar(128)    not null,
	SHA_OVL                           nvarchar(128),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK__METADATA primary key(VERSION)
);;

alter table QName(MODEL, ADDRESS_PHONE) add constraint FK_PERSON_ADDRESS
	foreign key(DOC_TYPE, DOC_CODE, ADDRESS_SEQ_ID)
	references QName(MODEL, PERSON_ADDRESS)(DOC_TYPE, DOC_CODE, SEQ_ID);;

alter table QName(MODEL, CATEGORY) add constraint FK_PARENT
	foreign key(PARENT_CODE)
	references QName(MODEL, CATEGORY)(CODE);;

alter table QName(MODEL, PARTICIPANT) add constraint FK_ADDRESS
	foreign key(ADDRESS_CODE)
	references QName(MODEL, ADDRESS)(CODE);;

alter table QName(MODEL, PERSON_ADDRESS) add constraint FK_PERSON
	foreign key(DOC_TYPE, DOC_CODE)
	references QName(MODEL, PERSON)(DOC_TYPE, DOC_CODE);;

alter table QName(MODEL, PRODUCT) add constraint FK_MAIN_CATEGORY
	foreign key(MAIN_CATEGORY_CODE)
	references QName(MODEL, CATEGORY)(CODE);;

alter table QName(MODEL, PRODUCT) add constraint FK_OPT_CATEGORY
	foreign key(OPT_CATEGORY_CODE)
	references QName(MODEL, CATEGORY)(CODE);;

create unique index IndexName(MODEL,PERSON_UPD)
	on QName(MODEL, PERSON) (UPDATE_TIME);;

create index IndexName(MODEL,PERSON_NAME)
	on QName(MODEL, PERSON) (LAST_NAME, FIRST_NAME);;

