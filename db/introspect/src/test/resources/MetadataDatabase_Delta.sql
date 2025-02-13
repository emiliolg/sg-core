alter  table QName(MODEL, DUMMY_INNER)
	drop constraint DUMMY_DUMMY_INNER_FK;;

alter  table QName(MODEL, INNER_A)
	drop constraint A_WITH_INNER_FK;;

alter  table QName(MODEL, PERSON_ADDRESS)
	drop constraint FK_PERSON;;

alter  table QName(MODEL, ADDRESS_PHONE)
	drop constraint FK_PERSON_ADDRESS;;

alter  table QName(MODEL, PARTICIPANT)
	drop constraint FK_ADDRESS;;

alter  table QName(MODEL, SERIAL_NUMBER)
	drop constraint LOCATION_SERIAL_NUMBER_FK;;

drop   table QName(MODEL, A_WITH_INNER);;

drop   table QName(MODEL, DUMMY);;

drop   table QName(MODEL, DUMMY_INNER);;

drop   table QName(MODEL, INNER_A);;

drop   table QName(MODEL, PERSON_ADDRESS);;

drop   table QName(MODEL, SEQUENCER2);;

alter  table QName(MODEL, TYPES)
	rename to TYPES_NEW;;

create table QName(MODEL, BASE_PRODUCT) (
	CODE                              nvarchar(64)     not null,
	NAME                              nvarchar(64)     not null,

	constraint PK_BASE_PRODUCT primary key(CODE),
	constraint PNAME_UNQT unique (NAME)
);;

alter  table QName(MODEL, ADDRESS_PHONE)
	drop constraint PK_ADDRESS_PHONE;;

alter  table QName(MODEL, ADDRESS_PHONE)
	add constraint PK_ADDRESS_PHONE primary key(DOC_CODE, DOC_TYPE, ADDRESS_SEQ_ID, SEQ_ID);;

alter  table QName(MODEL, CATEGORY)
	SetDefault(UPDATE_TIME, CurrentTime);;

alter  table QName(MODEL, CATEGORY)
	SetNotNull(UPDATE_TIME);;

alter  table QName(MODEL, CATEGORY)
	drop constraint NAME_UNQT;;

alter  table QName(MODEL, JOSE)
	drop constraint PK_JOSE;;

alter  table QName(MODEL, JOSE)
	drop column ID;;

alter  table QName(MODEL, JOSE)
	AddColumn(ZIP                               int              default 0 not null);;

alter  table QName(MODEL, JOSE)
	add constraint PK_JOSE primary key(IDENT);;

alter  table QName(MODEL, LOCATION)
	drop constraint PK_LOCATION;;

alter  table QName(MODEL, LOCATION)
	drop column ID;;

alter  table QName(MODEL, LOCATION)
	AddColumn(WAREHOUSE_ID                      int              default 0 not null);;

alter  table QName(MODEL, LOCATION)
	AddColumn(AREA                              int              default 1 not null);;

alter  table QName(MODEL, LOCATION)
	add constraint PK_LOCATION primary key(WAREHOUSE_ID, AREA, NAME);;

alter  table QName(MODEL, PARTICIPANT)
	drop column ADDRESS_CODE;;

alter  table QName(MODEL, PARTICIPANT)
	add constraint FULL_NAME_UNQT unique (FIRST_NAME, LAST_NAME);;

alter  table QName(MODEL, PERSON)
	SetNotNull(UPDATE_TIME);;

alter  index IndexName(MODEL, PERSON_UPD)
	rename to PERSON_UPD2;;

drop   index IndexName(MODEL, PERSON_NAME);;

create index IndexName(MODEL, PERSON_NAME)
	on QName(MODEL, PERSON) (FIRST_NAME, LAST_NAME);;

alter  table QName(MODEL, PRODUCT)
	AddColumn(DESCRIPTION                       nvarchar(256)    default EmptyString not null);;

alter  table QName(MODEL, PRODUCT)
	AlterColumnType(NAME, nvarchar(70));;

alter  table QName(MODEL, PRODUCT)
	SetDefault(COLOR, 1);;

alter  table QName(MODEL, SERIAL_NUMBER)
	RenameColumn(LOCATION_ID, LOCATION_WAREHOUSE_ID);;

alter  table QName(MODEL, SERIAL_NUMBER)
	AddColumn(LOCATION_AREA                     int              default 1 not null);;

alter  table QName(MODEL, SERIAL_NUMBER)
	AddColumn(LOCATION_NAME                     nvarchar(40)     not null);;

alter  table QName(MODEL, TYPES_NEW)
	drop constraint PK_TYPES;;

alter  table QName(MODEL, TYPES_NEW)
	RenameColumn(NUM92, NUM99);;

alter  table QName(MODEL, TYPES_NEW)
	SetDefault(INT1, null);;

alter  table QName(MODEL, TYPES_NEW)
	SetDefault(REAL1, null);;

alter  table QName(MODEL, TYPES_NEW)
	SetDefault(DATE1, null);;

alter  table QName(MODEL, TYPES_NEW)
	SetDefault(DT0, null);;

alter  table QName(MODEL, TYPES_NEW)
	AlterColumnType(DT3, datetime(4));;

alter  table QName(MODEL, TYPES_NEW)
	SetDefault(BOOL, null);;

alter  table QName(MODEL, TYPES_NEW)
	SetDefault(STR, null);;

alter  table QName(MODEL, TYPES_NEW)
	SetDefault(NUM99, null);;

alter  table QName(MODEL, TYPES_NEW)
	add constraint PK_TYPES_NEW primary key(INT1);;

alter table QName(MODEL, SERIAL_NUMBER) add constraint LOCATION_SERIAL_NUMBER_FK
	foreign key(LOCATION_WAREHOUSE_ID, LOCATION_AREA, LOCATION_NAME)
	references QName(MODEL, LOCATION)(WAREHOUSE_ID, AREA, NAME);;

-- if NeedsCreateSequence

drop   sequence QName(MODEL, DUMMY_SEQ) /* Ignore Errors */;;

drop   sequence QName(MODEL, JOSE_SEQ) /* Ignore Errors */;;

drop   sequence QName(MODEL, LOCATION_SEQ) /* Ignore Errors */;;

drop   sequence QName(MODEL, SEQUENCER2_SEQ) /* Ignore Errors */;;

-- end
AlterView QName(MODEL, PRODUCT_SQL_VIEW) as
	select NAME,CODE from QName(MODEL,PRODUCT)  where UPDATE_TIME > to_timestamp('01011970', 'DDMMYYYY');;

CommentOnView QName(MODEL, PRODUCT_SQL_VIEW) is 'select NAME,CODE from QName(MODEL,PRODUCT)  where UPDATE_TIME > to_timestamp(''01011970'', ''DDMMYYYY'')';;

