
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
import java.util.EnumSet;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.QName;
import tekgenesis.field.MetaModelReference;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateRolePermissionsException;
import tekgenesis.metadata.role.RoleBuilder;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.Modifier;

import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.core.Constants.PERMISSION_ALL;
import static tekgenesis.mmcompiler.ast.MMToken.*;
import static tekgenesis.mmcompiler.builder.ASTMetaModelReference.unresolvedMetaModel;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;

/**
 * Create {@link RoleBuilder}.
 */
class RoleMaker extends Maker {

    //~ Constructors .................................................................................................................................

    RoleMaker(MetaModelAST handlerNode, BuilderFromAST builderFromAST, String sourceName, QContext context) {
        super(handlerNode, builderFromAST, sourceName, context);
    }

    //~ Methods ......................................................................................................................................

    @Override protected RoleBuilder createBuilder(QName fqn, String label, EnumSet<Modifier> modifiers) {
        final RoleBuilder builder = new RoleBuilder(sourceName, fqn.getQualification(), fqn.getName()).label(label).withModifiers(modifiers);

        for (final MetaModelAST node : rootNode) {
            switch (node.getType()) {
            case LIST:
                node.children(ROLE_ELEMENT).forEach(p -> expandPermissions(builder, p));
                break;
            default:
                // Ignore
            }
        }
        return builder;
    }

    private void duplicateErrors(List<PermissionNode> nodes, DuplicateRolePermissionsException e) {
        for (final String duplicate : e.getDuplicates())
            filter(nodes, n -> n.matches(duplicate)).drop(1).forEach(n -> error(n.node, e));
    }

    private void expandPermissions(RoleBuilder role, MetaModelAST permissions) {
        final MetaModelAST       application = permissions.getChild(0);
        final String             fqn         = retrieveReferenceQualifiedId(application);
        final MetaModelReference model       = unresolvedMetaModel(this, application, context, fqn);

        final List<PermissionNode> ps = new ArrayList<>();

        filter(permissions, n -> n.getType() == LIST).forEach(n -> {  //
            n.children(ROLE_PERMISSION).map(PermissionNode::new).forEach(ps::add);
        });

        if (ps.isEmpty()) ps.add(new PermissionNode(PERMISSION_ALL, permissions));

        try {
            // Set permissions for model
            role.permissions(model, map(ps, PermissionNode::getPermission));
        }
        catch (final DuplicateRolePermissionsException e) {
            duplicateErrors(ps, e);
        }
        catch (final BuilderException e) {
            error(permissions, e);
        }
    }

    //~ Inner Classes ................................................................................................................................

    private static class PermissionNode {
        private final MetaModelAST node;

        private final String permission;

        private PermissionNode(@NotNull final MetaModelAST node) {
            this(node.getChild(0).getText(), node);
        }

        private PermissionNode(@NotNull final String permission, @NotNull final MetaModelAST node) {
            this.permission = permission;
            this.node       = node;
        }

        private boolean matches(@NotNull final String p) {
            return permission.equals(p);
        }

        private String getPermission() {
            return permission;
        }
    }
}  // end class RoleMaker
