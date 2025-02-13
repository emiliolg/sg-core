
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.VideoPanel;

/**
 * Image Field Widget.
 */
public class VideoUI extends FieldWidgetUI implements HasScalarValueUI {

    //~ Instance Fields ..............................................................................................................................

    private final VideoPanel videoPanel;

    private String videoURL = null;

    //~ Constructors .................................................................................................................................

    /** Creates default VideoUI. */
    public VideoUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        videoPanel = new VideoPanel();
        initWidget(videoPanel);
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {}

    @Nullable @Override public Object getValue() {
        return videoURL;
    }

    @Override public void setValue(@Nullable Object modelValue) {
        videoURL = String.valueOf(modelValue);
        if (modelValue != null) videoPanel.setVideoURL(String.valueOf(modelValue));
    }

    @Override public void setValue(@Nullable Object modelValue, boolean fireEvents) {
        setValue(modelValue);
    }

    // ** This widget doesn't support icon */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }
}
