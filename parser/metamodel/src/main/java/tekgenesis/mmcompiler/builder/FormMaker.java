
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.QName;
import tekgenesis.field.FieldOption;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateAttributeException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormBuilder;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;

import static tekgenesis.common.core.Strings.parseAsInt;
import static tekgenesis.common.core.Strings.unquote;
import static tekgenesis.metadata.exception.BuilderErrors.*;
import static tekgenesis.metadata.form.widget.Form.DEFAULT_SCHEDULE_INTERVAL;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.form;
import static tekgenesis.mmcompiler.ast.MMToken.LIST;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;
import static tekgenesis.type.permission.PredefinedPermission.isPredefined;

/**
 * Maker for creating a {@link FormBuilder}.
 */
class FormMaker extends UiModelMaker<Form, FormBuilder> {

    //~ Constructors .................................................................................................................................

    FormMaker(MetaModelAST formNode, BuilderFromAST builderFromAST, String sourceName, QContext context) {
        super(formNode, builderFromAST, sourceName, context);
    }

    //~ Methods ......................................................................................................................................

    /** Create a form Builder. */
    @NotNull FormBuilder createBuilder(QName fqn, String label, Type binding, EnumSet<Modifier> modifiers) {
        final FormBuilder builder = form(sourceName, fqn.getQualification(), fqn.getName()).label(label).withModifiers(modifiers);

        if (binding.isDatabaseObject()) builder.withBinding(binding);  // Until binding refactor limit to db model
        else if (binding.getFinalType() instanceof MetaModel) error(rootNode, illegalBindingType(fqn.getName(), (MetaModel) binding.getFinalType()));

        rootNode.forEach(n -> addFormOption(fqn, builder, n));

        if (rootNode.children(LIST).isEmpty())  // If no children, generate default form
            generateDefaultModelUi(builder);

        checkBuilder(builder);

        return builder.withRepository(repository);
    }  // end method createBuilder

    @SuppressWarnings("OverlyLongMethod")
    private void addFormOption(QName fqn, FormBuilder form, MetaModelAST n)
    {
        try {
            switch (n.getType()) {
            case UNRESTRICTED:
                form.unrestricted();
                break;
            case DATAOBJECT_REF:
                // Backward compatibility...
                if (form.getBinding().isEmpty()) getMetaModelReferenceCheckType(DbObject.class, n).ifPresent(form::withBinding);
                else bindingAlreadySpecified(fqn, n);
                break;
            case LISTING:
                // Backward compatibility...
                if (form.getBinding().isEmpty()) {
                    form.asListing();
                    getMetaModelReferenceCheckType(DbObject.class, n).ifPresent(form::withBinding);
                }
                else bindingAlreadySpecified(fqn, n);
                break;
            case PRIMARY_KEY:
                form.primaryKey(retrieveFieldIds(n).values());
                break;
            case PARAMETERS:
                form.parameters(retrieveFieldIds(n).values());
                break;
            case PERMISSIONS:
                form.permissions(retrievePermissions(n));
                break;
            case ON_LOAD:
                form.with(FieldOption.ON_LOAD, n.getChild(0).getChild(0).getText());
                break;
            case ON_DISPLAY:
                form.with(FieldOption.ON_DISPLAY, n.getChild(0).getChild(0).getText());
                break;
            case ON_CANCEL:
                form.with(FieldOption.ON_CANCEL, n.getChild(0).getChild(0).getText());
                break;
            case ON_ROUTE:
                form.onRoute(unquote(n.getChild(0).getText()));
                break;
            case HANDLER:
                form.handler(getAsFqn(n));
                break;
            case ON_SCHEDULE:
                form.with(FieldOption.ON_SCHEDULE, n.getChild(0).getChild(0).getEffectiveNode().getText());
                form.with(FieldOption.ON_SCHEDULE_INTERVAL, parseAsInt(n.getChild(1).getText(), DEFAULT_SCHEDULE_INTERVAL));
                break;
            case PROJECT:
                form.project(unquote(n.getChild(0).getText()));
                break;
            case LIST:
                if (form.isListing()) error(n, listingFormWithChildWidgets(fqn.getName()));
                buildWidgets(form, form.getBinding(), n, new FieldsChecker(fqn.getName()));
                break;
            default:
                // Ignore
            }
        }
        catch (final BuilderException exception) {
            error(n, exception);
        }
    }  // end method addFormOption

    private void bindingAlreadySpecified(QName fqn, MetaModelAST n) {
        error(n, createError("Binding already specified", fqn.getFullName()));
    }

    private boolean duplicatedPermission(List<String> result, String permissionName) {
        for (final String p : result) {
            if (p.equalsIgnoreCase(permissionName)) return true;
        }
        return false;
    }

    private Collection<String> retrievePermissions(MetaModelAST node) {
        final List<String> result = new ArrayList<>();
        for (final MetaModelAST n : node) {
            final String permissionName = n.getChild(0).getText();
            if (!permissionName.isEmpty()) {
                // error on duplicates...
                if (duplicatedPermission(result, permissionName))
                    error(n, new DuplicateAttributeException(permissionName, node.getType().getText(), ""));
                // error if it's a predefined one.
                else if (isPredefined(permissionName)) error(n, addingPredefinedPermissionError(permissionName));
                // finally, adding it.
                else result.add(permissionName);
            }
        }
        return result;
    }

    private String getAsFqn(MetaModelAST arg) {
        return context.withPackage(retrieveReferenceQualifiedId(arg)).getFullName();
    }
}  // end class FormMaker
