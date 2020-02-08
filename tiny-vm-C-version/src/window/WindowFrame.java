package window;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class WindowFrame {
	public static JFrame getWindow() {
		JFrame jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jframe.setLocation(dim.width/2-jframe.getSize().width/2,
				dim.height/2-jframe.getSize().height/2);
		jframe.setSize(600, 450);
		return jframe;
	}
}