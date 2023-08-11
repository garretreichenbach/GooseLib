package garretreichenbach.gooselib.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The LogManager class provides utility methods for logging messages and exceptions.
 *
 * <p>It supports logging warning, error, and critical messages to a log file and the console.
 * The log files are stored in the "./logs" folder, with a maximum of 10 log files. When the
 * maximum number of log files is reached, the oldest log file is deleted.
 *
 * <p>The LogManager class provides the following methods:
 * <ul>
 *   <li> {@link #initialize()}: Initializes the LogManager by creating the log folder and log file.
 *   <li> {@link #logDebug(String)}: Logs a debug message.
 *   <li> {@link #logWarning(String, Exception)}: Logs a warning message with an optional exception.
 *   <li> {@link #logMessage(MessageType, String)}: Logs a message with a specified message type.
 *   <li> {@link #logException(String, Exception)}: Logs an exception with an optional message.
 *   <li> {@link #logCritical(String, Exception)}: Logs a critical exception with an optional message. Will force-stop the program after logging.
 *   <li> {@link #clearExtraLogs()}: Clears log files exceeding the maximum number of logs.
 * </ul>
 *
 * <p>Note: This class makes use of the following supporting classes:
 * <ul>
 *   <li> {@link DateUtils}: Provides utility methods for working with date and time.
 *   <li> {@link MessageType}: Defines the different types of log messages.
 * </ul>
 */
public class LogManager {

	public static final int MAX_LOGS = 10;
	private static FileWriter logWriter;

	/**
	 * Initializes the LogManager by creating the log folder and log file.
	 */
	public static void initialize() {
		String logFolderPath = "./logs";
		File logsFolder = new File(logFolderPath);
		if(!logsFolder.exists()) logsFolder.mkdirs();
		else {
			if(logsFolder.listFiles() != null && logsFolder.listFiles().length > 0) {
				File[] logFiles = new File[logsFolder.listFiles().length];
				int j = logFiles.length - 1;
				for(int i = 0; i < logFiles.length && j >= 0; i++) {
					logFiles[i] = logsFolder.listFiles()[j];
					j--;
				}
				for(File logFile : logFiles) {
					String fileName = logFile.getName().replace(".txt", "");
					int logNumber = Integer.parseInt(fileName.substring(fileName.indexOf("log") + 3)) + 1;
					String newName = logFolderPath + "/log" + logNumber + ".txt";
					if(logNumber < MAX_LOGS - 1) logFile.renameTo(new File(newName));
					else logFile.delete();
				}
			}
		}
		try {
			File newLogFile = new File(logFolderPath + "/log0.txt");
			if(newLogFile.exists()) newLogFile.delete();
			newLogFile.createNewFile();
			logWriter = new FileWriter(newLogFile);
			clearExtraLogs();
		} catch(IOException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Logs a debug message.
	 * @param message The message to log.
	 */
	public static void logDebug(String message) {
		logMessage(MessageType.DEBUG, message);
	}

	/**
	 * Logs an info message.
	 * @param message The message to log.
	 */
	public static void logInfo(String message) {
		logMessage(MessageType.INFO, message);
	}

	/**
	 * Logs a warning message with an optional exception.
	 * @param message The message to log.
	 * @param exception The exception to log.
	 */
	public static void logWarning(String message, Exception exception) {
		if(exception != null) logMessage(MessageType.WARNING, message + ":\n" + exception.getMessage());
		else logMessage(MessageType.WARNING, message);
	}

	/**
	 * Logs an error message with an optional exception.
	 * @param message The message to log.
	 * @param exception The exception to log.
	 */
	public static void logException(String message, Exception exception) {
		exception.printStackTrace();
		logMessage(MessageType.ERROR, message + ":\n" + exception.getMessage());
	}

	/**
	 * Logs a critical exception with an optional message. Will force-stop the program after logging.
	 * @param message The message to log.
	 * @param exception The exception to log.
	 */
	public static void logCritical(String message, Exception exception) {
		exception.printStackTrace();
		logMessage(MessageType.CRITICAL, message + ":\n" + exception.getMessage());
	}

	private static void logMessage(MessageType messageType, String message) {
		String prefix = "[" + DateUtils.getTimeFormatted() + "] " + messageType.prefix;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append(prefix);
			String[] lines = message.split("\n");
			if(lines.length > 1) {
				for(int i = 0; i < lines.length; i++) {
					builder.append(lines[i]);
					if(i < lines.length - 1) {
						if(i > 1) for(int j = 0; j < prefix.length(); j++) builder.append(" ");
					}
				}
			} else {
				builder.append(message);
			}
			System.out.println(builder);
			logWriter.append(builder.toString()).append("\n");
			logWriter.flush();
		} catch(IOException var3) {
			var3.printStackTrace();
		}
		if(messageType.equals(MessageType.CRITICAL)) System.exit(1);
	}

	/**
	 * Clears log files exceeding the maximum number of logs.
	 */
	public static void clearExtraLogs() {
		String logFolderPath = "./logs";
		File logsFolder = new File(logFolderPath);
		if(logsFolder.listFiles() != null && logsFolder.listFiles().length > 0) {
			for(File logFile : logsFolder.listFiles()) {
				String logName = logFile.getName().replace(".txt", "");
				int logNumber = Integer.parseInt(logName.substring(logName.indexOf("log") + 3));
				if(logNumber != 0 && logNumber - 1 >= MAX_LOGS) logFile.delete();
			}
		}
	}
}
