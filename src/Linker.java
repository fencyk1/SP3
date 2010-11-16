import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Linker {
	static ObjectInInterface object1 = new ObjectIn();
	static ObjectInInterface object2 = new ObjectIn(); 
	static ObjectInInterface object3 = new ObjectIn();
	static ObjectInInterface object4 = new ObjectIn();
	static ObjectInInterface object5 = new ObjectIn();

	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		//create global variables
		ArrayList<ArrayList<String>> objectArray;
		boolean validObject = true;
		LinkerTableInterface GST = new LinkerTable();
		

		
		//begin loop for multiple files
		for(int inc = 0; inc < args.length && inc < 2; inc++)
		{
		
			//import object files into object arrays
			//class object imports
			ObjectInInterface objectFile = new ObjectIn();
			
			//make objecFileName from args
			File objectFileName = new File(args[inc]);
			
			//Read object file
			objectFile.readObjectFile(objectFileName);
		
			//create object array
			objectArray = objectFile.outputObjectArray();
			
			//set validObject boolean
			validObject = checkObjectValidity(objectArray);
			
			//if the object is valid, then
			if (validObject)
			{
				
				//store in appropriate ObjectIn global var
				if(inc == 0)
				{
					object1 = objectFile;
				}
				else if(inc == 1)
				{
					object2 = objectFile;
				}
				else if(inc == 2)
				{
					object3 = objectFile;
				}
				else if(inc == 3)
				{
					object4 = objectFile;
				}
				else if(inc == 4)
				{
					object5 = objectFile;
				}
				
			}
			
			//else, output error 
			else
			{
				
			}
			
			
		
			
			
		//end for loop for multiple files
		}
		
		//populate symbol table
		populateSymbolTable(GST);
		
		
		
		
		//things that need to be done outside of loop
		//they need all objects to be processed before they can be done
		
		
		//create linker file
		
		// iteration:
		// if objectX [0<X<2] is not empty, call the buildLinkerFile method on it
				// transfer text records into linker file and adjust as needed
		/* --> check that whatever is external in one place is internal in another
					- if not internal elsewhere, throw error and *abort* 
						[will likely create issues elsewhere in code]
					- "external label is not defined internally anywhere"
		  <--
		*/
		// else: just skip to the next until no other objectIn's need be 'linked'
				
		

	}
	
	/**This method will check if the object file is valid.
	 * 
	 * @param objectArray ArrayList<ArrayList<String>> containing tokenized object file
	 * @return true if object is valid
	 */
	static boolean checkObjectValidity(ArrayList<ArrayList<String>> objectArray)
	{
		
		//check for header
		
		
		//check the rest of the records  have valid labels and sizes
		
		//-----------------------------_______________ START OF KASHFLOW EDIT _______________-----------------------------//
	     /*
	        My sub-skeleton starts here. When reading through, the lines and blocks with two question marks [??]
	        are questions I have for the graders, and the lines and blocks with three question marks [???] are
	        questions I have for you in terms of designing and coding conventions / preferences.
	     */

	    /*  
	        Four cases for going through lines: header rec, linking rec, text rec, end rec:
	        Check first line; should be header record. If not, throw 'no/missing header record' error and abort
	            -boolean hasHeader to check ???
	    */

	    /*
	        for a header record:

	        -should come immediately following an end record; if not, throw a 'no/missing end record' error and abort
	            -boolean hasEnded to check?

	        -check syntax of each part of the record
	                ==> one overall error for unallowed syntax or several to break it down ??
	                ==> call method boolean isValidHeaderRecord ???
	        -ensure program length, assembler assigned program load address, and Execution start address for this module
	         are all == four hex digits
	            -also check for validity of address bounds; if violated give 'address out of bounds' error [abort?]

	        -if no link-stopping errors abound:
	            - create variables :
	                -set boolean hasEnded to false
	                -textRecs [number of text records] 
	                -linkRecs [number of linking records]
	                -linkLoadAddr [linker-adjusted loading address to use for overall linking file address]
	                -??? execStartAddr ??? [execution start address for program]
	                -pgmLen [program length]
	                -others???

	            - decrement textRecs and linkRecs each time one of the corresponding text/linking records are found
	            - if either get to zero: 
	                -throw an error [naming convention for error ???]
	                - ?? skip rest of corresponding files ??

	        ----> what else for header records ?? ???
	    */


	    /*
	        for a linking record:

	        -should come after a header record and before an end record; if not: throw a 
	         'no/missing end record' error and abort
	            -boolean hasEnded to check?

	        -check syntax of each part of the record
	                ==> one overall error for unallowed syntax or several to break it down ??
	                ==> call method boolean isValidLinkingRecord ???
	        -ensure entry address == four hex digits
	            -also check for validity of address bounds; if violated give 'address out of bounds' error [abort?]

	        -if no syntax errors or other issues:
	            -update global symbol table with newly found symbol
	            ==>what else for linking records ??
				
			
	    */

	    /*
	        for a end record:

	        -should come immediately following an end record; if not, throw a 'no/missing end record' error and abort
	            -boolean hasEnded to check?

	        -check syntax of each part of the record
	                ==> one overall error for unallowed syntax or several to break it down ??
	                ==> call method boolean isValidEndRecord ???
	            ==>check program name with that of the header record???

	        -if no syntax errors or other issues:
	            -check "total number of records" [imp'd by objectX.size] field with computation [textRecs + linkRecs + 2]
	                -if not equal, throw error for unequal record number field
	            -set boolean hasEnded to true
	            ==>what else for end records ??             
	    */

	    /*
	        for a text record: 

	        -should come after a header record and before an end record; if not: throw a 
	         'no/missing end record' error and abort
	            -boolean hasEnded to check?

	        -check syntax of each part of the record
	                ==> one overall error for unallowed syntax or several to break it down ??
	                ==> call method boolean isValidTextRecord ???
	        -ensure program length, assembler assigned program load address, and Execution start address for this module
	         are all == four hex digits
	            -also check for validity of address bounds; if violated give 'address out of bounds' error [abort?]
		
			---> use the .equals function for checking String objects
			
			
			
	    */


	    //-----------------------------_______________ END OF KASHFLOW EDIT _______________-----------------------------//


		
		return false;
	}

	/**This method takes an ObjectIn object file and output the correct parts to the linker
	 * file.
	 * 
	 * @param linkerFile = the file to output to
	 * @param symbolTable = the object containing the linker's global symbol table
	 * @param objectfile = the ObjectIn containing the object file to be converted
	 */
	static void buildLinkerFile(File linkerFile, LinkerTableInterface symbolTable, ObjectInInterface objectfile)
	{
		//TODO: everything
	}

	/**This method takes an ObjectIn and and gets the linker records and inputs the needed
	 * data into the LinkerTable(also know as the global symbol table).
	 * 
	 * @param symbolTable
	 * @param objectFile
	 */
	static void populateSymbolTable(LinkerTableInterface symbolTable)
	{
		
		//create converter class
		ConverterInterface converter = new Converter();
		
		//storage variables
		int startLocation = 0; //this will hold the beginning address of the program
		
		//create objectArrays
		ArrayList<ArrayList<String>> object1Array = object1.outputObjectArray();
		ArrayList<ArrayList<String>> object2Array = object2.outputObjectArray();
		ArrayList<ArrayList<String>> object3Array = object3.outputObjectArray();
		ArrayList<ArrayList<String>> object4Array = object4.outputObjectArray();
		ArrayList<ArrayList<String>> object5Array = object5.outputObjectArray();
		
		
		//get start location
		startLocation = Integer.parseInt(converter.hexToDec(object1Array.get(0).get(4)));
		
		
		
		
		//increment through object1Array and find the linker files
		for(int inc = 0; inc < object1Array.size(); inc ++)
		{
			
			//check record type. If 'L' continue
			if (object1Array.get(inc).get(0).equals("L"))
			{
				
				//get name
				String name = object1Array.get(inc).get(1);
				
				//get type
				String type = object1Array.get(inc).get(3);
				
				//get location
				int location = Integer.parseInt(converter.hexToDec(object1Array.get(inc).get(2)));
				
				//add to symbol table
				symbolTable.add(name, type, location);
			}
			
		}
		
		
	}
	
}
