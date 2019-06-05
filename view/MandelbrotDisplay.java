// MandelbrotDisplay.java

package view;
import control.*;
import model.*;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import javax.swing.SwingConstants;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;


public class MandelbrotDisplay extends JPanel{
	
	private JLabel top;
	private JLabel bottom;
	private JLabel left;
	private JLabel right;

	public void set_bounds_labels(
		String top, 
		String bottom, 
		String left, 
		String right)
	{
		this.top.setText(top);
		this.bottom.setText(bottom);
		this.left.setText(left);
		this.right.setText(right);
	}

	public void toggle_hide_show_bounds_labels(){
		this.top.setVisible(! this.top.isVisible());
		this.bottom.setVisible(! this.bottom.isVisible());
		this.left.setVisible(! this.left.isVisible());
		this.right.setVisible(! this.right.isVisible());
	}

	private void init_listeners(){
		this.addMouseListener(new MouseListener(){
			@Override public void mouseClicked(MouseEvent e)
			{  
				Controller.get_instance().mandelbrot_display_was_clicked(e);
			}
			@Override public void mouseEntered(MouseEvent e){}
			@Override public void mousePressed(MouseEvent e){}
			@Override public void mouseReleased(MouseEvent e){}
			@Override public void mouseExited(MouseEvent e){}
		});
	}

	@Override public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(MandelbrotImage.get_instance().get_image(), null, null);
	}

	private void construct(){
		this.setLayout(new BorderLayout());
		this.setPreferredSize(
			new Dimension(
				MandelbrotImage.get_instance().get_width(), 
				MandelbrotImage.get_instance().get_height()
			)
		);
		
		this.buildBorderLabels();
		this.add(this.top, BorderLayout.NORTH);
		this.add(this.bottom, BorderLayout.SOUTH);
		this.add(this.left, BorderLayout.WEST);
		this.add(this.right, BorderLayout.EAST);
		this.top.setHorizontalAlignment(SwingConstants.CENTER);
		this.bottom.setHorizontalAlignment(SwingConstants.CENTER);
		this.init_listeners();
	}

	private void buildBorderLabels(){
		this.top = new JLabel(new Double(ComplexFrame.get_instance().get_top()).toString());
		this.bottom = new JLabel(new Double(ComplexFrame.get_instance().get_bottom()).toString());
		this.left = new JLabel(new Double(ComplexFrame.get_instance().get_left()).toString());
		this.right = new JLabel(new Double(ComplexFrame.get_instance().get_right()).toString());
	}

	private static MandelbrotDisplay instance = null;

	private MandelbrotDisplay(){
		super();
		this.construct();
	}

	public static MandelbrotDisplay get_instance(){
		if ( instance == null ) instance = new MandelbrotDisplay();
		return instance;
	}
}