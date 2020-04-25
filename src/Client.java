import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client 
{
	static PrintStream out = System.out;
	static PrintStream err = System.err;
	static Scanner scan = new Scanner(System.in);
	final static String TERMINATE = "end";
	final static String GAME = "start game";
	
	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter server's IP address (enter \"localhost\" if server is on the same computer): ");
		String ip = sc.nextLine();
		try 
		{
			Socket s = new Socket(ip, 4999); //client starts connection
			
			PrintWriter pr = new PrintWriter(s.getOutputStream());
			InputStreamReader in = new InputStreamReader(s.getInputStream());
			BufferedReader bf = new BufferedReader(in);
			
			boolean SIDE = true;
			ConnectFour cf = new ConnectFour("", ""); //just for initialization
			
			out.print("Enter chat name: ");
			String chatName = scan.nextLine();
			System.out.println("Server: Welcome, " + chatName + ".");
            out.println("Chat started. Enter \"start game\" to start Connect Four game");
			pr.println(chatName);
			pr.flush();
			
			out.print(chatName+": ");
			String str = scan.nextLine();
			if (str.equals(TERMINATE))
			{
				s.close();
			}
			
			pr.println(chatName+": "+str);// prints on server side
			if(str.equals(GAME)) {
				cf = new ConnectFour("Server", chatName);
				System.out.println("System: Game begin! your pieces are black.");
                System.out.println("System: type c# to drop your piece. # is column number.");
				cf.printBoard("Current");
				SIDE = false;
			}
			pr.flush();
			
			String clientStr="", serverStr="";
			while (true)
			{
				serverStr = bf.readLine(); //server message
				if (serverStr==null||serverStr.equals(TERMINATE))
				{
					s.close();
					out.println("SERVER CLOSED");
					System.exit(0);
				}
				out.println("Server: "+ serverStr);
				
				if(serverStr.equals(GAME) && cf.getServerName().length()==0) {
					cf = new ConnectFour(chatName, "Server");
					System.out.println("System: Game begin! your pieces are red.");
                    System.out.println("System: type c# to drop your piece. # is column number.");
					cf.printBoard("Current");
					SIDE = true;
				}else {
					cf.turns(serverStr, !SIDE);
				}
				
				out.print(chatName+": ");
				clientStr = scan.nextLine();
				cf.turns(clientStr, SIDE);
				
				if (clientStr.equals(TERMINATE))
				{
					s.close();
					out.println("CONNECTION CLOSED BY CLIENT");
					System.exit(0);
				}else if(clientStr.equals(GAME) && cf.getServerName().length()==0) {
					cf = new ConnectFour("Server", chatName);
					System.out.println("System: Game begin! your pieces are Black.");
					cf.printBoard("Current");
					SIDE = false;
				}
				
				pr.println(chatName+": "+clientStr);
				pr.flush();
			}
		}
		catch (Throwable e)
		{
			err.println(e);
		}
		sc.close();
		scan.close();

	}

}
