
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.configuration.RadioGroupConfiguration;
import tekgenesis.model.KeyMap;

import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.showcase.DisplayRadioTagBase.Field.RADIO_TAGS;
import static tekgenesis.showcase.g.SimpleEntityTable.SIMPLE_ENTITY;

/**
 * User class for Form: DisplayRadioTag
 */
public class DisplayRadioTag extends DisplayRadioTagBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changed() {
        return actions.navigate(SimpleEntityForm.class, getRadioTagsKey());
    }

    @NotNull @Override public Action changeStyles() {
        final List<String> cs = new ArrayList<>();
        final long         s  = selectFrom(SIMPLE_ENTITY).count();
        final Random       r  = new Random();

        for (int i = 0; i < s; i++) {
            if (isCheckBox()) cs.add(BTN_CLASSES.get(r.nextInt(4)));
            else cs.add(ALERT_CLASSES.get(r.nextInt(3)));
        }

        this.<RadioGroupConfiguration>configuration(RADIO_TAGS).styleClasses(cs);
        return actions.getDefault();
    }

    @Override public void load() {
        final KeyMap ks = KeyMap.create();
        final int[]  i  = new int[1];
        i[0] = 0;

        final List<String> cs = new ArrayList<>();
        final Random       r  = new Random();
        SimpleEntity.forEach(s -> {
            ks.put(s.keyAsString(), s.toString() + " " + i[0]);
            cs.add(ALERT_CLASSES.get(r.nextInt(3)));
            i[0]++;
        });
        setRadioTagsOptions(ks);
        this.<RadioGroupConfiguration>configuration(RADIO_TAGS).styleClasses(cs);
    }

    //~ Static Fields ................................................................................................................................

    private static final List<String> ALERT_CLASSES = new ArrayList<>();
    private static final List<String> BTN_CLASSES   = new ArrayList<>();

    static {
        ALERT_CLASSES.add("alert-error");
        ALERT_CLASSES.add("alert-info");
        ALERT_CLASSES.add("alert-success");

        BTN_CLASSES.add("btn btn-danger");
        BTN_CLASSES.add("btn btn-warning");
        BTN_CLASSES.add("btn btn-success");
        BTN_CLASSES.add("btn btn-info");
    }
}  // end class DisplayRadioTag
