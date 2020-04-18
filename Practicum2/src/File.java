package Practicum2.src; 
import be.kuleuven.cs.som.annotate.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Date;

/**
 * A class of files.
 *
 * 
 * @invar	Each file must have a valid size.
 * 			| isValidSize(getSize())
 * @invar 	Each file must have a valid type.
 * 			| isValidType(getType())
 * 
 * @author  Jérôme D'hulst, Marie Levrau, Art Willems
 * @version 3.1
 * 
 * @note		See Coding Rule 48 for more info on the encapsulation of class invariants.
 */
public class File extends FileSystem {

    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Initialize a new file with directory reference, name, size, writability and type.
     * @param	dir
     *			The directory where the new file is located.
     * @param  	name
     *         	The name of the new file.
     * @param  	size
     *         	The size of the new file.
     * @param  	writable
     *         	The writability of the new file.
     * @param 	type
     * 			The type of the new file.
     * @effect	This new file is initialized as a fileSystem with a given name, a given
     * 			directory reference and a given writability.
     * 			| super(name,dir,writable)
     * @effect	The size is set to the given size (must be valid)
     * 			| setSize(size)
     * @effect	The writability is set to the given flag
     * 			| setWritable(writable)
     * @effect	The type is set to the given type (must be valid)
     * 			| setType(type)
     * @post    The new creation time of this file is initialized to some time during
     *          constructor execution.
     *          | (new.getCreationTime().getTime() >= System.currentTimeMillis()) &&
     *          | (new.getCreationTime().getTime() <= (new System).currentTimeMillis())
     * @post    The new file has no time of last modification.
     *          | new.getModificationTime() == null
     */
	public File(Directory dir, String name,int size, boolean writable, String type) {
        super(name,dir,writable);
        setSize(size);
        setType(type);
    }

    /**
     * Initialize a new file with given name.
     *
     * @param   name
     *          The name of the new file.
     * @effect  This new file is initialized with the given name, a zero size
     * 			,true writability, given directory reference and a default type "txt".
     *         | this(null,name,0,true,'txt')
     */
    public File(String name) {
        this(null,name,0,true,"txt");
    }
    
    
    /**
     * Initialize a new file with the given name and type.
     * @param 	name
     * 			The name of the new file.
     * @param 	type
     * 			The type of the new file.
     * @effect	This new file is initialized with a 'null' directory reference
     * 			(meaning this is a root-file), a given name, a zero size,
     * 			a true writability and a given type.
     * 			|this(null,name,0,true,type)
     * 			
     */
    public File(String name, String type) {
    	this(null,name,0,true,type);
    }
    
    
    
    /**
     * Initialize a new file with the given name, size, writability and type.
     * @param 	name
     * 			The name of the new file.
     * @param	size
     * 			The size of the new file.
     * @param   writable
     * 			The writability of the new file.
     * @param 	type
     * 			The type of the new file.
     * @effect	This new file is initialized with a given name, size, writability and type.
     * 			|this(null,name,size,writable,type)
     * 			
     */
    public File(String name, int size, boolean writable, String type) {
    	this(null,name,size,writable,type);
    }
    
    
    
    /**
     * Initialize a new file with the given directory reference, name and type.
     * @param 	dir
     * 			The directory reference of the new file.
     * @param 	name
     * 			The name of the new file.
     * @param 	type
     * 			The type of the new file.
     * @effect	This new file is initialized with a given directory reference, name, a zero size
     * 			a true writability and a type.
     * 			|this(dir,name,0,true,type);
     */
    public File(Directory dir, String name, String type) {
    	this(dir,name,0,true,type);
    }
    
    
    
    
    /**********************************************************
     * size - nominal programming
     **********************************************************/
    
    /**
     * Variable registering the size of this file (in bytes).
     */
    private int size = 0;
    
    /**
     * Variable registering the maximum size of any file (in bytes).
     */
    private static final int maximumSize = Integer.MAX_VALUE;


