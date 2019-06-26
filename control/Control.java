// Control.java

package control;
import view.*;
import model.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JOptionPane;

public class Control{

	private View view;
	private Model model;

	public Control(){
		this.construct();
	}

	private void construct(){
		this.view = new View();
		this.model = new Model();
		this.view.setMandelbrotImage(this.model.getImage());
		this.addEventHandlers();
	}

	private void addEventHandlers(){
		this.view.addMouseListenerToMandelbrotDisplay(new MouseListener(){
			@Override public void mouseClicked(MouseEvent e)
			{  
				Control.this.mandelbrotDisplayWasClicked(e);
			}
			@Override public void mouseEntered(MouseEvent e){}
			@Override public void mousePressed(MouseEvent e){}
			@Override public void mouseReleased(MouseEvent e){}
			@Override public void mouseExited(MouseEvent e){}
		});
		this.view.addActionToButton("bounds", new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Control.this.boundsButtonClicked();
			}
		});
		this.view.addActionToButton("zoom", new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Control.this.zoomButtonClicked();
			}
		});
		this.view.addActionToButton("save", new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Control.this.saveButtonWasClicked();
			}
		});
		this.view.addActionToButton("back", new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Control.this.backButtonWasClicked();
			}
		});
	}

	private void mandelbrotDisplayWasClicked(MouseEvent e){
		double selectedReal = this.model.mapXToReal(e.getX());
		double selectedImag = this.model.mapYToImag(e.getY());
		this.model.zoomAboutReal = selectedReal;
		this.model.zoomAboutImag = selectedImag;
		this.view.setTextFieldText("zoom_about_real", Double.toString(selectedReal));
		this.view.setTextFieldText("zoom_about_imag", Double.toString(selectedImag));
	}

	private void saveButtonWasClicked(){
		System.out.println("save");
	}

	private void backButtonWasClicked(){
		this.model.popToLastImage();
		this.updateViewsBoundsLabels();
		this.view.setMandelbrotImage(this.model.getImage());
	}

	private void boundsButtonClicked(){
		this.view.toggleBoundsVisible();
	}

	private void zoomButtonClicked(){
		try{
			this.model.zReal         = Double.parseDouble(this.view.getTextFieldText("z_real"));
			this.model.zImag         = Double.parseDouble(this.view.getTextFieldText("z_imag"));
			this.model.zoomFactor    = Double.parseDouble(this.view.getTextFieldText("zoom_factor"));
			this.model.zoomAboutReal = Double.parseDouble(this.view.getTextFieldText("zoom_about_real"));
			this.model.zoomAboutImag = Double.parseDouble(this.view.getTextFieldText("zoom_about_imag"));
			this.model.setMagnitudeCap(Double.parseDouble(this.view.getTextFieldText("mag_cap")));
			this.model.setIterationCap(Double.parseDouble(this.view.getTextFieldText("itr_cap")));
			this.model.setThreadCount(Double.parseDouble(this.view.getTextFieldText("thread_count")));
		}catch( IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}catch( NumberFormatException e){
			JOptionPane.showMessageDialog(null, "Input Must be Numerical", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.model.zoom();
		this.updateViewsBoundsLabels();
		this.view.setMandelbrotImage(this.model.getImage());
	}

	private void updateViewsBoundsLabels(){
		this.view.setLabelText("top", Double.toString(this.model.getTop()));
		this.view.setLabelText("bottom", Double.toString(this.model.getBottom()));
		this.view.setLabelText("left", Double.toString(this.model.getLeft()));
		this.view.setLabelText("right", Double.toString(this.model.getRight()));
	}
}