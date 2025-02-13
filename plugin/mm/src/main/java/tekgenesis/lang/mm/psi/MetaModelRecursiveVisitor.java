
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * @author max
 */
package tekgenesis.lang.mm.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWalkingState;

/**
 * MetaModel language psi element visitor.
 */
public abstract class MetaModelRecursiveVisitor extends MetaModelElementVisitor {

    //~ Instance Fields ..............................................................................................................................

    private final PsiWalkingState state = new PsiWalkingState(this) {};

    //~ Methods ......................................................................................................................................

    @Override public void visitElement(PsiElement element) {
        state.elementStarted(element);
    }
}
