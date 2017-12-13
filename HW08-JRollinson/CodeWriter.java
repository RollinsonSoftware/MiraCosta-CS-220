/********************************************
 * AUTHOR: 		Jack Rollinson
 * COURSE:		CS 220 Comp. Arc.
 * DATE: 		10/25/2017
 ********************************************/

/*****************************************************************************
 * HOMEWORK NUMBER 8: VM Translator.
 *****************************************************************************
 * PROGRAM DESCRIPTION/HOMEWORK PROBLEM:
 * Translates VM commands into HACK assembly code.
 *****************************************************************************/

//IMPORTS:
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CodeWriter 
{
	//INSTANCE VARIABLES:
    private int arthJumpFlag;
    private PrintWriter outputStream;
    
   	// DESCRIPTION: Creates a new output stream for file output. 
	// PRE-CONDITION: None.	
	// POST-CONDITION: output stream to file has been created and is ready for use.
    public CodeWriter(File fileOut) 
    {
        try 
        {
            outputStream = new PrintWriter(fileOut);
            arthJumpFlag = 0;
        } 
        catch (FileNotFoundException e) 
        {
			e.getMessage();
            e.printStackTrace();
        }
    }
    	
   	// DESCRIPTION: Translates the given arithmetic command to assembly code.
   	//					Parameter 'command' is case sensitive, the command must be exact.
	// PRE-CONDITION: command is a valid Arithmetic command with a corresponding assembly
	//					translation.
	// POST-CONDITION: The correct assembly translation has been 
    public void writeArithmetic(String command)
    {
        if (command.equals("add"))
        {
            outputStream.print(arithmeticTemplate1() + "M=M+D\n");
        }
        else if (command.equals("sub"))
        {
            outputStream.print(arithmeticTemplate1() + "M=M-D\n");
        }
        else if (command.equals("and"))
        {
            outputStream.print(arithmeticTemplate1() + "M=M&D\n");
        }
        else if (command.equals("or"))
        {
            outputStream.print(arithmeticTemplate1() + "M=M|D\n");
        }
        else if (command.equals("gt"))
        {
            outputStream.print(arithmeticTemplate2("JLE")); //not less than or equal to
            arthJumpFlag++;
        }
        else if (command.equals("lt"))
        {
            outputStream.print(arithmeticTemplate2("JGE")); //not greater than or equal to
            arthJumpFlag++;
        }
        else if (command.equals("eq"))
        {
            outputStream.print(arithmeticTemplate2("JNE")); //not greater/less than
            arthJumpFlag++;
        }
        else if (command.equals("not"))
        {
            outputStream.print("@SP\nA=M-1\nM=!M\n");
        }
        else if (command.equals("neg"))
        {
            outputStream.print("D=0\n@SP\nA=M-1\nM=D-M\n");
        }
        else 
        {
           	throw new IllegalArgumentException("ERROR: Call writeArithmetic() for a non-arithmetic command!");
        }
    }
    
   	// DESCRIPTION: Translates ethier PUSH or POP commands to the correct assembly code.
	// PRE-CONDITION: Given parameters are for a PUSH/POP COMMAND.
	// POST-CONDITION: Assembly translation has been written to the outputFile if valid command,
	//						else throws an IllegalArgumentException.
    public void writePushPop(int command, String fragment, int index)
    {
        if (command == Parser.PUSH)
        {
            if (fragment.equals("constant"))
            {
                outputStream.print("@" + index + "\n" + "D=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
            }
            else if (fragment.equals("local"))
            {
                outputStream.print(pushTemplate("LCL",index,false));
            }
            else if (fragment.equals("argument"))
            {
                outputStream.print(pushTemplate("ARG",index,false));
            }
            else if (fragment.equals("this"))
            {
                outputStream.print(pushTemplate("THIS",index,false));
            }
            else if (fragment.equals("that"))
            {
                outputStream.print(pushTemplate("THAT",index,false));
            }
            else if (fragment.equals("temp"))
            {
                outputStream.print(pushTemplate("R5", index + 5,false));
            }
            else if (fragment.equals("pointer") && index == 0)
            {
                outputStream.print(pushTemplate("THIS",index,true));
            }
            else if (fragment.equals("pointer") && index == 1)
            {
                outputStream.print(pushTemplate("THAT",index,true));
            }
            else if (fragment.equals("static"))
            {
                outputStream.print(pushTemplate(String.valueOf(16 + index),index,true));
            }
        }
        else if(command == Parser.POP)
        {
            if (fragment.equals("local"))
            {
                outputStream.print(popTemplate("LCL",index,false));
            }
            else if (fragment.equals("argument"))
            {
                outputStream.print(popTemplate("ARG",index,false));
            }
            else if (fragment.equals("this"))
            {
                outputStream.print(popTemplate("THIS",index,false));
            }
            else if (fragment.equals("that"))
            {
                outputStream.print(popTemplate("THAT",index,false));
            }
            else if (fragment.equals("temp"))
            {
                outputStream.print(popTemplate("R5", index + 5,false));
            }
            else if (fragment.equals("pointer") && index == 0)
            {
                outputStream.print(popTemplate("THIS",index,true));
            }
            else if (fragment.equals("pointer") && index == 1)
            {
                outputStream.print(popTemplate("THAT",index,true));
            }
            else if (fragment.equals("static"))
            {
                outputStream.print(popTemplate(String.valueOf(16 + index),index,true));
            }
        }
        else 
        {
            throw new IllegalArgumentException("ERROR: Called writePushPop() for a non-pushpop command!");
        }
    }
    
   	// DESCRIPTION: Template for addition, subtraction, and, or.
	// PRE-CONDITION: This should only be used for add, sub, and, or.
	// POST-CONDITION: returns a string template for the operations: add, sub, and, or.
    private String arithmeticTemplate1()
    {
        return "@SP\n" + "AM=M-1\n" + "D=M\n" + "A=A-1\n";
    }
    
   	// DESCRIPTION: Template for greater than, less than, and equals.		
	// PRE-CONDITION: parameter is one of the following jump commands: JLE, JGT, or JEQ.
	// POST-CONDITION: returns a string template for the operations: JLE, JGT, JEQ.
    private String arithmeticTemplate2(String type)
    {
        return "@SP\n" + "AM=M-1\n" + "D=M\n" + "A=A-1\n" + "D=M-D\n" +
                "@FALSE" + arthJumpFlag + "\n" + "D;" + type + "\n" +
                "@SP\n" + "A=M-1\n" + "M=-1\n" + "@CONTINUE" + arthJumpFlag + "\n" +
                "0;JMP\n" + "(FALSE" + arthJumpFlag + ")\n" + "@SP\n" + "A=M-1\n" +
                "M=0\n" + "(CONTINUE" + arthJumpFlag + ")\n";
    }

   	// DESCRIPTION: Template for pushing local, that, this, push, temp, static, pointer, argument.	
	// PRE-CONDITION: Being called for ONLY: local, that, this, push, static, pointer, argument. 
	// POST-CONDITION: When it is a pointer, just read the data stored in THIS or THAT.
	//						When it is static, just read the data stored in that address.	
    private String pushTemplate(String fragment, int index, boolean isDirect)
    {
        String noPointerCode = (isDirect)? "" : "@" + index + "\n" + "A=D+A\nD=M\n";

        return "@" + fragment + "\n" + "D=M\n"+ noPointerCode + "@SP\n" + "A=M\n" +
                "M=D\n" + "@SP\n" + "M=M+1\n";
    }
    
   	// DESCRIPTION: Template for popping local, that, this, push, temp, static, pointer, argument.
	// PRE-CONDITION: Being called for ONLY: local, that, this, push, static, pointer, argument.	
	// POST-CONDITION: When it is a pointer R13 will store the address of THIS or THAT,
	//						When it is a static R13 will store the index address.
    private String popTemplate(String fragment, int index, boolean isDirect)
    {
        String noPointerCode = (isDirect)? "D=A\n" : "D=M\n@" + index + "\nD=D+A\n";

        return "@" + fragment + "\n" + noPointerCode + "@R13\n" + "M=D\n" + "@SP\n" +
                "AM=M-1\n" + "D=M\n" + "@R13\n" + "A=M\n" + "M=D\n";
    }
    
    // DESCRIPTION:	Closes the outputFile stream.	
	// PRE-CONDITION: File stream is currently open.
	// POST-CONDITION: File stream has been closed.
    public void close()
    {
        outputStream.close();
    }
}