
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.menu;

import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.model.KeyMap;
import tekgenesis.view.client.ui.base.HtmlList;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.shared.response.OrgUnitsResponse;
import tekgenesis.view.shared.response.Response;
import tekgenesis.view.shared.service.FormService;

import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.metadata.form.model.FormConstants.DROPDOWN_MENU;
import static tekgenesis.metadata.form.model.FormConstants.DROPDOWN_OPEN_LEFT;
import static tekgenesis.metadata.form.model.FormConstants.DROPDOWN_SUBMENU;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.ul;

/**
 * Renders an Org Unit changer, based on the Org Units that the User has (or nothing if it has one).
 */
public class OuChanger {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Panel panel;

    //~ Constructors .................................................................................................................................

    private OuChanger(@NotNull final Panel panel) {
        this.panel = panel;

        FormService.App.getInstance().getOrgUnits(new OuChangerCallback());
    }

    //~ Methods ......................................................................................................................................

    /** Fetches and attaches the Org Unit changer to the dom. */
    public static void attach(@NotNull final String changerId) {
        new OuChanger(RootPanel.get(changerId));
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(OuChanger.class);

    //~ Inner Classes ................................................................................................................................

    private class OuChangerCallback implements AsyncCallback<Response<OrgUnitsResponse>> {
        @Override public void onFailure(@NotNull Throwable caught) {
            logger.info("Error retrieving Organizational Units.");
        }

        @Override public void onSuccess(Response<OrgUnitsResponse> result) {
            final OrgUnitsResponse response = result.getResponse();

            final KeyMap orgUnits = response.getOrgUnits();

            if (!orgUnits.isEmpty()) {
                panel.addStyleName(DROPDOWN_SUBMENU);
                panel.addStyleName(DROPDOWN_OPEN_LEFT);

                final Anchor orgUnitsMenu = anchor(" " + MSGS.organizationalUnits());
                Icon.inWidget(orgUnitsMenu, IconType.SITEMAP.getClassName());
                panel.add(orgUnitsMenu);

                final HtmlList.Unordered ul = ul();
                ul.setStyleName(DROPDOWN_MENU);

                for (final Map.Entry<Object, String> ouEntry : orgUnits) {
                    final String ouKey  = (String) ouEntry.getKey();
                    final Anchor anchor = anchor(ouEntry.getValue());
                    anchor.setTitle(ouKey);
                    anchor.addClickHandler(event ->
                            FormService.App.getInstance().setOrgUnit(ouKey, new AsyncCallback<Void>() {
                                    // Upon a successful change we must reload.
                                    @Override public void onSuccess(Void v) {
                                        Window.Location.reload();
                                    }

                                    @Override public void onFailure(Throwable caught) {
                                        logger.warning("Cannot set Organizational Unit: '" + ouKey + "'", caught);
                                    }
                                }));

                    final HtmlList.Item li = HtmlWidgetFactory.li();
                    if (ouKey.equals(response.getCurrentOrgUnitKey())) li.setStyleName(FormConstants.ACTIVE_STYLE);

                    li.add(anchor);
                    ul.add(li);
                }

                panel.add(ul);
            }
        }  // end method onSuccess
    }  // end class OuChangerCallback
}  // end class OuChanger
