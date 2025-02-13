
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.dsl.schema;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.codegen.CodeGeneratorConstants;
import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.codegen.entity.EntityBaseCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.core.DateTime;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.persistence.IxInstance;
import tekgenesis.persistence.IxUtil;
import tekgenesis.persistence.TableField;
import tekgenesis.type.Kind;

import static tekgenesis.codegen.common.MMCodeGenConstants.TABLE_FACTORY;
import static tekgenesis.common.core.Strings.getterName;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.util.JavaReservedWords.THIS;
import static tekgenesis.md.MdConstants.UPDATE_TIME;

/**
 * Class to manage the generation of the EntityBase code.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class IxEntityBaseCodeGenerator extends EntityBaseCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    private String[] mFieldsDef = { "mdate", "mtime" };

    //~ Constructors .................................................................................................................................

    /** Create an EntityBaseCodeGenerator. */
    public IxEntityBaseCodeGenerator(JavaCodeGenerator cg, @NotNull Entity entity, @Nullable String[] mfields) {
        super(cg, entity, entity.getName());
        if (mfields != null) mFieldsDef = mfields;
        withAnnotation(extractImport(CodeGeneratorConstants.JSON_PROPERTY_ORDER), "alphabetic=true");
        addInner(createTable(cg));
    }

    //~ Methods ......................................................................................................................................

    /** Return Table singleton name. */
    public String getTableSingleton() {
        return tableSingleton;
    }

    protected void addInterfaces(String type, String keyType) {
        super.addInterfaces(type, keyType);
        asSerializable();
        withInterfaces(generic(IxInstance.class, type, keyType));
    }

    @Override protected void addMyEntityTable() {
        doAddMyEntityTable("IxEntityTable");
    }

    protected String bindToTable(String tableType) {
        return invokeStatic(TABLE_FACTORY, "bind", classOf(tableType), tableSingleton, refStatic(IxInstance.class, "IX_STORE_TYPE"));
    }

    protected ClassGenerator createTable(@NotNull JavaCodeGenerator cg) {
        return new IxEntityTableCodeGenerator(this, getDbObject(), MMCodeGenConstants.BASE);
    }

    protected String defaultFor(final ClassGenerator cg, final TypeField field, boolean required) {
        final String defaultFor = super.defaultFor(cg, field, required);
        // "" is processed as null in IX, that's why we set " " to avoid nulls in ix
        if (required && field.getFinalType().getKind() == Kind.STRING) return quoted(" ");
        return defaultFor;
    }

    @Override protected boolean includeAttribute(Attribute attribute) {
        return !(attribute.getName().equals(UPDATE_TIME) && attribute.isSynthesized()) && super.includeAttribute(attribute);
    }
    protected void makeFinal(final String method) {}

    @Override protected void populate() {
        super.populate();
        generateDateTimeAccessor();
        field("MDATE_FIELD_NAME", String.class).notNull().asFinal().asStatic().withValue(quoted(mFieldsDef[0]));
        String mTimeFieldName = " ";
        if (mFieldsDef.length > 1) mTimeFieldName = mFieldsDef[1];

        field("MTIME_FIELD_NAME", String.class).notNull().asFinal().asStatic().withValue(quoted(mTimeFieldName));
        field("TABLE_NAME", String.class).notNull().asFinal().asStatic().withValue(quoted(dbObject.getTableName().getName()));

        final Method inc = method("incr").boxedNotNull();
        inc.arg("field", TableField.class);
        inc.arg("value", double.class);
        inc.suppressWarnings(MMCodeGenConstants.RAW_TYPES);
        inc.statement(invoke(invoke(MY_ENTITY_TABLE), "incr", referenceThisType(THIS), "field", "value"));

        final Method checkLock = method("checkLock", boolean.class).boxedNotNull();

        checkLock.return_(invoke(invoke(MY_ENTITY_TABLE), "checkLock", referenceThisType(THIS)));
    }

    private void generateAccessor(String dateAttrName, String timeAttrName) {
        final String methodPrefix = timeAttrName.charAt(0) == 'c' ? "creation" : "lastUpdate";
        final String name         = getterName(methodPrefix + "DateTime", "");
        method(name, DateTime.class).withAnnotation(extractImport(JSON_IGNORE_CLASS))
            .asFinal()
            .return_(invokeStatic(IxUtil.class, "toDateTime", refData(dateAttrName), refData(timeAttrName)));
    }

    private void generateDateTimeAccessor() {
        for (final Attribute dateAttribute : getDbObject().attributes().filter(DATE_ONLY)) {
            final String dateAttrName           = dateAttribute.getName();
            final String auditableFieldNamePair = auditableFields.get(dateAttrName);
            if (auditableFieldNamePair != null) {
                for (final Attribute timeAttr : getDbObject().getAttribute(auditableFieldNamePair))
                    generateAccessor(dateAttrName, timeAttr.getName());
            }
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final String JSON_IGNORE_CLASS = "com.fasterxml.jackson.annotation.JsonIgnore";

    private static final Predicate<Attribute> DATE_ONLY = a -> a != null && a.getType().getKind() == Kind.DATE;

    // Ix Auditable Fields

    private static final Map<String, String> auditableFields = new HashMap<>();

    static {
        auditableFields.put("cdate", "ctime");
        auditableFields.put("mdate", "mtime");
    }
}  // end class IxEntityBaseCodeGenerator
