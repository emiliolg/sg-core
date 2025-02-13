
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

import tekgenesis.type.Kind;
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.ModelType;

@SuppressWarnings("ALL")
public class TestMetaModel extends ModelType {

    //~ Constructors .................................................................................................................................

    public TestMetaModel(@NotNull String sourceName, @NotNull String domain, @NotNull String name) {
        super(sourceName, domain, name);
    }

    //~ Methods ......................................................................................................................................

    public String test() {
        return "TEST";
    }

    @Override public String toString() {
        return getKey().toString();
    }

    @NotNull @Override public Kind getKind() {
        return Kind.ANY;
    }
    @NotNull @Override public MetaModelKind getMetaModelKind() {
        return MetaModelKind.UNDEFINED;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5974151674907298822L;
}
