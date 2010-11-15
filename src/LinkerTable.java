import java.util.ArrayList;


public class LinkerTable implements LinkerTabelInterface {
	private ArrayList<String> symTable = new ArrayList<String>(0);
	private ArrayList<String> typeTable = new ArrayList<String>(0);
	private ArrayList<Integer> locTable = new ArrayList<Integer>(0);
	
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

	
	
}
