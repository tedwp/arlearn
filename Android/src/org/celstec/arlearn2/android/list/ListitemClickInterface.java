package org.celstec.arlearn2.android.list;

import android.view.View;
import android.widget.ListView;

public interface ListitemClickInterface {

//	public void onListItemClick(ListView l, View v, int position, long id);

	public void onListItemClick(View v, int position, GenericListRecord messageListRecord);
}
