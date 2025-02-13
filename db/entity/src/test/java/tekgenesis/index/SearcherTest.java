
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.test.basic.*;
import tekgenesis.test.basic.g.AuthorBase;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.collections.ImmutableList.fromIterable;
import static tekgenesis.index.SearchableExpr.EMPTY_EXPR;
import static tekgenesis.test.basic.g.AuthorSearcherBase.AUTHOR_SEARCHER;
import static tekgenesis.test.basic.g.DatabaseSearchableTypesSearcherBase.DATABASE_SEARCHABLE_TYPES_SEARCHER;
import static tekgenesis.test.basic.g.SearchableTypesSearcherBase.SEARCHABLE_TYPES_SEARCHER;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * IndexConsumerTest.
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc", "MagicNumber" })
public class SearcherTest extends IndexSuite {

    //~ Methods ......................................................................................................................................

    @Test public void testBasicSearch()
        throws InterruptedException
    {
        runInTransaction(() -> {
            createAuthor("pedro", "pelotas");
            createAuthor("pedro", "basaldua");
            createAuthor("pedro", "perkins");
            createAuthor("pedro", "fraile");
            createAuthor("jose", "basaldua");
            createAuthor("juan", "basaldua");
            createAuthor("martin", "gutierrez");
            createAuthor("juan", "martin");
            createAuthor("martin", "martin");

            createAuthor("joaquin", "buccaa");
            createAuthor("joaquin", "buccao");

            createAuthor("lucas", "luppani");
        });

        waitForProcessing(11);

        final List<SearchResult> basicSearch = AUTHOR_SEARCHER.search("pedro fraile");
        assertThat(basicSearch).extracting("toString").containsExactly("pedro fraile");
        assertThat(basicSearch).extracting("score").containsExactly(0.38490018);

        final List<SearchResult> basicSearch2 = AUTHOR_SEARCHER.search("joaquin bucca");
        assertThat(basicSearch2).extracting("toString").containsOnly("joaquin buccaa", "joaquin buccao");
        assertThat(basicSearch2).extracting("score").containsExactly(0.096225046, 0.096225046);

        final List<SearchResult> basicSearch3 = AUTHOR_SEARCHER.search("pedro");
        assertThat(basicSearch3).extracting("toString").containsOnly("pedro basaldua", "pedro fraile", "pedro pelotas", "pedro perkins");
        assertThat(basicSearch3).extracting("score").containsExactly(0.27216554, 0.27216554, 0.27216554, 0.27216554);

        final List<SearchResult> basicSearch4 = AUTHOR_SEARCHER.search("martin");
        assertThat(basicSearch4).extracting("toString").containsOnly("martin martin", "juan martin", "martin gutierrez");
        assertThat(basicSearch4).extracting("score").containsExactly(1.0886621, 0.27216554, 0.27216554);

        final List<SearchResult> basicSearch5 = AUTHOR_SEARCHER.search("fraile pedro");
        assertThat(basicSearch5).extracting("toString").containsExactly("pedro fraile");
        assertThat(basicSearch5).extracting("score").containsExactly(0.38490018);

        final List<SearchResult> basicSearch6 = AUTHOR_SEARCHER.search("joaquin bucca true");
        assertThat(basicSearch6).extracting("toString").containsExactly("joaquin buccaa");
        assertThat(map(basicSearch6, r -> r.getField(AUTHOR_SEARCHER.FORCE))).containsExactly(true);
        assertThat(basicSearch6).extracting("score").containsExactly(0.10248645);
    }  // end method testBasicSearch

