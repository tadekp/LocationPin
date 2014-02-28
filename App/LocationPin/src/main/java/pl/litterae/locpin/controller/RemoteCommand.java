package pl.litterae.locpin.controller;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import pl.litterae.locpin.common.ResultNotifier;

public abstract class RemoteCommand extends AbstractCommand {
	public static enum Error {
		NO_INTERNET_CONNECTION("No connection with the Internet."),
		INCOMPLETE_SERVER_START_INFO("The starting information from the server is currently incomplete."),
		SERVER_ERROR("Server error.");

		private final String message;

		private Error(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	private final String url;
	private final AsyncHttpClient client;

	abstract protected Object prepareSuccessfulResultFrom(Header[] headers, JSONObject json);

	protected RemoteCommand(Type type, String url) {
		super(type);
		this.url = url;
		client = new AsyncHttpClient();
	}

	@Override
	public final void executeWith(final ResultNotifier resultNotifier) {
		client.get(url, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseBody) {
				super.onSuccess(statusCode, headers, responseBody);
				if (resultNotifier != null) {
					JSONObject json = prepareResponseJson(responseBody);
					Object result = prepareSuccessfulResultFrom(headers, json);
					if (result instanceof Error) {
						resultNotifier.completedWithSuccess(false, result);
					} else {
						resultNotifier.completedWithSuccess(true, result);
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
				super.onFailure(statusCode, headers, responseBody, error);
				if (resultNotifier != null) {
					resultNotifier.completedWithSuccess(false, getErrorFrom(headers));
				}
			}
		});
	}

	private JSONObject prepareResponseJson(String responseBody) {
		try {
			return new JSONObject(responseBody);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Error getErrorFrom(Header[] headers) {
		//TODO: produce error from headers
		return Error.SERVER_ERROR;
	}
}