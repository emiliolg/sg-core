-- SQL for Schema BASIC --

create table QName(BASIC, CATEGORY) (
	ID                                int              not null,
	NAME                              nvarchar(30)     not null,
	DESCR                             nvarchar(120)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CATEGORY            primary key (ID)
);;

create table QName(BASIC, CATEGORY_DEFAULT) (
	ID                                Identity         not null,
	NAME                              nvarchar(30)     not null,
	DESCR                             nvarchar(120)    not null,
	PARENT_ID                         int,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,
	DEPRECATION_TIME                  datetime(0),
	DEPRECATION_USER                  nvarchar(100),

	constraint PK_CATEGORY_DEFAULT    primary key (ID)
);;

create table QName(BASIC, CITY) (
	ID                                Identity         not null,
	NAME                              nvarchar(30)     not null,
	STATE_PROVINCE_COUNTRY_ISO2       nvarchar(2)      not null,
	STATE_PROVINCE_CODE               nvarchar(2)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CITY                primary key (ID)
);;

create table QName(BASIC, COUNTRY) (
	NAME                              nvarchar(30)     not null,
	ISO2                              nvarchar(2)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_COUNTRY             primary key (ISO2)
);;

create table QName(BASIC, CUSTOMER) (
	DOCUMENT_TYPE                     int              not null,
	DOCUMENT_ID                       decimal(10,0)    not null,
	FIRST_NAME                        nvarchar(50)     not null,
	LAST_NAME                         nvarchar(50)     not null,
	NICKNAME                          nvarchar(50)     not null,
	SEX                               nvarchar(50)     not null,
	PHOTO                             nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CUSTOMER            primary key (DOCUMENT_TYPE,DOCUMENT_ID,SEX),
	constraint CUSTOMER_NICK_UNQT     unique      (NICKNAME)
);;

create table QName(BASIC, IMAGE) (
	PRODUCT_PRODUCT_ID                nvarchar(8)      not null,
	SEQ_ID                            int              not null,
	IMAGE_ID                          nvarchar(128)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_IMAGE               primary key (PRODUCT_PRODUCT_ID,SEQ_ID)
);;

create table QName(BASIC, INVOICE) (
	ID                                int              not null,
	INVOICE_DATE                      date             not null,
	CUSTOMER_DOCUMENT_TYPE            int              not null,
	CUSTOMER_DOCUMENT_ID              decimal(10,0)    not null,
	CUSTOMER_SEX                      nvarchar(50)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_INVOICE             primary key (ID)
);;

create table QName(BASIC, ITEM) (
	INVOICE_ID                        int              not null,
	SEQ_ID                            int              not null,
	PRODUCT_PRODUCT_ID                nvarchar(8)      not null,
	QUANTITY                          int              not null,
	DISCOUNT                          int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_ITEM                primary key (INVOICE_ID,SEQ_ID)
);;

create table QName(BASIC, LONG_KEY) (
	A1                                int              not null,
	A2                                int              not null,
	A3                                int              not null,
	A4                                int              not null,
	A5                                int              not null,
	A6                                int              not null,
	A7                                int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_LONG_KEY            primary key (A1,A2,A3,A4,A5,A6)
);;

create table QName(BASIC, PAYMENT) (
	INVOICE_ID                        int              not null,
	SEQ_ID                            int              not null,
	PAYMENT_ID                        int              not null,
	AMOUNT                            decimal(10,2)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PAYMENT             primary key (INVOICE_ID,SEQ_ID)
);;

create table QName(BASIC, PAYMENT_TYPE) (
	ID                                Identity         not null,
	TYPE                              nvarchar(50)     not null,
	SUBTYPE                           nvarchar(30)     not null,
	DESCRIPTION                       nvarchar(160)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PAYMENT_TYPE        primary key (ID)
);;

create table QName(BASIC, PREFERENCES) (
	ID                                Identity         not null,
	CUSTOMER_DOCUMENT_TYPE            int              not null,
	CUSTOMER_DOCUMENT_ID              decimal(10,0)    not null,
	CUSTOMER_SEX                      nvarchar(50)     not null,
	MAIL                              nvarchar(60)     not null,
	TWITTER                           nvarchar(60)     not null,
	DIGEST                            nvarchar(50)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PREFERENCES         primary key (ID),
	constraint PREFERENCES_CUSTOMER_UNQT unique      (CUSTOMER_DOCUMENT_TYPE,CUSTOMER_DOCUMENT_ID,CUSTOMER_SEX)
);;

