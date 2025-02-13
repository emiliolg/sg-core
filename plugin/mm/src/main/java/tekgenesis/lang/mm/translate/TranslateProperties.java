
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.translate;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.io.PropertyWriter;
import tekgenesis.common.logging.Logger;
import tekgenesis.lang.mm.FileUtils;

import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Constants.*;

/**
 * Represents a translate properties file.
 */
class TranslateProperties  /*implements Iterable<Property>*/ {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Map<String, Property> properties;

    //~ Constructors .................................................................................................................................

    /** Create properties. */
    TranslateProperties() {
        properties = new LinkedHashMap<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TranslateProperties)) return false;
        final TranslateProperties that = (TranslateProperties) o;
        return properties.equals(that.properties);
    }

    @Override public int hashCode() {
        return properties.hashCode();
    }

    void add(@NotNull final Property property) {
        properties.put(property.getKey(), property);
    }

    void add(String key, String value, boolean generated, String label) {
        add(key, value, generated, label, false, false);
    }

    void add(String key, String value, boolean generated, String label, boolean added, boolean changed) {
        properties.put(key, new Property(key, value, generated, label, added, changed));
    }

    Seq<Property> changed() {
        return immutable(properties.values()).filter(Property::isChanged);
    }

    /** Calculates changes to properties given last translation definitions. */
    PropertiesDelta delta(@NotNull final Map<String, String> last) {
        final PropertiesDelta delta = new PropertiesDelta();

        // register in delta deleted properties and those that have @Generated and changed their label
        immutable(properties.values()).forEach(property -> {
            final String lastValue = last.get(property.getKey());
            if (lastValue == null) delta.deleted();
        });

        for (final Map.Entry<String, String> entry : last.entrySet()) {
            final String   key      = entry.getKey();
            final Property property = properties.get(key);

            // if property its null, its an added property
            if (property == null) delta.property(new Property(key, entry.getValue(), true, entry.getValue(), true, true));
            // if label changed or has generated tag then mark it as changed so its translated again
            else if (!entry.getValue().trim().equals(property.getLabel().trim()) || property.isGenerated())
                delta.property(new Property(key, entry.getValue(), property.isGenerated(), entry.getValue(), false, true));
            // if nothing changed
            else delta.property(property);
        }

        return delta;
    }  // end method delta

    /** Write all properties to writer. */
    void write(@NotNull final PropertyWriter writer) {
        for (final Property property : properties.values())
            property.write(writer);
    }

    @NotNull Map<String, Property> getProperties() {
        return properties;
    }

    private int addedSize() {
        return immutable(properties.values()).filter(Property::isAdded).size();
    }

    private int size() {
        return properties.size();
    }

    //~ Methods ......................................................................................................................................

    /** Load properties from file. */
    static TranslateProperties load(@NotNull final File file) {
        final TranslateProperties properties = new TranslateProperties();

        try {
            loadInto(properties, file);
        }
        catch (final IOException ignored) {
            Logger.getLogger(TranslateProperties.class).error(ignored);
        }

        return properties;
    }

    @SuppressWarnings(
            {
                "MagicNumber", "OverlyComplexMethod", "OverlyLongMethod",
                "OverlyNestedMethod", "ContinueStatement", "Duplicates"
            }
                     )
    private static void loadInto(@NotNull final TranslateProperties properties, @NotNull final File file)
        throws IOException
    {
        boolean generated              = false;
        String  labelBeforeTranslation = "";
        // The spec says that the file must be encoded using ISO-8859-1.
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), ISO_8859_1));
        String               line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            char c   = 0;
            int  pos = 0;
            // Leading whitespaces must be deleted first.
            while (pos < line.length() && Character.isWhitespace(c = line.charAt(pos)))
                pos++;

            if (pos < line.length() && line.charAt(pos) == '#') {
                if (line.contains(FileUtils.GENERATED)) generated = true;
                else {
                    pos++;
                    while (pos < line.length() && Character.isWhitespace(line.charAt(pos)))
                        pos++;
                    labelBeforeTranslation = line.substring(pos);
                }
                continue;
            }
            // The characters up to the next Whitespace, ':', or '='
            // describe the key.  But look for escape sequences.
            // Try to short-circuit when there is no escape char.
            final int          start       = pos;
            final boolean      needsEscape = line.indexOf('\\', pos) != -1;
            final StringBuffer key         = needsEscape ? new StringBuffer() : null;

            while (pos < line.length() && !Character.isWhitespace(c = line.charAt(pos++)) && c != '=' && c != ':') {
                if (needsEscape && c == '\\') {
                    if (pos == line.length()) {
                        // The line continues on the next line.  If there
                        // is no next line, just treat it as a key with an
                        // empty value.
                        line = reader.readLine();
                        if (line == null) line = "";
                        pos = 0;
                        while (pos < line.length() && Character.isWhitespace(line.charAt(pos)))
                            pos++;
                    }
                    else {
                        c = line.charAt(pos++);
                        switch (c) {
                        case 'n':
                            key.append('\n');
                            break;
                        case 't':
                            key.append('\t');
                            break;
                        case 'r':
                            key.append('\r');
                            break;
                        case 'u':
                            if (pos + 4 <= line.length()) {
                                final char uni = (char) Integer.parseInt(line.substring(pos, pos + 4), 16);
                                key.append(uni);
                                pos += 4;
                            }  // else throw exception?
                            break;
                        default:
                            key.append(c);
                            break;
                        }
                    }
                }
                else if (needsEscape) key.append(c);
            }

            final boolean isDelim   = (c == ':' || c == '=');
            final String  keyString;
            if (needsEscape) keyString = key.toString();
            else if (isDelim || Character.isWhitespace(c)) keyString = line.substring(start, pos - 1);
            else keyString = line.substring(start, pos);

            while (pos < line.length() && Character.isWhitespace(c = line.charAt(pos)))
                pos++;

            if (!isDelim && (c == ':' || c == '=')) {
                pos++;
                while (pos < line.length() && Character.isWhitespace(line.charAt(pos)))
                    pos++;
            }

            // Short-circuit if no escape chars found.
            if (!needsEscape && !"".equals(keyString)) {
                properties.add(keyString, line.substring(pos), generated, labelBeforeTranslation);
                generated              = false;
                labelBeforeTranslation = "";
                continue;
            }

            // Escape char found so iterate through the rest of the line.
            final StringBuilder element = new StringBuilder(line.length() - pos);

            while (pos < line.length()) {
                c = line.charAt(pos++);
                if (c == '\\') {
                    if (pos == line.length()) {
                        // The line continues on the next line.
                        line = reader.readLine();
                        // We might have seen a backslash at the end of
                        // the file.  The JDK ignores the backslash in
                        // this case, so we follow for compatibility.
                        if (line == null) break;

                        pos = 0;
                        while (pos < line.length() && Character.isWhitespace(line.charAt(pos)))
                            pos++;
                        element.ensureCapacity(line.length() - pos + element.length());
                    }
                    else {
                        c = line.charAt(pos++);
                        switch (c) {
                        case 'n':
                            element.append('\n');
                            break;
                        case 't':
                            element.append('\t');
                            break;
                        case 'r':
                            element.append('\r');
                            break;
                        case 'u':
                            if (pos + 4 <= line.length()) {
                                final char uni = (char) Integer.parseInt(line.substring(pos, pos + 4), 16);
                                element.append(uni);
                                pos += 4;
                            }  // else throw exception?
                            break;
                        default:
                            element.append(c);
                            break;
                        }
                    }
                }
                else element.append(c);
            }
            if (!"".equals(keyString)) {
                properties.add(keyString, element.toString(), generated, labelBeforeTranslation);
                labelBeforeTranslation = "";
                generated              = false;
            }
        }
    }                          // end method loadInto

    //~ Inner Classes ................................................................................................................................

    /**
     * Delta including unchanged, added and deleted properties.
     */
    static class PropertiesDelta {
        private int                                added;
        private int                                changed;
        private int                                deleted;
        @NotNull private final TranslateProperties properties;

        private PropertiesDelta() {
            properties = new TranslateProperties();
            deleted    = 0;
            changed    = 0;
            added      = 0;
        }

        void message(@NotNull final StringBuilder message) {
            message.append("[ unchanged: ")
                .append(properties.size() - added - changed)
                .append(", added: ")
                .append(added)
                .append(", changed: ")
                .append(changed)
                .append(", deleted: ")
                .append(deleted)
                .append(" ]");
        }

        void property(@NotNull Property property) {
            if (property.isAdded()) added++;
            else if (property.isChanged()) changed++;
            properties.add(property);
        }

        @NotNull TranslateProperties getProperties() {
            return properties;
        }

        /** Increment delete count. */
        private void deleted() {
            deleted++;
        }
    }  // end class PropertiesDelta
}  // end class TranslateProperties
