#Transaction Handling

##Transaction usage in Forms
Form transactions are automatically created on data manipulation. And automatically submitted or rolled-back when form methods are invoked. As we have seen in [form's generated code](forms/forms.html#generated-code), interaction methods such as cancel, submit, onclick methods, on suggest, etc, return Actions. The transaction will be committed, unless the action result is an error, or an exception is thrown, in such case, the transaction will be rolled-back.

> Returning actions.error() **will** rollback the transaction.
 
##Transaction usage in Handles
As in forms, transactions managed automatically, and on each interaction transactions are committed, or rolled-back if an error is returned.

##Transaction usage in Tasks
It is always encouraged to use Processor Tasks, since it provides out of the box transaction handling options. If not possible other tasks also provide automatic transaction handling, by enclosing the code in a single transaction. This behavior can be overridden by the user, allowing him to manage the transactions.
###Handling transactions manually
If the code needs to be executed within a transaction, a transaction context can be enforced using runInTransaction() from  tekgenesis.transaction.Transaction. See [Sui Generis API Documentation](javadoc/index.html).
Using this method we can be sure that the code will be invoked inside a transaction; if code is already being executed in a transaction, nothing will be done, else a new transaction will bre created and committed afterwards. 

> When there are nested *runInTransaction* calls, only the root *runInTransaction* opens and commits a transaction, new transaction are only opened if code is not **in** a transaction.

##Running code after Transactions
Sui Generis framework also provides the ability to execute a piece of code after the transaction is committed or rolled back. 
Transaction.runAfter method provides such functionality. See [Sui Generis API Documentation](javadoc/index.html)
This gives the opportunity of storing other database information, regardless of the current transaction being rolled back, or logging information, etc.