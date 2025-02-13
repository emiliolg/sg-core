
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.social;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.Option.some;

/**
 * Social provider service loading.
 */
public class SocialProviderService {

    //~ Instance Fields ..............................................................................................................................

    private final Seq<SocialProvider<SocialCredential, Profile>> providers;

    //~ Constructors .................................................................................................................................

    private SocialProviderService() {
        providers = loadProviders();
    }

    //~ Methods ......................................................................................................................................

    /** Load all social providers and filter with those enabled by configuration. */
    private Seq<SocialProvider<SocialCredential, Profile>> loadProviders() {
        return seq(getAvailableSocialProviders());
    }

    @NotNull private List<SocialProvider<SocialCredential, Profile>> getAvailableSocialProviders() {
        @SuppressWarnings("rawtypes")
        final ServiceLoader<SocialProvider>                   loader = ServiceLoader.load(SocialProvider.class);
        final List<SocialProvider<SocialCredential, Profile>> result = new ArrayList<>();

        final Environment environment = Context.getEnvironment();
        for (final SocialProvider<?, ?> client : loader) {
            final SocialProviderProps props = environment.get(client.getName(), SocialProviderProps.class);
            if (props.enabled) result.add(cast(client));
        }
        return result;
    }

    //~ Methods ......................................................................................................................................

    /** Find optional provider, matching specified name, if enabled. */
    @NotNull public static <C extends SocialProvider<? extends SocialCredential, ? extends Profile>> Option<C> findProvider(
        @NotNull final String provider) {
        for (final SocialProvider<SocialCredential, Profile> client : getProviders()) {
            if (equal(client.getName(), provider)) return some(cast(client));
        }
        return Option.empty();
    }

    /** Return true if there is at least one available and enabled social client. */
    public static boolean isActive() {
        return !getProviders().isEmpty();
    }

    /** Return client, matching specified class, if enabled. */
    @NotNull public static <C extends SocialProvider<SocialCredential, Profile>> C getProvider(@NotNull final Class<C> clientClass) {
        for (final SocialProvider<SocialCredential, Profile> client : getProviders()) {
            if (clientClass.isInstance(client)) return cast(client);
        }
        throw SocialException.cannotFindClient(clientClass.getSimpleName());
    }

    /**
     * Return provider, matching specified name, if enabled. Throws exception if provider is not
     * found.
     */
    @NotNull public static <C extends SocialProvider<? extends SocialCredential, ? extends Profile>> C getProvider(@NotNull final String provider) {
        final Option<C> result = findProvider(provider);
        return result.orElseThrow(() -> SocialException.cannotFindClient(provider));
    }

    /** Return all enabled social providers. */
    public static Seq<SocialProvider<SocialCredential, Profile>> getProviders() {
        return SocialClientServiceHolder.service.providers;
    }

    //~ Inner Classes ................................................................................................................................

    private static class SocialClientServiceHolder {
        private static final SocialProviderService service = new SocialProviderService();
    }
}  // end class SocialProviderService
