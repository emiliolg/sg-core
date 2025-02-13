
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.project;

import java.io.File;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.codegen.impl.js.JsCodeGenerator;
import tekgenesis.codegen.js.AngularModuleCodeGenerator;
import tekgenesis.common.core.Constants;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.handler.Handler;
import tekgenesis.metadata.handler.HandlerBuilder;

import static tekgenesis.codegen.common.MMCodeGenConstants.MODULE;
import static tekgenesis.codegen.js.AngularModuleCodeGenerator.jsFileName;
import static tekgenesis.codegen.project.HandlerGenerationTest.*;
import static tekgenesis.common.core.QName.qualify;
import static tekgenesis.common.tools.test.Tests.checkDiff;

/**
 * Test Js Handler Code Generator;
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "ClassWithTooManyMethods" })
public class HandlerJsGenerationTest {

    //~ Instance Fields ..............................................................................................................................

    private final File goldenDir = new File("codegen/metamodel/src/test/data");

    private final File outputDir = new File("target/codegen/metamodel/test-output");

    //~ Methods ......................................................................................................................................

    @Test public void anyRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForAnyRouting();
        checkHandlerRemoteFileGeneration(builder.build());
    }

    @Test public void remoteAliasOverloadingRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForAliasOverloadingRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remoteAliasRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForAliasRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remoteHtmlRoutingWithParameters()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForHtmlRoutingWithParameters();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remotePostWithBodyRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForPostWithBodyRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remotePostWithMultipleBodyRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForPostWithMultipleBodyRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remoteServiceRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForServiceRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void typedRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForTypedRouting();
        checkHandlerRemoteFileGeneration(builder.build());
    }

    @Test public void validPartsIdsRoutingRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForValidPartsIdsRouting();
        checkHandlerRemoteFileGeneration(builder.build());
    }

    @Test public void voidRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForVoidRouting();
        checkHandlerRemoteFileGeneration(builder.build());
    }

    private void checkHandlerRemoteFileGeneration(@NotNull Handler handler) {
        final JsCodeGenerator            cg   = new JsCodeGenerator(outputDir, handler.getDomain());
        final AngularModuleCodeGenerator base = new AngularModuleCodeGenerator(cg, handler);
        base.generate();

        checkJsFile(handler, MODULE);
    }

    private void checkJsFile(Handler handler, String suffix) {
        final String fileName = qualify(handler.getDomain(), jsFileName(handler)).replaceAll("\\.", File.separator);
        checkDiff(new File(outputDir, fileName + "." + Constants.JS_EXT), new File(goldenDir, fileName + "." + Constants.JS_EXT));
    }
}  // end class HandlerJsGenerationTest
