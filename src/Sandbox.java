import javax.swing.JFrame;

public class Sandbox {

	public static void main(String[] args) 
	{
		JFrame f = new ChatFrame("Chat Room");
		f.setSize(600, 400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

}
