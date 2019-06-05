// MandelbrotNumber.java

package model;

public class MandelbrotNumber{
	
	private double real;
	private double imag;
	private double magnitudeAtDivergence;
	private int    iterationAtDivergence;

	private double zRealSqrAtDivergence;
	private double zImagSqrAtDivergence;

	public double get_real(){ return this.real; }
	public double get_imag(){ return this.imag; }
	public double get_magnitude_at_divergence(){ return this.magnitudeAtDivergence; }
	public double get_real_sqr_at_divergence(){ return this.zRealSqrAtDivergence; }
	public double get_imag_sqr_at_divergence(){ return this.zImagSqrAtDivergence; }
	public int    get_iteration_at_divergence(){ return this.iterationAtDivergence; }


	public MandelbrotNumber(double real, double imag){
		this.real = real;
		this.imag = imag;
		this.mandelbrot_algorithm();
	}

	private void mandelbrot_algorithm(){
		AlgorithmContext context = AlgorithmContext.get_instance();
		double zReal = context.get_z_real();
		double zImag = context.get_z_imag();
		double zRealSqr = zReal * zReal;
		double zImagSqr = zImag * zImag;
		double magCap = context.get_magnitude_cap();
		double magCapSqr = magCap * magCap;
		int    itrCap = context.get_iteration_cap();
		
		for (int i = 0; true; i ++){
			if ( (zRealSqr + zImagSqr) >= magCapSqr || i == itrCap){
				this.iterationAtDivergence = i;
				this.magnitudeAtDivergence = Math.sqrt(zRealSqr + zImagSqr);
				this.zRealSqrAtDivergence = zRealSqr;
				this.zRealSqrAtDivergence = zImagSqr;
				break;
			}
			zImag = 2 * zReal * zImag + imag;
			zReal = zRealSqr - zImagSqr + real;
			zRealSqr = zReal * zReal;
			zImagSqr = zImag * zImag;
		}
	}

	@Override
	public String toString(){
		return (
			Double.toString(this.real) + 
			" + " +
			Double.toString(this.imag) +
			"i, (M= " +
			Double.toString(this.magnitudeAtDivergence) +
			", D= " + 
			Double.toString(this.iterationAtDivergence) +
			")"
		);
	}
}