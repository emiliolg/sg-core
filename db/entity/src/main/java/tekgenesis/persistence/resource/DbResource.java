
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.resource;

import java.awt.*;
import java.io.*;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Metadata;
import tekgenesis.common.core.Resource;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.media.Mimes;
import tekgenesis.common.util.Files;
import tekgenesis.common.util.Sha;
import tekgenesis.database.Database;
import tekgenesis.database.SqlStatement;
import tekgenesis.database.type.Lob;
import tekgenesis.persistence.exception.ResourceException;
import tekgenesis.type.resource.AbstractResource;
import tekgenesis.type.resource.SimpleResourceImpl;

import static java.lang.Math.min;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Constants.UTF8;
import static tekgenesis.common.media.Mimes.isImage;
import static tekgenesis.common.media.Mimes.isText;
import static tekgenesis.database.type.Lob.createClob;
import static tekgenesis.persistence.resource.ResourcesConstants.*;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * An implementation of a {@link Resource} stored in the database.
 */
public class DbResource extends AbstractResource {

    //~ Instance Fields ..............................................................................................................................

    private final Database     db;
    private Map<String, Entry> entries;

    //~ Constructors .................................................................................................................................

    /** Constructor with Database and uuid. */
    public DbResource(Database db, String uuid) {
        super(uuid);
        this.db = db;
        entries = null;
    }

    //~ Methods ......................................................................................................................................

    @Override public Factory addLarge() {
        return addVariant(LARGE);
    }

    @Override public Factory addThumb() {
        return addVariant(THUMB);
    }

    @Override public Factory addVariant(String variant) {
        return new DbFactory(this, variant);
    }

    @Override public SimpleResourceImpl asSimple() {
        return new SimpleResourceImpl(getUuid(), entries());
    }

    @NotNull @Override protected Map<String, Entry> entries() {
        if (entries != null) return entries;
        return loadEntries();
    }

    private void addEntry(@NotNull String variant, boolean external, @NotNull String name, @NotNull String url, @NotNull String mimeType,
                          @NotNull Metadata metadata) {
        final Entry e = new DbEntry(variant, external, name, url, mimeType, metadata);
        entries().put(e.getVariant(), e);
    }

    private HashMap<String, Entry> buildEntryMap(final Seq<EntryInfo> es) {
        final HashMap<String, Entry> entriesMap = new HashMap<>();
        for (final EntryInfo e : es)
            entriesMap.put(e.variant, new DbEntry(e.variant, e.external, e.name, e.url, e.mimeType, e.metadata));
        return entriesMap;
    }

    private SqlStatement insertInto(String table, String... columns) {
        final StrBuilder cols   = new StrBuilder();
        final StrBuilder values = new StrBuilder();
        for (final String c : columns) {
            cols.appendElement(c);
            values.appendElement("?");
        }
        return db.sqlStatement("insert into Schema(SG).%s (%s) values (%s)", table, cols.toString(), values.toString());
    }
    @NotNull private synchronized Map<String, Entry> loadEntries() {
        if (entries == null) entries = buildEntryMap(loadEntries(db, listOf(getUuid())));
        return entries;
    }

    private Resource upload(@NotNull String name, @NotNull String mimeType, @NotNull ContentData content, @NotNull String variant) {
        if (!exists(db, content.sha))
            insertInto(CONTENT_TABLE, SHA, SIZE, MIME_TYPE, content.lob.isClob() ? TEXT_DATA : BINARY_DATA)  //
            .onArgs(content.sha, content.getSize(), mimeType, content.lob).executeDml();

        final Lob lob = createClob(clobToString(content.getMetadata()));
        insertInto(INDEX_TABLE, UUID, VARIANT, EXTERNAL, NAME, URL, INFO).onArgs(getUuid(), variant, false, name, content.sha, lob).executeDml();
        addEntry(variant, false, name, content.sha, mimeType, content.getMetadata());
        return this;
    }

    private Resource upload(@NotNull String name, @NotNull String url, @NotNull String variant, boolean external, @NotNull String mimeType) {
        insertInto(INDEX_TABLE, UUID, VARIANT, EXTERNAL, NAME, URL).onArgs(getUuid(), variant, external, name, url).executeDml();
        addEntry(variant, external, name, url, mimeType, Metadata.empty());
        return this;
    }

    private Object writeReplace() {
        return new SimpleResourceImpl(getUuid(), entries());
    }

    //~ Methods ......................................................................................................................................

    /** CLOB content to String. */
    public static <T> String clobToString(@NotNull T content) {
        final ObjectMapper shared = JsonMapping.shared();
        String             clob   = "";
        try {
            final StringWriter writer = new StringWriter();
            shared.writeValue(writer, content);
            clob = writer.toString();
        }
        catch (final IOException e) {
            logger.error(e);
        }
        return clob;
    }

