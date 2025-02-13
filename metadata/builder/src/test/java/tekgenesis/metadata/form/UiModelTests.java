
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.entity.EntityBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.WidgetDef;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.expr.ExpressionFactory.*;
import static tekgenesis.metadata.entity.EntityBuilder.*;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.*;

/**
 * Utility class for {@link UiModel ui model} tests.
 */
public class UiModelTests {

    //~ Constructors .................................................................................................................................

    private UiModelTests() {}

    //~ Methods ......................................................................................................................................

    public static WidgetDef bindingAddressWidget(@NotNull ModelRepository repository, @NotNull Entity address, @NotNull String name)
        throws BuilderException
    {
        final WidgetDef result = widget("", "tekgenesis.test", name).withBinding(address)
                                 .children(                                     //
                    field("").id("street").withBinding(address.getAttribute("street").get()),
                    field("").id("number").withBinding(address.getAttribute("number").get()),
                    field("").id("postal").withBinding(address.getAttribute("postal").get()),
                    check("").id("primary").withBinding(address.getAttribute("primary").get()))
                                 .withRepository(repository)
                                 .build();

        repository.add(result);

        return result;
    }

    public static Form bindingCustomerForm(@NotNull ModelRepository repository, @NotNull Entity customer, @NotNull WidgetDef addressWidget)
        throws BuilderException
    {
        return form("", "tekgenesis.test", "CustomerForm").withBinding(customer)
               .children(                                             //
                    internal("doc").withBinding(customer.getAttribute("doc").get()),
                    field("First").withBinding(customer.getAttribute("first").get()),
                    field("Last").withBinding(customer.getAttribute("last").get()),
                    widget("Home Address", addressWidget).withBinding(customer.getAttribute("homeAddress").get()),
                    widget("Work Address", addressWidget).withBinding(customer.getAttribute("workAddress").get()).optional())
               .withRepository(repository)
               .build();
    }

    public static Entity buildAddressEntity(@NotNull ModelRepository repository)
        throws BuilderException
    {
        final Entity result = entity("tekgenesis.test", "Address").primaryKey("street", "number")
                              .fields(string("street").id("street"),
                    EntityBuilder.integer("number").id("number"),
                    EntityBuilder.integer("postal").id("postal"),
                    bool("primary").id("primary"))
                              .build();

        repository.add(result);

        return result;
    }

    public static Entity buildCustomerEntity(@NotNull ModelRepository repository, @NotNull Entity address)
        throws BuilderException
    {
        final Entity result = entity("tekgenesis.test", "Customer").primaryKey("doc")
                              .fields(EntityBuilder.integer("doc").id("doc"),
                    string("first").id("first"),
                    string("last").id("last"),
                    reference("homeAddress", address).id("homeAddress"),
                    reference("workAddress", address).id("workAddress").optional())
                              .build();

        repository.add(result);

        return result;
    }

    public static Form employeeForm(@NotNull ModelRepository repository, @NotNull WidgetDef employeeWidget)
        throws BuilderException
    {
        return form("", "tekgenesis.test", "EmployeeForm").children(  //
                widget("Employee", employeeWidget).id("employee"),    //
                display("Label").id("display").is(ref("employee.full"))  //
            ).withRepository(repository)
               .build();
    }

    public static WidgetDef employeeWidget(@NotNull ModelRepository repository)
        throws BuilderException
    {
        final WidgetDef result = widget("", "tekgenesis.test", "EmployeeWidget").children(  //
                    field("").id("first"),
                    field("").id("last"),
                    field("").id("legacy"),
                    field("").id("full").is(add(ref("last"), add(str(", "), ref("first")))),
                    check("").id("primary"),
                    widget("Manager", "tekgenesis.test.EmployeeWidget").id("manager").optional())
                                 .withRepository(repository)
                                 .build();

        repository.add(result);

        return result;
    }
}  // end class UiModelTests
