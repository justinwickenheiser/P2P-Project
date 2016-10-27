import java.io.*;
import java.net.*;
import java.util.*;

public class FTPClient {

	public static void main (String argv[]) throws IOException {
		Scanner input = new Scanner(System.in);

		//CONNECT with Server
		System.out.println("Connect to Server: CONNECT <server> <port>");

		String cmd = input.nextLine();
		String[] args = cmd.split(" ");

		if (args.length != 3) {
			input.close();
			throw new IllegalArgumentException("Syntax Error: CONNECT <server> <port>");
		}
		
		String server = args[1];
		int port = new Integer(args[2]).intValue();
		byte[] buffer;

		//Create socket and Input / Output Stream
		//TODO: create as Buffer Readers
		Socket socket = new Socket(server, port);
		BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		OutputStream out = socket.getOutputStream();

		System.out.println("Connecting..." 
				+ "\nServer: " + server + "\nPort: " + port);

		while (true) {
			System.out.println("\nEnter Command:");
			cmd = input.nextLine();
			args = cmd.split(" ");
			buffer = cmd.getBytes();
			

			switch (args[0]) {
			case "LIST" :
				
				//EST Data Communication Socket
				Socket data = new Socket(server, 4097);
				BufferedReader dataIn  = 
						new BufferedReader(new InputStreamReader(data.getInputStream()));
				PrintStream dataOut = new PrintStream(data.getOutputStream());
				
				dataOut.println(cmd);
				String response = dataIn.readLine();
				
				while (response.length() != 0) {
					System.out.println(response);
					response = dataIn.readLine();	
				}
				
				dataOut.flush();
				data.close();
				break;

			case "RETR" :
				if (args.length == 2) {
					String fileName = args[1];
					
					//EST Data Communication Socket
					data = new Socket(server, 4097);
					InputStream dataIS  = data.getInputStream();
					dataOut = new PrintStream(data.getOutputStream());
					
					dataOut.println(cmd);
					File file = new File("./client_files/" + fileName);
					FileOutputStream fileOut = new FileOutputStream(file);
					
					byte[] b = new byte [1024];
					int amount_read;
					
					while ((amount_read = dataIS.read(b)) != -1) {
						fileOut.write(b, 0, amount_read);
					}
					
					fileOut.close();
					System.out.println("File " + fileName + " retrieved.");
					
					dataOut.flush();
					data.close();
					break;

				} else {
					System.out.println("Syntax error: RETR <filename>");
					break;
				}

			case "STOR" :
				if (args.length == 2) {

					String fileName = args[1];
					fileName = "./client_files/" + fileName;
					
					//EST Data Communication Socket
					data = new Socket(server, 4097);
					dataOut = new PrintStream(data.getOutputStream());
					
					dataOut.println(cmd);
					
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
		            
					dataOut.flush();
					data.close();
					break;

				} else {
					System.out.println("Syntax error: STOR <filename>");
					break;
				}

			case "QUIT" :
				data = new Socket(server, 4097);
				dataOut = new PrintStream(data.getOutputStream());
				
				System.out.println("Ending Session...");
				dataOut.println(cmd);
				
				dataOut.flush();
				data.close();
				input.close();
				socket.close();
				
				System.out.println("Session Ended.");
				System.exit(0);
				break;	
			}
		}
	}
	
	private static void sendFile(FileInputStream fis, OutputStream os) throws Exception {
		System.out.println("Sending File...");
		byte[] buffer = new byte[1024];
		int bytes = 0;
		
		while ((bytes = fis.read(buffer)) != -1) {
			System.out.println("Writing File...");
			os.write(buffer, 0, bytes);
		}
	}
}
