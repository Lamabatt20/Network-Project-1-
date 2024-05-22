package Part3;

import java.io.*;
import java.net.*;

public class Server {
	public static void main(String[] args) throws IOException {
		int port = 6060;
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server listening on port " + port);

		while (true) {
			Socket clientSocket = serverSocket.accept();
			System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress() + ",Port:"
					+ clientSocket.getPort());

			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			OutputStream out = clientSocket.getOutputStream();

			String requestLine = in.readLine();

			while (requestLine != null && !requestLine.isEmpty()) {
				// Check if the request is a GET request
				if (requestLine.startsWith("GET")) {
					//Print the received request
					 System.out.println("Received request: " + requestLine);
					int start = requestLine.indexOf("/");
					String path = requestLine.substring(start);
					int end = path.indexOf(" ");
					if (end != -1) {
						path = path.substring(0, end);
					}
					handleRequest(path, out, clientSocket);
				}
				requestLine = in.readLine();
			}
			// Close the client socket
			clientSocket.close();
			System.out.println("Connection closed");
		}

	}

	private static void handleRequest(String path, OutputStream out, Socket clientSocket) throws IOException {
		File file = null;
		String contentType = "text/html"; // Default content type
		
        //Verify that the path fits the html page
		// If the answer is yes, the html file is read and its contents are sent to the client
		if (path.equals("/") || path.equals("/index.html") || path.equals("/main_en.html") || path.equals("/en")) {
			FileInputStream fileInputStream = new FileInputStream("src/Part3/main_en.html");
			byte[] data = new byte[fileInputStream.available()];
			fileInputStream.read(data);
			out.write("HTTP/1.1 200 OK\r\n".getBytes());
			out.write("Content-Type: text/html\r\n".getBytes());
			out.write(("\r\n").getBytes());
			out.write(data);
			fileInputStream.close();
		} else if (path.equals("/ar") || path.equals("/main_ar.html")) {
			FileInputStream fileInputStream = new FileInputStream("src/Part3/main_ar.html");
			byte[] data = new byte[fileInputStream.available()];
			fileInputStream.read(data);
			out.write("HTTP/1.1 200 OK\r\n".getBytes());
			out.write("Content-Type: text/html\r\n".getBytes());
			out.write(("\r\n").getBytes());
			out.write(data);
			fileInputStream.close();
		} else if (path.endsWith(".html")) {
			FileInputStream fileInputStream = null;
			if (new File("src/part3" + path).exists()) {
				fileInputStream = new FileInputStream("src/part3" + path);
			} else {
				fileInputStream = new FileInputStream("src/part3/colgroup.html");
			}
			byte[] data = new byte[fileInputStream.available()];
			fileInputStream.read(data);
			out.write("HTTP/1.1 200 OK\r\n".getBytes());
			out.write(("Content-Type: " + contentType + "\r\n").getBytes());
			out.write(("Content-Length: " + data.length + "\r\n").getBytes());
			out.write(("\r\n").getBytes());
			out.write(data);
			fileInputStream.close();
		} else if (path.endsWith(".css")) {
			FileInputStream fileInputStream = null;
			if (new File("src/part3" + path).exists()) {
				fileInputStream = new FileInputStream("src/part3" + path);
			} else {
				fileInputStream = new FileInputStream("src/part3/Style.css");
			}
			byte[] data = new byte[fileInputStream.available()];
			fileInputStream.read(data);
			contentType = "text/css";
			out.write("HTTP/1.1 200 OK\r\n".getBytes());
			out.write(("Content-Type: " + contentType + "\r\n").getBytes());
			out.write(("Content-Length: " + data.length + "\r\n").getBytes());
			out.write(("\r\n").getBytes());
			out.write(data);
			fileInputStream.close();
		} else if (path.endsWith(".png")) {
			FileInputStream fileInputStream = null;
			if (new File("src/part3" + path).exists()) {
				fileInputStream = new FileInputStream("src/part3" + path);
			} else {
				fileInputStream = new FileInputStream("src/part3/icons8-world-50.png");
			}
			byte[] data = new byte[fileInputStream.available()];
			fileInputStream.read(data);
			contentType = "image/png";
			out.write("HTTP/1.1 200 OK\r\n".getBytes());
			out.write(("Content-Length: " + data.length + "\r\n").getBytes());
			out.write(("Content-Type: " + contentType + "\r\n").getBytes());
			out.write(("\r\n").getBytes());
			out.write(data);
			fileInputStream.close();
		} else if (path.endsWith(".jpg")) {
			FileInputStream fileInputStream = null;
			if (new File("src/part3" + path).exists()) {
				fileInputStream = new FileInputStream("src/part3" + path);
			} else {
				fileInputStream = new FileInputStream("src/part3/download.jpg");
			}
			byte[] data = new byte[fileInputStream.available()];
			fileInputStream.read(data);
			contentType = "image/jpg";
			out.write("HTTP/1.1 200 OK\r\n".getBytes());
			out.write(("Content-Length: " + data.length + "\r\n").getBytes());
			out.write(("Content-Type: " + contentType + "\r\n").getBytes());
			out.write(("\r\n").getBytes());
			out.write(data);
			fileInputStream.close();

		} else if (path.equals("/so")) {
			out.write(("HTTP/1.1 307 Temporary Redirect\r\n" + "Content-Type: text/html\r\n" + "Location: "
					+ "https://stackoverflow.com" + "\r\n" + "\r\n").getBytes());
			out.flush();
		} else if (path.equals("/itc")) {
			out.write(("HTTP/1.1 307 Temporary Redirect\r\n" + "Content-Type: text/html\r\n" + "Location: "
					+ "https://itc.birzeit.edu" + "\r\n" + "\r\n").getBytes());
		} else {
			//if the request is wrong or the file doesn’t exist the server should send a HTML error page
			sendNotFound(out, clientSocket);

		}

		out.flush();
	}
