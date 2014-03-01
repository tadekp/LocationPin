package pl.litterae.locpin.controller;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import pl.litterae.locpin.model.Key;
import pl.litterae.locpin.model.PinModel;
import pl.litterae.locpin.model.StartInfo;

public final class StartCommand extends RemoteCommand {
	StartCommand(String url) {
		super(Type.START, url, false);
	}

	@Override
	protected Object prepareSuccessfulResultFromJson(Header[] headers, JSONObject json) {
		try {
			String imageUrl = json.getString(Key.IMAGE.toString());
			String text = json.getString(Key.TEXT.toString());
			JSONObject locationJson = json.getJSONObject(Key.LOCATION.toString());
			String latitudeStr = locationJson.getString(Key.LATITUDE.toString());
			String longitudeStr = locationJson.getString(Key.LONGITUDE.toString());
			StartInfo startInfo = new StartInfo(imageUrl, text, latitudeStr, longitudeStr);
			return startInfo;
		} catch (JSONException e) {
			e.printStackTrace();
			return Error.INCOMPLETE_SERVER_START_INFO;
		}
	}

	@Override
	protected Object prepareSuccessfulResultFromBinaryData(Header[] headers, byte[] binaryData) {
		return null;
	}

	@Override
	public Type getNextCommandType() {
		return Type.ACCESS_IMAGE;
	}

	@Override
	public void consumeResult(Object result) {
		StartInfo startInfo = (StartInfo) result;
		PinModel.getInstance().setText(startInfo.getText());
	}
}
