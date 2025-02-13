
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.surround;

import com.intellij.lang.surroundWith.SurroundDescriptor;
import com.intellij.lang.surroundWith.Surrounder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static tekgenesis.lang.mm.surround.LoggerSurroundDescriptor.LoggerMode.DEBUG;
import static tekgenesis.lang.mm.surround.LoggerSurroundDescriptor.LoggerMode.ERROR;

/**
 * Descriptor to log in java with tek Logger class.
 */
public class LoggerSurroundDescriptor implements SurroundDescriptor {

    //~ Methods ......................................................................................................................................

    @Override public boolean isExclusive() {
        return false;
    }

    @NotNull @Override public PsiElement[] getElementsToSurround(PsiFile file, int startOffset, int endOffset) {
        if (startOffset >= endOffset - 1) return EMPTY_ARRAY;
        final PsiElement element = firstNonWhiteElement(file.findElementAt(startOffset), true);
        return new PsiElement[] { element };
    }

    @NotNull @Override public Surrounder[] getSurrounders() {
        return SURROUNDERS;
    }

    //~ Methods ......................................................................................................................................

    @Nullable protected static PsiElement firstNonWhiteElement(PsiElement element, final boolean lookRight) {
        PsiElement result = element;
        if (result instanceof PsiWhiteSpace) result = lookRight ? result.getNextSibling() : result.getPrevSibling();
        return result;
    }

    //~ Static Fields ................................................................................................................................

    private static final Surrounder[] SURROUNDERS = { new LoggerSurrounder(DEBUG), new LoggerSurrounder(ERROR) };

    protected static final PsiElement[] EMPTY_ARRAY = new PsiElement[0];

    //~ Enums ........................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    public enum LoggerMode {
        ERROR("e", "error"), DEBUG("d", "debug");

        private final String log;
        private final String method;

        LoggerMode(String log, String method) {
            this.log    = log;
            this.method = method;
        }

        /** Get final letter for template. */
        public String getLog() {
            return log;
        }
        /** Get method name for template. */
        public String getMethod() {
            return method;
        }
    }
}  // end class LoggerSurroundDescriptor
