import be.kuleuven.cs.som.annotate.*;

/**
 * A class of exceptions signalling that a file has an illegal type. 
 * 
 * @author 	Jérôme D'hulst, Marie Levrau, Art Willems
 * @version	2.1
 */

public class IllegalTypeException extends RuntimeException{
	
	/**
	 * Required because this class inherits from Exception
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Variable referencing the file that has an illegal type.
	 */
	private final File file;
	
	/**
	 * Initialize this new illegal type exception involving the given file.
	 * 
	 * @param	file
	 * 			The file for the new illegal type exception.
	 * @post	The file involved in the new illegal type exception
	 * 			is set to the given file.
	 * 			| new.getFile() == file
	 */
	@Raw
	public IllegalTypeException(File file) {
		this.file = file;
	}
	
	/**
	 * Return the file involved in this illegal type exception.
	 */
	@Raw @Basic @Immutable
	public File getFile() {
		return file;
	}
	
	
}
