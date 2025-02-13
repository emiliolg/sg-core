

# Database API
The tekgenesis.database API (see [Sui Generis API Documentation](../../../javadoc/index.html)), can be used to query tekgenesis entities. 
the *sqlStatement* method can be used to create a statement that will later on be executed, over a database.
The query string that this method accepts should follow these conventions:
* Identifiers MUST be written in UPPERCASE
* Statements MUST be written in LOWERCASE.

*If these conventions are not followed, the identifiers or statements will not be correctly detected.* 
