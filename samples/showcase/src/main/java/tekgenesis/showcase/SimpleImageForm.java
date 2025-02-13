
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

import tekgenesis.common.collections.ImmutableList;

/**
 * User class for Form: SimpleImageForm
 */
@Generated(value = "tekgenesis/showcase/ImageShowcase.mm", date = "1362164196991")
public class SimpleImageForm extends SimpleImageFormBase {

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void load() {
        setImgEnum(Bands.THE_NATIONAL);
        setImgString("http://userserve-ak.last.fm/serve/500/44002633/The+National+2010.jpg");
        setImgMultiple(ImmutableList.of("public/img/homero2.jpeg", "public/img/homero.jpeg"));
        // setGallery(images);
    }

    @NotNull @Override public ImageResource populate() {
        final ImageResource populate = super.populate();
        if (getImgBound() != null) setImgResource(getImgBound());
        return populate;
    }
}
