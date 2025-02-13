
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Files;
import tekgenesis.common.util.Sha;
import tekgenesis.common.util.VersionString;
import tekgenesis.database.exception.DatabaseException;
import tekgenesis.database.exception.EvolutionException;
import tekgenesis.database.type.Lob;
import tekgenesis.properties.SchemaProps;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.util.Files.readLines;
import static tekgenesis.common.util.Files.toSystemIndependentName;
import static tekgenesis.common.util.Resources.readerFromUrl;
import static tekgenesis.database.SqlConstants.hasGeneratedMark;

class SchemaVersion {

    //~ Instance Fields ..............................................................................................................................

    private final Database db;
    private final boolean  delta;

    private boolean      mainLoaded;
    private Lob          mainLob;
    private String       mainSha;
    private final String overlay;

    private boolean                    overlayLoaded;
    private Lob                        overlayLob;
    private String                     overlaySha;
    @Nullable private final List<File> resourcePath;
    private final String               schema;

    private final String        sql;
    private final VersionString version;

    //~ Constructors .................................................................................................................................

    SchemaVersion(VersionString versionString, File dir, final Database db, final String schema, boolean delta,
                  @Nullable final List<File> resourcePath) {
        this.schema       = schema;
        version           = versionString;
        sql               = toSystemIndependentName(new File(dir, SchemaDefinition.schemaFileName(schema)).getPath());
        overlay           = toSystemIndependentName(new File(dir, SchemaDefinition.overlayFileName(schema)).getPath());
        this.delta        = delta;
        overlayLoaded     = false;
        mainLoaded        = false;
        overlaySha        = null;
        overlayLob        = null;
        mainSha           = null;
        mainLob           = null;
        this.db           = db;
        this.resourcePath = resourcePath;
    }

    //~ Methods ......................................................................................................................................

    @Override public String toString() {
        return sql;
    }

    /** Execute the sql scripts. */
    void executeSql(boolean ignoreErrors) {
        if (delta) Logger.getLogger(SchemaDefinition.class).info("Applying delta " + version + " for schema " + schema);
        final SchemaProps schemaProps = Context.getEnvironment().get(schema, SchemaProps.class);
        try {
            executeSql(mainLob(), schemaProps, ignoreErrors);
            executeSql(overlayLob(), schemaProps, ignoreErrors);
        }
        catch (final DatabaseException e) {
            throw new EvolutionException(e, schema, version);
        }
    }

    @NotNull Lob mainLob() {
        loadMain();
        assert mainLob != null;
        return mainLob;
    }

    @NotNull String mainSha() {
        loadMain();
        assert mainSha != null;
        return mainSha;
    }

    @Nullable Lob overlayLob() {
        loadOverlay();
        return overlayLob;
    }

    @Nullable String overlaySha() {
        loadOverlay();
        return overlaySha;
    }

    boolean shaMatches(SchemaVersion that) {
        return equal(mainSha(), that.mainSha()) && equal(overlaySha(), that.overlaySha());
    }

    void updateMetadataEntry(boolean insert) {
        mainLoaded = overlayLoaded = false;
        final MetaDataEntry e = new MetaDataEntry(version, mainSha(), overlaySha(), mainLob(), overlayLob());
        if (insert) e.insert(db, schema);
        else e.update(db, schema);
    }

    boolean isCurrent(@Nullable MetaDataEntry dbVersion) {
        return dbVersion != null && dbVersion.getSha().equals(mainSha()) && equal(overlaySha(), dbVersion.getOverlaySha());
    }

    /** Returns the schema version. */
    VersionString getVersion() {
        return version;
    }

    private void executeSql(@Nullable final Lob lob, final SchemaProps schemaProps, boolean ignoreErrors) {
        if (lob == null) return;
        SqlStatement sqlStatement = db.sqlStatement(lob.getReader()).ignoreErrors(ignoreErrors);
        if (delta) sqlStatement = sqlStatement.withInfo();
        sqlStatement.withSchemaProps(schemaProps).executeScript();
    }

    private void loadMain() {
        if (mainLoaded) return;
        final Tuple<String, Lob> t = ensureNotNull(readResource(sql), CANNOT_READ + sql);
        mainSha    = t.first();
        mainLob    = t.second();
        mainLoaded = true;
    }

    private void loadOverlay() {
        if (overlayLoaded) return;
        final Tuple<String, Lob> t = readResource(overlay);
        if (t != null) {
            overlaySha = t.first();
            overlayLob = t.second();
        }
        overlayLoaded = true;
    }

    @Nullable private Tuple<String, Lob> readResource(String resource) {
        final URL          url    = getResource(resource);
        Tuple<String, Lob> result = null;
        if (url != null) {
            for (final Reader r : readerFromUrl(url)) {
                final Sha    sha      = new Sha();
                final char[] outChars = sha.filter(dropGeneratedMark(r));
                final String digest   = sha.getDigestAsString();
                result = tuple(digest, Lob.createClob(outChars));
            }
        }
        return result;
    }

    @Nullable private URL getResource(final String resource) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
        if (url == null) url = SchemaVersion.class.getClassLoader().getResource(resource);  // Maybe is a Sui Generis schema
        return url != null || resourcePath == null ? url : new URLClassLoader(Files.toURL(resourcePath)).getResource(resource);
    }

    //~ Methods ......................................................................................................................................

    private static Reader dropGeneratedMark(final Reader reader) {
        ImmutableList<String> lines = readLines(reader);
        if (hasGeneratedMark(lines)) lines = lines.drop(2);
        return new StringReader(lines.mkString("\n"));
    }

    //~ Static Fields ................................................................................................................................

    private static final String CANNOT_READ = "Cannot read ";
}  // end class SchemaVersion
