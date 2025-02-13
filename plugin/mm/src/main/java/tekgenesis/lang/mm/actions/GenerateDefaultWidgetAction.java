
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

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.PsiType;

import static tekgenesis.lang.mm.MMPluginConstants.WIDGET;

/**
 * Generate Default Widget for Entity.
 */
public class GenerateDefaultWidgetAction extends GenerateForModelAction {

    //~ Constructors .................................................................................................................................

    /** Generate Default Widget Definition for Entity. */
    public GenerateDefaultWidgetAction() {}

    /** Generate Default Widget Definition for Entity. */
    public GenerateDefaultWidgetAction(Editor editor, Project project) {
        super(editor, project);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean hasValidContext() {
        return super.hasValidContext() || psiModel instanceof PsiType;
    }

    @Override String getActionName() {
        return GENERATE_DEFAULT_WIDGET;
    }

    @Override String getTextToWrite() {
        return "\n\nwidget " + getWidgetName() + " : " + psiModel.getName() + ";";
    }

    @Override String getTextToWriteInModel() {
        return " widget " + getWidgetName() + " ";
    }

    @NotNull private String getWidgetName() {
        final String defaultWidgetName = psiModel.getFullName() + WIDGET;
        String       fullWidgetName    = defaultWidgetName;
        String       widgetName        = psiModel.getName() + WIDGET;
        boolean      hasWidgetWithName = psiModel.hasWidgetWithName(fullWidgetName);
        int          i                 = 0;
        while (hasWidgetWithName) {
            i++;
            fullWidgetName    = defaultWidgetName + i;
            widgetName        = psiModel.getName() + WIDGET + i;
            hasWidgetWithName = psiModel.hasWidgetWithName(fullWidgetName);
        }
        return widgetName;
    }

    //~ Static Fields ................................................................................................................................

    public static final String GENERATE_DEFAULT_WIDGET = "GenerateDefaultWidget";
}  // end class GenerateDefaultWidgetAction
