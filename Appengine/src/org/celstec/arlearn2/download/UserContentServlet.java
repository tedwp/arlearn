package org.celstec.arlearn2.download;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.celstec.arlearn2.jdo.manager.FilePathManager;
import org.celstec.arlearn2.upload.BlobStoreServlet;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class UserContentServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(BlobStoreServlet.class.getName());

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getPathInfo();
		System.out.println(path);
		String account = getFirstPath(path);
		path = "/"+getReminder(path);
		System.out.println(account);
		System.out.println(path);
		BlobKey bk = FilePathManager.getBlobKey(account, null, path);
		if (bk != null) {
			blobstoreService.serve(bk, resp);
		} else {
			resp.setStatus(404);
		}
	}
	
	private String getFirstPath(String path) {
		if (path == null)
			return null;
		if (path.startsWith("/"))
			return getFirstPath(path.substring(1));
		if (path.contains("/"))
			return path.substring(0, path.indexOf("/"));
		return path;
	}
	
	private String getReminder(String path) {
		if (path == null)
			return null;
		if (path.startsWith("/"))
			return getReminder(path.substring(1));
		if (path.contains("/"))
			return path.substring(path.indexOf("/") + 1);
		return null;
	}

}
