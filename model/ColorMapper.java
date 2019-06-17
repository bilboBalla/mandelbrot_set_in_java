// ColorMapper.java

package model;

import java.awt.Color;

public class ColorMapper{
	
	public Color map(MandelbrotNumber number){
		/*//mu = iterations + 1 - Math.Log(Math.Log(Z.Magnitude)) / log_escape;
		double mu = (
			number.get_iteration_at_divergence() + 1 - Math.log(Math.log(number.get_magnitude_at_divergence())) / Math.log(AlgorithmContext.get_magnitude_cap())
		);*/
		if ( number.get_iteration_at_divergence() == AlgorithmContext.get_instance().get_iteration_cap()){
			return Color.BLACK;
		}
		return Color.WHITE;
	}

	private static ColorMapper instance = null;

	private ColorMapper(){

	}

	public static ColorMapper get_instance(){
		if ( instance == null ) instance = new ColorMapper();
		return instance;
	}
}