# Views

Views are a restricted version of an entity. Views can be defined from another entity choosing which attributes to use. They can also change the name for the shown attributes.

```
view RestrictedPerson "Restricted person" of Person
{
	firstName : firstName;
	lastNane  : lastName;
	gender	  : gender;
}
 
entity Person "Person"
{
	firstName : String;
	lastName  : String;
	ssn		  : Int;
	gender	  : Gender;
	occupation: String;
}
```

# Sql views

Views can also be defined using a SQL query. In this case the type and number of the attributes must match with the ones from the query.

```
view PersonView "Person view"
    as """ select FIRST_NAME, LAST_NAME, GENDER from TableName(SCHEMA,PERSON) """
    of Person
    primary_key id
{
    id : Int;
    firstName : String;
    lastName  : String;
	gender	  : Gender;
}
```
