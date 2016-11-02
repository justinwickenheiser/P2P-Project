import java.io.*;
import java.net.*;
import java.util.*;


public class FTPServer {

	public FTPServer(int listenPort) throws IOException {
		// Establish the listen socket.
		ServerSocket serverListenSocket = new ServerSocket(listenPort);
		ServerSocket dataListenSocket = new ServerSocket(2265);
		while (true) {
			// Listen for a TCP connection request.
			Socket clientSocket = serverListenSocket.accept();

			HandlerFTP handlerForClient = new HandlerFTP(clientSocket, dataListenSocket);
			handlerForClient.start();
		}
	}

	public static void main (String args[]) throws IOException { 
		if (args.length != 1) {
			throw new RuntimeException ("Syntax: java FTPServer <listen_port>\nSuggested port: 2264"); 
		}
		new FTPServer(Integer.parseInt (args[0])); 
	}
	
}




/*

public class FTPServer {


	public static void main (String argv[]) throws IOException {

		int port = 4096;
		ServerSocket servSocket = new ServerSocket(port);

		Socket clientSocket = servSocket.accept();
		BufferedReader in = 
				new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
		DataOutputStream out = 
				new DataOutputStream(clientSocket.getOutputStream());

		System.out.println("Handling Request...\n" + 
				"Host: " + clientSocket.getInetAddress().getHostAddress() + 
				"\nPort: " + clientSocket.getPort());

		while (!clientSocket.isClosed()) {
			ServerSocket dataSocket = new ServerSocket(4097);
			Socket data = dataSocket.accept();

			BufferedReader dataIn = 
					new BufferedReader(new InputStreamReader (data.getInputStream()));
			InputStream dataIS = data.getInputStream();
			
			OutputStream dataOut = data.getOutputStream();

			String requestLine = dataIn.readLine();
			
			if (requestLine.startsWith("Q")) {
				System.out.println("\nRequest: " + requestLine);
				System.out.println("Closing Connection...");
				break;
			}

			processRequest(requestLine, dataIn, dataIS, dataOut);

			dataIn.close();
			dataOut.close();
			dataSocket.close();

		}
		
		System.out.println("Session Ended.");
		out.close();
		in.close();
		clientSocket.close();
		servSocket.close();
	}

	private static void processRequest(String cmd, BufferedReader dataIn, 
										InputStream dataIS, OutputStream dataOut) {
		System.out.println("\nRequest: " + cmd);
		String pwd = "/Users/kaye/Desktop/workspace/FTPWebServer/src/server_files";

		String[] args = cmd.split(" ");

		switch(args[0]) {

		case ("LIST") :
			StringBuffer list = new StringBuffer();
			PrintStream printOS = new PrintStream(dataOut);

			for (String line : files(pwd)) {
				list.append(line);
				list.append("\n");
			}

			System.out.println(list.toString());
			printOS.println(list);

			printOS.flush();
			break;

		case ("RETR") :

			String fileName = args[1];
			fileName = "./server_files/" + fileName;
			
			//System.out.println("Path for InputStream: " + fileName);
		
	        FileInputStream fis = null;
	        boolean fileExists = true ;
	        try {
	        	fis = new FileInputStream(fileName);
	        	System.out.println("File Found.");
	            
	        } catch (FileNotFoundException e) {
	            fileExists = false ;
	        }
	        
	        //Send the File
	        if (fileExists) {
	        	try {
					sendFile(fis, dataOut);
					fis.close();
					System.out.println("Sent File: " + fileName);
					
				} catch (Exception e) {
					System.out.println("ERROR in FILE TRANSFER.");
				}
	        }
	        
			break;

		case ("STOR") :
			String newFileName = args[1];
			File file = new File("./server_files/" + newFileName);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				copyFile(dataIS, fos);
				fos.close();
				
			} catch (Exception e) {
				System.out.println("\nERROR in COPYING FILE.");
			}

			System.out.println("File " + newFileName + " retrieved.");
			break;
		}
	}

	private static List<String> files(String pwd) {
		ArrayList<String> files = new ArrayList<String>();

		try {
			// Launch external process
			String[] commands = {"/bin/bash", "-c", "ls " + pwd};
			Process p = Runtime.getRuntime().exec(commands);

			// Place generated lines in a List
			Scanner input = new Scanner(p.getInputStream());
			while (input.hasNext()) {
				files.add(input.nextLine());
			}
			
			input.close();
			
		} catch (IOException e) {
			files.add("There was a problem: " + e);
		}
		return files;
	}

	private static void sendFile(FileInputStream fis, OutputStream os) throws Exception {
		System.out.println("Sending File...");
		byte[] buffer = new byte[1024];
		int bytes = 0;
		
		while ((bytes = fis.read(buffer)) != -1) {
			System.out.println("Writing File...");
			os.write(buffer, 0, bytes);
		}
		
		os.flush();
	}
	
	private static void copyFile(InputStream is, FileOutputStream fos) throws Exception{
		System.out.println("Receiving File...");
		byte[] b = new byte [1024];
		int amount_read;
	
		while ((amount_read = is.read(b)) != -1) {
			System.out.println("Copying File...");
			fos.write(b, 0, amount_read);
		}
		
		is.close();;
	}

}
*/