
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.configuration;

import org.jetbrains.annotations.NotNull;

/**
 * Configuration for Map widgets.
 */
@SuppressWarnings("UnusedReturnValue")
public interface UploadConfiguration extends WidgetConfiguration {

    //~ Methods ......................................................................................................................................

    /** Set image cropping on upload. */
    @NotNull UploadConfiguration crop(boolean crop);

    /** Set upload max image size constrains. */
    @NotNull UploadConfiguration maxSize(final int maxWidth, final int maxHeight);

    /** Set upload min image size constrains. */
    @NotNull UploadConfiguration minSize(final int minWidth, final int minHeight);

    /** Set upload image ratio. */
    @NotNull UploadConfiguration ratio(final int widthRatio, final int heightRatio);

    /** Set upload exact image size constrains. */
    @NotNull UploadConfiguration size(final int width, final int height);
}  // end interface UploadConfiguration
