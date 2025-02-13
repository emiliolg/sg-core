
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.exprs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckType;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.MailDomain;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;
import tekgenesis.model.TreeNode;
import tekgenesis.type.ArrayType;
import tekgenesis.type.Type;
import tekgenesis.view.client.ui.*;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.equalElements;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.metadata.form.widget.WidgetType.SEARCH_BOX;
import static tekgenesis.model.KeyMap.EMPTY;

/**
 * Populate the view with the model.
 */
class FormDataHandler {

    //~ Constructors .................................................................................................................................

    private FormDataHandler() {}

    //~ Methods ......................................................................................................................................

    /** Populate the view with the model. */
    static void modelToView(@NotNull final Model contextModel, @NotNull final BaseWidgetUI widget) {
        new ModelToViewVisitor(contextModel).visitChild(widget);
    }

    //~ Inner Classes ................................................................................................................................

    private static class ModelToViewVisitor implements ModelUiVisitor {
        private final Model model;

        private ModelToViewVisitor(Model model) {
            this.model = model;
        }

        @Override public void traverse(Iterable<WidgetUI> children) {
            // Do not traverse. Model to view is just for scalar fields.
        }

        @Override public void visit(TreeViewUI widget) {
            // todo check that the instance have change, or check for a dirty flag, or something so we don't re-populate
            final KeyMap options = model.getOptions(widget.getModel());
            // Expects typed OptionsMap<String,TreeNode>
            widget.setOptions(toTreeNodesIterable(options));
        }

        @Override public void visit(HasOptionsUI widget) {
            widget.setOptions(model.getOptions(widget.getModel()));
        }

        @Override public void visit(HasRangeOptionsUI widget) {
            widget.setRangeOptions(model.getOptions(widget.getModel()));
        }

        @Override public void visit(SuggestBoxUI suggest) {
            if (suggest.getModel().getWidgetType() != SEARCH_BOX) {
                final Widget widget = suggest.getModel();
                final Object value  = suggest.getValue();

                final Type type = widget.getType();
                if (type.isDatabaseObject() || type.isEnum() || type.isString()) {
                    final KeyMap options = model.getOptions(suggest.getModel());
                    suggest.setOptions(options);
                    suggest.setLabel(options.get(value));
                }
                else suggest.setFormattedLabel(value);
            }
        }

        @Override public void visit(MailFieldUI mailField) {
            final KeyMap options = model.getOptions(mailField.getModel());
            if (!options.isEmpty()) mailField.setOptions(options);
            else mailField.setOptions(MailDomain.domainsKeyMap());
        }

        @Override public void visit(TagsSuggestBoxUI suggest) {
            suggest.setValues(model.getArray(suggest.getModel()));
            suggest.setOptions(model.getOptions(suggest.getModel()));
        }

        @Override public void visit(DisplayUI display) {
            final Widget widget = display.getModel();
            final Object value  = display.getValue();
            final Type   type   = widget.getType();

            if (type.isDatabaseObject() || type.isEnum()) {
                // get the describe by text and set it
                final KeyMap options = model.getOptions(widget);
                final KeyMap images  = type.isDatabaseObject() && widget.hasImage() ? model.getImages(widget) : EMPTY;
                display.setDescribeByValue(options.get(value), images.get(value));
            }
            else
            // sets a primitive object value
            display.setPrimitiveValue(value);
        }

        @Override public void visit(MultipleDisplayUI display) {
            final Widget      widget  = display.getModel();
            final Type        type    = ((ArrayType) widget.getType()).getElementType();
            final Iterable<?> options = type.isDatabaseObject() || type.isEnum() ? model.getOptions(widget).values() : display.getValues();
            final KeyMap      images  = type.isDatabaseObject() && widget.hasImage() ? model.getImages(widget) : EMPTY;
            display.setOptionsText(options, images);
        }

        @Override public void visit(HasValueUI widget) {
            if (widget.getModel().isMultiple()) visit((HasArrayValueUI) widget);
            else visit(((HasScalarValueUI) widget));
        }

        @Override public void visit(MessageUI message) {
            final CheckType msgType = message.getModel().getMsgType();

            if (model instanceof FormModel && msgType == CheckType.ENTITY) {
                final FormModel formModel = (FormModel) model;
                message.setValue(formModel.isUpdate() ? formModel.getDescribeBy() : formModel.metadata().getLabel());
            }
        }

        @Nullable private TreeNode asTreeNode(final Object o) {
            return o instanceof TreeNode ? (TreeNode) o : null;
        }

        private Iterable<TreeNode> toTreeNodesIterable(@NotNull final KeyMap options) {
            if (options.isEmpty()) return emptyIterable();
            return map(options.keySet(), this::asTreeNode);
        }

        private void visit(HasScalarValueUI valued) {
            final Widget elem       = valued.getModel();
            final Object modelValue = model.get(elem);
            if (!equal(modelValue, valued.getValue())) valued.setValue(modelValue);
        }

        private void visit(HasArrayValueUI valued) {
            final Widget           elem       = valued.getModel();
            final Iterable<Object> modelValue = model.getArray(elem);
            if (!equalElements(modelValue, valued.getValues())) valued.setValues(modelValue);
        }
    }  // end class ModelToViewVisitor
}  // end class FormDataHandler
