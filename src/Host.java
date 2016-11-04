import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class Host {

	public HostGUI gui;
	public FTPClient ftpClient;
	public volatile boolean quit = false;

	public String server;
	public int serverPort;
	public String cmd;
	public volatile boolean receivedGroupOne = false;

	public String keyword;
	public volatile boolean receivedGroupTwo = false;


	public Host() throws IOException {

		// Create GUI and handle events
		gui = new HostGUI("Napster and Friends");
		gui.connect.addActionListener( new ButtonListener(this, gui, ftpClient, 1) );
		gui.search.addActionListener( new ButtonListener(this, gui, ftpClient, 2) );
		gui.go.addActionListener( new ButtonListener(this, gui, ftpClient, 3) );
		
		Scanner input = new Scanner(System.in);

		//server = gui.getServerHostname();
		//serverPort = 4096; //gui.getServerPort();
		
		while (!receivedGroupOne) {
			;
		}
			
		/*
		 *
		 *	INITIAL CONNECTION
		 *
		 */
				
		// Create socket that is connected to server on specified port
		Socket socket = new Socket(server, serverPort);
		DataInputStream in  = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		// Create data socket that is connected to server on port 4097
		Socket dataSocket = new Socket(server, 4097);
		DataInputStream dataIn  = new DataInputStream(dataSocket.getInputStream());
		DataOutputStream dataOut = new DataOutputStream(dataSocket.getOutputStream());

		System.out.println("Connecting..." + "\nServer: " + server + "\nPort: " + serverPort);

		// write to handler the username, users hostname, and speed
		out.writeUTF(cmd);



		// Send filelist.xml file
		String fileName = new String("fileList.xml");
		FileInputStream fis = null;
		boolean fileExists = true ;
		try {
			fis = new FileInputStream(fileName);
			System.out.println("\nThe filelist.xml was found.");

		} catch (FileNotFoundException e) {
			fileExists = false ;
		}

		if (fileExists) {
			try {
				sendFile(fis, dataOut);
				fis.close();
				System.out.println("Sent File: " + fileName);
			} catch (Exception e) {
				System.out.println("ERROR in FILE TRANSFER.");
			}
		}

		// close dataSocket
		dataIn.close();
		dataOut.close();
		dataSocket.close();


		/*
		 *
		 *	KEYWORD SEARCH
		 *
		 */
		String receivedMsg = new String("");
		while (!quit) {
			while (!receivedGroupTwo) {
				;
			}

			// reset receivedMsg
			receivedMsg = "";
			
			// Do a keyword search
			out.writeUTF(keyword);

			if (!keyword.equals("quit")) {
				// read responses from keyword search from central server
				try {
					System.out.println("\n-- Results --");
					while (!(receivedMsg = in.readUTF()).equals("done")) {
						System.out.println(receivedMsg);
					}
				} catch (EOFException e) {
					System.out.println("\n-- End of Results --");
				}
				receivedGroupTwo = false;
			} else {
				quit = true;
			}
		
		}

		// close connection socket
		in.close();
		out.close();
		socket.close();
		System.exit(0);
	}
	
	private static void sendFile(FileInputStream fis, DataOutputStream os) throws Exception {
		System.out.println("Sending filelist.xml");
		byte[] buffer = new byte[1024];
		int bytes = 0;
		
		while ((bytes = fis.read(buffer)) != -1) {
			System.out.println("Writing File...");
			os.write(buffer, 0, bytes);
		}
	}

	public void setGroupOne(String serverName, int sPort, String command) {
		server = serverName;
		serverPort = sPort;
		cmd = command;
		receivedGroupOne = true;
	}

	public void setGroupTwo(String kw) {
		keyword = kw;
		receivedGroupTwo= true;
	}

	public static void main (String args[]) throws IOException { 
		if (args.length != 0) {
			throw new RuntimeException ("Syntax: java Host <Server> <port>"); 
		}
		Host h = new Host(); 
	}

}