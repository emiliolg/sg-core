
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.postfixTemplate;

import java.util.HashSet;
import java.util.Set;

import com.intellij.codeInsight.template.postfix.templates.JavaPostfixTemplateProvider;
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.lang.mm.surround.LoggerSurroundDescriptor.LoggerMode.DEBUG;
import static tekgenesis.lang.mm.surround.LoggerSurroundDescriptor.LoggerMode.ERROR;

/**
 * Return custom java postfix templates.
 */
public class CustomJavaPostfixTemplateProvider extends JavaPostfixTemplateProvider {

    //~ Instance Fields ..............................................................................................................................

    private final Set<PostfixTemplate> templates;

    //~ Constructors .................................................................................................................................

    /** Provider default constructor. */
    public CustomJavaPostfixTemplateProvider() {
        templates = new HashSet<>();
        templates.add(new LoggerPostfixTemplate(DEBUG));
        templates.add(new LoggerPostfixTemplate(ERROR));
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Set<PostfixTemplate> getTemplates() {
        return templates;
    }
}
