import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.*;

public class ChatFrame extends JFrame {

	private static final long serialVersionUID = -8716022739348960951L;
	
	private ChatPanel panel;
	private JTextArea area;
	public ChatFrame(String title)
	{
		super(title);
		panel = new ChatPanel();
		area = new JTextArea();
		
		//add action functionality
		panel.addChatListener(
				new ChatListener()
				{
					@Override
					public void sendEventOccured(SendEvent s) 
					{
						String tmp = panel.getBufferText();
						area.append(tmp);
						panel.clearBuffer();
					}
					
				});
		
		//add to panel
		Container c = getContentPane();
		c.add(panel, BorderLayout.SOUTH);
		c.add(area, BorderLayout.CENTER);
	}

}
