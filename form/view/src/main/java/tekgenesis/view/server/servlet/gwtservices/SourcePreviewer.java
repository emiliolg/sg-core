
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet.gwtservices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Strings;
import tekgenesis.common.util.Files;
import tekgenesis.view.shared.response.ResponseError;

import static java.io.File.separator;
import static java.lang.Math.max;
import static java.lang.Math.min;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.QName.extractQualification;
import static tekgenesis.view.shared.response.ResponseError.Line;

/**
 * Source previewer.
 */
class SourcePreviewer {

    //~ Constructors .................................................................................................................................

    private SourcePreviewer() {}

    //~ Methods ......................................................................................................................................

    static Option<List<Line>> preview(@NotNull ResponseError error, @Nullable String sources) {
        Option<List<Line>> result = Option.empty();

        if (isNotEmpty(error.getClassName()) && isNotEmpty(sources)) {
            final String domain = extractQualification(error.getClassName()).replace(".", separator);
            final String path   = domain + separator + error.getFileName();
            for (final String root : Strings.split(sources, File.pathSeparator.charAt(0))) {
                final File file = new File(root + separator + path);
                if (file.exists()) {
                    result = preview(file, error.getFileName(), error.getLine());
                    break;
                }
            }
        }

        return result;
    }

    private static Option<List<Line>> preview(File file, String source, int line) {
        Option<List<Line>> result = Option.empty();

        try {
            final List<String> strings = Files.readLines(new FileReader(file));
            final List<Line>   preview = new ArrayList<>(LINES_PREVIEW);

            final int from = max(0, line - LINES_PREVIEW / 2 - 2);
            final int to   = min(strings.size(), max((line + LINES_PREVIEW / 2 + 1), LINES_PREVIEW));

            for (int i = from; i < to; i++)
                preview.add(new Line(i + 1, source, strings.get(i)));
            result = Option.some(preview);
        }
        catch (final FileNotFoundException ignored) {}

        return result;
    }

    //~ Static Fields ................................................................................................................................

    private static final int LINES_PREVIEW = 10;
}  // end class SourcePreviewer
