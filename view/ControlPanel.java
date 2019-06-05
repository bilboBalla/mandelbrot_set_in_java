// ControlPanel.java

package view;

import control.*;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class ControlPanel extends JPanel{

	private JButton zoomButton;
	private JButton hideShowBounds;
	private JLabel percentComputed;

	public void set_percent_computed(int percent){
		this.percentComputed.setText(new Integer(percent).toString()+"% done");
		this.percentComputed.repaint();
	}

	public void toggle_hide_show_bounds_button(){
		if (this.hideShowBounds.getText() == "hide bounds")
			this.hideShowBounds.setText("show bounds");
		else
			this.hideShowBounds.setText("hide bounds");
	}
	private void construct(){
		this.setLayout(new GridLayout(0, 1));

		this.zoomButton = new JButton("zoom");
		this.zoomButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Controller.get_instance().zoom_button_clicked(e);
			}
		});

		this.hideShowBounds = new JButton("hide bounds");
		this.hideShowBounds.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Controller.get_instance().hide_show_bounds_button_pressed(e);
			}
		});

		this.percentComputed = new JLabel("test");


		this.add(AlgorithmSettings.get_instance());
		this.add(ZoomSettings.get_instance());
		this.add(this.hideShowBounds);
		this.add(this.zoomButton);
		this.add(this.percentComputed);
	}

	public static ControlPanel instance = null;

	public static ControlPanel get_instance(){
		if ( instance == null ) instance = new ControlPanel();
		return instance;
	}

	private ControlPanel(){
		super();
		this.construct();
	}
}