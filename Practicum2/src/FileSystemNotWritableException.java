/**
 * A class for signaling illegal attempts to change a filesystem.
 * 
 * @author 	Jérôme D'hulst, Marie Levrau, Art Willems
 * @version	2.1
 */
public class FileSystemNotWritableException extends RuntimeException {

	/**
	 * Required because this class inherits from Exception
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Variable referencing the filesystem to which change was denied.
	 */
	private final FileSystem FileSystem;

	/**
	 * Initialize this new filesystem not writable exception involving the
	 * given filesystem.
	 * 
	 * @param	filesystem
	 * 			The filesystem for the new filesystem not writable exception.
	 * @post	The filesystem involved in the new filesystem not writable exception
	 * 			is set to the given filesystem.
	 * 			| new.getFile() == filesystem
	 */
	@Raw
	public FileSystemNotWritableException(FileSystem FileSystem) {
		this.FileSystem = FileSystem;
	}
	
	/**
	 * Return the file involved in this filesystem not writable exception.
	 */
	@Raw @Basic @Immutable
	public FileSystem getFileSystem() {
		return FileSystem;
	}
	
	
}
