
# Expressions

Sui Generis makes thoroughly use of expressions to allow declarative programming. They use the standard arithmetic ( +, -, *, / ) , relational ( >, < , = , <=, >= , != ) and logical operators ( &&, ||, ! ) plus predefined functions (e.g. substring , today, capitalize).


<!--
## Form Expressions
| Expression      | Return type  | Parameter type          | 
|     ---      |     ---      |       ---               |  
| forbidden      | Boolean      | Permission              | 
| isUpdate      | Boolean      |                         | 

###forbidden
Returns true or false depending if the user accessing the form has the privileges required.
**Example** 
Disable button when you don't have permission "create"
 
```mm
button(add_row), disable when forbidden(create);
```
###isUpdate
Returns true if the form is on update state or false when is on create.
**Example**
Hides userName when form is in update 

```mm
userName, hide when isUpdate();
```


## String Expressions

| Expression      Return type  | Parameter type          | Example |
|       ---                                                                                                                                                                      |     ---      |       ---               |   ---   |
| substring       | String       | String, Integer, Integer| // Set as default username the first letter of firstName + lastName <br/> `userName, default substring(firstName,0,1) + lastName;` |
| substr          | String       | String, Integer, Integer| // Set as default username the first letter of firstName + lastName <br/> `userName, default substr(firstName,0,1) + lastName;` |
| trim            | String       | String                  | `id, text_field, check length(trim(id)) == length(id) : "User id cannot have leading and/or trailing spaces.";` |
| stripAccents    | String       | String                  | // Set as default nickname firstName + lastName and cleans the nickname of letter accents and other diacritical marks <br/> `nickname "Nickname" : nickname, default stripAccents(firstName + "" + lastName)` |
| length          | Integer      | String                  | // Checks that password isn't empty <br/> `password : password_field, check length(password) > 0 : "Password cannot be empty!";` |
| indexOf         | Integer      | String, String          | // Checks that the mail contains the @ char <br/> `mail : mail_field, check (indexOf(mail, "@") > 0 : "email must contain @");` |
| matches         | Boolean      | String, String          | // Checks that the license plate matches with a pattern of 3 digits, space, 3 digits <br/> `lPlate : String, check (matches(patent, "... ...") : "patent must contain 3 digit a space and another 3 digit");` |
| repeat          | String       | Integer, String         | // Duplicates one time the password field in order to check that the both fields are equals <br/> `repeatPassword : password_field, check (repeat(1, password) == password + "" + repeatPassword : "Passwords fields aren't equals");` |
| replace         | String       | String, String, String  | // Replace every '.' in field dni for an empty string <br/> `dniWithoutDots: String, is replace(dni, "\\W", "");` |
| toUpperCase     | String       | String                  | // Sets the value of fullName field with firstName and lastName all in upperCase <br/> `fullName: String, default toUpperCase(firstName + "" + lastName)` |
| toLowerCase     | String       | String                  | // Sets the value of fullName field with firstName and lastName all in lowerCase <br/> `fullName: String, default toLowerCase(firstName + "" + lastName)` |


###substring       
Returns a string that is a substring of this string. The substring begins at the specified index and extends to the character at index.                       
**Example**
Set as default username the first letter of firstName + lastName 
```mm
userName, default substring(firstName,0,1) + lastName;
``` 
###substr          
Returns a string that is a substring of this string. The substring begins at the specified index and extends to the amount of characters defined.             
 **Example**
Set as default username the first letter of firstName + lastName  
```mm
userName, default substr(firstName,0,1) + lastName;
``` 
###trim            
Returns a string whose value is this string, with any leading and trailing white space removed, or this string if it has no leading or trailing white space.  
`id, text_field, check length(trim(id)) == length(id) : "User id cannot have leading and/or trailing spaces.";
``` 
###stripAccents    
Returns a string whose letter accents and other diacritical marks has been stripped.                                                                          
**Example**
Set as default nickname firstName + lastName and cleans the nickname of letter accents and other diacritical marks 
```mm
nickname "Nickname" : nickname, default stripAccents(firstName + "" + lastName)
 ```
