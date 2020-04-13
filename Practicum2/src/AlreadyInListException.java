import be.kuleuven.cs.som.annotate.*;


/**
 * A class of exceptions signalling that a file or a subdirectory is already in the directory. 
 * 
 * @author 	Jérôme D'hulst, Marie Levrau, Art Willems
 * @version	2.1
 */

public class AlreadyInListException extends RuntimeException {
	
	/**
	 * Required because this class inherits from Exception
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Variable referencing the file or subdirectory that is already in the directory.
	 */
	private final FileSystem fileSystem;
	
	/**
	 * Initialize this new already in list exception involving the given fileSystem.
	 * 
	 * @param	fileSystem
	 * 			The file for the new file not writable exception.
	 * @post	The file involved in the new file not writable exception
	 * 			is set to the given file.
	 * 			| new.getFile() == file
	 */
	@Raw
	public AlreadyInListException(FileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}
	
	/**
	 * Return the fileSystem involved in this already in list exception.
	 */
	@Raw @Basic @Immutable
	public FileSystem getFileSystem() {
		return fileSystem;
	}
	
}
