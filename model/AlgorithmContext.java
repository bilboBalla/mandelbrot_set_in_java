// AlgorithmContext.java

package model;

public class AlgorithmContext{

	private double zReal;
	private double zImag;
	private double magnitudeCap;
	private int    iterationCap;

	public double get_z_real(){ return this.zReal; }
	public double get_z_imag(){ return this.zImag; }
	public double get_magnitude_cap(){ return this.magnitudeCap; }
	public int    get_iteration_cap(){ return this.iterationCap; }

	public void set_z_real(double zReal){
		// error checking
		this.zReal = zReal;
	}

	public void set_z_imag(double zImag){
		// error checking
		this.zImag = zImag;
	}

	public void set_magnitude_cap(double magnitudeCap){
		// error checking
		this.magnitudeCap = magnitudeCap;
	}

	public void set_iteration_cap(int iterationCap){
		// error checking
		this.iterationCap = iterationCap;
	}
	
	private static AlgorithmContext instance = null;

	private AlgorithmContext(){
		this.zReal = 0;
		this.zImag = 0;
		this.magnitudeCap = 2;
		this.iterationCap = 100;
	}

	public static AlgorithmContext get_instance(){
		if (instance == null) instance = new AlgorithmContext();
		return instance;
	}
}