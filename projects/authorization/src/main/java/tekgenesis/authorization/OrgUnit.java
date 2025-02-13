
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.g.OrgUnitBase;
import tekgenesis.authorization.shiro.SuiGenerisAuthorizingRealm;
import tekgenesis.common.collections.ImmutableSet;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.authorization.OrganizationalUnit;
import tekgenesis.metadata.authorization.Property;

import static tekgenesis.authorization.shiro.SuiGenerisAuthorizingRealm.ROOT_OU;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.metadata.authorization.PropertyImpl.createProperty;

/**
 * Organizational Unit class.
 */
@SuppressWarnings({ "WeakerAccess", "DuplicateStringLiteralInspection" })
public class OrgUnit extends OrgUnitBase implements OrganizationalUnit {

    //~ Methods ......................................................................................................................................

    @Nullable @Override public OrganizationalUnit getCompany() {
        return getCompany(this);
    }

    @NotNull @Override public ImmutableSet<String> getHierarchy() {
        return immutable(data().hierarchy);
    }

    /** Returns an OrgUnit's path to the root. */
    public Seq<OrgUnit> getPath() {
        final LinkedList<OrgUnit> path = new LinkedList<>();
        for (OrgUnit orgUnit = this; orgUnit != null; orgUnit = orgUnit.getParent())
            path.add(0, orgUnit);
        return seq(path);
    }

    @NotNull @Override public Iterable<Property> getProperties() {
        return getProps().map(ouProp -> createProperty(ouProp.getPropertyId(), ouProp.getPropertyName(), ouProp.getValue()));
    }

    @Nullable @Override public Property getProperty(final String key) {
        final Option<OrgUnitProperties> ouProp = getProps().filter(orgUnitProps ->
                    orgUnitProps != null && orgUnitProps.getProperty().getId().equals(key)).getFirst();

        if (ouProp.isPresent()) {
            final OrgUnitProperties prop = ouProp.get();
            return createProperty(prop.getPropertyId(), prop.getPropertyName(), prop.getValue());
        }
        else return null;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Returns an OrgUnit's company OU. (a.k.a. the root OrgUnit from which the base one descends,
     * discarding 'root OU').
     */
    @Nullable public static OrgUnit getCompany(OrgUnit base) {
        for (OrgUnit orgUnit = base; orgUnit != null; orgUnit = orgUnit.getParent()) {
            final OrgUnit parent = orgUnit.getParent();
            if (parent != null && parent.getName().equals(ROOT_OU)) return orgUnit;
        }
        return null;
    }

    /** Returns the root of the OrgUnit tree. */
    @NotNull public static OrgUnit getRoot() {
        return SuiGenerisAuthorizingRealm.getRootOu();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 0L;

    //~ Inner Classes ................................................................................................................................

    public static final class Data extends OpenData {
        private final Set<String> hierarchy = new HashSet<>();

        @Override public void onLoad(OrgUnit instance) {
            hierarchy.add(instance.getName());
            for (final OrgUnit child : instance.getChildren())
                hierarchy.addAll(child.getHierarchy());
        }

        private static final long serialVersionUID = 0L;
    }
}  // end class OrgUnit
