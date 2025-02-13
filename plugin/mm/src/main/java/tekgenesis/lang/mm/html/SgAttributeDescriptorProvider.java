
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.html;

import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlAttributeDescriptorsProvider;
import com.intellij.xml.XmlElementDescriptor;
import com.intellij.xml.impl.schema.AnyXmlAttributeDescriptor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.html.XhtmlTags.SgAttribute;

import static tekgenesis.lang.mm.html.XhtmlTags.getAttributeDescriptorsForTag;
import static tekgenesis.lang.mm.html.XhtmlTags.toXhtmlAttribute;

/**
 * Provider for Sui Generis XHTML Custom Attributes.
 */
public class SgAttributeDescriptorProvider implements XmlAttributeDescriptorsProvider {

    //~ Methods ......................................................................................................................................

    @Nullable @Override public XmlAttributeDescriptor getAttributeDescriptor(final String s, final XmlTag xmlTag) {
        return xmlTag != null ? XhtmlTags.getAttributeDescriptor(s) : null;
    }

    @Override public XmlAttributeDescriptor[] getAttributeDescriptors(final XmlTag xmlTag) {
        final XmlElementDescriptor descriptor = xmlTag.getDescriptor();
        if (descriptor instanceof SgElementDescriptorProvider.SgElementDescriptor) return descriptor.getAttributesDescriptors(xmlTag);
        else return getAttributeDescriptorsForTag(xmlTag);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Sui Generis XHTML Attribute Descriptor (like sg-view).
     */
    static class SgAttributeDescriptor extends AnyXmlAttributeDescriptor {
        /** Descriptor constructor. */
        SgAttributeDescriptor(@NotNull final SgAttribute s) {
            super(toXhtmlAttribute(s.name()));
        }
    }
}