    @Test public void testDatabaseSearchableTypes()
        throws InterruptedException
    {
        final DatabaseSearchableTypes one = invokeInTransaction(() -> {
                final DateTime    currentDateTime = DateTime.current();
                final DateOnly    dateOnly        = DateOnly.date(1992, 7, 31);
                final SuperSimple superSimple     = SuperSimple.create().setName("nameOne").insert();
                return DatabaseSearchableTypes.create()
                                              .setBool(true)
                                              .setDate(dateOnly)
                                              .setDateTime(currentDateTime)
                                              .setDecimal(BigDecimal.valueOf(98988989))
                                              .setEn(DocumentType.DNI)
                                              .setEns(EnumSet.allOf(DocumentType.class))
                                              .setEnti(superSimple)
                                              .setInteger(312345)
                                              .setReal(482221334)
                                              .setStr("guti")
                                              .setOpt(null)
                                              .setNullEnum(null)
                                              .insert();
            });

        runInTransaction(() -> {
            final DatabaseSearchableTypes comp = DatabaseSearchableTypes.find(one.keyAsString());
            assertThat(comp).isNotNull();

            final List<SearchResult> strSuffixSearch = DATABASE_SEARCHABLE_TYPES_SEARCHER.search("uti");
            assertThat(strSuffixSearch).hasSize(1);

            final List<SearchResult> strPrefixSearch = DATABASE_SEARCHABLE_TYPES_SEARCHER.search("gut");
            assertThat(strPrefixSearch).hasSize(1);

            final List<SearchResult> strFailSearch = DATABASE_SEARCHABLE_TYPES_SEARCHER.search("gutu");
            assertThat(strFailSearch).hasSize(0);

            final List<SearchResult> strFullSearch = DATABASE_SEARCHABLE_TYPES_SEARCHER.search("guti");
            assertThat(strFullSearch).hasSize(1);

            final List<SearchResult> numberFailedSearch = DATABASE_SEARCHABLE_TYPES_SEARCHER.search("9999999999");
            assertThat(numberFailedSearch).hasSize(0);

            final List<SearchResult> integerSearch = DATABASE_SEARCHABLE_TYPES_SEARCHER.search("1234");
            assertThat(integerSearch).hasSize(1);

            final List<SearchResult> integerFullSearch = DATABASE_SEARCHABLE_TYPES_SEARCHER.search("312345");
            assertThat(integerFullSearch).hasSize(1);

            final List<SearchResult> realSearch = DATABASE_SEARCHABLE_TYPES_SEARCHER.search("8222");
            assertThat(realSearch).hasSize(1);

            final List<SearchResult> decimalSearch = DATABASE_SEARCHABLE_TYPES_SEARCHER.search("8898");
            assertThat(decimalSearch).hasSize(1);

            final List<SearchResult> decimalFullSearch = DATABASE_SEARCHABLE_TYPES_SEARCHER.search("98988989");
            assertThat(decimalFullSearch).hasSize(1);

            final List<SearchResult> enumSearch = DATABASE_SEARCHABLE_TYPES_SEARCHER.search("NI");
            assertThat(enumSearch).hasSize(1);

            final List<SearchResult> enumFullSearch = DATABASE_SEARCHABLE_TYPES_SEARCHER.search("DNI");
            assertThat(enumFullSearch).hasSize(1);

            final List<SearchResult> emptySearch = DATABASE_SEARCHABLE_TYPES_SEARCHER.search("");
            assertThat(emptySearch).hasSize(1);
        });
    }  // end method testDatabaseSearchableTypes

    @Test public void testExact()
        throws InterruptedException
    {
        runInTransaction(() -> {
            createAuthor("Juliana", "Cortazar");
            createAuthor("Julia", "Verne");
            createAuthor("Juana", "Crichton");
            createAuthor("Juan", "Grisham");
            createAuthor("Pedro", "Contala");
            createAuthor("Pedro", "Conta");
        });

        waitForProcessing(6);

        final List<SearchResult> julia = AUTHOR_SEARCHER.search("Julia");
        assertThat(julia).extracting("toString").containsExactly("Julia Verne", "Juliana Cortazar");

        final List<SearchResult> juan = AUTHOR_SEARCHER.search("Juan");
        assertThat(juan).extracting("toString").containsExactly("Juan Grisham", "Juana Crichton");

        final List<SearchResult> juana = AUTHOR_SEARCHER.search("Juana");
        assertThat(juana).extracting("toString").containsExactly("Juana Crichton");

        final List<SearchResult> cortazar = AUTHOR_SEARCHER.search("Julia Cortazar");
        assertThat(cortazar).extracting("toString").containsExactly("Juliana Cortazar");

        final List<SearchResult> pedro = AUTHOR_SEARCHER.search("Pedro Conta");
        assertThat(pedro).extracting("toString").containsExactly("Pedro Conta", "Pedro Contala");
    }

