
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
import com.intellij.xml.DefaultXmlExtension;
import com.intellij.xml.XmlElementDescriptor;

import org.jetbrains.annotations.Nullable;

import static tekgenesis.lang.mm.html.XhtmlTags.getDescriptorForTag;

/**
 * Extension for custom.
 */
public class SgXhtmlExtension extends DefaultXmlExtension {

    //~ Methods ......................................................................................................................................

    @Nullable @Override public XmlElementDescriptor getElementDescriptor(XmlTag xmlTag, XmlTag xmlTag1, XmlElementDescriptor xmlElementDescriptor) {
        final XmlElementDescriptor elementDescriptor = super.getElementDescriptor(xmlTag, xmlTag1, xmlElementDescriptor);
        return elementDescriptor != null ? elementDescriptor : getCustomTagDescriptor(xmlTag);
    }

    @Nullable private XmlElementDescriptor getCustomTagDescriptor(XmlTag tag) {
        return getDescriptorForTag(tag);
    }
}
