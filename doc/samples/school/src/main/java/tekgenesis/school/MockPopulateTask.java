
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

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.Select;
import tekgenesis.school.g.CourseHomeWorkTable;
import tekgenesis.school.g.TestTable;
import tekgenesis.task.LifecycleTask;
import tekgenesis.task.Status;

/**
 * User class for Task: MockPopulateTask
 */
public class MockPopulateTask extends MockPopulateTaskBase {

    //~ Constructors .................................................................................................................................

    private MockPopulateTask(@NotNull LifecycleTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Status onShutdown() {
        throw new IllegalStateException("not implemented");
    }

    @NotNull @Override public Status onStartup() {
        createCourses(5, " sala ", new String[] { "A", "B", "C" });
        createCourses(7, " grado ", new String[] { "A", "B", "C" });
        createCourses(5, "a±no", new String[] { "Comercial", "Bachiller" });

        final ImmutableList<Course> courses = Course.list().list();

        // for(final Course course:courses) {
        // CourseHomeWork.create().setCourseId(course.getId()).setYear(YEAR).persist();
        // CourseHomeWork.create().setCourseId(course.getId()).setYear(YEAR).persist();
        // CourseHomeWork.create().setCourseId(course.getId()).setYear(YEAR).persist();
        // CourseHomeWork.create().setCourseId(course.getId()).setYear(YEAR).persist();
        // Test.create().setCourseId(course.getId()).setYear(YEAR).persist();
        // Test.create().setCourseId(course.getId()).setYear(YEAR).persist();
        // Test.create().setCourseId(course.getId()).setYear(YEAR).persist();
        // Test.create().setCourseId(course.getId()).setYear(YEAR).persist();
        // }

        for (int i = 0; i < 10; i++) {
            final Family family = Family.create(i).setFamilyName("LastName" + i).persist();
            for (int j = 0; j < Math.random() * (MAX_CHILD); j++) {
                final Course  currentCourse = courses.get((int) (Math.random() * courses.size()));
                final Student student       = Student.create(i * 100 + j + "")
                                              .setBirthday(DateOnly.current())
                                              .setCurrentCourse(currentCourse)
                                              .setFamily(family)
                                              .setNames("Name " + i)
                                              .setLastNames(family.getFamilyName())
                                              .setSex(Sex.Female)
                                              .setStartYear(2001)
                                              .persist();
                // should check year.
                final Select<CourseHomeWork> homeworks = CourseHomeWork.listWhere(
                        CourseHomeWorkTable.COURSE_HOME_WORK.COURSE_ID.eq(currentCourse.getId()));
                final Select<Test>           tests     = Test.listWhere(TestTable.TEST.COURSE_ID.eq(currentCourse.getId()));
                // Test.forEach(Test.selectFrom());
                for (final CourseHomeWork homework : homeworks.list())
                    HomeWorkGrade.create().setHomeWork(homework).setHomeWorkGrade(6).setHomeWorkGradeBase(10).setHomeWorkStudent(student).persist();
                for (final Test test : tests.list())
                    TestGrade.create().setTest(test).setGrade(6).setGradeBase(10).setStudent(student).persist();
            }
        }

        return Status.ok();
    }  // end method onStartup

    private void createCourses(final int gradeCount, final String descriptionPart, final String[] divisions) {
        for (int grade = 1; grade <= gradeCount; grade++) {
            for (final String division : divisions) {
                final Course course = Course.create()
                                      .setDescription(grade + descriptionPart + division)
                                      .setDivision(division)
                                      .setGrade(grade)
                                      .persist();
                // final int    testCount     = (int) (Math.random() * 10);
                // final int    homeWorkCount = (int) (Math.random() * 30);
                CourseHomeWork.create()
                    .setCourseId(course.getId())
                    .setYear(YEAR)
                    .setDueDate(DateTime.current())
                    .setGivenDate(DateTime.current())
                    .persist();
                Test.create().setCourseId(course.getId()).setYear(YEAR).setDate(DateTime.current()).persist();
                CourseHomeWork.create()
                    .setCourseId(course.getId())
                    .setYear(YEAR)
                    .setDueDate(DateTime.current())
                    .setGivenDate(DateTime.current())
                    .persist();
                Test.create().setCourseId(course.getId()).setYear(YEAR).setDate(DateTime.current()).persist();
                CourseHomeWork.create()
                    .setCourseId(course.getId())
                    .setYear(YEAR)
                    .setDueDate(DateTime.current())
                    .setGivenDate(DateTime.current())
                    .persist();
                Test.create().setCourseId(course.getId()).setYear(YEAR).setDate(DateTime.current()).persist();
                CourseHomeWork.create()
                    .setCourseId(course.getId())
                    .setYear(YEAR)
                    .setDueDate(DateTime.current())
                    .setGivenDate(DateTime.current())
                    .persist();
                Test.create().setCourseId(course.getId()).setYear(YEAR).setDate(DateTime.current()).persist();
                // CourseHomeWork.create().setCourseId(course.getId()).setYear(YEAR).persist();
                // CourseHomeWork.create().setCourseId(course.getId()).setYear(YEAR).persist();
                // CourseHomeWork.create().setCourseId(course.getId()).setYear(YEAR).persist();
                // CourseHomeWork.create().setCourseId(course.getId()).setYear(YEAR).persist();
                // Test.create().setCourseId(course.getId()).setYear(YEAR).persist();
                // Test.create().setCourseId(course.getId()).setYear(YEAR).persist();
                // Test.create().setCourseId(course.getId()).setYear(YEAR).persist();
                // Test.create().setCourseId(course.getId()).setYear(YEAR).persist();
            }
        }
    }  // end method createCourses

    //~ Static Fields ................................................................................................................................

    private static final int MAX_CHILD = 12;

    private static final int YEAR = 2016;
}  // end class MockPopulateTask
