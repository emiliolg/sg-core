
# Remote Entities and Views
## Remote Entities

## Remote Views

Remote views are used to synchronize entities from one Sui Generis application to another one. In order to use remote views, a jar with the metamodels from the project to synchronize from, must be added to the classpath. The framework generates a task to make the synchronization with the other system. Like regular views, the user chooses which attributes to use from the original entity. In runtime configuration, the URL from the origin Sui Generis instance must be specified for the schema of the origin entity.

```
remote view Person "Remote person" of Person
{
	firstName : firstName;
	lastNane  : lastName;
	gender	  : gender;
}
```
