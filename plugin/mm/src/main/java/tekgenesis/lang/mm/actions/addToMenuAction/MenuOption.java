
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions.addToMenuAction;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

/**
 * Abstract class that represents a selectable menu option.
 */
public abstract class MenuOption {

    //~ Instance Fields ..............................................................................................................................

    final String name;

    //~ Constructors .................................................................................................................................

    /** Constructs an option. */
    protected MenuOption(String name) {
        this.name = name;
    }

    //~ Methods ......................................................................................................................................

    /** Method to execute when an option is chosen. */
    public abstract void onChosen(PsiElement clickedElement, Project project, Editor editor);

    /** Get menu name. */
    public String getName() {
        return name;
    }
}
