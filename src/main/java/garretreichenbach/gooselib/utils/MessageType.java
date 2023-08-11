package garretreichenbach.gooselib.utils;

/**
 * MessageType provides a list of message types for use in the Logger class.
 *
 * @author Garret Reichenbach
 */
public enum MessageType {
	DEBUG("[DEBUG]: "),
	INFO("[INFO]: "),
	WARNING("[WARNING]: "),
	ERROR("[ERROR]: "),
	CRITICAL("[CRITICAL]: ");

	public final String prefix;

	MessageType(String prefix) {
		this.prefix = prefix;
	}
}
