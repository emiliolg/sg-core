
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;

import static java.sql.Types.NULL;

/**
 * A Base abstract type that includes default implementations of common {@link Type} methods.
 */
public abstract class AbstractType implements Type {

    //~ Methods ......................................................................................................................................

    @Override public Type applyParameters(Seq<String> strings) {
        return this;
    }

    @Nullable @Override public String asString(@Nullable Object obj) {
        return obj == null ? null : obj.toString();
    }

    @NotNull @Override public Type commonSuperType(@NotNull Type that) {
        return equivalent(that) ? that : Types.anyType();
    }

    @Override public boolean equivalent(Type t) {
        return getKind() == t.getKind();
    }

    @Override public String toString() {
        return getKind().toString();
    }

    @Override public Object valueOf(String str) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean isUndefined() {
        return isNull();
    }

    @Override public boolean isVoid() {
        return false;
    }

    @Override public Object getDefaultValue() {
        return null;
    }

    @Override public boolean isComposite() {
        return false;
    }

    @Override public boolean isResource() {
        return getKind() == Kind.RESOURCE;
    }

    @Override public boolean isTime() {
        return false;
    }

    @Override public boolean isType() {
        return getKind() == Kind.TYPE;
    }

    @Override public Type getFinalType() {
        return this;
    }

    @Override public final boolean isString() {
        return getKind() == Kind.STRING;
    }

    @Nullable @Override public Class<?> getImplementationClass() {
        return getKind().getImplementationClass();
    }

    @Override public String getImplementationClassName() {
        final Class<?> c = getImplementationClass();
        return c == null ? "" : c.getName();  // using get Name instead of getCanonicalName for GWT compilation
    }

    @Override public boolean isHtml() {
        return getKind() == Kind.HTML;
    }

    @Override public boolean isNull() {
        return getKind() == Kind.NULL;
    }
    @Override public Option<Integer> getLength() {
        return Option.empty();
    }

    @Override public final boolean isEnum() {
        return getKind() == Kind.ENUM;
    }

    @Override public boolean isBoolean() {
        return getKind() == Kind.BOOLEAN;
    }

    @Override public int getParametersCount() {
        return 0;
    }

    @Override public boolean isInner() {
        return false;
    }

    @Override public final boolean isNumber() {
        return getKind().isNumber();
    }

    @NotNull @Override public String getSqlImplementationType(boolean multiple) {
        return "";
    }

    @Override public int getSqlType() {
        return NULL;
    }

    @Override public boolean isDatabaseObject() {
        return false;
    }

    @Override public boolean isView() {
        return false;
    }

    @Override public boolean isAny() {
        return getKind() == Kind.ANY;
    }

    @Override public final boolean isArray() {
        return getKind() == Kind.ARRAY;
    }

    @Override public boolean isEntity() {
        return false;
    }

    //~ Methods ......................................................................................................................................

    public static void addMetaModelTypes(Set<MetaModel> result, Type type) {
        if (type instanceof MetaModel) result.add((MetaModel) type);
        else if (type.isArray()) {
            final ArrayType array = (ArrayType) type;
            addMetaModelTypes(result, array.getElementType());
        }
    }  // end method addMetaModelTypes

    protected static boolean isString(@Nullable final Object o) {
        return o instanceof String && !((String) o).isEmpty();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 6534988872187163926L;
}  // end class AbstractType
