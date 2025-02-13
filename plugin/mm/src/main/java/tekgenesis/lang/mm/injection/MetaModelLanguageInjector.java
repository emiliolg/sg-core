
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.injection;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiLanguageInjectionHost;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiInterpolation;
import tekgenesis.lang.mm.psi.PsiPath;

import static tekgenesis.expr.ExpressionFactory.isInterpolation;

/**
 * Describes logic for injecting metamodel language inside string literal elements.
 */

public class MetaModelLanguageInjector implements LanguageInjector {

    //~ Methods ......................................................................................................................................

    @Override public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces registrar) {
        if (host instanceof PsiPath && host.getText().startsWith("\""))
            registrar.addPlace(host.getLanguage(), TextRange.from(1, host.getTextLength() - 2), "package p; PATH : ", ";");
        else if (host instanceof PsiInterpolation && isInterpolation(host.getText()))
            registrar.addPlace(host.getLanguage(), TextRange.from(1, host.getTextLength() - 2), "package p; INTERPOLATION : ", "INTERPOLATION;");
    }

    //~ Methods ......................................................................................................................................

    /** Return true is file is not injected. */
    public static boolean isNotInjected(@NotNull MMFile file) {
        return !InjectedLanguageManager.getInstance(file.getProject()).isInjectedFragment(file);
    }
}
