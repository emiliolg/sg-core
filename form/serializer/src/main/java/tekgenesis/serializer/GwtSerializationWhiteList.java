
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.serializer;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

import tekgenesis.aggregate.AggregateList;
import tekgenesis.metadata.form.model.FilterData;
import tekgenesis.model.TreeNode;
import tekgenesis.model.ValueCount;
import tekgenesis.type.assignment.AssignmentType;

/**
 * Hack Class to add custom classes to the serialization white list.
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
public class GwtSerializationWhiteList implements IsSerializable {

    //~ Instance Fields ..............................................................................................................................

    // Custom classes
    private ValueCount countLabel;
    private FilterData filterData;

    // Java classes
    private BigDecimal                    javaDecimal;
    private Double                        javaDouble;
    private int[]                         javaIntArray;
    private Integer                       javaInteger;
    private Long                          javaLong;
    private LinkedHashMap<Object, Object> linkedHashMap;  // GWT compiler doesn't like <?, ?>
    private AggregateList                 list;
    private TreeNode                      treeNode;
    private AssignmentType                type;
}
