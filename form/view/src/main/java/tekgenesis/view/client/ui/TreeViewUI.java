
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.TreeNode;
import tekgenesis.view.client.ui.base.HtmlList;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Strings.coverText;
import static tekgenesis.metadata.form.model.FormConstants.TREENODE;
import static tekgenesis.metadata.form.model.FormConstants.TREEVIEW;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.querySelector;

/**
 * TreeView widget.
 */
public class TreeViewUI extends FieldWidgetUI implements HasScalarValueUI {

    //~ Instance Fields ..............................................................................................................................

    private ValueChangeHandler<Object> changeHandler = null;
    private final String               iconStyle;
    private Map<Object, String>        nodes         = null;
    private Object                     selectedKey   = null;
    private final HtmlList.Unordered   ul;

    //~ Constructors .................................................................................................................................

    /** Creates a TreeViewUI Widget. */
    public TreeViewUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        ul = HtmlWidgetFactory.ul();
        initWidget(ul);
        addControlStyleName(TREEVIEW);
        nodes = new HashMap<>();
        final String style = model.getIconStyle();
        iconStyle = isEmpty(style) ? IconType.CARET_RIGHT.getClassName() : style;
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> handler) {
        changeHandler = handler;
    }

    /** Set options tree model. */
    public void setOptions(final Iterable<TreeNode> items) {
        ul.clear();
        populateTree(items, ul);

        if (selectedKey != null) {
            // deselect old value
            final Element oldSelected = querySelector(ul.getElement(), ".well");
            if (oldSelected != null) oldSelected.removeClassName("well");

            final String id = nodes.get(selectedKey);

            // select/open all parent nodes
            final Element link = querySelector(ul.getElement(), "*[data-link='" + id + "']");
            if (link != null) {
                link.addClassName("well");
                checkParentElements(link.getParentElement());
            }
        }
    }  // end method setOptions

    @Override public Object getValue() {
        return selectedKey;
    }

    @Override public void setValue(@Nullable Object modelValue) {
        selectedKey = modelValue;
    }

    @Override public void setValue(@Nullable Object modelValue, boolean fireEvents) {
        setValue(modelValue);
        changeHandler.onValueChange(null);  // hack... improve ui framework
    }

    // ** This widget doesn't support icon */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    private String addNode(Object node) {
        final String id = nodes.size() + "";
        nodes.put(node, id);
        return id;
    }

    private void checkParentElements(final Element child) {
        Element element = child;
        while (!element.getClassName().contains(TREEVIEW) || !"DIV".equals(element.getTagName())) {
            if ("LI".equals(element.getTagName())) {
                final InputElement input = element.getFirstChild().cast();
                input.setChecked(true);
            }
            element = element.getParentElement();
        }
    }

    private void populateTree(Iterable<TreeNode> items, HtmlList.Unordered someUl) {
        for (final TreeNode item : items) {
            final Object key        = item.getKey();
            final String id         = addNode(key);
            final String coverLabel = getModel().isEntity() ? coverText(item.getLabel()) : item.getLabel();

            if (item.hasChildren()) {
                final HtmlList.Item li   = HtmlWidgetFactory.li();
                final Icon          icon = new Icon();
                icon.addStyleName("fa fa-" + iconStyle);
                icon.addStyleName(TREENODE);

                final String       uid   = DOM.createUniqueId();
                final InputElement input = DOM.createInputCheck().cast();
                input.setId(uid);

                final HtmlList.Label label = new HtmlList.Label(coverLabel, uid);
                if (getModel().isEntity()) label.setTitle(item.getLabel());
                label.getElement().setAttribute("data-link", id);

                label.addDomHandler(clickEvent -> {
                        label.addStyleName("well");
                        setValue(key, true);
                    },
                    ClickEvent.getType());

                li.getElement().appendChild(input);
                li.add(icon);
                li.add(label);

                final HtmlList.Unordered innerUl = HtmlWidgetFactory.ul();
                li.add(innerUl);
                someUl.add(li);

                populateTree(item, innerUl);
            }
            else {
                final HtmlList.Item li     = HtmlWidgetFactory.li();
                final Anchor        anchor = HtmlWidgetFactory.anchor(coverLabel);
                anchor.getElement().setAttribute("data-link", id);

                anchor.addClickHandler(clickEvent -> {
                    anchor.addStyleName("well");
                    clickEvent.preventDefault();
                    setValue(key, true);
                });

                li.add(anchor);
                someUl.add(li);
            }
        }
    }  // end method populateTree
}  // end class TreeViewUI
