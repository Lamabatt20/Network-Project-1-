import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UDPclient extends Thread {

	private DatagramSocket socket;// client UDP socket
	private InetAddress serverAddress;// save IP address
	private int serverPort;// save port
	private static Scanner scanner = new Scanner(System.in);// scanner for reading user inputs

	public static void main(String[] args) {

		// initialize client, connect to server of specified IP address on port 5051
		UDPclient client = new UDPclient("26.42.34.148", 5051);

		client.start();// starting thread to allow client to receive data from server

		try {
			enterInfo(client);// get and send the user input for first name, last name, and message

		} catch (Exception e) {// catch exceptions
			System.out.println("Error sending to server");
		}

		while (true) {// as long as client exists
			try {

				/*
				 * repeatedly asking the user if they want to send another message by typing m
				 * or read an existing one by entering the number and the letter D
				 */
				System.out.println(
						"\nType 'm' to send a new message, or read a message by typing its number than the letter D.\n");

				String response = scanner.nextLine();// wait for input from user

				// input is in form of "number + D"
				if (!response.isEmpty() && response.matches("\\d+D")) {
					client.readMessage(response);// send to server the number of message to print
					System.out.println(// line separator
							"-----------------------------------------------------------------------------------------------------\n");
				}

				// input is "m"
				if (!response.isEmpty() && response.equals("m")) {
					enterInfo(client); // invoke user to enter info to send a new message

				}

			} catch (Exception e) {// catch exceptions
				System.out.println("Error sending to server");
			}
		}
	}

	/*
	 * this method invokes the user to enter their first name, last name, and
	 * message to send to the server then to all peers
	 */
	public static void enterInfo(UDPclient client) throws IOException {

		// variables to store inputs
		String firstName;
		String lastName;
		String message;

		// getting user inputs
		System.out.print("Enter your first name: ");
		firstName = scanner.nextLine();
		System.out.print("Enter your last name: ");
		lastName = scanner.nextLine();
		System.out.print("Enter your message: ");
		message = scanner.nextLine();

		System.out.println("---------------------------------------------\n");
		client.sendMessage(firstName, lastName, message);// sends the message to server for distribution

	}

	/*
	 * this method takes in the users info and message content then sends it to the
	 * server to distribute it to all of the existing peers connected to the server
	 */
	public void sendMessage(String firstName, String lastName, String message) throws IOException {

		/*
		 * concatenating the message with format "firstName lastName message" to ease
		 * parsing it by the server from the same format
		 */
		String format = firstName + " " + lastName + " " + message;

		byte[] data = format.getBytes();// converting to byte array

		// creating packet with the data
		DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverAddress, serverPort);
		socket.send(sendPacket);// send packet to server

	}

	/*
	 * this method takes in the entered message number to read and passes it to the
	 * server to reveal content
	 */
	public void readMessage(String message) throws IOException {

		byte[] msgNum = message.getBytes();// converting message to bytes array

		// creating packet with the message number
		DatagramPacket sendPacket = new DatagramPacket(msgNum, msgNum.length, serverAddress, serverPort);
		socket.send(sendPacket);// send packet

	}

	/*
	 * this thread is to continuously receive packets from the server if sent
	 */
	public void run() {// starting thread

		while (true)// as long as client object exists and thread is running

			try {
				byte[] received = new byte[1024];// byte array to hold any data received from server
				// allocating byte array with packet
				DatagramPacket receivedPack = new DatagramPacket(received, received.length);

				socket.receive(receivedPack);// accept data packet if exists from server

				// converting the received data to a string and then printing it
				String receivedMessage = new String(receivedPack.getData(), 0, receivedPack.getLength());

				System.out.println("\n**Message from server** \n" + receivedMessage);// print

			} catch (Exception e) {// catch potential exception if socket suddenly closed
			}

	}

	// constructor with parameters for client initialization
	public UDPclient(String serverAddress, int serverPort) {
		try {

			// convert the server address from string to InetAddress object
			this.serverAddress = InetAddress.getByName(serverAddress);
			this.serverPort = serverPort;// save port
			this.socket = new DatagramSocket();// initializing socket

		} catch (Exception e) {// if socket cannot be opened or the host is unknown
			System.out.println("Error connecting from client to server");
		}
	}

}