    /** load all resource entries for a list of resources uuid. */
    @NotNull public static ImmutableList<EntryInfo> loadEntries(Database db, Seq<String> uuids) {
        return
            invokeInTransaction(() -> {
                final ArrayList<EntryInfo> result = new ArrayList<>();
                for (int i = 0; i < uuids.size();) {
                    final int           to           = min(i + 1000, uuids.size());
                    final StringBuilder uuidsStrings = new StringBuilder();

                    for (final String s : uuids.slice(i, to))
                        uuidsStrings.append("'").append(s).append("'").append(",");

                    result.addAll(
                        db.sqlStatement(
                            "select R.UUID, R.VARIANT, R.EXTERNAL, R.URL, R.NAME, R.INFO, C.MIME_TYPE from QName(SG, RESOURCE_INDEX) R left outer join QName(SG, RESOURCE_CONTENT) C on R.URL = C.SHA where R.UUID in (%s)",
                            uuidsStrings.deleteCharAt(uuidsStrings.length() - 1).toString()).list(rs -> {
                            @Nullable final InputStream stream;

                            if (db.getDatabaseType().supportsLobs()) {
                                final Clob clob = rs.getClob(6);
                                stream = clob != null ? clob.getAsciiStream() : null;
                            }
                            else stream = rs.getBinaryStream(6);

                            try {
                                return new EntryInfo(rs.getString(1),
                                    rs.getString(2),
                                    rs.getBoolean(3),
                                    rs.getString(4),
                                    notNull(rs.getString(5), "NoName"),
                                    stream,
                                    notNull(rs.getString(7), ""));
                            }
                            catch (final IOException e) {
                                logger.error(e);
                                return null;
                            }
                        }));
                    // noinspection AssignmentToForLoopParameter
                    i += 1000;
                }
                return Colls.immutable(result);
            });
    }  // end method loadEntries

    static int downloadTo(Database db, String sha, OutputStream os) {
        return invokeInTransaction(() -> {
            final SqlStatement select = select(db, BINARY_DATA, sha);
            final InputStream  is;

            if (!db.getDatabaseType().supportsLobs()) is = select.get(InputStream.class);
            else {
                try {
                    final Blob blob = select.get(Blob.class);
                    assert blob != null;
                    is = blob.getBinaryStream();
                }
                catch (final SQLException e) {
                    throw ResourceException.errorLoading(sha, e);
                }
            }

            if (is == null) throw ResourceException.notFound(sha);

            return Files.copy(is, os, false);
        });
    }  // end method downloadTo

    static int downloadTo(Database db, String sha, Writer writer) {
        return invokeInTransaction(() -> {
            final SqlStatement select = select(db, TEXT_DATA, sha);
            final Reader       r;

            if (!db.getDatabaseType().supportsLobs()) r = select.get(Reader.class);
            else {
                try {
                    final Clob clob = select.get(Clob.class);
                    assert clob != null;
                    r = clob.getCharacterStream();
                }
                catch (final SQLException e) {
                    throw ResourceException.errorLoading(sha, e);
                }
            }

            if (r == null) throw ResourceException.notFound(sha);
            return Files.copy(r, writer);
        });
    }  // end method downloadTo

    static int downloadTo(Database db, String sha, String mimeType, OutputStream os) {
        if (isText(mimeType)) {
            try(final Writer writer = new BufferedWriter(new OutputStreamWriter(os, UTF8))) {
                return downloadTo(db, sha, writer);
            }
            catch (final IOException e) {
                throw ResourceException.errorLoading(sha, e);
            }
        }
        else return downloadTo(db, sha, os);
    }

    @Nullable static String mimeType(Database db, String sha) {
        return invokeInTransaction(() -> select(db, MIME_TYPE, sha).get(String.class));
    }

    private static boolean exists(Database db, String sha) {
        return select(db, SHA, sha).get(String.class) != null;
    }

