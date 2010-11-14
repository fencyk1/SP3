import java.util.ArrayList;

/**
 * This class will take a string and separate it by white space and '|'
 * 
 * @author Everett
 *
 */
public interface TokenizerInterface {

	/**
	 * This will take a string and separate it into tokens by white space.
	 * 
	 * @param line = string to be separated
	 */
	void tokenizeBySpace(String line);
	
	/**
	 * This will take a string and separate it into tokens by pipes.
	 * 
	 * @param line = string to be separated
	 */
	void tokenizeByPipe(String line);
	
	/**
	 * This method will take a string and separate it into tokens by both space and pipes.
	 * 
	 * @param line = string to be separated
	 * @return ArrayList<String> of separated tokens
	 */
	ArrayList<String> tokenize(String line);
}
