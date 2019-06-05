// ZoomSettings.java

package view;

import model.*;
import control.*;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.GridLayout;



public class ZoomSettings extends JPanel{
	
	private JTextField zoomFactor;
	private JTextField zoomAboutReal;
	private JTextField zoomAboutImag;

	public String get_zoom_factor_text(){ return this.zoomFactor.getText(); }
	public String get_zoom_about_real_text(){ return this.zoomAboutReal.getText(); }
	public String get_zoom_about_imag_text(){ return this.zoomAboutImag.getText(); }

	public void set_zoom_factor_text(String text){
		this.zoomFactor.setText(text);
	}

	public void set_zoom_about_real_text(String text){
		this.zoomAboutReal.setText(text);
	}

	public void set_zoom_about_imag_text(String text){
		this.zoomAboutImag.setText(text);
	}

	private void construct(){
		JLabel zoomFactorLabel = new JLabel("zoomFactor");
		this.zoomFactor = new JTextField("1", 5);

		JLabel zoomAboutRealLabel = new JLabel("zoomAboutReal");
		this.zoomAboutReal = new JTextField("0", 5);

		JLabel zoomAboutImagLabel = new JLabel("zoomAboutImag");
		this.zoomAboutImag = new JTextField("0", 5);

		this.setLayout(new GridLayout(0, 2));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.add(zoomFactorLabel);
		this.add(zoomFactor);
		this.add(zoomAboutRealLabel);
		this.add(zoomAboutReal);
		this.add(zoomAboutImagLabel);
		this.add(zoomAboutImag);
	}

	private ZoomSettings(){
		super();
		this.construct();
	}

	private static ZoomSettings instance = null;

	public static ZoomSettings get_instance(){
		if ( instance == null ) instance = new ZoomSettings();
		return instance;
	}
}