create index IndexName(SG, DELETED_TS_INDEX)
    on TableName(SG, DELETED_INSTANCES)(ENTITY, TS) IndexTableSpace;;
