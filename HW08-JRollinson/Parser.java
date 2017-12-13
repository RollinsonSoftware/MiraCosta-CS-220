/********************************************
 * AUTHOR: 		Jack Rollinson
 * COURSE:		CS 220 Comp. Arc.
 * DATE: 		10/25/2017
 ********************************************/

/*****************************************************************************
 * HOMEWORK NUMBER 8: VM Translator.
 *****************************************************************************
 * PROGRAM DESCRIPTION/HOMEWORK PROBLEM:
 * Handles the parsing of a .vm file, encapsulates access to assemebler code.
 *****************************************************************************/
 
//IMPORTS:
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class Parser 
{
	//CONSTANTS:
	public static final int ARITHMETIC = 0;
    public static final int PUSH = 1;
    public static final int POP = 2;
    public static final int LABEL = 3;
    public static final int GOTO = 4;
    public static final int IF = 5;
    public static final int FUNCTION = 6;
    public static final int RETURN = 7;
    public static final int CALL = 8;
    
    public static final ArrayList<String> arithmeticCmds = new ArrayList<String>();
    
    static 
   	{
		arithmeticCmds.add("add");
		arithmeticCmds.add("sub");
		arithmeticCmds.add("neg");
		arithmeticCmds.add("eq");
		arithmeticCmds.add("gt");
        arithmeticCmds.add("lt");
        arithmeticCmds.add("and");
        arithmeticCmds.add("or");
        arithmeticCmds.add("not");
    }
    
    //INSTANCE VARIABLES:
    private Scanner commands;
    private String currentCommand;
    
    private int argumentType;
    private String argument1;
    private int argument2;

   	// DESCRIPTION: Creats a new Parser object, and cleans all the lines of the .vm file.
	// PRE-CONDITION: inputFile is a valid file stream that has been initialized. 
	// POST-CONDITION: Each line of the .vm file has been cleaned and stores in a string (preprocessed),
	//						this string is navigated via a Scanner object. 
    public Parser(File inputFile) 
    {
    	//Initializing arguments.
        argumentType = -1;
        argument1 = "";
        argument2 = -1;

        try 
        {
            commands = new Scanner(inputFile);

            String preprocessed = "";
            String line = "";

            while(commands.hasNext())
            {
                line = removeComments(commands.nextLine()).trim(); //Clean the new line.

                if (line.length() > 0) 
                {
                    preprocessed += line + "\n";
                }
            }
            commands = new Scanner(preprocessed.trim());
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("ERROR: File not found!");
            e.getMessage();
            e.printStackTrace();
        }
    }

	// DESCRIPTION: Checks to see if there are more commands to read.	
	// PRE-CONDITION: inputFile stream as been created.
	// POST-CONDITION: return true if there are more lines on the inputFile, false otherwise.
    public boolean hasMoreCommands()
    {
       return commands.hasNextLine();
    }

  	// DESCRIPTION: Reads the next command from the inputFile, updates current command.
	// PRE-CONDITION: hasMoreCommands() has been called and returned true.
	// POST-CONDITION: currentCommand has been updated to the line that was just read.
    public void advance()
    {

        currentCommand = commands.nextLine();
        argument1 = ""; //initialize arg1
        argument2 = -1; //initialize arg2

        String[] segment = currentCommand.split(" ");

        if (segment.length > 3)
        {
            throw new IllegalArgumentException("ERROR: Too many arguments!");
        }
        if (arithmeticCmds.contains(segment[0]))
        {
            argumentType = ARITHMETIC;
            argument1 = segment[0];
        }
        else if (segment[0].equals("return")) 
        {
            argumentType = RETURN;
            argument1 = segment[0];
        }
        else 
        {
            argument1 = segment[1];

            if(segment[0].equals("push"))
            {
                argumentType = PUSH;
            }
            else if(segment[0].equals("pop"))
            {
                argumentType = POP;
            }
            else if(segment[0].equals("label"))
            {
                argumentType = LABEL;
            }
            else if(segment[0].equals("if"))
            {
                argumentType = IF;
            }
            else if (segment[0].equals("goto"))
            {
                argumentType = GOTO;
            }
            else if (segment[0].equals("function"))
            {
                argumentType = FUNCTION;
            }
            else if (segment[0].equals("call"))
            {
                argumentType = CALL;
            }
            else 
            {
                throw new IllegalArgumentException("Unknown Command Type!");
            }
            if (argumentType == PUSH || argumentType == POP || argumentType == FUNCTION || argumentType == CALL)
            {
                try 
                {
                    argument2 = Integer.parseInt(segment[2]);
                }
                catch (Exception e)
                {
                	e.getMessage();
                	e.printStackTrace();
                    throw new IllegalArgumentException("ERROR: argument2 is not an integer!");
                }
            }
        }
    }
    
	// DESCRIPTION: Returns the command type for the current line in the parser.	
	// PRE-CONDITION: Assumes that VM commands have been read in from a valid file stream.
	// POST-CONDITION: Returns the current command type, if there is none, throws an
	//						IllegalStateException.
    public int getCommandType()
    {
        if (argumentType != -1) 
        {
            return argumentType;
        }
        else 
        {
            throw new IllegalStateException("ERROR: No command found!");
        }
    }
	
	// DESCRIPTION: Returns the first argument for the current command.
	// PRE-CONDITION: Assumes that VM commands have been read in from a valid file stream.
	// POST-CONDITION: Returns arugment1 if it is an Arithmetic command, else throws
	// 						IllegalStateException.
    public String getArgument1()
    {

        if (getCommandType() != RETURN)
        {
            return argument1;
        }
        else         
        {
            throw new IllegalStateException("ERROR: Can not get arg1 from a 'RETURN' type command!");
        }
    }
    
	// DESCRIPTION: Returns the second argument for the current command.	
	// PRE-CONDITION: Assumes that VM commands have been read in from a valid file stream.
	// POST-CONDITION: Returns argument2 if the current command type is PUSH, POP, FUNCTION or CALL,
	//						else throws an IllegalStateException.
    public int getArgument2()
    {
        if (getCommandType() == PUSH || getCommandType() == POP || getCommandType() == FUNCTION || getCommandType() == CALL)
        {
            return argument2;
        }
        else 
        {
            throw new IllegalStateException("ERROR: Can not find argument2!");
        }
    }
    
	// DESCRIPTION: Gets the extetsion for the given file.		
	// PRE-CONDITION: Assumes that fileName is a valid file stream or file path.
	// POST-CONDITION: Returns the files extension, if there is no extension, returns
	// 						the empty string.
    public static String getExtension(String fileName)
    {
        int index = fileName.lastIndexOf('.');

        if (index != -1)
        {
            return fileName.substring(index);
        }
        else
        {
        	System.out.println("ERROR: File extension could not be found!");
            return "";
        }
    }
    
	// DESCRIPTION: Removes all comments from a line.
	// PRE-CONDITION: None.
	// POST-CONDITION: The string that comes after "//" has been removed. 
    private static String removeComments(String line)
    {
        int position = line.indexOf("//");

        if (position != -1)
        {
            line = line.substring(0, position);
        }
        return line;
    }
	
	// DESCRIPTION: Removes all spaces from a line.
	// PRE-CONDITION: None.
	// POST-CONDITION: All " " have been removed from the String line.
    private static String removeSpaces(String line)
    {
        String result = "";

        if (line.length() != 0)
        {
            String[] segment = line.split(" ");

            for (String s: segment)
            {
                result += s;
            }
        }
        return result;
    }
}