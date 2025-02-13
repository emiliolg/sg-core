package tekgenesis.test;

import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringList
    extends ArrayList<String>
{

    //~ Constructors .............................................................................................................

    public StringList(@Nullable Integer size) { super(size); }

    public StringList(@NotNull String name) { }

    //~ Methods ..................................................................................................................

    @NotNull public String get(int n) { return super.get(n); }

}
