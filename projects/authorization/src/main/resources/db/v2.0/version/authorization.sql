-- SQL for Schema AUTHORIZATION --

-- if NeedsCreateSequence

create sequence QName(AUTHORIZATION, FAVORITE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(AUTHORIZATION, ROLE_PERMISSION_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(AUTHORIZATION, APPLICATION) (
	ID                                nvarchar(256)    default EmptyString      not null,
	TOKEN                             nvarchar(512)    default EmptyString      not null,
	USER_ID                           nvarchar(256)                             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_APPLICATION         primary key (ID),
	constraint APPLICATION_TOKEN_UNQT unique      (TOKEN)
);;

create table QName(AUTHORIZATION, APPLICATION_AUDIT) (
	APPLICATION                       nvarchar(255)    default EmptyString      not null,
	USER_ID                           nvarchar(256)                             not null,
	LAST_EVENT                        datetime(0)      default CurrentTime      not null,
	YESTERDAY_EVENTS                  int              default 0                not null,
	DAY_EVENTS                        int              default 0                not null,
	LAST_WEEK_EVENTS                  int              default 0                not null,
	WEEK_EVENTS                       int              default 0                not null,
	LAST_MONTH_EVENTS                 int              default 0                not null,
	MONTH_EVENTS                      int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_APPLICATION_AUDIT   primary key (APPLICATION,USER_ID)
);;

create table QName(AUTHORIZATION, DEVICE) (
	USER_ID                           nvarchar(256)                             not null,
	SEQ_ID                            int              default 0                not null,
	NAME                              nvarchar(25)     default EmptyString      not null,
	DEVICE_ID                         nvarchar(256)    default EmptyString      not null,
	DISABLED                          boolean          default False CheckBoolConstraint(DEVICE_DISABLED_B, DISABLED) not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_DEVICE              primary key (USER_ID,SEQ_ID)
);;

create table QName(AUTHORIZATION, FAVORITE) (
	ID                                Serial(1,FAVORITE_SEQ)                    not null,
	USER_ID                           nvarchar(256)                             not null,
	LINK                              nvarchar(150)    default EmptyString      not null,
	INDEX                             int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_FAVORITE            primary key (ID)
);;

create table QName(AUTHORIZATION, INVALIDATED_SESSION) (
	SESSION_ID                        nvarchar(255)    default EmptyString      not null,
	EXPIRED                           datetime(0)      default CurrentTime      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_INVALIDATED_SESSION primary key (SESSION_ID)
);;

create table QName(AUTHORIZATION, ORG_UNIT) (
	NAME                              nvarchar(10)     default EmptyString      not null,
	DESCRIPTION                       nvarchar(60)     default EmptyString      not null,
	PARENT_NAME                       nvarchar(10),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ORG_UNIT            primary key (NAME)
);;

create table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES) (
	ORG_UNIT_NAME                     nvarchar(10)                              not null,
	SEQ_ID                            int              default 0                not null,
	PROPERTY_ID                       nvarchar(10)                              not null,
	PROPERTY_NAME                     nvarchar(20)                              not null,
	PROPERTY_TYPE                     nvarchar(50)                              not null,
	VALUE                             nvarchar(20)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ORG_UNIT_PROPERTIES primary key (ORG_UNIT_NAME,SEQ_ID)
);;

create table QName(AUTHORIZATION, PERMISSIONS) (
	ROLE_PERMISSION_ID                int                                       not null,
	SEQ_ID                            int              default 0                not null,
	PERMISSION                        nvarchar(50)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PERMISSIONS         primary key (ROLE_PERMISSION_ID,SEQ_ID)
);;

create table QName(AUTHORIZATION, PROPERTY) (
	ID                                nvarchar(10)     default EmptyString      not null,
	NAME                              nvarchar(20)     default EmptyString      not null,
	TYPE                              nvarchar(50)     default 'STRING'         not null,
	REQUIRED                          boolean          default False CheckBoolConstraint(PROPERTY_REQUIRED_B, REQUIRED) not null,
	SCOPE                             nvarchar(50)     default 'COMPANY'        not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_PROPERTY            primary key (ID,NAME,TYPE)
);;

create table QName(AUTHORIZATION, ROLE) (
	ID                                nvarchar(20)     default EmptyString      not null,
	NAME                              nvarchar(25)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ROLE                primary key (ID)
);;

create table QName(AUTHORIZATION, ROLE_ASSIGNMENT) (
	USER_ID                           nvarchar(256)                             not null,
	ROLE_ID                           nvarchar(20)                              not null,
	OU_NAME                           nvarchar(10)                              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ROLE_ASSIGNMENT     primary key (USER_ID,ROLE_ID,OU_NAME)
);;

create table QName(AUTHORIZATION, ROLE_PERMISSION) (
	ID                                Serial(1,ROLE_PERMISSION_SEQ)             not null,
	ROLE_ID                           nvarchar(20)                              not null,
	DOMAIN                            nvarchar(50)     default EmptyString      not null,
	APPLICATION                       nvarchar(50)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ROLE_PERMISSION     primary key (ID),
	constraint ROLE_PERMISSION_AP_8BD3DA_UNQT unique      (ROLE_ID,DOMAIN,APPLICATION)
);;

create table QName(AUTHORIZATION, SOCIAL_PROFILE) (
	PROVIDER                          nvarchar(30)     default EmptyString      not null,
	PROFILE                           nvarchar(256)    default EmptyString      not null,
	USER_ID                           nvarchar(256)                             not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SOCIAL_PROFILE      primary key (PROVIDER,PROFILE)
);;

create table QName(AUTHORIZATION, USER) (
	ID                                nvarchar(256)    default EmptyString      not null,
	NAME                              nvarchar(25)     default EmptyString      not null,
	DEFAULT_OU_NAME                   nvarchar(10),
	EMAIL                             nvarchar(100),
	LOCALE                            nvarchar(10),
	PASSWORD                          nvarchar(256),
	PICTURE                           nvarchar(128),
	LAST_LOGIN                        datetime(0),
	CONFIRMATION_CODE                 nvarchar(50),
	CONFIRMATION_CODE_EXPIRATION      datetime(0),
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,
	DEPRECATION_TIME                  datetime(3),
	DEPRECATION_USER                  nvarchar(100),
	CREATION_TIME                     datetime(3)      default CurrentTime      not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_USER                primary key (ID),
	constraint USER_CONFIRMATION_CODE_UNQT unique      (CONFIRMATION_CODE)
);;

create table QName(AUTHORIZATION, USER_PROPERTIES) (
	USER_ID                           nvarchar(256)                             not null,
	SEQ_ID                            int              default 0                not null,
	PROPERTY_ID                       nvarchar(10)                              not null,
	PROPERTY_NAME                     nvarchar(20)                              not null,
	PROPERTY_TYPE                     nvarchar(50)                              not null,
	VALUE                             nvarchar(512)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_USER_PROPERTIES     primary key (USER_ID,SEQ_ID)
);;

create table QName(AUTHORIZATION, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

create index IndexName(AUTHORIZATION, INVALIDATED_SESSIO_BC9FEC_IDXT)
	on QName(AUTHORIZATION, INVALIDATED_SESSION) (EXPIRED) IndexTableSpace;;

create index IndexName(AUTHORIZATION, ROLE_ASSIGNMENT_AS_F0B874_IDXT)
	on QName(AUTHORIZATION, ROLE_ASSIGNMENT) (USER_ID,OU_NAME) IndexTableSpace;;

create index IndexName(AUTHORIZATION, ROLE_PERMISSION_DOMAIN_IDXT)
	on QName(AUTHORIZATION, ROLE_PERMISSION) (ROLE_ID,DOMAIN) IndexTableSpace;;

create index IndexName(AUTHORIZATION, SOCIAL_PROFILE_USER_IDXT)
	on QName(AUTHORIZATION, SOCIAL_PROFILE) (USER_ID) IndexTableSpace;;

create index IndexName(AUTHORIZATION, USER_EMAIL_IDXT)
	on QName(AUTHORIZATION, USER) (EMAIL) IndexTableSpace;;

alter table QName(AUTHORIZATION, APPLICATION) add constraint USER_APPLICATION_FK
	foreign key (USER_ID)
	references QName(AUTHORIZATION, USER) (ID);;

alter table QName(AUTHORIZATION, APPLICATION_AUDIT) add constraint USER_APPLICATION_AUDIT_FK
	foreign key (USER_ID)
	references QName(AUTHORIZATION, USER) (ID);;

alter table QName(AUTHORIZATION, DEVICE) add constraint USER_DEVICE_FK
	foreign key (USER_ID)
	references QName(AUTHORIZATION, USER) (ID);;

alter table QName(AUTHORIZATION, FAVORITE) add constraint USER_FAVORITE_FK
	foreign key (USER_ID)
	references QName(AUTHORIZATION, USER) (ID);;

alter table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES) add constraint ORG_UNIT_ORG_UNIT_PR_B2949B_FK
	foreign key (ORG_UNIT_NAME)
	references QName(AUTHORIZATION, ORG_UNIT) (NAME);;

alter table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES) add constraint PROPERTY_ORG_UNIT_PR_922E2B_FK
	foreign key (PROPERTY_ID, PROPERTY_NAME, PROPERTY_TYPE)
	references QName(AUTHORIZATION, PROPERTY) (ID, NAME, TYPE);;

