
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

import tekgenesis.form.Action;

/**
 * User class for Form: TypeTest
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber" })
public class TypeTest extends TypeTestBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changed() {
        System.out.println("TypeTest.changed");
        return actions.getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void load() {
        setId("Test ID");

        final PersonType p = new PersonType();
        p.setName("Lucas Luppani");
        p.setAge(31);
        setPersonType(p);
    }
}
