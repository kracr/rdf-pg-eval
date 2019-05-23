package neo4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log{
	private String exception;
	private String dateTime;

	public static void createlog(String message) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date1 = new Date();
		String currentdate = sdf.format(date1);
		File file1 = new File(System.getProperty("user.home") + "/Logs.txt");
		FileWriter fw = new FileWriter(file1, true);
		PrintWriter pw = new PrintWriter(fw);
		pw.append("\n");
		pw.append("\n");
		pw.append(currentdate + ": " + message);
		pw.append("\n");
		pw.close();
	}

	public String getDateTime() {
		return dateTime;
	}

	public String getException() {
		return exception;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

}
