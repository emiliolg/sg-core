package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: MailFieldShowcaseForm */
public class MailFieldShowcaseForm
    extends MailFieldShowcaseFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when mail_field(mailChange) value ui changes */
    @Override @NotNull public Action changedEmail() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class MailTableRow
        extends MailTableRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when mail_field(tableChange) value changes */
        @Override @NotNull public Action tableMailChange() { throw new IllegalStateException("To be implemented"); }

    }

    public class UiTableRow
        extends UiTableRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when text_field(name) value ui changes */
        @Override @NotNull public Action changedNameUpdateNick() { throw new IllegalStateException("To be implemented"); }

    }
}
