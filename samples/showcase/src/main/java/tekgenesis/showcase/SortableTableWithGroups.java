
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.math.BigDecimal;
import java.util.Random;

import tekgenesis.common.core.DateTime;

/**
 * User class for Form: SortableTableWithGroups
 */
public class SortableTableWithGroups extends SortableTableWithGroupsBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        for (int i = 0; i < 10; i++)
            getSales().add().populate(i);
    }

    //~ Static Fields ................................................................................................................................

    public static final String[] DEVELOPERS = { "Diego Larralde", "Gastón Lodieu", "agustin lopeZ" };
    public static final String[] MAILS      = { "diego.larralde@gmail.com", "glodiue@hotmail.com", "agustinlg@gmail.com" };

    //~ Inner Classes ................................................................................................................................

    public class SalesRow extends SalesRowBase {
        @SuppressWarnings("DuplicateStringLiteralInspection")
        void populate(int i) {
            setId(Integer.parseInt("20000" + i));
            setSession("admin");

            final Random r       = new Random();
            final int    nextInt = r.nextInt(DEVELOPERS.length);

            final String developer = DEVELOPERS[nextInt];
            setFirst(developer.substring(0, developer.indexOf(" ")));
            setLast(developer.substring(developer.indexOf(" ") + 1));
            setEmail(MAILS[nextInt]);

            setFullfillment(Options.values()[r.nextInt(Options.values().length)]);
            setAmount(new BigDecimal(r.nextDouble()));
            setSync(r.nextBoolean());
            setUpdateTime(DateTime.current().addDays(-i));
        }
    }
}
