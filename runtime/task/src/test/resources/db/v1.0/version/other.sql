-- SQL for Schema OTHER --

create table QName(OTHER, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

-- if NeedsGrantReference

grant select on QName(VIEWS,E) to SchemaOrUser(OTHER) with grant option;;

-- end

create view QName(OTHER, EVIEW) (R_ID,DESC) as
	select R_ID, DESC
	from QName(VIEWS, E);;

CommentOnView  QName(OTHER, EVIEW) is 'select R_ID, DESC from  QName(VIEWS, E)';;

