package Practicum2.src; 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;


/**
 * @invar	Each filesystem must have a properly spelled name.
 * 			| isValidName(getName())
 * @invar   Each filesystem must have a valid creation time.
 *          | isValidCreationTime(getCreationTime())
 * @invar   Each filesystem must have a valid modification time.
 *          | canHaveAsModificationTime(getModificationTime())
 * @author 	Jerome D'hulst, Marie Levrau, Art Willems
 *
 */
public class FileSystem {
	
	
	/**
	 * Initialize a new fileSystem with a given name, directory reference and writabililty.
	 * 
	 * @param	name
	 * 			The name of the new fileSystem.
	 * @param 	dir
	 * 			The directory reference of the new fileSystem.
	 * @param 	writable
	 * 			The writability of the new fileSystem.
	 * @effect	The name of the fileSystem is set to the given name.
	 * 			If the given name is not valid, a default name is set.
	 * 			| setName(name)
	 * @effect	The directory reference of the fileSystem is set to the given directory.
	 * 			If the given directory is set to null then this file is root.
	 * 			| setDir(dir)
	 * @effect	The writability is set to the given flag.
	 * 			| setWritablity(writable)
	 */
	public FileSystem(String name, Directory dir, Boolean writable) {
		setName(name);
		setWritable(writable);
		setDir(dir);
	}
	
	
	
	
	/**********************************************************
     * name 
     **********************************************************/
	
	/**
	 * Variable referencing the name of this fileSystem.
	 */
	private String name = null;
	
	
	/**
	 * Check whether the given name is a legal name for a fileSystem.
	 * 
	 * @param 	name
	 * 			The name to be checked.
	 * @return	True if the given string is effective, not empty and consisting
	 * 			only of letters, digits, dots, hyphens and underscores; false otherwise.
	 * 			| result ==
	 * 			|	(name != null) && name.matches("[a-zA-Z_0-9.-]+")
	 */
	public static boolean isValidName(String name) {
		return (name != null && name.matches("[a-zA-Z_0-9.-]+"));
	}
	
	
	
	/**
	 * Set the name of this fileSystem to the given name.
	 * 
	 * @param	name
	 * 			The new name for this fileSystem.
	 * @post	If the given name is valid, the name of this fileSystem is set to the given name,
	 * 			otherwise the name of this fileSystem is set to a valid name (the default).
	 * 			| if (isValidName(name))
	 * 			|		then new.getName().equals(name)
	 * 			|		else new.getName().equals(getDefaultName())
	 */
	@Model @Raw
	private void setName(String name) {
		if (isValidName(name)) {
			this.name = name;
		}
		else {
			this.name = getDefaultName();
		}
	}
	
	
	
	
	/**
	 * Return the name for a new fileSystem which is to be used when the
	 * given name is not valid.
	 * 
	 * @return	A valid file name.
	 * 			| isValidName(result)
	 */
	@Model 
	private static String getDefaultName() {
		return "new_fileSystem";
	}
	
	
	
	/**
	 * Return the name of this fileSystem.
	 */
	@Basic @Raw
	public String getName() {
		return name;
	}
	
	
	
	/**
     * Change the name of this fileSystem to the given name.
     *
     * @param	name
     * 			The new name for this fileSystem.
     * @effect  The name of this fileSystem is set to the given name, 
     * 			if this is a valid name and the fileSystem is writable, 
     * 			otherwise there is no change.
     * 			| if (isValidName(name) && isWritable())
     *          | then setName(name)
     * @effect 	The modification time is updated.
     *         	| setModificationTime()
     * @throws  FileSystemNotWritableException(this)
     *          This fileSystem is not writable
     *          | ! isWritable() 
     */
	public void changeName(String name) throws FileSystemNotWritableException{
		if (isWritable()) {
			if(isValidName(name)) {
				setName(name);
				setModificationTime();
			}
			}
		else {
			throw new FileSystemNotWritableException(this);
		}
	}
	
	
	/**********************************************************
     * writable
     **********************************************************/
	
	/**
	 * Variable registering whether or not this filesystem is writable.
	 */
	private boolean isWritable = true;
	
	/**
	 * Check whether this file is writable.
	 */
	public boolean isWritable() {
		return isWritable;
	}

	
	/**
	 * Set the writability of this file to the given writability.
	 * 
	 * @param 	isWritable
	 * 			The new writability.
	 * @post	The new given writablity is registered as the new writability for 
	 * 			this fileSystem.
	 * 			| new.isWritable() == isWritable
	 */
	@Raw
	public void setWritable(boolean isWritable) {
		this.isWritable = isWritable;
	}
	
	
	
	/**********************************************************
     * directory reference
     **********************************************************/
	
	/**
	 * An object of the class directory that refers to the directory where
	 * this fileSystem is located.
	 */
	private Directory dir = null;
	
	
	/**
	 * Return the directory reference of this fileSystem.
	 */
	@Basic @Raw
	public Directory getDir() {
		return dir;
	}
	
	
	
