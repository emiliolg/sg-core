
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model.g;

import org.jetbrains.annotations.Nullable;

import tekgenesis.persistence.PersistableInstance;
import tekgenesis.persistence.nomm.model.Category;
import tekgenesis.persistence.nomm.model.CategoryForUpdate;

/**
 * Test Class.
 */
@SuppressWarnings(
        {
            "WeakerAccess", "InstanceVariableMayNotBeInitialized", "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber",
            "FieldMayBeFinal", "ParameterHidesMemberVariable"
        }
                 )
public class CategoryForUpdateBase extends Category implements PersistableInstance<Category, String> {

    //~ Methods ......................................................................................................................................

    public long incrementVersion() {
        return ((CategoryBase) this).instanceVersion++;
    }

    public CategoryForUpdate setName(String name) {
        ((CategoryBase) this).name = name;
        return (CategoryForUpdate) this;
    }

    public CategoryForUpdate setParent(@Nullable Category parent) {
        ((CategoryBase) this).parent.set(parent);
        ((CategoryBase) this).parentCode = parent == null ? null : parent.getCode();
        return (CategoryForUpdate) this;
    }

    //~ Methods ......................................................................................................................................

    public static CategoryForUpdate categoryForUpdate(String code) {
        final CategoryForUpdate c = new CategoryForUpdate();
        ((CategoryBase) c).code = code;
        return c;
    }

    public static CategoryForUpdate categoryForUpdate(CategoryBase category) {
        return category.copyTo(new CategoryForUpdate());
    }
}
