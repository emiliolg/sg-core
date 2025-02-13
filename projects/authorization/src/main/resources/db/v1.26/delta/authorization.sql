create table QName(AUTHORIZATION, SOCIAL_PROFILE) (
	PROVIDER                          nvarchar(30)     default EmptyString not null,
	PROFILE                           nvarchar(256)    default EmptyString not null,
	USER_ID                           nvarchar(256)    not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_SOCIAL_PROFILE primary key(PROVIDER, PROFILE)
);;

create index IndexName(AUTHORIZATION, SOCIAL_PROFILE_USER_IDXT)
	on QName(AUTHORIZATION, SOCIAL_PROFILE) (USER_ID);;


alter table QName(AUTHORIZATION, SOCIAL_PROFILE) add constraint USER_SOCIAL_PROFILE_FK
	foreign key(USER_ID)
	references QName(AUTHORIZATION, USER)(ID);;


