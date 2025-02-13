
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.module;

import javax.swing.*;

import com.intellij.ide.projectWizard.ModuleTypeCategory;
import com.intellij.ide.util.frameworkSupport.FrameworkRole;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.lang.mm.facet.MMFacet;
import tekgenesis.lang.mm.facet.MMFrameworkType;

import static com.intellij.ide.util.projectWizard.JavaModuleBuilder.JAVA_WEIGHT;

/**
 * Sui Generis project category.
 */
public class SuiGenerisProjectCategory extends ModuleTypeCategory {

    //~ Constructors .................................................................................................................................

    /** Sui Generis project category. */
    public SuiGenerisProjectCategory() {
        super(new SuiGenerisModuleType());
    }

    //~ Methods ......................................................................................................................................

    @Override public FrameworkRole[] getAcceptableFrameworkRoles() {
        return new FrameworkRole[] { ROLE };
    }

    @Override public String getDescription() {
        return Constants.SUI_GENERIS;
    }

    @Override public String getDisplayName() {
        return Constants.SUI_GENERIS;
    }

    @Override public Icon getIcon() {
        return MMFileType.ENTITY_FILE_ICON;
    }

    @Override public String getId() {
        return MMFacet.ID.toString();
    }

    @NotNull @Override public String[] getPreselectedFrameworkIds() {
        return new String[] { MMFrameworkType.MM_FRAMEWORK };
    }

    @Override public int getWeight() {
        return JAVA_WEIGHT + 1;  /* Up there... */
    }

    //~ Static Fields ................................................................................................................................

    public static final FrameworkRole ROLE = new FrameworkRole(MMFacet.ID.toString());
}  // end class SuiGenerisProjectCategory