create table QName(BASIC, PRODUCT) (
	PRODUCT_ID                        nvarchar(8)      not null,
	MODEL                             nvarchar(30)     not null,
	DESCRIPTION                       nvarchar(100),
	PRICE                             decimal(10,2)    not null,
	STATE                             nvarchar(50)     not null,
	CATEGORY_ID                       int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PRODUCT             primary key (PRODUCT_ID)
);;

create table QName(BASIC, PRODUCT_BY_CAT) (
	PRODUCT_PRODUCT_ID                nvarchar(8)      not null,
	SEQ_ID                            int              not null,
	SECONDARY_CATEGORY_ID             int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,
	CREATION_TIME                     datetime(3)      default CurrentTime not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_PRODUCT_BY_CAT      primary key (PRODUCT_PRODUCT_ID,SEQ_ID)
);;

create table QName(BASIC, PRODUCT_DATA) (
	ID                                Identity         not null,
	ENTITY_PRODUCT_ID                 nvarchar(8)      not null,
	CREATION                          datetime(0)      default CurrentTime not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PRODUCT_DATA        primary key (ID)
);;

create table QName(BASIC, PRODUCT_DATA_WORK_ITEM) (
	ID                                Identity         not null,
	TASK                              nvarchar(256)    not null,
	PARENT_CASE_ID                    int              not null,
	CREATION                          datetime(0)      default CurrentTime not null,
	ASSIGNEE                          nvarchar(256)    not null,
	REPORTER                          nvarchar(256),
	OU_NAME                           nvarchar(256),
	CLOSED                            boolean          not null,
	DESCRIPTION                       nvarchar(256)    not null,
	BUSINESS_KEY                      nvarchar(256),
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PRODUCT_DATA_WORK_ITEM primary key (ID)
);;

create table QName(BASIC, PRODUCT_DEFAULT) (
	ID                                Identity         not null,
	MODEL                             nvarchar(30)     not null,
	DESCRIPTION                       nvarchar(100),
	PRICE                             decimal(10,2)    not null,
	STATE                             nvarchar(50)     not null,
	MAIN_CATEGORY_ID                  int              not null,
	IMAGE                             nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PRODUCT_DEFAULT     primary key (ID)
);;

create table QName(BASIC, PRODUCT_DEFAULT_INNERS) (
	ID                                Identity         not null,
	MODEL                             nvarchar(30)     not null,
	DESCRIPTION                       nvarchar(100),
	PRICE                             decimal(10,2)    not null,
	STATE                             nvarchar(50)     not null,
	MAIN_CATEGORY_ID                  int              not null,
	IMAGE                             nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PRODUCT_DEFAULT_INNERS primary key (ID)
);;

create table QName(BASIC, PROTECTED_PRODUCT) (
	PRODUCT_ID                        nvarchar(8)      not null,
	MODEL                             nvarchar(30)     not null,
	DESCRIPTION                       nvarchar(100),
	PRICE                             decimal(10,2)    not null,
	STATE                             nvarchar(50)     not null,
	CATEGORY_ID                       int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PROTECTED_PRODUCT   primary key (PRODUCT_ID)
);;

create table QName(BASIC, REV) (
	PRODUCT_DEFAULT_INNERS_ID         int              not null,
	SEQ_ID                            int              not null,
	REVIEW                            nvarchar(256)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,
	CREATION_TIME                     datetime(3)      default CurrentTime not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_REV                 primary key (PRODUCT_DEFAULT_INNERS_ID,SEQ_ID)
);;

create table QName(BASIC, REVIEW) (
	ID                                Identity         not null,
	PRODUCT_ID                        int              not null,
	REVIEW                            nvarchar(256)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,
	CREATION_TIME                     datetime(3)      default CurrentTime not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_REVIEW              primary key (ID)
);;

