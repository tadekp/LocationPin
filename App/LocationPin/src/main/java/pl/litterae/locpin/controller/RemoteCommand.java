package pl.litterae.locpin.controller;

import android.app.ProgressDialog;
import android.view.Menu;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import pl.litterae.locpin.common.ResultNotifier;
import pl.litterae.locpin.util.Internet;

public abstract class RemoteCommand extends AbstractCommand {
	private static final int ASYNC_HTTP_TIMEOUT = 20000;

	public static enum Error {
		NO_INTERNET_CONNECTION("No connection to the Internet."),
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
	private final boolean binary;
	private final AsyncHttpClient client;
	//mutable
	private ProgressDialog progressDialog;

	abstract protected Object prepareSuccessfulResultFromJson(Header[] headers, JSONObject json);
	abstract protected Object prepareSuccessfulResultFromBinaryData(Header[] headers, byte[] binaryData);

	protected RemoteCommand(Type type, String url, boolean binary) {
		super(type);
		this.url = url;
		this.binary = binary;
		client = new AsyncHttpClient();
	}

	@Override
	public final void executeWith(final ResultNotifier resultNotifier) {
		if (!Internet.Manager.getInstance().detectConnected(MenuController.getInstance().getCurrentContext())) {
			if (resultNotifier != null) {
				resultNotifier.completedWithSuccess(false, Error.NO_INTERNET_CONNECTION);
			}
			return;
		}

		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		MenuController menuController = MenuController.getInstance();
		progressDialog = ProgressDialog.show(menuController.getCurrentContext(), menuController.getAppTitle(), getType().getPerformingDescription());

		client.setTimeout(ASYNC_HTTP_TIMEOUT);
		if (binary) {
			String[] allowedContentTypes = getAllowedContentTypes();
			client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
					super.onSuccess(statusCode, headers, binaryData);
					progressDialog.dismiss();
					if (resultNotifier != null) {
						Object result = prepareSuccessfulResultFromBinaryData(headers, binaryData);
						completeWith(resultNotifier, result);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
					super.onFailure(statusCode, headers, binaryData, error);
					progressDialog.dismiss();
					if (resultNotifier != null) {
						resultNotifier.completedWithSuccess(false, getErrorFrom(headers));
					}
				}
			});
		} else {
			client.get(url, new TextHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, String responseBody) {
					super.onSuccess(statusCode, headers, responseBody);
					progressDialog.dismiss();
					if (resultNotifier != null) {
						JSONObject json = prepareResponseJson(responseBody);
						Object result = prepareSuccessfulResultFromJson(headers, json);
						completeWith(resultNotifier, result);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
					super.onFailure(statusCode, headers, responseBody, error);
					progressDialog.dismiss();
					if (resultNotifier != null) {
						resultNotifier.completedWithSuccess(false, getErrorFrom(headers));
					}
				}
			});
		}
	}

	@Override
	public String getErrorMessageWith(Object result) {
		if (result instanceof Error) {
			Error error = (Error) result;
			return error.getMessage();
		} else {
			return null;
		}
	}

	private void completeWith(ResultNotifier resultNotifier, Object result) {
		if (resultNotifier != null) {
			if (result instanceof Error) {
				resultNotifier.completedWithSuccess(false, result);
			} else {
				resultNotifier.completedWithSuccess(true, result);
			}
		}
	}

	protected String[] getAllowedContentTypes() {
		return null;
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
