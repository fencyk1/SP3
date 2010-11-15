import java.io.IOException;

/**
 * 
 */

/**
 * This class is an object class. It holds the symbol table from the linker. It has three fields:
 * Name, type, and location, where name is the program or variable name, type is start or ent,
 * and location is the linker assigned memory location.
 * 
 * @author Everett
 *
 */
public interface LinkerTableInterface {

	/**This method adds a symbol to the symbol table
	 * 
	 * @param name = variable name
	 * @param type = type of variable: start or ent
	 * @param location = int value of linker assigned mem address
	 * @return true if successful, false if name already exists
	 */
	boolean add(String name, String type, int location);
	
	/**This method changes the location of a pre-existing variable in the table.
	 * 
	 * @param name = the name of the variable to be changed
	 * @param location = the new location
	 * @return true if successful, false if the variable didn't exist in the table.
	 */
	boolean changeLocation(String name, int location);
	
	/**This method will return true if the variable is defined in the table.
	 * 
	 * @param name = the name of the variable to search for
	 * @return true if name exists, false if not
	 */
	boolean isDefined(String name);
	
	/**This method returns the assigned location of the variable name.
	 * 
	 * @param name = the name of the variable to retrieve the location for
	 * @return int location of name
	 */
	int getLocation(String name);
	
	/**This method will print out the symbol table to the console.
	 * 
	 */
	void print();
	
	/**
	 * This method will print out the symbol table into a file called "GlobalSymbolTable.txt".
	 * @throws IOException 
	 */
	void printToFile() throws IOException;
}
