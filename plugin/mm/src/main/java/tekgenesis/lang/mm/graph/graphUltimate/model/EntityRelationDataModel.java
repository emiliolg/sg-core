
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.graph.graphUltimate.model;

import com.intellij.openapi.module.Module;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.entity.View;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.repository.MultiModuleRepository;
import tekgenesis.type.*;

import static tekgenesis.common.Predefined.cast;

/**
 * Graph model : methods and their dependencies.
 */
public class EntityRelationDataModel extends MMGraphDataModel {

    //~ Constructors .................................................................................................................................

    /** Data Model for an Entity Relation Graph. */
    public EntityRelationDataModel(Module module) {
        super(module);
        addNodes();
        addEdges();
    }

    //~ Methods ......................................................................................................................................

    @NotNull public String getEdgeName(Edge<MMGraphNode<?>> dep) {
        return dep.getLabel();
    }

    @SuppressWarnings("IfStatementWithIdenticalBranches")
    private void addEdges() {
        for (final MMGraphNode<?> node : getNodes()) {
            if (node instanceof EntityNode) {
                final EntityNode entityNode = cast(node);
                for (final Edge<MMGraphNode<?>> edge : getEdgesFrom(entityNode))
                    add(edge);
            }
            else if (node instanceof FormNode  /*&& GraphContextManager.getGraphContext(graph).isFormsShowing()*/) {
                final FormNode                     formNode = cast(node);
                final Option<Edge<MMGraphNode<?>>> edge     = getEdgeFrom(formNode);
                if (edge.isPresent()) add(edge.get());
            }
            else if (node instanceof JavaNode) {
                final JavaNode                     javaNode = cast(node);
                final Option<Edge<MMGraphNode<?>>> edgeFrom = getEdgeFrom(javaNode);
                if (edgeFrom.isPresent()) add(edgeFrom.get());
            }
        }
    }

    private void addEntities(String domain) {
        Seq<Entity> entities = repository.getModels(domain, Entity.class);
        if (entities.isEmpty() && repository instanceof MultiModuleRepository)
            entities = PsiUtils.getLibRepository(module).getModels(domain, Entity.class);
        for (final Entity entity : entities)
            createEntityNode(entity);
    }

    private void addEnums(String domain) {
        Seq<EnumType> enums = repository.getModels(domain, EnumType.class);
        if (enums.isEmpty() && repository instanceof MultiModuleRepository)
            enums = PsiUtils.getLibRepository(module).getModels(domain, EnumType.class);
        for (final EnumType enumType : enums)
            createEnumNode(enumType);
    }

    private void addForms(String domain) {
        Seq<Form> forms = repository.getModels(domain, Form.class);
        if (forms.isEmpty() && repository instanceof MultiModuleRepository) forms = PsiUtils.getLibRepository(module).getModels(domain, Form.class);
        for (final Form form : forms)
            createFormNode(form);
    }

    private void addNodes() {
        for (final String domain : repository.getDomains()) {
            addEntities(domain);
            addViews(domain);
            addEnums(domain);
            addForms(domain);
            addGeneratedFiles(domain);
            addJarJavaFiles(domain);
        }
    }

    private void addViews(String domain) {
        Seq<View> views = repository.getModels(domain, View.class);
        if (views.isEmpty() && repository instanceof MultiModuleRepository) views = PsiUtils.getLibRepository(module).getModels(domain, View.class);
        for (final View view : views)
            createViewNode(view);
    }
}  // end class EntityRelationDataModel
