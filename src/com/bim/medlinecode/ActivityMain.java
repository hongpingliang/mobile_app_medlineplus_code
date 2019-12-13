package com.bim.medlinecode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.bim.core.ActivityBase;

public class ActivityMain extends ActivityBase {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.main);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon);

		View mProblem = findViewById(R.id.main_problem_button);
		mProblem.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				ActivitySearch.startActivity(ActivityMain.this,
						Setting.CATEGORY_Problem, 0);
			}
		});

		View mMedication = findViewById(R.id.main_medication_button);
		mMedication.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				ActivitySearch.startActivity(ActivityMain.this,
						Setting.CATEGORY_Medication, 0);
			}
		});

		View mLab = findViewById(R.id.main_lab_button);
		mLab.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				ActivitySearch.startActivity(ActivityMain.this,
						Setting.CATEGORY_Lab_test, 0);
			}
		});
		
		if ( 1 != getPreferenceInt(Setting.KEY_Disclaimer)) {
			showDisclaimer();
		}		

	}
	
	private void showDisclaimer() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.disclaimer_title);
		alert.setIcon(R.drawable.app_icon);
		alert.setMessage(R.string.disclaimer_message);

		alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				savePreference(Setting.KEY_Disclaimer, 1);
				return;
			}
		});

		alert.setNegativeButton("Deny",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
		alert.show();
	}		

}