	/**
	 * Set the directory reference of this fileSystem to the given directory.
	 * 
	 * @param 	dir
	 * 			The new directory reference.
	 * @post	The new given directory is registered as the new directory reference for 
	 * 			this fileSystem.
	 * 			| new.getDir() == dir
	 */
	@Raw @Model
	void setDir(Directory dir) {
		this.dir = dir;
		this.move(dir);
		
	}
	
	
	

	/**********************************************************
     * creationTime
     **********************************************************/

    /**
     * Variable referencing the time of creation.
     */
    private final Date creationTime = new Date();
   
    /**
     * Return the time at which this fileSystem was created.
     */
    @Raw @Basic @Immutable
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * Check whether the given date is a valid creation time.
     *
     * @param  	date
     *         	The date to check.
     * @return 	True if and only if the given date is effective and not
     * 			in the future.
     *         	| result == 
     *         	| 	(date != null) &&
     *         	| 	(date.getTime() <= System.currentTimeMillis())
     */
    public static boolean isValidCreationTime(Date date) {
    	return 	(date!=null) &&
    			(date.getTime()<=System.currentTimeMillis());
    }

    

    /**********************************************************
     * modificationTime
     **********************************************************/

    /**
     * Variable referencing the time of the last modification,
     * possibly null.
     */
    private Date modificationTime = null;
   
    /**
     * Return the time at which this fileSystem was last modified, that is
     * at which the fileSystem was last changed. If this fileSysteem has
     * not yet been modified after construction, null is returned.
     */
    
    @Raw @Basic
    public Date getModificationTime() {
        return modificationTime;
    }
	
	
    /**
     * Set the modification time of this fileSystem to the current time.
     *
     * @post   The new modification time is effective.
     *         | new.getModificationTime() != null
     * @post   The new modification time lies between the system
     *         time at the beginning of this method execution and
     *         the system time at the end of method execution.
     *         | (new.getModificationTime().getTime() >=
     *         |                    System.currentTimeMillis()) &&
     *         | (new.getModificationTime().getTime() <=
     *         |                    (new System).currentTimeMillis())
     */
    @Model
	protected void setModificationTime() {
        modificationTime = new Date();
    }
    
    /**
     * Check whether this fileSystem can have the given date as modification time.
     *
     * @param	date
     * 			The date to check.
     * @return 	True if and only if the given date is either not effective
     * 			or if the given date lies between the creation time and the
     * 			current time.
     *         | result == (date == null) ||
     *         | ( (date.getTime() >= getCreationTime().getTime()) &&
     *         |   (date.getTime() <= System.currentTimeMillis())     )
     */
    public boolean canHaveAsModificationTime(Date date) {
        return (date == null) ||
               ( (date.getTime() >= getCreationTime().getTime()) &&
                 (date.getTime() <= System.currentTimeMillis()) );
    }

    /**
     * Return whether this fileSystem and the given other fileSystem have an
     * overlapping use period.
     *
     * @param 	other
     *        	The other fileSystem to compare with.
     * @return 	False if the other fileSystem is not effective
     * 			False if the prime object does not have a modification time
     * 			False if the other fileSystem is effective, but does not have a modification time
     * 			otherwise, true if and only if the open time intervals of this fileSystem and
     * 			the other fileSystem overlap
     *        	| if (other == null) then result == false else
     *        	| if ((getModificationTime() == null)||
     *        	|       other.getModificationTime() == null)
     *        	|    then result == false
     *        	|    else 
     *        	| result ==
     *        	| ! (getCreationTime().before(other.getCreationTime()) && 
     *        	|	 getModificationTime().before(other.getCreationTime()) ) &&
     *        	| ! (other.getCreationTime().before(getCreationTime()) && 
     *        	|	 other.getModificationTime().before(getCreationTime()) )
     */
    public boolean hasOverlappingUsePeriod(FileSystem other) {
        if (other == null) return false;
        if(getModificationTime() == null || other.getModificationTime() == null) return false;
        return ! (getCreationTime().before(other.getCreationTime()) && 
        	      getModificationTime().before(other.getCreationTime()) ) &&
        	   ! (other.getCreationTime().before(getCreationTime()) && 
        	      other.getModificationTime().before(getCreationTime()) );
    }

   
	
