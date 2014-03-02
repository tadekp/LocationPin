package pl.litterae.locpin.common;

public interface Command extends Cleanupable {
	static enum Type {
		START("Obtaining data from the remote server..."),
		ACCESS_IMAGE("Downloading the pin image..."),
		LOCATE_ME("Trying to localize the device..."),
		SHOW_DISTANCE("Showing distance to the pin...");

		private final String performingDescription;

		private Type(String performingDescription) {
			this.performingDescription = performingDescription;
		}

		public String getPerformingDescription() {
			return performingDescription;
		}
	}

	Type getType();
	Type getNextCommandType();
	void executeWith(ResultNotifier resultNotifier);
	void consumeResult(Object result);
	String getErrorMessageWith(Object result);
}
