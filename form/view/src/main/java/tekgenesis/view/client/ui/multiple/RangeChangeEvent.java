
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package tekgenesis.view.client.ui.multiple;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import tekgenesis.view.client.ui.ItemsRange;
import tekgenesis.view.client.ui.MultipleUI;

/**
 * Represents a range change event.
 */
public class RangeChangeEvent extends GwtEvent<RangeChangeEvent.Handler> {

    //~ Instance Fields ..............................................................................................................................

    private final ItemsRange range;

    //~ Constructors .................................................................................................................................

    /** Creates a {@link RangeChangeEvent}. */
    private RangeChangeEvent(ItemsRange range) {
        this.range = range;
    }

    //~ Methods ......................................................................................................................................

    @Override public final Type<Handler> getAssociatedType() {
        return TYPE;
    }

    /** Gets the new range. */
    public ItemsRange getNewRange() {
        return range;
    }

    @Override protected void dispatch(Handler handler) {
        handler.onRangeChange(this);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Fires a {@link RangeChangeEvent} on all registered handlers in the handler manager. If no
     * such handlers exist, this method will do nothing.
     */
    public static void fire(MultipleUI source, ItemsRange range) {
        if (TYPE != null) source.fireEvent(new RangeChangeEvent(range));
    }

    /** Gets the type associated with this event. */
    @SuppressWarnings("NonThreadSafeLazyInitialization")
    public static Type<Handler> getType() {
        if (TYPE == null) TYPE = new Type<>();
        return TYPE;
    }

    //~ Static Fields ................................................................................................................................

    /** Handler type. */
    private static Type<Handler> TYPE = null;

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Handler interface for {@link RangeChangeEvent} events.
     */
    public interface Handler extends EventHandler {
        /** Called when a {@link RangeChangeEvent} is fired. */
        void onRangeChange(RangeChangeEvent event);
    }
}  // end class RangeChangeEvent
