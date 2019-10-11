// View.java

package view;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Point;
import java.util.HashMap;

public class View extends JFrame{
	
	private boolean boundsAreVisible;
	private HashMap<String, Double> settings;
	private BufferedImage mandelbrotImage;
	private JPanel mandelbrotDisplay;
	private JPanel controlPanel;
	private HashMap<String, JLabel> labels;
	private HashMap<String, JButton> buttons;
	private HashMap<String, JTextField> textFields;
	private int textFieldLength = 5;

	public void hideControlPanel(){
		this.controlPanel.setVisible(false);
	}

	public void showControlPanel(){
		this.controlPanel.setVisible(true);
		this.buttons.get("showControlPanel").setVisible(false);
	}

	public void addMouseListenerToMandelbrotDisplay(MouseListener listener){
		this.mandelbrotDisplay.addMouseListener(listener);
	}

	public void addMouseMotionListenerToMandelbrotDisplay(MouseMotionListener listener){
		this.mandelbrotDisplay.addMouseMotionListener(listener);
	}

	public void setMandelbrotImage(BufferedImage newMandelbrotImage){
		this.mandelbrotImage = newMandelbrotImage;
		this.mandelbrotDisplay.repaint();
	}

	public void addActionToButton(String button, ActionListener listener){
		this.buttons.get(button).addActionListener(listener);
	}

	@Override
	public void addKeyListener(KeyListener listener){
		this.mandelbrotDisplay.addKeyListener(listener);
	}

	public void setTextFieldText(String textField, String text){
		this.textFields.get(textField).setText(text);
	}

	public String getTextFieldText(String textField){
		return this.textFields.get(textField).getText();
	}

	public void setLabelText(String label, String text){
		this.labels.get(label).setText(text);
	}

	public void toggleBoundsVisible(){
		this.labels.get("top").setVisible(!this.boundsAreVisible);
		this.labels.get("bottom").setVisible(!this.boundsAreVisible);
		this.labels.get("left").setVisible(!this.boundsAreVisible);
		this.labels.get("right").setVisible(!this.boundsAreVisible);
		this.boundsAreVisible = !this.boundsAreVisible;
	}

	// to be added to an event listener by the Control class
	public void mouseMovedInMandelbrotDisplay(MouseEvent e){
		if ( this.controlPanel.isVisible() ) return;
		int triggerWidth = this.mandelbrotDisplay.getWidth()-70;
		int triggerHeight = this.mandelbrotDisplay.getHeight()-40;
		Point mousePosition = e.getPoint();
		boolean mouseInRange = mousePosition.getX()>triggerWidth && mousePosition.getY()>triggerHeight;
		if ( mouseInRange ) this.buttons.get("showControlPanel").setVisible(true);
		else this.buttons.get("showControlPanel").setVisible(false);
	}

	public void repaintMandelbrotDisplay(){
		this.mandelbrotDisplay.repaint();
	}

	public void reFocusToMandelbrotDisplay(){
		this.mandelbrotDisplay.requestFocusInWindow();
	}


	public View(HashMap<String, Double> settings){
		super();
		this.settings = settings;
		this.construct();
	}

