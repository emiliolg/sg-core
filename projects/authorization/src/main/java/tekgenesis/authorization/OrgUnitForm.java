
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.form.configuration.DynamicConfiguration;
import tekgenesis.type.DynamicTypeConverter;

import static tekgenesis.authorization.OrgUnit.getRoot;
import static tekgenesis.authorization.PropertyScope.ORG_UNIT;
import static tekgenesis.authorization.g.PropertyTable.PROPERTY;
import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * Organizational Unit form.
 */
@SuppressWarnings("WeakerAccess")
public class OrgUnitForm extends OrgUnitFormBase {

    //~ Methods ......................................................................................................................................

    /** Adds a child to the current OU. */
    @NotNull @Override public Action addChild() {
        final OrgUnitForm to = self();
        if (getTreeView() != null) {
            to.setParent(find());
            to.setTreeView(getTreeView());
        }
        to.setEdit(false);
        return actions.navigate(to);
    }

    /** Creates an org unit and stays. */
    @NotNull @Override public Action create() {
        if (isEdit()) super.update();
        else super.create();
        return actions.navigate(self());
    }

    /** Deletes a child to the current OU. */
    @NotNull @Override public Action deleteChild() {
        final OrgUnit orgUnit = getTreeView();
        if (orgUnit != null) {
            orgUnit.delete();

            final OrgUnitForm form = self();
            form.setEdit(false);
            return actions.navigate(form).withMessage(orgUnit.getName() + " Deleted");
        }
        return actions.getDefault();
    }

    /** On load, load the Org Units. */
    public void load() {
        setTreeViewOptions(Colls.listOf(getRoot()));
        final FormTable<PropsRow> properties = getProps();
        selectFrom(PROPERTY)                 //
        .where(PROPERTY.SCOPE.eq(ORG_UNIT))  //
        .forEach(property -> properties.add().populate(property));
    }

    /** Selects a node from the tree. */
    @NotNull @Override public Action nodeSelected() {
        final OrgUnit     selected = getTreeView();
        final OrgUnitForm form     = self();
        if (selected != null) {
            form.setName(selected.getName());
            final OrgUnit parent = selected.getParent();
            if (parent != null) {
                form.setParent(parent);
                final OrgUnit company = OrgUnit.getCompany(selected);
                if (company != null) form.setCompany(company.getDescription());
            }
            form.setName(selected.getName());
            form.setDescription(selected.getDescription());
            form.setEdit(true);
            if (!isEdit()) form.setTreeView(selected);
        }
        return actions.navigate(form);
    }

    /** Overriding populate to set user properties. */
    @NotNull @Override public OrgUnit populate() {
        final OrgUnit orgUnit = find();

        setName(orgUnit.getName());
        setDescription(orgUnit.getDescription());
        final OrgUnit parent = orgUnit.getParent();
        if (parent != null) setParent(parent);

        final FormTable<PropsRow> propertiesRows = getProps();

        for (final OrgUnitProperties orgUnitProperties : orgUnit.getProps())
            propertiesRows.get(getPropertyRowIndex(propertiesRows, orgUnitProperties.getProperty())).populate(orgUnitProperties);

        return orgUnit;
    }

    /** Validates the chosen parent. */
    @NotNull @Override
    @SuppressWarnings("DuplicateBooleanBranch")  // These are not duplicated, same method but different arguments!
    public Action validateParent() {
        if (isDefined(Field.PARENT) && isDefined(Field.NAME)) setValidParent(!getParent().getName().equals(getName()));
        return actions.getDefault();
    }

    private OrgUnitForm self() {
        return forms.initialize(OrgUnitForm.class);
    }

    private int getPropertyRowIndex(FormTable<PropsRow> propertiesRows, Property property) {
        int i = 0;
        for (final PropsRow row : propertiesRows) {
            if (row.getProperty().equals(property)) return i;
            i++;
        }
        return -1;
    }

    //~ Inner Classes ................................................................................................................................

    public class PropsRow extends PropsRowBase {
        @Override public void copyTo(@NotNull OrgUnitProperties orgUnitProperties) {
            super.copyTo(orgUnitProperties);
            final DynamicTypeConverter converter = getDynamicConversion();
            orgUnitProperties.setValue(converter.toString(ensureNotNull(getValue(), "Null value.")));
        }
        /** Populate row with orgUnitProperties instance. */
        @Override public void populate(@NotNull OrgUnitProperties orgUnitProperties) {
            final Property property = orgUnitProperties.getProperty();
            populate(property);
            setValue(getDynamicConversion().fromString(orgUnitProperties.getValue()));
        }

        private void populate(@NotNull final Property p) {
            setProperty(p);
            setType(p.getType());
            setRequired(p.isRequired());

            p.configureDynamicType(this.<DynamicConfiguration>configuration(Field.VALUE).getTypeConfiguration());
        }

        private DynamicTypeConverter getDynamicConversion() {
            final DynamicConfiguration configuration = configuration(Field.VALUE);
            return configuration.getTypeConverter();
        }
    }
}  // end class OrgUnitForm
