
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;

/**
 * User class for Model: StyleShowcase
 */
@Generated(value = "tekgenesis/showcase/StyleShowcase.mm", date = "1382542922652")
public class StyleShowcase extends StyleShowcaseBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        getTable().add().insert("Pedro", "Colunga", "pcolunga");
        getTable().add().insert("Agustin", "Lopez Gabeiras", "alopezg");
        getTable().add().insert("Lucas", "Luppani", "lluppani");
        getTable().add().insert("Pablo", "Celentano", "celen");

        getTable1().add().insert("Pedro", "Colunga", "pcolunga");
        getTable1().add().insert("Agustin", "Lopez Gabeiras", "alopezg");
        getTable1().add().insert("Lucas", "Luppani", "lluppani");
        getTable1().add().insert("Pablo", "Celentano", "celen");

        setImage(Constants.TEK_LOGO);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1449310910991872227L;

    //~ Inner Classes ................................................................................................................................

    public final class Table1Row extends Table1RowBase {
        void insert(@NotNull final String firstName, @NotNull final String lasttName, @NotNull final String username) {
            setFirstName1(firstName);
            setLastName1(lasttName);
            setUsername1(username);
        }
    }

    public final class TableRow extends TableRowBase {
        void insert(@NotNull final String firstName, @NotNull final String lasttName, @NotNull final String username) {
            setFirstName(firstName);
            setLastName(lasttName);
            setUsername(username);
        }
    }
}  // end class StyleShowcase
