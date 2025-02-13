
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.IndentedWriter;
import tekgenesis.common.core.Constants;

import static java.lang.String.format;

import static tekgenesis.codegen.CodeGeneratorConstants.BOOLEAN_GETTER_COMMENT;
import static tekgenesis.codegen.CodeGeneratorConstants.GETTER_COMMENT;
import static tekgenesis.codegen.CodeGeneratorConstants.SETTER_COMMENT;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Constants.BOOLEAN;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.common.core.Strings.toWords;

/**
 * Elements containing a name and type.
 */
public class Element<T extends Element<T>> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final List<String> comments;
    @NotNull private final String       name;
    @NotNull private String             type;

    //~ Constructors .................................................................................................................................

    protected Element(@NotNull String nm, @NotNull String t) {
        name     = nm;
        type     = t;
        comments = new LinkedList<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public String toString() {
        return getName();
    }

    /** Add comments to the element. */
    public T withComments(@NotNull final String... comment) {
        Collections.addAll(getComments(), comment);
        return cast(this);
    }

    /** Add the default getter comment to the element. */
    public T withGetterComments(String fieldName) {
        return withComments(format(isBoolean(getType()) ? BOOLEAN_GETTER_COMMENT : GETTER_COMMENT, toWords(fromCamelCase(fieldName))));
    }

    /** Add the default setter comment to the element. */
    public T withSetterComments(String fieldName) {
        return withComments(format(SETTER_COMMENT, toWords(fromCamelCase(fieldName))));
    }

    /** Returns true if the type is an String. */
    public boolean isString() {
        return getType().equals(Constants.STRING) || getType().equals(String.class.getName());
    }

    /** The name of the Element being generated. */
    @NotNull public String getName() {
        return name;
    }

    /** Return the type of the Element. */
    @NotNull public String getType() {
        return type;
    }

    protected void comments(IndentedWriter writer) {
        if (!comments.isEmpty()) {
            final String open  = "/** ";
            final String close = " */";

            if (comments.size() == 1) writer.print(open).print(comments.get(0)).println(close);
            else {
                boolean first = true;
                for (final String comment : comments) {
                    if (first) {
                        writer.println(open);
                        first = false;
                    }
                    writer.print(" * ").println(comment);
                }
                writer.println(close);
            }
        }
    }  // end method comments

    @NotNull protected List<String> getComments() {
        return comments;
    }

    protected boolean isBoolean(String f) {
        return f.toLowerCase().equals(BOOLEAN.toLowerCase()) || f.equals(Boolean.class.getName());
    }

    protected void setType(@NotNull String type) {
        this.type = type;
    }
}
