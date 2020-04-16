import be.kuleuven.cs.som.annotate.*;

/**
 * A class of exceptions signalling that a directory is not a direct or indirect subdirectory of a given directory. 
 * 
 * @author 	Jérôme D'hulst, Marie Levrau, Art Willems
 * @version	2.1
 */

public class NotDirectOrIndirectSubdirectoryException extends RuntimeException{
	
	/**
	 * Required because this class inherits from Exception
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Variable referencing the directory that is not a direct or indirect subdirectory of a given directory.
	 */
	private final Directory directory;
	
	/**
	 * Initialize this new not direct or indirect subdirectory of exception involving the given directory.
	 * 
	 * @param	directory
	 * 			The directory for the new not direct or indirect subdirectory of exception.
	 * @post	The directory involved in the new not direct or indirect subdirectory of exception
	 * 			is set to the given directory.
	 * 			| new.getDirectory() == directory
	 */
	@Raw
	public NotDirectOrIndirectSubdirectoryException(Directory directory) {
		this.directory = directory;
	}
	
	/**
	 * Return the directory involved in this not direct or indirect subdirectory of exception.
	 */
	@Raw @Basic @Immutable
	public Directory getDirectory() {
		return directory;
	}
}
