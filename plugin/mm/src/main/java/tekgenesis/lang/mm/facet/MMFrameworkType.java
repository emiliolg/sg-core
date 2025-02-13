
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.facet;

import javax.swing.*;

import com.intellij.framework.FrameworkTypeEx;
import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMFileType;

import static tekgenesis.lang.mm.MMPluginConstants.SUI_GENERIS;

/**
 * MM Framework Type.
 */
public class MMFrameworkType extends FrameworkTypeEx {

    //~ Constructors .................................................................................................................................

    MMFrameworkType() {
        super(MM_FRAMEWORK);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public FrameworkSupportInModuleProvider createProvider() {
        return new MMFrameworkSupportProvider();
    }

    @NotNull @Override public Icon getIcon() {
        return MMFileType.ENTITY_FILE_ICON;
    }

    @NotNull @Override public String getPresentableName() {
        return SUI_GENERIS;
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String MM_FRAMEWORK = "mm-framework";
}
