package pl.litterae.locpin.controller;

import android.location.Location;

import pl.litterae.locpin.common.KeyValues;
import pl.litterae.locpin.common.ResultNotifier;
import pl.litterae.locpin.controller.impl.DistancePopupController;
import pl.litterae.locpin.controller.impl.DistancePopupHelper;
import pl.litterae.locpin.model.CurrentLocationInfo;
import pl.litterae.locpin.model.PinModel;
import pl.litterae.locpin.view.popup.Popup;

public final class ShowDistanceCommand extends AbstractCommand {
	private final Location currentLocation;

	ShowDistanceCommand(Location currentLocation) {
		super(Type.SHOW_DISTANCE);
		this.currentLocation = currentLocation;
	}

	@Override
	public void executeWith(ResultNotifier resultNotifier) {
		Location pinLocation = PinModel.getInstance().getLocation();
		if (resultNotifier != null) {
			resultNotifier.completedWithSuccess(true, new CurrentLocationInfo(currentLocation, pinLocation));
		}
	}

	@Override
	public String getErrorMessageWith(Object result) {
		return null;
	}

	@Override
	public void consumeResult(Object result) {
		CurrentLocationInfo currentLocationInfo = (CurrentLocationInfo) result;
		Popup.Helper helper = new DistancePopupHelper();
		Popup.Controller controller = new DistancePopupController(currentLocationInfo);
		Popup popup = new Popup(MenuController.getInstance().getCurrentContext(), helper);
		popup.show(Popup.ButtonsType.CLOSE, controller,
				new KeyValues.Operator() {
					@Override
					public void operateWith(KeyValues data) {
					}
				}, new KeyValues.Operator() {
					@Override
					public void operateWith(KeyValues data) {
					}
				});
	}
}
