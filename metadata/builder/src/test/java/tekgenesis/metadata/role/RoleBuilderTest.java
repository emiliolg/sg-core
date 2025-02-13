
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.role;

import java.util.Set;

import org.junit.Test;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.common.ModelLinkerImpl;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.exception.*;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormBuilder;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModelKind;

import static java.util.Collections.emptySet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.collections.Colls.set;
import static tekgenesis.common.core.Constants.PERMISSION_ALL;
import static tekgenesis.field.MetaModelReference.referenceMetaModel;
import static tekgenesis.metadata.entity.EntityBuilder.entity;
import static tekgenesis.metadata.entity.EntityBuilder.string;
import static tekgenesis.metadata.exception.DuplicateRolePermissionsException.PERMISSION_ALREADY_DEFINED;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.*;
import static tekgenesis.type.Types.anyType;
import static tekgenesis.type.permission.PredefinedPermission.CREATE;
import static tekgenesis.type.permission.PredefinedPermission.READ;

/**
 * Role builder test.
 */
public class RoleBuilderTest {

    //~ Methods ......................................................................................................................................

    @Test public void testRoleDuplicateMetaModel()
        throws BuilderException
    {
        final QName       name    = QName.createQName("test.SimpleRole");
        final RoleBuilder builder = new RoleBuilder("", name.getQualification(), name.getName());

        builder.permissions(referenceMetaModel("test.SimpleFormA"), listOf(CREATE.getName()));
        builder.permissions(referenceMetaModel("test.SimpleFormB"), listOf(PERMISSION_ALL));

        try {
            builder.permissions(referenceMetaModel("test.SimpleFormA"), listOf(READ.getName()));
            failBecauseExceptionWasNotThrown(MetaModelAlreadyDefinedException.class);
        }
        catch (final MetaModelAlreadyDefinedException e) {
            assertThat(e.getMessage()).isEqualTo("Role already defines permissions for SimpleFormA");
        }
    }

    @Test public void testRoleDuplicatePermissions()
        throws BuilderException
    {
        final QName       name    = QName.createQName("test.SimpleRole");
        final RoleBuilder builder = new RoleBuilder("", name.getQualification(), name.getName());

        final ModelRepository repository = new ModelRepository();
        final Form            form       = buildForm(repository, emptySet());

        final ImmutableList<String> permissions = Colls.listOf("read", "create", "print", "read", "print");

        try {
            builder.permissions(referenceMetaModel(form.getFullName()), permissions);
        }
        catch (final DuplicateRolePermissionsException e) {
            assertThat(e.getDuplicates()).contains("read", "print");
            assertThat(e).hasMessage(PERMISSION_ALREADY_DEFINED);
        }
    }

    @Test public void testRoleWithExistingCustomPermissions()
        throws BuilderException
    {
        final QName       name    = QName.createQName("test.SimpleRole");
        final RoleBuilder builder = new RoleBuilder("", name.getQualification(), name.getName());

        final ModelRepository repository = new ModelRepository();
        final Form            form       = buildForm(repository, set("print", "draft"));

        final ImmutableList<String> permissions = Colls.listOf("read", "create", "print", "draft");
        builder.permissions(referenceMetaModel(form.getFullName()), permissions);

        final Role role = builder.build();

        new ModelLinkerImpl(repository).link(role);

        assertThat(role.getChildren()).hasSize(4);

        role.getChildren().zip(permissions).forEach(t -> assertThat(t.first().getPermission()).isEqualTo(t.second()));
    }

    @Test public void testRoleWithUnexistingCustomPermissions()
        throws BuilderException
    {
        final QName       name    = QName.createQName("test.SimpleRole");
        final RoleBuilder builder = new RoleBuilder("", name.getQualification(), name.getName());

        final ModelRepository repository = new ModelRepository();
        final Form            form       = buildForm(repository, set("print", "draft"));

        final ImmutableList<String> permissions = Colls.listOf("read", "create", "print", "draft", "other");
        builder.permissions(referenceMetaModel(form.getFullName()), permissions);

        final Role role = builder.build();

        try {
            new ModelLinkerImpl(repository).link(role);
            failBecauseExceptionWasNotThrown(PermissionNotDeclaredException.class);
        }
        catch (final PermissionNotDeclaredException e) {
            assertThat(e).hasMessageContaining("Permission 'other' not declared on form");
        }
    }

    @Test public void testRoleWithUnexpectedMetaModel()
        throws BuilderException
    {
        final QName       name    = QName.createQName("test.SimpleRole");
        final RoleBuilder builder = new RoleBuilder("", name.getQualification(), name.getName());

        final ModelRepository repository = new ModelRepository();

        final Entity entity = buildEntity(repository);

        final ImmutableList<String> permissions = Colls.listOf("read", "create");
        builder.permissions(referenceMetaModel(entity.getFullName()), permissions);

        final Role role = builder.build();

        try {
            new ModelLinkerImpl(repository).link(role);
            failBecauseExceptionWasNotThrown(UnexpectedMetaModelKindException.class);
        }
        catch (final UnexpectedMetaModelKindException e) {
            assertThat(e).hasMessageContaining("Unexpected MetaModel");
        }
    }

    @Test public void testSimpleRole()
        throws BuilderException
    {
        final QName       name    = QName.createQName("test.SimpleRole");
        final RoleBuilder builder = new RoleBuilder("", name.getQualification(), name.getName());

        final ModelRepository repository = new ModelRepository();
        final Form            form       = buildForm(repository, emptySet());

        builder.permissions(referenceMetaModel(form.getFullName()), listOf(PERMISSION_ALL));

        final Role role = builder.build();

        new ModelLinkerImpl(repository).link(role);

        assertThat(role.getName()).isEqualTo("SimpleRole");
        assertThat(role.getDomain()).isEqualTo("test");
        assertThat(role.getFullName()).isEqualTo("test.SimpleRole");
        assertThat(role.getMetaModelKind()).isEqualTo(MetaModelKind.ROLE);
        assertThat(role.getChildren()).hasSize(1);

        final RolePermission permission = role.getChildren().getFirst().getOrFail("Expected Role Permission as children");
        assertThat(permission.getType()).isEqualTo(anyType());
        assertThat(permission.getName()).isEqualTo("test.SimpleForm.*");
        assertThat(permission.getPermission()).isEqualTo("*");
        assertThat(permission.getMetaModel()).isEqualTo(form);
    }

    private Entity buildEntity(ModelRepository repository)
        throws BuilderException
    {
        final Entity entity = entity("test", "SimpleEntity").fields(string("attr")).build();
        repository.add(entity);
        return entity;
    }

    private Form buildForm(ModelRepository repository, Set<String> permissions)
        throws BuilderException
    {
        final FormBuilder builder = form("", "test", "SimpleForm").children(field("A").id("a"), check("B").id("b"));
        builder.permissions(permissions);
        final Form form = builder.withRepository(repository).build();
        repository.add(form);
        return form;
    }
}  // end class RoleBuilderTest
