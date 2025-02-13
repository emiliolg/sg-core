-- SQL for Schema TEST --

create table QName(TEST, EA) (
	A                                 int              default 0                not null,
	B                                 decimal(10,2)    default 0                not null,
	C                                 nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_EA                  primary key (A,B)
);;

create table QName(TEST, EB) (
	A                                 int              default 0                not null,
	B                                 decimal(10,2)    default 0                not null,
	C                                 nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_EB                  primary key (A,B)
);;

create table QName(TEST, EC) (
	A                                 int              default 0                not null,
	B                                 decimal(10,2)    default 0                not null,
	C                                 nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_EC                  primary key (A,B)
);;

create table QName(TEST, ED) (
	A                                 int              default 0                not null,
	B                                 decimal(10,2)    default 0                not null,
	C                                 nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ED                  primary key (A,B)
);;

create table QName(TEST, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

