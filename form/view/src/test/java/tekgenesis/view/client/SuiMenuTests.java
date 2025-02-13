
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.view.client.menu.LeftMenu;
import tekgenesis.view.client.menu.MenuBox;
import tekgenesis.view.client.menu.MenuConfiguration;
import tekgenesis.view.client.menu.RecentPopup;
import tekgenesis.view.client.menu.SuggestPopup;
import tekgenesis.view.client.suggest.KeySuggestOracle;

import static tekgenesis.common.core.Constants.SHOWCASE_MODELS;
import static tekgenesis.view.client.InitTestContextService.App.getInstance;

/**
 * Client side testing of Menu Metamodel.
 *
 * <p>To run them from Intellij IDEA:</p>
 *
 * <p>1. Go to Module Settings, locate the forms web facet and delete the web.xml (NOT DELETE IT
 * FROM DISK, PLEASE!) 2. Create a configuration to run it and put the following VM Options: 3.
 * -Xmx1024M -Dgwt.args="-sourceLevel 1.8 -localWorkers 3 -draftCompile -logLevel ERROR -devMode"
 * </p>
 */
@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class SuiMenuTests extends BaseClientTest {

    //~ Methods ......................................................................................................................................

    @Test public void testMenuBoxDefault() {
        testMenu(new MenuConfiguration.Default());
    }

    @Test public void testTopMenuBox() {
        final MenuConfiguration configuration = new MenuConfiguration() {
                @Override public boolean hideRoot() {
                    return false;
                }
                @Override public String getFqn() {
                    return "";
                }
                @Override public int getMaxMenus() {
                    return 0;
                }
                @Override public String getPosition() {
                    return "top";
                }
            };

        testMenu(configuration);
    }

    @NotNull @Override public String[] getProjectPaths() {
        return new String[] { SHOWCASE_MODELS };
    }

    private void testMenu(final MenuConfiguration configuration) {
        getInstance().initTestContext(getProjectPaths(), getName(), new AsyncCallback<Void>() {
                @Override public void onFailure(Throwable caught) {
                    fail("Test failed reaching server! " + caught.getMessage());
                }

                @Override
                @SuppressWarnings("Convert2Lambda")
                // BE CAREFUL! GWT compilation breaks if this inner class is replaced by a lambda expression.
                public void onSuccess(Void result) {
                    final Element panel = new SimplePanel().getElement();
                    panel.setId(MENU_DIV_ID);
                    DOM.appendChild(RootPanel.getBodyElement(), panel);

                    MenuBox.attach(MENU_DIV_ID, configuration, new MenuBox.MenuBoxListener() {
                            @Override public void onLoad() {
                                final List<MenuBox> menuBoxes = MenuBox.getMenuBoxes();
                                assertEquals(menuBoxes.size(), 1);

                                final MenuBox menuBox = menuBoxes.get(0);
                                assertTrue(!"left".equals(configuration.getPosition()) || menuBox.getBaseMenu() instanceof LeftMenu);

                                final Map<String, String> forms = RecentPopup.getRecentPopup().getForms();
                                assertFalse(forms.isEmpty());

                                final KeySuggestOracle suggestOracle = SuggestPopup.getSuggestPopup().getSuggestOracle();
                                assertFalse(suggestOracle.getOptions().isEmpty());

                                menuBox.detach();
                                finishTest();
                            }
                        });
                }
            });
        delayTestFinish(100);
    }

    //~ Static Fields ................................................................................................................................

    private static final String MENU_DIV_ID = "menu";
}  // end class SuiMenuTests
