
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.configuration;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.Predefined;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.metadata.form.widget.WidgetTypes;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

/**
 * Dynamic widget serializable configuration.
 */
@SuppressWarnings("UnusedReturnValue")
public class DynamicConfig extends WidgetConfig implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private boolean       multiple;
    private boolean       secret;
    private boolean       signed;
    @NotNull private Type type;

    @NotNull private WidgetType widget;

    //~ Constructors .................................................................................................................................

    /** Create default dynamic configuration. */
    public DynamicConfig() {
        widget   = WidgetType.NONE;
        type     = Types.stringType(WidgetTypes.MAX_TEXT_FIELD_LENGTH);
        multiple = false;
        signed   = false;
        secret   = false;
    }

    //~ Methods ......................................................................................................................................

    @Override public void deserializeFields(StreamReader r) {
        widget   = WidgetType.values()[r.readInt()];
        type     = Kind.instantiateType(r);
        signed   = r.readBoolean();
        multiple = r.readBoolean();
        secret   = r.readBoolean();
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DynamicConfig)) return false;

        final DynamicConfig that = (DynamicConfig) o;

        return Predefined.equal(type, that.type) && widget == that.widget && that.multiple == multiple && that.signed == signed &&
               that.secret == secret;
    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (multiple ? 1 : 0);
        result = 31 * result + (signed ? 1 : 0);
        result = 31 * result + (secret ? 1 : 0);
        result = 31 * result + type.hashCode();
        result = 31 * result + widget.hashCode();
        return result;
    }

    /** True if widget is defined on configuration. */
    public boolean hasWidget() {
        return widget != WidgetType.NONE;
    }

    @Override public void serializeFields(StreamWriter w) {
        w.writeInt(widget.ordinal());
        Kind.serializeType(w, type);
        w.writeBoolean(signed);
        w.writeBoolean(multiple);
        w.writeBoolean(secret);
    }

    /** Sets this configuration to be signed (only for Numbers). */
    public DynamicConfig signed() {
        signed = true;
        return this;
    }

    /** Returns true if this configuration is set to be signed. */
    public boolean isSigned() {
        return signed;
    }

    /** Return dynamic multiple. */
    public boolean isMultiple() {
        return multiple;
    }

    /** Set dynamic multiple. */
    public DynamicConfig setMultiple(boolean m) {
        multiple = m;
        return this;
    }

    /** Set dynamic secret. */
    public DynamicConfig setSecret(boolean s) {
        secret = s;
        return this;
    }

    /** Return dynamic secret. */
    public boolean isSecret() {
        return secret;
    }

    /** Return dynamic type. */
    @NotNull public Type getType() {
        return type;
    }

    /** Set dynamic type. */
    public DynamicConfig setType(@NotNull Type t) {
        type = t;
        return this;
    }

    /** Return dynamic widget. */
    @NotNull public WidgetType getWidget() {
        return widget;
    }

    /** Set dynamic widget. */
    public DynamicConfig setWidget(@NotNull WidgetType w) {
        widget = w;
        return this;
    }

    @Override WidgetType getWidgetType() {
        return WidgetType.DYNAMIC;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -8085847541701428849L;

    public static final DynamicConfig DEFAULT = new DynamicConfig();
}  // end class DynamicConfig
