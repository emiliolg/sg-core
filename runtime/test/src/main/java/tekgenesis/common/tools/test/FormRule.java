
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.env.security.Session;
import tekgenesis.form.Actions;
import tekgenesis.form.ActionsImpl;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.repository.ModelRepository;
import tekgenesis.security.shiro.ShiroConfiguration;
import tekgenesis.security.shiro.ShiroSession;

import static tekgenesis.common.env.context.Context.bind;
import static tekgenesis.common.env.context.Context.getEnvironment;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection" })
public class FormRule implements TestRule {

    //~ Instance Fields ..............................................................................................................................

    public ShiroConfiguration shiroConfiguration = null;

    //~ Methods ......................................................................................................................................

    @Override public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override public void evaluate()
                throws Throwable
            {
                final ShiroProps.ShiroUserProps user1 = new ShiroProps.ShiroUserProps();
                user1.userConfig = "password";
                getEnvironment().put("user1", user1);

                final ShiroProps shiroProps = new ShiroProps();
                shiroProps.adminUserConfig = "password";
                shiroProps.users           = "user1";

                getEnvironment().put(shiroProps);

                final ApplicationProps applicationProps = new ApplicationProps();
                applicationProps.defaultRoles = "Developer";
                applicationProps.adminUser    = "admin";
                getEnvironment().put(applicationProps);

                shiroConfiguration = new ShiroConfiguration(true, getEnvironment());
                bind(Session.class, ShiroSession.class);

                SecurityUtils.rebindSession();

                bind(Actions.class, ActionsImpl.class);

                SecurityUtils.getSession().authenticate("admin", "password");
                final ModelRepositoryLoader loader = new ModelRepositoryLoader(Thread.currentThread().getContextClassLoader());
                Context.getContext().setSingleton(ModelRepository.class, loader.build());

                base.evaluate();
            }
        };
    }
}  // end class FormRule
