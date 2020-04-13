import be.kuleuven.cs.som.annotate.*;

/**
 * A class of exceptions signalling that an index is out of range in the directory list. 
 * 
 * @author 	Jérôme D'hulst, Marie Levrau, Art Willems
 * @version	2.1
 */

public class IndexOutOfRangeException extends RuntimeException{
	
	/**
	 * Required because this class inherits from Exception
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Variable referencing the directory where the index is out of range.
	 */
	private final Directory Directory;
	
	/**
	 * Initialize this new index out of range exception involving the given directory.
	 * 
	 * @param	directory
	 * 			The directory for the new index out of range exception.
	 * @post	The directory involved in the new index out of range exception
	 * 			is set to the given directory.
	 * 			| new.getDirectory() == directory
	 */
	@Raw
	public IndexOutOfRangeException(Directory Directory) {
		this.Directory = Directory;
	}
	
	/**
	 * Return the directory involved in this index out of range exception.
	 */
	@Raw @Basic @Immutable
	public Directory getDirectory() {
		return Directory;
	}
	
	
}


