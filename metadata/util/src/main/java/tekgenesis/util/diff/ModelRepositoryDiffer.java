
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import tekgenesis.check.Check;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.expr.Expression;
import tekgenesis.field.FieldOption;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.EnumType;
import tekgenesis.type.EnumValue;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.util.diff.DiffConstants.CHECK;

/**
 * A differ for a {@link ModelRepository}.
 */
@SuppressWarnings("WeakerAccess")
public class ModelRepositoryDiffer {

    //~ Constructors .................................................................................................................................

    private ModelRepositoryDiffer() {}

    //~ Methods ......................................................................................................................................

    /** Diff two {@link ModelRepository}. */
    public static ChangeSet diff(ModelRepository left, ModelRepository right) {
        final ChangeSet changeSet = new ChangeSet();
        diffDomains(left, right, changeSet);
        return changeSet;
    }

    private static boolean checksEqual(final Tuple<Tuple<Expression, String>, Tuple<Expression, String>> checks) {
        return checks != null && expressionsEqual(tuple(checks.first().first(), checks.second().first())) &&
               equal(checks.first().second(), checks.second().second());
    }

    private static void difAttributeSeq(Seq<Attribute> leftAttributes, Seq<Attribute> rightAttributes, String text, ChangeSet changeSet) {
        if (!leftAttributes.equals(rightAttributes)) changeSet.add(new AttributeSeqChanged(leftAttributes, rightAttributes, text));
    }

    @Nullable
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static Change diffAttribute(Attribute left, Attribute right) {
        final ChangeSet changeSet = new ChangeSet();
        diffLabel(left, right, changeSet);
        if (!left.getTypeAsString().equals(right.getTypeAsString())) changeSet.add(new AttributeTypeChanged(left, right.getTypeAsString()));
        diffCheck(left, left.getCheck(), right.getCheck(), changeSet);
        diffOptionExpression(left, left.getDefaultValue(), right.getDefaultValue(), changeSet, FieldOption.DEFAULT);
        diffOptionExpression(left, left.getOptional(), right.getOptional(), changeSet, FieldOption.OPTIONAL);
        if (!left.getMask().equals(right.getMask()))
            changeSet.add(new AttributeOptionChanged(left, left.getMask(), right.getMask(), FieldOption.CUSTOM_MASK));
        if (left.isSigned() != right.isSigned())
            changeSet.add(new AttributeOptionChanged(left, left.isSigned(), right.isSigned(), FieldOption.SIGNED));

        if (left.isMultiple() != right.isMultiple())
            changeSet.add(new AttributeOptionChanged(left, left.isMultiple(), right.isMultiple(), FieldOption.MULTIPLE));
        if (!left.getReverseReference().equals(right.getReverseReference()))
            changeSet.add(new AttributeOptionChanged(left, left.getReverseReference(), right.getReverseReference(), "using"));

        return changeSet.isEmpty() ? null : changeSet;
    }

    private static <T> void diffAttributeOptions(Attribute attribute, List<T> oldValues, List<T> newValues, ChangeSet changeSet, String option,
                                                 Predicate<Tuple<T, T>> equals) {
        if (oldValues.size() != newValues.size()) changeSet.add(new AttributeOptionChanged(attribute, oldValues, newValues, option));
        else {
            for (final T oldValue : oldValues) {
                boolean notFound = true;
                for (final T newValue : newValues) {
                    if (equals.test(Tuple.tuple(oldValue, newValue))) notFound = false;
                }
                if (notFound) {
                    changeSet.add(new AttributeOptionChanged(attribute, oldValues, newValues, option));
                    break;
                }
            }
        }
    }  // end method diffAttributeOptions

    private static void diffCheck(Attribute attribute, List<Check> oldValue, List<Check> newValue, ChangeSet changeSet) {
        final List<Tuple<Expression, String>> oldChecks = new ArrayList<>();
        for (final Check check : oldValue)
            oldChecks.add(Tuple.tuple(check.getExpr(), check.getMsgText()));

        final List<Tuple<Expression, String>> newChecks = new ArrayList<>();
        for (final Check check : newValue)
            newChecks.add(Tuple.tuple(check.getExpr(), check.getMsgText()));

        diffAttributeOptions(attribute, oldChecks, newChecks, changeSet, CHECK, ModelRepositoryDiffer::checksEqual);
    }

    private static void diffDomains(ModelRepository left, ModelRepository right, ChangeSet changeSet) {
        final Collection<String> leftDomains  = left.getDomains();
        final Collection<String> rightDomains = right.getDomains();
        for (final String domainName : leftDomains) {
            if (!rightDomains.contains(domainName)) changeSet.add(new DomainRemovedChange(domainName));
            else {
                final Change change = diffModelsForDomain(left, right, domainName);
                if (change != null) changeSet.add(change);
            }
        }
        for (final String domainName : rightDomains) {
            if (!leftDomains.contains(domainName)) changeSet.add(new DomainRemovedChange(domainName));
        }
    }  // end method diffDomains

