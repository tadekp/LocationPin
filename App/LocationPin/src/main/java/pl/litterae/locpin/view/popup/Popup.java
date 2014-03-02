package pl.litterae.locpin.view.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import pl.litterae.locpin.R;
import pl.litterae.locpin.common.Cleanupable;
import pl.litterae.locpin.common.KeyValues;

public final class Popup implements Cleanupable {
	public static enum ButtonsType {
		OK("OK", null),
		CLOSE("Close", null),
		OK_CANCEL("OK", "Cancel"),
		YES_NO("YES", "NO");

		private final String label1;
		private final String label2;

		private ButtonsType(String label1, String label2) {
			this.label1 = label1;
			this.label2 = label2;
		}

		public boolean isDouble() {
			switch (this) {
				case OK_CANCEL:
				case YES_NO:
					return true;
				default:
					return false;
			}
		}
	}

	public static interface Helper extends Cleanupable {
		int getLayoutID();
		KeyValues getInput();
		KeyValues getOutput();
	}

	public static interface Controller {
		String getTitle();
		void initWith(ViewGroup container, Button button1, Button button2, KeyValues.Operator initializer, KeyValues input, KeyValues output);
	}

	private final Context context;
	private final Helper helper;

	public Popup(Context context, Helper helper) {
		this.context = context;
		this.helper = helper;
	}

	@Override
	public void cleanup() {
	}

	public void show(ButtonsType buttonsType, Controller controller, KeyValues.Operator initializer, final KeyValues.Operator positiveOperator) {
		final Dialog dlg = new Dialog(context);
		dlg.setContentView(R.layout.generic_popup);
		FrameLayout container = (FrameLayout) dlg.findViewById(R.id.popup_content_frame);
		LayoutInflater li = LayoutInflater.from(dlg.getContext());
		View content = li.inflate(helper.getLayoutID(), container, false);

		if (content != null) {
			container.addView(content);
		} else {
			//TODO: throw PopupException (or UIDefException)
		}

		final Handler handler = new Handler();
		Button button1;
		Button button2;
		if (buttonsType.isDouble()) {
			button1 = (Button) dlg.findViewById(R.id.popup_button21);
			button1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dlg.dismiss();
					if (positiveOperator != null) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								positiveOperator.operateWith(helper.getOutput());
							}
						});
					}
				}
			});

			button2 = (Button) dlg.findViewById(R.id.popup_button22);
			button2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dlg.dismiss();
				}
			});
			switch (buttonsType) {
				case OK_CANCEL:
					button1.setEnabled(false);
					break;
			}

			button1.setText(buttonsType.label1);
			button2.setText(buttonsType.label2);
			View twoButtons = dlg.findViewById(R.id.popup_two_buttons);
			twoButtons.setVisibility(View.VISIBLE);
		} else {
			button1 = (Button) dlg.findViewById(R.id.popup_button1);
			button1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dlg.dismiss();
					if (positiveOperator != null) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								positiveOperator.operateWith(helper.getOutput());
							}
						});
					}
				}
			});
			button2 = null;
			button1.setText(buttonsType.label1);
			button1.setVisibility(View.VISIBLE);
		}

		cleanup();
		dlg.setTitle(controller.getTitle());
		controller.initWith(container, button1, button2, initializer, helper.getInput(), helper.getOutput());

		dlg.show();
	}
}
