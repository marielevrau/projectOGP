import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signaling that a filesystem has already been deleted.
 * 
 * @author 	Jérôme D'hulst, Marie Levrau, Art Willems
 * @version	2.1
 */

public class FileSystemAlreadyDeletedException {

	/**
	 * Required because this class inherits from Exception
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Variable referencing the filesystem that has already been deleted.
	 */
	private final FileSystem fileSystem;
	
	/**
	 * Initialize this new filesystem already deleted exception involving the given filesystem.
	 * 
	 * @param	filesystem
	 * 			The filesystem for the new filesystem already deleted exception.
	 * @post	The filesystem involved in the new filesystem already deleted exception
	 * 			is set to the given filesystem.
	 * 			| new.getFileSystem() == fileSystem
	 */
	@Raw
	public FileSystemAlreadyDeletedException(FileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}
	
	/**
	 * Return the fileSystem involved in this filesystem already deleted exception.
	 */
	@Raw @Basic @Immutable
	public FileSystem getFileSystem() {
		return fileSystem;
	}
	
}

}
