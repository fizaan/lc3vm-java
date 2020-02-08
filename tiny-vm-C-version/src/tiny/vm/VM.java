package tiny.vm;

import java.util.Scanner;
import javax.swing.JTextArea;
import window.LCKeyListener;


public class VM {
	
	public static int[] memory = new int[0xFFFF];
	private static Scanner input = new Scanner(System.in); 
	
	public static final boolean TROUBLE_SHOOT=false; 

	/* Registers */
	
	private static int R_R0 = 0;
	private static int R_R1 = 1;
	private static int R_R2 = 2;
	private static int R_R3 = 3;
    private static int R_R4 = 4;
	private static int R_R5 = 5;
	private static int R_R6 = 6;
	private static int R_R7 = 7;
	private static int R_PC = 8; /* program counter */
	private static int R_COND = 9;
	private static int R_COUNT = 10;


	/* Opcodes */
	public static final int OP_BR = 0; /* branch */
	public static final int OP_ADD = 1;    /* add  */
	public static final int OP_LD = 2;    /* load */
	public static final int OP_ST = 3; /* store */
	public static final int OP_JSR = 4; /* jump register */
	public static final int OP_AND = 5;    /* bitwise and */
	public static final int OP_LDR = 6;    /* load register */
	public static final int OP_STR = 7;    /* store register */
	public static final int OP_RTI = 8;    /* unused */
	public static final int OP_NOT = 9;    /* bitwise not */
	public static final int OP_LDI = 10;    /* load indirect */
	public static final int OP_STI = 11;    /* store indirect */
	public static final int OP_JMP = 12;    /* jump */
	public static final int OP_RES = 13;    /* reserved (unused) */
	public static final int OP_LEA = 14;    /* load effective address */
	public static final int OP_TRAP = 15;    /* execute trap */

	/* Condition Flags */
	private static int  FL_POS = 1 << 0; /* P */
	private static int FL_ZRO = 1 << 1; /* Z */
	private static int FL_NEG = 1 << 2; /* N */

	/* Memory Mapped Registers */
	public static int MR_KBSR = 0xFE00; /* keyboard status */
	public static int MR_KBDR = 0xFE02;  /* keyboard data */


	/* TRAP Codes */
	
	public static final int TRAP_GETC = 0x20;  /* get character from keyboard, not echoed onto the terminal */
	public static final int TRAP_OUT = 0x21;   /* output a character */
	public static final int TRAP_PUTS = 0x22;  /* output a word string */
	public static final int TRAP_IN = 0x23;    /* get character from keyboard, echoed onto the terminal */
	public static final int TRAP_PUTSP = 0x24; /* output a byte string */
	public static final int TRAP_HALT = 0x25;   /* halt the program */


	/* Register Storage */
	private static int[] reg = new int[R_COUNT];


	/* Functions */
	/* Sign Extend */
	private static int sign_extend(char x, int bit_count)
	{
		//rpendleton
		char m = (char) (1 << (bit_count - 1));
        x &= (1 << bit_count) - 1;
        return (x ^ m) - m;
		
		
		//if (((x >> (bit_count - 1)) & 1) == 1) {
	    //    x |= (0xFFFF << bit_count);
	    //}
	    //return x;
		
		//char pcoffset = offsetExtract(x,bit_count);
		
		/*boolean isSet =  isNeg(x, 16 - bit_count + 1);
		if(isSet)
			return getNegativeValue(x,bit_count);
		else
			return x;*/
	}
	
	public static int maskBy(int n) {
		int k = 0;
		for(int i=1;i<=n;i++)
			k += Math.pow(2,(n - i));
		return k;
		
		//e.g. mask by 5 bits = operand & 0b11111 (0b1111 = 31)
	}
	
	public static int getNegativeValue(char operand,int maskbits) {
		operand = (char) ~ operand;
		operand = (char) (operand & maskBy(maskbits));
		operand = (char) (operand + 1);
		return (operand * -1);
	}
	
	public static boolean isNeg(char x,int startindex) {
		return bitExtracted(x,startindex,startindex) == 1;
	}
	
	//endindex is inclusive. so 8-16 will grab the last 9-bits.
	public static char bitExtracted(char c, int startindex, int endindex) {
		int numberofbits = endindex - startindex + 1;
		return (char) (c >> (16 - endindex) &  maskBy(numberofbits));
	}

	/* Update Flags */
	private static void update_flags(int r)
	{
	    if (reg[r] == 0)
	    {
	        reg[R_COND] = FL_ZRO;
	    }
	    else if (reg[r] >> 15 == 1) /* a 1 in the left-most bit indicates negative */
	    {
	        reg[R_COND] = FL_NEG;
	    }
	    else
	    {
	        reg[R_COND] = FL_POS;
	    }
	}


	/* Memory Access */
	public static void mem_write(int address, int val)
	{
	    memory[address] = val;
	}
	