    @NotNull private static SqlStatement select(Database db, String data, String sha) {
        return db.sqlStatement("select %s from QName(SG, RESOURCE_CONTENT) where SHA = ?", data).onArgs(sha);
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String UNNAMED = "unnamed";

    private static final Logger logger = Logger.getLogger(DbResource.class);

    private static final long serialVersionUID = -8067556336370518474L;

    //~ Inner Classes ................................................................................................................................

    static class ContentData {
        private final Lob      lob;
        private final Metadata metadata;
        private final String   sha;

        ContentData(String sha, char[] chars) {
            this(sha, createClob(chars), Metadata.empty());
        }

        ContentData(String sha, Lob lob, Metadata metadata) {
            this.sha      = sha;
            this.lob      = lob;
            this.metadata = metadata;
        }

        ContentData(String sha, byte[] bytes, Metadata metadata) {
            this(sha, Lob.createBlob(new ByteArrayInputStream(bytes), bytes.length), metadata);
        }

        public Metadata getMetadata() {
            return metadata;
        }

        public int getSize() {
            return lob.getSize();
        }

        static ContentData generateSha(Reader reader) {
            final Sha    sha      = new Sha();
            final char[] outChars = sha.filter(reader);
            return new ContentData(sha.getDigestAsString(), outChars);
        }

        static ContentData generateSha(InputStream inputStream, String mimeType) {
            final Sha    sha      = new Sha();
            final byte[] outBytes = sha.filter(inputStream);
            return new ContentData(sha.getDigestAsString(), outBytes, createMetadata(mimeType, outBytes));
        }

        @Nullable static Dimension getDimension(String mimeType, byte[] bytes) {
            if (!isImage(mimeType)) return null;
            try(final ImageInputStream is = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
                final Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(is);

                if (imageReaders.hasNext()) {
                    final ImageReader reader = imageReaders.next();
                    reader.setInput(is);
                    return new Dimension(reader.getWidth(0), reader.getHeight(0));
                }
            }
            catch (final IOException e) {
                logger.error(e);
            }

            return null;
        }

        private static Metadata createMetadata(String mimeType, byte[] outBytes) {
            final Dimension dimension = getDimension(mimeType, outBytes);
            double          width     = -1;
            double          height    = -1;
            if (dimension != null) {
                width  = dimension.getWidth();
                height = dimension.getHeight();
            }
            return Metadata.create().withDimension(width, height).withSize(outBytes.length);
        }
    }  // end class ContentData

    class DbEntry extends EntryImpl {
        DbEntry(@NotNull String variant, boolean external, @NotNull String name, @NotNull String url, @NotNull String mimeType,
                @NotNull Metadata metadata) {
            super(variant, external, name, url, mimeType, metadata);
        }

        @Override public int copyTo(Writer writer) {
            return downloadTo(db, getSha(), writer);
        }

        @Override public int copyTo(OutputStream os) {
            return downloadTo(db, getSha(), getMimeType(), os);
        }

        // @Override public String getMimeType() { return mimeType(db, getSha()); }

        private static final long serialVersionUID = 3682132665148280917L;
    }

    static class DbFactory implements Factory {
        private final DbResource resource;
        private final String     variant;

        public DbFactory(DbResource resource, String variant) {
            this.resource = resource;
            this.variant  = variant;
        }

        @Override public Resource upload(@NotNull File file) {
            final String name     = file.getName();
            final String mimeType = Mimes.getMimeType(name);
            try {
                return isText(mimeType) ? upload(name, mimeType, new FileReader(file)) : upload(name, mimeType, new FileInputStream(file));
            }
            catch (final FileNotFoundException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override public Resource upload(@NotNull String name, @NotNull String url) {
            return invokeInTransaction(() -> resource.upload(name, url, variant, true, ""));
        }

        @Override public Resource upload(@NotNull String name, @NotNull String mimeType, @NotNull Reader reader) {
            if (!isText(mimeType)) throw new IllegalArgumentException("Invalid Text Type: " + mimeType);
            return invokeInTransaction(() -> resource.upload(name, mimeType, ContentData.generateSha(reader), variant));
        }

        @Override public Resource upload(@NotNull String name, @NotNull String mimeType, @NotNull InputStream inputStream) {
            if (isText(mimeType)) throw new IllegalArgumentException("Invalid Binary Type: " + mimeType);
            return invokeInTransaction(() -> resource.upload(name, mimeType, ContentData.generateSha(inputStream, mimeType), variant));
        }

        @Override public Resource uploadFromSha(@NotNull String name, @NotNull String sha, @NotNull String mimeType) {
            return resource.upload(name, sha, variant, false, mimeType);
        }
    }

    public static class EntryInfo {
        private final boolean           external;
        @NotNull private final Metadata metadata;
        @NotNull private final String   mimeType;
        @NotNull private final String   name;
        @NotNull private final String   url;
        @NotNull private final String   uuid;
        @NotNull private final String   variant;

        @SuppressWarnings("ConstructorWithTooManyParameters")
        EntryInfo(@NotNull String uuid, @NotNull String variant, boolean external, @NotNull String url, @NotNull String name,
                  @Nullable InputStream metadata, @NotNull String mimeType)
            throws IOException
        {
            this.uuid     = uuid;
            this.variant  = variant;
            this.external = external;
            this.name     = name;
            this.url      = url;
            this.mimeType = mimeType;
            final ObjectMapper shared = JsonMapping.shared();
            this.metadata = metadata != null ? shared.readValue(metadata, Metadata.class) : Metadata.empty();
        }

        /** Return if the resource entry is external. */
        public boolean isExternal() {
            return external;
        }

        /** Return resource entry metadata. */
        @NotNull public Metadata getMetadata() {
            return metadata;
        }
        /** Return resource entry mime type. */
        @NotNull public String getMimeType() {
            return mimeType;
        }

        /** Return resource entry name. */
        @NotNull public String getName() {
            return name;
        }

        /** Return resource url. */
        @NotNull public String getUrl() {
            return url;
        }

        /** Return resource uuid. */
        @NotNull public String getUUID() {
            return uuid;
        }

        /** Return resource entry variant. */
        @NotNull public String getVariant() {
            return variant;
        }
    }  // end class EntryInfo
}  // end class DbResource
