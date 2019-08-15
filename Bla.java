import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
/*w w  w  . j  av a 2 s  . c  o m*/
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Bla {

  public static void main(String[] args) {
    JLabel label = new JLabel("Hello");
    label.setOpaque(true);
    label.setBackground(Color.red);

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(label, BorderLayout.LINE_END);

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
    mainPanel.setPreferredSize(new Dimension(400, 400));

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(mainPanel);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}