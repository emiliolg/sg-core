-- SQL for Schema BASIC --

-- if NeedsCreateSequence

create sequence QName(BASIC, CAT_SEQ)
	start with SequenceStartValue(20) increment by 1 SequenceCache;;

create sequence QName(BASIC, CATC_SEQ)
	start with SequenceStartValue(20) increment by 1 SequenceCache;;

create sequence QName(BASIC, CATEGORY_DEFAULT_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC, CITY_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC, CUSTOMER_SEARCHABLE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC, DATABASE_CUSTOMER_S_C5E1B6_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC, PAYMENT_TYPE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC, PREFERENCES_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC, PRODUCT_DATA_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC, PRODUCT_DATA_WORK_ITEM_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC, PRODUCT_DEFAULT_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC, PRODUCT_DEFAULT_INNERS_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC, REVIEW_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC, STORE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(BASIC, CATEGORY) (
	ID_KEY                            BigSerial(20,CAT_SEQ)                     not null,
	NAME                              nvarchar(30)     default EmptyString      not null,
	DESCR                             nvarchar(120)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CATEGORY            primary key (ID_KEY)
);;

create table QName(BASIC, CATEGORY_COMPOSITE) (
	ID_KEY                            BigSerial(20,CATC_SEQ)                    not null,
	NAME                              nvarchar(30)     default EmptyString      not null,
	DESCR                             nvarchar(120)    default EmptyString      not null,
	SHORT_DESC                        nvarchar(120)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CATEGORY_COMPOSITE  primary key (ID_KEY,DESCR,SHORT_DESC)
);;

create table QName(BASIC, CATEGORY_DEFAULT) (
	ID                                Serial(1,CATEGORY_DEFAULT_SEQ)            not null,
	NAME                              nvarchar(30)     default EmptyString      not null,
	DESCR                             nvarchar(120)    default EmptyString      not null,
	PARENT_ID                         int,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	DEPRECATION_TIME                  datetime(3),
	DEPRECATION_USER                  nvarchar(100),

	constraint PK_CATEGORY_DEFAULT    primary key (ID)
);;

create table QName(BASIC, CITY) (
	ID                                Serial(1,CITY_SEQ)                        not null,
	NAME                              nvarchar(30)     default EmptyString      not null,
	STATE_PROVINCE_COUNTRY_ISO2       nvarchar(2)                               not null,
	STATE_PROVINCE_CODE               nvarchar(2)                               not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CITY                primary key (ID)
);;

create table QName(BASIC, COUNTRY) (
	NAME                              nvarchar(30)     default EmptyString      not null,
	ISO2                              nvarchar(2)      default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_COUNTRY             primary key (ISO2)
);;

create table QName(BASIC, CUSTOMER) (
	DOCUMENT_TYPE                     int              default 0                not null,
	DOCUMENT_ID                       decimal(10,0)    default 0                not null,
	FIRST_NAME                        nvarchar(50)     default EmptyString      not null,
	LAST_NAME                         nvarchar(50)     default EmptyString      not null,
	NICKNAME                          nvarchar(50)     default EmptyString      not null,
	SEX                               nvarchar(50)     default 'F'              not null,
	PHOTO                             nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CUSTOMER            primary key (DOCUMENT_TYPE,DOCUMENT_ID,SEX),
	constraint CUSTOMER_NICK_UNQT     unique      (NICKNAME)
);;

create table QName(BASIC, CUSTOMER_SEARCHABLE) (
	ID                                Serial(1,CUSTOMER_SEARCHABLE_SEQ)         not null,
	FIRST_NAME                        nvarchar(255)    default EmptyString      not null,
	LAST_NAME                         nvarchar(255)    default EmptyString      not null,
	DOCUMENT                          decimal(10,0)    default 0                not null,
	BIRTH_DATE                        datetime(0)      default CurrentTime      not null,
	SEX                               nvarchar(50)     default 'F'              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CUSTOMER_SEARCHABLE primary key (ID)
);;

create table QName(BASIC, DATABASE_CUSTOMER_SEARCHABLE) (
	ID                                Serial(1,DATABASE_CUSTOMER_S_C5E1B6_SEQ)  not null,
	FIRST_NAME                        nvarchar(255)    default EmptyString      not null,
	LAST_NAME                         nvarchar(255)    default EmptyString      not null,
	DOCUMENT                          decimal(10,0)    default 0                not null,
	BIRTH_DATE                        datetime(0)      default CurrentTime      not null,
	SEX                               nvarchar(50)     default 'F'              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_DATABASE_CUSTOMER_SEARCHABL primary key (ID)
);;

create table QName(BASIC, IMAGE) (
	PRODUCT_PRODUCT_ID                nvarchar(8)                               not null,
	SEQ_ID                            int              default 0                not null,
	IMAGE_ID                          nvarchar(128)                             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_IMAGE               primary key (PRODUCT_PRODUCT_ID,SEQ_ID)
);;

create table QName(BASIC, INVOICE) (
	ID_KEY                            int              default 0                not null,
	INVOICE_DATE                      date             default CurrentDate      not null,
	CUSTOMER_DOCUMENT_TYPE            int                                       not null,
	CUSTOMER_DOCUMENT_ID              decimal(10,0)                             not null,
	CUSTOMER_SEX                      nvarchar(50)                              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_INVOICE             primary key (ID_KEY)
);;

create table QName(BASIC, ITEM) (
	INVOICE_ID_KEY                    int                                       not null,
	SEQ_ID                            int              default 0                not null,
	PRODUCT_PRODUCT_ID                nvarchar(8)                               not null,
	QUANTITY                          int              default 0                not null,
	DISCOUNT                          int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ITEM                primary key (INVOICE_ID_KEY,SEQ_ID)
);;

create table QName(BASIC, LONG_KEY) (
	A1                                int              default 0                not null,
	A2                                int              default 0                not null,
	A3                                int              default 0                not null,
	A4                                int              default 0                not null,
	A5                                int              default 0                not null,
	A6                                int              default 0                not null,
	A7                                int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_LONG_KEY            primary key (A1,A2,A3,A4,A5,A6)
);;

create table QName(BASIC, PAYMENT) (
	INVOICE_ID_KEY                    int                                       not null,
	SEQ_ID                            int              default 0                not null,
	PAYMENT_ID                        int                                       not null,
	AMOUNT                            decimal(10,2)    default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PAYMENT             primary key (INVOICE_ID_KEY,SEQ_ID)
);;

create table QName(BASIC, PAYMENT_TYPE) (
	ID                                Serial(1,PAYMENT_TYPE_SEQ)                not null,
	TYPE                              nvarchar(2)      default 'CA'             not null,
	SUBTYPE                           nvarchar(30)     default EmptyString      not null,
	DESCRIPTION                       nvarchar(160)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PAYMENT_TYPE        primary key (ID)
);;

create table QName(BASIC, PREFERENCES) (
	ID                                Serial(1,PREFERENCES_SEQ)                 not null,
	CUSTOMER_DOCUMENT_TYPE            int                                       not null,
	CUSTOMER_DOCUMENT_ID              decimal(10,0)                             not null,
	CUSTOMER_SEX                      nvarchar(50)                              not null,
	MAIL                              nvarchar(60)     default EmptyString      not null,
	TWITTER                           nvarchar(60)     default EmptyString      not null,
	DIGEST                            nvarchar(50)     default 'DAILY'          not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PREFERENCES         primary key (ID),
	constraint PREFERENCES_CUSTOMER_UNQT unique      (CUSTOMER_DOCUMENT_TYPE,CUSTOMER_DOCUMENT_ID,CUSTOMER_SEX)
);;

create table QName(BASIC, PRODUCT) (
	PRODUCT_ID                        nvarchar(8)                               not null,
	MODEL                             nvarchar(30)     default EmptyString      not null,
	DESCRIPTION                       nvarchar(100),
	PRICE                             decimal(10,2)    default 0                not null,
	STATE                             nvarchar(50)     default 'CREATED'        not null,
	CATEGORY_ID_KEY                   bigint                                    not null,
	CATEGORY_ATT_PERSISTED            nvarchar(150)    default EmptyString      not null,
	TAGS                              bigint           default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PRODUCT             primary key (PRODUCT_ID)
);;

create table QName(BASIC, PRODUCT_BY_CAT) (
	PRODUCT_PRODUCT_ID                nvarchar(8)                               not null,
	SEQ_ID                            int              default 0                not null,
	SECONDARY_CATEGORY_ID_KEY         bigint                                    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	CREATION_TIME                     datetime(3)      default CurrentTime      not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_PRODUCT_BY_CAT      primary key (PRODUCT_PRODUCT_ID,SEQ_ID)
);;

create table QName(BASIC, PRODUCT_DATA) (
	ID                                Serial(1,PRODUCT_DATA_SEQ)                not null,
	ENTITY_PRODUCT_ID                 nvarchar(8)                               not null,
	CREATION                          datetime(0)      default CurrentTime      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PRODUCT_DATA        primary key (ID)
);;

create table QName(BASIC, PRODUCT_DATA_WORK_ITEM) (
	ID                                Serial(1,PRODUCT_DATA_WORK_ITEM_SEQ)      not null,
	TASK                              nvarchar(256)    default EmptyString      not null,
	PARENT_CASE_ID                    int                                       not null,
	CREATION                          datetime(0)      default CurrentTime      not null,
	ASSIGNEE                          nvarchar(256)    default EmptyString      not null,
	REPORTER                          nvarchar(256),
	OU_NAME                           nvarchar(256),
	CLOSED                            boolean          default False CheckBoolConstraint(PRODUCT_DATA_WORK_ITE_2E30D7_B, CLOSED) not null,
	DESCRIPTION                       nvarchar(256)    default EmptyString      not null,
	TITLE                             nvarchar(256),
	PRIORITY_CODE                     int              default 3,
	BUSINESS_KEY                      nvarchar(256),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PRODUCT_DATA_WORK_ITEM primary key (ID)
);;

create table QName(BASIC, PRODUCT_DEFAULT) (
	ID                                Serial(1,PRODUCT_DEFAULT_SEQ)             not null,
	MODEL                             nvarchar(30)     default EmptyString      not null,
	DESCRIPTION                       nvarchar(100),
	PRICE                             decimal(10,2)                             not null,
	STATE                             nvarchar(50)     default 'CREATED'        not null,
	MAIN_CATEGORY_ID                  int                                       not null,
	IMAGE                             nvarchar(128),
	COMMENTS                          clob             default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PRODUCT_DEFAULT     primary key (ID)
);;

create table QName(BASIC, PRODUCT_DEFAULT_INNERS) (
	ID                                Serial(1,PRODUCT_DEFAULT_INNERS_SEQ)      not null,
	MODEL                             nvarchar(30)     default EmptyString      not null,
	DESCRIPTION                       nvarchar(100),
	PRICE                             decimal(10,2)    default 0                not null,
	STATE                             nvarchar(50)     default 'CREATED'        not null,
	MAIN_CATEGORY_ID                  int                                       not null,
	IMAGE                             nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PRODUCT_DEFAULT_INNERS primary key (ID)
);;

create table QName(BASIC, PROTECTED_PRODUCT) (
	PRODUCT_ID                        nvarchar(8)                               not null,
	MODEL                             nvarchar(30)     default EmptyString      not null,
	DESCRIPTION                       nvarchar(100),
	PRICE                             decimal(10,2)    default 0                not null,
	STATE                             nvarchar(50)     default 'CREATED'        not null,
	CATEGORY_ID_KEY                   bigint                                    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PROTECTED_PRODUCT   primary key (PRODUCT_ID)
);;

create table QName(BASIC, REV) (
	PRODUCT_DEFAULT_INNERS_ID         int                                       not null,
	SEQ_ID                            int              default 0                not null,
	REVIEW                            nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	CREATION_TIME                     datetime(3)      default CurrentTime      not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_REV                 primary key (PRODUCT_DEFAULT_INNERS_ID,SEQ_ID)
);;

create table QName(BASIC, REVIEW) (
	ID                                Serial(1,REVIEW_SEQ)                      not null,
	PRODUCT_ID                        int                                       not null,
	REVIEW                            nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	CREATION_TIME                     datetime(3)      default CurrentTime      not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_REVIEW              primary key (ID)
);;

create table QName(BASIC, STATE_PROVINCE) (
	COUNTRY_ISO2                      nvarchar(2)                               not null,
	CODE                              nvarchar(2)      default EmptyString      not null,
	NAME                              nvarchar(30)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_STATE_PROVINCE      primary key (COUNTRY_ISO2,CODE)
);;

create table QName(BASIC, STORE) (
	ID                                Serial(1,STORE_SEQ)                       not null,
	NAME                              nvarchar(50)     default EmptyString      not null,
	ADDRESS                           nvarchar(150)    default EmptyString      not null,
	LAT                               double           default 0                not null,
	LNG                               double           default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_STORE               primary key (ID)
);;

create table QName(BASIC, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

create table QName(BASIC, CATEGORY_COMPOSITE_VIEW) (
	VID                               bigint           default 0                not null,
	VNAME                             nvarchar(30)     default EmptyString      not null,
	VDESCR                            nvarchar(120)    default EmptyString      not null,
	VSHORT                            nvarchar(120)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CATEGORY_COMPOSITE_VIEW primary key (VID,VDESCR,VSHORT)
);;

create table QName(BASIC, CATEGORY_DEFAULT_VIEW) (
	ID                                int              default 0                not null,
	VNAME                             nvarchar(30)     default EmptyString      not null,
	VDESCR                            nvarchar(120)    default EmptyString      not null,
	VPARENT_ID                        int,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	DEPRECATION_TIME                  datetime(3),
	DEPRECATION_USER                  nvarchar(100),

	constraint PK_CATEGORY_DEFAULT_VIEW primary key (ID)
);;

create view QName(BASIC, CATEGORY_SQL_VIEW) as
	select ID_KEY, NAME, DESCR, UPDATE_TIME from TableName(BASIC, CATEGORY);;

CommentOnView  QName(BASIC, CATEGORY_SQL_VIEW) is 'select ID_KEY, NAME, DESCR, UPDATE_TIME from TableName(BASIC, CATEGORY)';;

create table QName(BASIC, CATEGORY_SQL_VIEW_VIEW) (
	VNAME                             nvarchar(30)     default EmptyString      not null,
	VID                               int              default 0                not null,
	VDESCR                            nvarchar(120)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CATEGORY_SQL_VIEW_VIEW primary key (VID)
);;

create table QName(BASIC, CATEGORY_VIEW) (
	VID                               bigint           default 0                not null,
	VNAME                             nvarchar(30)     default EmptyString      not null,
	VDESCR                            nvarchar(120)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CATEGORY_VIEW       primary key (VID)
);;

create table QName(BASIC, CITY_VIEW) (
	ID                                int              default 0                not null,
	NAME                              nvarchar(30)     default EmptyString      not null,
	STATE_PROVINCE_COUNTRY_ISO        nvarchar(2)      default EmptyString      not null,
	STATE_PROVINCE_CODE               nvarchar(2)      default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CITY_VIEW           primary key (ID)
);;

create table QName(BASIC, COUNTRY_VIEW) (
	ISO                               nvarchar(2)      default EmptyString      not null,
	NAME                              nvarchar(30)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_COUNTRY_VIEW        primary key (ISO)
);;

create table QName(BASIC, PROD_BY_CAT_VIEW) (
	SEQ_ID                            int              default 0                not null,
	CAT_VID                           bigint           default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	PRODUCT_VID                       nvarchar(8)                               not null,

	constraint PK_PROD_BY_CAT_VIEW    primary key (PRODUCT_VID,SEQ_ID)
);;

create table QName(BASIC, PRODUCT_DEFAULT_SQL_VIEW) (
	ID                                int              default 0                not null,
	MODEL                             nvarchar(30)     default EmptyString      not null,
	PRICE                             decimal(10,2)    default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PRODUCT_DEFAULT_SQL_VIEW primary key (ID)
);;

create table QName(BASIC, PRODUCT_DEFAULT_VIEW) (
	ID                                int              default 0                not null,
	VDESCR                            nvarchar(100),
	V_CATEGORY_ID                     int              default 0                not null,
	IMAGE                             nvarchar(128),
	STATE                             nvarchar(50)     default 'CREATED'        not null,
	PRICE                             decimal(10,2)                             not null,
	COMMENTS                          clob             default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PRODUCT_DEFAULT_VIEW primary key (ID)
);;

create table QName(BASIC, PRODUCT_DEFAULT_VIEW_INNERS) (
	ID                                int              default 0                not null,
	VDESCR                            nvarchar(100),
	V_CATEGORY_ID                     int              default 0                not null,
	IMAGE                             nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PRODUCT_DEFAULT_VIEW_INNERS primary key (ID)
);;

create view QName(BASIC, PRODUCT_SQL_VIEW) as
	select ID,MODEL,PRICE,UPDATE_TIME from QName(BASIC,PRODUCT_DEFAULT);;

CommentOnView  QName(BASIC, PRODUCT_SQL_VIEW) is 'select ID,MODEL,PRICE,UPDATE_TIME from QName(BASIC,PRODUCT_DEFAULT)';;

create table QName(BASIC, PRODUCT_VIEW) (
	VID                               nvarchar(8)                               not null,
	VDESCR                            nvarchar(100),
	VCATEGORY_VID                     bigint           default 0                not null,
	CATEGORY_ATT                      nvarchar(150)    default EmptyString      not null,
	TAGS                              bigint           default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PRODUCT_VIEW        primary key (VID)
);;

create table QName(BASIC, REV_INNER_VIEW) (
	SEQ_ID                            int              default 0                not null,
	REV                               nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	PRODUCT_DEFAULT_INNERS_ID         int              default 0                not null,

	constraint PK_REV_INNER_VIEW      primary key (PRODUCT_DEFAULT_INNERS_ID,SEQ_ID)
);;

create table QName(BASIC, REV_VIEW) (
	ID                                int              default 0                not null,
	REV                               nvarchar(255)    default EmptyString      not null,
	PROD_ID                           int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_REV_VIEW            primary key (ID)
);;

create table QName(BASIC, STATE_PROVINCE_VIEW) (
	COUNTRY_ISO                       nvarchar(2)      default EmptyString      not null,
	CODE                              nvarchar(2)      default EmptyString      not null,
	NAME                              nvarchar(30)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_STATE_PROVINCE_VIEW primary key (COUNTRY_ISO,CODE)
);;

create index IndexName(BASIC, CATEGORY_DEFAULT_D_264B99_IDXT)
	on QName(BASIC, CATEGORY_DEFAULT) (DEPRECATION_TIME) IndexTableSpace;;

create index IndexName(BASIC, CUSTOMER_LAST_IDXT)
	on QName(BASIC, CUSTOMER) (LAST_NAME,FIRST_NAME) IndexTableSpace;;

create index IndexName(BASIC, PAYMENT_TYPE_TYPE_IDXT)
	on QName(BASIC, PAYMENT_TYPE) (TYPE) IndexTableSpace;;

create index IndexName(BASIC, PRODUCT_BY_CAT_UPD_7A017A_IDXT)
	on QName(BASIC, PRODUCT_BY_CAT) (UPDATE_TIME) IndexTableSpace;;

create index IndexName(BASIC, PRODUCT_BY_CAT_CATEGORY_IDXT)
	on QName(BASIC, PRODUCT_BY_CAT) (SECONDARY_CATEGORY_ID_KEY) IndexTableSpace;;

create index IndexName(BASIC, PRODUCT_DATA_WORK__9A5116_IDXT)
	on QName(BASIC, PRODUCT_DATA_WORK_ITEM) (CLOSED) IndexTableSpace;;

create index IndexName(BASIC, PRODUCT_DATA_WORK__CA07F8_IDXT)
	on QName(BASIC, PRODUCT_DATA_WORK_ITEM) (UPDATE_TIME) IndexTableSpace;;

create index IndexName(BASIC, PRODUCT_VIEW_VDESCR_IDXT)
	on QName(BASIC, PRODUCT_VIEW) (VDESCR) IndexTableSpace;;

create index IndexName(BASIC, REVIEW_PRODUCT_IDXT)
	on QName(BASIC, REVIEW) (PRODUCT_ID) IndexTableSpace;;

create index IndexName(BASIC, STATE_PROVINCE_VIE_2285D8_IDXT)
	on QName(BASIC, STATE_PROVINCE_VIEW) (COUNTRY_ISO) IndexTableSpace;;

alter table QName(BASIC, CITY) add constraint STATE_PROVINCE_CITY_FK
	foreign key (STATE_PROVINCE_COUNTRY_ISO2, STATE_PROVINCE_CODE)
	references QName(BASIC, STATE_PROVINCE) (COUNTRY_ISO2, CODE);;

alter table QName(BASIC, IMAGE) add constraint PRODUCT_IMAGE_FK
	foreign key (PRODUCT_PRODUCT_ID)
	references QName(BASIC, PRODUCT) (PRODUCT_ID);;

alter table QName(BASIC, INVOICE) add constraint CUSTOMER_INVOICE_FK
	foreign key (CUSTOMER_DOCUMENT_TYPE, CUSTOMER_DOCUMENT_ID, CUSTOMER_SEX)
	references QName(BASIC, CUSTOMER) (DOCUMENT_TYPE, DOCUMENT_ID, SEX);;

alter table QName(BASIC, ITEM) add constraint INVOICE_ITEM_FK
	foreign key (INVOICE_ID_KEY)
	references QName(BASIC, INVOICE) (ID_KEY);;

alter table QName(BASIC, ITEM) add constraint PRODUCT_ITEM_FK
	foreign key (PRODUCT_PRODUCT_ID)
	references QName(BASIC, PRODUCT) (PRODUCT_ID);;

alter table QName(BASIC, PAYMENT) add constraint INVOICE_PAYMENT_FK
	foreign key (INVOICE_ID_KEY)
	references QName(BASIC, INVOICE) (ID_KEY);;

alter table QName(BASIC, PAYMENT) add constraint PAYMENT_PAYMENT_FK
	foreign key (PAYMENT_ID)
	references QName(BASIC, PAYMENT_TYPE) (ID);;

alter table QName(BASIC, PREFERENCES) add constraint CUSTOMER_PREFERENCES_FK
	foreign key (CUSTOMER_DOCUMENT_TYPE, CUSTOMER_DOCUMENT_ID, CUSTOMER_SEX)
	references QName(BASIC, CUSTOMER) (DOCUMENT_TYPE, DOCUMENT_ID, SEX);;

alter table QName(BASIC, PRODUCT) add constraint CATEGORY_PRODUCT_FK
	foreign key (CATEGORY_ID_KEY)
	references QName(BASIC, CATEGORY) (ID_KEY);;

alter table QName(BASIC, PRODUCT_BY_CAT) add constraint PRODUCT_PRODUCT_BY_CAT_FK
	foreign key (PRODUCT_PRODUCT_ID)
	references QName(BASIC, PRODUCT) (PRODUCT_ID);;

alter table QName(BASIC, PRODUCT_BY_CAT) add constraint SECONDARY_CATEGORY_P_53571B_FK
	foreign key (SECONDARY_CATEGORY_ID_KEY)
	references QName(BASIC, CATEGORY) (ID_KEY);;

alter table QName(BASIC, PRODUCT_DATA) add constraint ENTITY_PRODUCT_DATA_FK
	foreign key (ENTITY_PRODUCT_ID)
	references QName(BASIC, PRODUCT) (PRODUCT_ID);;

alter table QName(BASIC, PRODUCT_DATA_WORK_ITEM) add constraint PARENT_CASE_PRODUCT__5EB7E4_FK
	foreign key (PARENT_CASE_ID)
	references QName(BASIC, PRODUCT_DATA) (ID);;

alter table QName(BASIC, PRODUCT_DEFAULT) add constraint MAIN_CATEGORY_PRODUC_591169_FK
	foreign key (MAIN_CATEGORY_ID)
	references QName(BASIC, CATEGORY_DEFAULT) (ID);;

alter table QName(BASIC, PRODUCT_DEFAULT_INNERS) add constraint MAIN_CATEGORY_PRODUC_222B5D_FK
	foreign key (MAIN_CATEGORY_ID)
	references QName(BASIC, CATEGORY_DEFAULT) (ID);;

alter table QName(BASIC, PROTECTED_PRODUCT) add constraint CATEGORY_PROTECTED_PRODUCT_FK
	foreign key (CATEGORY_ID_KEY)
	references QName(BASIC, CATEGORY) (ID_KEY);;

alter table QName(BASIC, REV) add constraint PRODUCT_DEFAULT_INNERS_REV_FK
	foreign key (PRODUCT_DEFAULT_INNERS_ID)
	references QName(BASIC, PRODUCT_DEFAULT_INNERS) (ID);;

alter table QName(BASIC, REVIEW) add constraint PRODUCT_REVIEW_FK
	foreign key (PRODUCT_ID)
	references QName(BASIC, PRODUCT_DEFAULT) (ID);;

alter table QName(BASIC, STATE_PROVINCE) add constraint COUNTRY_STATE_PROVINCE_FK
	foreign key (COUNTRY_ISO2)
	references QName(BASIC, COUNTRY) (ISO2);;

-- if NeedsSerialComment
comment on column QName(BASIC,CATEGORY).ID_KEY             is 'BigSerial(20,CAT_SEQ)';;
comment on column QName(BASIC,CATEGORY_COMPOSITE).ID_KEY   is 'BigSerial(20,CATC_SEQ)';;
comment on column QName(BASIC,CATEGORY_DEFAULT).ID         is 'Serial(1,CATEGORY_DEFAULT_SEQ)';;
comment on column QName(BASIC,CITY).ID                     is 'Serial(1,CITY_SEQ)';;
comment on column QName(BASIC,CUSTOMER_SEARCHABLE).ID      is 'Serial(1,CUSTOMER_SEARCHABLE_SEQ)';;
comment on column QName(BASIC,DATABASE_CUSTOMER_SEARCHABLE).ID is 'Serial(1,DATABASE_CUSTOMER_S_C5E1B6_SEQ)';;
comment on column QName(BASIC,PAYMENT_TYPE).ID             is 'Serial(1,PAYMENT_TYPE_SEQ)';;
comment on column QName(BASIC,PREFERENCES).ID              is 'Serial(1,PREFERENCES_SEQ)';;
comment on column QName(BASIC,PRODUCT_DATA).ID             is 'Serial(1,PRODUCT_DATA_SEQ)';;
comment on column QName(BASIC,PRODUCT_DATA_WORK_ITEM).ID   is 'Serial(1,PRODUCT_DATA_WORK_ITEM_SEQ)';;
comment on column QName(BASIC,PRODUCT_DEFAULT).ID          is 'Serial(1,PRODUCT_DEFAULT_SEQ)';;
comment on column QName(BASIC,PRODUCT_DEFAULT_INNERS).ID   is 'Serial(1,PRODUCT_DEFAULT_INNERS_SEQ)';;
comment on column QName(BASIC,REVIEW).ID                   is 'Serial(1,REVIEW_SEQ)';;
comment on column QName(BASIC,STORE).ID                    is 'Serial(1,STORE_SEQ)';;
-- end

