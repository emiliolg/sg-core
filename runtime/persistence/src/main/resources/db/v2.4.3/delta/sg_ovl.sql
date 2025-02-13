create table QName(SG, INDEX_LOCK) (
	INDEX_NAME                        nvarchar(255)    default EmptyString      not null,
	UPDATING_NODE                     nvarchar(255)    default EmptyString      not null,

	constraint PK_INDEX_LOCK          primary key (INDEX_NAME)
);;