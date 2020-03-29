import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class for signaling illegal attempts to change a file.
 * 
 * @author 	Tommy Messelis
 * @version	2.1
 */
public class FileNotWritableException extends RuntimeException {

	/**
	 * Required because this class inherits from Exception
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Variable referencing the file to which change was denied.
	 */
	private final File file;

	/**
	 * Initialize this new file not writable exception involving the
	 * given file.
	 * 
	 * @param	file
	 * 			The file for the new file not writable exception.
	 * @post	The file involved in the new file not writable exception
	 * 			is set to the given file.
	 * 			| new.getFile() == file
	 */
	@Raw
	public FileNotWritableException(File file) {
		this.file = file;
	}
	
	/**
	 * Return the file involved in this file not writable exception.
	 */
	@Raw @Basic @Immutable
	public File getFile() {
		return file;
	}
	
	
}

