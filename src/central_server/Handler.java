import java.io.*;
import java.net.*;
import java.util.*;

public class Handler extends Thread {

	Socket clientSocket;
	BufferedReader in;
	DataOutputStream out;
	ServerSocket dataSocket;


	// Constructor
	public Handler(Socket socket) throws IOException {
		this.clientSocket = socket;
		in = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
		out = new DataOutputStream(clientSocket.getOutputStream());
		// Set up data socket to receive file descriptions
		dataSocket = new ServerSocket(4097);
			
	}

	public void run() {
		try {
			System.out.println("Handling Request...\n" + 
					"Host: " + clientSocket.getInetAddress().getHostAddress() + 
					"\nPort: " + clientSocket.getPort());

			// Get Username, Hostname, and Connection Speed from the client

			// Store that info into "Users" table


			Socket data = dataSocket.accept();

			BufferedReader dataIn = 
					new BufferedReader(new InputStreamReader (data.getInputStream()));
			InputStream dataIS = data.getInputStream();
			OutputStream dataOut = data.getOutputStream();

			String requestLine = dataIn.readLine();

		
			System.out.println("Session Ended.");
			out.close();
			in.close();
			clientSocket.close();
			data.close();
		} catch (IOException ex) {
			System.out.println("-- Connection to user lost.");
		} finally {
			
		}
	}
}