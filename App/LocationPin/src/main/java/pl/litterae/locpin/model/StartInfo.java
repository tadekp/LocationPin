package pl.litterae.locpin.model;

import com.google.android.gms.maps.model.LatLng;

public final class StartInfo {
	private final String imageUrl;
	private final LatLng position;
	private final String text;

	public StartInfo(String imageUrl, String text, String latitudeStr, String longitudeStr) {
		this.imageUrl = imageUrl;
		this.text = text;
		position = new LatLng(Double.parseDouble(latitudeStr), Double.parseDouble(longitudeStr));
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getText() {
		return text;
	}

	public LatLng getPosition() {
		return position;
	}
}
