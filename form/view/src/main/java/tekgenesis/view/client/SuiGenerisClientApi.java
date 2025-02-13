
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.security.shiro.web.URLConstants;
import tekgenesis.view.client.dialog.HelpDialog;
import tekgenesis.view.client.dialog.LoginDialog;
import tekgenesis.view.client.menu.MenuBox;
import tekgenesis.view.client.menu.MenuConfiguration;
import tekgenesis.view.client.menu.OuChanger;
import tekgenesis.view.client.menu.UsernameConfiguration;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.shared.response.ResponseError;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.model.FormConstants.ALIGNED_ICONS;
import static tekgenesis.view.client.Application.getInstance;
import static tekgenesis.view.client.BoxConfiguration.DEFAULT;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.querySelector;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.img;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.span;

/**
 * Api to interact with the forms in the client.
 */
public class SuiGenerisClientApi {

    //~ Constructors .................................................................................................................................

    private SuiGenerisClientApi() {}

    //~ Methods ......................................................................................................................................

    /** Returns the id of the current user. */
    static native String user()  /*-{ return $wnd.__gwt_UserId || ""; }-*/;

    //J-
    /** Register SuiGenerisClientApi. */
    static native void register()  /*-{

        var inject = $wnd.sg ? $wnd.sg.inject : $wnd.$ ? $wnd.$.suigeneris : null;
        if (!inject) {
            @tekgenesis.view.client.SuiGenerisClientApi::missingSuigenerisObject()();
        } else {

            // Attach box.
            inject.box = function (id, configuration) {
                @tekgenesis.view.client.SuiGenerisClientApi::attach(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(id, configuration)
            };

            // Detach box.
            inject.detach = function (id) {
                @tekgenesis.view.client.BoxHandler::detach(Ljava/lang/String;)(id);
            };

            // Feedback.
            inject.feedback = function (id) {
                @tekgenesis.view.client.SuiGenerisClientApi::feedback(Ljava/lang/String;)(id);
            };

            // Load specified form.
            inject.load = function (id, fqn, pk) {
                @tekgenesis.view.client.BoxHandler::load(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(id, fqn, pk);
            };

            // Cancel current form.
            inject.cancel = function (id) {
                @tekgenesis.view.client.BoxHandler::cancel(Ljava/lang/String;)(id);
            };

            // Request box focus.
            inject.focus = function (id) {
                @tekgenesis.view.client.BoxHandler::focus(Ljava/lang/String;)(id);
            };

            // Add box listener.
            inject.addListener = function (id, listener) {
                @tekgenesis.view.client.SuiGenerisClientApi::addBoxListener(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(id, listener);
            };

            // Remove box listener.
            inject.removeListener = function (id, listener) {
                @tekgenesis.view.client.SuiGenerisClientApi::removeBoxListener(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(id, listener);
            };

            // Attach menu.
            inject.menu = function (id, configuration) {
                @tekgenesis.view.client.SuiGenerisClientApi::attachMenu(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(id, configuration)
            };

            // Switch user.
            inject.switchUser = function (id) {
                @tekgenesis.view.client.SuiGenerisClientApi::switchUser(Ljava/lang/String;)(id);
            };

            // Change ou.
            inject.changeOu = function (id) {
                @tekgenesis.view.client.SuiGenerisClientApi::changeOu(Ljava/lang/String;)(id);
            };

            // Shortcuts.
            inject.shortcuts = function (id) {
                @tekgenesis.view.client.SuiGenerisClientApi::shortcuts(Ljava/lang/String;)(id);
            };

            // Logout.
            inject.logout = function (id) {
                @tekgenesis.view.client.SuiGenerisClientApi::logout(Ljava/lang/String;)(id);
            };

            // Fullscreen.
            inject.fullscreen = function (id) {
                @tekgenesis.view.client.SuiGenerisClientApi::fullscreen(Ljava/lang/String;)(id);
            };

            // Menu Toggle.
            inject.toggleMenu = function (id) {
                @tekgenesis.view.client.SuiGenerisClientApi::toggleMenu(Ljava/lang/String;)(id);
            };

            // User profile.
            inject.userProfile = function (id) {
                @tekgenesis.view.client.SuiGenerisClientApi::userProfile(Ljava/lang/String;)(id);
            };

            // User Name.
            inject.userName = function (id, configuration) {
                @tekgenesis.view.client.SuiGenerisClientApi::username(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(id, configuration);
            };

            inject.resolve();
        }
    }-*/;
    //J+

    private static void addBoxListener(@NotNull String boxId, @Nullable JavaScriptObject object) {
        if (object != null) BoxHandler.startListening(boxId, object.<JsFormBoxListener>cast());
    }

    private static void attach(@NotNull String boxId, @Nullable JavaScriptObject object) {
        final BoxConfiguration configuration = object != null ? object.<JsBoxConfiguration>cast() : DEFAULT;
        BoxHandler.attach(boxId, configuration);
    }

