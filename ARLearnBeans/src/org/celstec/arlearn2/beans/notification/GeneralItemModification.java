package org.celstec.arlearn2.beans.notification;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

public class GeneralItemModification extends Bean{
	
	public final static int CREATED = 1;
	public final static int DELETED = 2;
	public final static int ALTERED = 3;
	
	private Integer modificationType;
	private Long runId;
	private GeneralItem generalItem;
	
	public Integer getModificationType() {
		return modificationType;
	}
	
	public void setModificationType(Integer modificationType) {
		this.modificationType = modificationType;
	}
	
	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public GeneralItem getGeneralItem() {
		return generalItem;
	}
	
	public void setGeneralItem(GeneralItem generalItem) {
		this.generalItem = generalItem;
	}
	
}
