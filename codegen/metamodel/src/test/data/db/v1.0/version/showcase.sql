-- SQL for Schema SHOWCASE --

create table QName(SHOWCASE, ADDRESS) (
	ID                                Identity         not null,
	STREET                            nvarchar(60)     not null,
	CITY                              nvarchar(40)     not null,
	STATE                             nvarchar(40),
	ZIP                               nvarchar(10),
	COUNTRY                           nvarchar(30)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_ADDRESS             primary key (ID)
);;

create table QName(SHOWCASE, ADDRESSES) (
	ID                                Identity         not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_ADDRESSES           primary key (ID)
);;

create table QName(SHOWCASE, ANOTHER_DEPRECABLE_ENTITY) (
	ID                                Identity         not null,
	NAME                              nvarchar(20)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,
	DEPRECATION_TIME                  datetime(0),
	DEPRECATION_USER                  nvarchar(100),

	constraint PK_ANOTHER_DEPRECABLE_ENTITY primary key (ID)
);;

create table QName(SHOWCASE, ANOTHER_SIMPLE_ENTITY) (
	ID                                Identity         not null,
	NAME                              nvarchar(30)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_ANOTHER_SIMPLE_ENTITY primary key (ID)
);;

create table QName(SHOWCASE, CAR) (
	ID                                Identity         not null,
	MAKE_ID                           int              not null,
	MODEL_MAKE_ID                     int              not null,
	MODEL_SEQ_ID                      int              not null,
	YEAR                              int              not null,
	ENGINE                            nvarchar(50)     not null,
	PRICE                             decimal(7,0)     not null,
	MILEAGE                           int              not null,
	TRANSMISSION                      nvarchar(50)     not null,
	COLOR                             nvarchar(30)     not null,
	AIR                               boolean          not null,
	BLUETOOTH                         boolean          not null,
	CRUISE                            boolean          not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CAR                 primary key (ID)
);;

create table QName(SHOWCASE, CLASSROOM) (
	ID                                int              not null,
	ROOM                              nvarchar(4)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CLASSROOM           primary key (ID)
);;

create table QName(SHOWCASE, CLIENT) (
	ID                                Identity         not null,
	NAME                              nvarchar(50)     not null,
	ADDRESS_ID                        int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CLIENT              primary key (ID)
);;

create table QName(SHOWCASE, DATE_ENTITY) (
	ID                                Identity         not null,
	DATE                              date             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_DATE_ENTITY         primary key (ID)
);;

create table QName(SHOWCASE, DATE_SHOWCASE) (
	ID                                int              not null,
	DATE_FROM                         date,
	DATE_TO                           date,
	TIME_FROM                         datetime(0),
	TIME_TO                           datetime(0),
	DOUBLE_DATE_FROM                  date,
	DOUBLE_DATE_TO                    date,
	DATE_COMBO                        date,
	DATE_COMBO1                       date,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_DATE_SHOWCASE       primary key (ID)
);;

create table QName(SHOWCASE, DEPRECABLE_ENTITY) (
	ID                                Identity         not null,
	NAME                              nvarchar(20)     not null,
	SIMPLE_ENTITY_NAME                nvarchar(256)    not null,
	ANOTHER_DEPRECABLE_ENTITY_ID      int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,
	DEPRECATION_TIME                  datetime(0),
	DEPRECATION_USER                  nvarchar(100),

	constraint PK_DEPRECABLE_ENTITY   primary key (ID)
);;

create table QName(SHOWCASE, DYNAMIC_PROPERTY) (
	ID                                Identity         not null,
	PROPERTY_NAME                     nvarchar(20)     not null,
	PROPERTY_TYPE                     nvarchar(50)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_DYNAMIC_PROPERTY    primary key (ID)
);;

create table QName(SHOWCASE, DYNAMIC_VALUE) (
	DYNAMIC_PROPERTY_ID               int              not null,
	SEQ_ID                            int              not null,
	VALUE                             nvarchar(100)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_DYNAMIC_VALUE       primary key (DYNAMIC_PROPERTY_ID,SEQ_ID)
);;

create table QName(SHOWCASE, FLIGHT) (
	ID                                int              not null,
	NAME                              nvarchar(20)     not null,
	FROM                              nvarchar(20)     not null,
	TO                                nvarchar(20)     not null,
	PRICE                             int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_FLIGHT              primary key (ID)
);;

create table QName(SHOWCASE, IMAGE_RESOURCE) (
	ID                                int              not null,
	IMG                               nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_IMAGE_RESOURCE      primary key (ID)
);;

