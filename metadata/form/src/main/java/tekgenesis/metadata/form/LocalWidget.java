
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.common.serializer.Streams;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.UiModel;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.serializer.Streams.writeNullableInteger;
import static tekgenesis.metadata.form.SourceWidget.buttonTypeSupportsTableDiscovery;
import static tekgenesis.metadata.form.widget.WidgetTypes.isMultiple;

/**
 * Local widget (local to an specified model) without qualification.
 */
@SuppressWarnings("FieldMayBeFinal")  // gwt serialization
public class LocalWidget {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private Integer item;
    @NotNull private String   name;

    //~ Constructors .................................................................................................................................

    public LocalWidget(@NotNull final String name, @Nullable final Integer item) {
        this.name = name;
        this.item = item;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalWidget)) return false;
        final LocalWidget that = (LocalWidget) o;
        return equal(item, that.item) && equal(name, that.name);
    }

    @Override public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + name.hashCode();
        return result;
    }

    @NotNull public Option<Integer> getItem() {
        return ofNullable(item);
    }

    /** Given form metadata returns MultipleWidget for this SourceWidget. */
    public Option<MultipleWidget> getMultipleWidget(@NotNull UiModel model) {
        if (isEmpty(name)) return empty();

        return model.getWidget(name).flatMap(widget -> {
            if (buttonTypeSupportsTableDiscovery(widget.getButtonType())) return some((MultipleWidget) model.getElement(widget.getButtonBoundId()));
            if (isMultiple(widget.getWidgetType())) return some((MultipleWidget) widget);
            return widget.getMultiple();
        });
    }

    //~ Methods ......................................................................................................................................

    /** LocalWidget de-serialization. */
    @Nullable public static LocalWidget deserialize(StreamReader r) {
        if (!r.readBoolean()) return null;
        final String  name = r.readString();
        final Integer item = Streams.readNullableInteger(r);
        return new LocalWidget(name, item);
    }

    /** LocalWidget serialization. */
    public static void serialize(@NotNull StreamWriter w, @Nullable LocalWidget s) {
        if (s == null) w.writeBoolean(false);
        else {
            w.writeBoolean(true);
            w.writeString(s.name);
            writeNullableInteger(w, s.item);
        }
    }  // end method serialize
}
