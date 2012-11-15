package org.celstec.arlearn2.upload;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.celstec.arlearn2.jdo.manager.FilePathManager;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class UploadUserContentServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(BlobStoreServletWithExternalUrl.class.getName());

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String account = req.getParameter("account");
		String path = req.getPathInfo();
		System.out.println(path);
		System.out.println(account);
		if (account != null) {
			String uploadUrl = blobstoreService.createUploadUrl("/uploadUserContent" + path + "?account=" + account);
			String page = "<body>";
			page += "<form action=\"" + uploadUrl + "\" method=\"post\" enctype=\"multipart/form-data\">";
			page += "<input type=\"file\" name=\"myFile\">";
			page += "<input type=\"submit\" value=\"Submit\">";
			page += "</form></body>";
			res.getWriter().write(page);
		}

		// http://streetlearn.appspot.com//uploadService/623053/arlearn1/recording52396.amr
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("in upload");
		System.out.println(req.getParameter("account"));
		System.out.println(req.getPathInfo());
		java.util.Map<java.lang.String,java.util.List<BlobKey>> blobs = blobstoreService.getUploads(req);
		for (String key: blobs.keySet()) {
			
			FilePathManager.addFile(null, req.getParameter("account"), req.getPathInfo(), blobs.get(key).get(0));
		}
		// try {
		// Long runId = null;
		// String account = null;
		// String fileName = null;
		// runId = Long.parseLong(req.getParameter("runId"));
		// account = req.getParameter("account");
		// fileName = req.getParameter("fileName");
		// if (req.getParameter("withBlob") == null) {
		// String uploadUrl =
		// blobstoreService.createUploadUrl("/uploadServiceWithUrl?withBlob=true&runId="+runId+"&account="+account+"&fileName="+fileName);
		// res.getWriter().write(uploadUrl);
		// } else {
		//
		// java.util.Map<java.lang.String,java.util.List<BlobKey>> blobs =
		// blobstoreService.getUploads(req);
		// for (String key: blobs.keySet()) {
		// FilePathManager.addFile(runId, account, fileName,
		// blobs.get(key).get(0));
		// }
		// }
		//
		// } catch (Exception ex) {
		// throw new ServletException(ex);
		// }
	}
}