###length          
Returns an integer that represents the length of this string.                                                                                                 
**Example**
Checks that password isn't empty  
```mm
password : password_field, check length(password) > 0 : "Password cannot be empty!";
``` 
###indexOf         
Returns the index within the string of the first occurrence of the specified substring.                                                                       
**Example**
Checks that the mail contains the @ char  
```mm
mail : mail_field, check (indexOf(mail, "@") > 0 : "email must contain @");
``` 
###matches         
Returns true if, and only if, the string matches the given regular expression.                                                                                
**Example**
Checks that the license plate matches with a pattern of 3 digits, space, 3 digits  
```mm
lPlate : String, check (matches(patent, "... ...") : "patent must contain 3 digit a space and another 3 digit");
```
###repeat          
Replaces each substring of this string that matches the given regular expression with the given replacement.                                                  
**Example**
Duplicates one time the password field in order to check that the both fields are equals  
```mm
repeatPassword : password_field, check (repeat(1, password) == password + "" + repeatPassword : "Passwords fields aren't equals");
```
###replace         
Returns a string that each substring of the given string that matches the given regular expression has been replaced with the given replacement.              
**Example**
Replace every '.' in field dni for an empty string  
```mm
dniWithoutDots: String, is replace(dni, "\\W", "");
``` 
###toUpperCase     
Returns a string with all of the characters converted to upper case.                                                                                          
**Example**
Sets the value of fullName field with firstName and lastName all in upperCase 
```mm
fullName: String, default toUpperCase(firstName + "" + lastName)
``` 
###toLowerCase     
Returns a string with all of the characters converted to lower case.                                                                                          
**Example**
Sets the value of fullName field with firstName and lastName all in lowerCase  
```mm
fullName: String, default toLowerCase(firstName + "" + lastName)
``` 

## Date Expressions

| Expression      | Description                                                                                                                                                                                                               | Return type  | Parameter type          | Example |
|       ---       |     ---                                                                                                                                                                                                                   |     ---      |       ---               |   ---   |
| today           | Returns a Date that represents only the date of today in milliseconds.                                                                                                                                                    | Date         |                         | // dueDate check <br/> `dueDate, check dueDate > today() : "Invalid due date";` |
| now             | Returns a DateTime that represents the current instance of time with date and time.                                                                                                                                       | DateTime     |                         | // Sets timeFrom default value to the current instance of time <br/> `timeFrom "From" : timeFrom, optional, default now()` |
| firstDayOfMonth | Returns a Date that represents the first day of a month.                                                                                                                                                                  | Date         | Date                    | // Sets date default value to the first day of this current month <br/> `date : Date, default firstDayOfMonth(today());` |
| lastDayOfMonth  | Returns a Date that represents the last day of a month.                                                                                                                                                                   | Date         | Date                    | // Sets date default value to the last day of this current month <br/> `date : Date, default lastDayOfMonth(today());` |

## Math Expressions
| Expression      | Description                                                                                                                                                                                                               | Return type  | Parameter type          | Example |
|       ---       |     ---                                                                                                                                                                                                                   |     ---      |       ---               |   ---   |
| sqrt            | Returns the correctly rounded positive square root of a value.                                                                                                                                                            | Real         | Real                    | // Calculates de hypotenuse of a triangle using sqrt and pow expressions <br/> `h : Real, is sqrt((pow(a, exp) + pow(b, exp)));` |
| pow             | Returns the value of the first argument raised to the power of the second argument.                                                                                                                                       | Real         | Real, Real              | // Calculates de hypotenuse of a triangle using sqrt and pow expressions <br/> `h : Real, is sqrt((pow(a, exp) + pow(b, exp)));` |
| scale           | Returns a decimal whose scale is the specified value, and whose unscaled value is determined by multiplying or dividing this BigDecimal's unscaled value by the appropriate power of ten to maintain its overall value.   | Decimal      | Decimal, Integer        | // Sets area with 3 digits behind the comma <br/> `area: Decimal, is scale(m2, 3);` |




## Form Expressions

