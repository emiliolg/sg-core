alter  table QName(AUTHORIZATION, APPLICATION)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, APPLICATION_AUDIT)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, DEVICE)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, FAVORITE)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, INVALIDATED_SESSION)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, ORG_UNIT)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, PERMISSIONS)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, PROPERTY)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, ROLE)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, ROLE_ASSIGNMENT)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, ROLE_PERMISSION)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, SOCIAL_PROFILE)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, USER)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(AUTHORIZATION, USER_PROPERTIES)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

