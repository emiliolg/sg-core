-- Pretty SQL File ----
-- Tables used for the DbEntitySuite
--
-- if $TABLE_TABLESPACE
alter user $SCHEMA(MODEL) default tablespace $table_tablespace;
-- end

--if NeedsCreateSequence
  create sequence TableName(MODEL, SEQUENCER_SEQ)
    start with SequenceStartValue(1)
    increment by 1;;
--end


create table TableName(SG, LAST_DELETED) (
  ENTITY    nvarchar(128) not null,
  TS   timestamp(3)  not null,

  constraint PK_LAST_DELETED_INDEX primary key (ENTITY)
);;

create table TableName(MODEL, _METADATA) (
    VERSION                  double          not null,
    SHA                      nvarchar(128)   not null,
    SHA_OVL                  nvarchar(128),
    SCHEMA                   clob,
    OVERLAY                  clob,
    constraint PK__METADATA
        primary key(VERSION)
);;


create table TableName(MODEL, ADDRESS) (
    CODE    nvarchar(64) not null,
    ROOM    nvarchar(64),
    STREET  nvarchar(64),
    UPDATE_TIME                       datetime(3),
  constraint pk_address primary key(CODE)
);;

create table   TableName(MODEL, PARTICIPANT) (
  ID             int not null,
  FIRST_NAME     nvarchar(64) not null,
  LAST_NAME      nvarchar(64) not null,
  ADDRESS_CODE   nvarchar(64),
  NATIONALITIES bigint default 0 not null,

  UPDATE_TIME                       datetime(3),

  constraint pk_participant primary key(ID),
  constraint fk_address foreign key(ADDRESS_CODE) references TableName(MODEL, ADDRESS)(CODE)
);;


create table TableName(MODEL, CATEGORY) (
    CODE         nvarchar(30) not null,
    NAME         nvarchar(30) not null,
    PARENT_CODE  nvarchar(30),
    UPDATE_TIME                       datetime(3),

  constraint pk_category  primary key(CODE),
  constraint fk_parent    foreign key(PARENT_CODE) references TableName(MODEL, CATEGORY)(CODE)
);;

create table TableName(MODEL, PRODUCT) (
    CODE                nvarchar(64) not null,
    NAME                nvarchar(64) not null,
    MAIN_CATEGORY_CODE  nvarchar(64) not null,
    COLOR               integer not null,
    OPT_CATEGORY_CODE   nvarchar(64),
    UPDATE_TIME                       datetime(3),

  constraint pk_product       primary key(CODE),
  constraint fk_main_category foreign key(MAIN_CATEGORY_CODE) references TableName(MODEL, CATEGORY) (CODE),
  constraint fk_opt_category  foreign key(OPT_CATEGORY_CODE)  references TableName(MODEL, CATEGORY) (CODE)
);;

create table TableName(MODEL, PERSON) (
    DOC_TYPE int not null,
    DOC_CODE varchar(20) not null,
    FIRST_NAME varchar(64) not null,
    LAST_NAME varchar(64) not null,
    BIRTHDAY date not null,
    SALARY decimal(10,2) not null,
    SEX varchar(2) not null,
    UPDATE_TIME                       datetime(3),

  constraint PK_PERSON primary key(DOC_TYPE, DOC_CODE)
);;

create table TableName(MODEL, PERSON_ADDRESS) (
    DOC_TYPE int not null,
    DOC_CODE varchar(20) not null,
    SEQ_ID int not null,
    STREET varchar(64) not null,
    UPDATE_TIME                       datetime(3),

    constraint PK_PERSON_ADDRESS primary key(DOC_TYPE, DOC_CODE, SEQ_ID),
    constraint FK_PERSON foreign key(DOC_TYPE, DOC_CODE) references TableName(MODEL, PERSON)(DOC_TYPE, DOC_CODE)
);;

create table TableName(MODEL, ADDRESS_PHONE) (
  DOC_TYPE int not null,
  DOC_CODE varchar(20) not null,
  ADDRESS_SEQ_ID int not null,
  SEQ_ID int not null,
  PHONE int not null,
  UPDATE_TIME                       datetime(3),

  constraint PK_ADDRESS_PHONE primary key(DOC_TYPE, DOC_CODE, ADDRESS_SEQ_ID, SEQ_ID),
  constraint FK_PERSON_ADDRESS foreign key(DOC_TYPE, DOC_CODE, ADDRESS_SEQ_ID)
            references TableName(MODEL, PERSON_ADDRESS)(DOC_TYPE, DOC_CODE, SEQ_ID)
);;


create table TableName(MODEL, SEQUENCER) (
    ID Serial(1,SEQUENCER_SEQ) not null,
    FIRST_NAME varchar(64) not null,
    LAST_NAME varchar(64) not null,
    UPDATE_TIME                       datetime(3),

  constraint PK_SEQUENCER primary key(ID)
);;

create table TableName(MODEL, CLIENT) (
    ID    int not null,
    NAME  nvarchar(30) not null,
    MAIL  nvarchar(30),
    UPDATE_TIME                       datetime(3),


  constraint PK_CLIENT  primary key(ID)
);;

create table TableName(MODEL, MARKETING_CLIENT) (
    ID    int not null,
    ORGANIZATION  nvarchar(30),
    CODE  int,
    UPDATE_TIME                       datetime(3),


  constraint PK_MCLIENT  primary key(ID)
);;




