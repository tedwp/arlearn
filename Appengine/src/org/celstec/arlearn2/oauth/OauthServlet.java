/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class OauthServlet  extends HttpServlet {

	private static final String FACEBOOK = "facebook";
	private static final String GOOGLE = "google";
	private static final String LINKEDIN = "linkedin";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		OauthWorker worker = null;
		if (req.getPathInfo().contains(FACEBOOK)) {
			worker = new OauthFbWorker();
		} else if (req.getPathInfo().contains(GOOGLE)) {
			worker = new OauthGoogleWorker();
		}else if (req.getPathInfo().contains(LINKEDIN)) {
			worker = new OauthLinkedInWorker();
		}
		if (worker != null) {
			String baseurl =  "http://" +req.getServerName();
			if (req.getServerPort() != 80) baseurl+=":"+req.getServerPort();
			worker.setBaseUrl(baseurl);
			worker.setCode(req.getParameter("code"));
			worker.setResponse(resp);
			worker.exchangeCodeForAccessToken();
		}
	}
	

}
