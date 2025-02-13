-- SQL for Schema SHOWCASE --

-- if NeedsCreateSequence

create sequence QName(SHOWCASE, ADDRESS_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, ADDRESSES_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, ANOTHER_DEPRECABLE_ENTITY_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, ANOTHER_SIMPLE_ENTITY_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, CAR_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, CLIENT_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, DATE_ENTITY_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, DEPRECABLE_ENTITY_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, DYNAMIC_PROPERTY_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, IMAGE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, LABEL_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, MAKE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, NODE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, SUGGESTED_PERSON_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, SYNCHRONOUS_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, TABLE_ENTITY_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, TEST_PROTECTED_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, TYPE_A_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, TYPE_B_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, TYPE_C_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, VIEW_DATA_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(SHOWCASE, ADDRESS) (
	ID                                Serial(1,ADDRESS_SEQ)                     not null,
	STREET                            nvarchar(60)     default EmptyString      not null,
	CITY                              nvarchar(40)     default EmptyString      not null,
	STATE                             nvarchar(40),
	ZIP                               nvarchar(10),
	COUNTRY                           nvarchar(30)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ADDRESS             primary key (ID)
);;

create table QName(SHOWCASE, ADDRESSES) (
	ID                                Serial(1,ADDRESSES_SEQ)                   not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ADDRESSES           primary key (ID)
);;

create table QName(SHOWCASE, ANOTHER_DEPRECABLE_ENTITY) (
	ID                                Serial(1,ANOTHER_DEPRECABLE_ENTITY_SEQ)   not null,
	NAME                              nvarchar(20)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	DEPRECATION_TIME                  datetime(3),
	DEPRECATION_USER                  nvarchar(100),

	constraint PK_ANOTHER_DEPRECABLE_ENTITY primary key (ID)
);;

create table QName(SHOWCASE, ANOTHER_SIMPLE_ENTITY) (
	ID                                Serial(1,ANOTHER_SIMPLE_ENTITY_SEQ)       not null,
	NAME                              nvarchar(30)     default EmptyString      not null,
	OPTIONS                           bigint           default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ANOTHER_SIMPLE_ENTITY primary key (ID)
);;

create table QName(SHOWCASE, CAR) (
	ID                                Serial(1,CAR_SEQ)                         not null,
	MAKE_ID                           int                                       not null,
	MODEL_MAKE_ID                     int                                       not null,
	MODEL_SEQ_ID                      int                                       not null,
	YEAR                              int                                       not null,
	ENGINE                            nvarchar(50)     default 'GAS'            not null,
	PRICE                             decimal(7,0)     default 0                not null,
	MILEAGE                           int              default 0                not null,
	TRANSMISSION                      nvarchar(50)     default 'MANUAL'         not null,
	COLOR                             nvarchar(30)     default EmptyString      not null,
	AIR                               boolean          default False CheckBoolConstraint(CAR_AIR_B, AIR) not null,
	BLUETOOTH                         boolean          default False CheckBoolConstraint(CAR_BLUETOOTH_B, BLUETOOTH) not null,
	CRUISE                            boolean          default False CheckBoolConstraint(CAR_CRUISE_B, CRUISE) not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CAR                 primary key (ID)
);;

create table QName(SHOWCASE, CLASSROOM) (
	ID_KEY                            int              default 0                not null,
	ROOM                              nvarchar(4)      default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CLASSROOM           primary key (ID_KEY)
);;

create table QName(SHOWCASE, CLIENT) (
	ID                                Serial(1,CLIENT_SEQ)                      not null,
	NAME                              nvarchar(50)     default EmptyString      not null,
	ADDRESS_ID                        int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CLIENT              primary key (ID)
);;

create table QName(SHOWCASE, CUSTOMER) (
	DOCUMENT                          int              default 0                not null,
	FIRST_NAME                        nvarchar(255)    default EmptyString      not null,
	LAST_NAME                         nvarchar(255)    default EmptyString      not null,
	HOME_ADDRESS_ID                   int                                       not null,
	WORK_ADDRESS_ID                   int,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CUSTOMER            primary key (DOCUMENT)
);;

create table QName(SHOWCASE, DNI) (
	NUMBER                            int              default 0                not null,
	DESCR                             nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_DNI                 primary key (NUMBER)
);;

create table QName(SHOWCASE, DATE_ENTITY) (
	ID                                Serial(1,DATE_ENTITY_SEQ)                 not null,
	DATE                              date             default CurrentDate      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_DATE_ENTITY         primary key (ID)
);;

create table QName(SHOWCASE, DATE_SHOWCASE) (
	ID_KEY                            int              default 0                not null,
	DATE_FROM                         date,
	DATE_TO                           date,
	TIME_FROM                         datetime(0),
	TIME_TO                           datetime(0),
	DOUBLE_DATE_FROM                  date,
	DOUBLE_DATE_TO                    date,
	DATE_COMBO                        date,
	DATE_COMBO1                       date,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_DATE_SHOWCASE       primary key (ID_KEY)
);;

create table QName(SHOWCASE, DEPRECABLE_ENTITY) (
	ID                                Serial(1,DEPRECABLE_ENTITY_SEQ)           not null,
	NAME                              nvarchar(20)     default EmptyString      not null,
	SIMPLE_ENTITY_NAME                nvarchar(255)                             not null,
	ANOTHER_DEPRECABLE_ENTITY_ID      int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	DEPRECATION_TIME                  datetime(3),
	DEPRECATION_USER                  nvarchar(100),

	constraint PK_DEPRECABLE_ENTITY   primary key (ID)
);;

create table QName(SHOWCASE, DYNAMIC_PROPERTY) (
	ID                                Serial(1,DYNAMIC_PROPERTY_SEQ)            not null,
	PROPERTY_NAME                     nvarchar(20)                              not null,
	PROPERTY_TYPE                     nvarchar(50)                              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_DYNAMIC_PROPERTY    primary key (ID)
);;

create table QName(SHOWCASE, DYNAMIC_VALUE) (
	DYNAMIC_PROPERTY_ID               int                                       not null,
	SEQ_ID                            int              default 0                not null,
	VALUE                             nvarchar(100)                             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_DYNAMIC_VALUE       primary key (DYNAMIC_PROPERTY_ID,SEQ_ID)
);;

create table QName(SHOWCASE, FLIGHT) (
	ID_KEY                            int              default 0                not null,
	NAME                              nvarchar(20)     default EmptyString      not null,
	FROM                              nvarchar(20)     default EmptyString      not null,
	TO                                nvarchar(20)     default EmptyString      not null,
	PRICE                             int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_FLIGHT              primary key (ID_KEY)
);;

create table QName(SHOWCASE, IMAGE) (
	ID                                Serial(1,IMAGE_SEQ)                       not null,
	NAME                              nvarchar(255)    default EmptyString      not null,
	RESOURCE                          nvarchar(128)                             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_IMAGE               primary key (ID)
);;

create table QName(SHOWCASE, IMAGE_RESOURCE) (
	ID_KEY                            int              default 0                not null,
	IMG                               nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_IMAGE_RESOURCE      primary key (ID_KEY)
);;

create table QName(SHOWCASE, IMAGE_RESOURCES) (
	IMAGE_RESOURCE_ID_KEY             int                                       not null,
	SEQ_ID                            int              default 0                not null,
	NAME                              nvarchar(255)    default EmptyString      not null,
	IMG                               nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_IMAGE_RESOURCES     primary key (IMAGE_RESOURCE_ID_KEY,SEQ_ID)
);;

create table QName(SHOWCASE, INNER_ADDRESS) (
	ADDRESSES_ID                      int                                       not null,
	SEQ_ID                            int              default 0                not null,
	STREET                            nvarchar(60)     default EmptyString      not null,
	CITY                              nvarchar(40)     default EmptyString      not null,
	STATE                             nvarchar(40),
	ZIP                               nvarchar(10),
	COUNTRY                           nvarchar(30)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_INNER_ADDRESS       primary key (ADDRESSES_ID,SEQ_ID)
);;

create table QName(SHOWCASE, INNER_TEST_PROTECTED) (
	TEST_PROTECTED_ID                 int                                       not null,
	SEQ_ID                            int              default 0                not null,
	DESC                              nvarchar(255)    default EmptyString      not null,
	LENGTH                            int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_INNER_TEST_PROTECTED primary key (TEST_PROTECTED_ID,SEQ_ID)
);;

create table QName(SHOWCASE, LABEL) (
	ID                                Serial(1,LABEL_SEQ)                       not null,
	WITH_LABEL1                       nvarchar(255)    default EmptyString      not null,
	WITH_LABEL2                       nvarchar(255)    default EmptyString      not null,
	NO_LABEL1                         nvarchar(255)    default EmptyString      not null,
	NO_LABEL2                         nvarchar(255)    default EmptyString      not null,
	NO_LABEL3                         nvarchar(255)    default EmptyString      not null,
	NO_LABEL4                         nvarchar(255)    default EmptyString      not null,
	SOME                              nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_LABEL               primary key (ID)
);;

create table QName(SHOWCASE, LISTING) (
	VIEW_DATA_ID                      int                                       not null,
	SEQ_ID                            int              default 0                not null,
	PK                                int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_LISTING             primary key (VIEW_DATA_ID,SEQ_ID)
);;

create table QName(SHOWCASE, MAKE) (
	ID                                Serial(1,MAKE_SEQ)                        not null,
	NAME                              nvarchar(30)     default EmptyString      not null,
	ORIGIN                            nvarchar(50)     default 'ARGENTINA'      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_MAKE                primary key (ID)
);;

create table QName(SHOWCASE, MODEL) (
	MAKE_ID                           int                                       not null,
	SEQ_ID                            int              default 0                not null,
	MODEL                             nvarchar(30)     default EmptyString      not null,
	RELEASED                          date             default CurrentDate      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_MODEL               primary key (MAKE_ID,SEQ_ID)
);;

create table QName(SHOWCASE, MY_PROP) (
	TEXT_SHOWCASE_ID_KEY              int                                       not null,
	SEQ_ID                            int              default 0                not null,
	TYPE                              nvarchar(50)     default 'STRING'         not null,
	VALUE                             nvarchar(20)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_MY_PROP             primary key (TEXT_SHOWCASE_ID_KEY,SEQ_ID)
);;

create table QName(SHOWCASE, NAMED_ITEM) (
	ID_KEY                            int              default 0                not null,
	NAME                              nvarchar(20)     default EmptyString      not null,
	COLOR                             nvarchar(30)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_NAMED_ITEM          primary key (ID_KEY)
);;

create table QName(SHOWCASE, NODE) (
	ID                                Serial(1,NODE_SEQ)                        not null,
	NAME                              nvarchar(255)    default EmptyString      not null,
	PARENT_ID                         int,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_NODE                primary key (ID)
);;

create table QName(SHOWCASE, NUMBERS) (
	NAME                              nvarchar(255)    default EmptyString      not null,
	UNSIGNED_INT4                     int              default 0                not null,
	SIGNED_INT5                       int              default 0                not null,
	SIGNED_TO_BE_UNSIGNED             int              default 0                not null,
	UNSIGNED_INTEGER                  int                                       not null,
	SIGNED_INTEGER                    int                                       not null,
	UNSIGNED_DECIMAL52                decimal(5,2)     default 0                not null,
	SIGNED_DECIMAL52                  decimal(5,2)     default 0                not null,
	UNSIGNED_REAL                     double           default 0                not null,
	SIGNED_REAL                       double           default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_NUMBERS             primary key (NAME)
);;

create table QName(SHOWCASE, PERSON_WITH_DNI) (
	DNI_NUMBER                        int                                       not null,
	NAME                              nvarchar(255)    default EmptyString      not null,
	LASTNAME                          nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PERSON_WITH_DNI     primary key (DNI_NUMBER)
);;

create table QName(SHOWCASE, PROPERTY) (
	NAME                              nvarchar(20)     default EmptyString      not null,
	TYPE                              nvarchar(50)     default 'STRING'         not null,
	MULTIPLE                          boolean          default False CheckBoolConstraint(PROPERTY_MULTIPLE_B, MULTIPLE) not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PROPERTY            primary key (NAME,TYPE)
);;

create table QName(SHOWCASE, SIMPLE_ENTITIES) (
	ANOTHER_SIMPLE_ENTITY_ID          int                                       not null,
	SEQ_ID                            int              default 0                not null,
	SIMPLE_ENTITY_NAME                nvarchar(255)                             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SIMPLE_ENTITIES     primary key (ANOTHER_SIMPLE_ENTITY_ID,SEQ_ID)
);;

create table QName(SHOWCASE, SIMPLE_ENTITY) (
	NAME                              nvarchar(255)    default EmptyString      not null,
	DESCRIPTION                       nvarchar(30)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SIMPLE_ENTITY       primary key (NAME)
);;

create table QName(SHOWCASE, STUDENT) (
	CLASSROOM_ID_KEY                  int                                       not null,
	SEQ_ID                            int              default 0                not null,
	DNI                               int              default 0                not null,
	FIRST_NAME                        nvarchar(20)                              not null,
	LAST_NAME                         nvarchar(20)     default EmptyString      not null,
	AGE                               int              default 0                not null,
	GENDER                            nvarchar(50)     default 'MALE'           not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_STUDENT             primary key (CLASSROOM_ID_KEY,SEQ_ID)
);;

create table QName(SHOWCASE, SUGGESTED_PERSON) (
	ID                                Serial(1,SUGGESTED_PERSON_SEQ)            not null,
	NAME                              nvarchar(30)     default EmptyString      not null,
	LAST_NAME                         nvarchar(30),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SUGGESTED_PERSON    primary key (ID)
);;

create table QName(SHOWCASE, SYNCHRONOUS) (
	ID                                Serial(1,SYNCHRONOUS_SEQ)                 not null,
	NAME                              nvarchar(40)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	CREATION_TIME                     datetime(3)      default CurrentTime      not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_SYNCHRONOUS         primary key (ID),
	constraint SYNCHRONOUS_NAME_UNQT  unique      (NAME)
);;

create table QName(SHOWCASE, TABLE_ENTITY) (
	ID                                Serial(1,TABLE_ENTITY_SEQ)                not null,
	MOE                               nvarchar(255)    default EmptyString      not null,
	LARRY                             int              default 0                not null,
	CURLY                             double           default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TABLE_ENTITY        primary key (ID)
);;

create table QName(SHOWCASE, TAG) (
	NAME                              nvarchar(20)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TAG                 primary key (NAME)
);;

create table QName(SHOWCASE, TEST_PROTECTED) (
	ID                                Serial(1,TEST_PROTECTED_SEQ)              not null,
	NAME                              nvarchar(255)    default EmptyString      not null,
	BLA                               nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TEST_PROTECTED      primary key (ID)
);;

create table QName(SHOWCASE, TEXT_FIELD_SHOWCASE) (
	ID_KEY                            int              default 0                not null,
	F1                                nvarchar(50),
	F2                                nvarchar(50),
	F3                                nvarchar(50),
	F4                                nvarchar(50),
	PATENTE                           nvarchar(6),
	MONEY                             decimal(10,2),
	T1                                decimal(4,2),
	T2                                decimal(4,2),
	T3                                decimal(4,2),
	T4                                decimal(4,2),
	A1                                int,
	A2                                int,
	A3                                int,
	A4                                int,
	HTML                              nvarchar(255),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TEXT_FIELD_SHOWCASE primary key (ID_KEY)
);;

create table QName(SHOWCASE, TEXT_SHOWCASE) (
	ID_KEY                            int              default 0                not null,
	TXT                               nvarchar(20)     default EmptyString      not null,
	DATE                              date             default CurrentDate      not null,
	BOOL                              boolean          default False CheckBoolConstraint(TEXT_SHOWCASE_BOOL_B, BOOL) not null,
	OPTION                            nvarchar(50)     default 'OPTION1'        not null,
	ENTITY_NAME                       nvarchar(255)                             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TEXT_SHOWCASE       primary key (ID_KEY)
);;

create table QName(SHOWCASE, TYPE_A) (
	ID                                Serial(1,TYPE_A_SEQ)                      not null,
	D                                 decimal(10,2)                             not null,
	I                                 int                                       not null,
	R                                 double                                    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TYPE_A              primary key (ID)
);;

create table QName(SHOWCASE, TYPE_B) (
	ID                                Serial(1,TYPE_B_SEQ)                      not null,
	S                                 nvarchar(60)                              not null,
	T                                 datetime(0)                               not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TYPE_B              primary key (ID)
);;

create table QName(SHOWCASE, TYPE_C) (
	ID                                Serial(1,TYPE_C_SEQ)                      not null,
	A                                 nvarchar(60)                              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TYPE_C              primary key (ID)
);;

create table QName(SHOWCASE, VALID_VALUE) (
	PROPERTY_NAME                     nvarchar(20)                              not null,
	PROPERTY_TYPE                     nvarchar(50)                              not null,
	SEQ_ID                            int              default 0                not null,
	VALUE                             nvarchar(100)                             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_VALID_VALUE         primary key (PROPERTY_NAME,PROPERTY_TYPE,SEQ_ID)
);;

create table QName(SHOWCASE, VIEW_DATA) (
	ID                                Serial(1,VIEW_DATA_SEQ)                   not null,
	CURRENT                           int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_VIEW_DATA           primary key (ID)
);;

create table QName(SHOWCASE, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

alter table QName(SHOWCASE, CAR) add constraint MAKE_CAR_FK
	foreign key (MAKE_ID)
	references QName(SHOWCASE, MAKE) (ID);;

alter table QName(SHOWCASE, CAR) add constraint MODEL_CAR_FK
	foreign key (MODEL_MAKE_ID, MODEL_SEQ_ID)
	references QName(SHOWCASE, MODEL) (MAKE_ID, SEQ_ID);;

alter table QName(SHOWCASE, CLIENT) add constraint ADDRESS_CLIENT_FK
	foreign key (ADDRESS_ID)
	references QName(SHOWCASE, ADDRESS) (ID);;

alter table QName(SHOWCASE, CUSTOMER) add constraint HOME_ADDRESS_CUSTOMER_FK
	foreign key (HOME_ADDRESS_ID)
	references QName(SHOWCASE, ADDRESS) (ID);;

alter table QName(SHOWCASE, CUSTOMER) add constraint WORK_ADDRESS_CUSTOMER_FK
	foreign key (WORK_ADDRESS_ID)
	references QName(SHOWCASE, ADDRESS) (ID);;

alter table QName(SHOWCASE, DEPRECABLE_ENTITY) add constraint SIMPLE_ENTITY_DEPREC_5B586F_FK
	foreign key (SIMPLE_ENTITY_NAME)
	references QName(SHOWCASE, SIMPLE_ENTITY) (NAME);;

alter table QName(SHOWCASE, DEPRECABLE_ENTITY) add constraint ANOTHER_DEPRECABLE_E_9AA58C_FK
	foreign key (ANOTHER_DEPRECABLE_ENTITY_ID)
	references QName(SHOWCASE, ANOTHER_DEPRECABLE_ENTITY) (ID);;

alter table QName(SHOWCASE, DYNAMIC_PROPERTY) add constraint PROPERTY_DYNAMIC_PROPERTY_FK
	foreign key (PROPERTY_NAME, PROPERTY_TYPE)
	references QName(SHOWCASE, PROPERTY) (NAME, TYPE);;

alter table QName(SHOWCASE, DYNAMIC_VALUE) add constraint DYNAMIC_PROPERTY_DYN_82BF94_FK
	foreign key (DYNAMIC_PROPERTY_ID)
	references QName(SHOWCASE, DYNAMIC_PROPERTY) (ID);;

alter table QName(SHOWCASE, IMAGE_RESOURCES) add constraint IMAGE_RESOURCE_IMAGE_FFBD71_FK
	foreign key (IMAGE_RESOURCE_ID_KEY)
	references QName(SHOWCASE, IMAGE_RESOURCE) (ID_KEY);;

alter table QName(SHOWCASE, INNER_ADDRESS) add constraint ADDRESSES_INNER_ADDRESS_FK
	foreign key (ADDRESSES_ID)
	references QName(SHOWCASE, ADDRESSES) (ID);;

alter table QName(SHOWCASE, INNER_TEST_PROTECTED) add constraint TEST_PROTECTED_INNER_35C0B2_FK
	foreign key (TEST_PROTECTED_ID)
	references QName(SHOWCASE, TEST_PROTECTED) (ID);;

alter table QName(SHOWCASE, LISTING) add constraint VIEW_DATA_LISTING_FK
	foreign key (VIEW_DATA_ID)
	references QName(SHOWCASE, VIEW_DATA) (ID);;

alter table QName(SHOWCASE, MODEL) add constraint MAKE_MODEL_FK
	foreign key (MAKE_ID)
	references QName(SHOWCASE, MAKE) (ID);;

alter table QName(SHOWCASE, MY_PROP) add constraint TEXT_SHOWCASE_MY_PROP_FK
	foreign key (TEXT_SHOWCASE_ID_KEY)
	references QName(SHOWCASE, TEXT_SHOWCASE) (ID_KEY);;

alter table QName(SHOWCASE, PERSON_WITH_DNI) add constraint DNI_PERSON_WITH_DNI_FK
	foreign key (DNI_NUMBER)
	references QName(SHOWCASE, DNI) (NUMBER);;

alter table QName(SHOWCASE, SIMPLE_ENTITIES) add constraint ANOTHER_SIMPLE_ENTIT_84502E_FK
	foreign key (ANOTHER_SIMPLE_ENTITY_ID)
	references QName(SHOWCASE, ANOTHER_SIMPLE_ENTITY) (ID);;

alter table QName(SHOWCASE, SIMPLE_ENTITIES) add constraint SIMPLE_ENTITY_SIMPLE_6277C0_FK
	foreign key (SIMPLE_ENTITY_NAME)
	references QName(SHOWCASE, SIMPLE_ENTITY) (NAME);;

alter table QName(SHOWCASE, STUDENT) add constraint CLASSROOM_STUDENT_FK
	foreign key (CLASSROOM_ID_KEY)
	references QName(SHOWCASE, CLASSROOM) (ID_KEY);;

alter table QName(SHOWCASE, TEXT_SHOWCASE) add constraint ENTITY_TEXT_SHOWCASE_FK
	foreign key (ENTITY_NAME)
	references QName(SHOWCASE, SIMPLE_ENTITY) (NAME);;

alter table QName(SHOWCASE, VALID_VALUE) add constraint PROPERTY_VALID_VALUE_FK
	foreign key (PROPERTY_NAME, PROPERTY_TYPE)
	references QName(SHOWCASE, PROPERTY) (NAME, TYPE);;

-- if NeedsSerialComment
comment on column QName(SHOWCASE,ADDRESS).ID               is 'Serial(1,ADDRESS_SEQ)';;
comment on column QName(SHOWCASE,ADDRESSES).ID             is 'Serial(1,ADDRESSES_SEQ)';;
comment on column QName(SHOWCASE,ANOTHER_DEPRECABLE_ENTITY).ID is 'Serial(1,ANOTHER_DEPRECABLE_ENTITY_SEQ)';;
comment on column QName(SHOWCASE,ANOTHER_SIMPLE_ENTITY).ID is 'Serial(1,ANOTHER_SIMPLE_ENTITY_SEQ)';;
comment on column QName(SHOWCASE,CAR).ID                   is 'Serial(1,CAR_SEQ)';;
comment on column QName(SHOWCASE,CLIENT).ID                is 'Serial(1,CLIENT_SEQ)';;
comment on column QName(SHOWCASE,DATE_ENTITY).ID           is 'Serial(1,DATE_ENTITY_SEQ)';;
comment on column QName(SHOWCASE,DEPRECABLE_ENTITY).ID     is 'Serial(1,DEPRECABLE_ENTITY_SEQ)';;
comment on column QName(SHOWCASE,DYNAMIC_PROPERTY).ID      is 'Serial(1,DYNAMIC_PROPERTY_SEQ)';;
comment on column QName(SHOWCASE,IMAGE).ID                 is 'Serial(1,IMAGE_SEQ)';;
comment on column QName(SHOWCASE,LABEL).ID                 is 'Serial(1,LABEL_SEQ)';;
comment on column QName(SHOWCASE,MAKE).ID                  is 'Serial(1,MAKE_SEQ)';;
comment on column QName(SHOWCASE,NODE).ID                  is 'Serial(1,NODE_SEQ)';;
comment on column QName(SHOWCASE,SUGGESTED_PERSON).ID      is 'Serial(1,SUGGESTED_PERSON_SEQ)';;
comment on column QName(SHOWCASE,SYNCHRONOUS).ID           is 'Serial(1,SYNCHRONOUS_SEQ)';;
comment on column QName(SHOWCASE,TABLE_ENTITY).ID          is 'Serial(1,TABLE_ENTITY_SEQ)';;
comment on column QName(SHOWCASE,TEST_PROTECTED).ID        is 'Serial(1,TEST_PROTECTED_SEQ)';;
comment on column QName(SHOWCASE,TYPE_A).ID                is 'Serial(1,TYPE_A_SEQ)';;
comment on column QName(SHOWCASE,TYPE_B).ID                is 'Serial(1,TYPE_B_SEQ)';;
comment on column QName(SHOWCASE,TYPE_C).ID                is 'Serial(1,TYPE_C_SEQ)';;
comment on column QName(SHOWCASE,VIEW_DATA).ID             is 'Serial(1,VIEW_DATA_SEQ)';;
-- end

