
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.form.Action;

import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.collections.Colls.listOf;

/**
 * User class for Form: ComboboxForm
 */
public class OptionWidgetsForm extends OptionWidgetsFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        final ImmutableList<SimpleEntity> list    = SimpleEntity.list().list();
        final ImmutableList<SimpleEntity> options = list.isEmpty() ? emptyList() : listOf(list.get(1), list.get(2));
        setSimpleEOptions(options);
        setTagsSimpEOptions(options);
        setPickSimpEOptions(options);
        setBreadcrumbSimpEOptions(options);
        setRadioSimpEOptions(options);
        setCheckSimpEOptions(options);
    }

    @NotNull @Override public Action validate() {
        return actions().getDefault();
    }

    @NotNull @Override public Action setBreadcrumbE() {
        final ImmutableList<SimpleEntity> list = SimpleEntity.list().list();
        if (!list.isEmpty()) setBreadcrumbSimpEOptions(listOf(list.get(3), list.get(4)));
        return actions().getDefault();
    }

    @NotNull @Override public Action setBreadcrumbEOptions() {
        setBreadcrumbSimpEOptions(SimpleEntity.list().list());
        return actions().getDefault();
    }

    @NotNull @Override public Action setCheckGroupE() {
        final ImmutableList<SimpleEntity> list = SimpleEntity.list().list();
        if (!list.isEmpty()) {
            setCheckSimpEOptions(listOf(list.get(3), list.get(4)));
            setCheckSimpE(listOf(list.get(4), list.get(5)));
        }
        return actions().getDefault();
    }

    @NotNull @Override public Action setCheckGroupEOptions() {
        setCheckSimpEOptions(SimpleEntity.list().list());
        return actions().getDefault();
    }

    @NotNull @Override public Action setPicklistE() {
        final ImmutableList<SimpleEntity> list = SimpleEntity.list().list();
        if (!list.isEmpty()) {
            setPickSimpEOptions(listOf(list.get(3), list.get(4)));
            setPickSimpE(listOf(list.get(4), list.get(5)));
        }
        return actions().getDefault();
    }

    @NotNull @Override public Action setPicklistEOptions() {
        setPickSimpEOptions(SimpleEntity.list().list());
        return actions().getDefault();
    }

    @NotNull @Override public Action setRadioGroupE() {
        final ImmutableList<SimpleEntity> list = SimpleEntity.list().list();
        if (!list.isEmpty()) {
            setRadioSimpEOptions(listOf(list.get(3), list.get(4)));
            setRadioSimpE(list.get(5));
        }
        return actions().getDefault();
    }

    @NotNull @Override public Action setRadioGroupEOptions() {
        setRadioSimpEOptions(SimpleEntity.list().list());
        return actions().getDefault();
    }

    @NotNull @Override public Action setSimpleE() {
        final ImmutableList<SimpleEntity> list = SimpleEntity.list().list();
        if (!list.isEmpty()) {
            setSimpleEOptions(listOf(list.get(3), list.get(4)));
            setSimpleE(list.get(5));
        }
        return actions().getDefault();
    }

    @NotNull @Override public Action setSimpleEOptions() {
        setSimpleEOptions(SimpleEntity.list().list());
        return actions().getDefault();
    }

    @NotNull @Override public Action setTagsE() {
        final ImmutableList<SimpleEntity> list = SimpleEntity.list().list();
        setTagsSimpEOptions(listOf(list.get(3), list.get(4)));
        setTagsSimpE(listOf(list.get(4), list.get(5)));
        return actions().getDefault();
    }

    @NotNull @Override public Action setTagsEOptions() {
        setTagsSimpEOptions(SimpleEntity.list().list());
        return actions().getDefault();
    }
}  // end class OptionWidgetsForm
