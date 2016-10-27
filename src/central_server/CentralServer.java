import java.io.*;
import java.net.*;
import java.util.*;

public class CentralServer {

	public CentralServer(int listenPort) throws IOException {
		// Establish the listen socket.
		ServerSocket serverListenSocket = new ServerSocket(listenPort);
		
		while (true) {
			// Listen for a TCP connection request.
			Socket clientSocket = serverListenSocket.accept();

			BufferedReader in = 
					new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
			// DataOutputStream out = 
			// 		new DataOutputStream(clientSocket.getOutputStream());

			Handler handlerForClient = new Handler(clientSocket);
			handlerForClient.start();
		}
	}

	public static void main (String args[]) throws IOException { 
		if (args.length != 1) {
			throw new RuntimeException ("Syntax: java CentralServer <port>"); 
		}
		new CentralServer(Integer.parseInt (args[0])); 
	}
	
}