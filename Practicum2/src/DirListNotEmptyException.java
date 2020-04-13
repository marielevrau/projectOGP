import be.kuleuven.cs.som.annotate.*;

/**
 * A class of exceptions signalling that a directory is not empty. 
 * 
 * @author 	Jérôme D'hulst, Marie Levrau, Art Willems
 * @version	2.1
 */

public class DirListNotEmptyException extends RuntimeException{
	
	/**
	 * Required because this class inherits from Exception
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Variable referencing the directory that is not empty.
	 */
	private final Directory directory;
	
	/**
	 * Initialize this new directory list not empty exception involving the given directory.
	 * 
	 * @param	directory
	 * 			The directory for the new directory list not empty exception.
	 * @post	The directory involved in the new directory list not empty exception
	 * 			is set to the given directory.
	 * 			| new.getDirectory() == directory
	 */
	@Raw
	public DirListNotEmptyException(Directory directory) {
		this.directory = directory;
	}
	
	/**
	 * Return the directory involved in this directory list not empty exception.
	 */
	@Raw @Basic @Immutable
	public Directory getDirectory() {
		return directory;
	}
	
}

