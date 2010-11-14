import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



/**
 * This is a class to read in the object file.
 * 
 * @author Everett
 *
 */
public interface ObjectInInterface {

	
	/**
	 * Reads in the object file.
	 * 
	 * @param object = object file
	 * @throws IOException
	 */
	void readObjectFile(File object) throws IOException;
	
	/**
	 * 
	 * @return ArrayList<ArrayList<String>> form of the object file
	 */
	ArrayList<ArrayList<String>> outputObjectArray();
}
