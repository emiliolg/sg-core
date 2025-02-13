#Entity Life Cycle

Entities go through different stages during their Life Cycle.

1. Created In memmory

    BEFORE_DELETE, AFTER_DELETE, BEFORE_PERSIST, AFTER_PERSIST, BEFORE_INSERT, AFTER_INSERT, BEFORE_UPDATE, AFTER_UPDATE

## @Initialize method
Sui Generis provides as well an initialization method, when a method is marked with the @initialize annotation, it is ensured that this method will be invoked before that table is accessed.
