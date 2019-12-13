package com.bim.medlinecode;

import java.util.Locale;

import com.bim.core.SettingBase;

public class Setting extends SettingBase {
	public static final String URL_BASE  = "http://apps.nlm.nih.gov/medlineplus/services/mpconnect_service.cfm";

	public static final int CATEGORY_Problem  = 1;
	public static final int CATEGORY_Medication  = 2;
	public static final int CATEGORY_Lab_test  = 3;

	public static final String TYPE_Problem_SNOMED_CT  = "2.16.840.1.113883.6.96";
	public static final String TYPE_Problem_ICD_9_CM = "2.16.840.1.113883.6.103";
	public static final String TYPE_Medication_RXCUI  = "2.16.840.1.113883.6.88";
	public static final String TYPE_Medication_NDC  = "2.16.840.1.113883.6.69";
	public static final String TYPE_Lab_Test_LOINC  = "2.16.840.1.113883.6.1";
	
	public static final String KEY_Disclaimer = "pref_Disclaimer";	
	
	public static String getURL(String type, String code) {
		String t = URL_BASE + "?mainSearchCriteria.v.cs=" + type;
		t += "&mainSearchCriteria.v.c=" + code;
		
		try {
			if ( Locale.getDefault().getLanguage().equalsIgnoreCase("sp") ) {
				t += "informationRecipient.languageCode.c=sp";
			}
		} catch (Exception e) {
		}
		return t;
	}
	
}
