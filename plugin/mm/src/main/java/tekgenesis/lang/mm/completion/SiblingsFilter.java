
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.completion;

import java.util.HashSet;
import java.util.Set;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;

import static tekgenesis.common.collections.ImmutableList.fromArray;

/**
 * Filter for siblings.
 */
interface SiblingsFilter {

    //~ Methods ......................................................................................................................................

    /** Accept {@link LookupElementBuilder} given calculated siblings. */
    default boolean accept(@NotNull final Set<String> siblings, @NotNull final LookupElementBuilder l) {
        return !siblings.contains(l.getLookupString());
    }

    /** Defines sibling allowance. */
    default boolean allow(@NotNull PsiElement element) {
        return !(element instanceof PsiWhiteSpace);
    }
    @NotNull default Seq<LookupElementBuilder> filter(@NotNull Iterable<LookupElementBuilder> lookups, @NotNull PsiElement position) {
        //J-
        final Set<String> siblings = siblings(position)
                .filter(this::allow)
                .map(this::stringify)
                .into(new HashSet<>());

        return Colls.filter(lookups, l -> accept(siblings, l)).toList();
        //J+
    }

    /** Defines siblings resolution. */
    @NotNull default Seq<PsiElement> siblings(@NotNull PsiElement position) {
        return fromArray(position.getParent().getChildren());
    }

    /** Defines sibling stringify. */
    @NotNull default String stringify(PsiElement e) {
        final String[] split = e.getText().split(" ");
        return split.length == 0 ? "" : split[0];
    }
}
