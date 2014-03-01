package pl.litterae.locpin.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.Header;
import org.json.JSONObject;

import pl.litterae.locpin.model.PinModel;

public final class AccessImageCommand extends RemoteCommand {
	private static final String[] CONTENT_TYPES = new String[] { "image/png", "image/jpeg" };

	public AccessImageCommand(String url) {
		super(Type.ACCESS_IMAGE, url, true);
	}

	@Override
	protected Object prepareSuccessfulResultFromJson(Header[] headers, JSONObject json) {
		return null;
	}

	@Override
	protected Object prepareSuccessfulResultFromBinaryData(Header[] headers, byte[] binaryData) {
		if (binaryData != null && binaryData.length > 0) {
			Bitmap bm = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
			return bm;
		}
		return null;
	}

	@Override
	protected String[] getAllowedContentTypes() {
		return CONTENT_TYPES;
	}

	@Override
	public void consumeResult(Object result) {
		Bitmap bitmap = (Bitmap) result;
		PinModel.getInstance().setBitmap(bitmap);
	}
}
