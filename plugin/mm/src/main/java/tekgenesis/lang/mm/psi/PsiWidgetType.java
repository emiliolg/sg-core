
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import java.util.HashSet;
import java.util.Set;

import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.metadata.form.widget.WidgetTypes;

import static java.util.Collections.emptySet;

/**
 * Widget type node.
 */
public class PsiWidgetType extends ElementWithReferences {

    //~ Constructors .................................................................................................................................

    PsiWidgetType(MMElementType type) {
        super(type);
    }

    //~ Methods ......................................................................................................................................

    /** Get underlying @{@link WidgetType}. */
    @Nullable public WidgetType getWidgetType() {
        return WidgetTypes.fromId(getText());
    }

    /** Return PsiWidgetType arguments (if any). */
    Set<String> getWidgetTypeArguments() {
        final MMCommonComposite arguments = (MMCommonComposite) findChildByType(MMElementType.LIST);
        if (arguments == null) return emptySet();

        return ImmutableList.fromArray(arguments.getChildren())
               .filter(e ->
                    e instanceof MMCommonComposite || e instanceof MMIdentifier)
               .map(PsiElement::getText)
               .into(new HashSet<>());
    }
}
