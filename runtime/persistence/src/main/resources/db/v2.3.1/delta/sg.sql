create table QName(SG, TASK_ENTRY) (
	NAME                              nvarchar(256)    default EmptyString not null,
	STATUS                            nvarchar(50)     default 'NOT_SCHEDULED' not null,
	CRON_EXPRESSION                   nvarchar(256)    default EmptyString not null,
	DUE_TIME                          datetime(0)      default CurrentTime not null,
	EXPIRATION_TIME                   datetime(0)      default CurrentTime      not null,
	MEMBER                            nvarchar(256)    default EmptyString not null,
	DATA                              clob             default '' not null,
	DATA_TIME                         datetime(0),
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_TASK_ENTRY          primary key (NAME)
);;

