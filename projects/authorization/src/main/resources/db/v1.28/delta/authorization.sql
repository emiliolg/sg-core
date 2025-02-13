update QName(AUTHORIZATION, USER) set DEPRECATION_TIME = null where DEPRECATION_USER is not null;;

alter  table QName(AUTHORIZATION, USER)
	AlterColumnType(DEPRECATION_TIME, datetime(3));;

	update QName(AUTHORIZATION, USER) set DEPRECATION_TIME = CurrentTime where DEPRECATION_USER is not null;;


