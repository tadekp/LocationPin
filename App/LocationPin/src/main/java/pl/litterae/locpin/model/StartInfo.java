package pl.litterae.locpin.model;

public final class StartInfo {
	private final String imageUrl;
	//private final LatLng location;
	private final String text;

	public StartInfo(String imageUrl, String text) {
		this.imageUrl = imageUrl;
		this.text = text;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getText() {
		return text;
	}
}
