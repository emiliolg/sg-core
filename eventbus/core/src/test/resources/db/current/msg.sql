-- SQL for Schema MSG --

-- if NeedsCreateSequence

create sequence QName(MSG, COMPLEX_ENTITY_MESSAGE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(MSG, COMPLEX_ENTITY_MESSAGE) (
	ID                                Serial(1,COMPLEX_ENTITY_MESSAGE_SEQ)      not null,
	NAME                              nvarchar(255)    default EmptyString      not null,
	ENUM_ATTR                         nvarchar(50)     default 'NAME'           not null,
	ENTITY_NAME                       nvarchar(255)                             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_COMPLEX_ENTITY_MESSAGE primary key (ID)
);;

create table QName(MSG, ENTITY_MESSAGE) (
	NAME                              nvarchar(255)    default EmptyString      not null,
	NUMBER                            int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ENTITY_MESSAGE      primary key (NAME)
);;

create table QName(MSG, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

alter table QName(MSG, COMPLEX_ENTITY_MESSAGE) add constraint ENTITY_COMPLEX_ENTIT_2C7A54_FK
	foreign key (ENTITY_NAME)
	references QName(MSG, ENTITY_MESSAGE) (NAME);;

-- if NeedsSerialComment
comment on column QName(MSG,COMPLEX_ENTITY_MESSAGE).ID     is 'Serial(1,COMPLEX_ENTITY_MESSAGE_SEQ)';;
-- end

