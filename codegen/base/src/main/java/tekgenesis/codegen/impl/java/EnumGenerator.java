
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.impl.java;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.IndentedWriter;
import tekgenesis.common.collections.Seq;

import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.util.JavaReservedWords.ENUM;

/**
 * A Generator for Enums.
 */
public class EnumGenerator extends JavaItemGenerator<EnumGenerator> {

    //~ Instance Fields ..............................................................................................................................

    private final List<String> constants;

    //~ Constructors .................................................................................................................................

    protected EnumGenerator(JavaCodeGenerator cg, String name) {
        super(cg, name, ENUM);
        constants = new ArrayList<>();
    }

    EnumGenerator(JavaCodeGenerator cg, String name, Seq<String> enumConstants) {
        this(cg, name);
        enumConstants.into(constants);
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    EnumGenerator(JavaItemGenerator<?> parent, String name, Seq<String> enumConstants) {
        super(parent, name, ENUM);
        // suppress: difficult to detect when there is no body in the enum
        suppressWarnings(quoted("UnnecessarySemicolon"));
        constants = enumConstants.into(new ArrayList<>());
    }

    //~ Methods ......................................................................................................................................

    /** IntellijIdea fails to find enum interfaces unless they are full qualified :(. */
    @Override public EnumGenerator withInterfaces(final Class<?>... implementing) {
        return super.withInterfaces(false, implementing);
    }

    protected void addConstants(Seq<String> enumConstants) {
        enumConstants.into(constants);
    }

    protected void generateBody(@NotNull IndentedWriter w, @NotNull JavaArtifactGenerator ag) {
        boolean notFirst = false;
        for (final String c : constants) {
            if (notFirst) w.println(",");
            notFirst = true;
            w.print(c);
        }
        w.println(";");
        super.generateBody(w, ag);
    }
}  // end class EnumGenerator
