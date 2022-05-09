package org.intermine.webservice.server.oauth2;

/*
 * Copyright (C) 2002-2022 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import org.intermine.web.context.InterMineContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A Servlet for routing requests to the oauth2 AuthenticatorService.
 *
 * @author Daniela Butano
 */
public class AuthenticatorServlet extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        AuthenticatorService authenticatorService =
                new AuthenticatorService(InterMineContext.getInterMineAPI());
        authenticatorService.service(request, response);
    }
}
