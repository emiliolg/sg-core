
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

import tekgenesis.field.FieldOption;
import tekgenesis.lang.mm.MMElementType;

import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * An Option.
 */
public class PsiFieldOption extends MMCommonComposite {

    //~ Constructors .................................................................................................................................

    /** Creates an Identifier. */
    PsiFieldOption(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    /** Get Field Option. */
    @NotNull public FieldOption getOption() {
        final PsiElement option = getFirstChild();
        assert option != null : MSGS.fieldOptionNodeHasNoOption();
        final FieldOption result = FieldOption.fromId(option.getText());
        assert result != null : MSGS.parsedFieldOptionNodeInvalidId();
        return result;
    }
}
