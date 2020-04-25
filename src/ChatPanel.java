/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ChatPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	
	private GridBagConstraints gc;
	private JButton sendButton;
	private JTextField buffer;

    public ChatPanel() 
    {
    	super();
    	setBorder(BorderFactory.createTitledBorder("Type anything here"));
    	sendButton = new JButton("Send");
    	buffer = new JTextField();
    	
    	//action listener
    	sendButton.addActionListener(
    			new ActionListener()
    			{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						fireSendEvent(new SendEvent(this));
					}	
    			});
    	
    	Dimension size = getPreferredSize();
    	size.width = 450;
    	buffer.setPreferredSize(size);
    	gc = new GridBagConstraints();
    	
    	gc.weightx=.5;
    	gc.weighty=.5;
    	
    	gc.gridx = 0;
    	gc.gridy=0;
    	add(buffer, gc);
    	
    	gc.gridx = 1;
    	gc.gridy=0;
    	add(sendButton, gc);
    }//end of constructor

    public void fireSendEvent(SendEvent s)
    {
    	Object [] listeners = listenerList.getListenerList();
    	for (int i =0; i<listeners.length; i+=2)
    	{
    		if (listeners[i]==ChatListener.class)
    		{
    			((ChatListener)listeners[i+1]).sendEventOccured(s);
    		}
    	}
    }
    
    public void addChatListener(ChatListener listener)
    {
    	listenerList.add(ChatListener.class, listener);
    }
    
    public String getBufferText()
    {
    	return buffer.getText();
    }
    public void clearBuffer()
    {
    	buffer.setText("");
    }
}//end of class
