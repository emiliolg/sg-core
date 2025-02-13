
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

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.form.Action;

/**
 * Image Form class.
 */
@SuppressWarnings("WeakerAccess")
public class ImageForm extends ImageFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action imageClick() {
        return actions.getDefault();
    }

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void load() {
        setImgEnum(Bands.THE_NATIONAL);
        setImgString("https://www.google.com.ar/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png");
        final ImmutableList<String> images = ImmutableList.of("public/img/homero2.jpeg", "public/img/homero.jpeg");
        setImgMultiple(images);
        setGallery(images);
        setVideo2("http://www.youtube.com/embed/L2E4vihNehM");
    }

    @NotNull @Override public ImageResource populate() {
        final ImageResource populate = super.populate();
        if (getImgBound() != null) setImgResource(getImgBound());
        return populate;
    }
}
