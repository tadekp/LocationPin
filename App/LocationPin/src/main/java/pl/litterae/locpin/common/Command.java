package pl.litterae.locpin.common;

public interface Command extends Cleanupable {
	static enum Type {
		START,
		ACCESS_IMAGE;
	}

	Type getType();
	Type getNextCommandType();
	void executeWith(ResultNotifier resultNotifier);
	void consumeResult(Object result);
}
