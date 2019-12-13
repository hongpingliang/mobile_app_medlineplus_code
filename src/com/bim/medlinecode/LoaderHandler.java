package com.bim.medlinecode;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bim.core.Util;

public class LoaderHandler extends DefaultHandler {

	private Result result;

	private boolean isTitle = false;
	private boolean isSubtitle = false;
	private boolean isAuthor = false;
	private boolean isName = false;
	private boolean isUri = false;
	private boolean isUpdated = false;
	private boolean isEntry = false;
	private boolean isLink = false;
	private boolean isSummary = false;

	private StringBuffer mSb;
	private String attributeLinkHref;

	public LoaderHandler() {
		result = new Result();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		mSb = new StringBuffer();
		String tag = Util.trim(localName);

		if ("title".equals(tag)) {
			isTitle = true;
		} else if ("subtitle".equals(tag)) {
			isSubtitle = true;
		} else if ("author".equals(tag)) {
			isAuthor = true;
		} else if ("name".equals(tag)) {
			isName = true;
		} else if ("uri".equals(tag)) {
			isUri = true;
		} else if ("updated ".equals(tag)) {
			isUpdated = true;
		} else if ("entry".equals(tag)) {
			isEntry = true;
		} else if ("link".equals(tag)) {
			isLink = true;
			if ( attributes != null) {
				attributeLinkHref = attributes.getValue("href");
			}
		} else if ("summary".equals(tag)) {
			isSummary = true;
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		String tag = Util.trim(localName);

		String value = null;
		if ( mSb != null ) {
			value = Util.trim(mSb.toString());
		}
		
		
		if ("title".equals(tag)) {
			if ( isEntry ) {
				result.setEntryTitle(value);
			} else {
				result.setTitle(value);
			}
			isTitle = false;
		} else if ("subtitle".equals(tag)) {
			result.setSubtitle(value);
			isSubtitle = false;
		} else if ("author".equals(tag)) {
			isAuthor = false;
		} else if ("name".equals(tag)) {
			if ( isAuthor ) {
				result.setAuthorName(value);
			}
			isName = false;
		} else if ("uri".equals(tag)) {
			if ( isAuthor ) {
				result.setAuthorUrl(value);
			}
			isUri = false;
		} else if ("updated ".equals(tag)) {
			if ( isEntry) {
				result.setUpdated(value);
			}
			isUpdated = false;
		} else if ("entry".equals(tag)) {
			isEntry = false;
		} else if ("link".equals(tag)) {
			result.setEntryUrl(attributeLinkHref);
			isLink = false;
		} else if ("summary".equals(tag)) {
			result.setSummary(value);
			isSummary = false;
		}		
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (isTitle || isSubtitle || isName || isUri || isUpdated || isLink
				|| isSummary) {
			mSb.append(new String(ch, start, length));
		}
	}

	public void startDocument() throws SAXException {
	}

	public void endDocument() throws SAXException {
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
}
