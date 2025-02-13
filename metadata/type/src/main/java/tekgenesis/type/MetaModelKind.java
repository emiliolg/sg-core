
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import java.util.EnumSet;

/**
 * Metamodel Kinds.
 */
@SuppressWarnings("NonJREEmulationClassesInClientCode")
public enum MetaModelKind {

    //~ Enum constants ...............................................................................................................................

    ENUM, ENTITY, FORM, WIDGET, TYPE, ROLE, CASE, HANDLER, MENU, VIEW, UNDEFINED, TASK, LINK, SEARCHABLE;

    //~ Static Fields ................................................................................................................................

    /** Common Sets. */

    public static final EnumSet<MetaModelKind> DB_AND_UI             = EnumSet.of(ENTITY, TYPE, FORM, WIDGET);
    public static final EnumSet<MetaModelKind> FORM_ONLY             = EnumSet.of(FORM);
    public static final EnumSet<MetaModelKind> WIDGET_ONLY           = EnumSet.of(WIDGET);
    public static final EnumSet<MetaModelKind> UI_MODELS             = EnumSet.of(FORM, WIDGET);
    public static final EnumSet<MetaModelKind> ROLE_ONLY             = EnumSet.of(ROLE);
    public static final EnumSet<MetaModelKind> TYPE_ONLY             = EnumSet.of(TYPE);
    public static final EnumSet<MetaModelKind> HANDLER_ONLY          = EnumSet.of(HANDLER);
    public static final EnumSet<MetaModelKind> ENTITY_ONLY           = EnumSet.of(ENTITY);
    public static final EnumSet<MetaModelKind> CASE_ONLY             = EnumSet.of(CASE);
    public static final EnumSet<MetaModelKind> MENU_AND_CASE_ONLY    = EnumSet.of(CASE, MENU);
    public static final EnumSet<MetaModelKind> TASK_ONLY             = EnumSet.of(TASK);
    public static final EnumSet<MetaModelKind> MENU_ONLY             = EnumSet.of(MENU);
    public static final EnumSet<MetaModelKind> ENUM_ONLY             = EnumSet.of(ENUM);
    public static final EnumSet<MetaModelKind> FORM_AND_MENU_ONLY    = EnumSet.of(FORM, MENU);
    public static final EnumSet<MetaModelKind> ENTITY_AND_VIEW_ONLY  = EnumSet.of(ENTITY, VIEW);
    public static final EnumSet<MetaModelKind> HANDLER_AND_FORM_ONLY = EnumSet.of(FORM, HANDLER);
    public static final EnumSet<MetaModelKind> MODEL_TYPE            = EnumSet.of(ENTITY, VIEW, TYPE);
    public static final EnumSet<MetaModelKind> SEARCHABLE_ONLY       = EnumSet.of(SEARCHABLE);

    public static final EnumSet<MetaModelKind> SCOPE_TYPE   = EnumSet.of(ENTITY, ENUM);
    public static final EnumSet<MetaModelKind> SCOPE_TABLE  = EnumSet.of(ENTITY, VIEW);
    public static final EnumSet<MetaModelKind> SCOPE_STRUCT = EnumSet.of(TYPE, ENUM);
    public static final EnumSet<MetaModelKind> SCOPE_IMPORT = EnumSet.of(ENTITY, ENUM, VIEW, FORM, TYPE);
    public static final EnumSet<MetaModelKind> SCOPE_EMPTY  = EnumSet.noneOf(MetaModelKind.class);
}
