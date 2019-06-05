// AlgorithmSettings.java

package view;

import model.*;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import java.awt.Color;

public class AlgorithmSettings extends JPanel{

	private JTextField zReal;
	private JTextField zImag;
	private JTextField magnitudeCap;
	private JTextField iterationCap;

	public String get_z_real_text(){ return zReal.getText(); }
	public String get_z_imag_text(){ return zImag.getText(); }
	public String get_magnitude_cap_text(){ return magnitudeCap.getText(); }
	public String get_iteration_cap_text(){ return iterationCap.getText(); }

	public void set_z_real_text(String text){
		this.zReal.setText(text);
	}

	public void set_z_imag_text(String text){
		this.zImag.setText(text);
	}

	public void set_magnitude_cap_text(String text){
		this.magnitudeCap.setText(text);
	}

	public void set_iteration_cap_text(String text){
		this.iterationCap.setText(text);
	}

	private void construct(){
		JLabel zRealLabel = new JLabel("zReal");
		this.zReal = new JTextField(
			new Double(AlgorithmContext.get_instance().get_z_real()).toString(), 5
		);

		JLabel zImagLabel = new JLabel("zImag");
		this.zImag = new JTextField(
			new Double(AlgorithmContext.get_instance().get_z_imag()).toString(), 5
		);

		JLabel magnitudeCapLabel = new JLabel("magCap");
		this.magnitudeCap = new JTextField(
			new Double(AlgorithmContext.get_instance().get_magnitude_cap()).toString(), 5
		);

		JLabel iterationCapLabel = new JLabel("itrCap");
		this.iterationCap = new JTextField(
			new Integer(AlgorithmContext.get_instance().get_iteration_cap()).toString(), 5
		);
	
		this.setLayout(new GridLayout(0, 2));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.add(zRealLabel);
		this.add(zReal);
		this.add(zImagLabel);
		this.add(zImag);
		this.add(magnitudeCapLabel);
		this.add(magnitudeCap);
		this.add(iterationCapLabel);
		this.add(iterationCap);
	}
	
	private static AlgorithmSettings instance = null;

	private AlgorithmSettings(){
		super();
		this.construct();
	}

	public static AlgorithmSettings get_instance(){
		if ( instance == null ) instance = new AlgorithmSettings();
		return instance;
	}
}