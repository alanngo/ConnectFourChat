import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server // Codenamed
{
	static PrintStream out = System.out;
	static PrintStream err = System.err;
	static Scanner scan = new Scanner(System.in);
	final static String TERMINATE = "end";
	final static String GAME = "start game";

	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(4999);
			boolean SIDE = true;
			ConnectFour cf = new ConnectFour("", ""); //just for initialization
			String clientName;

			while (true) {
				out.println("Waiting on client...");
				Socket s = ss.accept(); // client makes connection request
				out.println("Client connected");
                out.println("Chat started. Enter \"start game\" to start Connect Four game");

				InputStreamReader in = new InputStreamReader(s.getInputStream());
				BufferedReader bf = new BufferedReader(in);
				PrintWriter pr = new PrintWriter(s.getOutputStream());
				
				clientName = bf.readLine();
				System.out.println("Client name is " + clientName);
				
				String clientStr = "", serverStr = "";
				while (true) {
					clientStr = bf.readLine(); // client message
					if (clientStr == null || clientStr.equals(TERMINATE)) {
						// reconnection problem
						out.println("CLIENT CLOSED");
						break;
					}
					
					out.println(clientStr);
					String clientString = clientStr.substring(clientName.length() + 2);
					if(clientString.equals(GAME) && cf.getServerName().length()==0) {
						cf = new ConnectFour("Server", clientName);
						System.out.println("System: Game begin! your pieces are red.");
                        System.out.println("System: type c# to drop your piece. # is column number.");
						cf.printBoard("Current");
						SIDE = true;
					} else {
						cf.turns(clientString, !SIDE);
					}
					
					out.print("Me: ");
					serverStr = scan.nextLine();
					cf.turns(serverStr, SIDE);
					
					if (serverStr.equals(TERMINATE)) {
						ss.close();
						out.println("CONNECTION CLOSED BY SERVER");
						System.exit(0);
					}else if(serverStr.equals(GAME) && cf.getServerName().length()==0) {
						cf = new ConnectFour(clientName, "Server");
						System.out.println("System: Game begin! your pieces are Black.");
                        System.out.println("System: type c# to drop your piece. # is column number.");
						cf.printBoard("Current");
						SIDE=false;
					}
					
					pr.println(serverStr);
					pr.flush();
				}
			}
		} catch (Throwable e) {
			err.println(e);
		}

	}

}
