package pl.litterae.locpin.model;

import android.location.Location;

public final class CurrentLocationInfo {
	private final Location location;
	private final Location pinLocation;
	private final double distance;

	public CurrentLocationInfo(Location location, Location pinLocation) {
		this.location = location;
		this.pinLocation = pinLocation;
		distance = (location != null && pinLocation != null) ? location.distanceTo(pinLocation) : 0;
	}

	public Location getLocation() {
		return location;
	}

	public Location getPinLocation() {
		return pinLocation;
	}

	public double getDistance() {
		return distance;
	}
}
