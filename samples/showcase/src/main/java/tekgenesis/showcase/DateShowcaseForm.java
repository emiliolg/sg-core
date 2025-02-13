
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * Date Showcase Form class.
 */
@SuppressWarnings("WeakerAccess")
public class DateShowcaseForm extends DateShowcaseFormBase implements DateOutput {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action addMessage() {
        message(Field.DATE_FROM, "Martin gatoooooo!");
        return actions.getDefault();
    }

    @NotNull @Override public Action addMessages() {
        message(Field.TIME_FROM, "Martin gatoooo!");
        return actions.getDefault();
    }

    @NotNull @Override public Action create() {
        final DateShowcase dateShowcase = DateShowcase.create(getId());
        copyTo(dateShowcase);
        dateShowcase.insert();
        sout(dateShowcase);
        return actions.getDefault();
    }

    @NotNull @Override public DateShowcase populate() {
        final DateShowcase date = super.populate();
        sout(date);
        return date;
    }

    @NotNull @Override public Action show() {
        sout(this);
        return actions.getDefault();
    }

    private void sout(DateOutput date) {
        if (isDefined(Field.DATE_FROM) && isDefined(Field.DATE_TO)) {
            final StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("\nDateFrom: ").append(date.getDateFrom());
            strBuilder.append("\t \tDateTo: ").append(date.getDateTo());
            System.out.println(strBuilder);
        }

        if (isDefined(Field.TIME_FROM) && isDefined(Field.TIME_TO)) {
            final StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("\nTimeFrom: ").append(date.getTimeFrom());
            strBuilder.append("\t\tTimeTo: ").append(date.getTimeTo());
            System.out.println(strBuilder);
        }

        if (isDefined(Field.DOUBLE_DATE_FROM) && isDefined(Field.DOUBLE_DATE_TO)) {
            final StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("\nDoubleDateFrom: ").append(date.getDoubleDateFrom());
            strBuilder.append("\t\tDoubleDateTo: ").append(date.getDoubleDateTo());
            System.out.println(strBuilder);
        }
    }
}  // end class DateShowcaseForm
