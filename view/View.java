// View.java

package view;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class View extends JFrame{
	
	private boolean boundsAreVisible;
	private BufferedImage mandelbrotImage;
	private JPanel mandelbrotDisplay;
	private HashMap<String, JLabel> labels;
	private HashMap<String, JButton> buttons;
	private HashMap<String, JTextField> textFields;
	private int textFieldLength = 5;

	public void addMouseListenerToMandelbrotDisplay(MouseListener listener){
		this.mandelbrotDisplay.addMouseListener(listener);
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

	public void repaintMandelbrotDisplay(){
		this.mandelbrotDisplay.repaint();
	}

	public void reFocusToMandelbrotDisplay(){
		this.mandelbrotDisplay.requestFocusInWindow();
	}


	public View(){
		super();
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
	}

	private void buildLabels(){
		this.labels = new HashMap<String,JLabel>();
		this.labels.put("top", new JLabel("1.25"));
		this.labels.put("bottom", new JLabel("-1.25"));
		this.labels.put("left", new JLabel("-2.5"));
		this.labels.put("right", new JLabel("1.5"));
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
		this.labels.put("hue", new JLabel("hue"));
		this.labels.put("saturation", new JLabel("saturation"));
		this.labels.put("brightness", new JLabel("brightness"));
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
		this.textFields.put("hue", new JTextField(this.textFieldLength));
		this.textFields.put("saturation", new JTextField(this.textFieldLength));
		this.textFields.put("brightness", new JTextField(this.textFieldLength));
	}

	private void buildMandelbrotDisplay(){
		JPanel manDisp = new JPanel(){
			@Override public void paintComponent(Graphics g){
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.drawImage(View.this.mandelbrotImage, null, null);
			}
		};
		manDisp.setLayout(new BorderLayout());
		manDisp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		manDisp.setPreferredSize(new Dimension(1000, 625));
		manDisp.add(this.labels.get("top"), BorderLayout.NORTH);
		manDisp.add(this.labels.get("bottom"), BorderLayout.SOUTH);
		manDisp.add(this.labels.get("left"), BorderLayout.WEST);
		manDisp.add(this.labels.get("right"), BorderLayout.EAST);
		this.labels.get("top").setHorizontalAlignment(SwingConstants.CENTER);
		this.labels.get("bottom").setHorizontalAlignment(SwingConstants.CENTER);
		this.mandelbrotDisplay = manDisp;
		this.boundsAreVisible = true;
		this.getContentPane().add(this.mandelbrotDisplay, BorderLayout.WEST);
	}

	private void buildControlPanel(){
		JPanel contPnl = new JPanel();
		contPnl.setLayout(new GridLayout(0,2));
		contPnl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
		contPnl.add(this.labels.get("hue"));
		contPnl.add(this.textFields.get("hue"));
		contPnl.add(this.labels.get("saturation"));
		contPnl.add(this.textFields.get("saturation"));
		contPnl.add(this.labels.get("brightness"));
		contPnl.add(this.textFields.get("brightness"));
		contPnl.add(this.buttons.get("back"));
		contPnl.add(this.buttons.get("save"));
		contPnl.add(this.buttons.get("bounds"));
		contPnl.add(this.buttons.get("zoom"));
		contPnl.add(this.labels.get("progress1"));
		contPnl.add(this.labels.get("progress2"));
		this.labels.get("progress1").setHorizontalAlignment(SwingConstants.CENTER);
		this.getContentPane().add(contPnl, BorderLayout.EAST);
	}
}