    @Nullable private static Change diffEntity(Entity leftModel, Entity rightModel) {
        final ChangeSet changeSet = new ChangeSet();
        diffLabel(leftModel, rightModel, changeSet);
        difFieldSeq(leftModel.describes(), rightModel.describes(), "describe_by", changeSet);
        difAttributeSeq(leftModel.getPrimaryKey(), rightModel.getPrimaryKey(), Constants.PRIMARY_KEY, changeSet);
        final Seq<Attribute> leftAttributes = leftModel.attributes();
        for (final Attribute leftAttribute : leftAttributes) {
            final Option<Attribute> rightAttribute = rightModel.getAttribute(leftAttribute.getName());
            if (rightAttribute.isEmpty()) changeSet.add(new AttributeRemoved(leftModel, leftAttribute.getName()));
            else {
                final Change change = diffAttribute(leftAttribute, rightAttribute.get());
                if (change != null) changeSet.add(change);
            }
        }
        for (final Attribute rightAttribute : rightModel.attributes()) {
            if (leftModel.getAttribute(rightAttribute.getName()).isEmpty()) changeSet.add(new AttributeAdded(leftModel, rightAttribute.getName()));
        }
        return changeSet.isEmpty() ? null : changeSet;
    }  // end method diffEntity

    @Nullable private static Change diffEnum(EnumType leftModel, EnumType rightModel) {
        final ChangeSet changeSet = new ChangeSet();
        diffLabel(leftModel, rightModel, changeSet);
        final Seq<EnumValue> lefts  = leftModel.getValues();
        final Seq<EnumValue> rights = rightModel.getValues();

        for (final EnumValue left : lefts) {
            final String leftId = left.getName();
            if (!rightModel.contains(leftId)) changeSet.add(new EnumIdRemoved(leftModel, leftId));
            else {
                final String leftLabel  = left.getLabel();
                final String rightLabel = rightModel.getLabel(leftId);
                if (!leftLabel.equals(rightLabel)) changeSet.add(new EnumLabelChanged(leftModel, leftId, leftLabel, rightLabel));
            }
        }
        for (final EnumValue right : rights) {
            final String rightId = right.getName();
            if (!leftModel.contains(rightId)) changeSet.add(new EnumIdRemoved(rightModel, rightId));
        }
        return changeSet.isEmpty() ? null : changeSet;
    }  // end method diffEnum

    @Nullable private static Change diffForm(Form leftModel, Form rightModel) {
        final ChangeSet changeSet = new ChangeSet();
        diffLabel(leftModel, rightModel, changeSet);

        return changeSet.isEmpty() ? null : changeSet;
    }

    private static void difFieldSeq(Seq<ModelField> leftFields, Seq<ModelField> rightFields, String text, ChangeSet changeSet) {
        if (!leftFields.equals(rightFields)) changeSet.add(new AttributeSeqChanged(leftFields, rightFields, text));
    }

    private static void diffLabel(MetaModel leftModel, MetaModel rightModel, ChangeSet changeSet) {
        final String leftLabel  = leftModel.getLabel();
        final String rightLabel = rightModel.getLabel();
        if (!leftLabel.equals(rightLabel)) changeSet.add(new LabelChanged(leftModel.getFullName(), leftLabel, rightLabel));
    }
    private static void diffLabel(Attribute leftModel, Attribute rightModel, ChangeSet changeSet) {
        final String leftLabel  = leftModel.getLabel();
        final String rightLabel = rightModel.getLabel();
        if (!leftLabel.equals(rightLabel)) changeSet.add(new LabelChanged(leftModel.getFullName(), leftLabel, rightLabel));
    }

    @Nullable private static Change diffModel(MetaModel leftModel, MetaModel rightModel) {
        if (!leftModel.getClass().getName().equals(rightModel.getClass().getName())) return new ModelTypeChanged(leftModel, rightModel);

        if (leftModel instanceof Entity) return diffEntity((Entity) leftModel, (Entity) rightModel);
        if (leftModel instanceof EnumType) return diffEnum((EnumType) leftModel, ((EnumType) rightModel));
        if (leftModel instanceof Form) return diffForm((Form) leftModel, (Form) rightModel);
        // throw new IllegalStateException("Type " + leftModel.getClass() + " does not have diff implemented");
        return null;
    }

    @Nullable private static Change diffModelsForDomain(ModelRepository left, ModelRepository right, String domainName) {
        final ChangeSet changeSet = new ChangeSet();
        for (final MetaModel leftModel : left.getModels(domainName)) {
            final Option<MetaModel> rightModel = right.getModel(leftModel.getKey());
            if (rightModel.isEmpty()) changeSet.add(new ModelRemovedChange(leftModel));
            else {
                final Change change = diffModel(leftModel, rightModel.get());
                if (change != null) changeSet.add(change);
            }
        }
        for (final MetaModel rightModel : right.getModels(domainName)) {
            final Option<MetaModel> leftModel = left.getModel(rightModel.getKey());
            if (leftModel.isEmpty()) changeSet.add(new ModelAddedChange(rightModel));
        }
        return !changeSet.isEmpty() ? changeSet : null;
    }

    private static void diffOptionExpression(Attribute attribute, Expression oldValue, Expression newValue, ChangeSet changeSet, FieldOption option) {
        if (oldValue.isNull()) {
            if (!newValue.isNull()) changeSet.add(new AttributeOptionAdded(attribute, option));
        }
        else if (newValue.isNull()) changeSet.add(new AttributeOptionRemoved(attribute, option));
        else if (!equal(oldValue.toString(), newValue.toString())) changeSet.add(new AttributeOptionChanged(attribute, oldValue, newValue, option));
    }

    private static boolean expressionsEqual(final Tuple<Expression, Expression> expressions) {
        return expressions != null && equal(expressions.first().toString(), expressions.second().toString());
    }
}