	private static boolean keepPolling() {
		return LCKeyListener.poll;
	}
	
	private static int mem_read(int address) {
		
		if (address == MR_KBSR)
	    {
	        while (keepPolling())
	        {	//System.out.println("Polling...");
	        	//System.out.println(memory[MR_KBSR]);
	        	//System.out.println(memory[MR_KBDR]);
	            //memory[MR_KBSR] = (1 << 15);
	            //memory[MR_KBDR] = getchar();
	        	Thread.currentThread();
		    	   try {
		    		   Thread.sleep(500);
		    	   } catch (InterruptedException e) {
		    		   e.printStackTrace();
		    	   }
	        }
	        //else
	        //{
	        //    memory[MR_KBSR] = 0;
	        //}
	    }
		else
			LCKeyListener.poll = true;
		
		//if(!keepPolling()) {
	    //	System.out.println(memory[MR_KBSR]);
		//    System.out.println(memory[MR_KBDR]);
	    //}
		
	    return memory[address];
	}

	/*private static int mem_read(int address) {	
		if (address == MR_KBSR) {
	       while(keepPolling()) {
	    	   Thread.currentThread();
	    	   try {
	    		   Thread.sleep(200);
	    	   } catch (InterruptedException e) {
	    		   e.printStackTrace();
	    	   }
	       } 
		}	
		else 
			LCKeyListener.poll = true;
	    
		if(!keepPolling()) {
	    	System.out.println(memory[MR_KBSR]);
		    System.out.println(memory[MR_KBDR]);
	    }
		
		return memory[address];
	}*/
	
	
	public static void printRegValues(int instruction) {
		String valbin = Integer.toBinaryString(instruction);
		String valhex = Integer.toHexString(instruction).toUpperCase();
		System.out.print(valbin+": "+valhex+": ");
		for(int i=0;i<R_COUNT;i++)
			System.out.print(reg[i]+",");
		System.out.println();
	}


	/* Main Loop */

