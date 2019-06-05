// App.java

import model.*;
import view.*;
import java.awt.EventQueue;

public class App{

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = Window.get_instance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}