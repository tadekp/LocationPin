package pl.litterae.locpin.controller;

import org.apache.http.Header;
import org.json.JSONObject;

public final class AccessImageCommand extends RemoteCommand {
	private static final String[] CONTENT_TYPES = new String[] { "image/png", "image/jpeg" };

	public AccessImageCommand(String url) {
		super(Type.ACCESS_IMAGE, url, true);
	}

	@Override
	protected Object prepareSuccessfulResultFrom(Header[] headers, JSONObject json) {
		return null;
	}

	@Override
	protected String[] getAllowedContentTypes() {
		return CONTENT_TYPES;
	}
}
