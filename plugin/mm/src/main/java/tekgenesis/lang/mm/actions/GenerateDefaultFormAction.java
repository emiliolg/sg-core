
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

/**
 * Generate Default Form for Entity.
 */
public class GenerateDefaultFormAction extends GenerateForModelAction {

    //~ Constructors .................................................................................................................................

    /** Generate Default Form for Entity. */
    public GenerateDefaultFormAction() {}

    /** Generate Default Form for Entity. */
    public GenerateDefaultFormAction(Editor editor, Project project) {
        super(editor, project);
    }

    //~ Methods ......................................................................................................................................

    @Override String getActionName() {
        return GENERATE_DEFAULT_FORM;
    }

    @Override String getTextToWrite() {
        final String defaultFormName = psiModel.getFullName() + "Form";
        String       fullFormName    = defaultFormName;
        String       formName        = psiModel.getName() + "Form";
        boolean      hasFormWithName = psiModel.hasFormWithName(fullFormName);
        int          i               = 0;
        while (hasFormWithName) {
            i++;
            fullFormName    = defaultFormName + i;
            formName        = psiModel.getName() + "Form" + i;
            hasFormWithName = psiModel.hasFormWithName(fullFormName);
        }
        return "\n\nform " + formName + " " + psiModel.getType().getText() + " " + psiModel.getName() + ";";
    }

    @Override String getTextToWriteInModel() {
        return "";
    }

    //~ Static Fields ................................................................................................................................

    public static final String GENERATE_DEFAULT_FORM = "GenerateDefaultForm";
}  // end class GenerateDefaultFormAction
