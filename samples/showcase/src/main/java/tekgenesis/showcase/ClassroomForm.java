
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.form.SwipeLoader;

import static tekgenesis.showcase.Gender.FEMALE;
import static tekgenesis.showcase.Gender.MALE;

/**
 * Classroom Form class.
 */
@SuppressWarnings("WeakerAccess")
public class ClassroomForm extends ClassroomFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action clear() {
        getStudents().clear();
        return actions.getDefault();
    }

    @NotNull @Override public Action clearAndFill() {
        getStudents().clear();
        fill();
        return actions.getDefault();
    }

    @NotNull @Override public Action fill() {
        return innerFill(10);
    }

    @NotNull @Override public Action first() {
        getStudents().setCurrentPage(1);
        return actions.getDefault();
    }

    @NotNull @Override public Action navigateToWidgetShowcase() {
        return actions.navigate(SimpleEntityForm.class).leaveWithConfirmation();
    }

    @NotNull @Override public Action random() {
        final Random r      = new Random();
        final int    random = r.nextInt(UPPER_LIMIT);
        setRows(random);
        return innerFill(random);
    }

    @NotNull @Override public Action rowClicked() {
        final Action action;

        if (isSwipe())
            action = actions.swipe(StudentLoader.class, getStudents().getCurrentIndex().get(), getStudents().size())
                     .dimension(WIDTH, HEIGHT)
                     .fetchSize(5)
                     .marginTop(MARGIN_TOP);
        else {
            final StudentForm to = forms.initialize(StudentForm.class);
            to.setDni(getStudents().getCurrent().getDni() + "");
            action = actions.detail(to).dimension(WIDTH, HEIGHT).marginTop(MARGIN_TOP);
        }

        return action;
    }  // end method rowClicked

    @NotNull @Override public Action second() {
        getStudents().setCurrentPage(2);
        return actions.getDefault();
    }

    private Action innerFill(int count) {
        final FormTable<StudentsRow> students = getStudents();

        for (int i = 0; i < count; i++) {
            final StudentsRow row = students.add();
            row.setAge(10 + i);
            row.setDni(row.getAge() * row.getAge());
            row.setFirstName("F" + i);
            row.setLastName("L" + i);
            // Bendito entre todas las mujeres!
            row.setGender(row.getAge() % 3 == 0 ? MALE : FEMALE);
        }

        return actions.getDefault();
    }

    //~ Static Fields ................................................................................................................................

    private static final int UPPER_LIMIT = 20;

    private static final int MARGIN_TOP = 50;

    private static final int WIDTH  = 854;
    private static final int HEIGHT = 480;

    //~ Inner Classes ................................................................................................................................

    public class StudentLoader implements SwipeLoader<StudentForm> {
        @Override public StudentForm load(int index) {
            final StudentForm to = forms.initialize(StudentForm.class);
            to.setDni(getStudents().get(index).getDni() + "");
            return to;
        }
    }

    public class StudentsRow extends StudentsRowBase {
        @NotNull @Override public Action swipe() {
            return rowClicked();
        }
    }
}  // end class ClassroomForm
