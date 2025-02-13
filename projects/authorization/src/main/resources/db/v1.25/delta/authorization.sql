alter  table QName(AUTHORIZATION, APPLICATION)
	SetDefault(USER_ID, null);;

alter  table QName(AUTHORIZATION, APPLICATION_AUDIT)
	SetDefault(USER_ID, null);;

alter  table QName(AUTHORIZATION, DEVICE)
	SetDefault(USER_ID, null);;

alter  table QName(AUTHORIZATION, FAVORITE)
	SetDefault(USER_ID, null);;

alter  table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES)
	SetDefault(ORG_UNIT_NAME, null);;

alter  table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES)
	SetDefault(PROPERTY_ID, null);;

alter  table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES)
	SetDefault(PROPERTY_NAME, null);;

alter  table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES)
	SetDefault(PROPERTY_TYPE, null);;

alter  table QName(AUTHORIZATION, PERMISSIONS)
	SetDefault(ROLE_PERMISSION_ID, null);;

alter  table QName(AUTHORIZATION, ROLE_ASSIGNMENT)
	SetDefault(USER_ID, null);;

alter  table QName(AUTHORIZATION, ROLE_ASSIGNMENT)
	SetDefault(ROLE_ID, null);;

alter  table QName(AUTHORIZATION, ROLE_ASSIGNMENT)
	SetDefault(OU_NAME, null);;

alter  table QName(AUTHORIZATION, ROLE_PERMISSION)
	SetDefault(ROLE_ID, null);;

alter  table QName(AUTHORIZATION, USER_PROPERTIES)
	SetDefault(USER_ID, null);;

alter  table QName(AUTHORIZATION, USER_PROPERTIES)
	SetDefault(PROPERTY_ID, null);;

alter  table QName(AUTHORIZATION, USER_PROPERTIES)
	SetDefault(PROPERTY_NAME, null);;

alter  table QName(AUTHORIZATION, USER_PROPERTIES)
	SetDefault(PROPERTY_TYPE, null);;

