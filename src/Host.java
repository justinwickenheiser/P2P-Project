import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class Host {

	public static void main(String[] args) throws IOException {
		if (args.length != 2) { // Test for correct # of args
			throw new IllegalArgumentException("Parameter(s): <Server> <port>");
		}

		Scanner input = new Scanner(System.in);

		String server = args[0];
		int serverPort = new Integer(args[1]).intValue();

		// Create socket that is connected to server on specified port
		Socket socket = new Socket(server, serverPort);
		DataInputStream in  = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		// Create data socket that is connected to server on port 4097
		Socket dataSocket = new Socket(server, 4097);
		DataInputStream dataIn  = new DataInputStream(dataSocket.getInputStream());
		DataOutputStream dataOut = new DataOutputStream(dataSocket.getOutputStream());

		System.out.println("Connecting..." + "\nServer: " + server + "\nPort: " + serverPort);


		// Speeds: ethernet, t1, t3, modem, etc.
		System.out.println("\nEnter the Following Command: <your_username> <your_hostname> <your_speed>");
		String cmd = input.nextLine();
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

		// Do a keyword search
		System.out.println("\nKeyword Search: ");
		String keyword = input.nextLine();
		out.writeUTF(keyword);

		// read responses from keyword search from central server
		try {
			System.out.println("\n-- Results --");
			while (!socket.isClosed()) {
				System.out.println(in.readUTF());
			}
		} catch (EOFException e) {
			System.out.println("\n-- End of Results --");
		}

		

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
	
}