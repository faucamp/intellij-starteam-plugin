/*
 * Copyright Notice
 * ================
 * This file contains proprietary information of Discovery Health.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2013
 */

package com.intellij.vcs.starteam;

import com.intellij.openapi.vcs.RepositoryLocation;
import com.intellij.openapi.vcs.VcsException;


/**
 * Starteam repository location
 *
 * @author Francois Aucamp
 */
public class StarteamRepositoryLocation implements RepositoryLocation {

    private String myURL;

    public StarteamRepositoryLocation(String URL) {
        this.myURL = URL;
    }

    @Override
    public String toPresentableString() {
        return myURL;
    }

    @Override
    public String toString() {
        return myURL;
    }

    @Override
    public String getKey() {
        return myURL;
    }

    @Override
    public void onBeforeBatch() throws VcsException {
    }

    @Override
    public void onAfterBatch() {
    }


}
