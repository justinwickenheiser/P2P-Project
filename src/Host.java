import java.io.*;
import java.net.*;
import java.util.*;

public class Host {

	public static void main(String[] args) throws IOException {
		if (args.length != 2) { // Test for correct # of args
			throw new IllegalArgumentException("Parameter(s): <Server> <port>");
		}

		String server = args[0];
		int serverPort = new Integer(args[1]).intValue();

		// Create socket that is connected to server on specified port
		Socket socket = new Socket(server, serverPort);
		BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		OutputStream out = socket.getOutputStream();

		System.out.println("Connecting..." + "\nServer: " + server + "\nPort: " + serverPort);
	}
	
	
}