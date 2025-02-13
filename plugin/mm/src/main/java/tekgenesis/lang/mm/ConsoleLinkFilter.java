
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intellij.execution.filters.Filter;
import com.intellij.ide.browsers.OpenUrlHyperlinkInfo;
import com.intellij.openapi.editor.markup.TextAttributes;

/**
 * Filter to show http url as clickable.
 */
@SuppressWarnings("WeakerAccess")
public class ConsoleLinkFilter implements Filter {

    //~ Methods ......................................................................................................................................

    @Override public Result applyFilter(String s, int endPoint) {
        final int     startPoint = endPoint - s.length();
        final Matcher matcher    = URL_PATTERN.matcher(s);
        if (matcher.find()) return new Result(startPoint + matcher.start(), startPoint + matcher.end(), new OpenUrlHyperlinkInfo(matcher.group(1)));
        else return new Result(startPoint, endPoint, null, new TextAttributes());
    }

    //~ Static Fields ................................................................................................................................

    private static final Pattern URL_PATTERN = Pattern.compile("(https?://[-_.!~*\\\\'()a-zA-Z0-9;\\\\/?:\\\\@&=+\\\\$,%#]+)");
}
