/**
 * A class for signaling that a filesystem is invalid.
 * 
 * @author 	Jérôme D'hulst, Marie Levrau, Art Willems
 * @version	2.1
 */
public class FileSystemInvalidException extends RuntimeException {

	/**
	 * Required because this class inherits from Exception
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Variable referencing the filesystem that is invalid.
	 */
	private final FileSystem FileSystem;

	/**
	 * Initialize this new filesystem invalid exception involving the
	 * given filesystem.
	 * 
	 * @param	filesystem
	 * 			The filesystem for the new filesystem invalid exception.
	 * @post	The filesystem involved in the new filesystem invalid exception
	 * 			is set to the given filesystem.
	 * 			| new.getFile() == filesystem
	 */
	@Raw
	public FileSystemInvalidException(FileSystem FileSystem) {
		this.FileSystem = FileSystem;
	}
	
	/**
	 * Return the file involved in this filesystem invalid exception.
	 */
	@Raw @Basic @Immutable
	public FileSystem getFileSystem() {
		return FileSystem;
	}
	
	
}

