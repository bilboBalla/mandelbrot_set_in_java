// Main.java

import model.*;
import view.*;
import control.*;
import java.awt.EventQueue;

public class Main{

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Control control = new Control();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}