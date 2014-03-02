package pl.litterae.locpin.view.popup;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pl.litterae.locpin.R;
import pl.litterae.locpin.common.Cleanupable;
import pl.litterae.locpin.common.KeyValues;

public final class InfoPopup implements Cleanupable {
	private static InfoPopup instance = new InfoPopup();
	private Popup popup;
	private TextView textView;

	private InfoPopup() {
	}

	@Override
	public void cleanup() {
		if (popup != null) {
			popup.cleanup();
			popup = null;
		}
		textView = null;
	}

	public static void show(String message, Context context, Runnable closed) {
		instance.showPopup(message, context, closed);
	}

	public static void show(String message, Context context) {
		show(message, context, null);
	}

	public static void show(int messageId, Context context, Runnable closed) {
		instance.showPopup(context.getString(messageId), context, closed);
	}

	public static void show(int messageId, Context context) {
		show(messageId, context, null);
	}

	private void showPopup(final String message, final Context context, final Runnable closed) {
		cleanup();
		popup = new Popup(context, new Popup.Helper() {
			private final KeyValues input = new KeyValues();

			@Override
			public int getLayoutID() {
				return R.layout.popup_info;
			}

			@Override
			public KeyValues getInput() {
				return input;
			}

			@Override
			public KeyValues getOutput() {
				return null;
			}

			@Override
			public void cleanup() {
				input.cleanup();
			}
		});
		popup.show(Popup.ButtonsType.OK, new Popup.Controller() {
					@Override
					public String getTitle() {
						return context.getString(R.string.app_name);
					}

					@Override
					public void initWith(ViewGroup container, Button button1, Button button2, KeyValues.Operator initializer, KeyValues input, KeyValues output) {
						textView = (TextView) container.findViewById(R.id.popup_info_message);
						textView.setText(message);
						button1.setEnabled(true);
					}
				}, new KeyValues.Operator() {
					@Override
					public void operateWith(KeyValues data) {
					}
				}, new KeyValues.Operator() {
					@Override
					public void operateWith(KeyValues data) {
						if (closed != null) {
							closed.run();
						}
					}
				}
		);
	}
}
