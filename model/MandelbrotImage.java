// MandelbrotImage.java

package model;

import view.*;
import control.*;
import java.awt.image.BufferedImage;

public class MandelbrotImage{
	
	private MandelbrotNumber[][] grid;
	private BufferedImage image;
	private int width;
	private int height;

	public void render(){
		this.generate_grid();
	}

	public BufferedImage get_image(){ return this.image; }
	public int get_width(){ return this.width; }
	public int get_height(){ return this.height; }

	public double pixel_x_to_real(int x){
		return this.grid[0][x].get_real();
	}
	public double pixel_y_to_imag(int y){
		return this.grid[y][0].get_imag();
	}

	private void construct(){
		this.width = 1000;
		this.height = 625;
		this.image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		this.grid = new MandelbrotNumber[this.height][this.width];
		this.generate_grid();
	}

	private void generate_grid(){
		ComplexFrame complexFrame = ComplexFrame.get_instance();
		double pixelWidth = complexFrame.get_frame_width() / (this.width-1);
		double pixelHeight = complexFrame.get_frame_height() / (this.height-1);
		double nextImag = complexFrame.get_top();
		double nextReal = complexFrame.get_left();
		for ( int i = 0; i < this.height; i ++ ){
			for ( int j = 0; j < this.width; j ++ ){
				this.grid[i][j] = new MandelbrotNumber(nextReal, nextImag);
				nextReal += pixelWidth;
				this.image.setRGB(j, i, ColorMapper.get_instance().map(this.grid[i][j]).getRGB());
			}
			nextReal = complexFrame.get_left();
			nextImag -= pixelHeight;
		}
	}

	private void update_progress(int row, int column){
		int totalPixels = this.width * this.height;
		int pixelsComputedSoFar = row * this.width + column;
		int percentComputed = (int)(100 * pixelsComputedSoFar/totalPixels + 1);
		ControlPanel.get_instance().set_percent_computed(percentComputed);
	}

	private static MandelbrotImage instance = null;

	private MandelbrotImage(){
		this.construct();
	}

	public static MandelbrotImage get_instance(){
		if ( instance == null ) instance = new MandelbrotImage();
		return instance;
	}

	@Override
	public String toString(){
		String string = "";
		for (int i = 0; i < this.height; ++i){
			for (int j = 0; j < this.width; ++ j)
				if ( grid[i][j].get_iteration_at_divergence() == AlgorithmContext.get_instance().get_iteration_cap())
					string += "*";
				else string += " ";
			string += "\n";
		}
		return string;
	}
}