create table QName(BASIC, STATE_PROVINCE) (
	COUNTRY_ISO2                      nvarchar(2)      not null,
	CODE                              nvarchar(2)      not null,
	NAME                              nvarchar(30)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_STATE_PROVINCE      primary key (COUNTRY_ISO2,CODE)
);;

create table QName(BASIC, _METADATA) (
	VERSION                           nvarchar(24)     not null,
	SHA                               nvarchar(128)    not null,
	SHA_OVL                           nvarchar(128),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

create table QName(BASIC, CATEGORY_DEFAULT_VIEW) (
	ID                                Identity         not null,
	VNAME                             nvarchar(30)     not null,
	VDESCR                            nvarchar(120)    not null,
	VPARENT_ID                        int,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,
	DEPRECATION_TIME                  datetime(0),
	DEPRECATION_USER                  nvarchar(100),

	constraint PK_CATEGORY_DEFAULT_VIEW primary key (ID)
);;

create view QName(BASIC, CATEGORY_SQL_VIEW) as
	select ID, NAME, DESCR, UPDATE_TIME from TableName(BASIC, CATEGORY);;

create table QName(BASIC, CATEGORY_SQL_VIEW_VIEW) (
	VNAME                             nvarchar(30)     not null,
	VID                               int              not null,
	VDESCR                            nvarchar(120)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CATEGORY_SQL_VIEW_VIEW primary key (VID)
);;

create table QName(BASIC, CATEGORY_VIEW) (
	VID                               int              not null,
	VNAME                             nvarchar(30)     not null,
	VDESCR                            nvarchar(120)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CATEGORY_VIEW       primary key (VID)
);;

create table QName(BASIC, CITY_VIEW) (
	ID                                Identity         not null,
	NAME                              nvarchar(30)     not null,
	STATE_PROVINCE_COUNTRY_ISO        nvarchar(2)      not null,
	STATE_PROVINCE_CODE               nvarchar(2)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CITY_VIEW           primary key (ID)
);;

create table QName(BASIC, COUNTRY_VIEW) (
	ISO                               nvarchar(2)      not null,
	NAME                              nvarchar(30)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_COUNTRY_VIEW        primary key (ISO)
);;

create table QName(BASIC, PROD_BY_CAT_VIEW) (
	SEQ_ID                            int              not null,
	CAT_VID                           int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,
	PRODUCT_VID                       nvarchar(8)      not null,

	constraint PK_PROD_BY_CAT_VIEW    primary key (PRODUCT_VID,SEQ_ID)
);;

create table QName(BASIC, PRODUCT_DEFAULT_VIEW) (
	ID                                Identity         not null,
	VDESCR                            nvarchar(100),
	V_CATEGORY_ID                     int              not null,
	IMAGE                             nvarchar(128),
	STATE                             nvarchar(50)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PRODUCT_DEFAULT_VIEW primary key (ID)
);;

create table QName(BASIC, PRODUCT_DEFAULT_VIEW_INNERS) (
	ID                                Identity         not null,
	VDESCR                            nvarchar(100),
	V_CATEGORY_ID                     int              not null,
	IMAGE                             nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PRODUCT_DEFAULT_VIEW_INNERS primary key (ID)
);;

create table QName(BASIC, PRODUCT_VIEW) (
	VID                               nvarchar(8)      not null,
	VDESCR                            nvarchar(100),
	VCATEGORY_VID                     int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PRODUCT_VIEW        primary key (VID)
);;

create table QName(BASIC, REV_INNER_VIEW) (
	SEQ_ID                            int              not null,
	REV                               nvarchar(256)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,
	PRODUCT_DEFAULT_INNERS_ID         int              not null,

	constraint PK_REV_INNER_VIEW      primary key (PRODUCT_DEFAULT_INNERS_ID,SEQ_ID)
);;

create table QName(BASIC, REV_VIEW) (
	ID                                Identity         not null,
	REV                               nvarchar(256)    not null,
	PROD_ID                           int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_REV_VIEW            primary key (ID)
);;

create table QName(BASIC, STATE_PROVINCE_VIEW) (
	COUNTRY_ISO                       nvarchar(2)      not null,
	CODE                              nvarchar(2)      not null,
	NAME                              nvarchar(30)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_STATE_PROVINCE_VIEW primary key (COUNTRY_ISO,CODE)
);;

create index IndexName(BASIC, CUSTOMER_LAST_IDXT)
	on QName(BASIC, CUSTOMER) (LAST_NAME,FIRST_NAME) IndexTableSpace;;

create index IndexName(BASIC, PAYMENT_TYPE_TYPE_IDXT)
	on QName(BASIC, PAYMENT_TYPE) (TYPE) IndexTableSpace;;

create index IndexName(BASIC, PRODUCT_BY_CAT_CATEGORY_IDXT)
	on QName(BASIC, PRODUCT_BY_CAT) (SECONDARY_CATEGORY_ID) IndexTableSpace;;

create index IndexName(BASIC, PRODUCT_BY_CAT_UPD_7A017A_IDXT)
	on QName(BASIC, PRODUCT_BY_CAT) (UPDATE_TIME) IndexTableSpace;;

create index IndexName(BASIC, PRODUCT_VIEW_VDESCR_IDXT)
	on QName(BASIC, PRODUCT_VIEW) (VDESCR) IndexTableSpace;;

create index IndexName(BASIC, REVIEW_PRODUCT_IDXT)
	on QName(BASIC, REVIEW) (PRODUCT_ID) IndexTableSpace;;

create index IndexName(BASIC, STATE_PROVINCE_VIE_2285D8_IDXT)
	on QName(BASIC, STATE_PROVINCE_VIEW) (COUNTRY_ISO) IndexTableSpace;;

-- if NeedsCreateSequence

create sequence QName(BASIC, CATEGORY_DEFAULT_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC, CITY_SEQ)
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

-- end

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
	foreign key (INVOICE_ID)
	references QName(BASIC, INVOICE) (ID);;

alter table QName(BASIC, ITEM) add constraint PRODUCT_ITEM_FK
	foreign key (PRODUCT_PRODUCT_ID)
	references QName(BASIC, PRODUCT) (PRODUCT_ID);;

alter table QName(BASIC, PAYMENT) add constraint INVOICE_PAYMENT_FK
	foreign key (INVOICE_ID)
	references QName(BASIC, INVOICE) (ID);;

alter table QName(BASIC, PAYMENT) add constraint PAYMENT_PAYMENT_FK
	foreign key (PAYMENT_ID)
	references QName(BASIC, PAYMENT_TYPE) (ID);;

alter table QName(BASIC, PREFERENCES) add constraint CUSTOMER_PREFERENCES_FK
	foreign key (CUSTOMER_DOCUMENT_TYPE, CUSTOMER_DOCUMENT_ID, CUSTOMER_SEX)
	references QName(BASIC, CUSTOMER) (DOCUMENT_TYPE, DOCUMENT_ID, SEX);;

alter table QName(BASIC, PRODUCT) add constraint CATEGORY_PRODUCT_FK
	foreign key (CATEGORY_ID)
	references QName(BASIC, CATEGORY) (ID);;

alter table QName(BASIC, PRODUCT_BY_CAT) add constraint PRODUCT_PRODUCT_BY_CAT_FK
	foreign key (PRODUCT_PRODUCT_ID)
	references QName(BASIC, PRODUCT) (PRODUCT_ID);;

alter table QName(BASIC, PRODUCT_BY_CAT) add constraint SECONDARY_CATEGORY_P_53571B_FK
	foreign key (SECONDARY_CATEGORY_ID)
	references QName(BASIC, CATEGORY) (ID);;

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
	foreign key (CATEGORY_ID)
	references QName(BASIC, CATEGORY) (ID);;

alter table QName(BASIC, REV) add constraint PRODUCT_DEFAULT_INNERS_REV_FK
	foreign key (PRODUCT_DEFAULT_INNERS_ID)
	references QName(BASIC, PRODUCT_DEFAULT_INNERS) (ID);;

alter table QName(BASIC, REVIEW) add constraint PRODUCT_REVIEW_FK
	foreign key (PRODUCT_ID)
	references QName(BASIC, PRODUCT_DEFAULT) (ID);;

alter table QName(BASIC, STATE_PROVINCE) add constraint COUNTRY_STATE_PROVINCE_FK
	foreign key (COUNTRY_ISO2)
	references QName(BASIC, COUNTRY) (ISO2);;

