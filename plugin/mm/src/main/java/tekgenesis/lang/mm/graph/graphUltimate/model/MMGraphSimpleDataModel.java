
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.graph.graphUltimate.model;

import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiClass;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiEntity;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.type.EnumType;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.lang.mm.psi.PsiUtils.getPsiClassesForDirectory;

/**
 * Graph model : methods and their dependencies.
 */
public class MMGraphSimpleDataModel extends MMGraphDataModel {

    //~ Instance Fields ..............................................................................................................................

    private final Entity entity;
    private final MMFile file;

    //~ Constructors .................................................................................................................................

    /** Data Model for an Entity Relation Graph. */
    public MMGraphSimpleDataModel(PsiEntity entity, MMFile file, Module module) {
        super(module);
        this.entity = entity.getModel().get();
        this.file   = file;
        addNodes();
        addEdges();
    }

    //~ Methods ......................................................................................................................................

    @NotNull public String getEdgeName(Edge<MMGraphNode<?>> dep) {
        return dep.getLabel();
    }

    @SuppressWarnings("IfStatementWithIdenticalBranches")
    private void addEdges() {
        final EntityNode entityNode = cast(getNodeByFQN(entity.getFullName()));
        if (entityNode == null) return;
        for (final Edge<MMGraphNode<?>> edge : getEdgesFrom(entityNode))
            add(edge);
        for (final MMGraphNode<?> myNode : myNodes) {
            if (myNode instanceof FormNode) {
                final Option<Edge<MMGraphNode<?>>> edgeOption = getEdgeFrom((FormNode) myNode);
                if (edgeOption.isPresent()) add(edgeOption.get());
            }
            if (myNode instanceof JavaNode) {
                final Option<Edge<MMGraphNode<?>>> edgeOption = getEdgeFrom((JavaNode) myNode);
                if (edgeOption.isPresent()) add(edgeOption.get());
            }
        }
    }

    private List<String> addForms(String domain) {
        final ArrayList<String> formNames = new ArrayList<>();
        for (final Form form : repository.getModels(domain, Form.class)) {
            if (form.getBinding().getFullName().equals(entity.getFullName())) {
                createFormNode(form);
                formNames.add(form.getName());
            }
        }
        return formNames;
    }

    private void addGeneratedFiles(String domain, List<String> names) {
        for (final PsiClass clazz : getPsiClassesForDirectory(module, domain)) {
            if (names.contains(clazz.getName())) add(new JavaNode(clazz));
        }
    }

    private void addNodes() {
        final PsiEntity entPsi = file.getEntity(entity.getName());
        add(new EntityNode(entity, entPsi));
        for (final Attribute attribute : entity.attributes()) {
            if (attribute.isComposite()) {
                final Entity entity1 = attribute.asEntity().getOrNull();
                if (entity1 != null) createEntityNode(entity1);
            }
            else if (attribute.isEnum()) {
                final EnumType enumModel = attribute.asEnum().getOrNull();
                if (enumModel != null) createEnumNode(enumModel);
            }
        }
        final List<String> names = addForms(entity.getDomain());
        names.add(entity.getName());
        addGeneratedFiles(entity.getDomain(), names);
    }
}  // end class MMGraphSimpleDataModel
