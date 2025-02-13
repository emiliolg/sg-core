
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet.gwtservices;

import tekgenesis.common.core.Constants;
import tekgenesis.expr.ExpressionFactory;
import tekgenesis.metadata.common.ModelLinkerImpl;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormBuilder;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.Types;
import tekgenesis.view.client.FormViewMessages;

import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.metadata.form.model.FormConstants.INPUT_XXLARGE;
import static tekgenesis.metadata.form.widget.ButtonType.CANCEL;
import static tekgenesis.metadata.form.widget.ButtonType.SAVE;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.*;

class BootService {

    //~ Constructors .................................................................................................................................

    private BootService() {}

    //~ Methods ......................................................................................................................................

    static Form createFeedbackForm() {
        final FormBuilder     builder    = form("/dev/null", "tekgenesis.boot", "FeedbackForm");
        final ModelRepository repository = new ModelRepository();
        builder.withRepository(repository);

        try {
            // noinspection DuplicateStringLiteralInspection
            builder.addWidget(field(FormViewMessages.MSGS.summary()).id(Constants.SUMMARY).contentStyleClass(INPUT_XXLARGE).length(SUMMARY_LENGTH));
            builder.addWidget(
                area(FormViewMessages.MSGS.description()).displayedRows(10)
                                                         .id(Constants.DESCRIPTION)
                                                         .contentStyleClass(INPUT_XXLARGE)
                                                         .length(DESCRIPTION_LENGTH));
            builder.addWidget(internal(Constants.HIDE_TYPE).withType(Types.booleanType()));
            builder.addWidget(radioGroup(FormViewMessages.MSGS.type()).id(Constants.TYPE).hide(ExpressionFactory.ref(Constants.HIDE_TYPE)));
            builder.addWidget(footer().addWidget(button(FormViewMessages.MSGS.report()).buttonType(SAVE)).addWidget(button(CANCEL)));

            final Form form = builder.build();
            ModelLinkerImpl.linkForm(repository, form);
            return form;
        }
        catch (final BuilderException ignore) {}

        throw unreachable();
    }

    //~ Static Fields ................................................................................................................................

    private static final int SUMMARY_LENGTH     = 255;
    private static final int DESCRIPTION_LENGTH = 2000;
}  // end class BootService
