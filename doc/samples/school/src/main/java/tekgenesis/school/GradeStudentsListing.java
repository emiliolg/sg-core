
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.school;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.FormTable;

/**
 * User class for Form: GradeStudentsListing
 */
public class GradeStudentsListing extends GradeStudentsListingBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action doLoadStudents() {
        getGradeStudents().clear();
        loadStudents();
        return actions.getDefault();
    }

    private void loadStudents() {
        final FormTable<GradeStudentsRow> table = getGradeStudents();
        if (isDefined(Field.COURSE)) {
            Student.forEach(currentStudent -> {
                if (currentStudent.getCurrentCourse() == getCourse()) table.add().populate(currentStudent);
            });
        }
        // Student.forEach(currentStudent -> table.add().populate(currentStudent));
        // super.loadStudents();
    }

    //~ Inner Classes ................................................................................................................................

    public class GradeStudentsRow extends GradeStudentsRowBase {}
}
