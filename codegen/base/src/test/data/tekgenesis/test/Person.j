package tekgenesis.test;

import org.jetbrains.annotations.Nullable;

public class Person
{

    //~ Fields ...................................................................................................................

    private final int code;

    @Nullable private String name;

    //~ Constructors .............................................................................................................

    public Person() { }

    public Person(int code, @Nullable String name) {
        this.code = code;
        this.name = name;
    }

    //~ Getters ..................................................................................................................

    /** Returns the Code. */
    public int getCode() { return code; }

    /** Returns the Name. */
    @Nullable public String getName() { return name; }

    //~ Setters ..................................................................................................................

    /** Sets the value of the Name. */
    public void setName(@Nullable String name) { this.name = name; }

}