    @Test public void testFilter()
        throws InterruptedException
    {
        runInTransaction(() -> {
            createAuthor("Diego Martin", "Rubinstein");
            createAuthor("Diego Martin", "Sabaris");
            createAuthor("Pedro", "Colunga");
        });

        waitForProcessing(3);

        final List<SearchResult> diegos = AUTHOR_SEARCHER.search("", AUTHOR_SEARCHER.NAME.eq("Diego Martin"));
        assertThat(diegos).hasSize(2);
        assertThat(diegos).extracting("toString").containsExactlyInAnyOrder("Diego Martin Rubinstein", "Diego Martin Sabaris");

        final List<SearchResult> diegos2 = AUTHOR_SEARCHER.search("", AUTHOR_SEARCHER.NAME.eq("Diego"));
        assertThat(diegos2).hasSize(2);
        assertThat(diegos2).extracting("toString").containsExactlyInAnyOrder("Diego Martin Rubinstein", "Diego Martin Sabaris");

        final List<SearchResult> pedro = AUTHOR_SEARCHER.search("", AUTHOR_SEARCHER.NAME.eq("Pedro"));
        assertThat(pedro).extracting("toString").containsExactly("Pedro Colunga");
    }

    @Test public void testIndexAllow()
        throws InterruptedException
    {
        runInTransaction(() -> {
            createAuthor("martin", "gutierrez");

            createAuthor("lucas", "luppani");
        });

        waitForProcessing(1);

        // +first:martin +last:gutierrez -> should index that entity.
        final List<SearchResult> allowed = AUTHOR_SEARCHER.search(AUTHOR_SEARCHER.NAME.eq("martin").and(AUTHOR_SEARCHER.LAST_NAME.eq("gutierrez")));
        assertThat(allowed).extracting("toString").containsExactly("martin gutierrez");

        // +first:lucas +last:luppani -> should not index that entity though.
        final List<SearchResult> notAllowed = AUTHOR_SEARCHER.search(AUTHOR_SEARCHER.NAME.eq("lucas").and(AUTHOR_SEARCHER.LAST_NAME.eq("luppani")));
        assertThat(notAllowed).isEmpty();
    }

