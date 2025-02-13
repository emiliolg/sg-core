
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.translate.speak;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.*;

import com.memetix.mst.language.Language;
import com.memetix.mst.language.SpokenDialect;
import com.memetix.mst.speak.Speak;
import com.memetix.mst.translate.Translate;

/**
 */
public class SpeakManager {

    //~ Constructors .................................................................................................................................

    private SpeakManager() {
        Speak.setClientId("hola");
        Speak.setClientSecret("GnEWqHjEhiepTKfSEg067FA09L9e6XlOEOQPI0T1YiY=");
    }

    //~ Methods ......................................................................................................................................

    /** translate to Language. */
    public static void translateAndSpeak(String text, SpokenDialect dialect) {
        try {
            final Language lang    = Language.fromString(dialect.toString().split("-")[0]);
            final String   trans   = Translate.execute(text, Language.ENGLISH, lang);
            final String   sWavUrl = Speak.execute(trans, dialect);
            // Now, makes an HTTP Connection to get the InputStream
            final URL               waveUrl = new URL(sWavUrl);
            final HttpURLConnection uc      = (HttpURLConnection) waveUrl.openConnection();
            playClip(uc.getInputStream());
        }
        catch (final Exception e) {
            try {
                final String            sWavUrl = Speak.execute("Wow massive error", dialect);
                final URL               waveUrl = new URL(sWavUrl);
                final HttpURLConnection uc      = (HttpURLConnection) waveUrl.openConnection();
                playClip(uc.getInputStream());
            }
            catch (final Exception e1) {
                // final InternetLostDialog dialog = new InternetLostDialog();
                // dialog.pack();
                // dialog.setLocationRelativeTo(null);
                // dialog.setVisible(true);
                // if (dialog.getResponse()) translateAndSpeak(text, dialect);
            }
        }
    }

    /** Get all SpokenLanguages as Strings. */
    @SuppressWarnings("NonThreadSafeLazyInitialization")
    public static String[] getAll() {
        if (allLanguagesAsStrings == null) {
            final ArrayList<String> all = new ArrayList<>();
            for (final SpokenDialect dialect : SpokenDialect.values())
                all.add(dialect.name());
            allLanguagesAsStrings = all.toArray(new String[all.size()]);
        }
        return allLanguagesAsStrings;
    }

    // Pass the input stream to the playClip method

    @SuppressWarnings("NestedTryStatement")
    private static void playClip(InputStream is)
        throws Exception
    {
        class AudioListener implements LineListener {
            @SuppressWarnings("BooleanVariableAlwaysNegated")
            private boolean done = false;

            public synchronized void update(LineEvent event) {
                final LineEvent.Type eventType = event.getType();
                if (eventType == LineEvent.Type.STOP || eventType == LineEvent.Type.CLOSE) {
                    done = true;
                    notifyAll();
                }
            }

            public synchronized void waitUntilDone()
                throws InterruptedException
            {
                while (!done)
                    wait();
            }
        }

        final AudioListener listener = new AudioListener();

        try(AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(is)) {
            final Clip clip = AudioSystem.getClip();
            clip.addLineListener(listener);
            clip.open(audioInputStream);
            try {
                clip.start();
                listener.waitUntilDone();
            }
            finally {
                clip.close();
            }
        }
    }  // end method playClip

    //~ Static Fields ................................................................................................................................

    private static String[] allLanguagesAsStrings = null;
}  // end class SpeakManager