| Expression      | Description                                                                                                                                                                                                               | Return type  | Parameter type          | Example |
|       ---       |     ---                                                                                                                                                                                                                   |     ---      |       ---               |   ---   |
| forbidden       | Returns true or false depending if the user accessing the form has the privileges required.                                                                                                                               | Boolean      | Permission              |  Disable button when you don't have permission "create" <br/> ```mm button(add_row), disable when forbidden(create);``` |
| isUpdate        | Returns true if the form is on update state or false when is on create.                                                                                                                                                   | Boolean      |                         | // Hides userName when form is in update <br/> `userName, hide when isUpdate();` |

## String Expressions

| Expression      | Description                                                                                                                                                                                                               | Return type  | Parameter type          | Example |
|       ---       |     ---                                                                                                                                                                                                                   |     ---      |       ---               |   ---   |
| substring       | Returns a string that is a substring of this string. The substring begins at the specified index and extends to the character at index.                                                                                   | String       | String, Integer, Integer| // Set as default username the first letter of firstName + lastName <br/> `userName, default substring(firstName,0,1) + lastName;` |
| substr          | Returns a string that is a substring of this string. The substring begins at the specified index and extends to the amount of characters defined.                                                                         | String       | String, Integer, Integer| // Set as default username the first letter of firstName + lastName <br/> `userName, default substr(firstName,0,1) + lastName;` |
| trim            | Returns a string whose value is this string, with any leading and trailing white space removed, or this string if it has no leading or trailing white space.                                                              | String       | String                  | `id, text_field, check length(trim(id)) == length(id) : "User id cannot have leading and/or trailing spaces.";` |
| stripAccents    | Returns a string whose letter accents and other diacritical marks has been stripped.                                                                                                                                      | String       | String                  | // Set as default nickname firstName + lastName and cleans the nickname of letter accents and other diacritical marks <br/> `nickname "Nickname" : nickname, default stripAccents(firstName + "" + lastName)` |
| length          | Returns an integer that represents the length of this string.                                                                                                                                                             | Integer      | String                  | // Checks that password isn't empty <br/> `password : password_field, check length(password) > 0 : "Password cannot be empty!";` |
| indexOf         | Returns the index within the string of the first occurrence of the specified substring.                                                                                                                                   | Integer      | String, String          | // Checks that the mail contains the @ char <br/> `mail : mail_field, check (indexOf(mail, "@") > 0 : "email must contain @");` |
| matches         | Returns true if, and only if, the string matches the given regular expression.                                                                                                                                            | Boolean      | String, String          | // Checks that the license plate matches with a pattern of 3 digits, space, 3 digits <br/> `lPlate : String, check (matches(patent, "... ...") : "patent must contain 3 digit a space and another 3 digit");` |
| repeat          | Replaces each substring of this string that matches the given regular expression with the given replacement.                                                                                                              | String       | Integer, String         | // Duplicates one time the password field in order to check that the both fields are equals <br/> `repeatPassword : password_field, check (repeat(1, password) == password + "" + repeatPassword : "Passwords fields aren't equals");` |
| replace         | Returns a string that each substring of the given string that matches the given regular expression has been replaced with the given replacement.                                                                          | String       | String, String, String  | // Replace every '.' in field dni for an empty string <br/> `dniWithoutDots: String, is replace(dni, "\\W", "");` |
| toUpperCase     | Returns a string with all of the characters converted to upper case.                                                                                                                                                      | String       | String                  | // Sets the value of fullName field with firstName and lastName all in upperCase <br/> `fullName: String, default toUpperCase(firstName + "" + lastName)` |
| toLowerCase     | Returns a string with all of the characters converted to lower case.                                                                                                                                                      | String       | String                  | // Sets the value of fullName field with firstName and lastName all in lowerCase <br/> `fullName: String, default toLowerCase(firstName + "" + lastName)` |

## Date Expressions

| Expression      | Description                                                                                                                                                                                                               | Return type  | Parameter type          | Example |
|       ---       |     ---                                                                                                                                                                                                                   |     ---      |       ---               |   ---   |
| today           | Returns a Date that represents only the date of today in milliseconds.                                                                                                                                                    | Date         |                         | // dueDate check <br/> `dueDate, check dueDate > today() : "Invalid due date";` |
| now             | Returns a DateTime that represents the current instance of time with date and time.                                                                                                                                       | DateTime     |                         | // Sets timeFrom default value to the current instance of time <br/> `timeFrom "From" : timeFrom, optional, default now()` |
| firstDayOfMonth | Returns a Date that represents the first day of a month.                                                                                                                                                                  | Date         | Date                    | // Sets date default value to the first day of this current month <br/> `date : Date, default firstDayOfMonth(today());` |
| lastDayOfMonth  | Returns a Date that represents the last day of a month.                                                                                                                                                                   | Date         | Date                    | // Sets date default value to the last day of this current month <br/> `date : Date, default lastDayOfMonth(today());` |

