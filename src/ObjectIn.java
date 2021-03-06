import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ObjectIn implements ObjectInInterface{
	public ArrayList<ArrayList<String>> objectArray = new ArrayList<ArrayList<String>>();

	@Override
	public void readObjectFile(File object) throws IOException {
		
		//print message declaring operation
		System.out.println("Importing object file : " + object);
		
		//clear the objectArray
		objectArray = new ArrayList<ArrayList<String>>(0);
		
		//get object file
		BufferedReader input = new BufferedReader(new FileReader(object));

		//create variable to hold line to tokenize
		String line;
		
		//take a line from the input object and tokenize it and insert it into the sourceArray
		while ((line = input.readLine()) != null)
		{
			/**
			//print line being read
			System.out.println(line);
			**/
			
			//create tokenizer object
			LinkerTokenizerInterface tokenizer = new LinkerTokenizer();
			
			//tokenize line and add to object
			objectArray.add(tokenizer.tokenize(line));
			
		}
		
	}

	@Override
	public ArrayList<ArrayList<String>> outputObjectArray() {
		
		//return copy of objectArray
		ArrayList<ArrayList<String>> objectArrayCopy = objectArray;
		return objectArrayCopy;
	}

}
