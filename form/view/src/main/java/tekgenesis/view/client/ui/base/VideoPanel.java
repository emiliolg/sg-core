
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Frame;

import org.jetbrains.annotations.NonNls;

import static tekgenesis.metadata.form.model.FormConstants.*;

/**
 * Frame to contain video.
 */
public class VideoPanel extends Frame {

    //~ Constructors .................................................................................................................................

    /** Video Panel. */
    public VideoPanel() {
        super();
        setStyleName(VIDEO_FRAME);
    }

    //~ Methods ......................................................................................................................................

    /** Set the video embedURL. */
    public void setVideoURL(String fullURL) {
        if (fullURL.contains(YOUTUBE_EMBED_SHORT) || fullURL.contains(VIMEO_EMBED_SHORT)) setUrl(fullURL);
        else {
            final RegExp      a    = RegExp.compile(
                    "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:[youtu\\.be\\/|youtube\\.com | vimeo.com]\\S*[^\\w\\-\\s])([\\w\\-]{4,15})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*",
                    "i");
            final MatchResult exec = a.exec(fullURL);
            if (exec != null) {
                final String id = exec.getGroup(1);
                setUrl(((id.length() == YOUTUBE_ID_LENGTH ? YOUTUBE_EMBED : VIMEO_EMBED) + id));
            }
        }
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String VIDEO_FRAME = "video-frame";
}
