package models;

import java.io.File;
import tekgenesis.persistence.etl.Importer;
import tekgenesis.task.ImporterTask;
import tekgenesis.task.ImporterTaskInstance;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import java.util.regex.Pattern;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.task.TransactionMode;

/** 
 * Generated base class for task: ImportTask.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings("WeakerAccess")
public abstract class ImportTaskBase
    extends ImporterTaskInstance
    implements Importer, LoggableInstance
{

    //~ Constructors .............................................................................................................

    protected ImportTaskBase(@NotNull ImporterTask task) { super(task); }

    //~ Methods ..................................................................................................................

    @Override public boolean accepts(@NotNull File file) { return FILTER.matcher(file.getPath()).matches(); }

    @Override public boolean accepts(@NotNull QName qname, @NotNull File file) { return FILTER.matcher(file.getPath()).matches(); }

    @Override @NotNull public TransactionMode getTransactionMode() { return TransactionMode.ALL; }

    @Override public int getBatchSize() { return 1; }

    @Override @NotNull public String getExclusionGroup() { return ""; }

    @Override public int getPurgePolicy() { return 15; }

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Pattern FILTER = Pattern.compile("*.csv");
    @NotNull private static final Logger logger = Logger.getLogger(ImportTask.class);

}
