package org.intermine.webservice.server;

/*
 * Copyright (C) 2002-2022 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

/**
 * @author Jakub Kulaviak
 **/
public abstract class WebServiceConstants
{
    private WebServiceConstants() {
    }


    /**
     * Error message returned in case of unexpected service failure.
     */
    public static final String SERVICE_FAILED_MSG = "Service failed. Please contact support.";

    /**
     * Name of module of web services.
     * All web services relative urls start with following prefix.
     */
    public static final String MODULE_NAME = "service";
}
