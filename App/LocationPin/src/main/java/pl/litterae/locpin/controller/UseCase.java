package pl.litterae.locpin.controller;

public enum UseCase {
	START_WITH_SERVER("Pin from server"),
	LOCATE_AND_MEASURE("I am here");

	private final String label;

	private UseCase(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
