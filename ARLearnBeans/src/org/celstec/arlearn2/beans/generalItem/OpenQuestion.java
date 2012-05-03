package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;

public class OpenQuestion extends Bean {

	private boolean withAudio;
	private boolean withPicture;
	public boolean isWithAudio() {
		return withAudio;
	}
	public void setWithAudio(boolean withAudio) {
		this.withAudio = withAudio;
	}
	public boolean isWithPicture() {
		return withPicture;
	}
	public void setWithPicture(boolean withPicture) {
		this.withPicture = withPicture;
	}
	
	
}
