
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.impl.js;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.impl.js.JsArtifactGenerator.LastLine;
import tekgenesis.common.IndentedWriter;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;

import static tekgenesis.common.Predefined.cast;

/**
 * The base class for generated Items (Modules, Factories) .
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "WeakerAccess", "UnusedReturnValue", "OverlyComplexClass" })
public abstract class JsItemGenerator<T extends JsItemGenerator<T>> extends JsElement<T> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull protected final JsArtifactGenerator artifactGenerator;
    @NotNull protected final List<JsElement<?>>  elements;

    //~ Constructors .................................................................................................................................

    /** Constructor for top level items. */
    protected JsItemGenerator(@NotNull JsCodeGenerator cg, String name, @NotNull String type) {
        this(cg.newArtifactGenerator(name), name, type);
    }

    protected JsItemGenerator(@NotNull JsArtifactGenerator ag, @NotNull String nm, @NotNull String type) {
        super(nm, type);
        artifactGenerator = ag;
        elements          = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    /** Returns an array {@link Array}. */
    public Array array() {
        return new Array();
    }

    /** Creates a constant {@link Constant}. */
    public Constant constant(@NotNull String constant) {
        return new Constant(constant);
    }

    /** Create and insert inline declaration. */
    public DeclareStatement declare(@NotNull String name, @NotNull String value) {
        return declare(name, new Constant(value));
    }

    /** Create and insert declaration of given statement. */
    public DeclareStatement declare(@NotNull String name, @NotNull JsElement<?> statement) {
        final DeclareStatement declaration = new DeclareStatement(name, statement);
        elements.add(declaration);
        return declaration;
    }

    /** Declares a variable assigned to closure function {@link Function}. */
    public Function declareFunction(@NotNull String name) {
        final Function function = function();
        declare(name, function);
        return function;
    }

    /** Declares a variable assigned to object {@link Object}. */
    public Object declareObject(@NotNull String name) {
        final Object object = object();
        declare(name, object);
        return object;
    }

    /** Creates an anonymous function {@link Function}. */
    public Function function() {
        final Function f = function("");
        f.asAnonymous();
        return f;
    }

    /** Creates a named function {@link Function}. */
    public Function function(@NotNull String functionName) {
        return new Function(artifactGenerator, functionName);
    }

    /** Generate the source code. */
    public void generate() {
        artifactGenerator.generate(this);
    }

    /** Generate the source code if the file is not present. */
    @SuppressWarnings("UnusedReturnValue")
    public final boolean generateIfAbsent() {
        final boolean generate = !getTargetFile().exists();
        if (generate) generate();
        return generate;
    }

    /** Generate the source code if the file does not exists or is older than the specified file. */
    public final boolean generateIfOlder(File source) {
        final File    target   = getTargetFile();
        final boolean generate = !target.exists() || target.lastModified() < source.lastModified();
        if (generate) generate();
        return generate;
    }

    /** Creates and inserts a named function {@link Function}. */
    public Function insertFunction(@NotNull String name) {
        final Function f = function(name);
        elements.add(f);
        return f;
    }

    /** Create and inserts function invocation statement. */
    public InvocationStatement insertInvocation(String function) {
        return insertInvocation(new Constant(function));
    }

    /** Create and inserts function invocation statement. */
    public InvocationStatement insertInvocation(JsElement<?> element) {
        final InvocationStatement invocation = invocation(element).semicolon();
        elements.add(invocation);
        return invocation;
    }

    /** Creates function invocation statement. */
    public InvocationStatement invocation(String function) {
        return invocation(new Constant(function));
    }

    /** Creates function invocation statement (invokes element name). */
    public InvocationStatement invocation(JsElement<?> function) {
        return new InvocationStatement(function);
    }

    /** Invoke a Method. */
    public String invoke(@NotNull String method) {
        return invoke("", method);
    }

    /** Invoke a Method. */
    public String invoke(@NotNull String target, @NotNull String method, String... args) {
        return invoke(target, method, ImmutableList.fromArray(args));
    }

    /** Invoke a Method. */
    public String invoke(String target, String method, Iterable<String> args) {
        return target + (target.isEmpty() ? "" : ".") + method + Colls.mkString(args, "(", ", ", ")");
    }

    /** Returns an object {@link Object}. */
    public Object object() {
        return new Object(artifactGenerator);
    }

    /** Return statement. */
    public T return_(@NotNull String expr) {
        return return_(new Constant(expr));
    }

    /** Return statement. */
    public T return_(@NotNull JsElement<?> element) {
        elements.add(new ReturnStatement(element));
        return cast(this);
    }

    /** Start if block. */
    public If startIf(@NotNull String condition) {
        final If block = new If(artifactGenerator, new Constant(condition));
        elements.add(block);
        return block;
    }

    /** Statement. */
    public void statement(@NotNull String value) {
        elements.add(new LineStatement(value));
    }

    /** return the target File of the Item. */
    public File getTargetFile() {
        return artifactGenerator.getTargetFile();
    }

    /** Generate. */
    protected void generate(IndentedWriter w, @NotNull JsArtifactGenerator ag, LastLine visit) {
        populate();
        generateElements(w, ag, visit);
    }

    /** Populate the generator. */
    protected void populate() {}

    private void generateElements(IndentedWriter w, @NotNull JsArtifactGenerator ag, LastLine visit) {
        for (final JsElement<?> element : elements) {
            element.generate(w, ag, visit);
            w.newLine();
        }
    }
}  // end class JsItemGenerator
