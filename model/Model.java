// Model.java

package model;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;
import javax.swing.SwingWorker;


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
	private Stack<Vector<Double>> boundsStack;
	private final int TOP    = 0;
	private final int BOTTOM = 1;
	private final int LEFT   = 2;
	private final int RIGHT  = 3;
	public double zoomFactor;
	public double zoomAboutReal;
	public double zoomAboutImag;

	// Image Context
	private int width;
	private int height;
	private Stack<BufferedImage> imageStack;

	// Getters
	public double getTop(){return this.boundsStack.peek().get(this.TOP);}
	public double getBottom(){return this.boundsStack.peek().get(this.BOTTOM);}
	public double getLeft(){return this.boundsStack.peek().get(this.LEFT);}
	public double getRight(){return this.boundsStack.peek().get(this.RIGHT);}

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

	public double mapYToImag(int y){
		return this.getTop() - y*this.pixelHeight();
	}

	public double mapXToReal(int x){
		return this.getLeft() + x*this.pixelWidth();
	}

	public BufferedImage getImage(){
		return this.imageStack.peek();
	}

	public void zoom(){
		double w = this.getRight() - this.getLeft();
		double h = this.getTop() - this.getBottom();
		Vector<Double> newBounds = new Vector<Double>(4);
		newBounds.add(this.TOP, this.zoomAboutImag + zoomFactor * h/2);
		newBounds.add(this.BOTTOM, this.zoomAboutImag - zoomFactor * h/2);
		newBounds.add(this.LEFT, this.zoomAboutReal - zoomFactor * w/2);
		newBounds.add(this.RIGHT, this.zoomAboutReal + zoomFactor * w/2);
		this.boundsStack.push(newBounds);
		this.pushBlankImageOntoStack();
		this.generateImage();
	}

	public void popToLastImage(){
		if ( this.boundsStack.size() == 1) return;
		this.boundsStack.pop();
		this.imageStack.pop();
	}

	public Model(){
		this.construct();
	}

	private double realWidth(){
		return this.getRight() - this.getLeft();
	}

	private double imagHeight(){
		return this.getTop() - this.getBottom();
	}

	private double pixelWidth(){
		return this.realWidth() / (this.width-1);

	}

	private double pixelHeight(){
		return this.imagHeight() / (this.height-1);
	}

	private void construct(){
		this.zReal = 0;
		this.zImag = 0;
		this.magnitudeCap = 100;
		this.iterationCap = 10000;
		this.threadCount = 4;
		this.boundsStack = new Stack<Vector<Double>>();
		this.boundsStack.push(new Vector<Double>(4));
		this.boundsStack.peek().add(this.TOP,     1.25);
		this.boundsStack.peek().add(this.BOTTOM, -1.25);
		this.boundsStack.peek().add(this.LEFT,   -2.5);
		this.boundsStack.peek().add(this.RIGHT,   1.5);
		this.zoomFactor = 0.5;
		this.zoomAboutReal = 0;
		this.zoomAboutImag = 0;
		this.width = 1000;
		this.height = 625;
		this.imageStack = new Stack<BufferedImage>();
		this.pushBlankImageOntoStack();
		this.generateImage();
	}

	private void pushBlankImageOntoStack(){
		this.imageStack.push(
			new BufferedImage(
				this.width, 
				this.height, 
				BufferedImage.TYPE_INT_RGB
			)
		);
	}

	private void generateImage(){
		long startTime = System.nanoTime();

		Vector<Thread> threads = new Vector<Thread>(this.threadCount);
		final int xRangePerThread = this.width/this.threadCount;
		int nextStartingX = 0;
		for ( int i = 0; i < this.threadCount; ++ i ){
			final int nextStartingXFinal = nextStartingX;
			Thread nextThread = new Thread(new Runnable(){
				@Override public void run(){
					Model.this.generatePartialImage(
						nextStartingXFinal
						, nextStartingXFinal + xRangePerThread
					);
				}
			});
			nextStartingX = nextStartingX + xRangePerThread;
			threads.add(nextThread);
		}
		if ( (this.width % this.threadCount) != 0 ){
			System.out.println("there was a remainder");
			final int startX = this.width - (this.width%this.threadCount);
			final int endX = this.width;
			threads.add(new Thread(new Runnable(){
				@Override public void run(){
					Model.this.generatePartialImage(startX, endX);
				}
			}));
		}
		for ( Thread thread : threads ) thread.start();
		try{
			for ( Thread thread : threads ) thread.join();
		}catch ( InterruptedException e ){}

		long endTime = System.nanoTime();
		double total = (double)(endTime - startTime)/1000000000;
		System.out.println(total);
	}

	private void generatePartialImage(int startX, int endX){
		System.out.println("indeed, execution is here");
		double nextImag = this.getTop();
		double nextReal = this.mapXToReal(startX);
		for ( int i = 0; i < this.height; i ++ ){
			for ( int j = startX; j < endX; j ++ ){
				int escapeIteration = this.mandelbrotAlgorithm(nextReal, nextImag);
				Color nextColor = this.mapColor(escapeIteration);
				this.imageStack.peek().setRGB(j, i, nextColor.getRGB());
				nextReal += this.pixelWidth();
			}
			nextReal = this.mapXToReal(startX);
			nextImag -= this.pixelHeight();
		}
	}

	private Color mapColor(int escapeIteration){
		if (escapeIteration == this.iterationCap)
			return Color.RED;
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
				return i;
			}
			zI = 2 * zR * zI + imag;
			zR = zRealSqr - zImagSqr + real;
			zRealSqr = zR * zR;
			zImagSqr = zI * zI;
		}
	}
}