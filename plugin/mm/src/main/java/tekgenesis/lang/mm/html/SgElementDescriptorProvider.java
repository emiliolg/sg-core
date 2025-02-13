
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.html;

import java.util.List;

import com.intellij.codeInsight.completion.XmlTagInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlDocumentImpl;
import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ArrayUtil;
import com.intellij.xml.*;
import com.intellij.xml.impl.schema.AnyXmlElementDescriptor;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.html.XhtmlTags.SgTag;

import static com.intellij.util.containers.ContainerUtil.find;

import static tekgenesis.lang.mm.html.XhtmlTags.getAttributeDescriptorsForTag;
import static tekgenesis.lang.mm.html.XhtmlTags.toXhtmlAttribute;

/**
 * Element descriptor provider for sui generis custom tags.
 */
public class SgElementDescriptorProvider implements XmlElementDescriptorProvider, XmlTagNameProvider {

    //~ Methods ......................................................................................................................................

    @Override public void addTagNameVariants(List<LookupElement> list, @NotNull XmlTag xmlTag, String s) {
        for (final SgTag sgTag : SgTag.values())
            addLookupItem(list, toXhtmlAttribute(sgTag.name()));
    }
    @Nullable @Override public XmlElementDescriptor getDescriptor(XmlTag xmlTag) {
        final XmlNSDescriptor      nsDescriptor = xmlTag.getNSDescriptor(xmlTag.getNamespace(), false);
        final XmlElementDescriptor descriptor   = nsDescriptor != null ? nsDescriptor.getElementDescriptor(xmlTag) : null;
        if (descriptor != null && !(descriptor instanceof AnyXmlElementDescriptor)) return null;

        return XhtmlTags.getDescriptorForTag(xmlTag);
    }

    //~ Methods ......................................................................................................................................

    private static void addLookupItem(List<LookupElement> elements, String tag) {
        elements.add(LookupElementBuilder.create(tag).withInsertHandler(XmlTagInsertHandler.INSTANCE));
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Xhtml Element descriptor for custom Sui Generis tags.
     */
    static class SgElementDescriptor implements XmlElementDescriptor {
        protected final String                 myName;
        private final XmlAttributeDescriptor[] myAttributes;
        private final PsiElement               myDeclaration;

        /** Element Descriptor constructor. */
        SgElementDescriptor(String name, XmlTag declaration) {
            myName        = name;
            myDeclaration = declaration;
            myAttributes  = getAttributeDescriptorsForTag(declaration);
        }

        @Override public void init(PsiElement element) {}

        @Nullable @Override public XmlAttributeDescriptor getAttributeDescriptor(XmlAttribute attribute) {
            return getAttributeDescriptor(attribute.getName(), attribute.getParent());
        }

        @Nullable @Override public XmlAttributeDescriptor getAttributeDescriptor(@NonNls final String attributeName, @Nullable XmlTag context) {
            return find(getAttributesDescriptors(context), descriptor -> attributeName.equals(descriptor.getName()));
        }

        @Override public XmlAttributeDescriptor[] getAttributesDescriptors(@Nullable XmlTag context) {
            return myAttributes;
        }

        @Override public int getContentType() {
            return CONTENT_TYPE_ANY;
        }

        @Override public PsiElement getDeclaration() {
            return myDeclaration;
        }

        @Override public String getDefaultName() {
            return myName;
        }

        @Nullable @Override public String getDefaultValue() {
            return null;
        }

        @Override public Object[] getDependences() {
            return ArrayUtil.EMPTY_OBJECT_ARRAY;
        }

        @Override public XmlElementDescriptor getElementDescriptor(XmlTag childTag, XmlTag contextTag) {
            final XmlTag parent = contextTag.getParentTag();
            if (parent == null) return null;
            final XmlNSDescriptor descriptor = parent.getNSDescriptor(childTag.getNamespace(), true);
            return descriptor == null ? null : descriptor.getElementDescriptor(childTag);
        }

        @Override public XmlElementDescriptor[] getElementsDescriptors(XmlTag context) {
            final XmlDocumentImpl xmlDocument = PsiTreeUtil.getParentOfType(context, XmlDocumentImpl.class);
            if (xmlDocument == null) return EMPTY_ARRAY;
            return xmlDocument.getRootTagNSDescriptor().getRootElementsDescriptors(xmlDocument);
        }

        @Override public String getName() {
            return myName;
        }

        @Override public String getName(PsiElement context) {
            return getName();
        }

        @Nullable @Override public XmlNSDescriptor getNSDescriptor() {
            return null;
        }

        @Override public String getQualifiedName() {
            return myName;
        }

        @Nullable @Override public XmlElementsGroup getTopGroup() {
            return null;
        }
    }  // end class SgElementDescriptor
}  // end class SgElementDescriptorProvider
