import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

public class UDPserver extends Thread {
	private DatagramSocket socket;// server UDP socket
	private ArrayList<User> peers = new ArrayList<>();// list to save the joined clients

	public UDPserver() {// constructor for initialization
		try {
			// initializing the DatagramSocket on port 5051 (specified in question)
			socket = new DatagramSocket(5051);
			// printing confirmation message
			System.out.println("Server Ready");

		} catch (Exception e) {// in-case the port is already in use
			System.out.println("Error opening server socket on port 5051");
		}
	}

	public void run() {// starting thread

		byte[] buffer = new byte[1024];// buffer for data input

		while (true) {// as long as the thread is running

			try {

				// receive data from client as a packet
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);

				// saving the senders address and port number
				InetAddress address = packet.getAddress();
				int port = packet.getPort();

				// converting data received to string
				String received = new String(packet.getData(), 0, packet.getLength());

				if (received.matches("\\d+D")) {// read a message indicated by number

					int number = Integer.parseInt(received.charAt(0) + "");// extract message number

					/*
					 * by creating a dummy user with the senders address, we can find them from the
					 * peers list. This way we can obtain their received messages list, and then
					 * extract the specified message which they want to view the contents of.
					 * 
					 */
					User req = peers.get(peers.indexOf(new User(address)));// get the user

					String data = req.getMessageContent(number);// using number sent as index for list

					byte[] dataAsByte = data.getBytes();// transforming the message to a byte array

					// creating packet to send back to client
					DatagramPacket pack = new DatagramPacket(dataAsByte, dataAsByte.length, address, port);
					socket.send(pack);// send to peer

					//////////////////////////////////////////////////////////////////
					///// sending the list of received messages to the peer again/////
					//////////////////////////////////////////////////////////////////

					String messages = req.getString();// get the received messages for this peer
					byte[] list = messages.getBytes();// convert list info to bytes

					pack = new DatagramPacket(list, list.length, address, port);// create packet
					socket.send(pack);// send to peer

				} else {// new client or new message

					/*
					 * extracting the first name, last name, and message content from received data
					 * string using a scanner
					 */
					Scanner scn = new Scanner(received);// creating scanner for input
					String first = scn.next();// first name
					String last = scn.next();// last name
					String msg = scn.nextLine();// message content
					scn.close();// closing scanner

					User u = new User(first, last, address, port);// creating user
					int index = peers.indexOf(u);// getting the users index form the peers list

					if (index == -1) {// new peer if not in list
						peers.add(u);// add to peers list
						System.out.println("connection from " + first + " " + last);// confirmation message

					} else// already exists
						u = peers.get(index);// retrieving the peer from list

					Message m = new Message(msg, u);// creating the message object
					addToMessages(m);// adding the message to the all of the peers received messages
					sendMessage();// adding to list of messages received for all peers

				}

			} catch (Exception e) {// in-case we attempt to send packet when the socket is closed
				System.out.println("Error sending to peers");
			}

		}

	}

	/*
	 * This method takes a message object and adds it to all of the peers received
	 * messages list except the sender himself
	 */
	public void addToMessages(Message m) {

		User sender = m.getSender();// saving the sender of the message

		for (User u : peers) {// loop peers
			if (!u.equals(sender))// not sender
				u.addMsg(m);// add message to received messages of peer

		}

	}

	/*
	 * This method sends to all of the peers a string representation of their
	 * received messages to choose which to read
	 */
	public void sendMessage() throws IOException {

		for (User peer : peers) {// loop all peers

			String messages = peer.getString();// get the received messages from other peers
			byte[] send = messages.getBytes();// convert to bytes array

			// create packet
			DatagramPacket pack = new DatagramPacket(send, send.length, peer.getAddress(), peer.getPort());
			socket.send(pack);// send packet to peer

		}
	}

	public static void main(String[] args) {
		new UDPserver().start();// initializing server
	}
}
