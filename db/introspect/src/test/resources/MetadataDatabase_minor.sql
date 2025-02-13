-- if NeedsCreateSequence

create sequence QName(MODEL, SEQUENCER1_SEQ)
	start with SequenceStartValue(1)
	increment by 1 SequenceCache;;

create sequence QName(MODEL, SEQUENCER2_SEQ)
	start with SequenceStartValue(1)
	increment by 1 SequenceCache;;

create sequence QName(MODEL, JOSE_SEQ)
	start with SequenceStartValue(1)
	increment by 1 SequenceCache;;

create sequence QName(MODEL, LOCATION_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;


create sequence QName(MODEL, DUMMY_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;



-- end

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

create table QName(MODEL, A_WITH_INNER) (
	CODE                              int default 0 not null,
	FIELD_A                           nvarchar(30)     default EmptyString not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_A_WITH_INNER primary key(CODE)
);;

create table QName(MODEL, DUMMY) (
	ID                                Serial(1,DUMMY_SEQ)                       not null,
	NAME                              nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	INSTANCE_VERSION                  bigint           default 0                not null,

	constraint PK_DUMMY               primary key (ID)
);;

create table QName(MODEL, DUMMY_INNER) (
	DUMMY_ID                          int                                       not null,
	SEQ_ID                            int              default 0                not null,
	ID2                               int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	INSTANCE_VERSION                  bigint           default 0                not null,

	constraint PK_DUMMY_INNER         primary key (DUMMY_ID,SEQ_ID)
);;

create table QName(MODEL, CATEGORY) (
	CODE                              nvarchar(30)     not null,
	NAME                              nvarchar(30)     not null,
	PARENT_CODE                       nvarchar(30),
	UPDATE_TIME                       datetime(3),

	constraint PK_CATEGORY primary key(CODE),
	constraint NAME_UNQT unique (NAME)
);;

create table QName(MODEL, INNER_A) (
	A_WITH_INNER_CODE                   int              default 0 not null,
	SEQ_ID                            int              default 0 not null,
	INNER_FIELD_A                     int              default 0 not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_INNER_A primary key(A_WITH_INNER_CODE, SEQ_ID)
);;

create table QName(MODEL, CATEGORY2) (
	CODE                              nvarchar(30)     not null,
	NAME                              nvarchar(30)     not null,
	PARENT_CODE                       nvarchar(30),
	UPDATE_TIME                       datetime(3),

	constraint PK_CATEGORY2 primary key(CODE),
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

create table QName(MODEL, SEQUENCER1) (
	ID                                Serial(1,SEQUENCER1_SEQ) not null,
	FIRST_NAME                        nvarchar(64)     not null,
	LAST_NAME                         nvarchar(64)     not null,
	UPDATE_TIME                       datetime(3),

	constraint PK_SEQUENCER1 primary key(ID)
);;

create table QName(MODEL, SEQUENCER2) (
	ID                                BigSerial(1,SEQUENCER2_SEQ) not null,
	FIRST_NAME                        nvarchar(64)     not null,
	LAST_NAME                         nvarchar(64)     not null,
	UPDATE_TIME                       datetime(3),

	constraint PK_SEQUENCER2 primary key(ID)
);;

create table QName(MODEL, TYPES) (
	INT1                              int              not null,
	NUM3                              decimal(3),
	NUM92                             decimal(9,2),
	REAL1                             double,
	DATE1                             date,
	DT0                               datetime(0),
	DT1                               datetime(1),
	DT3                               datetime(3),
	DT6                               datetime(6),
	BOOL                              boolean,
	STR                               nvarchar(10),

	constraint PK_TYPES primary key(INT1)
);;

create table QName(MODEL, JOSE) (
	ID                                Serial(1,JOSE_SEQ)                        not null,
	IDENT                             int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_JOSE                primary key (ID)
);;

create table QName(MODEL, _METADATA) (
	VERSION                           double           not null,
	SHA                               nvarchar(128)    not null,
	SHA_OVL                           nvarchar(128),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK__METADATA primary key(VERSION)
);;

create table QName(MODEL, LOCATION) (
	ID                                Serial(1,LOCATION_SEQ)                    not null,
	NAME                              nvarchar(40)                              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_LOCATION            primary key (ID)
);;

create table QName(MODEL, SERIAL_NUMBER) (
	PRODUCT_ID                        int              default 0                not null,
	SERIAL_NUMBER                     nvarchar(64)                              not null,
	LOCATION_ID                       int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SERIAL_NUMBER       primary key (PRODUCT_ID,SERIAL_NUMBER)
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

alter table QName(MODEL, SERIAL_NUMBER) add constraint LOCATION_SERIAL_NUMBER_FK
	foreign key (LOCATION_ID)
	references QName(MODEL, LOCATION) (ID);;

alter table QName(MODEL, INNER_A) add constraint A_WITH_INNER_FK
	foreign key(A_WITH_INNER_CODE)
	references QName(MODEL, A_WITH_INNER)(CODE);;


alter table QName(MODEL, DUMMY_INNER) add constraint DUMMY_DUMMY_INNER_FK
	foreign key (DUMMY_ID)
	references QName(MODEL, DUMMY) (ID);;


create unique index IndexName(MODEL,PERSON_UPD)
	on QName(MODEL, PERSON) (UPDATE_TIME);;

create index IndexName(MODEL,PERSON_NAME)
	on QName(MODEL, PERSON) (LAST_NAME, FIRST_NAME);;

-- if NeedsSerialComment
comment on column QName(MODEL, SEQUENCER1).ID is 'Serial(1,SEQUENCER1_SEQ)';;
comment on column QName(MODEL, SEQUENCER2).ID is 'BigSerial(1,SEQUENCER2_SEQ)';;
comment on column QName(MODEL,JOSE).ID        is 'Serial(1,JOSE_SEQ)';;
comment on column QName(MODEL,LOCATION).ID is 'Serial(1,LOCATION_SEQ)';;
comment on column QName(MODEL,A).ID                       is 'Serial(1,A_SEQ)';;
comment on column QName(MODEL,DUMMY).ID                   is 'Serial(1,DUMMY_SEQ)';;
-- end
