package pl.litterae.locpin.model;

public enum Key {
	IMAGE,
	LOCATION,
	TEXT,
	LONGITUDE,
	LATITUDE;

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
