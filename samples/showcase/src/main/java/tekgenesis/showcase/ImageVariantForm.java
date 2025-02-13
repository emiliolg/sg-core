
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Resource;
import tekgenesis.form.Action;
import tekgenesis.form.image.ImageResizer;
import tekgenesis.form.image.Images;
import tekgenesis.persistence.ResourceHandler;

import static tekgenesis.common.env.context.Context.getSingleton;

/**
 * User class for Form: ImageVariantForm
 */
public class ImageVariantForm extends ImageVariantFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action copyImage() {
        setGal(Colls.listOf(getResource()));
        setShowcase(Colls.listOf(getResource()));
        setShowcase1(Colls.listOf(getResource()));
        return actions.getDefault();
    }

    /** Invoked when button(fillButt) is clicked. */
    @NotNull @Override public Action fillGallery() {
        for (final Resource resource : getSingleton(ResourceHandler.class).findResource(getResource().getUuid())) {
            if (!Predefined.isDefined(resource.getEntry(JLONGO_CUSTOM))) {
                try {
                    Images.build(JLONGO_CUSTOM, WIDTH, HEIGHT)
                        .of(resource, resource.getMaster().getMetadata().getSize())
                        .using(ImageResizer.getInstance());
                }
                catch (final IOException e) {
                    return actions.getError().withMessage(e.getMessage());
                }
                setShowcase(Colls.listOf(resource));
                setShowcase1(Colls.listOf(resource));
                setGal(Colls.listOf(resource));
            }
        }
        return actions.getDefault();
    }

    //~ Static Fields ................................................................................................................................

    private static final String JLONGO_CUSTOM = "jlongo";
    private static final int    WIDTH         = 1000;
    private static final int    HEIGHT        = 1000;
}
