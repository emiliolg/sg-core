
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.graph.graphUltimate.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.intellij.openapi.graph.builder.CachedGraphDataModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.lang.mm.MMModuleComponent;
import tekgenesis.lang.mm.graph.GraphUtils;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.entity.View;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.EnumType;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.core.Option.some;
import static tekgenesis.lang.mm.psi.PsiUtils.findMMFileIncludingNonProjectItems;
import static tekgenesis.lang.mm.psi.PsiUtils.getPsiClassesForDirectory;

/**
 * Abstract Data Model for MMFiles.
 */
public abstract class MMGraphDataModel extends CachedGraphDataModel<MMGraphNode<?>, Edge<MMGraphNode<?>>> {

    //~ Instance Fields ..............................................................................................................................

    final Module module;

    final ModelRepository repository;

    private final Project project;

    //~ Constructors .................................................................................................................................

    MMGraphDataModel(Module module) {
        this.module = module;
        project     = module.getProject();
        repository  = module.getComponent(MMModuleComponent.class).getRepository();
    }

    //~ Methods ......................................................................................................................................

    /** Checks if graph contains Edge. */
    public boolean contains(Edge<MMGraphNode<?>> edge) {
        for (final Edge<MMGraphNode<?>> e : getEdges()) {
            if (edge.equals(e)) return true;
        }
        return false;
    }

    @Override public void dispose() {}

    /** Removes EntityNode from Graph. */
    public void remove(MMGraphNode<?> method) {
        myNodes.remove(method);
    }

    /** Removes Edges from Graph. */
    public void remove(Collection<Edge<MMGraphNode<?>>> edges) {
        for (final Edge<MMGraphNode<?>> edge : edges)
            remove(edge);
    }

    /** Gets Edges from Graph. */
    public Set<Edge<MMGraphNode<?>>> getEdges(MMGraphNode<?> node) {
        final Set<Edge<MMGraphNode<?>>> result = new HashSet<>();
        for (final Edge<MMGraphNode<?>> edge : getEdges()) {
            if (edge.getFrom().equals(node) || edge.getTo().equals(node)) result.add(edge);
        }
        return result;
    }

    @NotNull public String getNodeName(MMGraphNode<?> node) {  // legacy method
        return node.getName();
    }

    @Override protected void buildGraph() {}

    /** Adds node to Graph. */
    void add(MMGraphNode<?> node) {
        createNode(node);
    }

    /** Adds Edge to Graph. */
    void add(Edge<MMGraphNode<?>> edge) {
        createEdge(edge, edge.getFrom(), edge.getTo());
    }

    void addGeneratedFiles(String domain) {
        for (final PsiClass clazz : getPsiClassesForDirectory(module, domain))
            add(new JavaNode(clazz));
    }

    void addJarJavaFiles(String domain) {
        final ModelRepository libRep = PsiUtils.getLibRepository(module);
        if (!libRep.getModels(domain).isEmpty()) {
            final String[] split = libRep.getModels(domain).getFirst().get().getSourceName().split("!");
            final String   root  = split[0].split("file:")[1];
            final String   dom   = split[1];
            for (final VirtualFile libraryRoot : module.getComponent(MMModuleComponent.class).getLibraryRoots()) {
                if (libraryRoot.getPath().equals(root + "!/")) {
                    final VirtualFile  dir          = findDirInJar(libraryRoot, dom);
                    final PsiDirectory psiDirectory = dir == null ? null : PsiUtils.findPsiDirectory(module, dir);
                    if (psiDirectory != null)
                        for (final PsiClass clazz : JavaDirectoryService.getInstance().getClasses(psiDirectory))
                            add(new JavaNode(clazz));
                }
            }
        }
    }

    void createEntityNode(Entity entity) {
        final Option<MMFile> mmFileOption = findMMFileIncludingNonProjectItems(project, module, entity.getSourceName());
        add(new EntityNode(entity, mmFileOption.isEmpty() ? null : mmFileOption.get().getEntity(entity.getName())));
    }

    void createEnumNode(EnumType enumType) {
        final Option<MMFile> mmFileOption = findMMFileIncludingNonProjectItems(project, module, enumType.getSourceName());
        add(new EnumNode(enumType, mmFileOption.isEmpty() ? null : mmFileOption.get().getEnum(enumType.getName())));
    }

    void createFormNode(Form form) {
        final Option<MMFile> mmFileOption = findMMFileIncludingNonProjectItems(project, module, form.getSourceName());
        add(new FormNode(form, mmFileOption.isEmpty() ? null : mmFileOption.get().getForm(form.getName())));
    }

    void createViewNode(View view) {
        final Option<MMFile> mmFileOption = findMMFileIncludingNonProjectItems(project, module, view.getSourceName());
        add(new ViewNode(view, mmFileOption.isEmpty() ? null : mmFileOption.get().getView(view.getName())));
    }

    Option<Edge<MMGraphNode<?>>> getEdgeFrom(JavaNode javaNode) {
        final MMGraphNode<?> nodeByFQN = getNodeByFQN(javaNode.getBoundMMFullName());
        if (nodeByFQN == null) return Option.empty();
        return some(new Edge<>(javaNode, nodeByFQN));
    }

    Option<Edge<MMGraphNode<?>>> getEdgeFrom(FormNode formNode) {
        if ((formNode.getModel().getBinding() == QName.EMPTY)) return Option.empty();
        final MMGraphNode<?> node = getNodeByFQN(formNode.getModel().getBinding().getFullName());
        if (node == null) return Option.empty();
        return some(new Edge<>(formNode, node));
    }

    /** Gets Edges exiting from Node. */
    Set<Edge<MMGraphNode<?>>> getEdgesFrom(EntityNode node) {
        final Set<Edge<MMGraphNode<?>>> result = new HashSet<>();
        for (final Attribute attribute : node.getModel().attributes()) {
            if (GraphUtils.hasRelation(repository, attribute)) {
                final MetaModel model = GraphUtils.getBindedModelForAttribute(repository, attribute);
                if (model instanceof Entity && !(node.getModel().isInner() && model.equals(node.getModel().getParent().getOrNull())) ||
                    model instanceof EnumType)
                {
                    final MMGraphNode<?> node2 = getNodeByFQN(model.getFullName());
                    if (node2 != null) result.add(new Edge<>(node, node2, GraphUtils.getCardinality(attribute)));
                }
            }
        }
        return result;
    }

    /** Gets EntityNode by Fully Qualified Name. */
    @Nullable MMGraphNode<?> getNodeByFQN(String fqn) {
        for (final MMGraphNode<?> node : getNodes()) {
            if (node.getFullName().equals(fqn)) return node;
        }
        return null;
    }

    @Nullable
    @SuppressWarnings("UnsafeVfsRecursion")
    private VirtualFile findDirInJar(VirtualFile libraryRoot, String s) {
        final String[] split = s.split("/");
        for (final VirtualFile virtualFile : libraryRoot.getChildren()) {
            if (virtualFile.getName().equals(split[1])) {
                if (split.length <= 3) return virtualFile;
                return findDirInJar(virtualFile, s.split(split[1])[1]);
            }
        }
        return null;
    }

    /** Removes Edge from Graph. */
    private void remove(Edge<MMGraphNode<?>> edge) {
        myEdges.remove(edge);
    }
}  // end class MMGraphDataModel
