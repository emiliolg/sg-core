
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.filter;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.metadata.form.configuration.DynamicConfig;
import tekgenesis.metadata.form.widget.WidgetType;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.form.ReflectedMultipleInstance.RowInstance;

/**
 * Implementation class for {@link Filter}.
 */
abstract class FormFilter implements Filter {

    //~ Instance Fields ..............................................................................................................................

    /** Filter labels provider. May be updated by subclasses. */
    @NotNull Labels<Object> labels;

    /** Filter accepts provider. */
    @NotNull private Accepts<RowInstance, Object> accepts;

    /** Filter exclusion condition. */
    private boolean exclusive;

    /** Filter options provider. */
    @NotNull private Options<RowInstance, Object> options;

    /** Filter title. */
    @NotNull private String title;

    /** Widget type to display. May be updated by subclasses. */
    @NotNull private FilterType type;

    //~ Constructors .................................................................................................................................

    FormFilter(@NotNull final String title, @NotNull final Options<RowInstance, Object> options,
               @NotNull final Accepts<RowInstance, Object> accepts) {
        this.title   = title;
        this.options = options;
        this.accepts = accepts;
        type         = FilterType.CHECK;
        exclusive    = false;
        labels       = Labels.DEFAULT;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormFilter)) return false;
        final FormFilter that = (FormFilter) o;
        return labels.equals(that.labels) && options.equals(that.options) && title.equals(that.title) && type == that.type;
    }

    @Override public Filter exclusive() {
        exclusive = true;
        return this;
    }

    @Override public int hashCode() {
        int result = labels.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + options.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }

    @Override public Filter inclusive() {
        exclusive = false;
        return this;
    }

    @Override public Filter multiple() {
        type = FilterType.CHECK;
        return this;
    }

    @Override public Filter single() {
        type = FilterType.RADIO;
        return this;
    }

    @Override public Filter title(@NotNull String t) {
        title = t;
        return this;
    }

    boolean accepts(@NotNull RowInstance row, Object option) {
        return accepts.apply(row, option);
    }

    Seq<Object> compute(@NotNull final Seq<RowInstance> values) {
        return options.values(values);
    }

    void configurate(@NotNull final DynamicConfig dynamic) {
        switch (type) {
        case CHECK:
            dynamic.setWidget(WidgetType.CHECK_BOX_GROUP);
            break;
        case RADIO:
            dynamic.setWidget(WidgetType.RADIO_GROUP);
            break;
        }

        // Type???
    }

    String label(@NotNull final Object option) {
        return labels.value(option);
    }

    void setAccepts(@NotNull Accepts<RowInstance, ?> accepts) {
        this.accepts = cast(accepts);
    }

    @NotNull String getDynamicType() {
        return type.getRenderType();
    }

    boolean isExclusive() {
        return exclusive;
    }

    void setOptions(@NotNull Options<RowInstance, ?> options) {
        this.options = cast(options);
    }

    @NotNull String getTitle() {
        return title;
    }

    //~ Enums ........................................................................................................................................

    // Integer getFieldOrdinal() { return field.ordinal(); }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    enum FilterType {
        RADIO("RADIO_GROUP"), CHECK("CHECK_BOX_GROUP");

        @NotNull private final String type;

        FilterType(@NotNull final String type) {
            this.type = type;
        }

        @NotNull public String getRenderType() {
            return type;
        }
    }
}  // end class FormFilter
