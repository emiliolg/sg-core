
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.postfixTemplate;

import com.intellij.codeInsight.template.postfix.templates.SurroundPostfixTemplateBase;
import com.intellij.lang.surroundWith.Surrounder;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.surround.LoggerSurroundDescriptor.LoggerMode;
import tekgenesis.lang.mm.surround.LoggerSurrounder;

import static java.lang.String.format;

import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.JAVA_PSI_INFO;
import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.selectorTopmost;

import static tekgenesis.lang.mm.surround.LoggerSurrounder.IS_THROWABLE_OR_STRING;

/**
 * Postfix template for Logger class.
 */
public class LoggerPostfixTemplate extends SurroundPostfixTemplateBase {

    //~ Instance Fields ..............................................................................................................................

    private final LoggerMode loggerMode;

    //~ Constructors .................................................................................................................................

    /**  */
    public LoggerPostfixTemplate(LoggerMode loggerMode) {
        super(format("log%s", loggerMode.getLog()),
            format("logger.%s(expr)", loggerMode.getMethod()),
            JAVA_PSI_INFO,
            selectorTopmost(IS_THROWABLE_OR_STRING));
        this.loggerMode = loggerMode;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override protected Surrounder getSurrounder() {
        return new LoggerSurrounder(loggerMode);
    }
}
