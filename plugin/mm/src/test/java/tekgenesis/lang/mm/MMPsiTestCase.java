
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.testFramework.PsiTestCase;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Base MMPsi Test Cases.
 */
public class MMPsiTestCase extends PsiTestCase {

    //~ Methods ......................................................................................................................................

    /** .* */
    public void testEmpty() {
        final Boolean condition = true;
        // noinspection ConstantConditions
        assertThat(condition).isTrue();
    }
    @Override public void setUp()
        throws Exception
    {
        System.getProperties().remove(TestAction.IDEA_PLATFORM_PREFIX);
        System.getProperties().put("idea.home.path", "/tmp");
        super.setUp();
    }
}
