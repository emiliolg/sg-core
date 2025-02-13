
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.formatter;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import static java.lang.Character.*;

/**
 * CustomMask Handler.
 */

public class MaskInputHandler implements Formatter<String>, InputFilter {

    //~ Instance Fields ..............................................................................................................................

    private int currentMask = 0;

    // masks AAA-###
    private final List<String> masks;  // (AAA-###, AA-###-AA)

    //~ Constructors .................................................................................................................................

    MaskInputHandler(List<String> masks) {
        this.masks = masks;
    }

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    public String filter(String value) {
        final List<String> doesNotApply = new ArrayList<>();
        final List<String> applies      = new ArrayList<>();
        int                idx          = 0;
        for (final String mask : masks) {
            final String          inputMask = mask.replaceAll("[^A-Za-z0-9_#]", "");
            final List<Character> result    = new ArrayList<>(value.length());
            int                   maskIdx   = 0;

            for (int i = 0; i < value.length() && result.size() < inputMask.length(); i++) {
                final Character consume = getNextChar(value, inputMask, maskIdx, i);
                if (consume != null) {
                    result.add(consume);
                    maskIdx++;
                }
            }

            final String s = InputFilterUtil.createString(result);
            if (maskIdx == value.length()) {
                if (maskIdx == inputMask.length()) {
                    currentMask = idx;
                    return s;
                }
                if (applies.isEmpty()) currentMask = idx;
                applies.add(s);
            }
            else doesNotApply.add(s);
            idx++;
        }
        return applies.isEmpty() ? doesNotApply.get(0) : applies.get(0);
    }

    @Override public String format(String value) {
        if (value == null) return null;

        String result = value;
        for (int i = 0; i < masks.get(currentMask).length() && i <= value.length(); i++) {
            final char c = masks.get(currentMask).charAt(i);
            if (!isLetterOrDigit(c) && c != '#') result = result.substring(0, i) + c + result.substring(i, result.length());
        }
        return result;
    }

    @Nullable private Character getNextChar(String value, String inputMask, int maskIdx, int i) {
        final char next    = value.charAt(i);
        Character  consume = null;

        switch (inputMask.charAt(maskIdx)) {
        case 'A':
            if (isLetter(next)) consume = toUpperCase(next);
            break;
        case 'a':
            if (isLetter(next)) consume = toLowerCase(next);
            break;
        case '#':
            if (isDigit(next)) consume = next;
            break;
        case 'X':
            if (isLetterOrDigit(next)) consume = toUpperCase(next);
            break;
        case 'x':
            if (isLetterOrDigit(next)) consume = toLowerCase(next);
            break;
        }
        return consume;
    }
}  // end class MaskInputHandler
