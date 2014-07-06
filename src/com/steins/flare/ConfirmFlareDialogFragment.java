package com.steins.flare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class ConfirmFlareDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final WebView gif = (WebView) getActivity().getWindow().getDecorView()
				.getRootView().findViewById(R.id.webView1);
		

		String html = "<head><body><center><img src = \"file:///android_asset/loader.png\"></img></center></body></html> ";

		gif.loadDataWithBaseURL("file:///android_asset/loader.png", html,
				"text/html", "UTF-8", "");

		gif.setBackgroundColor(0x00000000);

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Warning: Misusing this tool will result in a ban.")
				.setPositiveButton("Send",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								GIFCreate(gif);
								
								((MainActivity) getActivity()).getSupportActionBar().setTitle("Uploading...");

								((MainActivity) getActivity()).setUpInternetFlare();
								
							}
						})
				.setNegativeButton("Don't send",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	public void GIFCreate(WebView gif) {

		String html = "<head><body><center><img src = \"file:///android_asset/loading_gif_new.gif\"></img></center></body></html> ";

		gif.loadDataWithBaseURL("file:///android_asset/loading_gif_new.gif",
				html, "text/html", "UTF-8", "");

		gif.setBackgroundColor(0x00000000);

		ImageView loader = (ImageView) getActivity().getWindow().getDecorView()
				.getRootView().findViewById(R.id.flareImage);

		loader.setVisibility(View.INVISIBLE);

		gif.setVisibility(View.VISIBLE);
		
	}
}