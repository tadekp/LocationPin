package pl.litterae.locpin.controller.impl;

import android.view.ViewGroup;
import android.widget.Button;

import pl.litterae.locpin.R;
import pl.litterae.locpin.common.KeyValues;
import pl.litterae.locpin.controller.UseCase;
import pl.litterae.locpin.model.CurrentLocationInfo;
import pl.litterae.locpin.view.component.CurrentLocationView;
import pl.litterae.locpin.view.popup.impl.AbstractPopupController;

public final class DistancePopupController extends AbstractPopupController {
	private final CurrentLocationInfo currentLocationInfo;

	public DistancePopupController(CurrentLocationInfo currentLocationInfo) {
		this.currentLocationInfo = currentLocationInfo;
	}

	@Override
	protected void initWith(ViewGroup container, Button button1, Button button2, KeyValues input, KeyValues output) {
		CurrentLocationView currentLocationView = (CurrentLocationView) container.findViewById(R.id.distance_info);
		currentLocationView.setInfo(currentLocationInfo);
	}

	@Override
	protected boolean isModified() {
		return false;
	}

	@Override
	public String getTitle() {
		return UseCase.LOCATE_AND_MEASURE.getLabel();
	}
}
