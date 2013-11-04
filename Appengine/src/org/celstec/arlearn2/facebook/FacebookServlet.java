package org.celstec.arlearn2.facebook;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import org.celstec.arlearn2.oauth.OauthFbWorker;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class FacebookServlet extends HttpServlet {
    private FacebookClient facebookClient;
    private static final Logger log = Logger.getLogger(FacebookServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Enumeration<String> names = request.getParameterNames();
        PrintWriter writer = response.getWriter();

        while (names.hasMoreElements()) {
            String name = names.nextElement();
            log.log(Level.SEVERE, "name  "+name+ " "+request.getParameter(name));
            writer.print("name  " + name + " " + request.getParameter(name));

        }
        writer.close();
        String code = request.getParameter("code");
        OauthFbWorker fbWorker = new OauthFbWorker();
        fbWorker.setCode(code);
        fbWorker.setResponse(response);
        fbWorker.exchangeCodeForAccessToken();
//        https://apps.facebook.com/122952504562527/?fb_source=bookmark_apps&ref=bookmarks&count=0&fb_bmpos=3_0
        writer.print("<script> top.location.href='https://apps.facebook.com/122952504562527'</script>");
        //resultDisplayRuns.html
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String signedRequest = (String) request.getParameter("signed_request");
        log.log(Level.SEVERE, "signedRequest "+signedRequest);

//        FacebookSignedRequest facebookSR = null;
//        try {
//            facebookSR = FacebookSignedRequest.getFacebookSignedRequest(signedRequest);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        String oauthToken = null;
//        String oauthToken = "CAACEdEose0cBAESRWoFIK3VlZB91DATZCU1nkHZBLUuXLCu33oXROL9S14f66foHlQSZCDV87zxvZAEG07tgerUx2epTZAwUtT5p9TxUbk9UEnfGZCLpXn2U56o7FM5w1ZCK9Xe7eMcsI8Alekv7bPkrgzVBwZBZAUgYDh2cQT0segkc6HVtWkrUVawooG6pzuwYUZD"; //signedRequest;

        log.log(Level.SEVERE, "getOauth_token "+oauthToken);

        PrintWriter writer = response.getWriter();
        if(oauthToken == null) {

//            response.setContentType("text/html");
//            String authURL = "https://www.facebook.com/dialog/oauth?client_id=" + "122952504562527" + "&redirect_uri=https://streetlearn.appspot.com/facebook/?login=true/&scope=";
//            writer.print("<script> top.location.href='"	+ authURL + "'</script>");
//            writer.close();
            for (Cookie cookie: request.getCookies()){
                writer.print("cookie "+ cookie.getName()+ " " + cookie.getValue());
                writer.close();
            }

        }else {

            facebookClient = new DefaultFacebookClient(oauthToken);
            User user = facebookClient.fetchObject("me", User.class);
            writer.print("<img src=\"https://graph.facebook.com/" + user.getId() + "/picture\"/> I am " + user.getName() +" - " + user.getId() + "");
//            Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);
//            writer.print("<table><tr><th>Photo</th><th>Name</th><th>Id</th></tr>");
//            for (List<User> myFriendsList : myFriends) {
//
//                for(User user: myFriendsList)
//                        writer.print("<tr><td><img src=\"https://graph.facebook.com/" + user.getId() + "/picture\"/></td><td>" + user.getName() +"</td><td>" + user.getId() + "</td></tr>");
//
//            }
//            writer.print("</table>");
            writer.close();

        }

//        } else {
//            writer.print("logged in");
//        }
//        writer.close();
    }

}
