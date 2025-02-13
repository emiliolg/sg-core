
-- SQL for Schema METAMODEL --

create table QName(METAMODEL, CITY) (
	CODE                              nvarchar(3)      not null,
	NAME                              nvarchar(150)    not null,
	TELEPHONE_CITY_CODE               nvarchar(5),
	STATE_PROVINCE_COUNTRY_CODE       nvarchar(2)      not null,
	STATE_PROVINCE_CODE               nvarchar(3)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CITY                primary key (CODE)
);;

create table QName(METAMODEL, CITY_MAPPING) (
	PROVIDER_CODE                     nvarchar(3)      not null,
	CITY_CODE                         nvarchar(3)      not null,
	PROVIDER_CITY_CODE                nvarchar(6)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CITY_MAPPING        primary key (PROVIDER_CODE,CITY_CODE)
);;

create table QName(METAMODEL, COUNTRY) (
	CODE                              nvarchar(2)      not null,
	NAME                              nvarchar(100)    not null,
	TELEPHONE_ACCESS_CODE             nvarchar(5),
	SOURCE                            nvarchar(20)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_COUNTRY             primary key (CODE)
);;

create table QName(METAMODEL, COUNTRY_MAPPING) (
	PROVIDER_CODE                     nvarchar(3)      not null,
	COUNTRY_CODE                      nvarchar(2)      not null,
	PROVIDER_COUNTRY_CODE             nvarchar(5)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_COUNTRY_MAPPING     primary key (PROVIDER_CODE,COUNTRY_CODE)
);;

create table QName(METAMODEL, CURRENCY) (
	CODE                              nvarchar(3)      not null,
	NAME                              nvarchar(150)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CURRENCY            primary key (CODE)
);;

create table QName(METAMODEL, CURRENCY_MAPPING) (
	PROVIDER_CODE                     nvarchar(3)      not null,
	CURRENCY_CODE                     nvarchar(3)      not null,
	PROVIDER_CURRENCY_CODE            nvarchar(5)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CURRENCY_MAPPING    primary key (PROVIDER_CODE,CURRENCY_CODE)
);;

create table QName(METAMODEL, HOTEL) (
	PROVIDER_CODE                     nvarchar(3)      not null,
	CODE                              nvarchar(5)      not null,
	NAME                              nvarchar(150)    not null,
	DESCRIPTION                       nvarchar(2000)   not null,
	SHORT_LOCATION                    nvarchar(50)     not null,
	LONG_LOCATION                     nvarchar(250)    not null,
	LONGITUDE                         nvarchar(150)    not null,
	LATITUDE                          nvarchar(150)    not null,
	ADDRESS_LINE                      nvarchar(150)    not null,
	STREET_NUMBER                     int              not null,
	POSTAL_CODE                       nvarchar(10)     not null,
	CITY_CODE                         nvarchar(3)      not null,
	RATING_CODE                       nvarchar(3)      not null,
	CREDIT_CARDS                      nvarchar(30),
	BUILDING_CONSTRUCTED_YEAR         nvarchar(4),
	BUILDING_RENOVATED_YEAR           nvarchar(4),
	TOTAL_ROOMS                       int,
	TOTAL_FLOORS                      int,
	CHECK_IN_FROM_TIME                nvarchar(5)      not null,
	CHECK_IN_TO_TIME                  nvarchar(5)      not null,
	CHECK_OUT_FROM_TIME               nvarchar(5)      not null,
	CHECK_OUT_TO_TIME                 nvarchar(5)      not null,
	CHILDREN_FROM_AGE                 nvarchar(2)      not null,
	CHILDREN_TO_AGE                   nvarchar(2)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_HOTEL               primary key (PROVIDER_CODE,CODE)
);;

create table QName(METAMODEL, HOTEL_AMENITY) (
	HOTEL_PROVIDER_CODE               nvarchar(3)      not null,
	HOTEL_CODE                        nvarchar(5)      not null,
	AMENITY_TYPE                      nvarchar(50)     not null,
	AMENITY_DESCRIPTION               nvarchar(250)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_HOTEL_AMENITY       primary key (HOTEL_PROVIDER_CODE,HOTEL_CODE)
);;

create table QName(METAMODEL, HOTEL_CONTACT_NUMBER) (
	HOTEL_PROVIDER_CODE               nvarchar(3)      not null,
	HOTEL_CODE                        nvarchar(5)      not null,
	AREA_CITY_CODE                    nvarchar(5)      not null,
	COUNTRY_ACCESS_CODE               nvarchar(2)      not null,
	PHONE_NUMBER                      nvarchar(10)     not null,
	PHONE_EXTENSION                   nvarchar(5)      not null,
	PHONE_TYPE                        nvarchar(50)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_HOTEL_CONTACT_NUMBER primary key (HOTEL_PROVIDER_CODE,HOTEL_CODE)
);;

