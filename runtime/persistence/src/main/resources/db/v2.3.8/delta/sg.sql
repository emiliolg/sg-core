-- if NeedsCreateSequence

create sequence QName(SG, CLUSTER_STATS_SEQ)
	start with SequenceStartValue(1)
	increment by 1 SequenceCache;;

-- end
create table QName(SG, CLUSTER_CONF) (
	NAME                              nvarchar(255)    default EmptyString not null,
	STATUS                            nvarchar(50)     default 'DEACTIVE' not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,
	CREATION_TIME                     datetime(3)      default CurrentTime not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_CLUSTER_CONF primary key(NAME)
);;

create table QName(SG, CLUSTER_STATS) (
	ID                                Serial(1,CLUSTER_STATS_SEQ) not null,
	CLUSTER_NAME                      nvarchar(255)    default EmptyString not null,
	TIME                              datetime(0)      default CurrentTime not null,
	STATS                             clob             default EmptyString not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CLUSTER_STATS primary key(ID)
);;

create table QName(SG, HOST_ADDRESS) (
	CLUSTER_CONF_NAME                 nvarchar(255)    default EmptyString not null,
	SEQ_ID                            int              default 0 not null,
	ADDRESS                           nvarchar(255)    default EmptyString not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_HOST_ADDRESS primary key(CLUSTER_CONF_NAME, SEQ_ID)
);;

create table QName(SG, NODE_ENTRY) (
	NAME                              nvarchar(255)    default EmptyString not null,
	STATUS                            nvarchar(50)     default 'FAILED' not null,
	LOG                               clob             default EmptyString not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,
	CREATION_TIME                     datetime(3)      default CurrentTime not null,
	CREATION_USER                     nvarchar(100),
	UPDATE_USER                       nvarchar(100),

	constraint PK_NODE_ENTRY primary key(NAME)
);;

alter table QName(SG, CLUSTER_STATS) add constraint CLUSTER_CLUSTER_STATS_FK
	foreign key(CLUSTER_NAME)
	references QName(SG, CLUSTER_CONF)(NAME);;


alter table QName(SG, HOST_ADDRESS) add constraint CLUSTER_CONF_HOST_ADDRESS_FK
	foreign key(CLUSTER_CONF_NAME)
	references QName(SG, CLUSTER_CONF)(NAME);;


