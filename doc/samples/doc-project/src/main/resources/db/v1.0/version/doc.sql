-- SQL for Schema DOC --

-- if NeedsCreateSequence

create sequence QName(DOC, CATEGORY_TYPE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(DOC, SEARCHABLE_PERSON_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(DOC, APERSON) (
	SSN                               int              default 0                not null,
	FIRST                             nvarchar(30)     default EmptyString      not null,
	LAST                              nvarchar(30)     default EmptyString      not null,
	BIRTH                             date             default CurrentDate      not null,
	SEX                               nvarchar(50)     default 'FEMALE'         not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_APERSON             primary key (SSN)
);;

create table QName(DOC, CAR) (
	MODEL                             nvarchar(255)    default EmptyString      not null,
	YEAR                              int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CAR                 primary key (MODEL,YEAR)
);;

create table QName(DOC, CATEGORY_TYPE) (
	ID                                Serial(1,CATEGORY_TYPE_SEQ)               not null,
	NOTHING                           nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CATEGORY_TYPE       primary key (ID)
);;

create table QName(DOC, IMAGE) (
	PRODUCT_PRODUCT_ID                nvarchar(50)                              not null,
	SEQ_ID                            int              default 0                not null,
	IMAGE_ID                          nvarchar(128)                             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_IMAGE               primary key (PRODUCT_PRODUCT_ID,SEQ_ID)
);;

create table QName(DOC, INNER_ADDRESS) (
	A_PERSON_SSN                      int                                       not null,
	SEQ_ID                            int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_INNER_ADDRESS       primary key (A_PERSON_SSN,SEQ_ID)
);;

create table QName(DOC, PRODUCT) (
	PRODUCT_ID                        nvarchar(50)     default 'NOT_INFORMED'   not null,
	MODEL                             nvarchar(30)     default EmptyString      not null,
	DESCRIPTION                       nvarchar(100),
	PRICE                             decimal(10,2)    default 0                not null,
	STATE                             nvarchar(50)     default 'CREATED'        not null,
	CATEGORY_ID                       int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PRODUCT             primary key (PRODUCT_ID)
);;

create table QName(DOC, PRODUCT_BY_CAT) (
	PRODUCT_PRODUCT_ID                nvarchar(50)                              not null,
	SEQ_ID                            int              default 0                not null,
	SECONDARY_CATEGORY                nvarchar(50)     default 'cat'            not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	CREATION_TIME                     datetime(3)      default CurrentTime      not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_PRODUCT_BY_CAT      primary key (PRODUCT_PRODUCT_ID,SEQ_ID)
);;

create table QName(DOC, SEARCHABLE_PERSON) (
	ID                                Serial(1,SEARCHABLE_PERSON_SEQ)           not null,
	NAME                              nvarchar(255)    default EmptyString      not null,
	LAST_NAME                         nvarchar(255)    default EmptyString      not null,
	BIRTHDAY                          datetime(0)      default CurrentTime      not null,
	SOME_EXTRA                        int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SEARCHABLE_PERSON   primary key (ID)
);;

create table QName(DOC, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

create index IndexName(DOC, PRODUCT_BY_CAT_UPD_7A017A_IDXT)
	on QName(DOC, PRODUCT_BY_CAT) (UPDATE_TIME) IndexTableSpace;;

create index IndexName(DOC, PRODUCT_BY_CAT_CATEGORY_IDXT)
	on QName(DOC, PRODUCT_BY_CAT) (SECONDARY_CATEGORY) IndexTableSpace;;

alter table QName(DOC, IMAGE) add constraint PRODUCT_IMAGE_FK
	foreign key (PRODUCT_PRODUCT_ID)
	references QName(DOC, PRODUCT) (PRODUCT_ID);;

alter table QName(DOC, INNER_ADDRESS) add constraint A_PERSON_INNER_ADDRESS_FK
	foreign key (A_PERSON_SSN)
	references QName(DOC, APERSON) (SSN);;

alter table QName(DOC, PRODUCT) add constraint CATEGORY_PRODUCT_FK
	foreign key (CATEGORY_ID)
	references QName(DOC, CATEGORY_TYPE) (ID);;

alter table QName(DOC, PRODUCT_BY_CAT) add constraint PRODUCT_PRODUCT_BY_CAT_FK
	foreign key (PRODUCT_PRODUCT_ID)
	references QName(DOC, PRODUCT) (PRODUCT_ID);;

-- if NeedsSerialComment
comment on column QName(DOC,CATEGORY_TYPE).ID              is 'Serial(1,CATEGORY_TYPE_SEQ)';;
comment on column QName(DOC,SEARCHABLE_PERSON).ID          is 'Serial(1,SEARCHABLE_PERSON_SEQ)';;
-- end

