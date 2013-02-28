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
	private String account;

	public DeleteBlobs() {
		super();
	}
	
	public DeleteBlobs(String token, Long runId) {
		super(token);
		this.runId = runId;
	}
	
	public DeleteBlobs(String token, Long runId, String account) {
		super(token);
		this.runId = runId;
		this.account = account;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public void run() {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		for (Iterator<FilePathJDO> iterator = FilePathManager.getFilePathJDOs(pm, getAccount(), getRunId(), null).iterator(); iterator.hasNext();) {
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
