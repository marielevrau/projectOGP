import be.kuleuven.cs.som.annotate.*;

/**
 * A class of exceptions signalling that a directory or a file is not in the list of a directory. 
 * 
 * @author 	Jérôme D'hulst, Marie Levrau, Art Willems
 * @version	2.1
 */

public class NotInListException extends RuntimeException {

	/**
	 * Required because this class inherits from Exception
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Variable referencing the directory where the directory or the file is not in its list.
	 */
	private final Directory directory;
	
	/**
	 * Initialize this new not in list exception involving the given directory.
	 * 
	 * @param	directory
	 * 			The directory for the new not in list exception.
	 * @post	The directory involved in the new not in list exception
	 * 			is set to the given directory.
	 * 			| new.getDirectory() == directory
	 */
	@raw
	public NotInListException(Directory directory) {
		this.directory = directory;
	}
	
	/**
	 * Return the directory involved in this not in list exception.
	 */
	@Raw @Basic @Immutable
	public Directory getDirectory() {
		return directory;
	}
	
}
