/********************************************
 * AUTHOR: 		Jack Rollinson
 * COURSE:		CS 220 Comp. Arc.
 * DATE: 		10/25/2017
 ********************************************/

/*****************************************************************************
 * HOMEWORK NUMBER 8: VM Translator.
 *****************************************************************************
 * PROGRAM DESCRIPTION/HOMEWORK PROBLEM:
 * Build the Jack/Hack architecture virtual machine part 1, in java, per the
 * instructions and guidance covered in class. 
 *****************************************************************************
 * ALGORITHM:
 * Get input file name from command line of from console input.
 * Check for single VM File or VM File Directory.
 * If it is a single file, add it to the list of VM files.
 * If it is a directory of VM files, add all the VM files in the directory to
 *		the list.
 * Update file path for the new .asm file being created.
 * After all files have been added to the list, begin the parsing process.
 *		While there are more commands on the current VM File
 *			check if Arithmetic command or Push/Pop Command.
 *			perform the correct translation for given command type.
 * Close file streams, and output to consol that the translation is done,
 *		with the file path of the new .asm file. 
 *****************************************************************************/
 
//IMPORTS:
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class VirtualMachinePart1
{    
    public static void main(String[] args) 
    {
    	//INSTANCE VARIABLES:
    	ArrayList<File> vmFiles = new ArrayList<File>();
    	
    	String inputFileName, outputFileName;
		String fileOutPath = "";
		
		File inputFile = null;
		File outputFile = null;
		
		CodeWriter writer;
		
		//get input file name from command line or console input
        if (args.length == 1)
        {
            System.out.println("command line arg = " + args[0]);
			inputFileName = args[0];
        }
        else 
        {
        	Scanner keyboard = new Scanner(System.in);

			System.out.println("Please enter assembly file name you would like to assemble.");
			System.out.println("Don't forget the .vm extension: ");
			inputFileName = keyboard.nextLine();
					
			keyboard.close();
			
            inputFile = new File(inputFileName);

            if (inputFile.isFile()) 
            {
                //if it is a single file, see whether it is a vm file.
                String path = inputFile.getAbsolutePath();

                if (!Parser.getExtension(path).equals(".vm")) 
                {
                    throw new IllegalArgumentException(".vm file is required!");
                }

                vmFiles.add(inputFile);

                fileOutPath = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().lastIndexOf(".")) + ".asm";

            } 
            else if (inputFile.isDirectory()) 
            {

                //if it is a directory, get all the vm files under that directory.
                vmFiles = getVMFiles(inputFile);

                //if no vm files are found in the directory.
                if (vmFiles.size() == 0) 
                {
                    throw new IllegalArgumentException("ERROR: No .vm file in this directory");
                }
                fileOutPath = inputFile.getAbsolutePath() + "/" +  inputFile.getName() + ".asm";
            }

            outputFile = new File(fileOutPath);
            writer = new CodeWriter(outputFile);

            for (File f : vmFiles) 
            {
                Parser parser = new Parser(f);

                int type = -1;

                //start parsing process.
                while (parser.hasMoreCommands()) 
                {
                    parser.advance();

                    type = parser.getCommandType();

                    if (type == Parser.ARITHMETIC) 
                    {
                        writer.writeArithmetic(parser.getArgument1());
                    } 
                    else if (type == Parser.POP || type == Parser.PUSH) 
                    {
                        writer.writePushPop(type, parser.getArgument1(), parser.getArgument2());
                    }
                }
            }
            //save file
            writer.close();
			
			System.out.println("\nDone!\n" + "\n...\n");
            System.out.println("File created @ " + fileOutPath);
		}
	}
	// DESCRIPTION: Adds all the VM files from the given directory to the ArrayList.
	// PRE-CONDITION: direcotry is a valid file stream.	
	// POST-CONDITION: All VM  files have bee added to the list.
    public static ArrayList<File> getVMFiles(File directory)
    {
        File[] files = directory.listFiles();

        ArrayList<File> result = new ArrayList<File>();

        for (File index : files)
        {
            if (index.getName().endsWith(".vm"))
            {
                result.add(index);
            }
        }
        return result;
    }
}