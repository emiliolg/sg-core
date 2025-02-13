alter  table QName(SG, CLUSTER_CONF)
	drop column INSTANCE_VERSION;;

alter  table QName(SG, CLUSTER_STATS)
	drop column INSTANCE_VERSION;;

alter  table QName(SG, HOST_ADDRESS)
	drop column INSTANCE_VERSION;;

alter  table QName(SG, NODE_ENTRY)
	drop column INSTANCE_VERSION;;

alter  table QName(SG, TASK_ENTRY)
	drop column INSTANCE_VERSION;;

alter  table QName(SG, TASK_EXECUTION_LOG)
	drop column INSTANCE_VERSION;;

