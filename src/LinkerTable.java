import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class LinkerTable implements LinkerTableInterface {
	private ArrayList<String> symTable = new ArrayList<String>(0);
	private ArrayList<String> typeTable = new ArrayList<String>(0);
	private ArrayList<Integer> locTable = new ArrayList<Integer>(0);
	private Integer length = 0;
	
	@Override
	public boolean add(String name, String type, int location) {
		
		//check if name is already defined
		if(symTable.contains(name))
		{
			return false;
		}
		
		//if name is not already defined, add symbol elements to end of lists
		symTable.add(name);
		typeTable.add(type);
		locTable.add(location);
		
		//return true for success
		return true;
		
		
	}

	@Override
	public boolean changeLocation(String name, int location) {
		
		//check if name exists in table
		if (symTable.contains(name))
		{
			
			//set the new location
			locTable.set(symTable.indexOf(name), location);
			
			//return true for success
			return true;
		}
		
		//else return false
		else
		{
			return false;
		}
		
	}

	@Override
	public boolean isDefined(String name) {
		
		//if defined return true
		if(symTable.contains(name))
		{
			return true;
		}
		
		//else return false
		else
		{
			return false;
		}
		
	}

	@Override
	public int getLocation(String name) {
		
		//return location at index of name
		return locTable.get(symTable.indexOf(name));
	}

	@Override
	public void print() {

		//print heading
		System.out.println("Name\tType\tLocation");
		
		//loop though and output all tables
		for(int inc = 0; inc < symTable.size(); inc++)
		{
			
			//print symbol name followed by tab
			System.out.print(symTable.get(inc) + "\t");
			
			//print symbol type followed by tab
			System.out.print(typeTable.get(inc) + "\t");
			
			//print symbol location followed by new line
			System.out.println(locTable.get(inc));
			
		}
		
	}

	@Override
	public void printToFile() throws IOException {

		//create file
		PrintWriter out = new PrintWriter (new BufferedWriter(new FileWriter(new File("GlobalSymbolTable.txt"))));

		//print heading
		out.println("Name\tType\tLocation");
		
		//loop though and output all tables
		for(int inc = 0; inc < symTable.size(); inc++)
		{
			
			//print symbol name followed by tab
			out.print(symTable.get(inc) + "\t");
			
			//print symbol type followed by tab
			out.print(typeTable.get(inc) + "\t");
			
			//print symbol location followed by new line
			out.println(locTable.get(inc));
			
		}
		
		out.close();
		
	}

	@Override
	public void increaseLength(int integer){
		
		length = length + integer;
		
	}
	
	@Override
	public int getLength(){
		return length;
	}
	
}
