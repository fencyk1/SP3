import java.util.ArrayList;
import java.util.StringTokenizer;


public class LinkerTokenizer implements LinkerTokenizerInterface {
	private ArrayList<String> pipeArray;
	private ArrayList<String> spaceArray;

	@Override
	public void tokenizeBySpace(String line) {

		
		//create a new tokenizer using line
		StringTokenizer bySpace = new StringTokenizer(line);
		
		//set variable tokenCount to be the number of tokens produced
		int tokenCount = bySpace.countTokens();
		
		//constuct the holding array
		spaceArray = new ArrayList<String>(tokenCount);
		
		//add the tokens to the array
		for(int inc =0; inc < tokenCount; inc++)
		{
			spaceArray.add(bySpace.nextToken());
		}
	}

	@Override
	public void tokenizeByPipe(String line) {
		
		//create a delimiter for the tokenizer
		String dlmtr = "|";
		
		//create a new tokenizer using dlmtr and line
		StringTokenizer byPipe = new StringTokenizer(line, dlmtr);
		
		//set variable tokenCount to be the number of tokens produced
		int tokenCount = byPipe.countTokens();
		
		//constuct the holding array
		pipeArray = new ArrayList<String>(tokenCount);
		
		//add the tokens to the array
		for(int inc =0; inc < tokenCount; inc++)
		{
			pipeArray.add(byPipe.nextToken());
		}
		

	}

	@Override
	public ArrayList<String> tokenize(String line) {
		
		//create storage array
		ArrayList<String> retArray = new ArrayList<String>(0);
		
		//print tokenizing message
		System.out.println(line);
		
		//tokenize by pipe
		tokenizeByPipe(line);
		
		//tokenize by space
		for(int inc = 0; inc < pipeArray.size(); inc++)
		{
			/**
			//create temp string
			String temp;
			
			//set temp equal to token at position inc in pipeArray
			temp = pipeArray.get(inc);
			
			//tokenize temp by space
			tokenizeBySpace(temp);
			
			//add tokens to retArray
			for(int inc2 = 0; inc2 < spaceArray.size(); inc++)
			{
				retArray.add(inc2, spaceArray.get(inc2));	
			}
			**/
			
			retArray.add(inc, pipeArray.get(inc));
			
		}
		
		
		
		
		
		//return
		return retArray;
	}

}
