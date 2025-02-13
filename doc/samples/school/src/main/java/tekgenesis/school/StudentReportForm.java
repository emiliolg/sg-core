
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

import tekgenesis.common.core.Mutable;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.persistence.Select;
import tekgenesis.school.g.CourseHomeWorkTable;
import tekgenesis.school.g.HomeWorkGradeTable;
import tekgenesis.school.g.TestGradeTable;
import tekgenesis.school.g.TestTable;

import static tekgenesis.persistence.Sql.selectFrom;

/**
 * User class for Form: StudentReportForm
 */
public class StudentReportForm extends StudentReportFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action studentChanged() {
        loadHomeworkGrades();
        loadTestsGrades();
        return actions.getDefault();
    }

    private void loadHomeworkGrades() {
        final FormTable<HomeworkGradesRow> homeworkGrades = getHomeworkGrades();
        homeworkGrades.clear();

        final Select<HomeWorkGrade> grades   = selectFrom(HomeWorkGradeTable.HOME_WORK_GRADE).join(CourseHomeWorkTable.COURSE_HOME_WORK,
                    HomeWorkGradeTable.HOME_WORK_GRADE.HOME_WORK_ID.eq(CourseHomeWorkTable.COURSE_HOME_WORK.ID))
                                               .where(
                HomeWorkGradeTable.HOME_WORK_GRADE.HOME_WORK_STUDENT_ID.eq(getFormStudent().getId())
                                                                       .and(
                                                                           CourseHomeWorkTable.COURSE_HOME_WORK.COURSE_ID.eq(
                                                                               getFormStudent().getCurrentCourse().getId())));
        final Mutable.Double        gradeSum = new Mutable.Double();

        grades.forEach(grade -> {
            homeworkGrades.add().populate(grade);
            gradeSum.add((double) grade.getHomeWorkGrade() / (double) grade.getHomeWorkGradeBase());
        });
        final int count = grades.size();
        setHomeworkAverage(gradeSum.value() / count);
    }
    // no contempla repetidores...
    private void loadTestsGrades() {
        final FormTable<TestGradesRow> testGrades = getTestGrades();
        testGrades.clear();
        final Select<TestGrade> grades = selectFrom(TestGradeTable.TEST_GRADE).join(TestTable.TEST,
                    TestGradeTable.TEST_GRADE.TEST_ID.eq(TestTable.TEST.ID))
                                         .where(
                TestGradeTable.TEST_GRADE.STUDENT_ID.eq(getFormStudent().getId())
                                                    .and(TestTable.TEST.COURSE_ID.eq(getFormStudent().getCurrentCourse().getId())));
        // select(TestGradeTable.TEST_GRADE.GRADE_BASE.add(TestGradeTable.TEST_GRADE.GRADE_BASE),
        // TestGradeTable.TEST_GRADE.GRADE_BASE.su);
        final Mutable.Double gradeSum = new Mutable.Double();
        grades.forEach(grade -> {
            testGrades.add().populate(grade);
            gradeSum.add((double) grade.getGrade() / (double) grade.getGradeBase());
        });
        final int count = grades.size();
        setTestAverage(gradeSum.value() / count);
    }

    //~ Inner Classes ................................................................................................................................

    public class HomeworkGradesRow extends HomeworkGradesRowBase {}

    public class TestGradesRow extends TestGradesRowBase {}
}  // end class StudentReportForm
