
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.metadata.form.widget.WidgetDef;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;
import tekgenesis.type.ModelType;

import static tekgenesis.codegen.common.MMCodeGenConstants.WIDGET_INSTANCE;
import static tekgenesis.common.collections.Colls.emptyIterable;

/**
 * Generate code for {@link WidgetDef} base classes.
 */
@SuppressWarnings("ClassTooDeepInInheritanceTree")
public class WidgetDefBaseCodeGenerator extends UiModelBaseCodeGenerator<WidgetDef> {

    //~ Constructors .................................................................................................................................

    /** Create a {@link UiModelBaseCodeGenerator}. */
    public WidgetDefBaseCodeGenerator(JavaCodeGenerator codeGenerator, @NotNull WidgetDef model, @NotNull Option<MetaModel> binding,
                                      ModelRepository repository) {
        super(codeGenerator, model, bindingToModelType(binding), repository);
    }

    //~ Methods ......................................................................................................................................

    @Override protected void populate() {
        asAbstract().withSuperclass(generic(WIDGET_INSTANCE, getWidgetBindingType()));

        // Generate populate method
        generatePopulateMethod(true);

        // Add copy to methods
        generateCopyToMethod();

        // Generate widget methods
        traverse(model);

        // Fields orthogonal accessors
        generateMessageMethod();
        generateIsDefinedMethod();
        generateResetMethod();
        generateFocusMethod();
        generateLabelMethod();
        generateConfigureMethod();

        super.populate();
    }

    @Override Iterable<InnerTableBaseCodeGenerator> getTables() {
        return emptyIterable();
    }

    private String getWidgetBindingType() {
        return getBinding().map(MetaModel::getFullName).orElseGet(Void.class::getName);
    }

    //~ Methods ......................................................................................................................................

    @NotNull private static Option<? extends ModelType> bindingToModelType(@NotNull Option<MetaModel> binding) {
        final Option<StructType> type = binding.castTo(StructType.class);
        if (type.isPresent()) return type;
        return binding.castTo(DbObject.class);
    }
}  // end class WidgetDefBaseCodeGenerator
