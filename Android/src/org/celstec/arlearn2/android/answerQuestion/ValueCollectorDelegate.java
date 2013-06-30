package org.celstec.arlearn2.android.answerQuestion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.ResponseDelegator;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.beans.run.Response;

public class ValueCollectorDelegate extends DataCollectorDelegate {
	private String message;
	public ValueCollectorDelegate(final NarratorItemActivity ctx, long runId, String account, String message) {
		super(ctx, runId, account);
		this.message = message;
		ImageView noteView = (ImageView) ctx.findViewById(R.id.value_button);
		noteView.setImageDrawable(ctx.getResources().getDrawable(R.drawable.dc_calculator_128));
		noteView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dcButtonClick();

			}
		});
	}

	@Override
	protected void dcButtonClick() {
		final EditText input = new EditText(ctx);
		input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
		alertDialogBuilder.setTitle(message);
		alertDialogBuilder.setCancelable(false).setPositiveButton("Send", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, close
				// current activity
				publishResponse(input.getText().toString());

			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				dialog.cancel();
			}
		});

		alertDialogBuilder.setView(input);

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private void publishResponse(String text) {
		Response r = createTextResponse(text);
		ResponseDelegator.getInstance().publishResponse(ctx, r);
	}

	@Override
	protected String getMimeType() {
		return null;
	}
}
