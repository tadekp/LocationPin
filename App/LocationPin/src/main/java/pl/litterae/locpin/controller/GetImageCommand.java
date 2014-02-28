package pl.litterae.locpin.controller;

import org.apache.http.Header;
import org.json.JSONObject;

public final class GetImageCommand extends RemoteCommand {
	public GetImageCommand(String url) {
		super(Type.GET_IMAGE, url);
	}

	@Override
	protected Object prepareSuccessfulResultFrom(Header[] headers, JSONObject json) {
		return null;
	}
}