//send a simple HTML error page
    private static void sendNotFound(OutputStream out, Socket clientSocket) throws IOException {
    	String htmlResponse = "<!DOCTYPE html>\r\n"
                + "<html lang=\"en\">\r\n"
                + "<head>\r\n"
                + "    <meta charset=\"UTF-8\">\r\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                + "    <title>Error 404</title>\r\n"
                + "    <style>\r\n"
                + "        body {\r\n"
                + "            font-family: Arial, sans-serif;\r\n"
                + "            background-color: #888B90;\r\n"
                + "        }\r\n"
                + "        .container {\r\n"
                + "            max-width: 600px;\r\n"
                + "            margin: 50px auto;\r\n"
                + "            padding: 20px;\r\n"
                + "            background-color: #fff;\r\n"
                + "            border-radius: 5px;\r\n"
                + "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\r\n"
                + "        }\r\n"
                + "        h1{\r\n"
                + "            color: #000000;\r\n"
                + "        }\r\n"
                + "        h2{\r\n"
                + "            color: red;\r\n"
                + "        }\r\n"
                + "        strong {\r\n"
                + "            font-weight: bold;\r\n"
                + "        }\r\n"
                + "    </style>\r\n"
                + "</head>\r\n"
                + "<body>\r\n"
                + "    <div class=\"container\">\r\n"
                + "        <h1>Error 404</h1>\r\n"
                + "        <h2>The file is not found.</h2>\r\n"
                + "        <p><strong>Lama Batta-1210922</strong></p>\r\n"
                + "        <p><strong>Yuna Nawahda-1211524</strong></p>\r\n"
                + "        <p><strong>Hanadi Asfour-1210209</strong></p>\r\n"
                + "        <p><strong>Client IP:</strong> " + clientSocket.getInetAddress().getHostAddress() + "</p>\r\n"
                + "        <p><strong>Client Port:</strong> " + clientSocket.getPort() + "</p>\r\n"
                + "    </div>\r\n"
                + "</body>\r\n"
                + "</html>";


        out.write(("HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + htmlResponse.length() + "\r\n" +
                "\r\n" + htmlResponse).getBytes());
        out.flush();
        clientSocket.close();
        out.close();
    }
}

