
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.mmcompiler.builder.QContext;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * MetaModel Psi Element.
 */
public interface MetaModelPsiElement extends PsiElement {

    //~ Methods ......................................................................................................................................

    /** Get Meta Model containing file. */
    @Override MMFile getContainingFile();

    /** Return domain text of element containing file. */
    @NotNull default String getDomain() {
        final PsiDomain domain = getContainingFile().getDomain();
        return domain != null ? domain.getDomainName() : "";
    }

    /** Return ModelRepository for current module. */
    @NotNull default ModelRepository getModelRepository() {
        return PsiUtils.getModule(this) != null ? PsiUtils.getModelRepository(getModule().getOrFail(MSGS.noModuleFound())) : new ModelRepository();
    }

    /** Get element module. */
    @NotNull default Option<Module> getModule() {
        return Option.option(PsiUtils.getModule(this));
    }

    /** Return file qualification context. */
    @NotNull default QContext getQContext() {
        return getContainingFile().getQContext();
    }
}
