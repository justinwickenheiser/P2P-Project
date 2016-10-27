import java.io.*;
import java.net.*;
import java.util.*;

public class Handler extends Thread {

	Socket clientSocket;
	DataInputStream in;
	DataOutputStream out;
	ServerSocket dataSocket;
	protected static Vector handlers = new Vector();

	// Constructor
	public Handler(Socket socket) throws IOException {
		this.clientSocket = socket;
		in = new DataInputStream (clientSocket.getInputStream());
		out = new DataOutputStream(clientSocket.getOutputStream());
	}

	public void run() {
		try {
			// Add this thread to the vector of threads
			handlers.addElement(this);

			System.out.println("Handling Request...\n" + 
					"Host: " + clientSocket.getInetAddress().getHostAddress() + 
					"\nPort: " + clientSocket.getPort());

			// Get Username, Hostname, and Connection Speed from the client
			String cmd = in.readUTF();
			String[] clientArgs = cmd.split(" ");

			// Store that info into "Users" table
			String clientUsername = clientArgs[0];
			String clientHostname = clientArgs[1];
			String clientSpeed = clientArgs[2];

			System.out.println("Added User to 'Users' Table...\n" + 
					"Username: " + clientUsername + 
					"\nHosename: " + clientHostname +
					"\nSpeed: " + clientSpeed + "\n");


			// Receive the filelist.xml file and add it to the "files" table
			


		
			System.out.println("Session Ended.");
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException ex) {
			System.out.println("-- Connection to user lost.");
		} finally {
			
		}
	}
}