    @SuppressWarnings("OverlyLongMethod")
    @Test public void testIndexDsl()
        throws InterruptedException
    {
        runInTransaction(() -> {
            createAuthor("pedro", "pelotas");
            createAuthor("pedro", "basaldua");
            createAuthor("pedro", "perkins");
            createAuthor("pedro", "fraile");
            createAuthor("jose", "basaldua");
            createAuthor("juan", "basaldua");
            createAuthor("martin", "gutierrez");
            createAuthor("juan", "martin");
            createAuthor("martin", "martin");

            createAuthor("joaquin", "buccaa");
            createAuthor("joaquin", "buccao");

            createAuthor("lucas", "luppani");
        });

        waitForProcessing(11);

        final AuthorSearcher authorSearcher = new AuthorSearcher();

        // +first:pedro -last:perkins
        final List<SearchResult> firstNoLast = authorSearcher.search(AUTHOR_SEARCHER.NAME.eq("pedro").and(AUTHOR_SEARCHER.LAST_NAME.ne("perkins")));
        assertThat(firstNoLast).extracting("toString").containsOnly("pedro basaldua", "pedro fraile", "pedro pelotas");
        assertThat(firstNoLast).extracting("score").containsExactly(1.7884574, 1.7884574, 1.7884574);

        // first:martin last:martin
        final List<SearchResult> firstNameFirst = authorSearcher.search(
                AUTHOR_SEARCHER.NAME.eq("martin").also(AUTHOR_SEARCHER.LAST_NAME.eq("martin")));
        assertThat(firstNameFirst).extracting("toString").containsOnly("martin martin", "juan martin", "martin gutierrez");
        assertThat(firstNameFirst).extracting("score").containsExactly(3.2516773, 0.8129193, 0.8129193);

        // first:martin last:martin^2
        final List<SearchResult> lastNameFirst = authorSearcher.search(
                AUTHOR_SEARCHER.NAME.eq("martin").also(AUTHOR_SEARCHER.LAST_NAME.eq("martin").boost()));
        assertThat(lastNameFirst).extracting("toString").containsExactly("martin martin", "juan martin", "martin gutierrez");
        assertThat(lastNameFirst).extracting("score").containsExactly(3.0848117, 1.0282706, 0.5141353);

        // +(first:pedro last:pedro) (+first:pedro +last:pedro)
        final List<SearchResult> separateContainers = authorSearcher.search(
                AUTHOR_SEARCHER.NAME.eq("pedro")
                                    .also(AUTHOR_SEARCHER.LAST_NAME.eq("pedro"))
                                    .must()
                                    .also(AUTHOR_SEARCHER.NAME.eq("pedro").and(AUTHOR_SEARCHER.LAST_NAME.eq("fraile"))));
        assertThat(separateContainers).extracting("toString").containsOnly("pedro fraile", "pedro basaldua", "pedro pelotas", "pedro perkins");
        assertThat(separateContainers).extracting("score").containsExactly(2.410277, 0.15910847, 0.15910847, 0.15910847);

        // +((first:pedro last:fraile) (+first:martin +last:gutierrez)^2 (+first:joaquin +last:bucca*))
        final List<SearchResult> moreThanTwoContainers = authorSearcher.search(
                SearchableExpr.also(AUTHOR_SEARCHER.NAME.eq("pedro").and(AUTHOR_SEARCHER.LAST_NAME.eq("fraile")),
                                  AUTHOR_SEARCHER.NAME.eq("martin").and(AUTHOR_SEARCHER.LAST_NAME.eq("gutierrez")).boost(),
                                  AUTHOR_SEARCHER.NAME.eq("joaquin").and(AUTHOR_SEARCHER.LAST_NAME.eq("bucca*")))
                              .must());
        assertThat(moreThanTwoContainers).extracting("toString").containsOnly("martin gutierrez", "joaquin buccaa", "pedro fraile", "joaquin buccao");
        assertThat(moreThanTwoContainers).extracting("score").containsExactly(1.0248082, 0.42750236, 0.25561327, 0.25561327);

        // +first:joaquin +last:bucca* force:true
        final List<SearchResult> withTrue = authorSearcher.search(
                AUTHOR_SEARCHER.NAME.eq("joaquin").and(AUTHOR_SEARCHER.LAST_NAME.eq("bucca*")).also(AUTHOR_SEARCHER.FORCE.eq(true)));
        assertThat(withTrue).extracting("toString").containsExactly("joaquin buccaa", "joaquin buccao");
        assertThat(withTrue).extracting("score").containsExactly(3.6881385, 1.1363822);
        assertThat(map(withTrue, r -> r.getField(AUTHOR_SEARCHER.FORCE))).containsExactly(true, false);

        // +first:joaquin +last:bucca* force:false
        final List<SearchResult> withFalse = authorSearcher.search(
                AUTHOR_SEARCHER.NAME.eq("joaquin").and(AUTHOR_SEARCHER.LAST_NAME.eq("bucca*")).also(AUTHOR_SEARCHER.FORCE.eq(false)));
        assertThat(withFalse).extracting("toString").containsExactly("joaquin buccao", "joaquin buccaa");
        assertThat(withFalse).extracting("score").containsExactly(2.6993892, 1.5526235);
        assertThat(map(withFalse, r -> r.getField(AUTHOR_SEARCHER.FORCE))).containsExactly(false, true);
    }  // end method testIndexDsl

