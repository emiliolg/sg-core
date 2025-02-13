
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.highlight;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.options.colors.AttributesDescriptor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.parser.Highlight;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.*;
import static com.intellij.openapi.editor.HighlighterColors.BAD_CHARACTER;
import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

import static tekgenesis.parser.Highlight.*;

/**
 * MM Attribute keys for each token kind.
 */
public class MMHighlighterColors {

    //~ Constructors .................................................................................................................................

    private MMHighlighterColors() {}

    //~ Methods ......................................................................................................................................

    /** Returns font Type for this Type(used in Highlighting). */
    public static int getFontStyleForWidgetType(@Nullable WidgetType type) {
        return type == WidgetType.INTERNAL ? 2 : -1;
    }

    static TextAttributesKey forKind(Highlight kind) {
        return map.get(kind);
    }

    private static void addHighlighting(@NotNull Highlight kind, @Nullable TextAttributesKey fallback) {
        final TextAttributesKey tak = createTextAttributesKey("MM_" + kind.name(), fallback);
        map.put(kind, tak);
        TAG_HIGHLIGHTING_MAP.put(kind.name().toLowerCase(), tak);
    }

    //~ Static Fields ................................................................................................................................

    private static final EnumMap<Highlight, TextAttributesKey> map = new EnumMap<>(Highlight.class);

    public static final Map<String, TextAttributesKey> TAG_HIGHLIGHTING_MAP = new HashMap<>();

    public static final AttributesDescriptor[] ATTRIBUTES;

    static {
        addHighlighting(KEYWORD_H, KEYWORD);
        addHighlighting(TYPE_H, KEYWORD);
        addHighlighting(WIDGET_H, null);
        addHighlighting(OPTION_H, null);
        addHighlighting(FIELD_REF_H, null);
        addHighlighting(REFERENCE_H, NUMBER);
        addHighlighting(STRING_H, STRING);
        addHighlighting(NUMBER_H, NUMBER);
        addHighlighting(OPERATOR_H, OPERATION_SIGN);
        addHighlighting(COMMENT_H, LINE_COMMENT);
        addHighlighting(DOCUMENTATION_H, METADATA);
        addHighlighting(BAD_CHAR_H, BAD_CHARACTER);

        ATTRIBUTES = new AttributesDescriptor[map.size()];
        int i = 0;
        for (final TextAttributesKey key : map.values())
            ATTRIBUTES[i++] = new AttributesDescriptor(MMBundle.message(key.getExternalName().toLowerCase()), key);
    }
}  // end class MMHighlighterColors
