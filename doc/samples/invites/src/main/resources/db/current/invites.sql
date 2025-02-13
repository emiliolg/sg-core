-- SQL for Schema INVITES --

-- if NeedsCreateSequence

create sequence QName(INVITES, EVENT_MODEL_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(INVITES, EVENT_MODEL) (
	ID                                Serial(1,EVENT_MODEL_SEQ)                 not null,
	HOST                              nvarchar(255)    default EmptyString      not null,
	HOST_MAIL                         nvarchar(255)    default EmptyString      not null,
	EVENT                             nvarchar(255)    default EmptyString      not null,
	TIME                              datetime(0)      default CurrentTime      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_EVENT_MODEL         primary key (ID)
);;

create table QName(INVITES, GUEST) (
	EVENT_MODEL_ID                    int                                       not null,
	SEQ_ID                            int              default 0                not null,
	NAME                              nvarchar(255)    default EmptyString      not null,
	MAIL                              nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_GUEST               primary key (EVENT_MODEL_ID,SEQ_ID)
);;

create table QName(INVITES, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

alter table QName(INVITES, GUEST) add constraint EVENT_MODEL_GUEST_FK
	foreign key (EVENT_MODEL_ID)
	references QName(INVITES, EVENT_MODEL) (ID);;

-- if NeedsSerialComment
comment on column QName(INVITES,EVENT_MODEL).ID            is 'Serial(1,EVENT_MODEL_SEQ)';;
-- end

