
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import javax.swing.*;

import com.intellij.util.ArrayFactory;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.field.FieldOption;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMFileType;

import static tekgenesis.common.core.Option.some;

/**
 * Handler fields: Route.
 */
public class PsiHandlerField extends PsiModelField {

    //~ Constructors .................................................................................................................................

    /** Create a route. */
    PsiHandlerField(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Override public Icon getIcon(int flags) {
        return MMFileType.ROUTE_ICON;
    }

    /** Return route parameters option or empty. */
    @NotNull public Option<PsiFieldOption> getParametersOption() {
        for (final PsiFieldOption option : getRouteOptions()) {
            if (option.getOption() == FieldOption.PARAMETERS) return some(option);
        }
        return Option.empty();
    }

    private PsiFieldOption[] getRouteOptions() {
        return getChildrenAsPsiElements(MMElementType.OPTION, OPTIONS_ARRAY_FACTORY);
    }

    //~ Static Fields ................................................................................................................................

    private static final ArrayFactory<PsiFieldOption> OPTIONS_ARRAY_FACTORY = PsiFieldOption[]::new;
}
