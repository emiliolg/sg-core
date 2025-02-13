
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import java.util.List;
import java.util.function.Function;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.entity.StructType;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;
import tekgenesis.type.Types;
import tekgenesis.type.UnresolvedTypeReference;

import static tekgenesis.codegen.html.HtmlFactoryCodeGenerator.*;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Constants.STRING;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Strings.split;
import static tekgenesis.lang.mm.psi.PsiUtils.getModelRepository;
import static tekgenesis.type.Types.referenceType;

/**
 * XHTML Files annotator.
 */
public class XHTMLAnnotator implements Annotator {

    //~ Methods ......................................................................................................................................

    @Override public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof XmlAttribute)) return;
        final XmlAttribute      attribute    = (XmlAttribute) element;
        final XmlAttributeValue valueElement = attribute.getValueElement();
        final String            params       = attribute.getValue();

        if (valueElement == null || (!SG_PARAMS.toString().equals(attribute.getName()) && !SUI_PARAMS.toString().equals(attribute.getName())) ||
            !isNotEmpty(params)) return;

        getModelRepository(element).ifPresent(r ->
                split(params, ',').forEach(param -> resolveParameterType(split(param, ':'), r, valueElement, holder)));
    }

    private void annotateElement(PsiElement element, AnnotationHolder holder, String param, String type)
    {
        holder.createErrorAnnotation(element, String.format(VIEWS_INVALID_ARGUMENT_TYPE, type, param));
    }

    private <T extends MetaModel> Type attemptTypeFromRepository(String typeName, Class<T> c, ModelRepository repository) {
        return repository.getModel(createQName(typeName), c)
               .map((Function<MetaModel, Type>) XHTMLAnnotator::metaModelToType)
               .orElse(Types.nullType());
    }

    private void resolveParameterType(@NotNull List<String> parts, ModelRepository repository, XmlAttributeValue valueElement,
                                      AnnotationHolder holder) {
        final String typePart = parts.size() > 1 ? parts.get(1).trim() : STRING;
        final String typeName = typePart.endsWith("*") ? typePart.substring(0, typePart.length() - 1) : typePart;
        if (Types.fromString(typeName).isNull() && !Types.htmlType().toString().equals(typeName)) {
            final Type attemptTypeFromRepository = attemptTypeFromRepository(typeName, StructType.class, repository);
            if (attemptTypeFromRepository.isNull()) annotateElement(valueElement, holder, parts.get(0), typeName);
        }
    }  // end method resolveParameterType

    //~ Methods ......................................................................................................................................

    @NotNull private static UnresolvedTypeReference metaModelToType(final MetaModel mm) {
        return referenceType(mm.getFullName());
    }
}  // end class XHTMLAnnotator
