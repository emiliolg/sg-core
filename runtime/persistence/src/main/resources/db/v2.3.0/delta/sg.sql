
alter table QName(SG, _METADATA) drop constraint PK__METADATA;;
alter table QName(SG, _METADATA) add constraint PK_METADATA            primary key (VERSION);;