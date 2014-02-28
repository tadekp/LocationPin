package pl.litterae.locpin.model;

public enum ServerUrl {
	START("https://dl.dropboxusercontent.com/u/6556265/test.json");

	private final String url;

	private ServerUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
