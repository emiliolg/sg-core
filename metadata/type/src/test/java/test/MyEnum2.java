
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

@SuppressWarnings("ALL")
public enum MyEnum2 implements Enumeration<MyEnum2, String> {

    //~ Enum constants ...............................................................................................................................

    A("aa"), B("bb");

    //~ Instance Fields ..............................................................................................................................

    private final String label;

    //~ Constructors .................................................................................................................................

    MyEnum2(String label) {
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
}