create table QName(METAMODEL, HOTEL_MULTIMEDIA_DESCRIPTION) (
	HOTEL_PROVIDER_CODE               nvarchar(3)      not null,
	HOTEL_CODE                        nvarchar(5)      not null,
	MULTIMEDIA_TYPE                   nvarchar(50)     not null,
	IS_MAIN                           int              not null,
	MULTIMEDIA_CATEGORY_CODE          nvarchar(5)      not null,
	URL                               nvarchar(500)    not null,
	TITLE                             nvarchar(250)    not null,
	DESCRIPTION                       nvarchar(2000)   not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_HOTEL_MULTIMEDIA_DESCRIPTIO primary key (HOTEL_PROVIDER_CODE,HOTEL_CODE)
);;

create table QName(METAMODEL, HOTEL_RELATIVE_POSITION) (
	HOTEL_PROVIDER_CODE               nvarchar(3)      not null,
	HOTEL_CODE                        nvarchar(5)      not null,
	DISTANCE                          int              not null,
	PROVIDER_UOM                      nvarchar(3)      not null,
	DESCRIPTION                       nvarchar(250)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_HOTEL_RELATIVE_POSITION primary key (HOTEL_PROVIDER_CODE,HOTEL_CODE)
);;

create table QName(METAMODEL, HOTEL_ROOM_TYPE) (
	HOTEL_PROVIDER_CODE               nvarchar(3)      not null,
	HOTEL_CODE                        nvarchar(5)      not null,
	ROOM_TYPE_CODE                    nvarchar(10)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_HOTEL_ROOM_TYPE     primary key (HOTEL_PROVIDER_CODE,HOTEL_CODE)
);;

create table QName(METAMODEL, HOTEL_ROOM_TYPES_AMENITIES) (
	ROOM_TYPE_HOTEL_PROVIDER_CODE     nvarchar(3)      not null,
	ROOM_TYPE_HOTEL_CODE              nvarchar(5)      not null,
	AMENITY_DESCRIPTION               nvarchar(250)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_HOTEL_ROOM_TYPES_AMENITIES primary key (ROOM_TYPE_HOTEL_PROVIDER_CODE,ROOM_TYPE_HOTEL_CODE)
);;

create table QName(METAMODEL, HOTEL_TYPE) (
	CODE                              nvarchar(3)      not null,
	NAME                              nvarchar(50)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_HOTEL_TYPE          primary key (CODE)
);;

create table QName(METAMODEL, HOTEL_TYPE_MAPPING) (
	PROVIDER_CODE                     nvarchar(3)      not null,
	HOTEL_TYPE_CODE                   nvarchar(3)      not null,
	PROVIDER_HOTEL_TYPE_CODE          nvarchar(5)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_HOTEL_TYPE_MAPPING  primary key (PROVIDER_CODE,HOTEL_TYPE_CODE)
);;

create table QName(METAMODEL, MULTIMEDIA_CATEGORY) (
	CODE                              nvarchar(5)      not null,
	DESCRIPTION                       nvarchar(2000)   not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_MULTIMEDIA_CATEGORY primary key (CODE)
);;

create table QName(METAMODEL, PRODUCT) (
	CODE                              nvarchar(3)      not null,
	NAME                              nvarchar(150)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PRODUCT             primary key (CODE)
);;

create table QName(METAMODEL, PRODUCT_PROVIDER) (
	PRODUCT_CODE                      nvarchar(3)      not null,
	PROVIDER_CODE                     nvarchar(3)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PRODUCT_PROVIDER    primary key (PRODUCT_CODE,PROVIDER_CODE)
);;

create table QName(METAMODEL, PROVIDER) (
	CODE                              nvarchar(3)      not null,
	NAME                              nvarchar(150)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PROVIDER            primary key (CODE)
);;

create table QName(METAMODEL, PROVIDER_PROPERTY) (
	PROVIDER_CODE                     nvarchar(3)      not null,
	PROPERTY                          nvarchar(50)     not null,
	VALUE                             nvarchar(400)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_PROVIDER_PROPERTY   primary key (PROVIDER_CODE,PROPERTY)
);;

create table QName(METAMODEL, RATING) (
	CODE                              nvarchar(3)      not null,
	DESCRIPTION                       nvarchar(150)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_RATING              primary key (CODE)
);;