	public static void load(JTextArea jt)
	{	/* set the PC to starting position */
	    /* 0x3000 is the default */
	    int PC_START = 0x3000;
	    reg[R_PC] = PC_START;

	    boolean running = true;
	    while (running)
	    {
	        /* FETCH */
	        int instr = mem_read(reg[R_PC]++);
	        int op = instr >> 12;

	        switch (op)
	        {
	            case OP_ADD:
	                /* ADD */
	                {
	                    /* destination register (DR) */
	                    int r0 = (instr >> 9) & 0x7;
	                    /* first operand (SR1) */
	                    int r1 = (instr >> 6) & 0x7;
	                    /* whether we are in immediate mode */
	                    int imm_flag = (instr >> 5) & 0x1;

	                    if (imm_flag==1)
	                    {
	                        int imm5 = sign_extend((char) (instr & 0x1F), 5);
	                        reg[r0] = reg[r1] + imm5;
	                    }
	                    else
	                    {
	                        int r2 = instr & 0x7;
	                        reg[r0] = reg[r1] + reg[r2];
	                    }

	                    update_flags(r0);
	                }

	                break;
	            case OP_AND:
	                /* AND */
	                {
	                    int r0 = (instr >> 9) & 0x7;
	                    int r1 = (instr >> 6) & 0x7;
	                    int imm_flag = (instr >> 5) & 0x1;

	                    if (imm_flag==1)
	                    {
	                        int imm5 = sign_extend((char) (instr & 0x1F), 5);
	                        reg[r0] = reg[r1] & imm5;
	                    }
	                    else
	                    {
	                        int r2 = instr & 0x7;
	                        reg[r0] = reg[r1] & reg[r2];
	                    }
	                    update_flags(r0);
	                }

	                break;
	            case OP_NOT:
	                /* NOT */
	                {
	                    int r0 = (instr >> 9) & 0x7;
	                    int r1 = (instr >> 6) & 0x7;

	                    reg[r0] = ~reg[r1];
	                    update_flags(r0);
	                }

	                break;
	            case OP_BR:
	                /* BR */
	                {
	                    int pc_offset = sign_extend((char) ((instr) & 0x1ff), 9);
	                    int cond_flag = (instr >> 9) & 0x7;
	                    if ((cond_flag & reg[R_COND])==1)
	                    {
	                        reg[R_PC] += pc_offset;
	                    }
	                }

	                break;
	            case OP_JMP:
	                /* JMP */
	                {
	                    /* Also handles RET */
	                    int r1 = (instr >> 6) & 0x7;
	                    reg[R_PC] = reg[r1];
	                }

	                break;
	            case OP_JSR:
	                /* JSR */
	                {
	                    int r1 = (instr >> 6) & 0x7;
	                    int long_pc_offset = sign_extend((char) (instr & 0x7ff), 11);
	                    int long_flag = (instr >> 11) & 1;

	                    reg[R_R7] = reg[R_PC];
	                    if (long_flag==1)
	                    {
	                        reg[R_PC] += long_pc_offset;  /* JSR */
	                    }
	                    else
	                    {
	                        reg[R_PC] = reg[r1]; /* JSRR */
	                    }
	                }

	                break;
	            case OP_LD:
	                /* LD */
	                {
	                    int r0 = (instr >> 9) & 0x7;
	                    int pc_offset = sign_extend((char) (instr & 0x1ff), 9);
	                    reg[r0] = mem_read(reg[R_PC] + pc_offset);
	                    update_flags(r0);
	                }

	                break;
	            case OP_LDI:
	                /* LDI */
	                {
	                    /* destination register (DR) */
	                    int r0 = (instr >> 9) & 0x7;
	                    /* PCoffset 9*/
	                    int pc_offset = sign_extend((char) (instr & 0x1ff), 9);
	                    /* add pc_offset to the current PC, look at that memory location to get the final address */
	                    reg[r0] = mem_read(mem_read(reg[R_PC] + pc_offset));
	                    update_flags(r0);
	                }

	                break;
	            case OP_LDR:
	                /* LDR */
	                {
	                    int r0 = (instr >> 9) & 0x7;
	                    int r1 = (instr >> 6) & 0x7;
	                    int offset = sign_extend((char) (instr & 0x3F), 6);
	                    reg[r0] = mem_read(reg[r1] + offset);
	                    update_flags(r0);
	                }

	                break;
	            case OP_LEA:
	                /* LEA */
	                {
	                    int r0 = (instr >> 9) & 0x7;
	                    int pc_offset = sign_extend((char) (instr & 0x1ff), 9);
	                    reg[r0] = reg[R_PC] + pc_offset;
	                    update_flags(r0);
	                }

	                break;
	            case OP_ST:
	                /* ST */
	                {
	                    int r0 = (instr >> 9) & 0x7;
	                    int pc_offset = sign_extend((char) (instr & 0x1ff), 9);
	                    mem_write(reg[R_PC] + pc_offset, reg[r0]);
	                }

	                break;
	            case OP_STI:
	                /* STI */
	                {
	                    int r0 = (instr >> 9) & 0x7;
	                    int pc_offset = sign_extend((char) (instr & 0x1ff), 9);
	                    mem_write(mem_read(reg[R_PC] + pc_offset), reg[r0]);
	                }

	                break;
	            case OP_STR:
	                /* STR */
	                {
	                    int r0 = (instr >> 9) & 0x7;
	                    int r1 = (instr >> 6) & 0x7;
	                    int offset = sign_extend((char) (instr & 0x3F), 6);
	                    mem_write(reg[r1] + offset, reg[r0]);
	                }

	                break;
	            case OP_TRAP:
	                /* TRAP */
	                switch (instr & 0xFF)
	                {
	                    case TRAP_GETC:
	                        /* TRAP GETC */
	                        /* read a single ASCII char */
	                		String c = input.next();
	                        reg[R_R0] = c.charAt(0);

	                        break;
	                    case TRAP_OUT:
	                        /* TRAP OUT */
	                    	jt.setText("" + ((char) reg[R_R0]));

	                        break;
	                    case TRAP_PUTS:
	                        /* TRAP PUTS */
	                    	StringBuilder sb = new StringBuilder();
	                    	sb.append(jt.getText());
	                    	int address = reg[R_R0];
	                		for(int i = address;;i++) {
	                			char val = (char) memory[i];
	                			if(val==0)
	                				break;
	                			//if(!TROUBLE_SHOOT)
	                				//System.out.print(val);
	                			
	                			sb.append(val);
	                		}
	                		
	                		jt.setText(sb.toString());

	                        break;
	                    case TRAP_IN:
	                        /* TRAP IN */
	                		c = input.next();
	                        reg[R_R0] = c.charAt(0);

	                        break;
	                    case TRAP_PUTSP:
	                    	sb = new StringBuilder();
	                    	sb.append(jt.getText());
	                    	address = reg[R_R0];
	                		for(int i = address;;i++) {
	                			char val = (char) memory[i];
	                			if(val==0)
	                				break;
	                			//if(!TROUBLE_SHOOT)
	                			//	System.out.print(val);
	                			
	                			sb.append(val);
	                		}
	                		
	                		jt.setText(sb.toString());
	                		
	                        break;
	                    case TRAP_HALT:
	                    	System.out.println("HALT");
	                        running = false;

	                        break;
	                }

	                break;
	            case OP_RES:
	            case OP_RTI:
	            default:
	                /* BAD OPCODE */
	                System.exit(1);

	                break;
	        }// end Trap switch case
	        
	        if(TROUBLE_SHOOT)
		    	printRegValues(instr);
	        
	        
	    } //end while loop
	    input.close();
	   
	}

}
