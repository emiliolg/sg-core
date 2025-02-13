package tekgenesis.test;

import org.jetbrains.annotations.NotNull;

/** 
 * Generated interface for context: InterfaceWithMultipleExtends.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public interface InterfaceWithMultipleExtends
    extends ImaginaryA, ImaginaryB
{

    //~ Methods ..................................................................................................................

    /** Sets the value of the A. */
    @NotNull InterfaceWithMultipleExtends setA(@NotNull String a);

    /** Returns the A. */
    @NotNull String getA();

    /** Sets the value of the B. */
    @NotNull InterfaceWithMultipleExtends setB(@NotNull String b);

    /** Returns the B. */
    @NotNull String getB();

    /** Sets the value of the C. */
    @NotNull InterfaceWithMultipleExtends setC(@NotNull String c);

    /** Returns the C. */
    @NotNull String getC();

}
