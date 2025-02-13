
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.form.Action;
import tekgenesis.form.Suggestion;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.showcase.SuggestBoxShowcaseBase.Field.ENTITY_TAGS;
import static tekgenesis.showcase.SuggestBoxShowcaseBase.Field.INTEGER_TAGS;
import static tekgenesis.showcase.SuggestBoxShowcaseBase.Field.OPTION_TAGS;
import static tekgenesis.showcase.SuggestBoxShowcaseBase.Field.STRING_TAGS;
import static tekgenesis.showcase.g.SimpleEntityTable.SIMPLE_ENTITY;

/**
 * Suggest Box Showcase form class.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class SuggestBoxShowcase extends SuggestBoxShowcaseBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changed() {
        if (isDefined(Field.STRINGS)) System.out.println("Seleccionó... " + getStrings());
        return actions.getDefault();
    }

    @NotNull @Override public Action clear() {
        reset(OPTION_TAGS, ENTITY_TAGS, INTEGER_TAGS, STRING_TAGS);
        return actions.getDefault();
    }

    @NotNull @Override public Action click() {
        if (isDefined(Field.OPTIONS)) System.out.println("Options suggest value is " + getOptions());

        if (isDefined(Field.ENTITY)) System.out.println("Entity suggest value is " + getEntity() + " with key " + getEntityKey());

        if (isDefined(Field.ENTITY_WITH_SUGGEST))
            System.out.println("Entity with on suggest value is " + getEntityWithSuggest() + " with key " + getEntityWithSuggestKey());

        if (isDefined(Field.INTEGER)) System.out.println("Integer suggest value is " + getInteger());

        if (isDefined(Field.STRINGS)) System.out.println("String suggest value is " + getStrings());

        if (isDefined(Field.STRINGS_SYNC)) System.out.println("String sync suggest value is " + getStringsSync());

        if (isDefined(Field.ENTITY_SYNC)) System.out.println("Entity sync suggest value is " + getEntitySync() + " with key " + getEntitySyncKey());

        for (final Options tag : getOptionTags())
            System.out.println("Tag = " + tag);

        for (final SimpleEntity tag : getEntityTags())
            System.out.println("Tag = " + tag);

        for (final Integer tag : getIntegerTags())
            System.out.println("Tag = " + tag);

        for (final String tag : getStringTags())
            System.out.println("Tag = " + tag);

        for (final String tag : getStringsTagsSync())
            System.out.println("Tag = " + tag);

        for (final SimpleEntity tag : getEntityTagsSync())
            System.out.println("Tag = " + tag);

        setEntityTags(Colls.listOf(SimpleEntity.list().getFirst().get()));

        return actions.getDefault();
    }

    @NotNull @Override public Action entityChanged() {
        if (isDefined(Field.ENTITY)) System.out.println("getEntity() = " + getEntity());
        return actions.getDefault();
    }

    @NotNull @Override public Iterable<SimpleEntity> entitySuggestSync(@Nullable String query) {
        return selectFrom(SIMPLE_ENTITY).list();
    }

    @NotNull @Override public Action entityTagsSyncChanged() {
        if (isDefined(Field.ENTITY_TAGS_SYNC)) for (final SimpleEntity tag : getEntityTagsSync())
            System.out.println("Tag = " + tag);
        return actions.getDefault();
    }

    @NotNull @Override public Action integerChanged() {
        if (isDefined(Field.INTEGER)) System.out.println("getInteger() = " + getInteger());
        return actions.getDefault();
    }

    @Override public void load() {
        final ArrayList<Options> options = new ArrayList<>();
        options.add(Options.OPTION1);
        setOptionTags(options);

        final SimpleEntity lucas = SimpleEntity.find("lucas");
        if (lucas != null) {
            final ArrayList<SimpleEntity> entities = new ArrayList<>();
            entities.add(lucas);
            setEntityTags(entities);

            setEntity(lucas);
            setEntityWithSuggest(lucas);
        }

        final ArrayList<String> strings = new ArrayList<>();
        strings.add("red");
        setStringTags(strings);

        setInteger(9);
        setStrings("cyan");
        setStringsSync(Suggestion.create("6", "red"));

        getTable().add();
    }

    @NotNull @Override public Action resetStrings() {
        setStrings((String) null);
        return actions().getDefault();
    }

    @NotNull @Override public Action stringsChanged() {
        if (isDefined(Field.STRINGS_SYNC)) System.out.println("getStringsSync() = " + getStringsSync());
        return actions.getDefault();
    }

    @NotNull @Override public Action stringsTagsSyncChanged() {
        if (isDefined(Field.STRINGS_TAGS_SYNC)) for (final String tag : getStringsTagsSync())
            System.out.println("Tag = " + tag);
        return actions.getDefault();
    }

    @NotNull @Override public Iterable<Suggestion> suggestSync(@Nullable String query) {
        System.out.println("strings sync query was " + query);
        return getStringsList();
    }

    //~ Methods ......................................................................................................................................

    /** Strings suggester method. */
    @NotNull public static Iterable<Suggestion> onSuggestStrings(String query, SimpleEntity arg) {
        System.out.println("strings query was " + query);
        System.out.println("strings entity arg was " + arg);
        return getStringsList();
    }

    /**
     * Simple Entity suggester method. WARNING! This is not encourage, to do this you should simply
     * use Suggest Box bounded to an entity, that will use the index (Best performance!).
     */
    @NotNull public static Iterable<SimpleEntity> suggestEntity(String query) {
        System.out.println("simple entities query was " + query);
        return selectFrom(SIMPLE_ENTITY).list();
    }

    /** Ints suggester method. */
    @NotNull public static Iterable<Integer> suggestInts(String query) {
        System.out.println("ints query was " + query);

        final List<Integer> integers = new ArrayList<>();

        final Random r = new Random();
        for (int i = 0; i < 10; i++) {
            final int key = r.nextInt();
            integers.add(key);
        }

        return integers;
    }

    private static Iterable<Suggestion> getStringsList() {
        final List<Suggestion> strings = new ArrayList<>();

        strings.add(Suggestion.create("1", ImmutableList.of("cyan"), "cyan"));
        strings.add(Suggestion.create("2", ImmutableList.of("magenta"), "magenta"));
        strings.add(Suggestion.create("3", ImmutableList.of("blue"), "blue"));
        strings.add(Suggestion.create("4", ImmutableList.of(BLACK), BLACK));

        return strings;
    }

    //~ Static Fields ................................................................................................................................

    public static final String BLACK = "black";

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends TableRowBase {
        @NotNull @Override public Action changed() {
            final Action action = actions.getDefault();
            if (isDefined(Field.COLUMN_STRINGS)) action.withMessage("Seleccionó... " + getColumnStrings());
            return action;
        }

        @NotNull @Override public Iterable<SimpleEntity> entitySuggestSync(@Nullable String query) {
            System.out.println("entity sync in table query = " + query);
            return SuggestBoxShowcase.this.entitySuggestSync(query);  // Delegate on form method
        }

        @NotNull @Override public Action stringsChanged() {
            final Action action = actions.getDefault();
            if (isDefined(Field.COLUMN_STRINGS_SYNC)) action.withMessage(notNull(getColumnStringsSync()));
            return action;
        }

        @NotNull @Override public Iterable<Suggestion> tableSuggestSync(@Nullable String query) {
            System.out.println("strings sync in table query was " + query);
            return getStringsList();
        }
    }
}  // end class SuggestBoxShowcase
