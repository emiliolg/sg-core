-- if NeedsCreateSequence

create sequence QName(MODEL, SEQUENCER1_SEQ)
	start with SequenceStartValue(1)
	increment by 1 SequenceCache;;


-- end

create table QName(MODEL, ADDRESS) (
  CODE                              nvarchar(64)     not null,
  ROOM                              nvarchar(64),
  STREET                            nvarchar(64),
  UPDATE_TIME                       datetime(3),

  constraint PK_ADDRESS primary key(CODE)
);;

create table QName(MODEL, ADDRESS_PHONE) (
  DOC_TYPE int not null,
  DOC_CODE nvarchar(20) not null,
  ADDRESS_SEQ_ID int not null,
  SEQ_ID int not null,
  PHONE int not null,
  UPDATE_TIME                       datetime(3),

  constraint PK_ADDRESS_PHONE primary key(DOC_CODE, DOC_TYPE, ADDRESS_SEQ_ID, SEQ_ID)
);;

create table QName(MODEL, CATEGORY) (
    CODE         nvarchar(30) not null,
    NAME         nvarchar(30) not null,
    PARENT_CODE  nvarchar(30),
    UPDATE_TIME                       datetime(3) default CurrentTime not null,

    constraint PK_CATEGORY  primary key(CODE)
);;

create table   QName(MODEL, PARTICIPANT) (
  PID            int not null,
  FIRST_NAME     nvarchar(64) not null,
  LAST_NAME      nvarchar(64) not null,
  UPDATE_TIME                       datetime(3),

  constraint PK_PARTICIPANT primary key(PID),
  constraint FULL_NAME_UNQT unique  (FIRST_NAME, LAST_NAME)
);;

create table QName(MODEL, PERSON) (
    DOC_TYPE int not null,
    DOC_CODE nvarchar(20) not null,
    FIRST_NAME nvarchar(64) not null,
    LAST_NAME nvarchar(64) not null,
    BIRTHDAY date not null,
    SALARY decimal(10,2) not null,
    SEX nvarchar(2) not null,
    UPDATE_TIME    datetime(3) not null,

    constraint PK_PERSON primary key(DOC_TYPE, DOC_CODE)
);;

create table QName(MODEL, PRODUCT) (
    CODE                nvarchar(64) not null,
    NAME                nvarchar(70) not null,
    MAIN_CATEGORY_CODE  nvarchar(64) not null,
    COLOR               int default 1 not null,
    DESCRIPTION         nvarchar(256) default EmptyString not null,
    OPT_CATEGORY_CODE   nvarchar(64),
    UPDATE_TIME                       datetime(3),

  constraint PK_PRODUCT       primary key(CODE)
);;

create table QName(MODEL, BASE_PRODUCT) (
    CODE                nvarchar(64) not null,
    NAME                nvarchar(64) not null,

    constraint PK_BASE_PRODUCT       primary key(CODE),
    constraint PNAME_UNQT unique      (NAME)
);;

create table QName(MODEL, SEQUENCER1) (
	ID                                Serial(1,SEQUENCER1_SEQ) not null,
	FIRST_NAME                        nvarchar(64)     not null,
	LAST_NAME                         nvarchar(64)     not null,
	UPDATE_TIME                       datetime(3),

	constraint PK_SEQUENCER1 primary key(ID)
);;

create table QName(MODEL, TYPES_NEW) (
  INT1    int,
  NUM3    decimal(3),
  NUM99   decimal(9,2),
  REAL1   double,
  DATE1   date,
  DT0     datetime(0),
  DT1     datetime(1),
  DT3     datetime(4),
  DT6     datetime(6),
  BOOL    boolean,
  STR     nvarchar(10),

  constraint PK_TYPES_NEW primary key(INT1)
);;

create table QName(MODEL, JOSE) (
	IDENT                             int              default 0                not null,
	ZIP                               INT              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_JOSE                primary key (IDENT)
);;


create table QName(MODEL, _METADATA) (
    VERSION                  double          not null,
    SHA                      nvarchar(128)   not null,
    SHA_OVL                  nvarchar(128),
    SCHEMA                   clob,
    OVERLAY                  clob,

    constraint PK__METADATA primary key(VERSION)
);;

create table QName(MODEL, LOCATION) (
	WAREHOUSE_ID                      int              default 0                not null,
	AREA                              int              default 1                not null,
	NAME                              nvarchar(40)                              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_LOCATION            primary key (WAREHOUSE_ID,AREA,NAME)
);;

create table QName(MODEL, SERIAL_NUMBER) (
	PRODUCT_ID                        int              default 0                not null,
	SERIAL_NUMBER                     nvarchar(64)                              not null,
	LOCATION_WAREHOUSE_ID             int              default 0                not null,
	LOCATION_AREA                     int              default 1                not null,
	LOCATION_NAME                     nvarchar(40)                              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SERIAL_NUMBER       primary key (PRODUCT_ID,SERIAL_NUMBER)
);;

alter table QName(MODEL, SERIAL_NUMBER) add constraint LOCATION_SERIAL_NUMBER_FK
	foreign key (LOCATION_WAREHOUSE_ID, LOCATION_AREA, LOCATION_NAME)
	references QName(MODEL, LOCATION) (WAREHOUSE_ID, AREA, NAME);;


create view QName(MODEL, PRODUCT_SQL_VIEW) as
	select NAME,CODE from QName(MODEL,PRODUCT) where UPDATE_TIME > to_timestamp('01011970', 'DDMMYYYY');;

alter table QName(MODEL, CATEGORY) add constraint FK_PARENT
    foreign key(PARENT_CODE)
    references QName(MODEL, CATEGORY)(CODE);;

alter table QName(MODEL, PRODUCT) add constraint FK_MAIN_CATEGORY
  foreign key(MAIN_CATEGORY_CODE)
  references QName(MODEL, CATEGORY) (CODE);;

alter table QName(MODEL, PRODUCT) add constraint FK_OPT_CATEGORY
  foreign key(OPT_CATEGORY_CODE)
  references QName(MODEL, CATEGORY) (CODE);;

create unique index IndexName(MODEL, PERSON_UPD2)
  on QName(MODEL, PERSON) (UPDATE_TIME);;

create index IndexName(MODEL, PERSON_NAME)
  on QName(MODEL, PERSON) (FIRST_NAME, LAST_NAME);;

-- if NeedsSerialComment
comment on column QName(MODEL, SEQUENCER1).ID is 'Serial(1,SEQUENCER1_SEQ)';;
-- end

CommentOnView QName(MODEL, PRODUCT_SQL_VIEW) is 'select NAME,CODE from QName(MODEL,PRODUCT)  where UPDATE_TIME > to_timestamp(''01011970'', ''DDMMYYYY'')';;
