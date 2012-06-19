package org.celstec.arlearn2.android.asynctasks;

import org.celstec.arlearn2.android.activities.IGeneralActivity;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.client.RunClient;

@Deprecated
public class CheckGameConfig extends RestInvocation {

		public CheckGameConfig(IGeneralActivity activity) {
			super(activity);
		}
		
		protected Void doInBackground(Object... params) {
			PropertiesAdapter pa = callingActivity.getMenuHandler().getPropertiesAdapter();
			RunClient rc = RunClient.getRunClient();
			Config c = rc.getConfig(pa.getFusionAuthToken(), pa.getCurrentRunId());
			if (c != null) processConfig(c, pa);
			return null;
		}

		private void processConfig(Config c, PropertiesAdapter pa) {
			if (c.getScoring() != null) pa.setScoringEnabled(c.getScoring());
//			if (c.getMapAvailable() != null) pa.setMapAvailable(c.getMapAvailable());
			
		}
}
