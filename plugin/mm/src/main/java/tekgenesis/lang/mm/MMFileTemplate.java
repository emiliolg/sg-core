
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.ide.fileTemplates.impl.FileTemplateBase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;

/**
 * File template for create actions.
 */
public class MMFileTemplate extends FileTemplateBase {

    //~ Instance Fields ..............................................................................................................................

    private String name;

    //~ Constructors .................................................................................................................................

    /** Template constructor with file name. */
    public MMFileTemplate(String name) {
        this.name = name;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public String getDescription() {
        return "";
    }

    @NotNull @Override public String getExtension() {
        return Constants.META_MODEL_EXT;
    }

    @Override public void setExtension(@NotNull String extension) {}

    @NotNull @Override public String getName() {
        return name;
    }

    @Override public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override public boolean isDefault() {
        return false;
    }
}
