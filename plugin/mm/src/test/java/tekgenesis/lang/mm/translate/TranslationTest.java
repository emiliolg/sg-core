
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.translate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import tekgenesis.common.io.PropertyWriter;
import tekgenesis.common.util.Diff;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

/**
 * TranslationTest.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class TranslationTest {

    //~ Instance Fields ..............................................................................................................................

    private final File propertiesGolden = new File("plugin/mm/src/test/resources/test.golden.properties");
    private final File propertiesInput  = new File("plugin/mm/src/test/resources/test.input.properties");
    private final File propertiesOutput = new File("target/plugin/mm/test-output/test.properties");

    //~ Methods ......................................................................................................................................

    @Test public void TranslateProperties()
        throws FileNotFoundException
    {
        final TranslateProperties.PropertiesDelta delta   = TranslateProperties.load(propertiesInput).delta(localizationMap);
        final StringBuilder                       builder = new StringBuilder();
        delta.message(builder);

        assertThat(builder.toString()).isEqualTo("[ unchanged: 3, added: 1, changed: 2, deleted: 2 ]");

        final PropertyWriter writer = new PropertyWriter(propertiesOutput);

        // Translate properties, just
        final TranslateProperties props = new TranslateProperties();

        final Map<String, Property> propsMap = delta.getProperties().getProperties();

        for (final Property p : propsMap.values()) {
            final String label = p.getLabel();
            props.add(p.getKey(), p.isChanged() ? translationMap.get(label) : p.getValue(), p.isGenerated(), label, p.isAdded(), p.isChanged());
        }

        // Write new properties
        props.write(writer);

        writer.close();

        // Test writer output equals golden
        final List<Diff.Delta<String>> diffs = Diff.caseSensitive().diff(new FileReader(propertiesOutput), new FileReader(propertiesGolden));

        if (!diffs.isEmpty()) fail("diff -y -W 150 " + propertiesOutput + " " + propertiesGolden + "\n" + Diff.makeString(diffs));
    }

    //~ Methods ......................................................................................................................................

    @BeforeClass public static void setUp() {
        localizationMap.put("john", "John Snow");
        localizationMap.put("winterfell", "Winterfell");
        localizationMap.put("myaStone", "Mya Stone");
        localizationMap.put("kingsLanding", "King's Landing");
        localizationMap.put("ellariaSand", "Ellaria Sand");
        localizationMap.put("auraneWaters", "Aurane Waters");

        translationMap.put("John Snow", "John Nieve");
        translationMap.put("Winterfell", "Invernalia");
        translationMap.put("Mya Stone", "Mya Piedra");
        translationMap.put("King's Landing", "Desembarco Del Rey");
        translationMap.put("Ellaria Sand", "Ellaria Arena");
        translationMap.put("Aurane Waters", "Aurane Aguas");
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<String, String> localizationMap = new LinkedHashMap<>();
    private static final Map<String, String> translationMap  = new LinkedHashMap<>();
}  // end class TranslationTest
