-- SQL for Schema CART --

create table QName(CART, CART) (
	ID                                Identity         not null,
	USER                              nvarchar(20)     not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CART                primary key (ID),
	constraint CART_USER_UNQT         unique      (USER)
);;

create table QName(CART, CART_ITEM) (
	CART_ID                           int              not null,
	SEQ_ID                            int              not null,
	PRODUCT_PRODUCT_ID                nvarchar(8)      not null,
	QUANTITY                          int              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime not null,

	constraint PK_CART_ITEM           primary key (CART_ID,SEQ_ID)
);;

create table QName(CART, _METADATA) (
	VERSION                           nvarchar(24)     not null,
	SHA                               nvarchar(128)    not null,
	SHA_OVL                           nvarchar(128),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

-- if NeedsCreateSequence

create sequence QName(CART, CART_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

alter table QName(CART, CART_ITEM) add constraint CART_CART_ITEM_FK
	foreign key (CART_ID)
	references QName(CART, CART) (ID);;

-- if NeedsGrantReference
grant references on QName(BASIC,PRODUCT) to SchemaOrUser(CART);;
-- end
alter table QName(CART, CART_ITEM) add constraint PRODUCT_CART_ITEM_FK
	foreign key (PRODUCT_PRODUCT_ID)
	references QName(BASIC, PRODUCT) (PRODUCT_ID);;

