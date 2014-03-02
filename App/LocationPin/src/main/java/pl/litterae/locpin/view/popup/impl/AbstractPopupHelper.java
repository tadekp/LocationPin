package pl.litterae.locpin.view.popup.impl;

import pl.litterae.locpin.common.KeyValues;
import pl.litterae.locpin.view.popup.Popup;

public abstract class AbstractPopupHelper implements Popup.Helper {
	private final int layoutID;
	private final KeyValues input = new KeyValues();
	private final KeyValues output = new KeyValues();

	protected AbstractPopupHelper(int layoutID) {
		this.layoutID = layoutID;
	}

	@Override
	public final void cleanup() {
		input.cleanup();
		output.cleanup();
	}

	@Override
	public final int getLayoutID() {
		return layoutID;
	}

	@Override
	public final KeyValues getInput() {
		return input;
	}

	@Override
	public final KeyValues getOutput() {
		return output;
	}
}