    /**
     * Return the size of this file (in bytes).
     */
    @Raw @Basic 
    public int getSize() {
        return size;
    }
    
    /**
     * Set the size of this file to the given size.
     *
     * @param  size
     *         The new size for this file.
     * @pre    The given size must be legal.
     *         | isValidSize(size)
     * @post   The given size is registered as the size of this file.
     *         | new.getSize() == size
     */
    @Raw @Model 
    private void setSize(int size) {
        this.size = size;
    }
   
    /**
     * Return the maximum file size.
     */
    @Basic @Immutable
    public static int getMaximumSize() {
        return maximumSize;
    }

    /**
     * Check whether the given size is a valid size for a file.
     *
     * @param  size
     *         The size to check.
     * @return True if and only if the given size is positive and does not
     *         exceed the maximum size.
     *         | result == ((size >= 0) && (size <= getMaximumSize()))
     */
    public static boolean isValidSize(int size) {
        return ((size >= 0) && (size <= getMaximumSize()));
    }

    /**
     * Increases the size of this file with the given delta.
     *
     * @param   delta
     *          The amount of bytes by which the size of this file
     *          must be increased.
     * @pre     The given delta must be strictly positive.
     *          | delta > 0
     * @effect  The size of this file is increased with the given delta.
     *          | changeSize(delta)
     *          
     */
    public void enlarge(int delta) throws FileSystemNotWritableException {
        changeSize(delta);
    }

    /**
     * Decreases the size of this file with the given delta.
     *
     * @param   delta
     *          The amount of bytes by which the size of this file
     *          must be decreased.
     * @pre     The given delta must be strictly positive.
     *          | delta > 0
     * @effect  The size of this file is decreased with the given delta.
     *          | changeSize(-delta)
     */
    public void shorten(int delta) throws FileSystemNotWritableException {
        changeSize(-delta);
    }

    /**
     * Change the size of this file with the given delta.
     *
     * @param  delta
     *         The amount of bytes by which the size of this file
     *         must be increased or decreased.
     * @pre    The given delta must not be 0
     *         | delta != 0
     * @effect The size of this file is adapted with the given delta.
     *         | setSize(getSize()+delta)
     * @effect The modification time is updated.
     *         | setModificationTime()
     * @throws FileSystemNotWritableException(this)
     *         This file is not writable.
     *         | ! isWritable()
     */
    @Model 
    private void changeSize(int delta) throws FileSystemNotWritableException{
        if (isWritable()) {
            setSize(getSize()+delta);
            setModificationTime();            
        }else{
        	throw new FileSystemNotWritableException(this);
        }
    }

   
   
    
    /**********************************************************
     * type
     **********************************************************/
    
	/**
	 * A list that contains the different types of a file.
	 */
    public static List<String> typeList = new ArrayList<String>(Arrays.asList("txt","java","pdf"));

	/**
	 * Variable referencing the type of this file.
	 */
    public String type = null;
    
    /**
     * Set the type of this file to the given type.
     * 
     * @param 	type
     * 			The new type for this file.
     * @post	If the given type is valid, the type of this file
     * 			is set to the given type.
     * 			| if (isValidType(type))
     * 					then new.getType().equals(type)
     * @throws 	IllegalTypeException(this)
     * 			This is not a legal type.
     * 			| ! isValidType()
     */
    @Model
    private void setType(String type) throws IllegalTypeException{
    	if (isValidType(type)) {
    		this.type = type;
    	}
    	else {
    		throw new IllegalTypeException(this);
    		
    	}
    	
    }
    
    /**
     * Check whether the given type is a legal type for a file.
     * @param	type
     * 			The type to be checked
     * @return 	True if the given type is either 'txt', 'pdf' or a 'java' type.
     * 			|
     */
    public boolean isValidType(String type) {
    	for (int i = 0;i < typeList.size(); i++) {
    		if (type == typeList.get(i)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    
    /**
     * Return the type of this file.
     */
    @Basic @Raw
    public String getType() {
    	return type;
    }
    

    
}
