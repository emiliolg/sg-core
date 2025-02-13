
# Types

Sui Generis supports several data types. These data types are mapped to the corresponding type in Java, Javascript, Expressions, or database column.
Some types have optional parameters to specify maximum length and precision. Java types used depend on whether the attributes defined are optional or not, if not optional, primitive types are preferred.

## Numbers
Sui Generis provides the following built-in types representing numbers (close to Java):
            
|Type|Description|Example|Generated Java Type|Generated&nbsp;Sql&nbsp;Type| 
|----|-----------|-------|-------------------|------------------------|
|Int|An unsigned integer value.<br/>It has an optional length parameter, if not specified 9 will be used. |Int| int<br/>long for length > 10<br/>java.lang.Integer and java.lang.Long for optional values | int<br/>bigint for length > 10<br/>number for oracle |
|Decimal|A decimal number representation.<br/>It has two optional parameters: decimals and precision.<br/>The first one defines the maximum number of digits including the decimals ones.<br/>The latter specifies the number of digits after the decimal point.<br/>If number of decimals is not specified 0 is used, if no precision is specified 16 will be used. |Decimal(10,2)| java.math.BigDecimal | decimal |
|Real|Represents a floating point number.|Real| double<br/>java.lang.Double for optional values | double precision |


##Booleans

|Type|Description|Example|Generated Java Type|Generated&nbsp;Sql&nbsp;Type| 
|----|-----------|-------|-------------------|------------------------|
|Boolean|A boolean value.|Boolean| bool<br/>java.lang.Boolean for optional values | boolean<br/>number(1) check in (0,1) for Oracle |

##Strings

|Type|Description|Example|Generated Java Type|Generated&nbsp;Sql&nbsp;Type| 
|----|-----------|-------|-------------------|------------------------|
|String|A string value.<br/>It has a optional length parameter. If not specified 255 will be used.|String(25)| java.lang.String | nvarchar<br/>clob for length > 2000<br/>nvarchar2 in Oracle |


## Dates

|Type|Description|Example|Generated Java Type|Generated&nbsp;Sql&nbsp;Type| 
|----|-----------|-------|-------------------|------------------------|
|DateTime|Represents an instant in time.<br/>It has one optional precision parameter. It represents precision as the number of decimal fractional seconds. (i.e. Precision 6 means microseconds).<br/>If not precision is specified 0 (seconds) will be used. |DateTime(3)|tekgenesis.common.core.DateTime|datetime<br/>timestamp in Oracle|
|Date|Represents a date in the calendar.|Date| tekgenesis.common.core.DateOnly | date |
