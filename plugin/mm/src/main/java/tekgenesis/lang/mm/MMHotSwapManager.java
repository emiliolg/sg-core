
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.debugger.impl.DebuggerManagerAdapter;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.execution.ExecutionResult;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowId;
import com.intellij.util.containers.HashMap;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Constants;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.notNull;

/**
 * Metamodel hot swap manager.
 */
public class MMHotSwapManager extends AbstractProjectComponent {

    //~ Instance Fields ..............................................................................................................................

    private final DebuggerManagerEx manager;

    //~ Constructors .................................................................................................................................

    /** Metamodel hot swap manager constructor. */
    @SuppressWarnings("WeakerAccess")
    public MMHotSwapManager(@NotNull Project project, DebuggerManagerEx manager) {
        super(project);
        this.manager = manager;

        manager.addDebuggerManagerListener(new DebuggerManagerAdapter() {
                public void sessionRemoved(DebuggerSession session) {
                    EXECUTIONS.remove(session.getProcess().getExecutionResult());
                }
            });
    }

    //~ Methods ......................................................................................................................................

    /** Ping refresh for applicable debug sessions. */
    public void refreshSessions() {
        pingRefreshSessions(Constants.REFRESH_URI);
    }

    /** Register execution with given uri. */
    public void register(@NotNull ExecutionResult result, @NotNull String baseUri) {
        EXECUTIONS.put(result, baseUri);
    }

    @NotNull public String getComponentName() {
        return "MMHotSwapManager";
    }

    /** Ping refresh resource for applicable debug sessions. */
    void refreshSessions(@Nullable String resource) {
        pingRefreshSessions(Constants.REFRESH_URI + "/" + resource);
    }

    private NotificationGroup notifier() {
        return notNull(NotificationGroup.findRegisteredGroup(MM_HOT_SWAP), NotificationGroup.toolWindowGroup(MM_HOT_SWAP, ToolWindowId.DEBUG, true));
    }

    private void notify(@NotNull String message, @NotNull NotificationType type) {
        notifier().createNotification(message, type).notify(myProject);
    }

    private void pingRefreshSessions(final String refreshUri) {
        final List<DebuggerSession> sessions = Colls.filter(manager.getSessions(),
                s -> s != null && s.isAttached() && s.getProcess().canRedefineClasses())
                                               .into(new ArrayList<>());

        for (final DebuggerSession session : sessions) {
            final String baseUri = EXECUTIONS.get(session.getProcess().getExecutionResult());
            if (baseUri != null) {
                try {
                    // Notify server to refresh
                    final URI               uri          = new URI(baseUri + refreshUri);
                    final HttpURLConnection con          = (HttpURLConnection) uri.toURL().openConnection();
                    final int               responseCode = con.getResponseCode();

                    // Eventually log a message to IDE!
                    if (responseCode == HttpURLConnection.HTTP_OK) notify(readContent(con), NotificationType.INFORMATION);
                    else notify(format("Meta-models refresh failed: %d -> %s", responseCode, con.getResponseMessage()), NotificationType.ERROR);
                }
                catch (IOException | URISyntaxException e) {
                    notify(format("Meta-models refresh failed: %s", e.getMessage()), NotificationType.INFORMATION);
                }
            }
        }
    }

    @NotNull private String readContent(final HttpURLConnection con)
        throws IOException
    {
        final InputStream  content = (InputStream) con.getContent();
        final StringWriter writer  = new StringWriter();
        IOUtils.copy(content, writer, con.getContentEncoding());
        IOUtils.closeQuietly(content);
        return writer.toString();
    }

    //~ Methods ......................................................................................................................................

    /** Return project instance. */
    public static MMHotSwapManager getInstance(Project project) {
        return project.getComponent(MMHotSwapManager.class);
    }

    //~ Static Fields ................................................................................................................................

    private static final String MM_HOT_SWAP = "MMHotSwap";

    private static final Map<ExecutionResult, String> EXECUTIONS = new HashMap<>();
}  // end class MMHotSwapManager
