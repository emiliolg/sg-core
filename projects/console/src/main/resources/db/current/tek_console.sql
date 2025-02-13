-- SQL for Schema TEK_CONSOLE --

-- if NeedsCreateSequence

create sequence QName(TEK_CONSOLE, CLUSTER_STATS_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(TEK_CONSOLE, CLUSTER_DEF) (
	NAME                              nvarchar(255)    default EmptyString      not null,
	STATUS                            nvarchar(50)     default 'DEACTIVE'       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CLUSTER_DEF         primary key (NAME)
);;

create table QName(TEK_CONSOLE, CLUSTER_STATS) (
	ID                                Serial(1,CLUSTER_STATS_SEQ)               not null,
	CLUSTER_NAME                      nvarchar(255)    default EmptyString      not null,
	TIME                              datetime(0)      default CurrentTime      not null,
	STATS                             clob             default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_CLUSTER_STATS       primary key (ID)
);;

create table QName(TEK_CONSOLE, IP) (
	CLUSTER_DEF_NAME                  nvarchar(255)    default EmptyString      not null,
	SEQ_ID                            int              default 0                not null,
	IP                                nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_IP                  primary key (CLUSTER_DEF_NAME,SEQ_ID)
);;

create table QName(TEK_CONSOLE, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

alter table QName(TEK_CONSOLE, CLUSTER_STATS) add constraint CLUSTER_CLUSTER_STATS_FK
	foreign key (CLUSTER_NAME)
	references QName(TEK_CONSOLE, CLUSTER_DEF) (NAME);;

alter table QName(TEK_CONSOLE, IP) add constraint CLUSTER_DEF_IP_FK
	foreign key (CLUSTER_DEF_NAME)
	references QName(TEK_CONSOLE, CLUSTER_DEF) (NAME);;

-- if NeedsSerialComment
comment on column QName(TEK_CONSOLE,CLUSTER_STATS).ID      is 'Serial(1,CLUSTER_STATS_SEQ)';;
-- end

