package com.luanxu.custom.album;

import java.util.List;

/**
 * @author TUNGDX
 */

/**
 * Listener for select media item.
 *
 */
public interface MediaSelectedListener {
    public void onHasNoSelected();

    public void onHasSelected(List<MediaItem> mediaSelectedList);
}