## Math Expressions
| Expression      | Description                                                                                                                                                                                                               | Return type  | Parameter type          | Example |
|       ---       |     ---                                                                                                                                                                                                                   |     ---      |       ---               |   ---   |
| sqrt            | Returns the correctly rounded positive square root of a value.                                                                                                                                                            | Real         | Real                    | // Calculates de hypotenuse of a triangle using sqrt and pow expressions <br/> `h : Real, is sqrt((pow(a, exp) + pow(b, exp)));` |
| pow             | Returns the value of the first argument raised to the power of the second argument.                                                                                                                                       | Real         | Real, Real              | // Calculates de hypotenuse of a triangle using sqrt and pow expressions <br/> `h : Real, is sqrt((pow(a, exp) + pow(b, exp)));` |
| scale           | Returns a decimal whose scale is the specified value, and whose unscaled value is determined by multiplying or dividing this BigDecimal's unscaled value by the appropriate power of ten to maintain its overall value.   | Decimal      | Decimal, Integer        | // Sets area with 3 digits behind the comma <br/> `area: Decimal, is scale(m2, 3);` |

-->

## Form Expressions

| Expression      | Description                                                                                                                                                                                                               | Return type  | Parameter type          | 
|       ---       |     ---                                                                                                                                                                                                                   |     ---      |       ---               | 
| **forbidden**       | Returns true or false depending if the user accessing the form has the privileges required.                                                                                                                               | Boolean      | Permission              | 
| **isUpdate**        | Returns true if the form is on update state or false when is on create.                                                                                                                                                   | Boolean      |                         | 

####Examples
**forbidden**
Disable button when you don't have permission "create" 

```mm 
button(add_row), disable when forbidden(create);
```                         
**isUpdate**
Hides userName when form is in update 

```mm 
userName, hide when isUpdate();
```                         

## String Expressions

| Expression          | Description                                                                                                                                                                                                               | Return type  | Parameter type          | 
|       ---           |     ---                                                                                                                                                                                                                   |     ---      |       ---               | 
| **substring**       | Returns a string that is a substring of this string. The substring begins at the specified index and extends to the character at index.                                                                                   | String       | String, Integer, Integer| 
| **substr**          | Returns a string that is a substring of this string. The substring begins at the specified index and extends to the amount of characters defined.                                                                         | String       | String, Integer, Integer| 
| **trim**            | Returns a string whose value is this string, with any leading and trailing white space removed, or this string if it has no leading or trailing white space.                                                              | String       | String                  | 
| **stripAccents**    | Returns a string whose letter accents and other diacritical marks has been stripped.                                                                                                                                      | String       | String                  | 
| **length**          | Returns an integer that represents the length of this string.                                                                                                                                                             | Integer      | String                  | 
| **indexOf**         | Returns the index within the string of the first occurrence of the specified substring.                                                                                                                                   | Integer      | String, String          | 
| **matches**         | Returns true if, and only if, the string matches the given regular expression.                                                                                                                                            | Boolean      | String, String          | 
| **repeat**          | Replaces each substring of this string that matches the given regular expression with the given replacement.                                                                                                              | String       | Integer, String         | 
| **replace**         | Returns a string that each substring of the given string that matches the given regular expression has been replaced with the given replacement.                                                                          | String       | String, String, String  | 
| **toUpperCase**     | Returns a string with all of the characters converted to upper case.                                                                                                                                                      | String       | String                  | 
| **toLowerCase**     | Returns a string with all of the characters converted to lower case.                                                                                                                                                      | String       | String                  | 

####Examples

**substring**
Set as default username the first letter of firstName + lastName 

```mm 
userName, default substring(firstName,0,1) + lastName;
``` 
 
