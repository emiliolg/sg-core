
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.errors;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.exception.BuilderError;

import static tekgenesis.common.Predefined.cast;

/**
 * Intention Fix provider for given BuilderErrors.
 */
interface FixProvider extends BiFunction<PsiElement, BuilderError, IntentionAction> {

    //~ Methods ......................................................................................................................................

    /** Provide fix intention for given element and error. */
    @Nullable @Override IntentionAction apply(@NotNull PsiElement element, @NotNull BuilderError error);

    //~ Inner Classes ................................................................................................................................

    abstract class TypedFixProvider<T extends PsiElement, E extends BuilderError> implements FixProvider, BiPredicate<PsiElement, BuilderError> {
        @NotNull private final Class<T>  elementClass;
        @NotNull private final Class<E>  errorClass;

        TypedFixProvider(@NotNull final Class<T> elementClass, @NotNull final Class<E> errorClass) {
            this.elementClass = elementClass;
            this.errorClass   = errorClass;
        }

        @Nullable @Override public IntentionAction apply(@NotNull PsiElement element, @NotNull BuilderError error) {
            return test(element, error) ? createFix(cast(element), cast(error)) : null;
        }

        @Override public boolean test(@NotNull PsiElement element, @NotNull BuilderError error) {
            return elementClass.isInstance(element) && errorClass.isInstance(error);
        }

        @Nullable abstract IntentionAction createFix(@NotNull T element, @NotNull E error);
    }
}
