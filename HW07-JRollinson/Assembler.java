/********************************************
 * AUTHOR: 		Jack Rollinson
 * COURSE:		CS 220 Comp. Arc.
 * DATE: 		10/19/2017
 ********************************************/

/*****************************************************************************
 * HOMEWORK NUMBER 7: Assembler
 *****************************************************************************
 * PROGRAM DESCRIPTION/HOMEWORK PROBLEM:
 *	Utilizes the Parser, SymbolTable, and Code Classes to translate assembly
 *	language to binary/machine codeTable.
 *****************************************************************************
 * ALGORITHM:
 	// get input file name
	// create output file name and stream
	
	// create symbol table
	// do first pass to build symbol table (no output yet!)
	// do second pass to output translated ASM to HACK codeTable
	
	// print out "done" message to user
	// close output file stream
 *****************************************************************************/
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;

public class Assembler {
	
	public static void main(String[] args) 
	{
		
		String inputFileName, outputFileName;
		PrintWriter outputFile = null; //keep compiler happy
		SymbolTable symbolTable;
		int romAddress, ramAddress;
	
		//get input file name from command line or console input
		if(args.length == 1) {
			System.out.println("command line arg = " + args[0]);
			inputFileName = args[0];
		}
		else
		{
			Scanner keyboard = new Scanner(System.in);

			System.out.println("Please enter assembly file name you would like to assemble.");
			System.out.println("Don't forget the .asm extension: ");
			inputFileName = keyboard.nextLine();
					
			keyboard.close();
		}
		
		outputFileName = inputFileName.substring(0,inputFileName.lastIndexOf('.')) + ".hack";
							
		try 
		{
			outputFile = new PrintWriter(new FileOutputStream(outputFileName));
		} 
		catch (FileNotFoundException ex) 
		{
			System.err.println("Could not open output file " + outputFileName);
			System.err.println("Run program again, make sure you have write permissions, etc.");
			System.exit(0);
		}
		//=====================================================================================
		
		Code codeTable = new Code();
		symbolTable = new SymbolTable();
			
		firstPass(inputFileName, symbolTable);
		System.out.println("Done with first pass...");
		
		secondPass(inputFileName, symbolTable, outputFile);
		System.out.println("Done with second pass...");
		
		outputFile.close();
	}
	
	//TODO: march through the source codeTable without generating any codeTable
	//for each label declaration (LABEL) that appears in the source codeTable,
	//add the pair <LABEL, n> to the symbol table n = romAddress which 
	//you should keep track of as you go through each line.
	//HINT: when should rom address increase? What kind of commands?
	private static void firstPass(String inputFileName, SymbolTable symbolTable) 
	{
		Parser parse = new Parser(inputFileName);
		String symbol = "";
		int romAddress;
		
		while(parse.hasMoreCommands())
		{
			parse.advance();
			romAddress = parse.getLineNumber();
			
			if(parse.getCommandType() == 'L')
			{
				symbol = parse.getSymbol();
				symbolTable.addEntry(symbol, romAddress);
			}
		}
	}
	
	// TODO: march again through the source codeTable and process each line:
		// if the line is a c-instruction, simple (translate)
		// if the line is @xxx where xxx is a number, simple (translate)
		// if the line is @xxx and xxx is a symbol, look it up in the symbol
		//	table and proceed as follows:
			// If the symbol is found, replace it with its numeric value and
			//	and complete the commands translation
			// If the symbol is not found, then it must represent a new variable:
				// add the pair <xxx, n> to the symbol table, where n is the next
				//	available RAM address, and complete the commands translation
	// HINT: when should rom address increase?  What should ram address start
	// at? When should it increase?  What do you do with L commands and No commands?
	private static void secondPass(String inputFileName, SymbolTable symbolTable, PrintWriter outputFile) 
	{
		Parser parse = new Parser(inputFileName);
		Code codeTable = new Code();
		
		String outputCode = "";
		int ramAddress = 16;
		
		
		while(parse.hasMoreCommands())
		{
			parse.advance();
			
			if(parse.getCommandType() == 'C')
			{
				if(codeTable.getComp(parse.getComp()) == null || codeTable.getDest(parse.getDest()) == null || codeTable.getJump(parse.getJump()) == null)
				{
					System.out.println("Syntax error at line number: " + parse.getLineNumber() + "\nShutting down now...");
					System.exit(0);
				}
				else
				{
					outputCode = "111" + codeTable.getComp(parse.getComp()) + codeTable.getDest(parse.getDest()) + codeTable.getJump(parse.getJump());
					System.out.println(outputCode);
					outputFile.println(outputCode);
				}
			}
			else if(parse.getCommandType() == 'A')
			{
				int num = 0;
				String numbers = "0123456789";
				String symbol = "";
				
				symbol = parse.getSymbol();
				
				if(numbers.indexOf(symbol.charAt(0)) != -1)
				{
					try
					{
						num = Integer.parseInt(symbol);
					}
					catch(NumberFormatException e)
					{
						System.out.println(e.getMessage());
						System.out.println("Wrong variable name at line number: " + parse.getLineNumber() + "\nShutting down now...");
						System.exit(0);
					}
					
					outputCode =  "0" + codeTable.decimalToBinary(num);
					outputFile.println(outputCode);
				}
				else
				{
					if(symbolTable.contains(symbol))
					{
						num = symbolTable.getAddress(symbol);
						
						outputCode = "0" + codeTable.decimalToBinary(num);
						outputFile.println(outputCode);
					}
					else
					{
						if(!symbolTable.addEntry(symbol, ramAddress))
						{
							System.out.println("Syntax error at line number: " + parse.getLineNumber() + "\nShutting down now...");
							System.exit(0);
						}
					}
					outputCode = "0" + codeTable.decimalToBinary(ramAddress);
					outputFile.println(outputCode);
					ramAddress++;
				}
			}
		}	
	}					
}
