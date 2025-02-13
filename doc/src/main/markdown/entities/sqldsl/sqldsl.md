# Querying Entities

## Sql Statements (DSL)

Sui Generis supports the creation and execution of Sql Statements ( *select*, *insert*, *update* and *delete*) through a *DSL* that tries to mimic SQL syntax.
For this to work, appropriate types, representing the *tables* and *columns* of the database must be generated. These will be automatically generated for the *entities* defined in the metamodel definition files.

###Example

Simple Example:
<table><tr><td>
  
```java
ImmutableList<Product> products = 
 selectFrom(PRODUCT)
  .join(CATEGORY, CATEGORY.ID.eq(PRODUCT.CATEGORY_ID)
  .where(CATEGORY.INTRODUCTION_DATE.ge(date(2010,1,1)))
  .orderBy(PRODUCT.ID)
  .list();
```

</td><td><p/> <pre>
SELECT FROM Product
JOIN Category ON CATEGORY.id=Product.categoryId
WHERE  Category.introductionDate>='20100101'
ORDER BY Product.id;

</pre><p/></td></tr></table>



The *selectFrom* method is a static method from the *tekgenesis.persistence.Sql*(see [Sui Generis API Documentation](../../../javadoc/index.html)) class.

## The Select Statement

The *select* statement is implemented in two forms: *selectFrom(TABLE)* and *select(expr..)*.
The first is used when you want to return all the columns from the specified table, while the latter will return only the specified columns.

### All columns Select (selectFrom)

The signature for *selectFrom* method is `<T> Select<T> selectFrom(TableLike<T> table)`.
Where `TableLike<T>` can be either `DbTable<I extends EntityInstance>`, that is the DbTable associated with a given instance, or another `Select<T>` in case you want to define a subquery.
For Example:

```java
// Returns a Select expression that will select all columns for all rows of the product table
Select<Product> = selectFrom(PRODUCT);

// The same in a more convoluted way. (The subquery makes no real sense here)

Select<Product> = selectFrom(selectFrom(PRODUCT))

```

#### Specifying the query

The *Select* object has a set of methods that allow you to build the different Sql clauses. That is:

* `where(Criteria...)` for example: `selectFrom(PRODUCT).where(PRODUCT.ID.ge(10))`
* `groupBy(Expr...)` for example: `selectFrom(PRODUCT).groupBy(PRODUCT.CATEGORY)`
* `having(Criteria...)` for example: `.....having(PRODUCT.CATEGORY.count().ge(10)`
* `orderBy(OrderSpec...)` for example: `.....orderBy(PRODUCT.PRICE.descending())`
* `join(TableLike, Criteria...)` or `leftOuterJoin(TableLike, Criteria...)` for example `.leftOuterJoin(CATEGORY, CATEGORY.ID.eq(PRODUCT.CATEGORY_ID)`
* `offset(long)`. Skip the specified number of rows.
* `limit(long)`. Only return the specified number of rows.
* `forUpdate()`
* `distinct()`

#### Operating with the query

Once the query is fully specified then you can operate with it using the following methods:

* `ImmutableList<T> list()`. Executes the query and returns a list of results.
* `@Nullable T get()`. Executes the query and returns the first result or `null`.
* `T getOrElse(T defaultValue)`. Executes the query and returns the first result or the specified defaultValue.
* `long count()`. Returns the number of rows that satisfies the query.
* `boolean exists()`. Returns true if there is at least one row that fulfils the condition.
* `forEach(Consumer<? super T> consumer)`. Executes the lambda for each row of the result.
* `forEach(Predicate<? super T> predicate)`. Executes the predicate for each row of the result, if the predicate returns `false`, it will stop the iteration.
* `String asSql()`. Returns an String representing the generated SqlStatement.

Besides, `Select<T>` implements `Iterable<T>` so you can use it in a 'for each' statement, for example:

####Iterating Results:
To check safe ways to iterate the results, not leaving database connections opened, see [recommended way to traverse entities](../iterating_entities.html) 
     
####Caching results.
 
By invoking the methods `cache(int seconds)` or `cache(int, TimeUnit)` You will instruct Sui Generis to cache the result of a query for the specified time duration.
In this way, if you execute the **same** query (where the same means having the exact final Sql statement) several times during the specified time, then the same result will be returned with no effective databases access.
 
 
### Specified Columns (Select)

When not all columns are expected as a result of the select statement, one of the `select` methods should be used.
Select method signatures are:

- `Select.Builder<QueryTuple> select(Expr<?> first, Expr<?>... rest)`
- ` <T> Select.Builder<T> select(Expr<T> expr)`.
 
Where `Expr<T>` can be a Table Column (like `PRODUCT.DESCRIPTION` from the above example), a column aggregation expression (`PRODUCT.QTY.sum or avg`), an operation between columns (`PRODUCT.QTY.add(PRODUCT.QTY_2)`) a rename (`PRODUCT.QTY.as("Quantity")`), a numeric operation such as floor (`PRODUCT.PRICE.floor()`) etc, see [Sui Generis API Documentation](../../javadoc/index.html) for further details.

A SelectBuilder is obtained in both cases,. 

The andAllOf method ,`Select.Builder<QueryTuple> andAllOf(TableLike<?> tableLike)`, can be applied to the select builder, providing the ability to add all of the table columns to the current statement, as `Table.*` would in sql. 

To complete the select statement the table from which to select should be specified, using the from method `Select<T> from(TableLike<?> tableLike)`
In all cases,`TableLike<T>` can be either `DbTable<I extends EntityInstance>`, or another `Select<T>` as specified before.

Simple Select Example:
<table><tr><td>
@inline-code(db/entity/src/test/java/tekgenesis/persistence/test/QueriesTest.java#select)
</td><td><p/> <pre>SELECT idKey, name, description
FROM Category
WHERE  idKey=1;</pre><p/></td></tr></table>

Example Using AndAllOf and aggregations for Columns
<table><tr><td>
@inline-code(db/entity/src/test/java/tekgenesis/persistence/test/QueriesTest.java#selectAndAllOf)
</td><td><p/> <pre>SELECT LEN(name), CATEGORY.*
FROM Category
WHERE  idKey=2;</pre><p/></td></tr></table>

Once the Select Object is obtained, the sql statement should be treated, as specified before


