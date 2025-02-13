
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.builder;

import java.util.EnumSet;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.common.ModelBuilder;
import tekgenesis.metadata.exception.DuplicateAttributeException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.link.Link;
import tekgenesis.metadata.link.LinkBuilder;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.Modifier;
import tekgenesis.type.Types;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Strings.*;

/**
 * {@link Link} maker.
 */
class LinkMaker extends Maker {

    //~ Constructors .................................................................................................................................

    LinkMaker(MetaModelAST linkNode, BuilderFromAST builderFromAST, String sourceName, QContext context) {
        super(linkNode, builderFromAST, sourceName, context);
    }

    //~ Methods ......................................................................................................................................

    @Override protected ModelBuilder<?, ?> createBuilder(QName fqn, String label, EnumSet<Modifier> modifiers) {
        final String      fixedLabel = isEmpty(label) ? toWords(fromCamelCase(fqn.getName())) : label;
        final LinkBuilder builder    = new LinkBuilder(sourceName, fqn.getQualification(), fqn.getName()).label(fixedLabel).withModifiers(modifiers);

        for (final MetaModelAST n : rootNode) {
            switch (n.getType()) {
            case FORM_REF:
                final Option<Form> form = getMetaModelReference(Form.class, n);
                if (form.isPresent()) builder.withForm(form.get());
                break;
            case LIST:
                for (final MetaModelAST assignment : n.children(MMToken.ASSIGNMENT)) {
                    final MetaModelAST node  = assignment.getChild(0);
                    final String       field = node.getChild(0).getText();
                    final Expression   value = getAsExpression(assignment.getChild(1), Types.anyType());
                    if (builder.containsAssignation(field)) error(assignment, new DuplicateAttributeException(field, "form assignments", ""));
                    else builder.addAssignation(new ASTFieldReference(field, this, node), value);
                }
                break;
            case STRING_LITERAL:
                final String link = unquote(rootNode.getChild(rootNode.children().size() - 1).getText());
                builder.withLink(link);
                break;
            default:
                // ignore?
            }
        }

        return builder;
    }
}  // end class LinkMaker
