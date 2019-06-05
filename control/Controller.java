// Controller.java

package control;
import model.*;
import view.*;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Controller{

	public void mandelbrot_display_was_clicked(MouseEvent e){
		MandelbrotImage mandelbrotImage = MandelbrotImage.get_instance();
		double selectedReal = mandelbrotImage.pixel_x_to_real(e.getX());
		double selectedImag = mandelbrotImage.pixel_y_to_imag(e.getY());
		String realString = new Double(selectedReal).toString();
		String imagString = new Double(selectedImag).toString();
		ZoomSettings.get_instance().set_zoom_about_real_text(realString);
		ZoomSettings.get_instance().set_zoom_about_imag_text(imagString);
	}

	public void hide_show_bounds_button_pressed(ActionEvent e){
		MandelbrotDisplay.get_instance().toggle_hide_show_bounds_labels();
		ControlPanel.get_instance().toggle_hide_show_bounds_button();
	}

	public void zoom_button_clicked(ActionEvent e){
		AlgorithmSettings algorithmSettings = AlgorithmSettings.get_instance();
		AlgorithmContext algorithmContext = AlgorithmContext.get_instance();
		algorithmContext.set_z_real(new Double(algorithmSettings.get_z_real_text()));
		algorithmContext.set_z_imag(new Double(algorithmSettings.get_z_imag_text()));
		algorithmContext.set_magnitude_cap(new Double(algorithmSettings.get_magnitude_cap_text()));
		algorithmContext.set_iteration_cap(new Integer(algorithmSettings.get_iteration_cap_text()));

		ComplexFrame complexFrame = ComplexFrame.get_instance();
		ZoomSettings zoomSettings = ZoomSettings.get_instance();
		double zoomFactor = new Double(zoomSettings.get_zoom_factor_text());
		double zoomAboutReal = new Double(zoomSettings.get_zoom_about_real_text());
		double zoomAboutImag = new Double(zoomSettings.get_zoom_about_imag_text());
		ComplexFrame.get_instance().set_zoom_about_real(zoomAboutReal);
		ComplexFrame.get_instance().set_zoom_about_imag(zoomAboutImag);
		ComplexFrame.get_instance().zoom(zoomFactor);
		
		MandelbrotDisplay.get_instance().set_bounds_labels(
			new Double(complexFrame.get_top()).toString(),
			new Double(complexFrame.get_bottom()).toString(),
			new Double(complexFrame.get_left()).toString(),
			new Double(complexFrame.get_right()).toString()
		);
		
		MandelbrotImage.get_instance().render();
		MandelbrotDisplay.get_instance().repaint();
	}
	
	private void construct(){

	}

	private static Controller instance = null;

	private Controller(){
		this.construct();
	}

	public static Controller get_instance(){
		if ( instance == null ) instance = new Controller();
		return instance;
	}
}