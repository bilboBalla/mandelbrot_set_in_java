// Control.java

package control;
import view.*;
import model.*;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.io.File;
import java.net.URL;


public class Control{

	private View view;
	private Model model;

	public Control(){
		this.construct();
	}

	private void construct(){
		HashMap<String, Double> settings = this.loadSettings();
		this.view = new View(settings);
		this.model = new Model(this.view, settings);
		this.updateView();
		this.addEventHandlers();
	}

	private HashMap<String, Double> loadSettings(){
		HashMap<String, Double> settings = new HashMap<String, Double>();
		try{
			InputStream in = getClass().getResourceAsStream("/defaultSettings.txt"); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String nextLine;
			while ( ( nextLine = reader.readLine() ) != null ){
				String field = nextLine.split(" ")[0];
				String value = nextLine.split(" ")[1];
				settings.put(field, Double.parseDouble(value));
			}
		}catch(IOException e){}
		return settings;
	}

	private void addEventHandlers(){

		this.view.addMouseListenerToMandelbrotDisplay(new MouseListener(){
			@Override public void mouseClicked(MouseEvent e){  
				Control.this.mandelbrotDisplayWasClicked(e);
			}
			@Override public void mouseEntered(MouseEvent e){}
			@Override public void mousePressed(MouseEvent e){}
			@Override public void mouseReleased(MouseEvent e){}
			@Override public void mouseExited(MouseEvent e){}
		});

		this.view.addMouseMotionListenerToMandelbrotDisplay(new MouseMotionListener(){
			@Override public void mouseMoved(MouseEvent e){
				Control.this.mouseMovedInMandelbrotDisplay(e);
			}
			@Override public void mouseDragged(MouseEvent e){}
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

		this.view.addActionToButton("about", new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Control.this.aboutButtonWasClicked();
			}
		});

		this.view.addActionToButton("hideControlPanel", new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Control.this.hideButtonWasClicked();
			}
		});

		this.view.addActionToButton("showControlPanel", new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Control.this.showButtonWasClicked();
			}
		});

		this.view.addKeyListener(new KeyListener(){
			@Override public void keyTyped(KeyEvent e){}
			@Override public void keyPressed(KeyEvent e){}
			@Override public void keyReleased(KeyEvent e){
				Control.this.enterKeyPressed(e);
			}
		});
	}

	private void enterKeyPressed(KeyEvent e){
		if ( e.getKeyCode() == KeyEvent.VK_ENTER )
			Control.this.zoomButtonClicked();
	}

	// For finding the show button when control panel is hidden
	private void mouseMovedInMandelbrotDisplay(MouseEvent e){
		this.view.mouseMovedInMandelbrotDisplay(e);
	}

	private void mandelbrotDisplayWasClicked(MouseEvent e){
		if ( SwingUtilities.isRightMouseButton(e) ){
			Control.this.backButtonWasClicked();
			return;
		}

		this.view.reFocusToMandelbrotDisplay();
		double selectedReal = this.model.mapXToReal(e.getX());
		double selectedImag = this.model.mapYToImag(e.getY());
		this.view.setTextFieldText("zoom_about_real", Double.toString(selectedReal));
		this.view.setTextFieldText("zoom_about_imag", Double.toString(selectedImag));
		this.model.zoomAboutReal = selectedReal;
		this.model.zoomAboutImag = selectedImag;

		if ( e.getClickCount() == 2 ){
			Control.this.zoomButtonClicked();
			return;
		}
	}

	private void saveButtonWasClicked(){
		try{
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showSaveDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
	            File outFile = chooser.getSelectedFile();
	            ImageIO.write(this.model.getImage(), "png", outFile);
	    	}
	    	else throw new Exception();
	    }catch( Exception e){}
	}

	private void backButtonWasClicked(){
		this.model.popToLastImage();
		this.updateView();
	}

	private void boundsButtonClicked(){
		this.view.toggleBoundsVisible();
	}

	private void zoomButtonClicked(){
		if ( this.model.isWorking() ) return;
		if ( ! this.updateModel() ) return;
		this.model.zoom();
		this.updateView();
	}

	private void areaSelected(double top, double bottom, double left, double right){
		if ( this.model.isWorking() ) return;
		if ( ! this.updateModel() ) return;
		this.model.zoom(top,bottom, left, right);
		this.updateView();
	}

	private void aboutButtonWasClicked(){
		JFrame aboutWindow = new JFrame();
		aboutWindow.setTitle("About");
		aboutWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		aboutWindow.setPreferredSize(new Dimension(630, 600));

		String contentString = "";
		try{
			InputStream in = getClass().getResourceAsStream("/view/about/about.html"); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String nextLine;
			while ( ( nextLine = reader.readLine() ) != null ){
				
				if ( nextLine.trim().equals("<!--insert_images_here-->".trim())){
					contentString += this.createImageHTML();
				}else{
					contentString += nextLine; 
				}
			}
		}catch(IOException e){}

		JEditorPane jep = new JEditorPane("text/html", contentString);
		jep.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(jep);
		jep.setCaretPosition(0);
		aboutWindow.add(scrollPane);
		aboutWindow.setResizable(false);
		aboutWindow.pack();
		aboutWindow.setVisible(true);
	}

	/*
		execution from jar file requires that resources be accessed like this
		go figure. Otherwise I would have had all this hard coded into about.html.
		This was the quickest work around to putting images in the about page, even
		though it totally breaks the separation of concerns princpal. oh well.
	*/
	private String createImageHTML(){
		URL url0 = getClass().getResource("/view/about/start.png");
		URL url1 = getClass().getResource("/view/about/one.png");
		URL url2 = getClass().getResource("/view/about/two.png");
		URL url3 = getClass().getResource("/view/about/three.png");
		return (
			"<img src=" + url0 + " width=600 height=375></img><br/>" +
			"<img src=" + url1 + " width=600 height=375></img><br/>" +
			"<img src=" + url2 + " width=600 height=375></img><br/>" +
			"<img src=" + url3 + " width=600 height=375></img><br/>"
		);
	}

	private void hideButtonWasClicked(){
		this.view.hideControlPanel();
		this.view.pack();
	}

	private void showButtonWasClicked(){
		this.view.showControlPanel();
		this.view.pack();
	}

	private boolean updateModel(){
		try{
			this.model.zReal         = Double.parseDouble(this.view.getTextFieldText("z_real"));
			this.model.zImag         = Double.parseDouble(this.view.getTextFieldText("z_imag"));
			this.model.zoomFactor    = Double.parseDouble(this.view.getTextFieldText("zoom_factor"));
			this.model.zoomAboutReal = Double.parseDouble(this.view.getTextFieldText("zoom_about_real"));
			this.model.zoomAboutImag = Double.parseDouble(this.view.getTextFieldText("zoom_about_imag"));
			this.model.hueMultiplier = Float.parseFloat(this.view.getTextFieldText("hue_multiplier"));
			this.model.hueAdder      = Float.parseFloat(this.view.getTextFieldText("hue_adder"));
			this.model.saturation    = Float.parseFloat(this.view.getTextFieldText("saturation"));
			this.model.brightness    = Float.parseFloat(this.view.getTextFieldText("brightness"));
			this.model.setMagnitudeCap(Double.parseDouble(this.view.getTextFieldText("mag_cap")));
			this.model.setIterationCap(Double.parseDouble(this.view.getTextFieldText("itr_cap")));
			this.model.setThreadCount(Double.parseDouble(this.view.getTextFieldText("thread_count")));
		}catch( IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}catch( NumberFormatException e){
			JOptionPane.showMessageDialog(null, "Input Must be Numerical", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private boolean updateView(){
		double areaInView = Math.abs(this.model.realWidth()*this.model.imagHeight());
		DecimalFormat df = new DecimalFormat("0.000E00");
		this.view.setLabelText("area_in_view_value", df.format(areaInView));
		this.view.setTextFieldText("z_real", Double.toString(this.model.zReal));
		this.view.setTextFieldText("z_imag", Double.toString(this.model.zImag));
		this.view.setTextFieldText("mag_cap", Double.toString(this.model.magnitudeCap));
		this.view.setTextFieldText("itr_cap", Integer.toString(this.model.iterationCap));
		this.view.setTextFieldText("thread_count", Integer.toString(this.model.threadCount));
		this.view.setTextFieldText("zoom_about_real", Double.toString(this.model.zoomAboutReal));
		this.view.setTextFieldText("zoom_about_imag", Double.toString(this.model.zoomAboutImag));
		this.view.setTextFieldText("zoom_factor", Double.toString(this.model.zoomFactor));
		this.view.setTextFieldText("hue_multiplier", Float.toString(this.model.hueMultiplier));
		this.view.setTextFieldText("hue_adder", Float.toString(this.model.hueAdder));
		this.view.setTextFieldText("saturation", Float.toString(this.model.saturation));
		this.view.setTextFieldText("brightness", Float.toString(this.model.brightness));
		this.view.setLabelText("top", Double.toString(this.model.getTop()));
		this.view.setLabelText("bottom", Double.toString(this.model.getBottom()));
		this.view.setLabelText("left", Double.toString(this.model.getLeft()));
		this.view.setLabelText("right", Double.toString(this.model.getRight()));
		this.view.setMandelbrotImage(this.model.getImage());
		return true;
	}
}