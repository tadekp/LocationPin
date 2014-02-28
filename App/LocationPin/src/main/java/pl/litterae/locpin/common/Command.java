package pl.litterae.locpin.common;

public interface Command extends Cleanupable {
	static enum Type {
		START,
		GET_IMAGE;
	}

	Type getType();
	void executeWith(ResultNotifier resultNotifier);
}
