
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

import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.core.Strings.toWords;

/**
 * MM Camel Case Macro.
 */
class MMCamelCaseMacro extends Macro {

    //~ Methods ......................................................................................................................................

    public Result calculateQuickResult(@NotNull Expression[] params, ExpressionContext context) {
        if (params.length == 0) return null;
        return camelcasedResult(params[0].calculateQuickResult(context));
    }

    @Override public Result calculateResult(@NotNull Expression[] params, ExpressionContext context) {
        if (params.length == 0) return null;
        return camelcasedResult(params[0].calculateResult(context));
    }

    @Override public String getName() {
        return "camelcase";
    }

    @Override public String getPresentableName() {
        return "Whitespace CamelCase";
    }

    @Override public boolean isAcceptableInContext(TemplateContextType context) {
        return context instanceof MMContextType;
    }

    private TextResult camelcasedResult(Result result) {
        return result != null ? new TextResult(toWords(result.toString())) : null;
    }
}
