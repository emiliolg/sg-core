
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.codegen.impl.js.JsCodeGenerator;
import tekgenesis.codegen.impl.js.JsElement.*;
import tekgenesis.codegen.impl.js.ScriptGenerator;
import tekgenesis.common.core.Constants;

import static tekgenesis.codegen.JavaGenerationTest.checkFile;
import static tekgenesis.codegen.JavaGenerationTest.outputDir;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.core.Strings.singleQuoted;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class JsGenerationTest {

    //~ Methods ......................................................................................................................................

    @Test public void array() {
        final String scriptName = "array";

        final ScriptGenerator s = createJsCodeGenerator().newScript(scriptName);

        final Function closure = s.function();
        closure.declare("count", "0").asPrivate();
        closure.return_(closure.function().return_("++count"));

        final Array array = s.array().elem(singleQuoted("config")).elem(singleQuoted("routes")).elem(closure);

        s.declare("array", array).asPrivate();

        s.generate();

        checkJsFile(scriptName);
    }

    @Test public void closure() {
        final String scriptName = "closure";

        final ScriptGenerator s = createJsCodeGenerator().newScript(scriptName);

        final Function closure = s.function();
        closure.declare("count", "0").asPrivate();
        closure.return_(closure.function().return_("++count"));

        final DeclareStatement declaration = s.declare("increment", closure.invoke()).asPrivate();

        s.insertInvocation(declaration);
        s.insertInvocation(declaration);

        s.generate();

        checkJsFile(scriptName);
    }

    @Test public void factorial() {
        final String scriptName = "factorial";

        final ScriptGenerator s = createJsCodeGenerator().newScript(scriptName);

        final Function function = s.insertFunction("factorial");
        final Argument arg      = function.arg("n");

        function.startIf(arg.getName() + " == 0").inline().return_("1");

        function.return_(arg.getName() + " * " + s.invoke("", function.getName(), arg.getName() + " - 1"));

        s.insertInvocation(function).arg("3");

        s.generate();

        checkJsFile(scriptName);
    }

    @Test public void module() {
        final String scriptName = "module";

        final ScriptGenerator s = createJsCodeGenerator().newScript(scriptName);

        final InvocationStatement module = s.invocation("module").arg(singleQuoted("productHandler")).arg(s.array().elem(singleQuoted("config")));
        module.target(s.constant("angular"));

        final Function body = s.function().expand();
        body.arg("$http");

        final InvocationStatement get = s.invocation("get");
        get.arg(singleQuoted("/services/search"));
        get.arg(s.object().member("params", s.object().member("q", "query").member("l", "limit")));
        get.target("$http");

        final Function search = s.function().expand();
        search.arg("query");
        search.arg("limit");
        search.return_(get);

        body.return_(s.object().member("search", search));

        final Array parameters = s.array().elem(singleQuoted("$http")).elem(body);

        final InvocationStatement services = s.insertInvocation(s.constant("factory")).arg(singleQuoted("services")).arg(parameters).semicolon();
        services.target(module).newLine();

        s.generate();

        checkJsFile(scriptName);
    }

    @Test public void object() {
        final String scriptName = "object";

        final JsCodeGenerator g = new JsCodeGenerator(outputDir, "tekgenesis.test");

        final ScriptGenerator s = g.newScript(scriptName);

        s.declareObject("car")
            .member("brand", quoted("Toyota"))
            .member("model", quoted("Corolla"))
            .member("year", "2000")
            .member("toString", s.function().return_("this.brand + \" \" + this.model"))
            .asPrivate();

        s.generate();

        checkJsFile(scriptName);
    }

    @NotNull private JsCodeGenerator createJsCodeGenerator() {
        return new JsCodeGenerator(outputDir, "tekgenesis.test");
    }

    //~ Methods ......................................................................................................................................

    private static void checkJsFile(String fileName) {
        checkFile(fileName, JS_EXT, JS_EXT);
    }

    //~ Static Fields ................................................................................................................................

    private static final String JS_EXT = "." + Constants.JS_EXT;
}  // end class JsGenerationTest
