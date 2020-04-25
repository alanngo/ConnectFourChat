import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class ChatServer // Codenamed
{
	JFrame newFrame = new JFrame("Connect Four Chat");
	JButton sendMessage;
	JTextField messageBox;
	JTextArea chatBox;
	PrintWriter pr;
	Socket s;
	static PrintStream out = System.out;
	static PrintStream err = System.err;
	static Scanner scan = new Scanner(System.in);
	final static String TERMINATE = "end";
	final static String GAME = "start game";
	static ConnectFour cf;
	static boolean goesFirst;
	public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ChatServer mainGUI = new ChatServer();
        mainGUI.display();
		
		try {
			//socket programming
			ServerSocket ss = new ServerSocket(4999);
			
			goesFirst = true;
			cf = new ConnectFour("", ""); //just for initialization
			String clientStr = "";
			
			mainGUI.s = ss.accept(); // client makes connection request
			
			mainGUI.chatBox.append("System: Client connect.\n");
			InputStreamReader in = new InputStreamReader(mainGUI.s.getInputStream());
			BufferedReader bf = new BufferedReader(in);
			mainGUI.pr = new PrintWriter(mainGUI.s.getOutputStream());
			
			//keep checking for messages
			while (true) {
				
				clientStr = bf.readLine(); // client message
				System.out.println("received");
				if (clientStr == null || clientStr.equals(TERMINATE)) 
				{
					mainGUI.chatBox.append("CLIENT CLOSED");
					ss.close();
					break;
	
				} 
				else if(clientStr.equals(GAME)) 
				{
					mainGUI.chatBox.append("Client:" + clientStr + "\n");				
					cf = new ConnectFour("Server", "Client");
					mainGUI.chatBox.append("System: Game begin! your pieces are red.\n");
                    mainGUI.chatBox.append("System: type c# to drop your piece. # is column number.\n");
					mainGUI.chatBox.append(cf.printBoard("Current"));
					goesFirst = true;
				} 
				else 
				{
					mainGUI.chatBox.append("<Client>:" + clientStr + "\n");				
			        String board = null;
			        if ((board = ChatServer.cf.turns(clientStr, !goesFirst)) != null) 
			        {
			            //System.out.println("reached hererfkagwl;rfkaw;rf\n\n\n");
			            mainGUI.chatBox.append(board); 
			        }
				}
			}
			
		} catch (Throwable e) {
			err.println(e);
		}

	}

    public void display() {
        newFrame.setVisible(true);
        JPanel southPanel = new JPanel();
        newFrame.add(BorderLayout.SOUTH, southPanel);
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        sendMessage = new JButton("Send Message");
        chatBox = new JTextArea();
        chatBox.setEditable(false);
        newFrame.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        chatBox.setLineWrap(true);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.WEST;
        GridBagConstraints right = new GridBagConstraints();
        right.anchor = GridBagConstraints.EAST;
        right.weightx = 2.0;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        sendMessage.addActionListener(new sendMessageButtonListener());
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(600, 400);
    }


class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
        	
        	if (messageBox.getText().length() < 1) {
        		return;
        	} else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            }else if (messageBox.getText().equals("end")) {
            	try {
    				s.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
            }
            else {
            	String str= messageBox.getText();
                chatBox.append("<" + "Server" + ">:  " + messageBox.getText() + "\n");
                messageBox.setText("");
                pr.println(str);
                String board;
                if ((board = cf.turns(str, goesFirst)) != null ) {
                	chatBox.append(board);
                }
    			if(str.equals(GAME)) {
					cf = new ConnectFour("Player", "Server");
    				chatBox.append("System: Game begin! your pieces are black.\n");
    				chatBox.append("System: type c# to drop your piece. # is column number.\n");
    				chatBox.append(cf.printBoard("Current"));
    				goesFirst = false;
    		}
            
                pr.flush();
            }
        }
    }
}



