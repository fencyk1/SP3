import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Linker {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		//create global variables
		ArrayList<ArrayList<String>> objectArray;
		boolean validObject = true;

		
		//begin loop for multiple files
		for(int inc = 0; inc < args.length; inc++)
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
				//create symbol table
		
				//populate symbol table
		
			}
			
			
			
		
			
			
		//end for loop for multiple files
		}
		
		//things that need to be done outside of loop
		//they need all objects to be processed before they can be done
		
		
		//create linker file
		
		//transfer text records into linker file and adjust as needed
		
		

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
		
		return false;
	}

}
