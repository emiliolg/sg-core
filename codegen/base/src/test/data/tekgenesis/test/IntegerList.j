package tekgenesis.test;

import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IntegerList
    extends ArrayList<Integer>
{

    //~ Constructors .............................................................................................................

    public IntegerList(@Nullable Integer size) { super(size); }

    public IntegerList(int name) { }

    //~ Methods ..................................................................................................................

    @NotNull public Integer get(int n) { return super.get(n); }

}
