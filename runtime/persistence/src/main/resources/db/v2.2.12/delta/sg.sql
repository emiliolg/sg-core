-- Pretty SQL File --
create table TableName(SG, TASK_LOCK) (
    LOCK_NAME                  nvarchar(256)   not null,

    constraint PK_TASK_LOCK
        primary key(LOCK_NAME)
);;


