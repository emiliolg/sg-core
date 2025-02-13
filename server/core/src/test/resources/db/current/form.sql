-- SQL for Schema FORM --

-- if NeedsCreateSequence

create sequence QName(FORM, AUTOINCREMENT_WITH_INNER_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(FORM, AUTOINCREMENT_WITH__768C03_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(FORM, BASE_WITH_AUTOINCREMENT_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(FORM, SOME_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(FORM, AUTOINCREMENT_WITH_INNER) (
	ID                                Serial(1,AUTOINCREMENT_WITH_INNER_SEQ)    not null,
	FIELD_A                           nvarchar(30)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_AUTOINCREMENT_WITH_INNER primary key (ID)
);;

create table QName(FORM, AUTOINCREMENT_WITH_MULTIPLE) (
	ID                                Serial(1,AUTOINCREMENT_WITH__768C03_SEQ)  not null,
	FIELD_D                           nvarchar(30)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_AUTOINCREMENT_WITH_MULTIPLE primary key (ID)
);;

create table QName(FORM, BASE_ENTITIES_PARTIAL_KEYS) (
	ENT_A_PK_A                        nvarchar(30)                              not null,
	ENT_A_PK_B                        nvarchar(30)                              not null,
	ENT_B_ID                          int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_BASE_ENTITIES_PARTIAL_KEYS primary key (ENT_A_PK_A,ENT_A_PK_B)
);;

create table QName(FORM, BASE_ENTITIES_WITH_AUT_INC_KEY) (
	ENT_A_PK_A                        nvarchar(30)                              not null,
	ENT_A_PK_B                        nvarchar(30)                              not null,
	ENT_B_ID                          int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_BASE_ENTITIES_WITH_AUT_INC_ primary key (ENT_A_PK_A,ENT_A_PK_B,ENT_B_ID)
);;

create table QName(FORM, BASE_ENTITIES_WITH_KEYS) (
	ENT_A_PK_A                        nvarchar(30)                              not null,
	ENT_A_PK_B                        nvarchar(30)                              not null,
	ENT_B_PK_A                        nvarchar(30)                              not null,
	ENT_B_PK_B                        nvarchar(30)                              not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_BASE_ENTITIES_WITH_KEYS primary key (ENT_A_PK_A,ENT_A_PK_B,ENT_B_PK_A,ENT_B_PK_B)
);;

create table QName(FORM, BASE_WITH_AUTOINCREMENT) (
	ID                                Serial(1,BASE_WITH_AUTOINCREMENT_SEQ)     not null,
	FIELD_C                           nvarchar(30)     default EmptyString      not null,
	WITH_AUTOINCREMENT_BACK_REF_ID    int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_BASE_WITH_AUTOINCREMENT primary key (ID)
);;

create table QName(FORM, BASE_WITH_COMPLEX_MULTIPLE) (
	FIELD_E                           nvarchar(30)     default EmptyString      not null,
	FIELD_F                           nvarchar(30)     default EmptyString      not null,
	FIELD_G                           nvarchar(30)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_BASE_WITH_COMPLEX_MULTIPLE primary key (FIELD_E,FIELD_F)
);;

create table QName(FORM, BASE_WITH_KEY) (
	PK_A                              nvarchar(30)     default EmptyString      not null,
	PK_B                              nvarchar(30)     default EmptyString      not null,
	FIELD_A                           nvarchar(30)     default EmptyString      not null,
	WITH_KEY_BACK_REF_ID              int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_BASE_WITH_KEY       primary key (PK_A,PK_B)
);;

create table QName(FORM, BASE_WITH_KEY_USING_REF) (
	PK_C                              nvarchar(30)     default EmptyString      not null,
	PK_D                              nvarchar(30)     default EmptyString      not null,
	FIELD_B                           nvarchar(30)     default EmptyString      not null,
	BACK_REF_ID                       int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_BASE_WITH_KEY_USING_REF primary key (PK_C,PK_D,BACK_REF_ID)
);;

create table QName(FORM, ENTITY_WITH_KEY_OF_SAME_TYPE) (
	FIELD_H_FIELD_E                   nvarchar(30)                              not null,
	FIELD_H_FIELD_F                   nvarchar(30)                              not null,
	FIELD_I                           int              default 0                not null,
	FIELD_J_FIELD_E                   nvarchar(30)                              not null,
	FIELD_J_FIELD_F                   nvarchar(30)                              not null,
	FIELD_K                           nvarchar(30)     default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_ENTITY_WITH_KEY_OF_SAME_TYP primary key (FIELD_H_FIELD_E,FIELD_H_FIELD_F,FIELD_I,FIELD_J_FIELD_E,FIELD_J_FIELD_F)
);;

create table QName(FORM, INNER_A) (
	AUTOINCREMENT_WITH_INNER_ID       int                                       not null,
	SEQ_ID                            int              default 0                not null,
	INNER_FIELD_A                     int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_INNER_A             primary key (AUTOINCREMENT_WITH_INNER_ID,SEQ_ID)
);;

create table QName(FORM, INSURANCE_COVERAGE) (
	CODE                              nvarchar(20)     default EmptyString      not null,
	NAME                              nvarchar(2000)   default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_INSURANCE_COVERAGE  primary key (CODE)
);;

create table QName(FORM, SOME) (
	ID                                Serial(1,SOME_SEQ)                        not null,
	SOME                              nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_SOME                primary key (ID)
);;

create table QName(FORM, TRANSACTIONAL_ENTITY) (
	KEY                               nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TRANSACTIONAL_ENTITY primary key (KEY)
);;

create table QName(FORM, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

alter table QName(FORM, BASE_ENTITIES_PARTIAL_KEYS) add constraint ENT_A_BASE_ENTITIES__16AAA7_FK
	foreign key (ENT_A_PK_A, ENT_A_PK_B)
	references QName(FORM, BASE_WITH_KEY) (PK_A, PK_B);;

alter table QName(FORM, BASE_ENTITIES_PARTIAL_KEYS) add constraint ENT_B_BASE_ENTITIES__FC1FDB_FK
	foreign key (ENT_B_ID)
	references QName(FORM, BASE_WITH_AUTOINCREMENT) (ID);;

alter table QName(FORM, BASE_ENTITIES_WITH_AUT_INC_KEY) add constraint ENT_A_BASE_ENTITIES__A9F577_FK
	foreign key (ENT_A_PK_A, ENT_A_PK_B)
	references QName(FORM, BASE_WITH_KEY) (PK_A, PK_B);;

alter table QName(FORM, BASE_ENTITIES_WITH_AUT_INC_KEY) add constraint ENT_B_BASE_ENTITIES__EFF98E_FK
	foreign key (ENT_B_ID)
	references QName(FORM, BASE_WITH_AUTOINCREMENT) (ID);;

alter table QName(FORM, BASE_ENTITIES_WITH_KEYS) add constraint ENT_A_BASE_ENTITIES__937D6D_FK
	foreign key (ENT_A_PK_A, ENT_A_PK_B)
	references QName(FORM, BASE_WITH_KEY) (PK_A, PK_B);;

alter table QName(FORM, BASE_ENTITIES_WITH_KEYS) add constraint ENT_B_BASE_ENTITIES__2A3C96_FK
	foreign key (ENT_B_PK_A, ENT_B_PK_B)
	references QName(FORM, BASE_WITH_KEY) (PK_A, PK_B);;

alter table QName(FORM, BASE_WITH_AUTOINCREMENT) add constraint WITH_AUTOINCREMENT_B_F76FDB_FK
	foreign key (WITH_AUTOINCREMENT_BACK_REF_ID)
	references QName(FORM, AUTOINCREMENT_WITH_MULTIPLE) (ID);;

alter table QName(FORM, BASE_WITH_KEY) add constraint WITH_KEY_BACK_REF_BA_55ACAA_FK
	foreign key (WITH_KEY_BACK_REF_ID)
	references QName(FORM, AUTOINCREMENT_WITH_MULTIPLE) (ID);;

alter table QName(FORM, BASE_WITH_KEY_USING_REF) add constraint BACK_REF_BASE_WITH_K_C8351C_FK
	foreign key (BACK_REF_ID)
	references QName(FORM, AUTOINCREMENT_WITH_MULTIPLE) (ID);;

alter table QName(FORM, ENTITY_WITH_KEY_OF_SAME_TYPE) add constraint FIELD_H_ENTITY_WITH__229061_FK
	foreign key (FIELD_H_FIELD_E, FIELD_H_FIELD_F)
	references QName(FORM, BASE_WITH_COMPLEX_MULTIPLE) (FIELD_E, FIELD_F);;

alter table QName(FORM, ENTITY_WITH_KEY_OF_SAME_TYPE) add constraint FIELD_J_ENTITY_WITH__29190A_FK
	foreign key (FIELD_J_FIELD_E, FIELD_J_FIELD_F)
	references QName(FORM, BASE_WITH_COMPLEX_MULTIPLE) (FIELD_E, FIELD_F);;

alter table QName(FORM, INNER_A) add constraint AUTOINCREMENT_WITH_I_B8C673_FK
	foreign key (AUTOINCREMENT_WITH_INNER_ID)
	references QName(FORM, AUTOINCREMENT_WITH_INNER) (ID);;

-- if NeedsSerialComment
comment on column QName(FORM,AUTOINCREMENT_WITH_INNER).ID  is 'Serial(1,AUTOINCREMENT_WITH_INNER_SEQ)';;
comment on column QName(FORM,AUTOINCREMENT_WITH_MULTIPLE).ID is 'Serial(1,AUTOINCREMENT_WITH__768C03_SEQ)';;
comment on column QName(FORM,BASE_WITH_AUTOINCREMENT).ID   is 'Serial(1,BASE_WITH_AUTOINCREMENT_SEQ)';;
comment on column QName(FORM,SOME).ID                      is 'Serial(1,SOME_SEQ)';;
-- end

