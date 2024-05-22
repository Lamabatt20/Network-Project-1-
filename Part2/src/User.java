import java.net.InetAddress;
import java.util.ArrayList;

public class User {

	private String first, last;// variables to hold first and last names
	private InetAddress address;// hold user address id
	private int port;// save port connected from
	private ArrayList<Message> messages = new ArrayList<>();// list of received messages from other peers

	// constructor with parameters
	public User(String first, String last, InetAddress address, int port) {
		this.first = first;
		this.last = last;
		this.address = address;
		this.port = port;
	}

	/*
	 * constructor used to create dummy user for located an already existing user
	 * with the same address from list of peers
	 */
	public User(InetAddress address) {
		this.address = address;
	}

	// adds the given message to the received messages list
	public void addMsg(Message m) {
		messages.add(m);
	}

	// given an index, obtain the message from the received messages list
	public String getMessageContent(int index) {

		if (index < 1 || index > messages.size()) // checking if index in bounds
			return "No Such Message....\n\n";

		Message m = messages.get(index - 1);// obtaining message with specified index
		return "Message:\n" + m.getContent();// returning content

	}

	@Override
	public boolean equals(Object obj) {// compares users by their addresses

		if (address == null && obj == null)// both null
			return true;

		return this.address.equals(((User) obj).address);// invoke IntetAddress equals method

	}

	/*
	 * This method returns a string representation of a list of numbered messages
	 * received by this user along with the time of sending
	 * 
	 */
	public String getString() {

		StringBuilder str = new StringBuilder();// to hold string

		str.append("Peer " + first + " " + last + "\n");// add headline

		int count = 1;// count the messages

		for (Message m : messages) {// loop the messages

			str.append(count + "- received a message from " + m.getSender().first + " " + m.getSender().last + " at "
					+ m.getTime() + "\n");

			count++;// next message number
		}

		if (count == 1)// there is no received messages
			return "";

		else// return string builder content as string
			return str.toString();// return the messages

	}

	// getters and setters
	public ArrayList<Message> getMessages() {
		return messages;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
