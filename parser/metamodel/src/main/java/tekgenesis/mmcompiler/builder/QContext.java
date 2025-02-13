
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.builder;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.QName;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.QName.*;

/**
 * Qualification context for {@link MetaModel metamodels} containing package, schema, and imports.
 */
public class QContext {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Map<String, String> imports;

    @NotNull private final String packageId;
    @NotNull private final String schema;

    //~ Constructors .................................................................................................................................

    /** Construct QContext with package, schema, and imports. */
    public QContext(@NotNull String packageId, @NotNull String schema, @NotNull List<String> imports) {
        this.packageId = packageId;
        this.schema    = schema;
        this.imports   = calculate(imports);
    }

    //~ Methods ......................................................................................................................................

    /** Returns the name part of a full qualified name. */
    @NotNull public String extractName(@NotNull String name) {
        return QName.extractName(name);
    }

    /** Return true if given reference has name conflict. */
    public boolean hasImportNameConflict(String fqn) {
        final String name  = extractName(fqn);
        final String match = imports.get(name);
        return !(isEmpty(match) || QName.extractQualification(fqn).equals(match));
    }

    /**
     * Return true if given reference needs qualification or not, based on qualification context.
     */
    public boolean needsQualification(@NotNull String fqn) {
        final String name     = extractName(fqn);
        final String expected = notNull(imports.get(name), packageId);
        return !QName.extractQualification(fqn).equals(expected);
    }

    /** Resolve qualification for given reference. */
    @NotNull public QName resolve(@NotNull String name) {
        return QName.createQName(extractQualification(name), extractName(name));
    }

    /** Return context schema. */
    @NotNull public String getSchema() {
        return schema;
    }

    /** Returns the qualification part of a full qualified name. */
    @NotNull String extractQualification(String name) {
        if (isQualified(name)) return QName.extractQualification(name);
        return notNull(imports.get(name), packageId);
    }

    /** Optionally package qualify a name (if not qualified). */
    @NotNull QName withPackage(String name) {
        return createQName(qualify(packageId, name));
    }

    private Map<String, String> calculate(@NotNull List<String> statements) {
        final Map<String, String> result = new TreeMap<>();
        for (final String statement : statements)
            result.put(QName.extractName(statement), QName.extractQualification(statement));
        return result;
    }
}  // end class QContext