create table QName(SHOWCASE, IMAGE_RESOURCES) (
	IMAGE_RESOURCE_ID                 int              not null,
	SEQ_ID                            int              not null,
	NAME                              nvarchar(256)    not null,
	IMG                               nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_IMAGE_RESOURCES     primary key (IMAGE_RESOURCE_ID,SEQ_ID)
);;

create table QName(SHOWCASE, INNER_ADDRESS) (
	ADDRESSES_ID                      int              not null,
	SEQ_ID                            int              not null,
	STREET                            nvarchar(60)     not null,
	CITY                              nvarchar(40)     not null,
	STATE                             nvarchar(40),
	ZIP                               nvarchar(10),
	COUNTRY                           nvarchar(30)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_INNER_ADDRESS       primary key (ADDRESSES_ID,SEQ_ID)
);;

create table QName(SHOWCASE, LISTING) (
	VIEW_DATA_ID                      int              not null,
	SEQ_ID                            int              not null,
	PK                                int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_LISTING             primary key (VIEW_DATA_ID,SEQ_ID)
);;

create table QName(SHOWCASE, MAKE) (
	ID                                Identity         not null,
	NAME                              nvarchar(30)     not null,
	ORIGIN                            nvarchar(50)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_MAKE                primary key (ID)
);;

create table QName(SHOWCASE, MODEL) (
	MAKE_ID                           int              not null,
	SEQ_ID                            int              not null,
	MODEL                             nvarchar(30)     not null,
	RELEASED                          date             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_MODEL               primary key (MAKE_ID,SEQ_ID)
);;

create table QName(SHOWCASE, MY_PROP) (
	TEXT_SHOWCASE_ID                  int              not null,
	SEQ_ID                            int              not null,
	TYPE                              nvarchar(50)     not null,
	VALUE                             nvarchar(20)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_MY_PROP             primary key (TEXT_SHOWCASE_ID,SEQ_ID)
);;

create table QName(SHOWCASE, NAMED_ITEM) (
	ID                                int              not null,
	NAME                              nvarchar(20)     not null,
	COLOR                             nvarchar(30)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_NAMED_ITEM          primary key (ID)
);;

create table QName(SHOWCASE, NODE) (
	ID                                Identity         not null,
	NAME                              nvarchar(256)    not null,
	PARENT_ID                         int,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_NODE                primary key (ID)
);;

create table QName(SHOWCASE, NUMBERS) (
	NAME                              nvarchar(256)    not null,
	UNSIGNED_INT4                     int              not null,
	SIGNED_INT5                       int              not null,
	UNSIGNED_INTEGER                  int              not null,
	SIGNED_INTEGER                    int              not null,
	UNSIGNED_DECIMAL52                decimal(5,2)     not null,
	SIGNED_DECIMAL52                  decimal(5,2)     not null,
	UNSIGNED_REAL                     double           not null,
	SIGNED_REAL                       double           not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_NUMBERS             primary key (NAME)
);;

create table QName(SHOWCASE, PROPERTY) (
	NAME                              nvarchar(20)     not null,
	TYPE                              nvarchar(50)     not null,
	MULTIPLE                          boolean          not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PROPERTY            primary key (NAME,TYPE)
);;

create table QName(SHOWCASE, SIMPLE_ENTITIES) (
	ANOTHER_SIMPLE_ENTITY_ID          int              not null,
	SEQ_ID                            int              not null,
	SIMPLE_ENTITY_NAME                nvarchar(256)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_SIMPLE_ENTITIES     primary key (ANOTHER_SIMPLE_ENTITY_ID,SEQ_ID)
);;

create table QName(SHOWCASE, SIMPLE_ENTITY) (
	NAME                              nvarchar(256)    not null,
	DESCRIPTION                       nvarchar(30)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_SIMPLE_ENTITY       primary key (NAME)
);;

create table QName(SHOWCASE, STUDENT) (
	CLASSROOM_ID                      int              not null,
	SEQ_ID                            int              not null,
	DNI                               int              not null,
	FIRST_NAME                        nvarchar(20)     not null,
	LAST_NAME                         nvarchar(20)     not null,
	AGE                               int              not null,
	GENDER                            nvarchar(50)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_STUDENT             primary key (CLASSROOM_ID,SEQ_ID)
);;

create table QName(SHOWCASE, TEXT_FIELD_SHOWCASE) (
	ID                                int              not null,
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
	HTML                              nvarchar(256),
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_TEXT_FIELD_SHOWCASE primary key (ID)
);;

create table QName(SHOWCASE, TEXT_SHOWCASE) (
	ID                                int              not null,
	TXT                               nvarchar(20)     not null,
	DATE                              date             not null,
	BOOL                              boolean          not null,
	OPTION                            nvarchar(50)     not null,
	ENTITY_NAME                       nvarchar(256)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_TEXT_SHOWCASE       primary key (ID)
);;

