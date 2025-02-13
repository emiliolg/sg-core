
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.completion;

import java.util.function.Function;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;

/**
 * Siblings {@link LookupElement} decorator.
 */
interface SiblingsDecorator extends Function<LookupElementBuilder, LookupElement> {

    //~ Inner Classes ................................................................................................................................

    class Default implements SiblingsDecorator {
        @Override public LookupElement apply(LookupElementBuilder element) {
            return element;
        }
    }
}
