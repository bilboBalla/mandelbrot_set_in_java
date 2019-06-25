// Control.java

package control;
import view.*;
import model.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		System.out.println(e.getX());
		System.out.println();
		System.out.println(e.getY());
	}

	private void saveButtonWasClicked(){
		System.out.println("save");
	}

	private void backButtonWasClicked(){
		System.out.println("back");
	}

	private void boundsButtonClicked(){
		this.view.toggleBoundsVisible();
	}

	private void zoomButtonClicked(){
		/*
		private double zReal;
		private double zImag;
		private double magnitudeCap;

		private int    iterationCap;
		private int    threadCount;
		private double zoomFactor;

		private double zoomAboutReal;
		private double zoomAboutImag;
		*/
		this.model.zReal         = Double.parseDouble(this.view.getTextFieldText("z_real"));
		this.model.zImag         = Double.parseDouble(this.view.getTextFieldText("z_imag"));
		this.model.magnitudeCap  = Double.parseDouble(this.view.getTextFieldText("mag_cap"));
		this.model.iterationCap  = Integer.parseInt(this.view.getTextFieldText("itr_cap"));
		this.model.threadCount   = Integer.parseInt(this.view.getTextFieldText("thread_count"));
		this.model.zoomFactor    = Double.parseDouble(this.view.getTextFieldText("zoom_factor"));
		this.model.zoomAboutReal = Double.parseDouble(this.view.getTextFieldText("zoom_about_real"));
		this.model.zoomAboutImag = Double.parseDouble(this.view.getTextFieldText("zoom_about_imag"));
		this.model.zoom();
		this.model.repaint();
		this.view.setLabelText("")
		this.view.setMandelbrotImage(this.model.getImage());
	}
}