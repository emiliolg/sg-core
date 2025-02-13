-- SQL for Schema BASIC_TEST --

-- if NeedsCreateSequence

create sequence QName(BASIC_TEST, AUTHOR_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, BOOK_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, CART_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, CLASSIFICATION_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, DATABASE_SEARCHABLE_TYPES_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, FEATURE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, H_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, LIBRARY_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, OPTIONAL_REFERENCE_MAPPING_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, PAYMENT_TYPE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, PREFERENCES_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, RES_A_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, RES_B_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, SEARCHABLE_TYPES_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, SEQ_ITEM_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, SEQ_OBJECT_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, SEQ_OPTIONAL_PROPERTIES_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, SEQ_PROPERTIES_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, SEQUENCER_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, STORE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, SUPER_SIMPLE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, BRAND_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, SECURITY_SYSTEM_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, TRACKING_SYSTEM_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, VEHICLE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(BASIC_TEST, ZONE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(BASIC_TEST, A) (
	A                                 int              default 0                not null,
	X                                 int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_A                   primary key (A)
);;

create table QName(BASIC_TEST, AUDITABLE_CUSTOMER) (
	DOCUMENT_TYPE                     nvarchar(50)     default 'DNI'            not null,
	DOCUMENT_ID                       decimal(10,0)    default 0                not null,
	FIRST_NAME                        nvarchar(50)     default EmptyString      not null,
	LAST_NAME                         nvarchar(50)     default EmptyString      not null,
	NICKNAME                          nvarchar(50)     default EmptyString      not null,
	SEX                               nvarchar(50)     default 'F'              not null,
	PHOTO                             nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	CREATION_TIME                     datetime(3)      default CurrentTime      not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_AUDITABLE_CUSTOMER  primary key (DOCUMENT_TYPE,DOCUMENT_ID,SEX),
	constraint AUDITABLE_CUSTOMER_NICK_UNQT unique      (NICKNAME)
);;

create table QName(BASIC_TEST, AUTHOR) (
	ID                                Serial(1,AUTHOR_SEQ)                      not null,
	NAME                              nvarchar(30)     default EmptyString      not null,
	LAST_NAME                         nvarchar(50)     default EmptyString      not null,
	NICK_NAME                         nvarchar(50),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_AUTHOR              primary key (ID)
);;

create table QName(BASIC_TEST, B) (
	BB                                int              default 0                not null,
	Y                                 int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_B                   primary key (BB)
);;

create table QName(BASIC_TEST, BASIC_TYPES) (
	ID_KEY                            int              default 0                not null,
	NAME                              nvarchar(20)     default EmptyString      not null,
	BOOL                              boolean          default False CheckBoolConstraint(BASIC_TYPES_BOOL_B, BOOL) not null,
	REAL                              double           default 0                not null,
	DATE                              date             default CurrentDate      not null,
	DATE_TIME                         datetime(3)      default CurrentTime      not null,
	DECIMAL                           decimal(10,4)    default 0                not null,
	NOT_LONG_NAME                     nvarchar(2000)   default EmptyString      not null,
	NOT_SO_LONG_NAME                  clob             default EmptyString      not null,
	LONG_NAME                         clob             default EmptyString      not null,
	LONG_NULLABLE_NAME                clob,
	MY_INT                            int              default 5                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_BASIC_TYPES         primary key (ID_KEY)
);;

create table QName(BASIC_TEST, BOOK) (
	ID                                Serial(1,BOOK_SEQ)                        not null,
	TITLE                             nvarchar(60)     default EmptyString      not null,
	AUTHOR_ID                         int                                       not null,
	COVER                             nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_BOOK                primary key (ID)
);;

create table QName(BASIC_TEST, BOOK_STORE) (
	CODIGO                            int              default 0                not null,
	NAME                              nvarchar(60)     default EmptyString      not null,
	EXTRA_CODE                        int,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_BOOK_STORE          primary key (CODIGO)
);;

create table QName(BASIC_TEST, C) (
	A_A                               int                                       not null,
	BB_BB                             int                                       not null,
	Z                                 int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_C                   primary key (A_A,BB_BB)
);;

create table QName(BASIC_TEST, CACHED_CATEGORY) (
	ID_KEY                            int              default 0                not null,
	NAME                              nvarchar(30)     default EmptyString      not null,
	DESCR                             nvarchar(120)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CACHED_CATEGORY     primary key (ID_KEY)
);;

create table QName(BASIC_TEST, CART) (
	ID                                Serial(1,CART_SEQ)                        not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CART                primary key (ID)
);;

create table QName(BASIC_TEST, CART_ITEM) (
	CART_ID                           int                                       not null,
	SEQ_ID                            int              default 0                not null,
	SALABLE_PRODUCT_PRODUCT_ID        nvarchar(8)                               not null,
	QUANTITY                          int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CART_ITEM           primary key (CART_ID,SEQ_ID)
);;

create table QName(BASIC_TEST, CATEG) (
	CODE                              int              default 0                not null,
	NAME                              nvarchar(25)     default EmptyString      not null,
	CLASSIFICATION_ID                 int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CATEG               primary key (CODE,CLASSIFICATION_ID)
);;

create table QName(BASIC_TEST, CATEGORY) (
	ID_KEY                            int              default 0                not null,
	NAME                              nvarchar(30),
	DESCR                             nvarchar(120)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	INSTANCE_VERSION                  bigint           default 0                not null,

	constraint PK_CATEGORY            primary key (ID_KEY),
	constraint CATEGORY_NAME_UNQT     unique      (NAME)
);;

create table QName(BASIC_TEST, CLASSIFICATION) (
	ID                                Serial(1,CLASSIFICATION_SEQ)              not null,
	NAME                              nvarchar(25)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CLASSIFICATION      primary key (ID)
);;

create table QName(BASIC_TEST, CUSTOMER) (
	DOCUMENT_TYPE                     nvarchar(50)     default 'DNI'            not null,
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

create table QName(BASIC_TEST, D) (
	C_A_A                             int                                       not null,
	C_BB_BB                           int                                       not null,
	D                                 int              default 0                not null,
	T                                 int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_D                   primary key (C_A_A,C_BB_BB,D)
);;

create table QName(BASIC_TEST, DATABASE_SEARCHABLE_TYPES) (
	ID                                Serial(1,DATABASE_SEARCHABLE_TYPES_SEQ)   not null,
	OPT                               nvarchar(255),
	STR                               nvarchar(255)    default EmptyString      not null,
	INTEGER                           int              default 0                not null,
	DECIMAL                           decimal(10,2)    default 0                not null,
	REAL                              double           default 0                not null,
	BOOL                              boolean          default False CheckBoolConstraint(DATABASE_SEARCHABLE_T_463DDD_B, BOOL) not null,
	DATE                              date             default CurrentDate      not null,
	DATE_TIME                         datetime(0)      default CurrentTime      not null,
	EN                                nvarchar(50)     default 'DNI'            not null,
	NULL_ENUM                         nvarchar(50),
	ENS                               bigint           default 0                not null,
	ENTI_ID                           int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_DATABASE_SEARCHABLE_TYPES primary key (ID)
);;

create table QName(BASIC_TEST, DEPRECABLE_ENTITY) (
	FIRST_NAME                        nvarchar(20)     default EmptyString      not null,
	LAST_NAME                         nvarchar(256)    default EmptyString      not null,
	DOCUMENT_NUMBER                   int              default 0                not null,
	EMAIL                             nvarchar(256),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	DEPRECATION_TIME                  datetime(3),
	DEPRECATION_USER                  nvarchar(100),

	constraint PK_DEPRECABLE_ENTITY   primary key (DOCUMENT_NUMBER)
);;

create table QName(BASIC_TEST, E) (
	ID_KEY                            int              default 0                not null,
	A_A                               int                                       not null,
	BB_BB                             int                                       not null,
	C_A_A                             int,
	C_BB_BB                           int,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_E                   primary key (ID_KEY),
	constraint E_B_UNQT               unique      (BB_BB)
);;

create table QName(BASIC_TEST, F) (
	ID_KEY                            int              default 0                not null,
	C_A_A                             int                                       not null,
	C_BB_BB                           int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_F                   primary key (ID_KEY)
);;

create table QName(BASIC_TEST, FATHER) (
	GRAND_FATHER_CODE                 int                                       not null,
	SEQ_ID                            int              default 0                not null,
	NAME                              nvarchar(25)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_FATHER              primary key (GRAND_FATHER_CODE,SEQ_ID)
);;

create table QName(BASIC_TEST, FEATURE) (
	ID                                Serial(1,FEATURE_SEQ)                     not null,
	NAME                              nvarchar(25)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_FEATURE             primary key (ID)
);;

create table QName(BASIC_TEST, FEATURE_BY_CAT) (
	CATEG_CODE                        int                                       not null,
	CATEG_CLASSIFICATION_ID           int                                       not null,
	SEQ_ID                            int              default 0                not null,
	FEATURE_ID                        int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_FEATURE_BY_CAT      primary key (CATEG_CODE,CATEG_CLASSIFICATION_ID,SEQ_ID)
);;

create table QName(BASIC_TEST, FEATURE_VALUE) (
	FEATURE_BY_CAT_CATEG_CODE         int                                       not null,
	FEATURE_BY_CAT_CATEG__B18A7F85    int                                       not null,
	FEATURE_BY_CAT_SEQ_ID             int                                       not null,
	SEQ_ID                            int              default 0                not null,
	VALUE                             nvarchar(25)                              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_FEATURE_VALUE       primary key (FEATURE_BY_CAT_CATEG_CODE,FEATURE_BY_CAT_CATEG__B18A7F85,FEATURE_BY_CAT_SEQ_ID,SEQ_ID)
);;

create table QName(BASIC_TEST, G) (
	G                                 int              default 0                not null,
	A_A                               int                                       not null,
	BB_BB                             int                                       not null,
	H_ID                              int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_G                   primary key (A_A,G),
	constraint G_INVERSE_UNQT         unique      (BB_BB,G)
);;

create table QName(BASIC_TEST, GRAND_FATHER) (
	CODE                              int              default 0                not null,
	NAME                              nvarchar(25)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_GRAND_FATHER        primary key (CODE)
);;

create table QName(BASIC_TEST, H) (
	ID                                Serial(1,H_SEQ)                           not null,
	NAME                              nvarchar(256)    default EmptyString      not null,
	H1_ID                             int,
	H2_ID                             int,
	G_A_A                             int,
	G_G                               int,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_H                   primary key (ID)
);;

create table QName(BASIC_TEST, IDEAFIX_PRODUCT) (
	COMPANY                           nvarchar(30)     default EmptyString      not null,
	CODADM                            int              default 0                not null,
	CODFLIA                           int              default 0                not null,
	COD_NAME                          nvarchar(25)     default EmptyString      not null,
	BRAND                             nvarchar(25)     default EmptyString      not null,
	CTAORD                            int,
	DIRECT_COMMISSION                 decimal(5,2),
	CLASSIFICATION                    nvarchar(2),
	COST                              decimal(9,2),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_IDEAFIX_PRODUCT     primary key (COMPANY,CODADM)
);;

create table QName(BASIC_TEST, IMAGE) (
	PRODUCT_PRODUCT_ID                nvarchar(8)                               not null,
	SEQ_ID                            int              default 0                not null,
	IMAGE_ID                          nvarchar(128)                             not null,
	STATE                             nvarchar(50)     default 'CREATED'        not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_IMAGE               primary key (PRODUCT_PRODUCT_ID,SEQ_ID)
);;

create table QName(BASIC_TEST, INVOICE) (
	ID_KEY                            int              default 0                not null,
	INVOICE_DATE                      date             default CurrentDate      not null,
	CUSTOMER_DOCUMENT_TYPE            nvarchar(50)                              not null,
	CUSTOMER_DOCUMENT_ID              decimal(10,0)                             not null,
	CUSTOMER_SEX                      nvarchar(50)                              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_INVOICE             primary key (ID_KEY)
);;

create table QName(BASIC_TEST, ITEM) (
	INVOICE_ID_KEY                    int                                       not null,
	SEQ_ID                            int              default 0                not null,
	PRODUCT_PRODUCT_ID                nvarchar(8)                               not null,
	QUANTITY                          int              default 0                not null,
	DISCOUNT                          int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ITEM                primary key (INVOICE_ID_KEY,SEQ_ID)
);;

create table QName(BASIC_TEST, LIBRARY) (
	ID                                Serial(1,LIBRARY_SEQ)                     not null,
	NAME                              nvarchar(255)    default EmptyString      not null,
	ZONE_ID                           int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	CREATION_TIME                     datetime(3)      default CurrentTime      not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_LIBRARY             primary key (ID)
);;

create table QName(BASIC_TEST, LOCATION) (
	STORE_ID                          int                                       not null,
	SEQ_ID                            int              default 0                not null,
	ADDRESS                           nvarchar(60)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_LOCATION            primary key (STORE_ID,SEQ_ID)
);;

create table QName(BASIC_TEST, OPTIONAL_REFERENCE_GROUP) (
	COMPANY                           nvarchar(256)    default EmptyString      not null,
	GROUP                             int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_OPTIONAL_REFERENCE_GROUP primary key (COMPANY,GROUP)
);;

create table QName(BASIC_TEST, OPTIONAL_REFERENCE_MAPPING) (
	ID                                Serial(1,OPTIONAL_REFERENCE_MAPPING_SEQ)  not null,
	GRUPART_COMPANY                   nvarchar(256),
	GRUPART_GROUP                     int,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_OPTIONAL_REFERENCE_MAPPING primary key (ID)
);;

create table QName(BASIC_TEST, PAYMENT) (
	INVOICE_ID_KEY                    int                                       not null,
	SEQ_ID                            int              default 0                not null,
	PAYMENT_ID                        int                                       not null,
	AMOUNT                            decimal(10,2)    default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PAYMENT             primary key (INVOICE_ID_KEY,SEQ_ID)
);;

create table QName(BASIC_TEST, PAYMENT_TYPE) (
	ID                                Serial(1,PAYMENT_TYPE_SEQ)                not null,
	TYPE                              nvarchar(50)     default 'CASH'           not null,
	SUBTYPE                           nvarchar(30)     default EmptyString      not null,
	DESCRIPTION                       nvarchar(160)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PAYMENT_TYPE        primary key (ID)
);;

create table QName(BASIC_TEST, PREFERENCES) (
	ID                                Serial(1,PREFERENCES_SEQ)                 not null,
	CUSTOMER_DOCUMENT_TYPE            nvarchar(50)                              not null,
	CUSTOMER_DOCUMENT_ID              decimal(10,0)                             not null,
	CUSTOMER_SEX                      nvarchar(50)                              not null,
	MAIL                              nvarchar(60)     default EmptyString      not null,
	TWITTER                           nvarchar(60)     default EmptyString      not null,
	DIGEST                            nvarchar(50)     default 'DAILY'          not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PREFERENCES         primary key (ID),
	constraint PREFERENCES_CUSTOMER_UNQT unique      (CUSTOMER_DOCUMENT_TYPE,CUSTOMER_DOCUMENT_ID,CUSTOMER_SEX)
);;

create table QName(BASIC_TEST, PROD) (
	PRODUCT_ID                        nvarchar(8)      default EmptyString      not null,
	MODEL                             nvarchar(30)     default EmptyString      not null,
	DESCR                             nvarchar(100)    default EmptyString      not null,
	PRICE                             decimal(10,2)    default 0                not null,
	STATE                             nvarchar(50)     default 'CREATED'        not null,
	ACTIVE                            boolean          default False CheckBoolConstraint(PROD_ACTIVE_B, ACTIVE) not null,
	CAT                               int                                       not null,
	MAIN_IMAGE                        nvarchar(128),
	BRAND_ID                          int,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	CREATION_TIME                     datetime(3)      default CurrentTime      not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_PROD                primary key (PRODUCT_ID)
);;

create table QName(BASIC_TEST, RES_A) (
	ID                                Serial(1,RES_A_SEQ)                       not null,
	NAME                              nvarchar(20)     default EmptyString      not null,
	RES                               nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_RES_A               primary key (ID)
);;

create table QName(BASIC_TEST, RES_B) (
	ID                                Serial(1,RES_B_SEQ)                       not null,
	NAME                              nvarchar(20)     default EmptyString      not null,
	RES1                              nvarchar(128),
	RES2                              nvarchar(128),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_RES_B               primary key (ID)
);;

create table QName(BASIC_TEST, ROW) (
	SEQUENCER_ID                      int                                       not null,
	SEQ_ID                            int              default 0                not null,
	NAME                              nvarchar(25)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ROW                 primary key (SEQUENCER_ID,SEQ_ID)
);;

create table QName(BASIC_TEST, SALE) (
	PA_ID                             int              default 0                not null,
	STORE_ID                          int                                       not null,
	PICKUP_STORE_ID                   int,
	DATE                              datetime(0)      default CurrentTime      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SALE                primary key (PA_ID)
);;

create table QName(BASIC_TEST, SEARCHABLE_TYPES) (
	ID                                Serial(1,SEARCHABLE_TYPES_SEQ)            not null,
	OPT                               nvarchar(255),
	STR                               nvarchar(255)    default EmptyString      not null,
	INTEGER                           int              default 0                not null,
	DECIMAL                           decimal(10,2)    default 0                not null,
	REAL                              double           default 0                not null,
	BOOL                              boolean          default False CheckBoolConstraint(SEARCHABLE_TYPES_BOOL_B, BOOL) not null,
	DATE                              date             default CurrentDate      not null,
	DATE_TIME                         datetime(0)      default CurrentTime      not null,
	EN                                nvarchar(50)     default 'DNI'            not null,
	NULL_ENUM                         nvarchar(50),
	ENS                               bigint           default 0                not null,
	ENTI_ID                           int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SEARCHABLE_TYPES    primary key (ID)
);;

create table QName(BASIC_TEST, SEQ_INVOICE) (
	NUMBER                            decimal(12,0)    default 0                not null,
	STATEMENT                         nvarchar(50)     default EmptyString      not null,
	INSTRUCTIONS                      nvarchar(100),
	DATE                              date             default CurrentDate      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SEQ_INVOICE         primary key (NUMBER)
);;

create table QName(BASIC_TEST, SEQ_ITEM) (
	ID                                Serial(1,SEQ_ITEM_SEQ)                    not null,
	PRODUCT_ID                        nvarchar(20)                              not null,
	INVOICE_NUMBER                    decimal(12,0)                             not null,
	QTY                               int              default 1                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SEQ_ITEM            primary key (ID)
);;

create table QName(BASIC_TEST, SEQ_OBJECT) (
	ID                                Serial(1,SEQ_OBJECT_SEQ)                  not null,
	VALUE                             nvarchar(50)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SEQ_OBJECT          primary key (ID)
);;

create table QName(BASIC_TEST, SEQ_OPTIONAL_PROPERTIES) (
	ID                                Serial(1,SEQ_OPTIONAL_PROPERTIES_SEQ)     not null,
	NAME                              nvarchar(50)     default EmptyString      not null,
	PROPERTIES_ID                     int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SEQ_OPTIONAL_PROPERTIES primary key (ID)
);;

create table QName(BASIC_TEST, SEQ_PRODUCT) (
	ID                                nvarchar(20)     default EmptyString      not null,
	DESCRIPTION                       nvarchar(50)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SEQ_PRODUCT         primary key (ID)
);;

create table QName(BASIC_TEST, SEQ_PROPERTIES) (
	ID                                Serial(1,SEQ_PROPERTIES_SEQ)              not null,
	NAME                              nvarchar(50)     default EmptyString      not null,
	DESCRIPTION                       nvarchar(100),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SEQ_PROPERTIES      primary key (ID)
);;

create table QName(BASIC_TEST, SEQ_PROPERTY) (
	SEQ_PROPERTIES_ID                 int                                       not null,
	SEQ_ID                            int              default 0                not null,
	NAME                              nvarchar(50)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SEQ_PROPERTY        primary key (SEQ_PROPERTIES_ID,SEQ_ID)
);;

create table QName(BASIC_TEST, SEQ_VALUE) (
	SEQ_PROPERTY_SEQ_PROPERTIES_ID    int                                       not null,
	SEQ_PROPERTY_SEQ_ID               int                                       not null,
	SEQ_ID                            int              default 0                not null,
	NAME                              nvarchar(50)     default EmptyString      not null,
	VALUE_ID                          int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SEQ_VALUE           primary key (SEQ_PROPERTY_SEQ_PROPERTIES_ID,SEQ_PROPERTY_SEQ_ID,SEQ_ID)
);;

create table QName(BASIC_TEST, SEQUENCER) (
	ID                                Serial(1,SEQUENCER_SEQ)                   not null,
	NAME                              nvarchar(25)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SEQUENCER           primary key (ID)
);;

create table QName(BASIC_TEST, SON) (
	FATHER_GRAND_FATHER_CODE          int                                       not null,
	FATHER_SEQ_ID                     int                                       not null,
	SEQ_ID                            int              default 0                not null,
	NAME                              nvarchar(25)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SON                 primary key (FATHER_GRAND_FATHER_CODE,FATHER_SEQ_ID,SEQ_ID)
);;

create table QName(BASIC_TEST, STORE) (
	ID                                Serial(1,STORE_SEQ)                       not null,
	NAME                              nvarchar(60)     default EmptyString      not null,
	ACTIVE_MODULES                    bigint           default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_STORE               primary key (ID)
);;

create table QName(BASIC_TEST, STORE_LOCATION) (
	BOOK_STORE_CODIGO                 int                                       not null,
	SEQ_ID                            int              default 0                not null,
	DIR                               nvarchar(256)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_STORE_LOCATION      primary key (BOOK_STORE_CODIGO,SEQ_ID)
);;

create table QName(BASIC_TEST, SUPER_SIMPLE) (
	ID                                Serial(1,SUPER_SIMPLE_SEQ)                not null,
	NAME                              nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SUPER_SIMPLE        primary key (ID)
);;

create table QName(BASIC_TEST, BRAND) (
	ID                                Serial(1,BRAND_SEQ)                       not null,
	NAME                              nvarchar(20)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_BRAND               primary key (ID)
);;

create table QName(BASIC_TEST, IMAGE_RESOURCES) (
	VEHICLE_ID                        int                                       not null,
	SEQ_ID                            int              default 0                not null,
	IMAGE                             nvarchar(128),
	DESCRIPTION                       nvarchar(256)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_IMAGE_RESOURCES     primary key (VEHICLE_ID,SEQ_ID)
);;

create table QName(BASIC_TEST, SECURITY_SYSTEM) (
	ID                                Serial(1,SECURITY_SYSTEM_SEQ)             not null,
	NAME                              nvarchar(20)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SECURITY_SYSTEM     primary key (ID)
);;

create table QName(BASIC_TEST, TRACKING_SYSTEM) (
	ID                                Serial(1,TRACKING_SYSTEM_SEQ)             not null,
	NAME                              nvarchar(20)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TRACKING_SYSTEM     primary key (ID)
);;

create table QName(BASIC_TEST, VEHICLE) (
	ID                                Serial(1,VEHICLE_SEQ)                     not null,
	PLATE                             nvarchar(256)    default EmptyString      not null,
	BRAND_ID                          int                                       not null,
	MODEL                             nvarchar(20)     default EmptyString      not null,
	TYPE                              nvarchar(50)     default 'CAR'            not null,
	VTV_EXPIRATION                    date             default CurrentDate      not null,
	INSURANCE_EXPIRATION              date             default CurrentDate      not null,
	ACTIVE                            boolean          default True CheckBoolConstraint(VEHICLE_ACTIVE_B, ACTIVE) not null,
	COMMENTS                          nvarchar(100)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_VEHICLE             primary key (ID)
);;

create table QName(BASIC_TEST, ZONE) (
	ID                                Serial(1,ZONE_SEQ)                        not null,
	NAME                              nvarchar(20)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ZONE                primary key (ID)
);;

create table QName(BASIC_TEST, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

create view QName(BASIC_TEST, CUSTOMER_SQL_VIEW) as
	
         select DOCUMENT_ID, FIRST_NAME, LAST_NAME, P.MAIL
         from TableName(BASIC_TEST, CUSTOMER)
         join TableName(BASIC_TEST, PREFERENCES) P
         on DOCUMENT_TYPE = P.CUSTOMER_DOCUMENT_TYPE and DOCUMENT_ID = P.CUSTOMER_DOCUMENT_ID and SEX = P.CUSTOMER_SEX
    ;;

CommentOnView  QName(BASIC_TEST, CUSTOMER_SQL_VIEW) is 'select DOCUMENT_ID, FIRST_NAME, LAST_NAME, P.MAIL  from TableName(BASIC_TEST, CUSTOMER)  join TableName(BASIC_TEST, PREFERENCES) P  on DOCUMENT_TYPE = P.CUSTOMER_DOCUMENT_TYPE and DOCUMENT_ID = P.CUSTOMER_DOCUMENT_ID and SEX = P.CUSTOMER_SEX';;

create view QName(BASIC_TEST, MINI_CUSTOMER) (DOCUMENT_TYPE,SEX,NOMBRE,APELLIDO,DOCUMENTO) as
	select DOCUMENT_TYPE, SEX, FIRST_NAME, LAST_NAME, DOCUMENT_ID
	from QName(BASIC_TEST, CUSTOMER);;

CommentOnView  QName(BASIC_TEST, MINI_CUSTOMER) is 'select DOCUMENT_TYPE, SEX, FIRST_NAME, LAST_NAME, DOCUMENT_ID from  QName(BASIC_TEST, CUSTOMER)';;

create view QName(BASIC_TEST, MINI_CUSTOMER_UPDATABLE) (DOCUMENT_TYPE,SEX,NOMBRE,APELLIDO,DOCUMENTO,UPDATE_TIME) as
	select DOCUMENT_TYPE, SEX, FIRST_NAME, LAST_NAME, DOCUMENT_ID, UPDATE_TIME
	from QName(BASIC_TEST, CUSTOMER);;

CommentOnView  QName(BASIC_TEST, MINI_CUSTOMER_UPDATABLE) is 'select DOCUMENT_TYPE, SEX, FIRST_NAME, LAST_NAME, DOCUMENT_ID, UPDATE_TIME from  QName(BASIC_TEST, CUSTOMER)';;

create view QName(BASIC_TEST, MINI_PREFERENCES) (ID,CLIENTE_DOCUMENT_TYPE,CLIENTE_DOCUMENT_ID,CLIENTE_SEX,CORREO,UPDATE_TIME) as
	select ID, CUSTOMER_DOCUMENT_TYPE, CUSTOMER_DOCUMENT_ID, CUSTOMER_SEX, MAIL, UPDATE_TIME
	from QName(BASIC_TEST, PREFERENCES);;

CommentOnView  QName(BASIC_TEST, MINI_PREFERENCES) is 'select ID, CUSTOMER_DOCUMENT_TYPE, CUSTOMER_DOCUMENT_ID, CUSTOMER_SEX, MAIL, UPDATE_TIME from  QName(BASIC_TEST, PREFERENCES)';;

create index IndexName(BASIC_TEST, AUDITABLE_CUSTOMER_LAST_IDXT)
	on QName(BASIC_TEST, AUDITABLE_CUSTOMER) (LAST_NAME,FIRST_NAME) IndexTableSpace;;

create index IndexName(BASIC_TEST, CART_ITEM_SALABLE_PRODUCT_IDXT)
	on QName(BASIC_TEST, CART_ITEM) (SALABLE_PRODUCT_PRODUCT_ID) IndexTableSpace;;

create index IndexName(BASIC_TEST, CUSTOMER_LAST_IDXT)
	on QName(BASIC_TEST, CUSTOMER) (LAST_NAME,FIRST_NAME) IndexTableSpace;;

create index IndexName(BASIC_TEST, E_A_IDXT)
	on QName(BASIC_TEST, E) (A_A) IndexTableSpace;;

create index IndexName(BASIC_TEST, G_A_IDXT)
	on QName(BASIC_TEST, G) (A_A) IndexTableSpace;;

create index IndexName(BASIC_TEST, PAYMENT_TYPE_TYPE_IDXT)
	on QName(BASIC_TEST, PAYMENT_TYPE) (TYPE) IndexTableSpace;;

create index IndexName(BASIC_TEST, PROD_DESCRIPTION_IDXT)
	on QName(BASIC_TEST, PROD) (DESCR) IndexTableSpace;;

create index IndexName(BASIC_TEST, PROD_STATE_IDXT)
	on QName(BASIC_TEST, PROD) (STATE) IndexTableSpace;;

alter table QName(BASIC_TEST, BOOK) add constraint AUTHOR_BOOK_FK
	foreign key (AUTHOR_ID)
	references QName(BASIC_TEST, AUTHOR) (ID);;

alter table QName(BASIC_TEST, C) add constraint A_C_FK
	foreign key (A_A)
	references QName(BASIC_TEST, A) (A);;

alter table QName(BASIC_TEST, C) add constraint BB_C_FK
	foreign key (BB_BB)
	references QName(BASIC_TEST, B) (BB);;

alter table QName(BASIC_TEST, CART_ITEM) add constraint CART_CART_ITEM_FK
	foreign key (CART_ID)
	references QName(BASIC_TEST, CART) (ID);;

alter table QName(BASIC_TEST, CART_ITEM) add constraint SALABLE_PRODUCT_CART_ITEM_FK
	foreign key (SALABLE_PRODUCT_PRODUCT_ID)
	references QName(BASIC_TEST, PROD) (PRODUCT_ID);;

alter table QName(BASIC_TEST, CATEG) add constraint CLASSIFICATION_CATEG_FK
	foreign key (CLASSIFICATION_ID)
	references QName(BASIC_TEST, CLASSIFICATION) (ID);;

alter table QName(BASIC_TEST, D) add constraint C_D_FK
	foreign key (C_A_A, C_BB_BB)
	references QName(BASIC_TEST, C) (A_A, BB_BB);;

alter table QName(BASIC_TEST, DATABASE_SEARCHABLE_TYPES) add constraint ENTI_DATABASE_SEARCH_B86897_FK
	foreign key (ENTI_ID)
	references QName(BASIC_TEST, SUPER_SIMPLE) (ID);;

alter table QName(BASIC_TEST, E) add constraint A_E_FK
	foreign key (A_A)
	references QName(BASIC_TEST, A) (A);;

alter table QName(BASIC_TEST, E) add constraint BB_E_FK
	foreign key (BB_BB)
	references QName(BASIC_TEST, B) (BB);;

alter table QName(BASIC_TEST, E) add constraint C_E_FK
	foreign key (C_A_A, C_BB_BB)
	references QName(BASIC_TEST, C) (A_A, BB_BB);;

alter table QName(BASIC_TEST, F) add constraint C_F_FK
	foreign key (C_A_A, C_BB_BB)
	references QName(BASIC_TEST, C) (A_A, BB_BB);;

alter table QName(BASIC_TEST, FATHER) add constraint GRAND_FATHER_FATHER_FK
	foreign key (GRAND_FATHER_CODE)
	references QName(BASIC_TEST, GRAND_FATHER) (CODE);;

alter table QName(BASIC_TEST, FEATURE_BY_CAT) add constraint CATEG_FEATURE_BY_CAT_FK
	foreign key (CATEG_CODE, CATEG_CLASSIFICATION_ID)
	references QName(BASIC_TEST, CATEG) (CODE, CLASSIFICATION_ID);;

alter table QName(BASIC_TEST, FEATURE_BY_CAT) add constraint FEATURE_FEATURE_BY_CAT_FK
	foreign key (FEATURE_ID)
	references QName(BASIC_TEST, FEATURE) (ID);;

alter table QName(BASIC_TEST, FEATURE_VALUE) add constraint FEATURE_BY_CAT_FEATU_CC90DB_FK
	foreign key (FEATURE_BY_CAT_CATEG_CODE, FEATURE_BY_CAT_CATEG__B18A7F85, FEATURE_BY_CAT_SEQ_ID)
	references QName(BASIC_TEST, FEATURE_BY_CAT) (CATEG_CODE, CATEG_CLASSIFICATION_ID, SEQ_ID);;

alter table QName(BASIC_TEST, G) add constraint A_G_FK
	foreign key (A_A)
	references QName(BASIC_TEST, A) (A);;

alter table QName(BASIC_TEST, G) add constraint BB_G_FK
	foreign key (BB_BB)
	references QName(BASIC_TEST, B) (BB);;

alter table QName(BASIC_TEST, G) add constraint H_G_FK
	foreign key (H_ID)
	references QName(BASIC_TEST, H) (ID);;

alter table QName(BASIC_TEST, H) add constraint G_H_FK
	foreign key (G_A_A, G_G)
	references QName(BASIC_TEST, G) (A_A, G);;

alter table QName(BASIC_TEST, IMAGE) add constraint PRODUCT_IMAGE_FK
	foreign key (PRODUCT_PRODUCT_ID)
	references QName(BASIC_TEST, PROD) (PRODUCT_ID);;

alter table QName(BASIC_TEST, INVOICE) add constraint CUSTOMER_INVOICE_FK
	foreign key (CUSTOMER_DOCUMENT_TYPE, CUSTOMER_DOCUMENT_ID, CUSTOMER_SEX)
	references QName(BASIC_TEST, CUSTOMER) (DOCUMENT_TYPE, DOCUMENT_ID, SEX);;

alter table QName(BASIC_TEST, ITEM) add constraint INVOICE_ITEM_FK
	foreign key (INVOICE_ID_KEY)
	references QName(BASIC_TEST, INVOICE) (ID_KEY);;

alter table QName(BASIC_TEST, ITEM) add constraint PRODUCT_ITEM_FK
	foreign key (PRODUCT_PRODUCT_ID)
	references QName(BASIC_TEST, PROD) (PRODUCT_ID);;

alter table QName(BASIC_TEST, LIBRARY) add constraint ZONE_LIBRARY_FK
	foreign key (ZONE_ID)
	references QName(BASIC_TEST, ZONE) (ID);;

alter table QName(BASIC_TEST, LOCATION) add constraint STORE_LOCATION_FK
	foreign key (STORE_ID)
	references QName(BASIC_TEST, STORE) (ID);;

alter table QName(BASIC_TEST, OPTIONAL_REFERENCE_MAPPING) add constraint GRUPART_OPTIONAL_REF_F5192B_FK
	foreign key (GRUPART_COMPANY, GRUPART_GROUP)
	references QName(BASIC_TEST, OPTIONAL_REFERENCE_GROUP) (COMPANY, GROUP);;

alter table QName(BASIC_TEST, PAYMENT) add constraint INVOICE_PAYMENT_FK
	foreign key (INVOICE_ID_KEY)
	references QName(BASIC_TEST, INVOICE) (ID_KEY);;

alter table QName(BASIC_TEST, PAYMENT) add constraint PAYMENT_PAYMENT_FK
	foreign key (PAYMENT_ID)
	references QName(BASIC_TEST, PAYMENT_TYPE) (ID);;

alter table QName(BASIC_TEST, PREFERENCES) add constraint CUSTOMER_PREFERENCES_FK
	foreign key (CUSTOMER_DOCUMENT_TYPE, CUSTOMER_DOCUMENT_ID, CUSTOMER_SEX)
	references QName(BASIC_TEST, CUSTOMER) (DOCUMENT_TYPE, DOCUMENT_ID, SEX);;

alter table QName(BASIC_TEST, PROD) add constraint CAT_PROD_FK
	foreign key (CAT)
	references QName(BASIC_TEST, CATEGORY) (ID_KEY);;

alter table QName(BASIC_TEST, PROD) add constraint BRAND_PROD_FK
	foreign key (BRAND_ID)
	references QName(BASIC_TEST, BRAND) (ID);;

alter table QName(BASIC_TEST, ROW) add constraint SEQUENCER_ROW_FK
	foreign key (SEQUENCER_ID)
	references QName(BASIC_TEST, SEQUENCER) (ID);;

alter table QName(BASIC_TEST, SALE) add constraint STORE_SALE_FK
	foreign key (STORE_ID)
	references QName(BASIC_TEST, STORE) (ID);;

alter table QName(BASIC_TEST, SALE) add constraint PICKUP_STORE_SALE_FK
	foreign key (PICKUP_STORE_ID)
	references QName(BASIC_TEST, STORE) (ID);;

alter table QName(BASIC_TEST, SEARCHABLE_TYPES) add constraint ENTI_SEARCHABLE_TYPES_FK
	foreign key (ENTI_ID)
	references QName(BASIC_TEST, SUPER_SIMPLE) (ID);;

alter table QName(BASIC_TEST, SEQ_ITEM) add constraint PRODUCT_SEQ_ITEM_FK
	foreign key (PRODUCT_ID)
	references QName(BASIC_TEST, SEQ_PRODUCT) (ID);;

alter table QName(BASIC_TEST, SEQ_ITEM) add constraint INVOICE_SEQ_ITEM_FK
	foreign key (INVOICE_NUMBER)
	references QName(BASIC_TEST, SEQ_INVOICE) (NUMBER);;

alter table QName(BASIC_TEST, SEQ_OPTIONAL_PROPERTIES) add constraint PROPERTIES_SEQ_OPTIO_12AC8E_FK
	foreign key (PROPERTIES_ID)
	references QName(BASIC_TEST, SEQ_PROPERTIES) (ID);;

alter table QName(BASIC_TEST, SEQ_PROPERTY) add constraint SEQ_PROPERTIES_SEQ_PROPERTY_FK
	foreign key (SEQ_PROPERTIES_ID)
	references QName(BASIC_TEST, SEQ_PROPERTIES) (ID);;

alter table QName(BASIC_TEST, SEQ_VALUE) add constraint SEQ_PROPERTY_SEQ_VALUE_FK
	foreign key (SEQ_PROPERTY_SEQ_PROPERTIES_ID, SEQ_PROPERTY_SEQ_ID)
	references QName(BASIC_TEST, SEQ_PROPERTY) (SEQ_PROPERTIES_ID, SEQ_ID);;

alter table QName(BASIC_TEST, SEQ_VALUE) add constraint VALUE_SEQ_VALUE_FK
	foreign key (VALUE_ID)
	references QName(BASIC_TEST, SEQ_OBJECT) (ID);;

alter table QName(BASIC_TEST, SON) add constraint FATHER_SON_FK
	foreign key (FATHER_GRAND_FATHER_CODE, FATHER_SEQ_ID)
	references QName(BASIC_TEST, FATHER) (GRAND_FATHER_CODE, SEQ_ID);;

alter table QName(BASIC_TEST, STORE_LOCATION) add constraint BOOK_STORE_STORE_LOCATION_FK
	foreign key (BOOK_STORE_CODIGO)
	references QName(BASIC_TEST, BOOK_STORE) (CODIGO);;

alter table QName(BASIC_TEST, IMAGE_RESOURCES) add constraint VEHICLE_IMAGE_RESOURCES_FK
	foreign key (VEHICLE_ID)
	references QName(BASIC_TEST, VEHICLE) (ID);;

alter table QName(BASIC_TEST, VEHICLE) add constraint BRAND_VEHICLE_FK
	foreign key (BRAND_ID)
	references QName(BASIC_TEST, BRAND) (ID);;

-- if NeedsSerialComment
comment on column QName(BASIC_TEST,AUTHOR).ID              is 'Serial(1,AUTHOR_SEQ)';;
comment on column QName(BASIC_TEST,BOOK).ID                is 'Serial(1,BOOK_SEQ)';;
comment on column QName(BASIC_TEST,CART).ID                is 'Serial(1,CART_SEQ)';;
comment on column QName(BASIC_TEST,CLASSIFICATION).ID      is 'Serial(1,CLASSIFICATION_SEQ)';;
comment on column QName(BASIC_TEST,DATABASE_SEARCHABLE_TYPES).ID is 'Serial(1,DATABASE_SEARCHABLE_TYPES_SEQ)';;
comment on column QName(BASIC_TEST,FEATURE).ID             is 'Serial(1,FEATURE_SEQ)';;
comment on column QName(BASIC_TEST,H).ID                   is 'Serial(1,H_SEQ)';;
comment on column QName(BASIC_TEST,LIBRARY).ID             is 'Serial(1,LIBRARY_SEQ)';;
comment on column QName(BASIC_TEST,OPTIONAL_REFERENCE_MAPPING).ID is 'Serial(1,OPTIONAL_REFERENCE_MAPPING_SEQ)';;
comment on column QName(BASIC_TEST,PAYMENT_TYPE).ID        is 'Serial(1,PAYMENT_TYPE_SEQ)';;
comment on column QName(BASIC_TEST,PREFERENCES).ID         is 'Serial(1,PREFERENCES_SEQ)';;
comment on column QName(BASIC_TEST,RES_A).ID               is 'Serial(1,RES_A_SEQ)';;
comment on column QName(BASIC_TEST,RES_B).ID               is 'Serial(1,RES_B_SEQ)';;
comment on column QName(BASIC_TEST,SEARCHABLE_TYPES).ID    is 'Serial(1,SEARCHABLE_TYPES_SEQ)';;
comment on column QName(BASIC_TEST,SEQ_ITEM).ID            is 'Serial(1,SEQ_ITEM_SEQ)';;
comment on column QName(BASIC_TEST,SEQ_OBJECT).ID          is 'Serial(1,SEQ_OBJECT_SEQ)';;
comment on column QName(BASIC_TEST,SEQ_OPTIONAL_PROPERTIES).ID is 'Serial(1,SEQ_OPTIONAL_PROPERTIES_SEQ)';;
comment on column QName(BASIC_TEST,SEQ_PROPERTIES).ID      is 'Serial(1,SEQ_PROPERTIES_SEQ)';;
comment on column QName(BASIC_TEST,SEQUENCER).ID           is 'Serial(1,SEQUENCER_SEQ)';;
comment on column QName(BASIC_TEST,STORE).ID               is 'Serial(1,STORE_SEQ)';;
comment on column QName(BASIC_TEST,SUPER_SIMPLE).ID        is 'Serial(1,SUPER_SIMPLE_SEQ)';;
comment on column QName(BASIC_TEST,BRAND).ID               is 'Serial(1,BRAND_SEQ)';;
comment on column QName(BASIC_TEST,SECURITY_SYSTEM).ID     is 'Serial(1,SECURITY_SYSTEM_SEQ)';;
comment on column QName(BASIC_TEST,TRACKING_SYSTEM).ID     is 'Serial(1,TRACKING_SYSTEM_SEQ)';;
comment on column QName(BASIC_TEST,VEHICLE).ID             is 'Serial(1,VEHICLE_SEQ)';;
comment on column QName(BASIC_TEST,ZONE).ID                is 'Serial(1,ZONE_SEQ)';;
-- end

