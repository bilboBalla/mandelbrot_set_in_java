// Window.java

package view;

import model.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class Window extends JFrame{

	private void construct(){
		this.setTitle("The Mandelbrot Set");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel contents = new JPanel();

		contents.setLayout(new BorderLayout());
		contents.add(MandelbrotDisplay.get_instance(), BorderLayout.WEST);
		contents.add(ControlPanel.get_instance(), BorderLayout.EAST);

		this.setContentPane(contents);
		this.pack();
		this.setVisible(true);
		this.setResizable(false);
	}

	private static Window instance = null;

	private Window(){
		super();
		this.construct();
	}

	public static Window get_instance(){
		if ( instance == null ) instance = new Window();
		return instance;
	}
}