alter table QName(AUTHORIZATION, PERMISSIONS) add constraint ROLE_PERMISSION_PERMISSIONS_FK
	foreign key (ROLE_PERMISSION_ID)
	references QName(AUTHORIZATION, ROLE_PERMISSION) (ID);;

alter table QName(AUTHORIZATION, ROLE_ASSIGNMENT) add constraint USER_ROLE_ASSIGNMENT_FK
	foreign key (USER_ID)
	references QName(AUTHORIZATION, USER) (ID);;

alter table QName(AUTHORIZATION, ROLE_ASSIGNMENT) add constraint ROLE_ROLE_ASSIGNMENT_FK
	foreign key (ROLE_ID)
	references QName(AUTHORIZATION, ROLE) (ID);;

alter table QName(AUTHORIZATION, ROLE_ASSIGNMENT) add constraint OU_ROLE_ASSIGNMENT_FK
	foreign key (OU_NAME)
	references QName(AUTHORIZATION, ORG_UNIT) (NAME);;

alter table QName(AUTHORIZATION, ROLE_PERMISSION) add constraint ROLE_ROLE_PERMISSION_FK
	foreign key (ROLE_ID)
	references QName(AUTHORIZATION, ROLE) (ID);;

alter table QName(AUTHORIZATION, SOCIAL_PROFILE) add constraint USER_SOCIAL_PROFILE_FK
	foreign key (USER_ID)
	references QName(AUTHORIZATION, USER) (ID);;

alter table QName(AUTHORIZATION, USER) add constraint DEFAULT_OU_USER_FK
	foreign key (DEFAULT_OU_NAME)
	references QName(AUTHORIZATION, ORG_UNIT) (NAME);;

alter table QName(AUTHORIZATION, USER_PROPERTIES) add constraint USER_USER_PROPERTIES_FK
	foreign key (USER_ID)
	references QName(AUTHORIZATION, USER) (ID);;

alter table QName(AUTHORIZATION, USER_PROPERTIES) add constraint PROPERTY_USER_PROPERTIES_FK
	foreign key (PROPERTY_ID, PROPERTY_NAME, PROPERTY_TYPE)
	references QName(AUTHORIZATION, PROPERTY) (ID, NAME, TYPE);;

-- if NeedsSerialComment
comment on column QName(AUTHORIZATION,FAVORITE).ID         is 'Serial(1,FAVORITE_SEQ)';;
comment on column QName(AUTHORIZATION,ROLE_PERMISSION).ID  is 'Serial(1,ROLE_PERMISSION_SEQ)';;
-- end

