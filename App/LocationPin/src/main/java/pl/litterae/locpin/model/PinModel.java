package pl.litterae.locpin.model;

import com.google.android.gms.maps.model.LatLng;

import pl.litterae.locpin.common.Cleanupable;

public final class PinModel implements Cleanupable {
	private static final PinModel instance = new PinModel();
	//mutable members
	private String text;
	private LatLng location;

	private PinModel() {
	}

	@Override
	public void cleanup() {
		text = null;
		location = null;
	}

	public static PinModel getInstance() {
		return instance;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}
}
