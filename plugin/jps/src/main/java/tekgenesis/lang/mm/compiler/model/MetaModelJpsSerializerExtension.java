
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.compiler.model;

import java.util.Collections;
import java.util.List;

import com.intellij.util.xmlb.XmlSerializer;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElement;
import org.jetbrains.jps.model.JpsElementFactory;
import org.jetbrains.jps.model.JpsSimpleElement;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.jetbrains.jps.model.serialization.facet.JpsFacetConfigurationSerializer;
import org.jetbrains.jps.model.serialization.library.JpsSdkPropertiesSerializer;

import tekgenesis.common.core.Constants;
import tekgenesis.lang.mm.MetaModelJpsUtils;

import static tekgenesis.common.core.Constants.INTERNAL_JDK;

/**
 * MetaModel Jps serializer extension.
 */
public class MetaModelJpsSerializerExtension extends JpsModelSerializerExtension {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public List<? extends JpsFacetConfigurationSerializer<?>> getFacetConfigurationSerializers() {
        return Collections.singletonList(new FacetSettingsSerializer());
    }

    @NotNull @Override public List<? extends JpsSdkPropertiesSerializer<?>> getSdkPropertiesSerializers() {
        return Collections.singletonList(new SuiGenerisSdkPropertiesSerializer());
    }

    //~ Inner Classes ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static class FacetSettingsSerializer extends JpsFacetConfigurationSerializer<MetaModelFacetSettings> {
        public FacetSettingsSerializer() {
            super(MetaModelJpsUtils.FACET_SETTINGS_ROLE, "SuiGeneris", "SuiGeneris");
        }

        @Override protected MetaModelFacetSettings loadExtension(@NotNull Element facetElement, String name, JpsElement parent, JpsModule module) {
            return new MetaModelFacetSettingsImpl(XmlSerializer.deserialize(facetElement, MetaModelFacetSettingsState.class));
        }

        @Override protected void saveExtension(MetaModelFacetSettings extension, Element facetElement, JpsModule module) {
            XmlSerializer.serializeInto(((MetaModelFacetSettingsImpl) extension).getState(), facetElement);
        }
    }

    private static class SuiGenerisSdkPropertiesSerializer extends JpsSdkPropertiesSerializer<JpsSimpleElement<SuiGenerisJpsSdkProperties>> {
        protected SuiGenerisSdkPropertiesSerializer() {
            super(Constants.SUI_GENERIS_SDK, SuiGenerisJpsSdkType.INSTANCE);
        }

        @NotNull @Override public JpsSimpleElement<SuiGenerisJpsSdkProperties> loadProperties(Element element) {
            final String jdkName = element != null ? element.getAttributeValue(INTERNAL_JDK) : null;
            return JpsElementFactory.getInstance().createSimpleElement(new SuiGenerisJpsSdkProperties(jdkName));
        }

        @Override public void saveProperties(@NotNull JpsSimpleElement<SuiGenerisJpsSdkProperties> properties, @NotNull Element element) {
            final String jdkName = properties.getData().getJdkName();
            if (jdkName != null) element.setAttribute(INTERNAL_JDK, jdkName);
        }
    }
}  // end class MetaModelJpsSerializerExtension
