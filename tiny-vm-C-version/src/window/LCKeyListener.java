package window;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import tiny.vm.VM;

public class LCKeyListener extends KeyAdapter {
	
	public static boolean poll = true;
	
    @Override
    public void keyPressed(KeyEvent event) {
    	char key = event.getKeyChar();
    	poll = false;
    	VM.mem_write(VM.MR_KBSR, (1 << 15));
		VM.mem_write(VM.MR_KBDR, key);
		
    }
}