    private static void attachMenu(@NotNull String menuId, @Nullable JavaScriptObject object) {
        final MenuConfiguration configuration = object != null ? object.<JsMenuConfiguration>cast() : MenuConfiguration.DEFAULT;
        MenuBox.attach(menuId, configuration);
    }

    private static void changeOu(@NotNull String id) {
        OuChanger.attach(id);
    }

    //J-
    private static native boolean fullscreen() /*-{
        var doc = $wnd.document;
        if (doc.fullScreenElement && doc.fullScreenElement !== null || !doc.mozFullScreen && !doc.webkitIsFullScreen) {
            var e = doc.documentElement;
            if (e.requestFullScreen) e.requestFullScreen();
            else if (e.webkitRequestFullScreen) e.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
            else if (e.mozRequestFullScreen) e.mozRequestFullScreen();
            return true;
        } else {
            if (doc.cancelFullScreen) doc.cancelFullScreen();
            else if (doc.webkitCancelFullScreen) doc.webkitCancelFullScreen();
            else if (doc.mozCancelFullScreen) doc.mozCancelFullScreen();
            return false;
        }
    }-*/;
    //J+

    private static void feedback(@NotNull String id) {
        final Panel  li     = RootPanel.get(id);
        final Anchor anchor = anchor(" " + MSGS.sendFeedback());
        anchor.addStyleName(ALIGNED_ICONS);
        Icon.inWidget(anchor, IconType.COMMENTS_O.getClassName());
        anchor.addClickHandler(event -> Application.feedback());
        li.add(anchor);
    }

    private static void fullscreen(@NotNull String id) {
        final Panel  li     = RootPanel.get(id);
        final Anchor anchor = anchor();
        anchor.setTitle(MSGS.fullscreen());
        Icon.inWidget(anchor, IconType.EXPAND.getClassName());
        anchor.addClickHandler(event ->
                Icon.replaceInWidget(anchor, fullscreen() ? IconType.COMPRESS.getClassName() : IconType.EXPAND.getClassName()));
        li.add(anchor);
    }

    private static void logout(@NotNull String id) {
        final Panel  li     = RootPanel.get(id);
        final Anchor anchor = anchor(" " + MSGS.logout());
        anchor.addStyleName(ALIGNED_ICONS);
        Icon.inWidget(anchor, IconType.SIGN_OUT.getClassName());
        anchor.setHref(URLConstants.LOGOUT_URI);
        li.add(anchor);
    }

    private static void missingSuigenerisObject() {
        GWT.log(
            "Could not load sg forms because the 'suigeneris' object was not found, did you forget to include the '/public/sg/js/sg-forms.js' script?");
    }

    private static void removeBoxListener(@NotNull String boxId, @Nullable JavaScriptObject object) {
        if (object != null) BoxHandler.stopListening(boxId, object.<JsFormBoxListener>cast());
    }

    private static void shortcuts(@NotNull String id) {
        final Panel  li     = RootPanel.get(id);
        final Anchor anchor = anchor(" " + MSGS.keyboardShortcuts());
        anchor.addStyleName(ALIGNED_ICONS);
        Icon.inWidget(anchor, IconType.KEYBOARD_O.getClassName());
        anchor.addClickHandler(event -> {
            event.stopPropagation();
            Application.modal(HelpDialog.getInstance());
        });
        li.add(anchor);
    }

    private static void switchUser(@NotNull String id) {
        final Panel  li     = RootPanel.get(id);
        final Anchor anchor = anchor(" " + MSGS.switchUser());
        anchor.addStyleName(ALIGNED_ICONS);
        Icon.inWidget(anchor, IconType.EXCHANGE.getClassName());
        anchor.addClickHandler(event -> {
            event.stopPropagation();
            Application.modal(LoginDialog.show(true, null));
        });
        li.add(anchor);
    }

    private static void toggleMenu(@NotNull String id) {
        final Panel button = RootPanel.get(id);
        button.addStyleName("navbar-toggle");
        Icon.inWidget(button, "bars");
        // for (int i = 0; i < 3; i++) {
        // final InlineHTML span = span();
        // span.addStyleName("icon-bar");
        // button.add(span);
        // }

        button.addDomHandler(event -> {
                final Element parent  = button.getElement().getParentElement().getParentElement();
                final Element element = querySelector(parent, ".navbar-collapse.collapse");
                element.toggleClassName("in");
                event.stopPropagation();
            },
            ClickEvent.getType());
    }

