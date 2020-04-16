import be.kuleuven.cs.som.annotate.*;

/**
 * A class of exceptions signalling that a directory is a root directory. 
 * 
 * @author 	Jérôme D'hulst, Marie Levrau, Art Willems
 * @version	2.1
 */

public class IsRootDirectoryException extends RuntimeException{
	
	/**
	 * Required because this class inherits from Exception
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Variable referencing the directory that is a root directory.
	 */
	private final Directory directory;
	
	/**
	 * Initialize this new is root directory exception involving the given directory.
	 * 
	 * @param	directory
	 * 			The directory for the new is root directory exception.
	 * @post	The directory involved in the new is root directory exception
	 * 			is set to the given directory.
	 * 			| new.getDirectory() == directory
	 */
	@Raw
	public IsRootDirectoryException(Directory directory) {
		this.directory = directory;
	}
	
	/**
	 * Return the directory involved in this is root directory exception.
	 */
	@Raw @Basic @Immutable
	public Directory getDirectory() {
		return directory;
	}

}
