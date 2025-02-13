
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.security.shiro.web;

import org.apache.shiro.config.Ini;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.mgt.WebSecurityManager;

import tekgenesis.common.env.context.Context;
import tekgenesis.security.shiro.ShiroConfiguration;

import static tekgenesis.common.Predefined.cast;

/**
 * Configure the Web Environment for Suigen.
 */
@SuppressWarnings("WeakerAccess")
public class WebEnvironment extends IniWebEnvironment {

    //~ Constructors .................................................................................................................................

    /** Creates a new Shiro Web Environment. */
    @SuppressWarnings("WeakerAccess")
    public WebEnvironment() {
        super();
    }

    //~ Methods ......................................................................................................................................

    @Override public Ini getIni() {
        return getShiroConfiguration().getIni();
    }

    @Override public org.apache.shiro.mgt.SecurityManager getSecurityManager()
        throws IllegalStateException
    {
        return getShiroConfiguration().getSecurityManager();
    }

    @Override public WebSecurityManager getWebSecurityManager() {
        return cast(getSecurityManager());
    }

    private ShiroConfiguration getShiroConfiguration() {
        // Todo... this does not look good...
        // I think the Shiro Configuration should be created here, and may be also in an standalone startup...
        // In this way the singleton will be useless..
        return Context.getSingleton(ShiroConfiguration.class);
    }

    // @Override
    // public FilterChainResolver getFilterChainResolver() {
    //
    // if(chainResolver == null) {
    // chainResolver = new PathMatchingFilterChainResolver();
    // final FilterChainManager manager = chainResolver.getFilterChainManager();
    //
    // final Map<String,String> urls  = ShiroConfiguration.getWebUrlsConfiguration();
    // for (Map.Entry<String, String> stringStringEntry : urls.entrySet()) {
    // manager.createChain(stringStringEntry.getKey(), stringStringEntry.getValue());
    // }
    // }
    // return chainResolver;
    // }
}  // end class WebEnvironment
