package pl.litterae.locpin.controller;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import pl.litterae.locpin.model.Key;
import pl.litterae.locpin.model.StartInfo;

public final class StartCommand extends RemoteCommand {
	StartCommand(String url) {
		super(Type.START, url);
	}

	@Override
	protected Object prepareSuccessfulResultFrom(Header[] headers, JSONObject json) {
		try {
			StartInfo startInfo = new StartInfo(json.getString(Key.IMAGE.toString()), json.getString(Key.TEXT.toString()));
			return startInfo;
		} catch (JSONException e) {
			e.printStackTrace();
			return Error.INCOMPLETE_SERVER_START_INFO;
		}
	}
}
