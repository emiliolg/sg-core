
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.psi.*;
import tekgenesis.repository.ModelRepository;

import static java.util.Arrays.asList;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.core.Constants.NOT_FOUND;
import static tekgenesis.lang.mm.ProjectUtils.getAllModules;

/**
 * Test using a project for psi functionality.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class PsiProjectTest extends MMProjectTest {

    //~ Methods ......................................................................................................................................

    /** test number of MMFiles in Project. */
    public void testAmountOfMMFiles() {
        final List<MMFile> allMMFilesInProject = ProjectUtils.getAllMMFilesInProject(myProject);
        assertEquals("Unexpected amount of mm files:", 7, allMMFilesInProject.size());
    }

    /** test number of entities. */
    @SuppressWarnings("MagicNumber")
    public void testEntities() {
        final Collection<PsiEntity> entities = getModelsByType(PsiEntity.class);
        assertEquals("Unexpected amount of entities:", 14, entities.size());
    }

    /** test reference to an entity. */
    public void testEntityReference() {
        final PsiEntityField field = getEntity("Articulo").getFieldNullable("familia");
        assert field != null;
        final PsiElement firstChild = field.getPsiType().getFirstChild();
        assertThat(firstChild).isInstanceOf(PsiTypeReference.class);
        final PsiElement element = firstChild.getFirstChild();
        assertThat(element).isInstanceOf(PsiMetaModelCodeReferenceElement.class);
        final PsiReference reference = element.getReference();
        assertThat(reference).isInstanceOf(PsiMetaModelCodeReference.class);
        assert reference != null;
        assertEquals("Type not resolved", getEntity("Familia"), reference.resolve());
    }

    /** test get Enum Method. */
    public void testEnum() {
        final Option<MMFile> mmFileinProject = ProjectUtils.findMmFileInProject(myProject, TestMMGraph.ESSOS, TestMMGraph.TARGARYEN);
        assertTrue(mmFileinProject.isPresent());
        final MMFile  mmFile  = mmFileinProject.get();
        final PsiEnum dragons = ensureNotNull(mmFile.getEnum("Dragons"));
        assertEquals("Enum not found Correctly", dragons.getName(), "Dragons");
        final List<PsiEnumField> values = asList(dragons.getFields());
        assertEquals("Amount of Values Incorrect", values.size(), 3);
        assertEquals("Value incorrect", "Viserion", values.get(0).getName());
        assertEquals("Value incorrect", "Rhaegal", values.get(1).getName());
        assertEquals("Value incorrect", "Drogon", values.get(2).getName());
        final PsiModelField drogon = dragons.getFieldNullable("Drogon");
        if (drogon != null) assertEquals("Value incorrect", "Drogon", drogon.getName());
        assertEquals("Icon Incorrect", MMFileType.ENUM_ICON, dragons.getIcon(0));
    }

    /** test reference to an enum. */
    public void testEnumReference() {
        final PsiEntityField field = getEntity("Articulo").getFieldNullable("estado");
        assert field != null;
        final PsiElement firstChild = field.getPsiType().getFirstChild();
        assertThat(firstChild).isInstanceOf(PsiTypeReference.class);
        final PsiElement element = firstChild.getFirstChild();
        assertThat(element).isInstanceOf(PsiMetaModelCodeReferenceElement.class);
        final PsiReference reference = element.getReference();
        assertThat(reference).isInstanceOf(PsiMetaModelCodeReference.class);
        assert reference != null;
        assertEquals("Type not resolved", getEnum("Estado"), reference.resolve());
    }

    /** test number of fields. */
    public void testFields() {
        final PsiEntityField[] attributes = getEntity("Articulo").getFields();
        assertEquals("Unexpected amount of fields for Articulo", 6, attributes.length);
    }

    /** Test form fields. */
    public void testFormFields() {
        final Seq<PsiWidget> formFields = getForm("CustomerForm").getWidgets();
        assertEquals("Unexpected amount of fields for Form", 4, formFields.size());
        final Seq<PsiWidget> formFields2 = getForm("InvoiceForm").getWidgets();
        assertEquals("Unexpected amount of fields for Form", 14, formFields2.size());
    }

    /** test forms. */
    public void testForms() {
        final Collection<PsiForm> forms = getModelsByType(PsiForm.class);

        assertEquals("Unexpected amount of forms:", 10, forms.size());
    }
    /** Test Inner Entities. */
    public void testGetEntity() {
        final Option<MMFile> mmFileOption  = ProjectUtils.findMmFileInProject(myProject, "westeros", "Lanisters");
        final Option<MMFile> mmFileOption2 = ProjectUtils.findMmFileInProject(myProject, "catalog", "Invoice");
        assertTrue(mmFileOption.isPresent() && mmFileOption2.isPresent());
        assertEquals("Inner Entity is Wrong", ensureNotNull(mmFileOption.get().getEntity("Provisions")).getName(), "Provisions");
        assertEquals("Inner Entity is Wrong", ensureNotNull(mmFileOption2.get().getEntity("Payment")).getName(), "Payment");
        assertEquals("InnerInner Entity is Wrong", ensureNotNull(mmFileOption.get().getEntity("Saddle")).getName(), "Saddle");
    }
    /** test number of enums. */
    public void testGetEnums() {
        int amountEnums = 0;
        for (final MMFile mmFile : ProjectUtils.getAllMMFilesInProject(myProject))
            amountEnums += mmFile.getEnums().length;
        assertEquals("Unexpected amount of models in Files", 7, amountEnums);
    }
    /** test Method getMMComposite. */
    public void testGetMMComposite() {
        final Option<MMFile> mmFileOption = ProjectUtils.findMmFileInProject(myProject, "catalog", "Invoice");
        assertTrue(mmFileOption.isPresent());
        final MMFile mmFile = mmFileOption.get();
        assertEquals("Unable to find MMcompisite in File",
            "Invoice",
            ensureNotNull(mmFile.getMetaModel("Invoice").orElse(null), "Invoice should not be null").getName());
        assertEquals("Unable to find MMcompisite in File",
            "PaymentType",
            ensureNotNull(mmFile.getMetaModel("PaymentType").orElse(null), "PaymentType should not be null").getName());
        assertEquals("Unable to find MMcompisite in File",
            "InvoiceForm",
            ensureNotNull(mmFile.getMetaModel("InvoiceForm").orElse(null), "InvoiceForm should not be null").getName());
        assertEquals("Unable to find MMcompisite in File",
            "PaymentOption",
            ensureNotNull(mmFile.getMetaModel("PaymentOption").orElse(null), "PaymentOption should not be null").getName());
        assertEquals("Unable to find MMcompisite in File",
            "Payment",
            ensureNotNull(mmFile.getMetaModel("Payment").orElse(null), "PAyment should not be null").getName());
    }

    /** test Method getModel. */
    public void testGetModel() {
        final Option<MMFile> mmFileOption = ProjectUtils.findMmFileInProject(myProject, "westeros", "Starks");
        assertTrue(mmFileOption.isPresent());
        final MMFile mmFile = mmFileOption.get();
        for (final PsiMetaModel<?> model : mmFile.getMetaModels()) {
            final String name = model.getName();
            assertEquals("Unable to find Model in File", model, mmFile.getMetaModel(name).get());
        }
    }
    /** test ModelRepository Getter. */
    public void testGetModelRepository() {
        final Option<MMFile> mmFileinProject = ProjectUtils.findMmFileInProject(myProject, TestMMGraph.ESSOS, TestMMGraph.TARGARYEN);
        assertTrue(mmFileinProject.isPresent());
        final MMFile          mmFile          = mmFileinProject.get();
        final ModelRepository modelRepository = mmFile.getModelRepository();
        assertTrue(modelRepository.getModels().size() >= 0);
        assertEquals("Invalid Amount of Domains", 4, modelRepository.getDomains().filter(s -> s != null && !s.startsWith("tekgenesis.")).size());
        assertEquals("Invalid Amount of Models", 36, modelRepository.getModels().size());
    }

    /** test number of entities. */
    @SuppressWarnings("MagicNumber")
    public void testModels() {
        int amountModelsRepository = 0;
        for (final Module module : getAllModules(myProject))
            amountModelsRepository += PsiUtils.getModelRepository(module).getModels().size();
        assertEquals("Unexpected amount of models in Repository:", 36, amountModelsRepository);
        int amountModelsFiles = 0;
        for (final MMFile mmFile : ProjectUtils.getAllMMFilesInProject(myProject))
            amountModelsFiles += mmFile.getMetaModels().size();
        assertEquals("Unexpected amount of models in Files", amountModelsRepository, amountModelsFiles);
    }

    /** test number of modules. */
    public void testModules() {
        assertEquals("Unexpected amount of modules:", 1, getAllModules(myProject).size());
    }
    /** test Method getPathToSource. */
    public void testPathToSource() {
        final Option<MMFile> mmFileOption = ProjectUtils.findMmFileInProject(myProject, "westeros", "Starks");
        assertTrue(mmFileOption.isPresent());
        final MMFile mmFile = mmFileOption.get();
        assertEquals("invalid path to source", "westeros", mmFile.getPathToSourceRoot());
    }

    /** test type for attribute. */
    public void testType() {
        final PsiEntityField field = getEntity("Articulo").getFieldNullable("codadm");
        assert field != null;
        assertEquals("Error for type name", "Decimal(10)", field.getTypeName());
    }

    @Override protected String getProjectPath() {
        return TestMMGraph.PLUGIN_MM_TEST_PROJECT;
    }

    private PsiEntity getEntity(String name) {
        return ensureNotNull(getModelByName(PsiEntity.class, name));
    }
    private PsiEnum getEnum(String name) {
        return ensureNotNull(getModelByName(PsiEnum.class, name));
    }
    private PsiForm getForm(String name) {
        return ensureNotNull(getModelByName(PsiForm.class, name));
    }

    private <T extends PsiMetaModel<?>> T getModelByName(Class<T> clazz, String name) {
        final Iterable<T> models = getModelsByType(clazz);
        for (final T model : models) {
            if (name.equals(model.getName())) return model;
        }
        fail(clazz.getName() + " " + name + NOT_FOUND);
        return null;
    }

    private <T extends PsiMetaModel<?>> Collection<T> getModelsByType(Class<T> clazz) {
        final ArrayList<T> result = new ArrayList<>();
        for (final MMFile mmFile : ProjectUtils.getAllMMFilesInProject(myProject))
            result.addAll(asList(mmFile.getModelsByType(clazz)));
        return result;
    }
}  // end class PsiProjectTest