	private void construct(){
		this.setTitle("The Mandelbrot Set");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		this.buildHashMaps();
		this.buildMandelbrotDisplay();
		this.buildControlPanel();
		this.pack();
		this.setVisible(true);
		this.setResizable(false);
		this.addWindowFocusListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		        View.this.reFocusToMandelbrotDisplay();
		    }
		});
	}

	private void buildHashMaps(){
		this.buildButtons();
		this.buildLabels();
		this.buildTextFields();
	}

	private void buildButtons(){
		this.buttons = new HashMap<String,JButton>();
		this.buttons.put("zoom", new JButton("Zoom"));
		this.buttons.put("bounds", new JButton("Bounds"));
		this.buttons.put("back", new JButton("Back"));
		this.buttons.put("save", new JButton("Save Image"));
		this.buttons.put("about", new JButton("About"));
		this.buttons.put("hideControlPanel", new JButton("Hide"));
		this.buttons.put("showControlPanel", new JButton("Show"));
	}

	private void buildLabels(){
		this.labels = new HashMap<String,JLabel>();
		this.labels.put("top", new JLabel(""));
		this.labels.put("bottom", new JLabel(""));
		this.labels.put("left", new JLabel(""));
		this.labels.put("right", new JLabel(""));
		this.labels.put("z_real", new JLabel("Z Real"));
		this.labels.put("z_imag", new JLabel("Z Imag"));
		this.labels.put("mag_cap", new JLabel("Mag. Cap"));
		this.labels.put("itr_cap", new JLabel("Itr. Cap"));
		this.labels.put("thread_count", new JLabel("Thread Count"));
		this.labels.put("zoom_factor", new JLabel("Zoom Factor"));
		this.labels.put("zoom_about_real", new JLabel("Zoom About R"));
		this.labels.put("zoom_about_imag", new JLabel("Zoom About I"));
		this.labels.put("progress1", new JLabel("progress"));
		this.labels.put("progress2", new JLabel("progress"));
		this.labels.put("hue_multiplier", new JLabel("hue Mltplr"));
		this.labels.put("hue_adder", new JLabel("hue adder"));
		this.labels.put("saturation", new JLabel("saturation"));
		this.labels.put("brightness", new JLabel("brightness"));
		this.labels.put("area_in_view", new JLabel("area in view: "));
		this.labels.put("area_in_view_value", new JLabel());
		this.labels.put("showControlPanelHolder", new JLabel());
	}

	private void buildTextFields(){
		this.textFields = new HashMap<String, JTextField>();
		this.textFields.put("z_real", new JTextField(this.textFieldLength));
		this.textFields.put("z_imag", new JTextField(this.textFieldLength));
		this.textFields.put("mag_cap", new JTextField(this.textFieldLength));
		this.textFields.put("itr_cap", new JTextField(this.textFieldLength));
		this.textFields.put("thread_count", new JTextField(this.textFieldLength));
		this.textFields.put("zoom_factor", new JTextField(this.textFieldLength));
		this.textFields.put("zoom_about_real", new JTextField(this.textFieldLength));
		this.textFields.put("zoom_about_imag", new JTextField(this.textFieldLength));
		this.textFields.put("hue_multiplier", new JTextField(this.textFieldLength));
		this.textFields.put("hue_adder", new JTextField(this.textFieldLength));
		this.textFields.put("saturation", new JTextField(this.textFieldLength));
		this.textFields.put("brightness", new JTextField(this.textFieldLength));
	}

	private void addComponentsToMandelbrotDisplay(){
		JLabel r0c0fill = new JLabel();
		this.mandelbrotDisplay.add(r0c0fill);
		this.mandelbrotDisplay.add(this.labels.get("top"));
		JLabel r0c2fill = new JLabel();
		this.mandelbrotDisplay.add(r0c2fill);
		this.mandelbrotDisplay.add(this.labels.get("left"));
		JLabel r1c1fill = new JLabel();
		this.mandelbrotDisplay.add(r1c1fill);
		this.mandelbrotDisplay.add(this.labels.get("right"));
		JLabel r2c0fill = new JLabel();
		this.mandelbrotDisplay.add(r2c0fill);
		this.mandelbrotDisplay.add(this.labels.get("bottom"));
		JLabel r2c2fill = this.labels.get("showControlPanelHolder");
		this.mandelbrotDisplay.add(r2c2fill);
		this.boundsAreVisible = true;
	}

	private void alignBoundsLabels(){
		this.labels.get("top").setHorizontalAlignment(SwingConstants.CENTER);
		this.labels.get("top").setVerticalAlignment(SwingConstants.TOP);
		this.labels.get("right").setHorizontalAlignment(SwingConstants.RIGHT);
		this.labels.get("bottom").setHorizontalAlignment(SwingConstants.CENTER);
		this.labels.get("bottom").setVerticalAlignment(SwingConstants.BOTTOM);
	}

	private void setUpShowControlPanelButton(){
		JLabel bottomRight = this.labels.get("showControlPanelHolder");
		JLabel container = new JLabel();
		bottomRight.setLayout(new BorderLayout());
		container.setLayout(new BorderLayout());
		container.setText(" ");
		bottomRight.add(container, BorderLayout.SOUTH);
		container.add(this.buttons.get("showControlPanel"), BorderLayout.EAST);
		this.buttons.get("showControlPanel").setVisible(false);
	}

	private void initMandelbrotDisplayAndPaintingBehavior(){
		this.mandelbrotDisplay = new JPanel(){
			@Override public void paintComponent(Graphics g){
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.drawImage(View.this.mandelbrotImage, null, null);
			}
		};
		this.mandelbrotDisplay.setLayout(new GridLayout(3, 3));
		this.mandelbrotDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.mandelbrotDisplay.setPreferredSize(
			new Dimension(
				(int)this.settings.get("width").doubleValue(), 
				(int)this.settings.get("height").doubleValue()
			)
		);
	}

	private void buildMandelbrotDisplay(){
		this.initMandelbrotDisplayAndPaintingBehavior();
		this.addComponentsToMandelbrotDisplay();
		this.alignBoundsLabels();
		this.setUpShowControlPanelButton();
		this.getContentPane().add(this.mandelbrotDisplay, BorderLayout.WEST);
	}

	private void buildControlPanel(){
		JPanel contPnl = new JPanel();
		contPnl.setLayout(new GridLayout(0,2));
		contPnl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contPnl.add(this.labels.get("area_in_view"));
		contPnl.add(this.labels.get("area_in_view_value"));
		contPnl.add(this.labels.get("z_real"));
		contPnl.add(this.textFields.get("z_real"));
		contPnl.add(this.labels.get("z_imag"));
		contPnl.add(this.textFields.get("z_imag"));
		contPnl.add(this.labels.get("mag_cap"));
		contPnl.add(this.textFields.get("mag_cap"));
		contPnl.add(this.labels.get("itr_cap"));
		contPnl.add(this.textFields.get("itr_cap"));
		contPnl.add(this.labels.get("thread_count"));
		contPnl.add(this.textFields.get("thread_count"));
		contPnl.add(this.labels.get("zoom_factor"));
		contPnl.add(this.textFields.get("zoom_factor"));
		contPnl.add(this.labels.get("zoom_about_real"));
		contPnl.add(this.textFields.get("zoom_about_real"));
		contPnl.add(this.labels.get("zoom_about_imag"));
		contPnl.add(this.textFields.get("zoom_about_imag"));
		contPnl.add(this.labels.get("hue_multiplier"));
		contPnl.add(this.textFields.get("hue_multiplier"));
		contPnl.add(this.labels.get("hue_adder"));
		contPnl.add(this.textFields.get("hue_adder"));
		contPnl.add(this.labels.get("saturation"));
		contPnl.add(this.textFields.get("saturation"));
		contPnl.add(this.labels.get("brightness"));
		contPnl.add(this.textFields.get("brightness"));
		contPnl.add(this.buttons.get("zoom"));
		contPnl.add(this.buttons.get("back"));
		contPnl.add(this.buttons.get("save"));
		contPnl.add(this.buttons.get("bounds"));
		contPnl.add(this.buttons.get("about"));
		contPnl.add(this.buttons.get("hideControlPanel"));
		contPnl.add(this.labels.get("progress1"));
		contPnl.add(this.labels.get("progress2"));
		this.labels.get("progress1").setHorizontalAlignment(SwingConstants.CENTER);
		this.controlPanel = contPnl;
		this.getContentPane().add(this.controlPanel, BorderLayout.EAST);
	}
}