
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type.resource;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.type.resource.Variant.Dimension.of;

/**
 * Predefined Resource Variants.
 */
@SuppressWarnings("MagicNumber")
public enum Variant {

    //~ Enum constants ...............................................................................................................................

    THUMB(AbstractResource.THUMB, some(of(150, 150))), LARGE(AbstractResource.LARGE, some(of(350, 350))), MASTER(AbstractResource.MASTER, empty());

    //~ Instance Fields ..............................................................................................................................

    @NotNull
    @SuppressWarnings("GwtInconsistentSerializableClass")
    private final Option<Dimension>          dimension;

    @NotNull private final String variantName;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("WeakerAccess")  // inspections bug
    Variant(@NotNull final String variantName, @NotNull final Option<Dimension> dimension) {
        this.variantName = variantName;
        this.dimension   = dimension;
    }

    //~ Methods ......................................................................................................................................

    /** Returns variant height. */
    public int getHeight() {
        return dimension.orElseThrow(Variant::illegalDimension).height;
    }

    /** Returns Resource variant name. */
    @NotNull public String getVariantName() {
        return variantName;
    }

    /** Returns variant width. */
    public int getWidth() {
        return dimension.orElseThrow(Variant::illegalDimension).width;
    }

    boolean hasDimension() {
        return dimension.isPresent();
    }

    //~ Methods ......................................................................................................................................

    @NotNull private static IllegalStateException illegalDimension() {
        return new IllegalStateException("Dimension not present");
    }

    //~ Inner Classes ................................................................................................................................

    static class Dimension {
        final int height;
        final int width;

        private Dimension(int width, int height) {
            this.width  = width;
            this.height = height;
        }

        public static Dimension of(int width, int height) {
            return new Dimension(width, height);
        }
    }
}
