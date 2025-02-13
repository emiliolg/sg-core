
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.shiro.AuthorizationUtils;
import tekgenesis.metadata.authorization.Assignee;
import tekgenesis.metadata.authorization.User;
import tekgenesis.persistence.Initialize;
import tekgenesis.sales.basic.g.ProductDataBase;

import static tekgenesis.metadata.form.model.FormConstants.VIEW_IMG_USERS_JPG;
import static tekgenesis.persistence.EntityListenerType.AFTER_PERSIST;
import static tekgenesis.workflow.WorkItemPriority.values;

/**
 * User class for Entity: ProductData
 */
@SuppressWarnings("WeakerAccess")
public class ProductData extends ProductDataBase {

    //~ Methods ......................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void process(@NotNull ProductDataWorkItem item, @NotNull String result) {
        if ("details".equals(item.getTask())) {
            if ("submit".equals(result)) createImage();
            else createDetails();  // Clone!
        }
        else if ("images".equals(item.getTask())) createImage();
    }

    private boolean createDetails() {
        final ProductDataWorkItem details = details();
        details.setAssignee(getAssignee());
        details.setReporter(getAssignee().asString());
        details.setDescription(
            "This is a great description and a very long one too see i can just keep typing and this keeps getting bigger isn't this awesome?! should i grow indefinitely or scroll ? what to do, what to do...");
        // details.setOuName("Root Ou");
        details.setTitle("Product Detail");
        details.setPriorityCode(new Random().nextInt(values().length) + 1);
        details.insert();
        return true;
    }

    private void createImage() {
        final ProductDataWorkItem images = images();
        images.setAssignee(getAssignee());
        images.insert();
    }

    private Assignee getAssignee() {
        return AuthorizationUtils.isAuthenticated() ? AuthorizationUtils.getCurrentUser() : DUMMY_ASSIGNEE;  // Testing purpose!
    }

    //~ Methods ......................................................................................................................................

    @Initialize private static void initialize() {
        addListener(AFTER_PERSIST, ProductData::createDetails);
    }

    //~ Static Fields ................................................................................................................................

    private static final Assignee DUMMY_ASSIGNEE = new Assignee() {
            @NotNull @Override public String asString() {
                return "u:dummy";
            }

            @Override public boolean includes(@NotNull User user) {
                return false;
            }

            @NotNull @Override public String getId() {  // noinspection DuplicateStringLiteralInspection
                return "dummy";
            }

            @NotNull @Override public String getName() {
                return "Dummy";
            }

            @NotNull @Override public String getImage() {
                return VIEW_IMG_USERS_JPG;
            }
        };

    private static final long serialVersionUID = 3875571849057160957L;
}  // end class ProductData
