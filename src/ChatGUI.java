
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.net.Socket;
import java.io.*;

public class ChatGUI {

    JFrame newFrame;
    JButton send;
    JTextField messageBox;
    JTextArea chatBox;
    JTextField name;
    JFrame preFrame2=new JFrame("Let's connect to a server");;
    JTextField ipaddrField;
    static boolean preprocessComplete = false;
    Socket s;
    boolean userinput;
    PrintWriter pr;
    boolean SIDE;
    ConnectFour cf;
    boolean goesFirst;
    
    
    
	static PrintStream err = System.err;

	//connect four variables
	final static String TERMINATE = "end";
	final static String GAME = "start game";
	
	
public static void main(String[] args) {
	//create the GUI
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
        e.printStackTrace();
    }
    ChatGUI mainGUI = new ChatGUI();
    mainGUI.processing();
    
    chat(mainGUI);
}


//socket programming- TCP 
public static void chat(ChatGUI gui) 
{
	while (preprocessComplete == false) {
		System.out.println("not done filling in needed chat info");
	
	}
	try 
	{
		//socket stuff
		Socket s = new Socket(gui.ipaddrField.getText(), 4999); //client starts connection
		gui.pr = new PrintWriter(s.getOutputStream());
		InputStreamReader in = new InputStreamReader(s.getInputStream());
		BufferedReader bf = new BufferedReader(in);
		gui.cf = new ConnectFour("", ""); //just for initialization
		
	
		gui.display();
		
		
		gui.chatBox.append("System: Welcome, " + gui.username + ".\n");
		gui.chatBox.append("Chat started. Enter \"start game\" to start Connect Four game\n");
		gui.chatBox.append("Enter \"end\" in order to end the chat.\n");

		
		String serverStr="";
		//instant messages being received from server
		while(true) {
			serverStr = bf.readLine(); //server message
			 if (serverStr==null||serverStr.equals(TERMINATE))
			{
				s.close();
				gui.chatBox.append("SERVER CLOSED THE CHAT");
				System.exit(0);
			}
			 gui.chatBox.append("<Server>: " + serverStr + "\n");

			if(serverStr.equals(GAME)) {
				gui.goesFirst = true;
				gui.cf = new ConnectFour("Player 1", "Server");
				gui.chatBox.append("System: Game begin! your pieces are red.\n");
                gui.chatBox.append("System: type c# to drop your piece. # is column number.\n");
				gui.chatBox.append(gui.cf.printBoard("Current"));
	     	}else {
					String board;
			         if ((board = gui.cf.turns(serverStr, !gui.goesFirst)) != null) {
				            //System.out.println("reached hererfkagwl;rfkaw;rf\n\n\n");
				            gui.chatBox.append(board); 
				            }
					}
				}
	}
	catch (Throwable e)
	{
		err.println(e);
	}

}
		
//method for the initial frame the will get the username and IP address information
public void processing() {
    userinput = false;
	preFrame2.setVisible(false);

    name = new JTextField();
    JLabel chooseName = new JLabel("Pick a username:");
    
    JButton enterServer = new JButton("Enter");

    
    JPanel prePanel = new JPanel(new GridBagLayout());
    
    GridBagConstraints preRight = new GridBagConstraints();
    preRight.anchor = GridBagConstraints.EAST;
    GridBagConstraints preLeft = new GridBagConstraints();
    preLeft.anchor = GridBagConstraints.WEST;
    preRight.weightx = 2.0;
    preRight.fill = GridBagConstraints.HORIZONTAL;
    preRight.gridwidth = GridBagConstraints.REMAINDER;

    prePanel.add(chooseName, preLeft);
    prePanel.add(name, preRight);
    enterServer.addActionListener(new enterServerButtonListener());

    preFrame2.setVisible(true);
    ipaddrField = new JTextField();
 	JLabel ipaddr = new JLabel("Enter the IP address of the server side");
   
    prePanel.add(ipaddr, preLeft);
    prePanel.add(ipaddrField, preRight);
    	
    preFrame2.add(BorderLayout.CENTER, prePanel);
    preFrame2.add(BorderLayout.SOUTH, enterServer);
    preFrame2.setVisible(true);
    preFrame2.setSize(400, 300);

    
}
public void display() {
	//GUI Chat frames
	
	newFrame = new JFrame("Let's Chat!");
	newFrame.setVisible(true);
    JPanel southPanel = new JPanel();
    newFrame.add(BorderLayout.SOUTH, southPanel);
    southPanel.setBackground(Color.BLUE);
    southPanel.setLayout(new GridBagLayout());

    messageBox = new JTextField(30);
    send = new JButton("Send Message");
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
    southPanel.add(send, right);

    chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
    send.addActionListener(new sendMessageButtonListener());
    newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    newFrame.setSize(600, 400);
}
class sendMessageButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
    	
    	if (messageBox.getText().length() < 1) {
    		return;
    	}else if (messageBox.getText().equals("end")) {
        	try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else {
        	//sending an actual message and update chat box
        	String str = messageBox.getText();
            chatBox.append("<" + username + ">:  " + str + "\n");
            messageBox.setText("");
            //send to other side
            pr.println(str);
            
            String board = null;
            if ((board = cf.turns(str, goesFirst)) != null) {
            	//System.out.println("reached hererfkagwl;rfkaw;rf\n\n\n");
            	chatBox.append(board); 
            }
            //check starting a connect four game
			if(str.equals(GAME)) {
				//starting game
				goesFirst = false;
				cf = new ConnectFour("Server", username);

				chatBox.append("System: Game begin! your pieces are black.\n");
				chatBox.append("System: type c# to drop your piece. # is column number.\n");
				chatBox.append(cf.printBoard("Current"));
		}
			//need another if/else for having a game choice and parsing if it's actually your turn or not
        
            pr.flush();
        }
    }
}

String username;
//class in order to get the client's username
class enterServerButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {

    	username = name.getText();
        preFrame2.setVisible(false);
        preprocessComplete = true;
    }

}
}
