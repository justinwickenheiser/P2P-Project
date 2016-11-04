import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class FTPClient {

	String cmd;
	String[] arguments;
	String server;
	int serverPort;

	Socket socket;
	DataInputStream in;
	DataOutputStream out; 

	Socket dataSocket;
	DataInputStream dataIn;
	DataOutputStream dataOut;

	public FTPClient (String command) throws IOException {
		this.cmd = command;
		this.arguments = cmd.split(" ");

		if (arguments.length == 2) {
			this.server = arguments[1];
			this.serverPort = 2264;
		} else {
			System.out.println("Syntax error: connect <hostname>");
			System.exit(1);
		}

		// Create socket that is connected to server on specified port
		this.socket = new Socket(server, serverPort);
		this.in  = new DataInputStream(socket.getInputStream());
		this.out = new DataOutputStream(socket.getOutputStream());

	}
	
	public void runCommand (String command) throws IOException {
	

		cmd = command;
		arguments = cmd.split(" ");

		// send cmd
		out.writeUTF(cmd);

		switch (arguments[0].toLowerCase()) {
		case "list" :
			
			// Create data socket that is connected to server on port 2265
			dataSocket = new Socket(server, 2265);
			dataIn  = new DataInputStream(dataSocket.getInputStream());
			dataOut = new DataOutputStream(dataSocket.getOutputStream());

			// number of files
			int numOfFiles = Integer.parseInt(dataIn.readUTF());
			for (int i = 0; i < numOfFiles; i++) {
				System.out.println(dataIn.readUTF());
			}
			
			dataIn.close();
			dataOut.close();
			dataSocket.close();
			break;

		case "retr" :
			if (arguments.length == 2) {
				String fileName = arguments[1];
				
				// Create data socket that is connected to server on port 2265
				dataSocket = new Socket(server, 2265);
				dataIn  = new DataInputStream(dataSocket.getInputStream());
				dataOut = new DataOutputStream(dataSocket.getOutputStream());
				
				File file = new File("./client_files/" + fileName);
				FileOutputStream fileOut = new FileOutputStream(file);
				
				byte[] b = new byte [1024];
				int amount_read;
				
				while ((amount_read = dataIn.read(b)) != -1) {
					fileOut.write(b, 0, amount_read);
				}
				
				fileOut.close();
				System.out.println("File " + fileName + " retrieved.");
				
				dataIn.close();
				dataOut.close();
				dataSocket.close();
				break;

			} else {
				System.out.println("Syntax error: RETR <filename>");
				break;
			}

		case "stor" :
			if (arguments.length == 2) {

				String fileName = arguments[1];
				fileName = "./client_files/" + fileName;
				
				// Create data socket that is connected to server on port 2265
				dataSocket = new Socket(server, 2265);
				dataIn  = new DataInputStream(dataSocket.getInputStream());
				dataOut = new DataOutputStream(dataSocket.getOutputStream());
				
				FileInputStream fis = null;
		        boolean fileExists = true ;
				try {
					fis = new FileInputStream(fileName);
		        	System.out.println("File Found.");
		        	
				} catch (FileNotFoundException e) {
					fileExists = false ;
				}
	            
				if (fileExists) {
		        	try {
						sendFile(fis, dataOut);
						fis.close();
						System.out.println("\nSent File: " + fileName);
						
					} catch (Exception e) {
						System.out.println("ERROR in FILE TRANSFER.");
					}
		        }
	            
				dataIn.close();
				dataOut.close();
				dataSocket.close();
				break;

			} else {
				System.out.println("Syntax error: STOR <filename>");
				break;
			}

		case "quit" :
			// Create data socket that is connected to server on port 2265
			dataSocket = new Socket(server, 2265);
			dataIn  = new DataInputStream(dataSocket.getInputStream());
			dataOut = new DataOutputStream(dataSocket.getOutputStream());
			
			System.out.println("Ending Session...");
			
			dataIn.close();
			dataOut.close();
			dataSocket.close();

			//input.close();

			in.close();
			out.close();
			socket.close();
			
			System.out.println("Session Ended.");
			break;	

		default :
			System.out.println("Invalid command.");
			break;
		}
	}

	private static void sendFile(FileInputStream fis, DataOutputStream os) throws Exception {
		System.out.println("Sending File...");
		byte[] buffer = new byte[1024];
		int bytes = 0;
		
		while ((bytes = fis.read(buffer)) != -1) {
			System.out.println("Writing File...");
			os.write(buffer, 0, bytes);
		}
	}

	public static void main (String args[]) throws IOException { 
		if (args.length != 1) {
			throw new RuntimeException ("Syntax: java FTPClient <command>"); 
		}
		FTPClient c = new FTPClient(args[0]); 
	}

}
