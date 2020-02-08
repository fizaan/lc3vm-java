package tiny.vm;

import java.awt.Font;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import window.LCKeyListener;
import window.WindowFrame;

public class CPU {

	public static final String IMAGE_FOLDER="";
	private JTextArea textArea;

	public JTextArea getJTArea() { return textArea; }

	public void setupWindow() {
		JFrame jframe = WindowFrame.getWindow();
		textArea = new JTextArea();
		//textArea.setHorizontalAlignment(SwingConstants.LEFT);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		textArea.addKeyListener(new LCKeyListener());
		textArea.setFont(new Font("Arial", Font.PLAIN, 18));
		jframe.add(textArea);
		jframe.setVisible(true);
	}

	public void loadImage(File file) throws IOException {
		InputStream input = new FileInputStream(file);
	    DataInputStream inst = new DataInputStream(input);
	    if(input.available() % 2 != 0) {
	    	System.out.println("Error: Image file must have even # of bytes\n" +
	    			"Loaded file is: "+input.available()+" bytes");
	    	System.exit(1);
	    }


	    int i = 0;
	    int start=0x3000;
	    while((inst.available())>0) {
	    		if(i++==0) {
	    			inst.readChar();
	    			continue; //we don't want the first char since it's the address (in this VM).
	    		}
	    		VM.memory[start++] = inst.readChar();
	    }
	    inst.close();
	    input.close();
	}


	public static void main(String args[]) throws Exception {
		CPU cpu = new CPU();
		cpu.loadImage(new File(IMAGE_FOLDER+"2048.obj"));
		cpu.setupWindow();
		VM.load(cpu.getJTArea());

	}
}
