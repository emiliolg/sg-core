
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.tree.TokenSet;
import com.intellij.testFramework.PsiTestUtil;

import org.junit.Test;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.common.util.Files;
import tekgenesis.field.FieldOption;
import tekgenesis.lang.mm.psi.*;
import tekgenesis.lang.mm.psi.PsiType;
import tekgenesis.mmcompiler.ast.MMToken;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.collections.Colls.map;

/**
 * Plugin References Test.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JUnit4AnnotatedMethodInJUnit3TestCase", "JavaDoc", "ConstantConditions" })
public class ReferencesTest extends MMPsiTestCase {

    //~ Instance Fields ..............................................................................................................................

    private PsiJavaFileImpl java = null;

    private MMFile mm = null;

    //~ Methods ......................................................................................................................................

    @Test public void testEntityHighLevelOptions() {
        final PsiEntity entity = mm.getEntity("A");

        final PsiEntityField a = entity.getFieldNullable("a");
        final PsiEntityField b = entity.getFieldNullable("b");
        final PsiEntityField c = entity.getFieldNullable("c");
        final PsiEntityField d = entity.getFieldNullable("d");

        final List<PsiFieldReference> pks = getFieldReferences(entity.getHighLevelOption(MMToken.PRIMARY_KEY));
        assertThat(map(pks, PsiFieldReference::getReference).map(r -> r.resolve())).containsExactly(a, b);

        final List<PsiFieldReference> indexes = getFieldReferences(entity.getHighLevelOption(MMToken.INDEX));
        assertThat(map(indexes, PsiFieldReference::getReference).map(r -> r.resolve())).containsExactly(a, b, c);

        final List<PsiFieldReference> uniques = getFieldReferences(entity.getHighLevelOption(MMToken.UNIQUE));
        assertThat(uniques).hasSize(1);
        assertThat(uniques.get(0).getReference().resolve()).isEqualTo(c);

        final List<PsiFieldReference> describes = getFieldReferences(entity.getHighLevelOption(MMToken.DESCRIBED_BY));
        assertThat(describes).hasSize(1);
        assertThat(describes.get(0).getReference().resolve()).isEqualTo(c);

        final List<PsiFieldReference> images = getFieldReferences(entity.getHighLevelOption(MMToken.IMAGE));
        assertThat(images).hasSize(1);
        assertThat(images.get(0).getReference().resolve()).isEqualTo(d);
    }

    @Test public void testEntityTypeNode() {
        final PsiEntity a              = mm.getEntity("A");
        final PsiType   aType          = mm.getType("AType");
        final PsiType   aQualifiedType = mm.getType("AQualifiedType");

        final PsiEntity        b      = mm.getEntity("B");
        final PsiEntityField[] fields = b.getFields();

        assertThat(
                ImmutableList.fromArray(fields)
                             .map(PsiEntityField::getTypeRef)
                             .map(PsiElement::getLastChild)
                             .map(PsiElement::getReference)
                             .map(PsiReference::resolve)).containsExactly(a, aType, aQualifiedType);
    }

    @Test public void testFormBoundToEntity() {
        final PsiEntity entity = mm.getEntity("B");
        final PsiForm   form   = mm.getForm("D");

        final PsiMetaModelCodeReferenceElement model = (PsiMetaModelCodeReferenceElement) form.getHighLevelOption(MMToken.DATAOBJECT_REF)
                                                       .getLastChild();
        assertThat(model.getReference().resolve()).isNotNull().isEqualTo(entity);

        final PsiWidget fieldA = form.getFieldNullable("a");

        final Option<ASTNode> fieldAType = fieldA.getWidgetDataType();
        assertThat(fieldAType.isPresent()).isTrue();
        assertThat(((PsiTypeReference) fieldAType.get()).resolve()).isEqualTo(entity.getFieldNullable("a"));

        final Option<ASTNode> fieldBType = form.getFieldNullable("b").getWidgetDataType();
        assertThat(fieldBType.isPresent()).isTrue();
        assertThat(((PsiTypeReference) fieldBType.get()).resolve()).isEqualTo(entity.getFieldNullable("b"));

        final ASTNode           check           = form.getFieldNullable("c").getFieldOption(FieldOption.CHECK).getFirstChildNode();
        final PsiFieldReference fieldAReference = (PsiFieldReference) check.getFirstChildNode().getFirstChildNode();
        assertThat(fieldAReference.getReference().resolve()).isEqualTo(fieldA);

        final ASTNode           invoke          = form.getFieldNullable("q").getFieldOption(FieldOption.DEFAULT);
        final PsiFieldReference fieldOReference = (PsiFieldReference) invoke.findChildByType(MMElementType.FIELD_REF);
        assertThat(fieldOReference.getReference().resolve()).isEqualTo(form.getFieldNullable("o"));

        final PsiFieldReference fieldPReference = (PsiFieldReference) form.getFieldNullable("r").getFieldOption(FieldOption.DISABLE);
        assertThat(fieldPReference.getReference().resolve()).isEqualTo(form.getFieldNullable("p"));
    }

    @Test public void testFormHighLevelOptions() {
        final PsiForm   form = mm.getForm("C");
        final PsiWidget a    = form.getFieldNullable("a");
        final PsiWidget b    = form.getFieldNullable("b");
        final PsiWidget c    = form.getFieldNullable("c");
        final PsiWidget d    = form.getFieldNullable("d");
        final PsiWidget e    = form.getFieldNullable("e");

        final PsiClass  javaClass    = java.findChildByClass(PsiClass.class);
        final PsiMethod loadMethod   = javaClass.findMethodsByName("load", false)[0];
        final PsiMethod cancelMethod = javaClass.findMethodsByName("cancel", false)[0];

        final List<PsiFieldReference> pks = getFieldReferences(form.getHighLevelOption(MMToken.PRIMARY_KEY));
        assertThat(map(pks, PsiFieldReference::getReference).map(r -> r.resolve())).containsExactly(a, b, c);

        final List<PsiFieldReference> parameters = getFieldReferences(form.getHighLevelOption(MMToken.PARAMETERS));
        assertThat(map(parameters, PsiFieldReference::getReference).map(r -> r.resolve())).containsExactly(d, e);

        final PsiMethodReference load = getMethodReference(form.getHighLevelOption(MMToken.ON_LOAD));
        assertThat(load.getReference().resolve()).isEqualTo(loadMethod);

        final PsiMethodReference cancel = getMethodReference(form.getHighLevelOption(MMToken.ON_CANCEL));
        assertThat(cancel.getReference().resolve()).isEqualTo(cancelMethod);
    }

    @Test public void testTypeAlias() {
        final PsiEntity expected = mm.getEntity("A");

        final PsiType          aType       = mm.getType("AType");
        final PsiEntityField[] aTypeFields = aType.getFields();
        assertThat(aTypeFields).hasSize(1);
        assertThat(aTypeFields[0].getTypeRef().getLastChild().getReference().resolve()).isEqualTo(expected);

        final PsiType          aQualifiedType       = mm.getType("AQualifiedType");
        final PsiEntityField[] aQualifiedTypeFields = aQualifiedType.getFields();
        assertThat(aQualifiedTypeFields).hasSize(1);
        assertThat(aQualifiedTypeFields[0].getTypeRef().getLastChild().getReference().resolve()).isEqualTo(expected);
    }

    public void setUp()
        throws Exception
    {
        super.setUp();
        final Module      module      = createMainModule();
        final VirtualFile moduleVFile = module.getModuleFile();
        assert moduleVFile != null;
        assertThat(moduleVFile).isNotNull();

        final File moduleDir = new File(moduleVFile.getParent().getPath());

        final File srcMM   = prepareSourceRoot(moduleDir, "mm");
        final File srcJava = prepareSourceRoot(moduleDir, "java");

        final File packageMM   = preparePackages(srcMM, "/idea/test");
        final File packageJava = preparePackages(srcJava, "/idea/test");

        mm   = (MMFile) copyFile(module, "plugin/mm/src/test/resources/idea/test/References.mm", packageMM);
        java = (PsiJavaFileImpl) copyFile(module, "plugin/mm/src/test/java/idea/test/C.java", packageJava);

        PsiTestUtil.addSourceRoot(module, LocalFileSystem.getInstance().refreshAndFindFileByIoFile(srcMM));
        PsiTestUtil.addSourceRoot(module, LocalFileSystem.getInstance().refreshAndFindFileByIoFile(srcJava));

        LocalFileSystem.getInstance().refreshAndFindFileByIoFile(new File(packageMM, mm.getName()));
        LocalFileSystem.getInstance().refreshAndFindFileByIoFile(new File(packageJava, java.getName()));

        module.getComponent(MMModuleComponent.class).initializeModelRepository();
    }

    private PsiFile copyFile(Module mainModule, String path, File destination) {
        final File        f         = new File(path);
        final String      content   = TestUtil.readFile(f);
        final VirtualFile refreshed = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(destination);
        return TestUtil.createFile(mainModule, refreshed, f.getName(), content);
    }

    private File preparePackages(File src, String packages) {
        final File dirs = new File(src, packages);
        dirs.mkdirs();
        return dirs;
    }

    private File prepareSourceRoot(File moduleDir, String technology) {
        final File src = new File(moduleDir, "../src/main/" + technology);
        Files.remove(src);
        src.mkdirs();
        return src;
    }

    private List<PsiFieldReference> getFieldReferences(MMCommonComposite node) {
        final List<PsiFieldReference> result = new ArrayList<>();
        for (final ASTNode reference : node.getChildren(TokenSet.create(MMElementType.FIELD_REF)))
            result.add((PsiFieldReference) reference);
        return result;
    }

    private PsiMethodReference getMethodReference(MMCommonComposite node) {
        final ASTNode method = node.findChildByType(TokenSet.create(MMElementType.METHOD_REF));
        return (PsiMethodReference) method;
    }
}  // end class ReferencesTest
