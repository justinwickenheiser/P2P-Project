import java.io.*;
import java.net.*;
import java.util.*;

public class CentralServer {

	public CentralServer(int listenPort) throws IOException {
		// Establish the listen socket.
		ServerSocket serverListenSocket = new ServerSocket(listenPort);
		ServerSocket dataListenSocket = new ServerSocket(4097);
		while (true) {
			// Listen for a TCP connection request.
			Socket clientSocket = serverListenSocket.accept();

			Handler handlerForClient = new Handler(clientSocket, dataListenSocket);
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