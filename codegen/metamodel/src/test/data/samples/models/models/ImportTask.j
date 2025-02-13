package models;

import java.io.File;
import tekgenesis.task.ImporterTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.core.QName;

/** User class for Task: ImportTask */
public class ImportTask
    extends ImportTaskBase
{

    //~ Constructors .............................................................................................................

    private ImportTask(@NotNull ImporterTask task) { super(task); }

    //~ Methods ..................................................................................................................

    @Override public void process(@Nullable File file) { throw new IllegalStateException("to be implemented"); }

    @Override public void process(@Nullable QName qname, @Nullable File file) { throw new IllegalStateException("to be implemented"); }

}
