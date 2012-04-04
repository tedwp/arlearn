package org.celstec.arlearn2.tasks.beans;

import java.util.Iterator;

import javax.jdo.PersistenceManager;

import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.FilePathJDO;
import org.celstec.arlearn2.jdo.manager.FilePathManager;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class DeleteBlobs extends GenericBean {
	
	private Long runId;

	public DeleteBlobs() {
		super();
	}
	
	public DeleteBlobs(String token, Long runId) {
		super(token);
		this.runId = runId;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}
	

	@Override
	public void run() {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		for (Iterator<FilePathJDO> iterator = FilePathManager.getFilePathJDOs(pm, null, getRunId(), null).iterator(); iterator.hasNext();) {
			FilePathJDO fpjdo = (FilePathJDO) iterator.next();
			try {
				blobstoreService.delete(fpjdo.getBlobKey());
				pm.deletePersistent(fpjdo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	

}
