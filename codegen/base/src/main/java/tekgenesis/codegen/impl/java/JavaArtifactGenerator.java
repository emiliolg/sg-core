
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.impl.java;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.ArtifactGenerator;
import tekgenesis.common.IndentedWriter;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.core.Strings;
import tekgenesis.common.util.Files;

import static tekgenesis.codegen.CodeGeneratorConstants.JAVA_LANG;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.QName.extractQualification;
import static tekgenesis.common.util.JavaReservedWords.PACKAGE;

/**
 * An Artifact is a Generated File with inside elements.
 */
class JavaArtifactGenerator extends ArtifactGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final Set<String>                   ignored;
    @NotNull private final Map<String, String>  imports;

    @NotNull private final String             notNullAnnotation;
    @NotNull private final String             nullableAnnotation;
    @NotNull private final String             packageName;
    @NotNull private final Map<String, QName> staticImports;

    //~ Constructors .................................................................................................................................

    JavaArtifactGenerator(File rootDir, @NotNull String currentPackage, String name, @NotNull String notNullAnnotation,
                          @NotNull String nullableAnnotation) {
        super(rootDir, currentPackage, name, Constants.JAVA_EXT);
        packageName             = currentPackage;
        imports                 = new TreeMap<>();
        staticImports           = new TreeMap<>();
        ignored                 = new HashSet<>();
        this.notNullAnnotation  = notNullAnnotation;
        this.nullableAnnotation = nullableAnnotation;
    }

    //~ Methods ......................................................................................................................................

    /** Process the class to extract the package and add it to the import list. */
    public String extractImport(Class<?> clazz) {
        return extractImport(clazz.getCanonicalName());
    }

    /** Process the class to extract the package and add it to the import list. */
    public String extractImport(String fqn) {
        if (!fqn.isEmpty() && Character.isUpperCase(fqn.charAt(0))) return fqn;

        // Handle Var args
        if (fqn.endsWith("...")) return extractImport(fqn.substring(0, fqn.length() - 3)) + "...";

        // skip generic (todo improve)
        if ("<T> T".equals(fqn)) return fqn;

        // Check Generics
        final int lt = fqn.indexOf('<');
        if (lt == -1) return doExtract(fqn);

        // Get Generic class and process
        String s = doExtract(fqn.substring(0, lt));
        // Get Generic arguments
        final int length = fqn.length();
        assert (fqn.charAt(length - 1) == '>');
        s += '<';

        final List<String> args = Strings.split(fqn.substring(lt + 1, length - 1), ',');
        for (int i = 0; i < args.size(); i++) {
            if (i > 0) s += ',';
            s += doExtract(args.get(i));
        }
        s += '>';
        return s;
    }

    /** Process the method/field to extract the name and add it to the static import list. */
    public QName extractStaticImport(QName fqn) {
        if (fqn.getQualification().isEmpty()) return fqn;

        final String simpleName  = fqn.getName();
        final QName  importedFqn = staticImports.get(simpleName);
        if (importedFqn == null) staticImports.put(simpleName, fqn);

        return importedFqn == null || importedFqn.equals(fqn) ? createQName("", simpleName) : fqn;
    }

    @SuppressWarnings("UnusedReturnValue")
    public File generate(@NotNull JavaItemGenerator<?> main) {
        FileWriter w    = null;
        final File file = getTargetFile();
        try {
            getDirectory().mkdirs();
            w = new FileWriter(file);

            final StringWriter sw = new StringWriter();
            main.generateElement(new IndentedWriter(sw), this);
            generateFileHeader(w);
            w.write(sw.toString());
        }
        catch (final IOException e) {
            // todo Log ??? or make generate returns an IOException ?
            throw new RuntimeException(e);
        }
        finally {
            Files.close(w);
        }
        return file;
    }

    /** Process the type to insert the corresponding package if exists in the import list. */
    public String insertImport(String type) {
        // Check Generics
        final int lt = type.indexOf('<');
        if (lt == -1) return doInsert(type);

        // Get Generic class and process
        String s = doInsert(type.substring(0, lt));
        // Get Generic arguments
        final int length = type.length();
        assert (type.charAt(length - 1) == '>');
        s += '<';

        final List<String> args = Strings.split(type.substring(lt + 1, length - 1), ',');
        for (int i = 0; i < args.size(); i++) {
            if (i > 0) s += ',';
            s += doInsert(args.get(i));
        }
        s += '>';
        return s;
    }

    /**
     * Insert the current package, Used for inner classes qualification. Do not use
     * qualify(pkg,name)
     */
    public String withPackage(String type) {
        return packageName + "." + type;
    }

    /** Returns the package Name. */
    @NotNull public String getPackageName() {
        return packageName;
    }

    void addIgnored(String simpleName) {
        ignored.add(simpleName);
    }

    void addNotNullAnnotation(StrBuilder result, boolean notNull) {
        result.appendElement("@").append(extractImport(notNull ? notNullAnnotation : nullableAnnotation));
    }

    void removeImport(String name) {
        final String key = extractName(name);
        final String fqn = imports.get(key);
        if (fqn.equals(name)) imports.remove(key);
    }

    private String doExtract(String fqn) {
        final int dot = fqn.lastIndexOf('.');
        if (dot == -1) return fqn;

        final String simpleName = fqn.substring(dot + 1);
        final String pkgName    = fqn.substring(0, dot);
        if (packageName.equals(pkgName)) {
            // no need to import, the fqn is in the same package
            addIgnored(simpleName);
            return simpleName;
        }
        if (ignored.contains(simpleName)) return fqn;

        final String importedFqn = imports.get(simpleName);

        if (importedFqn == null) {
            imports.put(simpleName, fqn);
            return simpleName;
        }
        return importedFqn.equals(fqn) ? simpleName : fqn;
    }

    private String doInsert(String type) {
        String    result = type;
        final int dot    = type.lastIndexOf('.');
        if (dot == -1) {
            final String fqn = imports.get(type);
            result = isEmpty(fqn) ? type : fqn;  // Same package and void
        }
        return result;
    }

    private void generateFileHeader(@NotNull Writer writer) {
        final IndentedWriter w = new IndentedWriter(writer);
        w.print(PACKAGE);
        w.print(" ");
        w.print(packageName);
        w.println(";");
        w.newLine();
        for (final String s : imports.values()) {
            if (!extractQualification(s).equals(JAVA_LANG)) w.printf("import %s;%n", s);
        }
        for (final QName s : staticImports.values())
            w.printf("import static %s;%n", s.getFullName());
    }
}  // end class JavaArtifactGenerator
