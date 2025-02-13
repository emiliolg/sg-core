
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.html;

import java.util.EnumMap;
import java.util.EnumSet;

import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlElementDescriptor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.html.SgAttributeDescriptorProvider.SgAttributeDescriptor;
import tekgenesis.lang.mm.html.SgElementDescriptorProvider.SgElementDescriptor;

import static java.util.EnumSet.of;

import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.lang.mm.html.XhtmlTags.SgAttribute.*;
import static tekgenesis.lang.mm.html.XhtmlTags.SgTag.*;

/**
 * Xhtml tag utils.
 */
public class XhtmlTags {

    //~ Constructors .................................................................................................................................

    private XhtmlTags() {}

    //~ Methods ......................................................................................................................................

    /** Converts enum attribute name to xhtml attribute name. */
    @NotNull public static String toXhtmlAttribute(@NotNull final String enumAttrName) {
        return enumAttrName.toLowerCase().replaceAll("_", "-");
    }

    /** Get attribute descriptor for given name. */
    @Nullable public static XmlAttributeDescriptor getAttributeDescriptor(@NotNull final String attrName) {
        final SgAttribute sgAttribute = SgAttribute.valueFromXhtmlName(attrName);
        return sgAttribute != null ? new SgAttributeDescriptor(sgAttribute) : null;
    }

    /** Get attributes compatible with given tag as descriptors. */
    @NotNull public static XmlAttributeDescriptor[] getAttributeDescriptorsForTag(XmlTag tag) {
        final EnumSet<SgAttribute> sgAttributes = supports.get(SgTag.valueFromXhtmlName(tag.getName()));

        if (sgAttributes != null && !sgAttributes.isEmpty()) {
            sgAttributes.add(ID);
            return attributesAsDescriptors(sgAttributes);
        }
        return attributesAsDescriptors(defaultAttributes);
    }

    /** Get descriptor for given tag element. */
    @Nullable public static XmlElementDescriptor getDescriptorForTag(XmlTag tag) {
        return SgTag.valueFromXhtmlName(tag.getName()) != null ? new SgElementDescriptor(tag.getName(), tag) : null;
    }

    @NotNull private static XmlAttributeDescriptor[] attributesAsDescriptors(@NotNull final EnumSet<SgAttribute> sgAttributes) {
        return map(sgAttributes, SgAttributeDescriptor::new).toArray(XmlAttributeDescriptor[]::new);
    }

    private static void tagSupportsAttributes(SgTag tag, EnumSet<SgAttribute> attrs) {
        supports.put(tag, attrs);
    }

    //~ Static Fields ................................................................................................................................

    private static final EnumMap<SgTag, EnumSet<SgAttribute>> supports          = new EnumMap<>(SgTag.class);
    private static final EnumSet<SgAttribute>                 defaultAttributes = of(SgAttribute.SUI_VIEW, SUI_MESSAGES, SUI_PARAMS);

    static {
        tagSupportsAttributes(SUI_USERNAME, of(HAS_PICTURE, HAS_USERNAME));
        tagSupportsAttributes(SUI_MENU, of(POSITION, FQN, HIDE_ROOT, MAX_MENUS));
        tagSupportsAttributes(SUI_FORMBOX, of(FQN, PK, LISTENER, FOCUS_ON_LOAD, HISTORY, HOME, HIDE_HEADER, HIDE_FOOTER));
        tagSupportsAttributes(SUI_INCLUDE, of(ID, SRC));
        tagSupportsAttributes(SgTag.SUI_VIEW, of(SgAttribute.SUI_VIEW, SUI_PARAMS));
        tagSupportsAttributes(SUI_TEMPLATE, of(SRC));
    }

    //~ Enums ........................................................................................................................................

    /**
     * Sui generis custom attributes.
     */
    enum SgAttribute {
        HAS_PICTURE, HAS_USERNAME, POSITION, FQN, HIDE_ROOT, MAX_MENUS, PK, LISTENER, FOCUS_ON_LOAD, HISTORY, HOME, HIDE_HEADER, HIDE_FOOTER,
        SUI_VIEW, SUI_PARAMS, SUI_MESSAGES, ID, SRC;

        /** Get value of xhtml string as enum value. */
        @Nullable public static SgAttribute valueFromXhtmlName(@NotNull final String xhtmlName) {
            for (final SgAttribute sgAttribute : values())
                if (toXhtmlAttribute(sgAttribute.name()).equals(xhtmlName)) return sgAttribute;
            return null;
        }
    }

    /**
     * Sui generis custom tags.
     */
    enum SgTag {
        SUI_SWITCHUSER, SUI_LOGOUT, SUI_CHANGEOU, SUI_SHORTCUTS, SUI_FEEDBACK, SUI_FULLSCREEN, SUI_USERPROFILE, SUI_TOGGLE, SUI_USERNAME, SUI_FORMBOX,
        SUI_MENU, SUI_INCLUDE, SUI_VIEW, SUI_TEMPLATE;

        /** Get value of xhtml string as enum value. */
        @Nullable public static SgTag valueFromXhtmlName(@NotNull final String xhtmlName) {
            for (final SgTag sgTag : values())
                if (toXhtmlAttribute(sgTag.name()).equals(xhtmlName)) return sgTag;
            return null;
        }
    }
}  // end class XhtmlTags
