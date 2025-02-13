
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.check.CheckMsg;
import tekgenesis.check.CheckType;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.exprs.ValidationListener;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.collections.Colls.immutable;

class ValidationsImpl implements Validations, ValidationListener {

    //~ Instance Fields ..............................................................................................................................

    private final Map<Integer, ImmutableList<Validation>> validations;

    //~ Constructors .................................................................................................................................

    ValidationsImpl() {
        validations = new HashMap<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public void onValidation(Widget widget, Model model, Option<Integer> item, Option<String> subformPath, Seq<CheckMsg> messages) {
        // Row is being discarded! Concept of scoped validations should be implemented!
        if (!messages.isEmpty()) validations.put(widget.getFieldSlot(), ValidationImpl.map(messages));
    }

    @NotNull @Override public Seq<Validation> getAllMessages() {
        final List<Validation> result = new ArrayList<>();
        for (final ImmutableList<Validation> list : validations.values())
            result.addAll(list);
        return immutable(result);
    }

    @NotNull @Override public Seq<Validation> getMessages(@NotNull Enum<?> field) {
        final ImmutableList<Validation> result = validations.get(field.ordinal());
        return result == null ? Colls.emptyList() : result;
    }

    @Override public boolean isEmpty() {
        return validations.isEmpty();
    }

    //~ Inner Classes ................................................................................................................................

    static class ValidationImpl implements Validation {
        private final String         msg;
        private final ValidationType type;

        private ValidationImpl(ValidationType type, String msg) {
            this.msg  = msg;
            this.type = type;
        }

        @Override public String toString() {
            return "Validation {msg='" + msg + '\'' + ", type=" + type + '}';
        }

        @Override public String getMessage() {
            return msg;
        }

        @Override public ValidationType getType() {
            return type;
        }

        @NotNull static ImmutableList<Validation> map(@NotNull final Seq<CheckMsg> messages) {
            if (messages.isEmpty()) return emptyList();
            return messages.map(ValidationImpl::validationFor).toList();
        }

        @NotNull private static Validation validationFor(final CheckMsg checkMsg) {
            return new ValidationImpl(getValidationType(checkMsg.getType()), checkMsg.getText());
        }

        private static ValidationType getValidationType(CheckType type) {
            final ValidationType result;
            switch (type) {
            case ERROR:
                result = ValidationType.ERROR;
                break;
            case WARNING:
                result = ValidationType.WARNING;
                break;
            case SUCCESS:
                result = ValidationType.SUCCESS;
                break;
            default:
                result = ValidationType.INFO;
                break;
            }
            return result;
        }
    }  // end class ValidationImpl
}  // end class ValidationsImpl
