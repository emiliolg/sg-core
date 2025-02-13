
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.document.markdown;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Colls;

import static tekgenesis.lang.mm.document.markdown.MarkdownToken.*;

/**
 * Markdown Markup Token.
 */
public final class MarkdownMarkup {

    //~ Constructors .................................................................................................................................

    private MarkdownMarkup() {}

    //~ Methods ......................................................................................................................................

    /** Bold text. */
    public static String bold(@Nullable String text) {
        return format(BOLD, text);
    }

    /**
     * Uses to format a String.
     *
     * <p>Valid formats: bold italic strikethrough underlined</p>
     *
     * @param   token  Format Token
     * @param   text   text
     *
     * @return  Text
     */
    @SuppressWarnings("WeakerAccess")
    public static String format(@NotNull String token, @Nullable String text) {
        return token + text + token;
    }

    /**
     * Heading text h1 h2 h3 h4 h5 h6.
     *
     * @param   hToken  Heading Token
     * @param   text    text
     *
     * @return  Text
     */
    public static String h(@NotNull String hToken, @Nullable String text) {
        return hToken + EMPTY_SPACE + Predefined.notEmpty(text, "");
    }

    /** italic. */
    public static String italic(@Nullable String text) {
        return format(ITALIC, text);
    }

    /**
     * Create List item. This can be called recursive in order to create sub-items
     *
     * @param   token  List token
     * @param   text   text
     *
     * @return  text
     */
    public static String list(@NotNull String token, @NotNull String text) {
        return token + EMPTY_SPACE + text;
    }

    /** table head generation. */
    public static String thead(@NotNull String... columnName) {
        final Set<String> colsSet = Colls.set(columnName);
        String            result  = Colls.mkString(colsSet, "|", "|", "|");
        result += "\n";
        result += Colls.mkString(Colls.map(colsSet, c -> "---"), "|", "|", "|");
        return result;
    }

    /** table row generation. */
    public static String tr(@NotNull String... columnValues) {
        return Colls.mkString(Colls.set(columnValues), "|", "|", "|");
    }

    /** Underline text. */
    public static String underline(@Nullable String text) {
        return format(UNDERLINE, text);
    }

    //~ Static Fields ................................................................................................................................

    private static final String EMPTY_SPACE = " ";
}  // end class MarkdownMarkup