    @Test public void testSearchableTypes()
        throws InterruptedException
    {
        final SearchableTypes one = invokeInTransaction(() -> {
                final DateTime    currentDateTime = DateTime.current();
                final DateOnly    dateOnly        = DateOnly.date(1992, 7, 31);
                final SuperSimple superSimple     = SuperSimple.create().setName("nameOne").insert();
                return SearchableTypes.create()
                                      .setBool(true)
                                      .setDate(dateOnly)
                                      .setDateTime(currentDateTime)
                                      .setDecimal(BigDecimal.ONE)
                                      .setEn(DocumentType.DNI)
                                      .setEns(EnumSet.allOf(DocumentType.class))
                                      .setEnti(superSimple)
                                      .setInteger(31)
                                      .setReal(48)
                                      .setStr("guti")
                                      .setOpt(null)
                                      .setNullEnum(null)
                                      .insert();
            });

        waitForProcessing(1);

        runInTransaction(() -> {
            final SearchableTypes comp = SearchableTypes.find(one.keyAsString());
            assertThat(comp).isNotNull();

            final List<SearchResult> suggestions = SEARCHABLE_TYPES_SEARCHER.getSuggestions();
            assertThat(suggestions).hasSize(1);

            final SearchResult searchResult = suggestions.get(0);
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.STR)).isEqualTo(comp.getStr());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.STRS)).isEqualTo(comp.getStrs().toList());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.BOOL)).isEqualTo(comp.isBool());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.BOOLS)).isEqualTo(comp.getBools().toList());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.DATE)).isEqualTo(comp.getDate());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.DATES)).isEqualTo(comp.getDates().toList());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.DATE_TIME)).isEqualTo(comp.getDateTime());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.DATE_TIMES)).isEqualTo(comp.getDateTimes().toList());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.DECIMAL)).isEqualTo(comp.getDecimal());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.DECIMALS)).isEqualTo(comp.getDecimals().toList());

            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.MY_ENTITY)).isEqualTo(comp.getEnti());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.MY_ENTITY.primitive())).isEqualTo(comp.getEnti().keyAsString());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.INTEGER)).isEqualTo(comp.getInteger());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.INTEGERS)).isEqualTo(comp.getIntegers().toList());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.REAL)).isEqualTo(comp.getReal());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.REALS)).isEqualTo(comp.getReals().toList());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.OPT)).isEqualTo("");

            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.MY_ENUM)).isEqualTo(comp.getEn());
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.MY_ENUMS)).isEqualTo(fromIterable(comp.getEns()));
            assertThat(searchResult.getField(SEARCHABLE_TYPES_SEARCHER.NULL_ENUM)).isEqualTo(null);
        });
    }  // end method testSearchableTypes

    @Test public void testSpaces()
        throws InterruptedException
    {
        runInTransaction(() -> {
            createAuthor("   Diego   ", " Rubinstein ");
            createAuthor("Martin", "Gutierrez");
        });

        waitForProcessing(2);

        final List<SearchResult> diego = AUTHOR_SEARCHER.search(
                AUTHOR_SEARCHER.NAME.eq("Diego ").and(AUTHOR_SEARCHER.LAST_NAME.eq("   Rubinstein     ")));
        assertThat(diego).extracting("toString").containsExactlyInAnyOrder("   Diego     Rubinstein ");

        final List<SearchResult> martin = AUTHOR_SEARCHER.search(
                AUTHOR_SEARCHER.NAME.eq("    martin ").and(AUTHOR_SEARCHER.LAST_NAME.eq("     gutierrez     ")));
        assertThat(martin).extracting("toString").containsExactlyInAnyOrder("Martin Gutierrez");
    }

    @Test public void testSuggestions()
        throws InterruptedException
    {
        runInTransaction(() -> {
            createAuthor("Julio", "Verne");
            createAuthor("Julio", "Cortazar");
            createAuthor("John", "Grisham");
            createAuthor("Michael", "Crichton");
            createAuthor("Gabriel", "Garcia Marquez");
            createAuthor("Jorge Luis", "Borges");
            createAuthor("Juan", "Perez");
            createAuthor("Andres", "Borges");
            createAuthor("Gabriel", "Mercado");
            createAuthor("Ramiro", "Funes Mori");
            createAuthor("Rodrigo", "Mora");
            createAuthor("Teo", "Gutierrez");
        });

        waitForProcessing(12);

        final List<SearchResult> suggestions = AUTHOR_SEARCHER.search(EMPTY_EXPR, 11);
        assertThat(suggestions.size()).isEqualTo(11);
    }

    private Author createAuthor(String name, String lastName) {
        final Author author = AuthorBase.create();
        author.setName(name);
        author.setLastName(lastName);

        author.insert();
        return author;
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class SearcherTest
