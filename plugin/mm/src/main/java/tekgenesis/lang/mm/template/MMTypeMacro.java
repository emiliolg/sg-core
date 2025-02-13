
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.template;

import com.intellij.codeInsight.template.*;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.vfs.VirtualFile;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.MMModuleComponent;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiDomain;
import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.repository.ModelRepository;

/**
 * MM Types Macro -- Actually using complete() expression instead.
 */
class MMTypeMacro extends Macro {

    //~ Methods ......................................................................................................................................

    public Result calculateQuickResult(@NotNull Expression[] params, ExpressionContext context) {
        if (params.length == 0) return null;
        return params[0].calculateQuickResult(context);
    }

    @Override public Result calculateResult(@NotNull Expression[] params, ExpressionContext context) {
        if (params.length == 0) return null;
        return params[0].calculateResult(context);
    }

    @Override public String getName() {
        return Constants.TYPES;
    }

    @Override public String getPresentableName() {
        return "MetaModel variable types";
    }

    @Override public boolean isAcceptableInContext(TemplateContextType context) {
        return context instanceof MMContextType;
    }

    private String resolveDomain(ExpressionContext context) {
        if (context.getEditor() instanceof EditorEx) {
            final EditorEx       editor = (EditorEx) context.getEditor();
            final VirtualFile    file   = editor.getVirtualFile();
            final Option<MMFile> mmFile = PsiUtils.findMMFile(context.getProject(), file.getPath());
            if (mmFile.isPresent()) {
                final PsiDomain domain = mmFile.get().getDomain();
                return domain != null ? domain.getDomain() : "";
            }
        }
        return "";
    }

    private ModelRepository resolveModelRepository(@NotNull final ExpressionContext context) {
        if (context.getEditor() instanceof EditorEx) {
            final EditorEx    editor = (EditorEx) context.getEditor();
            final VirtualFile file   = editor.getVirtualFile();
            final Module      module = ModuleUtil.findModuleForFile(file, context.getProject());
            if (module != null) return module.getComponent(MMModuleComponent.class).getRepository();
        }

        return new ModelRepository();
    }
}  // end class MMTypeMacro
