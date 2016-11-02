import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;

public class Handler extends Thread {

	Socket clientSocket;
	ServerSocket dataSocket;
	DataInputStream in;
	DataOutputStream out;
	DataInputStream dataIn;
	DataOutputStream dataOut;

	String clientUsername;
	String clientHostname;
	String clientSpeed;

	NodeList nameArray;
	NodeList descArray;
	protected static Vector handlers = new Vector();

	// Constructor
	public Handler(Socket socket, ServerSocket datalistener) throws IOException {
		this.clientSocket = socket;
		this.dataSocket = datalistener;
		this.in = new DataInputStream (clientSocket.getInputStream());
		this.out = new DataOutputStream(clientSocket.getOutputStream());
	}

	public void run() {
		// try connecting and receiving user info and filelist info
		try {
			// Add this thread to the vector of threads
			handlers.addElement(this);

			System.out.println("\nHandling Request...\n" + 
					"Host: " + clientSocket.getInetAddress().getHostAddress() + 
					"\nPort: " + clientSocket.getPort());

			// Get Username, Hostname, and Connection Speed from the client
			String cmd = in.readUTF();
			String[] clientArgs = cmd.split(" ");

			// Store that info into "Users" table
			clientUsername = clientArgs[0];
			clientHostname = clientArgs[1];
			clientSpeed = clientArgs[2];

			System.out.println("\nAdded User to 'Users' Table...\n" + 
					"Username: " + clientUsername + 
					"\nHostname: " + clientHostname +
					"\nSpeed: " + clientSpeed + "\n");

			// print who is all here
			System.out.println("\n---- Users ----");
			printHandlers();
			System.out.println("---------------");

			// accept the connection of data socket
			Socket clientDataSocket = dataSocket.accept();
			dataIn = new DataInputStream (clientDataSocket.getInputStream());
			dataOut = new DataOutputStream(clientDataSocket.getOutputStream());
			
			// Handle file upload
			try {
				// Receive the filelist.xml file and add it to the "files" table
				recvFile(dataIn);
				System.out.println("filelist.xml retrieved.");
			} catch (Exception e) {
				System.out.println("Error reading the filelist.");
				System.out.println(e);
			}

			// Handle search
			try {
				String keyword = in.readUTF();
				searchFile(keyword);
			} catch (Exception e) {
				System.out.println("Error searching files");
				System.out.println(e);
			}

			System.out.println("Session Ended.");
			out.close();
			in.close();
			dataIn.close();
			dataOut.close();
			clientSocket.close();
			clientDataSocket.close();
		} catch (IOException ex) {
			System.out.println("-- Connection to user lost.");
		}
	}

	private void recvFile(DataInputStream is) throws Exception {
		System.out.println("Receiving File...");
	
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(is);
		nameArray = document.getElementsByTagName("name");
		descArray = document.getElementsByTagName("description");
	}

	private void searchFile(String kw) throws Exception {
		synchronized (handlers) {
			Enumeration e = handlers.elements ();
			while (e.hasMoreElements()) {
				Handler handler = (Handler) e.nextElement();
				
				int numOfDescs = handler.descArray.getLength();

				String nameField = new String("");
				String descField = new String("");

				// loop through the descriptions
				for (int i = 0; i < numOfDescs; i++) {
					descField = handler.descArray.item(i).getTextContent();
					if (descField.toLowerCase().contains(kw.toLowerCase())) {
						// description contains keyword
						// get the name of the file with that description
						nameField = handler.nameArray.item(i).getTextContent();
						
						// output the speed, hostname, and filename
						System.out.println("\nSpeed: " + handler.clientSpeed);
						System.out.println("Hostname: " + handler.clientUsername + "/" + handler.clientHostname);
						System.out.println("File: " + nameField);

						String result = new String(handler.clientSpeed + " " + handler.clientUsername + "/" + handler.clientHostname + " " + nameField);
						
						try {
							handler.out.writeUTF(result);
						} catch (IOException ex) { 
							handler.stop (); 
						}
					}
				}	
			}
		}
	}

	private void printHandlers() {
		synchronized (handlers) {
			Enumeration e = handlers.elements ();
			while (e.hasMoreElements()) {
				Handler handler = (Handler) e.nextElement();
				
				// output the speed, hostname, and filename
				System.out.println("User: " + handler.clientUsername + "/" + handler.clientHostname + "\t" + handler.clientSpeed);
			}
		}
	}

}