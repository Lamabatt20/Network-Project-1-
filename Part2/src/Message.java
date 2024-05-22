import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Message {

	// variables to save info
	private String content, time;
	private User sender;

	// constructor with parameters
	public Message(String content, User sender) {
		this.sender = sender;
		this.content = content;

		// saving the time of the message
		Calendar cal = Calendar.getInstance();// calendar instance
		SimpleDateFormat format = new SimpleDateFormat("h:mm:ss a");// format for the time
		time = format.format(cal.getTime());// retrieving the time

	}

	// getters and setters
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

}
