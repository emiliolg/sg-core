
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.type.Types.stringType;

/**
 * A {@link Type} that represents a Resource Type.
 */
public class ResourceType extends AbstractType {

    //~ Constructors .................................................................................................................................

    private ResourceType() {}

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Kind getKind() {
        return Kind.RESOURCE;
    }

    @NotNull @Override public String getSqlImplementationType(boolean multiple) {
        return stringType(LARGE).getSqlImplementationType(multiple);
    }

    @Override public int getSqlType() {
        return stringType(LARGE).getSqlType();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 7933884796587163715L;

    private static final int LARGE = 128;

    static final ResourceType INSTANCE = new ResourceType();
}
