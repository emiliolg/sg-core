create table QName(AUTHORIZATION, INVALIDATED_SESSION) (
	SESSION_ID                        nvarchar(255)    default EmptyString not null,
	EXPIRED                           datetime(0)      default CurrentTime not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_INVALIDATED_SESSION primary key(SESSION_ID)
);;

create index IndexName(AUTHORIZATION, INVALIDATED_SESSIO_BC9FEC_IDXT)
	on QName(AUTHORIZATION, INVALIDATED_SESSION) (EXPIRED);;