create table QName(SHOWCASE, TYPE_A) (
	ID                                Identity         not null,
	D                                 decimal(10,2)    not null,
	I                                 int              not null,
	R                                 double           not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_TYPE_A              primary key (ID)
);;

create table QName(SHOWCASE, TYPE_B) (
	ID                                Identity         not null,
	S                                 nvarchar(60)     not null,
	T                                 datetime(0)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_TYPE_B              primary key (ID)
);;

create table QName(SHOWCASE, TYPE_C) (
	ID                                Identity         not null,
	A                                 nvarchar(60)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_TYPE_C              primary key (ID)
);;

create table QName(SHOWCASE, VALID_VALUE) (
	PROPERTY_NAME                     nvarchar(20)     not null,
	PROPERTY_TYPE                     nvarchar(50)     not null,
	SEQ_ID                            int              not null,
	VALUE                             nvarchar(100)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_VALID_VALUE         primary key (PROPERTY_NAME,PROPERTY_TYPE,SEQ_ID)
);;

create table QName(SHOWCASE, VIEW_DATA) (
	ID                                Identity         not null,
	CURRENT                           int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_VIEW_DATA           primary key (ID)
);;

create table QName(SHOWCASE, _METADATA) (
	VERSION                           nvarchar(24)     not null,
	SHA                               nvarchar(128)    not null,
	SHA_OVL                           nvarchar(128),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

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

create sequence QName(SHOWCASE, MAKE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SHOWCASE, NODE_SEQ)
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

alter table QName(SHOWCASE, CAR) add constraint MAKE_CAR_FK
	foreign key (MAKE_ID)
	references QName(SHOWCASE, MAKE) (ID);;

alter table QName(SHOWCASE, CAR) add constraint MODEL_CAR_FK
	foreign key (MODEL_MAKE_ID, MODEL_SEQ_ID)
	references QName(SHOWCASE, MODEL) (MAKE_ID, SEQ_ID);;

alter table QName(SHOWCASE, CLIENT) add constraint ADDRESS_CLIENT_FK
	foreign key (ADDRESS_ID)
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
	foreign key (IMAGE_RESOURCE_ID)
	references QName(SHOWCASE, IMAGE_RESOURCE) (ID);;

alter table QName(SHOWCASE, INNER_ADDRESS) add constraint ADDRESSES_INNER_ADDRESS_FK
	foreign key (ADDRESSES_ID)
	references QName(SHOWCASE, ADDRESSES) (ID);;

alter table QName(SHOWCASE, LISTING) add constraint VIEW_DATA_LISTING_FK
	foreign key (VIEW_DATA_ID)
	references QName(SHOWCASE, VIEW_DATA) (ID);;

alter table QName(SHOWCASE, MODEL) add constraint MAKE_MODEL_FK
	foreign key (MAKE_ID)
	references QName(SHOWCASE, MAKE) (ID);;

alter table QName(SHOWCASE, MY_PROP) add constraint TEXT_SHOWCASE_MY_PROP_FK
	foreign key (TEXT_SHOWCASE_ID)
	references QName(SHOWCASE, TEXT_SHOWCASE) (ID);;

alter table QName(SHOWCASE, SIMPLE_ENTITIES) add constraint ANOTHER_SIMPLE_ENTIT_84502E_FK
	foreign key (ANOTHER_SIMPLE_ENTITY_ID)
	references QName(SHOWCASE, ANOTHER_SIMPLE_ENTITY) (ID);;

alter table QName(SHOWCASE, SIMPLE_ENTITIES) add constraint SIMPLE_ENTITY_SIMPLE_6277C0_FK
	foreign key (SIMPLE_ENTITY_NAME)
	references QName(SHOWCASE, SIMPLE_ENTITY) (NAME);;

alter table QName(SHOWCASE, STUDENT) add constraint CLASSROOM_STUDENT_FK
	foreign key (CLASSROOM_ID)
	references QName(SHOWCASE, CLASSROOM) (ID);;

alter table QName(SHOWCASE, TEXT_SHOWCASE) add constraint ENTITY_TEXT_SHOWCASE_FK
	foreign key (ENTITY_NAME)
	references QName(SHOWCASE, SIMPLE_ENTITY) (NAME);;

alter table QName(SHOWCASE, VALID_VALUE) add constraint PROPERTY_VALID_VALUE_FK
	foreign key (PROPERTY_NAME, PROPERTY_TYPE)
	references QName(SHOWCASE, PROPERTY) (NAME, TYPE);;

