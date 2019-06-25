// Model.java

package model;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.IOException;


public class Model{

	// Algorithm Context
	public double zReal;
	public double zImag;
	public double magnitudeCap;
	public int    iterationCap;
	public int    threadCount;

	// Zoom Context
	private double top;
	private double bottom;
	private double left;
	private double right;
	public double zoomFactor;
	public double zoomAboutReal;
	public double zoomAboutImag;

	// Image Context
	private int width;
	private int height;
	private BufferedImage image;

	// Getters
	public double getTop(){return this.top;}
	public double getBottom(){return this.bottom;}
	public double getLeft(){return this.left;}
	public double getRight(){return this.right;}

	// Setters
	public void setIterationCap(double newIterationCap) throws IOException{
		if ( newIterationCap <= 0 || newIterationCap != Math.floor(newIterationCap)) 
			throw new IOException("Iteration Cap Must be Integer > 0");
		this.iterationCap = (int)newIterationCap;
	}

	public void setThreadCount(double newThreadCount) throws IOException{
		if ( newThreadCount < 1 || newThreadCount != Math.floor(newThreadCount)) 
			throw new IOException("Thread Count Must be Integer >= 1");
		this.threadCount = (int)newThreadCount;
	}

	public void setMagnitudeCap(double newMagnitudeCap) throws IOException{
		if ( newMagnitudeCap < 0) throw new IOException("Magnitude Cap Must be Integer >= 0");
		this.magnitudeCap = newMagnitudeCap;
	}

	public double realWidth(){
		return this.right - this.left;
	}

	public double imagHeight(){
		return this.top - this.bottom;
	}

	public double pixelWidth(){
		return this.realWidth() / (this.width-1);

	}

	public double pixelHeight(){
		return this.imagHeight() / (this.height-1);
	}

	public double mapYToImag(int y){
		return this.top - y*this.pixelHeight();
	}

	public double mapXToReal(int x){
		return this.left + x*this.pixelWidth();
	}

	public BufferedImage getImage(){
		return this.image;
	}

	public void repaint(){
		this.generateImage();
	}

	public void zoom(){
		double w = this.right - this.left;
		double h = this.top - this.bottom;
		this.top = this.zoomAboutImag + zoomFactor * h/2;
		this.bottom = this.zoomAboutImag - zoomFactor * h/2;
		this.right = this.zoomAboutReal + zoomFactor * w/2;
		this.left = this.zoomAboutReal - zoomFactor * w/2;
	}

	public Model(){
		this.construct();
	}

	private void construct(){
		this.zReal = 0;
		this.zImag = 0;
		this.magnitudeCap = 100;
		this.iterationCap = 100;
		this.threadCount = 1;
		this.top = 1.25;
		this.bottom = -1.25;
		this.left = -2.5;
		this.right = 1.5;
		this.zoomFactor = 1;
		this.zoomAboutReal = 0;
		this.zoomAboutImag = 0;
		this.width = 1000;
		this.height = 625;
		this.image = new BufferedImage(
			this.width, 
			this.height, 
			BufferedImage.TYPE_INT_RGB
		);
		this.generateImage();
	}

	private void generateImage(){
		double nextImag = this.top;
		double nextReal = this.left;
		for ( int i = 0; i < this.height; i ++ ){
			for ( int j = 0; j < this.width; j ++ ){
				int escapeIteration = this.mandelbrotAlgorithm(nextReal, nextImag);
				Color nextColor = this.mapColor(escapeIteration);
				this.image.setRGB(j, i, nextColor.getRGB());
				nextReal += this.pixelWidth();
			}
			nextReal = this.left;
			nextImag -= this.pixelHeight();
		}
	}

	private Color mapColor(int escapeIteration){
		if (escapeIteration == this.iterationCap)
			return Color.BLACK;
		return Color.WHITE;
	}

	private int mandelbrotAlgorithm(double real, double imag){
		double zR = this.zReal;
		double zI = this.zImag;
		double zRealSqr = zR * zR;
		double zImagSqr = zI * zI;
		double magCapSqr = this.magnitudeCap * this.magnitudeCap;
		
		for (int i = 0; true; i ++){
			if ( (zRealSqr + zImagSqr) >= magCapSqr || i == this.iterationCap){
				//System.out.println(i);
				return i;
			}
			zI = 2 * zR * zI + imag;
			zR = zRealSqr - zImagSqr + real;
			zRealSqr = zR * zR;
			zImagSqr = zI * zI;
		}
	}
}