create table QName(METAMODEL, RATING_MAPPING) (
	PROVIDER_CODE                     nvarchar(3)      not null,
	RATING_CODE                       nvarchar(3)      not null,
	PROVIDER_RATING_CODE              nvarchar(5)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_RATING_MAPPING      primary key (PROVIDER_CODE,RATING_CODE,PROVIDER_RATING_CODE)
);;

create table QName(METAMODEL, STATE_PROVINCE) (
	CODE                              nvarchar(3)      not null,
	COUNTRY_CODE                      nvarchar(2)      not null,
	NAME                              nvarchar(100)    not null,
	STATE_PROVINCE_TYPE_CODE          nvarchar(3),
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_STATE_PROVINCE      primary key (COUNTRY_CODE,CODE)
);;

create table QName(METAMODEL, STATE_PROVINCE_TYPE) (
	CODE                              nvarchar(3)      not null,
	NAME                              nvarchar(40)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_STATE_PROVINCE_TYPE primary key (CODE)
);;

create table QName(METAMODEL, UNIT_OF_MEASURE) (
	CODE                              nvarchar(3)      not null,
	DESCRIPTION                       nvarchar(150)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_UNIT_OF_MEASURE     primary key (CODE)
);;

create table QName(METAMODEL, UNIT_OF_MEASURE_MAPPING) (
	PROVIDER_CODE                     nvarchar(3)      not null,
	UNIT_OF_MEASURE_CODE              nvarchar(3)      not null,
	PROVIDER_UNIT_OF_MEASURE_CODE     nvarchar(5)      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_UNIT_OF_MEASURE_MAPPING primary key (PROVIDER_CODE,UNIT_OF_MEASURE_CODE)
);;

