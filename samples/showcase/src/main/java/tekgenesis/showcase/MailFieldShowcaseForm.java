
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.form.Action;
import tekgenesis.form.Suggestion;

/**
 * User class for Form: MailFieldShowcaseForm
 */
public class MailFieldShowcaseForm extends MailFieldShowcaseFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull public Action changedEmail() {
        setMail(CHANGE_MAIL);
        return actions.getError().withMessage("Changed email");
    }

    @Override public void load() {
        getMailTable().add();
        final UiTableRow add = getUiTable().add();
        add.setMailUiOptions(ImmutableList.of(FACEBOOK, COMPUMUNDO, GARBARINO));
        setMailOptionsOptions(ImmutableList.of(GARBARINO, COMPUMUNDO, FACEBOOK));
    }
    /** Suggest sync mails. */
    @NotNull public Iterable<Suggestion> suggestSyncMails(String query) {
        return getSuggestions(ImmutableList.of(Suggestion.create(GARBARINO), Suggestion.create(COMPUMUNDO)), query);
    }

    //~ Methods ......................................................................................................................................

    /** Suggest mails. */
    @NotNull public static Iterable<Suggestion> suggestMails(String query) {
        return getSuggestions(ImmutableList.of(Suggestion.create(COMPUMUNDO), Suggestion.create(FACEBOOK)), query);
    }

    /** Suggest table mails. */
    @NotNull public static Iterable<Suggestion> tableMailSuggest(String query) {
        return getSuggestions(ImmutableList.of(Suggestion.create(FACEBOOK), Suggestion.create(COMPUMUNDO), Suggestion.create(GARBARINO)), query);
    }

    @NotNull private static Iterable<Suggestion> getSuggestions(ImmutableList<Suggestion> list, @Nullable String written) {
        if (written != null) {
            final int    i    = written.indexOf('@');
            final String subs = written.substring(i + 1);
            return list.filter(item -> item.getKey().startsWith(subs));
        }
        return list;
    }

    //~ Static Fields ................................................................................................................................

    private static final String GARBARINO   = "garbarino.com.ar";
    private static final String COMPUMUNDO  = "compumundo.com.ar";
    private static final String FACEBOOK    = "facebook.com";
    public static final String  CHANGE_MAIL = "whut@hotmail.com";
    public static final String  SUPER_NICK  = "super nick";

    //~ Inner Classes ................................................................................................................................

    public class MailTableRow extends MailTableRowBase {
        @NotNull @Override public Action tableMailChange() {
            return actions.getError().withMessage("Mail change in table.");
        }

        /** Suggest sync mails. */
        @NotNull public Iterable<Suggestion> tableMailSuggestSync(String query) {
            return ImmutableList.of(Suggestion.create(GARBARINO), Suggestion.create(COMPUMUNDO));
        }
    }

    public class UiTableRow extends UiTableRowBase {
        @NotNull @Override public Action changedNameUpdateNick() {
            setNick(SUPER_NICK);
            return actions.getDefault();
        }
    }
}  // end class MailFieldShowcaseForm