	/**********************************************************
     * Root and move
     **********************************************************/
/**
 * Returns the root of this fileSystem.
 * 
 * @return the fileSystem that is the main reference of this fileSystem.
 * 		   If a fileSystem is a root fileSystem than the fileSystem itself 
 * 		   gets returned.
 */
@Basic @Raw
public FileSystem getRoot() {
	if (dir == null) {
		return this;
	}
	else {
		FileSystem root = this.getDir();
		while (root != null) {
			root = root.getDir();
		}
		return root;
		
	}
}





/**
 * This fileSystem becomes a root fileSystem.
 * 
 * @post	The directory reference of this fileSystem is set to null.
 * 			| setDir(null)
 * @effect  If the name is valid and the fileSystem is writable, the modification time 
 * 			of this fileSystem is updated.
 *      	| if (isValidName(name) && isWritable())
 *      	| then setModificationTime()
 * @throws 	FileSystemNotWritableException(this)
 * 			This fileSystem is not writable.
 * 			| !(this.isWritable())      
 * 			
 */
public void makeRoot() throws FileSystemNotWritableException {
	if (isWritable()) {
		setModificationTime();
		setDir(null);
	}
	else {
		throw new FileSystemNotWritableException(this);
	}
}


/**
 * Return the correct index in the reference list to add this fileSystem.
 * 
 * @param 	name
 * 			The name of the fileSystem of which the correct position in
 * 			the list of its reference is needed. 
 * @return	the correct index in the list where this file is lexicographically in the right position.
 * 
 */
public int seekAlphabeticPosition(String name) {
	List<FileSystem> list = this.getDir().getList();
	int pos = 0;
	for(int i = 0; i < list.size(); i++) {
		if(name.charAt(0) < list.get(i).getName().charAt(0)) {
			pos = i; 
		}
		if(name.charAt(0) == list.get(i).getName().charAt(0)) {
			for(int j = 1; j < list.get(i).getName().length();j++) {
				if(name.charAt(j) < list.get(i).getName().charAt(j)) {
					pos = i; 
				}
			}
		}
	}
	
	return pos; 
}


/**
 * 
 * Inserts a given fileSystem on a given position in the directory list.
 * 
 * @param 	position
 * 			The position where the fileSystem is inserted.
 * @param 	file
 * 			The fileSystem that is inserted into the directory list.
 * 
 */
public void insertNewIntoDirectory(int position, FileSystem file) {
	List<FileSystem> list = this.getDir().getList(); 
	list.add(position, file);
}
 
/**
 * Move this fileSystem to the list of the given directory.
 * 
 * @param 	fileSystem
 * 			The fileSystem to be moved.
 * @post	This fileSystem has been moved into the the list of the given directory.
 * 			| dir.insertNewIntoDirectory(pos, this)
 * @effect 	The modification time of this fileSystem is updated as well as the modification time of the given directory.
 *         	| this.setModificationTime()
 *         	| dir.setModificationTime()
 * @throws 	AlreadyInListException(this)
 * 			The given fileSystem is already in the list of this directory.
 * 			| (dir.isWritable()) && (dir.exists(this.getName()))
 * @throws 	FileSystemNotWritableException(this)
 * 			The directory where this fileSystem wants to move into, is not writable.
 * 			| ! (dir.isWritable())	
 * @throws 	FileSystemInvalidException(this)
 * 			This filesystem is invalid.
 * 			| ! (dir.isValidFileSystem(this))			
 */
public void move(Directory dir) throws FileSystemNotWritableException, AlreadyInListException, FileSystemInvalidException{
		if (dir.isWritable()) {
			if (dir.exists(this.getName())) {
				throw new AlreadyInListException(this);
			}
			else {
				if (dir.isValidFileSystem(this)) {
					int pos = dir.seekAlphabeticPosition(this.getName());
					dir.insertNewIntoDirectory(pos, this);
					this.setModificationTime();
					dir.setModificationTime();
				}
				else {
					throw new FileSystemInvalidException(this);
				}
			}
		}	
		else {
			throw new FileSystemNotWritableException(dir);
			}
	}

		
/**********************************************************
 * delete
 **********************************************************/
/**
 * Variable registering whether or not this fileSystem has been deleted.
 */
private boolean isDeleted = false;

/**
 * Check whether this fileSystem has been deleted.
 * 
 * @return	True if the fileSystem has been deleted.
 * 			| isDeleted
 */
public boolean isDeleted() {
	return isDeleted;
}

/**
 * 
 * @param 	isDeleted
 * 			The new status of isDeleted.
 * @post	The new given status to isDeleted is registered as the new status to isDeleted for 
 * 			this fileSystem.
 * 			| new.isDeleted() == isDeleted
 */
public void setDelete(boolean isDeleted) {
	this.isDeleted = isDeleted;
}


/**
 * 
 * Delete this fileSystem from the system.
 * 
 * @effect	This fileSystem has been deleted,
 * 			if the fileSystem has not already been deleted
 * 			| if (this.isDeleted() == false)
 * 			| then (ref.remove(this) && this.setDelete(true))
 * @effect 	The modification time is updated.
 *         	| setModificationTime()
 * @throws	AlreadyDeletedException(this)
 *			The fileSystem has already been deleted.
 *			| this.isDeleted() == true
 */
public void delete() throws AlreadyDeletedException{
	if (this.isDeleted() == false) {
		Directory ref = this.getDir();
    	ref.remove(this);
    	this.setDelete(true);	
    	this.setModificationTime();
	}
	else {
		throw new AlreadyDeletedException(this);	
		
	}
}





}

