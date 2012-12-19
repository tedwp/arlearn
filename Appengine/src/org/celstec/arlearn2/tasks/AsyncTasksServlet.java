package org.celstec.arlearn2.tasks;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.celstec.arlearn2.tasks.beans.GenericBean;


public class AsyncTasksServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(AsyncTasksServlet.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		try {
			BeanDeserialiser bd = new BeanDeserialiser(request);
			GenericBean gb = bd.deserialize();
			gb.run();
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
