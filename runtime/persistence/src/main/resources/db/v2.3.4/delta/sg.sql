-- if NeedsCreateSequence

create sequence QName(SG, TASK_EXECUTION_LOG_SEQ)
	start with SequenceStartValue(1)
	increment by 1 SequenceCache;;

-- end
create table QName(SG, TASK_EXECUTION_LOG) (
	ID                                Serial(1,TASK_EXECUTION_LOG_SEQ) not null,
	NAME                              nvarchar(256)    default EmptyString not null,
	MDC                               nvarchar(40)     default EmptyString not null,
	START_TIME                        datetime(3)      default CurrentTime not null,
	END_TIME                          datetime(3),
	MEMBER                            nvarchar(256)    default EmptyString not null,
	DATA                              clob             default EmptyString not null,
	DATA_TIME                         datetime(3),
	STATUS                            nvarchar(50)     default 'NOT_SCHEDULED' not null,
	ERROR_MESSAGE                     nvarchar(1000)   default EmptyString not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_TASK_EXECUTION_LOG primary key(ID)
);;

create index IndexName(SG, TASK_EXECUTION_LOG_6D3F07_IDXT)
	on QName(SG, TASK_EXECUTION_LOG) (START_TIME);;


create index IndexName(SG, TASK_EXECUTION_LOG_NAME_IDXT)
	on QName(SG, TASK_EXECUTION_LOG) (NAME, START_TIME);;


alter  table QName(SG, TASK_ENTRY)
	AddColumn(CURRENT_LOG_ID                    int);;

alter  table QName(SG, TASK_ENTRY)
	AddColumn(ERROR_MESSAGE                     nvarchar(1000)   default EmptyString not null);;

alter  table QName(SG, TASK_ENTRY)
	SetDefault(MDC, 'INVALID');;

alter  table QName(SG, TASK_ENTRY)
	SetNotNull(MDC);;

alter  table QName(SG, TASK_ENTRY)
	AlterColumnType(DATA_TIME, datetime(3));;

alter table QName(SG, TASK_ENTRY) add constraint CURRENT_LOG_TASK_ENTRY_FK
	foreign key(CURRENT_LOG_ID)
	references QName(SG, TASK_EXECUTION_LOG)(ID);;