create table QName(METAMODEL, _METADATA) (
	VERSION                           nvarchar(24)     not null,
	SHA                               nvarchar(128)    not null,
	SHA_OVL                           nvarchar(128),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

alter table QName(METAMODEL, CITY) add constraint STATE_PROVINCE_CITY_FK
	foreign key (STATE_PROVINCE_COUNTRY_CODE, STATE_PROVINCE_CODE)
	references QName(METAMODEL, STATE_PROVINCE) (COUNTRY_CODE, CODE);;

alter table QName(METAMODEL, CITY_MAPPING) add constraint PROVIDER_CITY_MAPPING_FK
	foreign key (PROVIDER_CODE)
	references QName(METAMODEL, PROVIDER) (CODE);;

alter table QName(METAMODEL, CITY_MAPPING) add constraint CITY_CITY_MAPPING_FK
	foreign key (CITY_CODE)
	references QName(METAMODEL, CITY) (CODE);;

alter table QName(METAMODEL, COUNTRY_MAPPING) add constraint PROVIDER_COUNTRY_MAPPING_FK
	foreign key (PROVIDER_CODE)
	references QName(METAMODEL, PROVIDER) (CODE);;

alter table QName(METAMODEL, COUNTRY_MAPPING) add constraint COUNTRY_COUNTRY_MAPPING_FK
	foreign key (COUNTRY_CODE)
	references QName(METAMODEL, COUNTRY) (CODE);;

alter table QName(METAMODEL, CURRENCY_MAPPING) add constraint PROVIDER_CURRENCY_MAPPING_FK
	foreign key (PROVIDER_CODE)
	references QName(METAMODEL, PROVIDER) (CODE);;

alter table QName(METAMODEL, CURRENCY_MAPPING) add constraint CURRENCY_CURRENCY_MAPPING_FK
	foreign key (CURRENCY_CODE)
	references QName(METAMODEL, CURRENCY) (CODE);;

alter table QName(METAMODEL, HOTEL) add constraint PROVIDER_HOTEL_FK
	foreign key (PROVIDER_CODE)
	references QName(METAMODEL, PROVIDER) (CODE);;

alter table QName(METAMODEL, HOTEL) add constraint CITY_HOTEL_FK
	foreign key (CITY_CODE)
	references QName(METAMODEL, CITY) (CODE);;

alter table QName(METAMODEL, HOTEL) add constraint RATING_HOTEL_FK
	foreign key (RATING_CODE)
	references QName(METAMODEL, RATING) (CODE);;

alter table QName(METAMODEL, HOTEL_AMENITY) add constraint HOTEL_HOTEL_AMENITY_FK
	foreign key (HOTEL_PROVIDER_CODE, HOTEL_CODE)
	references QName(METAMODEL, HOTEL) (PROVIDER_CODE, CODE);;

alter table QName(METAMODEL, HOTEL_CONTACT_NUMBER) add constraint HOTEL_HOTEL_CONTACT_NUMBER_FK
	foreign key (HOTEL_PROVIDER_CODE, HOTEL_CODE)
	references QName(METAMODEL, HOTEL) (PROVIDER_CODE, CODE);;

alter table QName(METAMODEL, HOTEL_MULTIMEDIA_DESCRIPTION) add constraint HOTEL_HOTEL_MULTIMED_6507B0_FK
	foreign key (HOTEL_PROVIDER_CODE, HOTEL_CODE)
	references QName(METAMODEL, HOTEL) (PROVIDER_CODE, CODE);;

alter table QName(METAMODEL, HOTEL_MULTIMEDIA_DESCRIPTION) add constraint MULTIMEDIA_CATEGORY__A335C5_FK
	foreign key (MULTIMEDIA_CATEGORY_CODE)
	references QName(METAMODEL, MULTIMEDIA_CATEGORY) (CODE);;

alter table QName(METAMODEL, HOTEL_RELATIVE_POSITION) add constraint HOTEL_HOTEL_RELATIVE_B4459C_FK
	foreign key (HOTEL_PROVIDER_CODE, HOTEL_CODE)
	references QName(METAMODEL, HOTEL) (PROVIDER_CODE, CODE);;

alter table QName(METAMODEL, HOTEL_ROOM_TYPE) add constraint HOTEL_HOTEL_ROOM_TYPE_FK
	foreign key (HOTEL_PROVIDER_CODE, HOTEL_CODE)
	references QName(METAMODEL, HOTEL) (PROVIDER_CODE, CODE);;

alter table QName(METAMODEL, HOTEL_ROOM_TYPES_AMENITIES) add constraint ROOM_TYPE_HOTEL_ROOM_EE8ED1_FK
	foreign key (ROOM_TYPE_HOTEL_PROVIDER_CODE, ROOM_TYPE_HOTEL_CODE)
	references QName(METAMODEL, HOTEL_ROOM_TYPE) (HOTEL_PROVIDER_CODE, HOTEL_CODE);;

alter table QName(METAMODEL, HOTEL_TYPE_MAPPING) add constraint PROVIDER_HOTEL_TYPE_MAPPING_FK
	foreign key (PROVIDER_CODE)
	references QName(METAMODEL, PROVIDER) (CODE);;

alter table QName(METAMODEL, HOTEL_TYPE_MAPPING) add constraint HOTEL_TYPE_HOTEL_TYP_B92734_FK
	foreign key (HOTEL_TYPE_CODE)
	references QName(METAMODEL, HOTEL_TYPE) (CODE);;

alter table QName(METAMODEL, PRODUCT_PROVIDER) add constraint PRODUCT_PRODUCT_PROVIDER_FK
	foreign key (PRODUCT_CODE)
	references QName(METAMODEL, PRODUCT) (CODE);;

alter table QName(METAMODEL, PRODUCT_PROVIDER) add constraint PROVIDER_PRODUCT_PROVIDER_FK
	foreign key (PROVIDER_CODE)
	references QName(METAMODEL, PROVIDER) (CODE);;

alter table QName(METAMODEL, PROVIDER_PROPERTY) add constraint PROVIDER_PROVIDER_PROPERTY_FK
	foreign key (PROVIDER_CODE)
	references QName(METAMODEL, PROVIDER) (CODE);;

alter table QName(METAMODEL, RATING_MAPPING) add constraint PROVIDER_RATING_MAPPING_FK
	foreign key (PROVIDER_CODE)
	references QName(METAMODEL, PROVIDER) (CODE);;

alter table QName(METAMODEL, RATING_MAPPING) add constraint RATING_RATING_MAPPING_FK
	foreign key (RATING_CODE)
	references QName(METAMODEL, RATING) (CODE);;

alter table QName(METAMODEL, STATE_PROVINCE) add constraint COUNTRY_STATE_PROVINCE_FK
	foreign key (COUNTRY_CODE)
	references QName(METAMODEL, COUNTRY) (CODE);;

alter table QName(METAMODEL, STATE_PROVINCE) add constraint STATE_PROVINCE_TYPE__CFAAFC_FK
	foreign key (STATE_PROVINCE_TYPE_CODE)
	references QName(METAMODEL, STATE_PROVINCE_TYPE) (CODE);;

alter table QName(METAMODEL, UNIT_OF_MEASURE_MAPPING) add constraint PROVIDER_UNIT_OF_MEA_AEC3FA_FK
	foreign key (PROVIDER_CODE)
	references QName(METAMODEL, PROVIDER) (CODE);;

alter table QName(METAMODEL, UNIT_OF_MEASURE_MAPPING) add constraint UNIT_OF_MEASURE_UNIT_2C0C0A_FK
	foreign key (UNIT_OF_MEASURE_CODE)
	references QName(METAMODEL, UNIT_OF_MEASURE) (CODE);;

