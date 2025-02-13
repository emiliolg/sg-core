
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.serializer;

import java.io.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;

import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.ImmutableList.Builder;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.form.Download;
import tekgenesis.form.DownloadImpl;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.model.WidgetDefModel;
import tekgenesis.metadata.form.widget.*;
import tekgenesis.repository.ModelRepository;
import tekgenesis.view.server.servlet.FormModelDownload;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.collections.Colls.deepSeq;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.metadata.form.IndexedWidget.createIndexed;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.*;

@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc", "MagicNumber" })
public class FormSerializationTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public ISRule hz = new ISRule();

    //~ Methods ......................................................................................................................................

    @Test public void testBigFormModelSerialization()
        throws IOException, BuilderException, ClassNotFoundException
    {
        final WidgetBuilder table = table("Table").children(field("Column").id("column")).id("table");

        final Form form = form("", "tekgenesis.test", "BigTestForm").children(field("Text").id("text"), table)
                          .unrestricted()
                          .withRepository(new ModelRepository())
                          .build();

        final Widget text = form.getElement("text");

        final FormModel modelToMap = new FormModel(form);
        modelToMap.set(text, "value");
        modelToMap.updateFocus(createIndexed(text, empty()).toSourceWidget());

        final MultipleModel tableModel = modelToMap.getMultiple("table");
        final Widget        column     = form.getElement("column");
        for (int i = 0; i < 15; i++) {
            final RowModel row = tableModel.addRow();
            row.set(column, "value" + i);
        }

        final FormModel deserialized = serializeDeserialize(serializeDeserialize(modelToMap));

        // Deserialize

        final FormModel modelFromMap = deserialized.init(form);
        assertThat(modelFromMap.get(text)).isEqualTo("value");
        assertThat(modelFromMap.getLastFocus().getPath()).isEqualTo(text.getName());
    }

    @Test public void testDownload()
        throws IOException, BuilderException, ClassNotFoundException
    {
        final Form form = form("", "tekgenesis.test", "HzTestForm").children(field("Text").id("text"))
                          .unrestricted()
                          .withRepository(new ModelRepository())
                          .build();

        final Widget text = form.getElement("text");

        final FormModel modelToMap = new FormModel(form);
        modelToMap.set(text, "value");
        modelToMap.updateFocus(createIndexed(text, empty()).toSourceWidget());

        final DownloadImpl downloadToMap = new DownloadImpl(TestDownload.class);
        downloadToMap.withFileName("hello.text");

        final FormModelDownload toMap = new FormModelDownload(modelToMap, downloadToMap);

        final ConcurrentMap<Object, Object> map = hz.getUserMap("test-tekgenesis-forms");

        serializeDeserialize(serializeDeserialize(modelToMap));

        // Serialize
        map.put(KEY, toMap);

        // Deserialize
        final FormModelDownload fromMap = (FormModelDownload) map.get(KEY);

        final DownloadImpl downloadFromMap = fromMap.getDownload();

        assertThat(downloadFromMap.getWriter()).isEqualTo(TestDownload.class);
        assertThat(downloadFromMap.getFilename()).isEqualTo("hello.text");

        final FormModel modelFromMap = fromMap.init(form);
        assertThat(modelFromMap.get(text)).isEqualTo("value");
        assertThat(modelFromMap.getLastFocus().getPath()).isEqualTo(text.getName());
    }

    @Test public void testFormModelSerialization()
        throws IOException, BuilderException, ClassNotFoundException
    {
        final Form form = form("", "tekgenesis.test", "HzTestForm").children(field("Text").id("text"))
                          .unrestricted()
                          .withRepository(new ModelRepository())
                          .build();

        final Widget text = form.getElement("text");

        final FormModel modelToMap = new FormModel(form);
        modelToMap.set(text, "value");
        modelToMap.updateFocus(createIndexed(text, empty()).toSourceWidget());

        final FormModel deserialized = serializeDeserialize(serializeDeserialize(modelToMap));

        final FormModel modelFromMap = deserialized.init(form);
        assertThat(modelFromMap.get(text)).isEqualTo("value");
        assertThat(modelFromMap.getLastFocus().getPath()).isEqualTo(text.getName());
    }

    @Test public void testSubformModelSerialization()
        throws IOException, BuilderException, ClassNotFoundException
    {
        final ModelRepository repository = new ModelRepository();

        final WidgetBuilder subformTable = table("Table").children(field("Text").id("columnText")).id("table");

        final Form subform = form("", "tekgenesis.test", "HzTestSubForm").children(field("Text").id("text"), subformTable)
                             .unrestricted()
                             .withRepository(repository)
                             .build();

        final WidgetBuilder table = table("Table").children(subform("Subform", subform.getFullName()).id("columnSubform")).id("table");

        final Form form = form("", "tekgenesis.test", "HzTestForm").children(subform("Subform", subform.getFullName()).id("subform"), table)
                          .unrestricted()
                          .withRepository(repository)
                          .build();

        final FormModel subformModelToMap = new FormModel(subform);
        final Widget    text              = subform.getElement("text");
        subformModelToMap.set(text, "value");

        final FormModel modelToMap = new FormModel(form);
        final Widget    sf         = form.getElement("subform");
        modelToMap.setSubform(sf, subformModelToMap);

        final MultipleWidget multiple           = (MultipleWidget) form.getElement("table");
        final MultipleModel  multipleModelToMap = modelToMap.getMultiple(multiple);
        final RowModel       row                = multipleModelToMap.addRow();
        final Widget         csf                = form.getElement("columnSubform");
        row.setSubform(csf, subformModelToMap);

        final FormModel deserialized = serializeDeserialize(serializeDeserialize(modelToMap));

        final FormModel modelFromMap        = deserialized.init(form);
        final FormModel subformModelFromMap = modelFromMap.getSubform(sf);
        assertThat(subformModelFromMap).isNotNull();

        assertThat(subformModelFromMap.get(text)).isEqualTo("value");

        final FormModel subFormInMultiple = modelFromMap.getMultiple("table").getRow(0).getSubform(csf);
        assertThat(subFormInMultiple).isNotNull();

        subFormInMultiple.init(subform);

        assertThat(subFormInMultiple.get(0)).isEqualTo("value");
    }  // end method testSubformModelSerialization

    @Test public void testWidgetDefModelSerialization()
        throws IOException, BuilderException, ClassNotFoundException
    {
        final ModelRepository repository = new ModelRepository();

        /*form BinaryTree {
         *  head    : widget(BinaryNode), optional;
         * }
         *
         * widget BinaryNode {
         *  value   : String;
         *  left    : widget(BinaryNode), optional;
         *  right   : widget(BinaryNode), optional;
         *}*/

        final WidgetDef node = widget("", "tekgenesis.test", "BinaryNode").children(  //
                    field("Value").id("value"),
                    widget("Left", "tekgenesis.test.BinaryNode").id("left").optional(),
                    widget("Right", "tekgenesis.test.BinaryNode").id("right").optional())
                               .withRepository(repository)
                               .build();

        final Form tree = form("", "tekgenesis.test", "BinaryTree").children(  //
                    widget("Head", node).id("head").optional())                //
                          .unrestricted().withRepository(repository).build();

        final Widget head  = tree.getElement("head");
        final Widget value = node.getElement("value");
        final Widget left  = node.getElement("left");
        final Widget right = node.getElement("right");

        final FormModel root = new FormModel(tree);

        final WidgetDefModel node4 = new WidgetDefModel(node, root, head, empty());
        node4.set(value, "4");
        root.setWidgetDef(head, node4);

        final WidgetDefModel node2 = new WidgetDefModel(node, node4, left, empty());
        node2.set(value, "2");
        node4.setWidgetDef(left, node2);

        final WidgetDefModel node7 = new WidgetDefModel(node, node4, right, empty());
        node7.set(value, "7");
        node4.setWidgetDef(right, node7);

        final WidgetDefModel node3 = new WidgetDefModel(node, node2, right, empty());
        node3.set(value, "3");
        node2.setWidgetDef(right, node3);

        final FormModel deserialized = serializeDeserialize(serializeDeserialize(root));

        class Node implements Iterable<Node> {
            private final WidgetDefModel node;

            private Node(WidgetDefModel node) {
                this.node = node;
            }

            @Override
            @SuppressWarnings("ConstantConditions")
            public Iterator<Node> iterator() {
                final Builder<Node> builder = ImmutableList.builder();
                if (node.isDefined(left)) builder.add(new Node(node.getWidgetDef(left)));
                if (node.isDefined(right)) builder.add(new Node(node.getWidgetDef(right)));
                return builder.build().iterator();
            }
        }

        final WidgetDefModel h = deserialized.getWidgetDef(head);
        assertThat(h).isNotNull();

        final Seq<String> nodes = deepSeq(listOf(new Node(h))).map(n -> (String) n.node.get(value));
        assertThat(nodes).containsExactly("4", "2", "3", "7");
    }  // end method testWidgetDefModelSerialization

    private FormModel serializeDeserialize(FormModel model)
        throws IOException, ClassNotFoundException
    {
        final ISFormModelSerializer serializer = new ISFormModelSerializer();
        final ByteArrayOutputStream out        = new ByteArrayOutputStream();
        final ObjectOutputStream    output     = new ObjectOutputStream(out);
        serializer.writeObject(output, model);
        output.flush();
        output.close();
        return serializer.readObject(new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())));
    }

    //~ Static Fields ................................................................................................................................

    private static final String KEY = "key1";

    //~ Inner Classes ................................................................................................................................

    private static class TestDownload implements Download.DownloadWriter {
        @Override public void into(@NotNull OutputStream stream)
            throws IOException
        {
            final String msg = "Hello Test World!";
            stream.write(msg.getBytes(Constants.UTF8));
            stream.flush();
        }
    }
}  // end class FormSerializationTest
