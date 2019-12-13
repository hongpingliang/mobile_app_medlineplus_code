package com.bim.medlinecode;

import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.bim.core.ActivityBase;
import com.bim.core.Log;
import com.bim.core.Util;

public class ActivityResult extends ActivityBase {
	private WebView mWebView;

	private int category;
	private String code;
	private String type;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.result);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon);

		Intent intent = getIntent();
		if (intent != null) {
			category = intent.getIntExtra("category", category);
			code = intent.getStringExtra("code");
			type = intent.getStringExtra("type");
		}

		mWebView = (WebView) findViewById(R.id.result_webview);
		// category = Setting.CATEGORY_Problem;
		// type = Setting.TYPE_Problem_SNOMED_CT;
		// code = "13645005";

		// type = Setting.TYPE_Problem_ICD_9_CM;
		// code = "493.22";

		// category = Setting.CATEGORY_Medication;
		// type = Setting.TYPE_Medication_NDC;
		// code = "00456140501";

		// category = Setting.CATEGORY_Lab_test;
		// type = Setting.TYPE_Lab_Test_LOINC;
		// code = "2472-9";

		if (category < 1 || Util.isNull(code) || Util.isNull(type)) {
			Log.d("ActivityResult: category, code, type ");
			finish();
			return;
		}

		int titleRes = 0;
		switch (category) {
		case Setting.CATEGORY_Problem:
			titleRes = R.string.main_code_problem;
			break;
		case Setting.CATEGORY_Medication:
			titleRes = R.string.main_code_medication;
			break;
		case Setting.CATEGORY_Lab_test:
			titleRes = R.string.main_code_lab;
			break;
		}
		if (titleRes > 0) {
			setTitle(getString(titleRes) + ": " + code);
		}
		load();
	}

	private void load() {
		Loader loader = new Loader(this, type, code);
		Thread thread = new Thread(loader);
		thread.start();
		showLoadingDialog();
	}

	public void onLoadReady(Result result) {
		closeDialog();
		if (result == null) {
			showMessage("Failed to find the data.");
			finish();
			return;
		}

		refreshUI(result);
	}

	private void refreshUI(Result result) {

		mWebView.getSettings().setJavaScriptEnabled(true);
		FrameLayout mContentView = (FrameLayout) getWindow().getDecorView()
				.findViewById(android.R.id.content);
		final View zoom = mWebView.getZoomControls();
		mContentView.addView(zoom, ZOOM_PARAMS);
		zoom.setVisibility(View.GONE);

		mWebView.setWebViewClient(new WebViewClient() {
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				setProgressBarIndeterminateVisibility(true);
			}

			public void onPageFinished(WebView view, String url) {
				setProgressBarIndeterminateVisibility(false);
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				setProgressBarIndeterminateVisibility(false);
			}
		});

		if (Util.isNull(result.getSummary())
				&& !Util.isNull(result.getEntryUrl())) {
			mWebView.loadUrl(result.getEntryUrl());
			return;
		}

		String data = "";
		data += "<html><head></head><body style=\"margin:0px;color:#666666;\">";
		data += "<div style=\"background-color:#6699FF;padding:5px 0px 5px 5px;\">";
		data += "<div style=\"background-color:white; padding-left:10px; padding-right:10px;\">";

		if (Util.isNull(result.getSummary()) ) {
			data += "<br/><center style=\"font-weight:bold;font-size:130%;\">";
			data += "No data. Please Check the code and select the right radio button.";
			data += "</center>";
		} else {
			data += "<br/><center style=\"font-weight:bold;font-size:130%;\">";
			if (Util.isNull(result.getEntryUrl())) {
				data += result.getEntryTitle();
			} else {
				data += "<a href=\"" + result.getEntryUrl() + "\">"
						+ result.getEntryTitle() + "</a>";
			}
			data += "</center>";
			data += result.getSummary();
		}
		
		
		data += "<br/><br/>";
		data += "</div>";
		data += "</div>";
		data += "</body></html>";

		mWebView.loadData(URLEncoder.encode(data).replaceAll("\\+", " "),
				"text/html", "utf8");
	}

	private static final FrameLayout.LayoutParams ZOOM_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);

	public static void startActivity(Activity activity, int category,
			String code, String type, int requestCode) {
		Intent intent = new Intent(activity, ActivityResult.class);
		intent.putExtra("category", category);
		intent.putExtra("code", code);
		intent.putExtra("type", type);

		activity.startActivityForResult(intent, requestCode);
	}

}