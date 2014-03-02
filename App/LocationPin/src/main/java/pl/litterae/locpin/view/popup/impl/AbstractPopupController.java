package pl.litterae.locpin.view.popup.impl;

import android.view.ViewGroup;
import android.widget.Button;

import pl.litterae.locpin.common.KeyValues;
import pl.litterae.locpin.view.popup.Popup;

public abstract class AbstractPopupController implements Popup.Controller {
	abstract protected void initWith(ViewGroup container, Button button1, Button button2, KeyValues input, KeyValues output);
	abstract protected boolean isModified();

	@Override
	public final void initWith(ViewGroup container, Button button1, Button button2, KeyValues.Operator initializer, KeyValues input, KeyValues output) {
		if (initializer != null) {
			initializer.operateWith(input);
		}
		initWith(container, button1, button2, input, output);
	}
}
