import java.io.IOException;

import javax.swing.JFrame;

public class Runner extends JFrame
{
	private static final long serialVersionUID = 1493771701578729880L;
	
	private GUI graphics;
	
	public static void main(String[] args) throws IOException
	{
		@SuppressWarnings("unused")
		Runner runner = new Runner();
	}
	
	public Runner() throws IOException
	{
		super("Seven Wonders");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1500, 1000);
		setLocationRelativeTo(null);
		setVisible(true);
		
		graphics = new GUI();
		
		add(graphics);
		setContentPane(graphics);
		pack();
	}
}
