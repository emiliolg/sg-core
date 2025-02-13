package tekgenesis.test schema TaskTest;


task TestTask "New Test Task";

task SlowSqlTask "Slow Sql Test Task";

task WaitingTask "Waiting Test Task";

processor task InfiniteTask "Infitie Test Task";

task ErrorTask "Task wlays fail with an error";

processor task MyProcessorTask transaction each 10;

processor task MyProcessorAllTask transaction all;

processor task IsolatedTask transaction isolated;

processor task ExtendedProcessorAllTask transaction all;

processor task ExtendedProcessorIsolatedTask transaction isolated ;

entity Isolated {
    count : Int;
}
