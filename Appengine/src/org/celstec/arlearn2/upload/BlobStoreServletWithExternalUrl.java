package org.celstec.arlearn2.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.celstec.arlearn2.jdo.manager.FilePathManager;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class BlobStoreServletWithExternalUrl extends HttpServlet {
	private static final Logger log = Logger.getLogger(BlobStoreServletWithExternalUrl.class.getName());

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		        BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
		        blobstoreService.serve(blobKey, res);
		    }
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			Long runId = null;
			String account = null;
			String fileName = null;
			runId = Long.parseLong(req.getParameter("runId"));
			account = req.getParameter("account");
			fileName = req.getParameter("fileName");
			
			if (req.getParameter("withBlob") == null) {
				String uploadUrl = blobstoreService.createUploadUrl("/uploadServiceWithUrl?withBlob=true&runId="+runId+"&account="+account+"&fileName="+fileName);
				System.out.println(uploadUrl);
				res.getWriter().write(uploadUrl);
			} else {

				java.util.Map<java.lang.String,java.util.List<BlobKey>> blobs = blobstoreService.getUploads(req);
				for (String key: blobs.keySet()) {
					System.out.println(key);
					System.out.println(blobs.get(key).get(0));
					FilePathManager.addFile(runId, account, fileName, blobs.get(key).get(0));
				}
//				Map<String,BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
			}
			
			//
			
			
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
	

}
