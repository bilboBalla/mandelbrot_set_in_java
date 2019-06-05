// ComplexFrame.java

package model;

public class ComplexFrame{

	private double top;
	private double bottom;
	private double left;
	private double right;

	private double zoomFactor;
	private double zoomAboutReal;
	private double zoomAboutImag;

	public double get_top(){ return this.top; }
	public double get_bottom(){ return this.bottom; }
	public double get_left(){ return this.left; }
	public double get_right(){ return this.right; }

	public double get_frame_width(){ return this.right - this.left; }
	public double get_frame_height(){ return this.top - this.bottom; }

	public double get_zoom_factor(){ return this.zoomFactor; }
	public double get_zoom_about_real(){ return this.zoomAboutReal; }
	public double get_zoom_about_imag(){ return this.zoomAboutImag; }

	public void set_zoom_factor(double zoomFactor){
		// error checking
		this.zoomFactor = zoomFactor;
	}

	public void set_zoom_about_real(double zoomAboutReal){
		// error checking
		this.zoomAboutReal = zoomAboutReal;
	}

	public void set_zoom_about_imag(double zoomAboutImag){
		// error checking
		this.zoomAboutImag = zoomAboutImag;
	}

	public void zoom(double zoomFactor){
		double width = this.get_frame_width();
		double height = this.get_frame_height();
		this.top = this.zoomAboutImag + zoomFactor * height/2;
		this.bottom = this.zoomAboutImag - zoomFactor * height/2;
		this.right = this.zoomAboutReal + zoomFactor * width/2;
		this.left = this.zoomAboutReal - zoomFactor * width/2;
	}
	
	private static ComplexFrame instance = null;

	private void construct(){
		this.top = 1.25;
		this.bottom = -1.25;
		this.left = -2.5;
		this.right = 1.5;
	}

	private ComplexFrame(){
		this.construct();
	}

	public static ComplexFrame get_instance(){
		if (instance == null) instance = new ComplexFrame();
		return instance;
	}
}