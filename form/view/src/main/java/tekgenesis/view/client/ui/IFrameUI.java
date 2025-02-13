
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasValue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.widget.Widget;

public class IFrameUI extends BaseHasScalarValueUI {

    //~ Instance Fields ..............................................................................................................................

    private final IFrame iFrame;

    //~ Constructors .................................................................................................................................

    public IFrameUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        iFrame = new IFrame();
        init(iFrame, true);
        iFrame.addLoadHandler(new FrameHandler());
    }

    //~ Inner Classes ................................................................................................................................

    private class FrameHandler implements LoadHandler {
        @Override public void onLoad(LoadEvent event) {
            final String url = iFrame.getUrl();
            GWT.log(url);
            setValue(url, true);
        }
    }

    private static class IFrame extends Frame implements HasValue<String> {
        IFrame() {
            super();
            addStyleName("iframe");
        }

        @Override public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueHandler) {
            return addHandler(valueHandler, ValueChangeEvent.getType());
        }

        @Nullable @Override public String getValue() {
            return getUrl();
        }

        @Override public void setValue(String value) {
            setUrl(value);
        }

        @Override public void setValue(String value, boolean fireEvents) {
            if (!value.equals(getValue())) {
                setValue(value);
                if (fireEvents) ValueChangeEvent.fire(this, value);
            }
        }
    }
}  // end class IFrameUI
