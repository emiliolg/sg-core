
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.errors;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;

import static tekgenesis.lang.mm.errors.MMErrorFixer.provideFix;

/**
 * Builder Error Listener used when building AST from Idea.*
 */
public class MMBuilderErrorListener implements BuilderErrorListener {

    //~ Instance Fields ..............................................................................................................................

    private boolean errors;

    private final AnnotationHolder holder;

    //~ Constructors .................................................................................................................................

    /** BuilderErrorListener constructor used when building AST from Idea.* */
    public MMBuilderErrorListener(AnnotationHolder holder) {
        this.holder = holder;
        errors      = false;
    }

    //~ Methods ......................................................................................................................................

    @Override public void error(BuilderError error) {
        final MetaModelAST node = ((MMFile) holder.getCurrentAnnotationSession().getFile()).getAnyNode(error.getModelName());
        errors = true;
        error(node, error);
    }

    @Override public void error(@Nullable MetaModelAST node, BuilderError error) {
        errors = true;
        if (node != null && !node.isEmpty()) {
            final PsiElement p = (PsiElement) node;
            if (p.isValid()) {
                final Annotation                                      annotation = holder.createErrorAnnotation(p, error.getMessage());
                final Option<MMErrorFixer.FixIntention<BuilderError>> fix        = provideFix(error);
                if (fix.isPresent()) annotation.registerFix(fix.get().withAnnotation(annotation).withNode(node));
            }
        }
    }

    @Override public boolean hasErrors() {
        return errors;
    }
}  // end class MMBuilderErrorListener
