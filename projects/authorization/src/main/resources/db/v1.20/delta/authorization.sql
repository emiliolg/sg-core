alter  table QName(AUTHORIZATION, APPLICATION)
	SetDefault(ID, EmptyString);;

alter  table QName(AUTHORIZATION, APPLICATION)
	SetDefault(TOKEN, EmptyString);;

alter  table QName(AUTHORIZATION, APPLICATION)
	SetDefault(USER_ID, EmptyString);;

alter  table QName(AUTHORIZATION, APPLICATION_AUDIT)
	SetDefault(APPLICATION, EmptyString);;

alter  table QName(AUTHORIZATION, APPLICATION_AUDIT)
	SetDefault(USER_ID, EmptyString);;

alter  table QName(AUTHORIZATION, APPLICATION_AUDIT)
	SetDefault(YESTERDAY_EVENTS, 0);;

alter  table QName(AUTHORIZATION, APPLICATION_AUDIT)
	SetDefault(DAY_EVENTS, 0);;

alter  table QName(AUTHORIZATION, APPLICATION_AUDIT)
	SetDefault(LAST_WEEK_EVENTS, 0);;

alter  table QName(AUTHORIZATION, APPLICATION_AUDIT)
	SetDefault(WEEK_EVENTS, 0);;

alter  table QName(AUTHORIZATION, APPLICATION_AUDIT)
	SetDefault(LAST_MONTH_EVENTS, 0);;

alter  table QName(AUTHORIZATION, APPLICATION_AUDIT)
	SetDefault(MONTH_EVENTS, 0);;

alter  table QName(AUTHORIZATION, DEVICE)
	SetDefault(USER_ID, EmptyString);;

alter  table QName(AUTHORIZATION, DEVICE)
	SetDefault(SEQ_ID, 0);;

alter  table QName(AUTHORIZATION, DEVICE)
	SetDefault(NAME, EmptyString);;

alter  table QName(AUTHORIZATION, DEVICE)
	SetDefault(DEVICE_ID, EmptyString);;

alter  table QName(AUTHORIZATION, DEVICE)
	SetDefault(DISABLED, False);;

alter  table QName(AUTHORIZATION, ORG_UNIT)
	SetDefault(NAME, EmptyString);;

alter  table QName(AUTHORIZATION, ORG_UNIT)
	SetDefault(DESCRIPTION, EmptyString);;

alter  table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES)
	SetDefault(ORG_UNIT_NAME, EmptyString);;

alter  table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES)
	SetDefault(SEQ_ID, 0);;

alter  table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES)
	SetDefault(PROPERTY_ID, EmptyString);;

alter  table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES)
	SetDefault(PROPERTY_NAME, EmptyString);;

alter  table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES)
	SetDefault(PROPERTY_TYPE, 'STRING');;

alter  table QName(AUTHORIZATION, ORG_UNIT_PROPERTIES)
	SetDefault(VALUE, EmptyString);;

alter  table QName(AUTHORIZATION, PERMISSIONS)
	SetDefault(ROLE_PERMISSION_ID, 0);;

alter  table QName(AUTHORIZATION, PERMISSIONS)
	SetDefault(SEQ_ID, 0);;

alter  table QName(AUTHORIZATION, PERMISSIONS)
	SetDefault(PERMISSION, EmptyString);;

alter  table QName(AUTHORIZATION, PROPERTY)
	SetDefault(ID, EmptyString);;

alter  table QName(AUTHORIZATION, PROPERTY)
	SetDefault(NAME, EmptyString);;

alter  table QName(AUTHORIZATION, PROPERTY)
	SetDefault(TYPE, 'STRING');;

alter  table QName(AUTHORIZATION, PROPERTY)
	SetDefault(REQUIRED, False);;

alter  table QName(AUTHORIZATION, PROPERTY)
	SetDefault(SCOPE, 'COMPANY');;

alter  table QName(AUTHORIZATION, ROLE)
	SetDefault(ID, EmptyString);;

alter  table QName(AUTHORIZATION, ROLE)
	SetDefault(NAME, EmptyString);;

alter  table QName(AUTHORIZATION, ROLE_ASSIGNMENT)
	SetDefault(USER_ID, EmptyString);;

alter  table QName(AUTHORIZATION, ROLE_ASSIGNMENT)
	SetDefault(ROLE_ID, EmptyString);;

alter  table QName(AUTHORIZATION, ROLE_ASSIGNMENT)
	SetDefault(OU_NAME, EmptyString);;

alter  table QName(AUTHORIZATION, ROLE_PERMISSION)
	SetDefault(ROLE_ID, EmptyString);;

alter  table QName(AUTHORIZATION, ROLE_PERMISSION)
	SetDefault(DOMAIN, EmptyString);;

alter  table QName(AUTHORIZATION, ROLE_PERMISSION)
	SetDefault(APPLICATION, EmptyString);;

alter  table QName(AUTHORIZATION, USER)
	SetDefault(ID, EmptyString);;

alter  table QName(AUTHORIZATION, USER)
	SetDefault(NAME, EmptyString);;

alter  table QName(AUTHORIZATION, USER_PROPERTIES)
	SetDefault(USER_ID, EmptyString);;

alter  table QName(AUTHORIZATION, USER_PROPERTIES)
	SetDefault(SEQ_ID, 0);;

alter  table QName(AUTHORIZATION, USER_PROPERTIES)
	SetDefault(PROPERTY_ID, EmptyString);;

alter  table QName(AUTHORIZATION, USER_PROPERTIES)
	SetDefault(PROPERTY_NAME, EmptyString);;

alter  table QName(AUTHORIZATION, USER_PROPERTIES)
	SetDefault(PROPERTY_TYPE, 'STRING');;

alter  table QName(AUTHORIZATION, USER_PROPERTIES)
	SetDefault(VALUE, EmptyString);;

