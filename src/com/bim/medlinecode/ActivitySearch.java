package com.bim.medlinecode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bim.core.ActivityBase;
import com.bim.core.Log;
import com.bim.core.Util;

public class ActivitySearch extends ActivityBase {

	private int category;
	private EditText mCode;
	private RadioGroup mTypeRadioGroup;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.search);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon);

		Intent intent = getIntent();
		if (intent != null) {
			category = intent.getIntExtra("category", 0);
		}
		if (category < 1) {
			Log.d("ActivitySearch: missing type");
			finish();
			return;
		}

		mCode = (EditText) findViewById(R.id.search_code_text);
		mTypeRadioGroup = (RadioGroup) findViewById(R.id.search_radio_group);

		RadioButton mSNOMED_CT = (RadioButton) findViewById(R.id.search_SNOMED_CT_radio);
		RadioButton mICD_9_CM = (RadioButton) findViewById(R.id.search_ICD_9_CM_radio);
		RadioButton mRXCUI = (RadioButton) findViewById(R.id.search_RXCUI_radio);
		RadioButton mNDC = (RadioButton) findViewById(R.id.search_NDC_radio);
		RadioButton mLOINC = (RadioButton) findViewById(R.id.search_LOINC_radio);

		TextView mHintSNOMED_CT = (TextView) findViewById(R.id.search_hint_SNOMED_CT);
		TextView mHintICD_9_CM = (TextView) findViewById(R.id.search_hint_ICD_9_CM);
		TextView mHintRXCUI = (TextView) findViewById(R.id.search_hint_RXCUI);
		TextView mHintNDC = (TextView) findViewById(R.id.search_hint_NDC);
		TextView mHintLOINC = (TextView) findViewById(R.id.search_hint_LOINC);
//		setSampleEvent(mHintSNOMED_CT, "195967001", R.id.search_SNOMED_CT_radio);
//		setSampleEvent(mHintICD_9_CM, "250.00", R.id.search_ICD_9_CM_radio);
//		setSampleEvent(mHintRXCUI, "849383", R.id.search_RXCUI_radio);
//		setSampleEvent(mHintNDC, "00456140501", R.id.search_NDC_radio);
//		setSampleEvent(mHintLOINC, "1668-3", R.id.search_LOINC_radio);
		
		switch (category) {
		case Setting.CATEGORY_Problem:
			mSNOMED_CT.setVisibility(View.VISIBLE);
			mICD_9_CM.setVisibility(View.VISIBLE);
			mTypeRadioGroup.check(R.id.search_ICD_9_CM_radio);
			mRXCUI.setVisibility(View.GONE);
			mNDC.setVisibility(View.GONE);
			mLOINC.setVisibility(View.GONE);
			
			mHintSNOMED_CT.setVisibility(View.VISIBLE);
			mHintICD_9_CM.setVisibility(View.VISIBLE);
			mHintRXCUI.setVisibility(View.GONE);
			mHintNDC.setVisibility(View.GONE);
			mHintLOINC.setVisibility(View.GONE);			
			
			setTitle(R.string.main_code_problem);
			break;
		case Setting.CATEGORY_Medication:
			mRXCUI.setVisibility(View.VISIBLE);
			mNDC.setVisibility(View.VISIBLE);
			mTypeRadioGroup.check(R.id.search_NDC_radio);
			mSNOMED_CT.setVisibility(View.GONE);
			mICD_9_CM.setVisibility(View.GONE);
			mLOINC.setVisibility(View.GONE);
			
			mHintRXCUI.setVisibility(View.VISIBLE);
			mHintNDC.setVisibility(View.VISIBLE);
			mHintSNOMED_CT.setVisibility(View.GONE);
			mHintICD_9_CM.setVisibility(View.GONE);
			mHintLOINC.setVisibility(View.GONE);
			
			setTitle(R.string.main_code_medication);
			break;
		case Setting.CATEGORY_Lab_test:
			mLOINC.setVisibility(View.VISIBLE);
			mTypeRadioGroup.check(R.id.search_LOINC_radio);
			mSNOMED_CT.setVisibility(View.GONE);
			mICD_9_CM.setVisibility(View.GONE);
			mRXCUI.setVisibility(View.GONE);
			mNDC.setVisibility(View.GONE);
			
			mHintLOINC.setVisibility(View.VISIBLE);
			mHintSNOMED_CT.setVisibility(View.GONE);
			mHintICD_9_CM.setVisibility(View.GONE);
			mHintRXCUI.setVisibility(View.GONE);
			mHintNDC.setVisibility(View.GONE);		
			
			setTitle(R.string.main_code_lab);
			break;

		default:
			break;
		}

		Button searchButton = (Button) findViewById(R.id.search_button);
		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				doSearch();
			}
		});
	}
	
	private void setSampleEvent(TextView mText, final String value, final int type) {
		mText.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mCode.setText(value);
				mTypeRadioGroup.check(type);
			}
		});
	}

	private void doSearch() {
		String code = mCode.getText().toString();
		if (Util.isNull(code)) {
			showMessage("Please enter the code");
			return;
		}

		String type = null;
		switch (mTypeRadioGroup.getCheckedRadioButtonId()) {
		case R.id.search_SNOMED_CT_radio:
			type = Setting.TYPE_Problem_SNOMED_CT;
			break;
		case R.id.search_ICD_9_CM_radio:
			type = Setting.TYPE_Problem_ICD_9_CM;
			break;
		case R.id.search_RXCUI_radio:
			type = Setting.TYPE_Medication_RXCUI;
			break;
		case R.id.search_NDC_radio:
			type = Setting.TYPE_Medication_NDC;
			break;
		case R.id.search_LOINC_radio:
			type = Setting.TYPE_Lab_Test_LOINC;
			break;
		}
		if (Util.isNull(type)) {
			showMessage("Please select a type");
			return;
		}

		ActivityResult.startActivity(this, category, code, type, 0);

	}

	public static void startActivity(Activity activity, int category,
			int requestCode) {
		Intent intent = new Intent(activity, ActivitySearch.class);
		intent.putExtra("category", category);
		activity.startActivityForResult(intent, requestCode);
	}
}