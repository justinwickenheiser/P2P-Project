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
	DataInputStream in;
	DataOutputStream out;
	ServerSocket dataSocket;
	NodeList nameArray;
	NodeList descArray;
	protected static Vector handlers = new Vector();

	// Constructor
	public Handler(Socket socket) throws IOException {
		this.clientSocket = socket;
		this.in = new DataInputStream (clientSocket.getInputStream());
		this.out = new DataOutputStream(clientSocket.getOutputStream());
	}

	public void run() {
		// try connecting and receiving user info and filelist info
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
					"\nHostname: " + clientHostname +
					"\nSpeed: " + clientSpeed + "\n");

			/*
			 * We are grabbing the user's keyword search now and writing it to the server now
			 * in order to prevent weird issues with the xml parser on the server side. We will
			 * not run the search until after handling the file upload from the client
			 */
			String keyword = in.readUTF();
			
			// Hand file upload
			try {
				// Receive the filelist.xml file and add it to the "files" table
				// Parse the filelist.xml file
				recvFile(in, nameArray, descArray);
				System.out.println("filelist.xml retrieved.");
			} catch (Exception e) {
				System.out.println("Error reading the filelist.");
				System.out.println(e);
			}

			// Handle search
			try {
				//searchFile(keyword, nameArray, descArray);
				System.out.println("nameArray[0]: " +  nameArray.item(0).getTextContent());

			} catch (Exception e) {
				System.out.println("Error searching files");
				System.out.println(e);
			}

			System.out.println("Session Ended.");
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException ex) {
			System.out.println("-- Connection to user lost.");
		}
	}

	private static void recvFile(InputStream is, NodeList nameFieldArray, NodeList descFieldArray) throws Exception {
		System.out.println("Receiving File...");
		byte[] b = new byte [1024];
		int amount_read;
	
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(is);
		nameFieldArray = document.getElementsByTagName("name");
		descFieldArray = document.getElementsByTagName("description");
		
		is.close();
		/*
		 * USAGE:
		 * String nameField = document.getElementsByTagName("name").item(0).getTextContent();
		 * String descField = document.getElementsByTagName("description").item(0).getTextContent();
		 */
	}

	private static void searchFile(String kw, NodeList nameFieldArray, NodeList descFieldArray) throws Exception {
		synchronized (handlers) {
			Enumeration e = handlers.elements ();
			while (e.hasMoreElements()) {
				Handler handler = (Handler) e.nextElement();
				//try {
					int numOfNames = nameFieldArray.getLength();
					int numOfDescs = descFieldArray.getLength();

					String nameField = new String("");
					String descField = new String("");

					// loop through the names
					for (int i = 0; i < numOfNames; i++) {
						nameField = nameFieldArray.item(i).getTextContent();
						if (nameField.toLowerCase().contains(kw.toLowerCase())) {
							// name contains keyword
							System.out.println(nameField);
						}
					}

					//handler.out.writeUTF(message);

				//} catch (IOException ex) { 
				//	handler.stop (); 
				//} 
			}
		}
	}

}