package tekgenesis.showcase;

import tekgenesis.form.Action;
import org.jetbrains.annotations.NotNull;

/** User class for form: UploadForm */
public class UploadForm
    extends UploadFormBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void cropConfig() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(butt) is clicked */
    @Override @NotNull public Action resetMe() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(fillButt) is clicked */
    @Override @NotNull public Action fillGallery() { throw new IllegalStateException("To be implemented"); }

}
