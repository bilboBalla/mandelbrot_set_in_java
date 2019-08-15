// Model.java

package model;
import view.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;
import java.util.List;
import javax.swing.SwingWorker;
import java.awt.Graphics;


public class Model{

	public View view;

	// Algorithm Context
	public double zReal;
	public double zImag;
	public double magnitudeCap;
	public int    iterationCap;
	public int    threadCount;
	public int    numberOfChunks;
	public Vector<Thread> threads;

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

	// Color Context
	public float hueMultiplier;
	public float hueAdder;
	public float saturation;
	public float brightness;

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
		if ( newThreadCount > this.width )
			throw new IOException("Thread Count Must not exceed width in pixels of image");
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

	public boolean isWorking(){
		for ( Thread thread : this.threads )
			if ( thread.isAlive() )
				return true;
			return false;
	}

	public int numberOfThreadsWorking(){
		int count = 0;
		for ( Thread thread : this.threads )
			if ( thread.isAlive() )
				count++;
		return count;
	}

	public void zoom(){
		this.pushToNextImage(
			this.zoomAboutImag + zoomFactor * this.imagHeight()/2,
			this.zoomAboutImag - zoomFactor * this.imagHeight()/2,
			this.zoomAboutReal - zoomFactor * this.realWidth()/2,
			this.zoomAboutReal + zoomFactor * this.realWidth()/2
		);
	}

	public void popToLastImage(){
		if ( this.boundsStack.size() <= 1) return;
		if ( this.imageStack.size() <= 1) return;
		this.boundsStack.pop();
		this.imageStack.pop();
	}

	public void pushToNextImage(double top, double bottom, double left, double right){
		if ( this.imageStack == null )
			this.imageStack = new Stack<BufferedImage>();
		if ( this.boundsStack == null )
			this.boundsStack = new Stack<Vector<Double>>();
		this.imageStack.push(new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB));
		this.boundsStack.push(new Vector<Double>(4));
		this.boundsStack.peek().add(this.TOP, top);
		this.boundsStack.peek().add(this.BOTTOM, bottom);
		this.boundsStack.peek().add(this.LEFT, left);
		this.boundsStack.peek().add(this.RIGHT, right);
		this.generateImage();
	}

	public BufferedImage copyImage(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}

	public Model(View view){
		this.view = view;
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
		this.magnitudeCap = 2;
		this.iterationCap = 10000;
		this.threadCount = 100;
		this.numberOfChunks = 650000;
		this.zoomFactor = 0.5;
		this.zoomAboutReal = 0;
		this.zoomAboutImag = 0;
		this.hueMultiplier = 10f;
		this.hueAdder = 0;
		this.saturation = 0.6f;
		this.brightness = 1.0f;
		this.width = 1000;
		this.height = 625;
		this.pushToNextImage(1.25, -1.25, -2.5, 1.5);
	}


	private void generateImage(){
		SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>(){
			@Override protected Integer doInBackground(){
				Model.this.threads = new Vector<Thread>(Model.this.threadCount);
				int xRangePerThread = Model.this.width/Model.this.threadCount;
				int nextStartingX = 0;
				for ( int i = 0; i < Model.this.threadCount; ++ i ){
					Chunk nextChunk = new Chunk(nextStartingX, nextStartingX + xRangePerThread, 0, Model.this.height);
					Model.this.threads.add(Model.this.setUpThread(nextChunk));
					nextStartingX = nextStartingX + xRangePerThread;
				}
				if ( (Model.this.width % Model.this.threadCount) != 0 ){
					Chunk leftOverChunk = new Chunk(
						Model.this.width - (Model.this.width%Model.this.threadCount), 
						Model.this.width, 
						0,
						Model.this.height
					);
					Model.this.threads.add(Model.this.setUpThread(leftOverChunk));
				}
				for ( Thread thread : Model.this.threads ) {
					thread.start();
					publish(Model.this.numberOfThreadsWorking());
				}
				while(Model.this.isWorking()) publish(Model.this.numberOfThreadsWorking());
				return 0;
			}

			@Override protected void process(List<Integer> chunks){
				float threadsWorking = chunks.get(chunks.size()-1);
				float percentOfThreadsWorking = threadsWorking/(float)Model.this.threadCount*100;
				int percentDone = 100 - (int)percentOfThreadsWorking;
				Model.this.view.setLabelText("progress2", Integer.toString(percentDone)+" %");
				Model.this.view.repaintMandelbrotDisplay();
			}

			@Override protected void done(){
				Model.this.view.setLabelText("progress2", "100 %");
				Model.this.view.repaintMandelbrotDisplay();
			}
			
		};
		worker.execute();
	}


	private class Chunk{
		public int startX;
		public int endX;
		public int startY;
		public int endY;
		public boolean isComputed;

		public Chunk(int startX, int endX, int startY, int endY){
			this.startX = startX;
			this.endX = endX;
			this.startY = startY;
			this.endY = endY;
			this.isComputed = false;
		}
	}

	private Thread setUpThread(Chunk chunk){
		return new Thread(new Runnable(){
			@Override public void run(){
				generatePartialImage(chunk);
			}
		});
	}


	private void generatePartialImage(Chunk chunk){
		double nextImag = this.mapYToImag(chunk.startY);
		double nextReal = this.mapXToReal(chunk.startX);
		for ( int i = chunk.startY; i < chunk.endY; i ++ ){
			for ( int j = chunk.startX; j < chunk.endX; j ++ ){
				Color nextColor = this.mandelbrotAlgorithm(nextReal, nextImag);
				this.imageStack.peek().setRGB(j, i, nextColor.getRGB());
				nextReal += this.pixelWidth();
			}
			nextReal = this.mapXToReal(chunk.startX);
			nextImag -= this.pixelHeight();
		}
	}

	private double logBase2(double number){
		return Math.log(number)/Math.log(2);
	}

	private Color mapColor(int escapeIteration, double zReal, double zImag){
		double absZ = Math.sqrt(zReal*zReal + zImag*zImag);
		if ( absZ <= this.magnitudeCap ) return Color.BLACK;
		double n = escapeIteration + 1 - this.logBase2(this.logBase2(absZ));
		n = n / this.iterationCap;
		return new Color(
			Color.HSBtoRGB(
				(this.hueMultiplier*(float)n)+this.hueAdder,
				this.saturation,
				this.brightness
			)
		);
	}

	private Color mandelbrotAlgorithm(double real, double imag){
		double zR = this.zReal;
		double zI = this.zImag;
		double zRealSqr = zR * zR;
		double zImagSqr = zI * zI;
		double magCapSqr = this.magnitudeCap * this.magnitudeCap;
		
		for (int i = 0; true; i ++){
			if ( (zRealSqr + zImagSqr) >= magCapSqr || i == this.iterationCap){
				return this.mapColor(i, zR, zI);
			}
			zI = 2 * zR * zI + imag;
			zR = zRealSqr - zImagSqr + real;
			zRealSqr = zR * zR;
			zImagSqr = zI * zI;
		}
	}
}