**substr**          
Set as default username the first letter of firstName + lastName 

```mm 
userName, default substr(firstName,0,1) + lastName;
``` 

**trim**            

```mm 
id, text_field, check length(trim(id)) == length(id) : "User id cannot have leading and/or trailing spaces.";
```

**stripAccents**
Set as default nickname firstName + lastName and cleans the nickname of letter accents and other diacritical marks 

```mm 
nickname "Nickname" : nickname, default stripAccents(firstName + "" + lastName)
```

**length**          
Checks that password isn't empty

```mm 
password : password_field, check length(password) > 0 : "Password cannot be empty!";
``` 

**indexOf**         
Checks that the mail contains the @ char 

```mm 
mail : mail_field, check (indexOf(mail, "@") > 0 : "email must contain @");
```

**matches**         
Checks that the license plate matches with a pattern of 3 digits, space, 3 digits

```mm 
lPlate : String, check (matches(patent, "... ...") : "patent must contain 3 digit a space and another 3 digit");
``` 

**repeat**          
Duplicates one time the password field in order to check that the both fields are equals 

```mm 
repeatPassword : password_field, check (repeat(1, password) == password + "" + repeatPassword : "Passwords fields aren't equals");
``` 

**replace**         
Replace every '.' in field dni for an empty string 

```mm 
dniWithoutDots: String, is replace(dni, "\\W", "");
``` 

**toUpperCase**     
Sets the value of fullName field with firstName and lastName all in upperCase 

```mm 
fullName: String, default toUpperCase(firstName + "" + lastName)
```

**toLowerCase**     
Sets the value of fullName field with firstName and lastName all in lowerCase 

```mm 
fullName: String, default toLowerCase(firstName + "" + lastName)
```



## Date Expressions

| Expression      | Description                                                                                                                                                                                                               | Return type  | Parameter type             | 
|       ---       |     ---                                                                                                                                                                                                                   |     ---      |       ---                   |
| **today**           | Returns a Date that represents only the date of today in milliseconds.                                                                                                                                                    | Date         |                         |
| **now**             | Returns a DateTime that represents the current instance of time with date and time.                                                                                                                                       | DateTime     |                         |
| **firstDayOfMonth** | Returns a Date that represents the first day of a month.                                                                                                                                                                  | Date         | Date                    |
| **lastDayOfMonth**  | Returns a Date that represents the last day of a month.                                                                                                                                                                   | Date         | Date                    |

## Examples
**today**                
dueDate check 

```mm 
dueDate, check dueDate > today() : "Invalid due date";
``` 

**now**                  
Sets timeFrom default value to the current instance of time 

```mm 
timeFrom "From" : timeFrom, optional, default now()
```

**firstDayOfMonth**      
Sets date default value to the first day of this current month 

```mm 
date : Date, default firstDayOfMonth(today());
``` 

**lastDayOfMonth**       
Sets date default value to the last day of this current month 

```mm 
date : Date, default lastDayOfMonth(today());
``` 



## Math Expressions
| Expression      | Description                                                                                                                                                                                                               | Return type  | Parameter type              | 
|       ---       |     ---                                                                                                                                                                                                                   |     ---      |       ---                   | 
| **sqrt**            | Returns the correctly rounded positive square root of a value.                                                                                                                                                            | Real         | Real                    | 
| **pow**             | Returns the value of the first argument raised to the power of the second argument.                                                                                                                                       | Real         | Real, Real              | 
| **scale**           | Returns a decimal whose scale is the specified value, and whose unscaled value is determined by multiplying or dividing this BigDecimal's unscaled value by the appropriate power of ten to maintain its overall value.   | Decimal      | Decimal, Integer        | 


## Examples

**sqrt**            
Calculates de hypotenuse of a triangle using sqrt and pow expressions 

```mm 
h : Real, is sqrt((pow(a, exp) + pow(b, exp)));
``` 

**pow**             
Calculates de hypotenuse of a triangle using sqrt and pow expressions 

```mm 
h : Real, is sqrt((pow(a, exp) + pow(b, exp)));
```
**scale**           
Sets area with 3 digits behind the comma 

```mm 
area: Decimal, is scale(m2, 3);
```
