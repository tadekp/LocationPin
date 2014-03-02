package pl.litterae.locpin.controller;

import android.location.Location;

import pl.litterae.locpin.common.ResultNotifier;
import pl.litterae.locpin.model.PinModel;

public final class LocateMeCommand extends AbstractCommand {
	public static enum Error {
		PIN_NON_STARTED("The START command should be executed first."),
		GPS_NOT_ALLOWED("GPS location not allowed.");

		private final String message;

		private Error(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	LocateMeCommand() {
		super(Type.LOCATE_ME);
	}

	@Override
	public void executeWith(ResultNotifier resultNotifier) {
		if (PinModel.getInstance().isComplete()) {
			CurrentLocationController controller = CurrentLocationController.getInstance();
			Location location = controller.readLocation();
			if (location != null) {
				if (resultNotifier != null) {
					resultNotifier.completedWithSuccess(true, location);
				}
			} else {
				if (resultNotifier != null) {
					resultNotifier.completedWithSuccess(false, Error.GPS_NOT_ALLOWED);
				}
			}
		} else {
			if (resultNotifier != null) {
				resultNotifier.completedWithSuccess(false, Error.PIN_NON_STARTED);
			}
		}
	}

	@Override
	public Type getNextCommandType() {
		return Type.SHOW_DISTANCE;
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

	@Override
	public void consumeResult(Object result) {
		CurrentLocationController.getInstance().broadcastObtainedLocation((Location) result);
	}
}
