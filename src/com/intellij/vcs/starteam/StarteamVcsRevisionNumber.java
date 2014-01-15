/*
 * Copyright Notice
 * ================
 * This file contains proprietary information of Discovery Health.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2013
 */

package com.intellij.vcs.starteam;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.starteam.Label;
import com.starteam.ViewMember;
import com.starteam.util.DotNotation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Starteam-specific revision number, that also provides dot-notation for
 * display purposes.
 *
 * @author Francois Aucamp
 */
public class StarteamVcsRevisionNumber extends VcsRevisionNumber.Int {

    private static final int ATTACHED_LABELS_CACHE_LIMIT = 50;
    private static final Map<Integer, Label[]> attachedLabels = new ConcurrentHashMap<Integer, Label[]>();

    private DotNotation dotNotation;
    private Label label = null;

    private static final Logger LOG = Logger.getInstance("#com.intellij.vcs.starteam.StarteamVcsRevisionNumber");

    public StarteamVcsRevisionNumber(ViewMember item) {
        super(item.getRevisionNumber() + 1);
        this.dotNotation = item.getDotNotation();

        Integer rootItemId = item.getRootObjectID();
        if (!attachedLabels.containsKey(rootItemId)) {
            if (attachedLabels.size() > ATTACHED_LABELS_CACHE_LIMIT) {
                // Remove an item from the cache
                Integer delKey = attachedLabels.keySet().iterator().next();
                attachedLabels.remove(delKey);
            }
            attachedLabels.put(rootItemId, item.getAttachedLabels());
        }
        // Check for build labels attached to this file/revision combo
        // (horrible, but Starteam's SDK does not have any nice way of doing this)
        for (Label checkLabel : attachedLabels.get(rootItemId)) {
            if (item.getModifiedTime().equals(checkLabel.getRevisionTime())) {
                label = checkLabel;
                break; // just get the first matching label for this revision
            }
        }
    }

    @Override public String asString() {
        if (label == null) {
            // Not a labeled revision
            return super.asString() + " <" + dotNotation.toString() + '>';
        } else {
            return super.asString() + " <" + dotNotation.toString() + "> [" + label.getName() + ']';
        }


    }

    public static void clearLabelsCache() {
        attachedLabels.clear();
    }
}
