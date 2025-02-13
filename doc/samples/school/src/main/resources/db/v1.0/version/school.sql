-- SQL for Schema SCHOOL --

-- if NeedsCreateSequence

create sequence QName(SCHOOL, COURSE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SCHOOL, COURSE_HOME_WORK_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SCHOOL, HOME_WORK_GRADE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SCHOOL, TEST_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

create sequence QName(SCHOOL, TEST_GRADE_SEQ)
	start with SequenceStartValue(1) increment by 1 SequenceCache;;

-- end

create table QName(SCHOOL, COURSE) (
	ID                                Serial(1,COURSE_SEQ)                      not null,
	GRADE                             int              default 0                not null,
	DIVISION                          nvarchar(255)    default EmptyString      not null,
	DESCRIPTION                       nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_COURSE              primary key (ID)
);;

create table QName(SCHOOL, COURSE_HOME_WORK) (
	ID                                Serial(1,COURSE_HOME_WORK_SEQ)            not null,
	COURSE_ID                         int                                       not null,
	YEAR                              int              default 0                not null,
	GIVEN_DATE                        datetime(0)      default CurrentTime      not null,
	DUE_DATE                          datetime(0)      default CurrentTime      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_COURSE_HOME_WORK    primary key (ID)
);;

create table QName(SCHOOL, FAMILY) (
	FAMILY_ID                         int              default 0                not null,
	FAMILY_NAME                       nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_FAMILY              primary key (FAMILY_ID)
);;

create table QName(SCHOOL, HOME_WORK_DATA) (
	COURSE_HOME_WORK_ID               int                                       not null,
	SEQ_ID                            int              default 0                not null,
	FILE_REFERENCE                    nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_HOME_WORK_DATA      primary key (COURSE_HOME_WORK_ID,SEQ_ID)
);;

create table QName(SCHOOL, HOME_WORK_GRADE) (
	ID                                Serial(1,HOME_WORK_GRADE_SEQ)             not null,
	HOME_WORK_ID                      int                                       not null,
	HOME_WORK_STUDENT_ID              nvarchar(255)                             not null,
	HOME_WORK_GRADE                   int              default 0                not null,
	HOME_WORK_GRADE_BASE              int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_HOME_WORK_GRADE     primary key (ID)
);;

create table QName(SCHOOL, STUDENT) (
	FAMILY_FAMILY_ID                  int                                       not null,
	ID                                nvarchar(255)    default EmptyString      not null,
	NAMES                             nvarchar(255)    default EmptyString      not null,
	LAST_NAMES                        nvarchar(255)    default EmptyString      not null,
	SEX                               nvarchar(50)     default 'Female'         not null,
	START_YEAR                        int              default 0                not null,
	BIRTHDAY                          date             default CurrentDate      not null,
	CURRENT_COURSE_ID                 int                                       not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_STUDENT             primary key (ID)
);;

create table QName(SCHOOL, TEST) (
	ID                                Serial(1,TEST_SEQ)                        not null,
	COURSE_ID                         int                                       not null,
	YEAR                              int              default 0                not null,
	DATE                              datetime(0)      default CurrentTime      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TEST                primary key (ID)
);;

create table QName(SCHOOL, TEST_DATA) (
	TEST_ID                           int                                       not null,
	SEQ_ID                            int              default 0                not null,
	FILE_REFERENCE                    nvarchar(255)    default EmptyString      not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TEST_DATA           primary key (TEST_ID,SEQ_ID)
);;

create table QName(SCHOOL, TEST_GRADE) (
	ID                                Serial(1,TEST_GRADE_SEQ)                  not null,
	TEST_ID                           int                                       not null,
	STUDENT_ID                        nvarchar(255)                             not null,
	GRADE                             int              default 0                not null,
	GRADE_BASE                        int              default 0                not null,
	UPDATE_TIME                       datetime(3)      default CurrentTime      not null,

	constraint PK_TEST_GRADE          primary key (ID)
);;

create table QName(SCHOOL, _METADATA) (
	VERSION                           nvarchar(24)                              not null,
	SHA                               nvarchar(128)                             not null,
	SHA_OVL                           nvarchar(128),
	UPDATE_TIME                       datetime(0),
	SCHEMA                            clob,
	OVERLAY                           clob,

	constraint PK_METADATA            primary key (VERSION)
);;

alter table QName(SCHOOL, COURSE_HOME_WORK) add constraint COURSE_COURSE_HOME_WORK_FK
	foreign key (COURSE_ID)
	references QName(SCHOOL, COURSE) (ID);;

alter table QName(SCHOOL, HOME_WORK_DATA) add constraint COURSE_HOME_WORK_HOM_980834_FK
	foreign key (COURSE_HOME_WORK_ID)
	references QName(SCHOOL, COURSE_HOME_WORK) (ID);;

alter table QName(SCHOOL, HOME_WORK_GRADE) add constraint HOME_WORK_HOME_WORK_GRADE_FK
	foreign key (HOME_WORK_ID)
	references QName(SCHOOL, COURSE_HOME_WORK) (ID);;

alter table QName(SCHOOL, HOME_WORK_GRADE) add constraint HOME_WORK_STUDENT_HO_5460AD_FK
	foreign key (HOME_WORK_STUDENT_ID)
	references QName(SCHOOL, STUDENT) (ID);;

alter table QName(SCHOOL, STUDENT) add constraint FAMILY_STUDENT_FK
	foreign key (FAMILY_FAMILY_ID)
	references QName(SCHOOL, FAMILY) (FAMILY_ID);;

alter table QName(SCHOOL, STUDENT) add constraint CURRENT_COURSE_STUDENT_FK
	foreign key (CURRENT_COURSE_ID)
	references QName(SCHOOL, COURSE) (ID);;

alter table QName(SCHOOL, TEST) add constraint COURSE_TEST_FK
	foreign key (COURSE_ID)
	references QName(SCHOOL, COURSE) (ID);;

alter table QName(SCHOOL, TEST_DATA) add constraint TEST_TEST_DATA_FK
	foreign key (TEST_ID)
	references QName(SCHOOL, TEST) (ID);;

alter table QName(SCHOOL, TEST_GRADE) add constraint TEST_TEST_GRADE_FK
	foreign key (TEST_ID)
	references QName(SCHOOL, TEST) (ID);;

alter table QName(SCHOOL, TEST_GRADE) add constraint STUDENT_TEST_GRADE_FK
	foreign key (STUDENT_ID)
	references QName(SCHOOL, STUDENT) (ID);;

-- if NeedsSerialComment
comment on column QName(SCHOOL,COURSE).ID                  is 'Serial(1,COURSE_SEQ)';;
comment on column QName(SCHOOL,COURSE_HOME_WORK).ID        is 'Serial(1,COURSE_HOME_WORK_SEQ)';;
comment on column QName(SCHOOL,HOME_WORK_GRADE).ID         is 'Serial(1,HOME_WORK_GRADE_SEQ)';;
comment on column QName(SCHOOL,TEST).ID                    is 'Serial(1,TEST_SEQ)';;
comment on column QName(SCHOOL,TEST_GRADE).ID              is 'Serial(1,TEST_GRADE_SEQ)';;
-- end

