package com.bim.medlinecode;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.Handler;

import com.bim.core.Log;

public class Loader implements Runnable {
	private ActivityResult activity;
	private String type;
	private String code;
	private Result result;

	private final Handler mLoadHandler = new Handler();
	final Runnable mLoadCallback = new Runnable() {
		public void run() {
			activity.onLoadReady(result);
		}
	};

	public Loader(ActivityResult activity, String type, String code) {
		this.activity = activity;
		this.type = type;
		this.code = code;
	}

	public void run() {
		String url = Setting.getURL(type, code);
		Log.d(url);
		try {
			LoaderHandler handler = new LoaderHandler();

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(handler);

			URL u = new URL(url);
			InputSource ip = new InputSource(u.openStream());
			xr.parse(ip);
			result = handler.getResult();
		} catch (Exception e) {
			Log.d(e);
		}

		mLoadHandler.post(mLoadCallback);
	}
}