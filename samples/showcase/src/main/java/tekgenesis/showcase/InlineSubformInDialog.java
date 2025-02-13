
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

/**
 * User class for Form: InlineSubformInDialog
 */
public class InlineSubformInDialog extends InlineSubformInDialogBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        getMergeReception().add().setReception("Reception1");
        getMergeReception().add().setReception("Reception2");
        getMergeReception().add().setReception("Reception3");
    }

    //~ Inner Classes ................................................................................................................................

    public class MergeReceptionRow extends MergeReceptionRowBase {}
}
