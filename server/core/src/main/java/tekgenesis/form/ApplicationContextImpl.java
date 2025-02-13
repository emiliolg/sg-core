
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.shiro.AuthorizationUtils;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.env.context.Context;
import tekgenesis.metadata.authorization.OrganizationalUnit;
import tekgenesis.metadata.authorization.User;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.env.context.Context.getContext;

/**
 * Implementation of Application Context.
 */
public class ApplicationContextImpl implements ApplicationContext {

    //~ Constructors .................................................................................................................................

    /** Default constructor for injection. */
    public ApplicationContextImpl() {}

    //~ Methods ......................................................................................................................................

    @Nullable @Override public OrganizationalUnit getCompany() {
        return getOrganizationalUnit().getCompany();
    }

    @NotNull @Override public Option<QName> getCurrentHistoryForm() {
        final String currentHistoryForm = getContext().getCurrentHistoryForm();
        return isNotEmpty(currentHistoryForm) ? some(createQName(currentHistoryForm)) : Option.empty();
    }

    @Override public boolean isAuthenticated() {
        return AuthorizationUtils.isAuthenticated();
    }

    @NotNull @Override public String getHost() {
        return getContext().getHost();
    }

    @NotNull @Override public InteractionMap getInteractionMap() {
        final String              id  = getLifeCycleId();
        final Map<Object, Object> map = Context.getSingleton(InfinispanCacheManager.class).getCache(Constants.SUIGENERIS_LIFE_CYCLE_MAP);
        return new InteractionMapImpl(id, map);
    }

    @NotNull @Override public OrganizationalUnit getOrganizationalUnit() {
        return AuthorizationUtils.getCurrentOrgUnit();
    }

    @NotNull @Override public User getUser() {
        return AuthorizationUtils.getCurrentUser();
    }

    private String getLifeCycleId() {
        return getContext().getLifeCycleId().getOrFail("Could not retrieve life cycle id! InteractionMap is disabled.");
    }

    //~ Methods ......................................................................................................................................

    /** Return ApplicationContext instance to be set on forms. */
    public static ApplicationContext getInstance() {
        return Context.getSingleton(ApplicationContext.class);
    }

    //~ Inner Classes ................................................................................................................................

    private static class InteractionMapImpl implements InteractionMap {
        @NotNull private final Map<Object, Object> holder;

        @NotNull private final String id;

        private InteractionMapImpl(@NotNull final String id, @NotNull final Map<Object, Object> holder) {
            this.id     = id;
            this.holder = holder;
        }

        @Override public boolean containsKey(@NotNull Object key) {
            final Map<Object, Object> map = getInteractionMapOrNull();
            return map != null && map.containsKey(key);
        }

        @Nullable @Override public Set<Map.Entry<Object, Object>> entrySet() {
            return holder.entrySet();
        }

        @Nullable @Override public Object get(@NotNull Object key) {
            final Map<Object, Object> map = getInteractionMapOrNull();
            return map != null ? map.get(key) : null;
        }

        @Nullable @Override public Set<Object> keySet() {
            return holder.keySet();
        }

        @Nullable @Override public Object put(@NotNull Object key, @NotNull Object value) {
            final Map<Object, Object> map    = getInteractionMap();
            final Object              result = map.put(key, value);
            holder.put(id, map);
            return result;
        }

        @Nullable @Override public Object remove(@NotNull Object key) {
            final Map<Object, Object> map = getInteractionMapOrNull();
            return map != null ? map.remove(key) : null;
        }

        @Nullable @Override public Collection<Object> values() {
            return holder.values();
        }

        private Map<Object, Object> getInteractionMap() {
            final Object              map    = holder.get(id);
            final Map<Object, Object> result;
            if (map == null) {
                result = new HashMap<>();
                holder.put(id, result);
            }
            else result = cast(map);
            return result;
        }

        /** Do not initialize map entry on hz unless strictly necessary! */
        @Nullable private Map<Object, Object> getInteractionMapOrNull() {
            return cast(holder.get(id));
        }
    }
}  // end class ApplicationContextImpl
