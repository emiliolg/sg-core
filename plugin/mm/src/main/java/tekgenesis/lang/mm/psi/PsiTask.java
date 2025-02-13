
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

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.Predefined;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.util.CronExpression;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.lang.mm.intention.ScheduleSuggesterIntention;
import tekgenesis.metadata.task.Task;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * Task Psi.
 */

public class PsiTask extends PsiMetaModel<Task> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiTask(MMElementType t) {
        super(t, MetaModelKind.TASK, Task.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public void annotate(AnnotationHolder holder) {
        super.annotate(holder);

        // TODO find a better and cleaner way
        final Option<Task> model = getModel();
        for (final Task modelElement : model) {
            for (final PsiElement child : getChildren())
                if (child.getText().contains(Constants.SCHEDULE)) {
                    final int startOffsetCronExpression = child.getStartOffsetInParent() + child.getText().indexOf('"') + getStartOffsetInParent();

                    if (Predefined.isEmpty(modelElement.getScheduleAfter())) {
                        ((MetaModelAST) child).getType().getHighlight();
                        final String    cronExpressionString = modelElement.getCronExpression();
                        final TextRange textRange            = TextRange.create(startOffsetCronExpression,
                                startOffsetCronExpression + cronExpressionString.length() + 2);
                        // the + 2 is to include '"' surrounding
                        if (!CronExpression.isValidExpression(cronExpressionString))
                            holder.createWarningAnnotation(textRange, MSGS.badChronologicalExpression());
                        else {
                            final Annotation annotation = holder.createWeakWarningAnnotation(textRange, MSGS.chronologicalExpression());
                            annotation.registerFix(
                                new ScheduleSuggesterIntention(cronExpressionString, getContainingFile(), startOffsetCronExpression));
                        }
                    }

                    return;
                }
        }
    }  // end method annotate

    @Override public PsiModelField getFieldNullable(String fieldName) {
        return null;
    }

    @NotNull @Override public PsiModelField[] getFields() {
        return EMPTY_MODEL_FIELDS;
    }

    @Override public Icon getIcon(int flags) {
        return MMFileType.TASK_ICON;
    }
}  // end class PsiTask
