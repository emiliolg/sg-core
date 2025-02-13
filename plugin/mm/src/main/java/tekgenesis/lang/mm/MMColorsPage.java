
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.util.Map;

import javax.swing.*;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.highlight.MMHighlighterColors;
import tekgenesis.lang.mm.highlight.MMSyntaxHighlighter;

/**
 * The Configuration Page.
 */
class MMColorsPage implements ColorSettingsPage {

    //~ Methods ......................................................................................................................................

    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return MMHighlighterColors.TAG_HIGHLIGHTING_MAP;
    }

    @NotNull public AttributesDescriptor[] getAttributeDescriptors() {
        return MMHighlighterColors.ATTRIBUTES;
    }

    @NotNull public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull public String getDemoText() {
        return
            "package sales;\n" +
            "\n" +
            "//Entity definition example\n" +
            "<keyword_h>entity</keyword_h> Customer \"Example entity\"\n" +
            "    <keyword_h>primary_key</keyword_h> <field_ref_h>id</field_ref_h>, <field_ref_h>attribute</field_ref_h>\n" +
            "    <keyword_h>searchable</keyword_h> <field_ref_h>attribute</field_ref_h>\n" +
            "{\n" +
            "    id \"Id\": <type_h>Decimal</type_h>(10,2);\n" +
            "    attribute \"Attribute\": <type_h>String</type_h>(25), <option_h>default</option_h> \"Some Value\";\n" +
            "}\n" +
            "\n" +
            "//Form definition example\n" +
            "<keyword_h>form</keyword_h> CustomerForm \"Example Form\"\n" +
            "    <keyword_h>entity</keyword_h> Customer\n" +
            "{\n" +
            "    <widget_h>header</widget_h> {\n" +
            "        <widget_h>message(title)</widget_h>;\n" +
            "    };\n" +
            "    <reference_h>id</reference_h>, <widget_h>internal</widget_h>, <option_h>optional</option_h>;\n" +
            "    \"Some Attribute\" : <reference_h>attribute</reference_h>;\n" +
            "    partner \"Partner\" : <reference_h>Customer</reference_h>, <option_h>on_suggest</option_h> <reference_h>search</reference_h>, <option_h>optional</option_h>;\n" +
            "\n" +
            "    <widget_h>footer</widget_h> {\n" +
            "        <widget_h>button(save)</widget_h>;\n" +
            "        <widget_h>button(cancel)</widget_h>;\n" +
            "        <widget_h>button(delete)</widget_h>, <option_h>style</option_h> \"pull-right\";\n" +
            "    };\n" +
            "}";
    }

    @NotNull public String getDisplayName() {
        return MMPluginConstants.META_MODEL;
    }

    @NotNull @Override public SyntaxHighlighter getHighlighter() {
        return new MMSyntaxHighlighter();
    }

    public Icon getIcon() {
        return MMFileType.INSTANCE.getIcon();
    }
}  // end class MMColorsPage
