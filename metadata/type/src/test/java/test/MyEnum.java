
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package test;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Enumeration;

@SuppressWarnings({ "JavaDoc", "WeakerAccess" })
public enum MyEnum implements Enumeration<MyEnum, String> {

    //~ Enum constants ...............................................................................................................................

    A("a"), B("b"), C("cc"), D("d");

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String label;

    //~ Constructors .................................................................................................................................

    MyEnum(@NotNull String label) {
        this.label = label;
    }

    //~ Methods ......................................................................................................................................

    @Override public String imagePath() {
        return null;
    }

    @Override public int index() {
        return ordinal();
    }

    @Override public String key() {
        return name();
    }

    @NotNull @Override public String label() {
        return label;
    }
}  // end class MyEnum