    private static void username(@NotNull String id, @Nullable JavaScriptObject object) {
        final Panel                 widgets       = RootPanel.get(id);
        final UsernameConfiguration configuration = object != null ? object.<JsUsernameConfiguration>cast() : UsernameConfiguration.DEFAULT;

        final Option<InlineHTML> username = configuration.username() ? some(span()) : empty();
        username.ifPresent(name -> {
            name.addStyleName("username-span");
            widgets.add(name);
        });

        final Canvas c   = Canvas.createIfSupported();
        final Image  img = img();
        if (configuration.picture()) {
            widgets.add(c);
            widgets.add(img);
        }

        getInstance().addBootListener(bootResponse -> {
            final String name = bootResponse.getUsername();
            for (final InlineHTML span : username)
                span.setHTML(name);

            if (configuration.picture()) {
                if (bootResponse.hasDefaultImage() && c != null) {
                    CanvasAvatar.createFor(name).buildAt(c);
                    c.setTitle(name);
                    c.addStyleName(USER_IMAGE_STYLE);
                    c.setVisible(true);
                    img.setVisible(false);
                }
                else {
                    img.setUrl(bootResponse.getUserImage());
                    img.setTitle(name);
                    img.addStyleName(USER_IMAGE_STYLE);
                    img.setVisible(true);
                    if (c != null) c.setVisible(false);
                }
            }
        });

        widgets.add(new Icon(IconType.CARET_DOWN));
    }  // end method username

    private static void userProfile(@NotNull String id) {
        final Panel  li     = RootPanel.get(id);
        final Anchor anchor = anchor(" " + MSGS.userProfile());
        anchor.addStyleName(ALIGNED_ICONS);
        Icon.inWidget(anchor, IconType.USER.getClassName());
        anchor.setHref("#form/tekgenesis.authorization.UserProfileForm");
        li.add(anchor);
    }

    //~ Static Fields ................................................................................................................................

    private static final String USER_IMAGE_STYLE = "img-circle navbar-img";

    //~ Inner Classes ................................................................................................................................

    /**
     * JavaScripts BoxConfiguration wrapper.
     */
    private static class JsBoxConfiguration extends JavaScriptObject implements BoxConfiguration {
        protected JsBoxConfiguration() {}

        @Override public final native boolean isFocusOnLoad()  /*-{ return this.focusOnLoad != undefined && this.focusOnLoad; }-*/;

        @Override public final native boolean isHomeFormEnabled()  /*-{ return this.home != undefined && this.home; }-*/;

        @NotNull @Override public final native String getInitialFqn()  /*-{ return this.fqn || ""; }-*/;

        @NotNull @Override public final native String getInitialPk()  /*-{ return this.pk || ""; }-*/;

        @Override public final native boolean isFooterHidden()  /*-{ return this.hideFooter != undefined && this.hideFooter; }-*/;

        @Override public final native boolean isHeaderHidden()  /*-{ return this.hideHeader != undefined && this.hideHeader; }-*/;

        @Override public final native boolean isBoundedByHistory()  /*-{ return this.history != undefined && this.history; }-*/;
    }

    /**
     * JavaScripts FormBoxListener wrapper. Delegates only if methods are defined to prevent
     * exceptions.
     */
    private static class JsFormBoxListener extends JavaScriptObject implements FormBoxListener {
        protected JsFormBoxListener() {}

        public final native void onError(String msg)  /*-{ if(this.onError) this.onError(msg); }-*/;

        @Override public final void onError(ResponseError error) {
            onError(error.getMessage());
        }

        @Override public final native void onLoad(String url)  /*-{ if(this.onLoad) this.onLoad(url); }-*/;

        @Override public final native void onLoad(String fqn, String pk, String parameters)  /*-{ if(this.onLoad) this.onLoad(fqn, pk, parameters); }-*/;

        @Override public final native void onUnload()  /*-{ if(this.onUnload) this.onUnload(); }-*/;

        @Override public final native void onUpdate()  /*-{ if(this.onUpdate) this.onUpdate(); }-*/;
    }

    /**
     * JavaScripts MenuConfiguration wrapper.
     */
    private static class JsMenuConfiguration extends JavaScriptObject implements MenuConfiguration {
        protected JsMenuConfiguration() {}

        @Override public final native boolean hideRoot()  /*-{ return this.hideRoot != undefined && this.hideRoot; }-*/;

        @Override public final native String getFqn()  /*-{ return this.fqn; }-*/;

        @Override public final native int getMaxMenus()  /*-{ return this.maxMenus || 0; }-*/;

        @Override public final native String getPosition()  /*-{ return this.position; }-*/;
    }

    /**
     * JavaScripts UsernameConfiguration wrapper.
     */
    private static class JsUsernameConfiguration extends JavaScriptObject implements UsernameConfiguration {
        protected JsUsernameConfiguration() {}

        @Override public final native boolean picture()  /*-{ return this.picture != undefined && this.picture; }-*/;

        @Override public final native boolean username()  /*-{ return this.username != undefined && this.username; }-*/;
    }
}  // end class SuiGenerisClientApi
