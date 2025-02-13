-- if NeedsCreateSequence

create sequence QName(AUTHORIZATION, FAVORITE_SEQ)
	start with SequenceStartValue(1)
	increment by 1 SequenceCache;;

-- end
create table QName(AUTHORIZATION, FAVORITE) (
	ID                                Serial(1,FAVORITE_SEQ) not null,
	USER_ID                           nvarchar(256)    default EmptyString not null,
	LINK                              nvarchar(150)    default EmptyString not null,
	INDEX                             int              default 0 not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_FAVORITE primary key(ID)
);;

alter table QName(AUTHORIZATION, FAVORITE) add constraint USER_FAVORITE_FK
	foreign key(USER_ID)
	references QName(AUTHORIZATION, USER)(ID);;


