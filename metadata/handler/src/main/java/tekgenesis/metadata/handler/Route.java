
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.service.Method;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.TypeField;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.QName.extractQualification;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.field.FieldOption.BODY;
import static tekgenesis.field.FieldOption.CLASS_METHOD;
import static tekgenesis.field.FieldOption.KEY;
import static tekgenesis.field.FieldOption.SECURE_METHOD;
import static tekgenesis.field.FieldOption.SUMMARY;
import static tekgenesis.metadata.handler.SecureMethod.USERNAMEPASSWORD;

/**
 * Represents a route definition.
 */
public class Route extends TypeField {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Body body;

    @NotNull private final List<Parameter> parameters;
    @NotNull private final Seq<Part>       parts;
    @NotNull private final String          path;
    @NotNull private final Signature       signature;

    //~ Constructors .................................................................................................................................

    /** Construct a dumb route: for tests and collision detection. */
    Route(@NotNull String path, @NotNull Seq<Part> parts, @NotNull FieldOptions options) {
        this(path, Types.anyType(), options, parts, Colls.emptyList());
    }

    /** Construct a new Route. */
    Route(@NotNull String path, @NotNull Type type, @NotNull FieldOptions options, @NotNull Seq<Part> parts, @NotNull List<Parameter> parameters) {
        super(path, type, options);
        this.path       = path;
        this.parts      = parts;
        this.parameters = parameters;
        body            = Body.fromType(getOptions().getType(BODY));
        signature       = createSignature();
    }

    //~ Methods ......................................................................................................................................

    /** Get route body. */
    @NotNull public Type getBodyType() {
        return body.getType();
    }

    /** Set route body type (once resolved). */
    public void setBodyType(@NotNull Type body) {
        this.body.setType(body);
    }

    /** Verify is the route is marked as unrestricted.* */
    public boolean isUnrestricted() {
        return getOptions().getBoolean(FieldOption.UNRESTRICTED);
    }

    /** Get http method. */
    @NotNull public Method getHttpMethod() {
        return getOptions().getEnum(FieldOption.METHOD, Method.class, Method.GET);
    }

    /** Get route key. */
    @NotNull public String getKey() {
        return notEmpty(getOptions().getString(KEY), () -> parts.map(r -> capitalizeFirst(r.getName())).mkString(""));
    }

    /** Verify is the route is for internal routing only.* */
    public boolean isInternal() {
        return getOptions().getBoolean(FieldOption.INTERNAL);
    }

    @NotNull @Override public String getLabel() {
        return getSummary();
    }

    /** Get fully qualified class method. */
    @NotNull public String getMethod() {
        return getOptions().getString(CLASS_METHOD);
    }

    /** Get method class name. */
    @NotNull public String getMethodClass() {
        return extractQualification(getMethod());
    }

    /** Get class method name. */
    @NotNull public String getMethodName() {
        return extractName(getMethod());
    }

    /** Get route parameters. */
    @NotNull public Seq<Parameter> getParameters() {
        return Colls.seq(parameters);
    }

    /** Return route parts. */
    @NotNull public Seq<Part> getParts() {
        return parts;
    }

    /** Get absolute route path. */
    @NotNull public String getPath() {
        return path;
    }

    /** Get route security method. */
    @NotNull public SecureMethod getSecureMethod() {
        return getOptions().getEnum(SECURE_METHOD, SecureMethod.class, USERNAMEPASSWORD);
    }

    /** Get route signature. */
    @NotNull public Signature getSignature() {
        return signature;
    }

    /** Get summary description. */
    @NotNull public String getSummary() {
        return getOptions().getString(SUMMARY);
    }

    private Signature createSignature() {
        final Signature s = new Signature(getMethodName());
        for (final Part part : parts) {
            if (part.isDynamic() || part.isWildcard()) s.part(part);
        }

        s.body(body);

        parameters.forEach(s::param);

        return s;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3917905294331012014L;

    //~ Inner Classes ................................................................................................................................

    public class Signature {
        private final List<TypeField> arguments;
        private int                   dynamics;
        private final String          name;

        private Signature(@NotNull String name) {
            this.name = name;
            arguments = new ArrayList<>();
        }

        /** Apply block for each argument. */
        public void forEachArgument(@NotNull Consumer<TypeField> block) {
            for (final TypeField argument : getAllArguments())
                block.accept(argument);
        }

        /** Apply block for each dynamic part. */
        public void forEachPart(@NotNull Consumer<TypeField> block) {
            for (final TypeField argument : getDynamicArguments())
                block.accept(argument);
        }

        /** Return true if signature has body. */
        public boolean hasBody() {
            return body.isDefined();
        }

        /** Hash signature name and all arguments types. */
        public int hashNameAndArgumentTypes() {
            return hashSignature(getAllArguments());
        }

        /** Hash signature name and dynamic parts type only (exclude body and parameters). */
        public int hashNameAndDynamicParts() {
            return hashSignature(getDynamicArguments());
        }

        /** Get signature arguments class names. */
        public Iterable<String> getArgumentClassNames() {
            return map(arguments, value -> extractName(value.getFinalType().getImplementationClassName()));
        }

        /** Get signature name. */
        public String getName() {
            return name;
        }

        /** Get signature type. */
        public Type getType() {
            return Route.this.getType();
        }

        private void body(Body b) {
            if (b.isDefined()) arguments.add(b);
        }

        private int hashSignature(List<TypeField> args) {
            int result = name.hashCode();
            for (final TypeField argument : args)
                result = Constants.HASH_SALT * result + argument.getType().hashCode();
            return result;
        }

        private void param(@NotNull Parameter parameter) {
            arguments.add(parameter);
        }

        private void part(@NotNull Part part) {
            dynamics++;
            arguments.add(part);
        }

        private List<TypeField> getAllArguments() {
            return arguments;
        }

        private List<TypeField> getDynamicArguments() {
            return arguments.subList(0, dynamics);
        }
    }  // end class RouteSignature
}  // end class Route
