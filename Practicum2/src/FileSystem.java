import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;


/**
 * @invar	Each file must have a properly spelled name.
 * 			| isValidName(getName())
 * @invar   Each file must have a valid creation time.
 *          | isValidCreationTime(getCreationTime())
 * @invar   Each file must have a valid modification time.
 *          | canHaveAsModificationTime(getModificationTime())
 * @author J�r�me D'hulst
 *
 */
public class FileSystem {
	
	
	/**
	 * Initialize a new filesystem with a given name, directory reference and writablilty.
	 * 
	 * @param	name
	 * 			The name of the new file.
	 * @param 	dir
	 * 			The directory reference of the new file.
	 * @param 	writable
	 * 			The writabilty of the new file.
	 * @effect	The name of the file is set to the given name
	 * 			If the given name is not valid, a default name is set.
	 * 			| setName(name)
	 * @effect	The directory reference of the file is set to the given directory.
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
	 * Variable referencing the name of this filesystem.
	 */
	private String name = null;
	
	
	/**
	 * Check whether the given name is a legal name for a filesystem.
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
	 * Set the name of this filesystem to the given name.
	 * 
	 * @param	name
	 * 			The new name for this filesystem.
	 * @post	If the given name is valid, the name of this filesystem is set to the given name,
	 * 			otherwise the name of this filesystem is set to a valid name (the default).
	 * 			| if (isValidName(name))
	 * 			|		then new.getName().equals(name)
	 * 			|		else new.getName().equals(getDefaultName())
	 */
	private void setName(String name) {
		if (isValidName(name)) {
			this.name = name;
		}
		else {
			this.name = getDefaultName();
		}
	}
	
	
	
	/**
	 * Return the name for a new filesystem which is to be used when the
	 * given name is not valid.
	 * 
	 * @return	A valid file name.
	 * 			| isValidName(result)
	 */
	private static String getDefaultName() {
		return "new_fileSystem";
	}
	
	
	
	/**
	 * Return the name of this filesystem.
	 */
	public String getName() {
		return name;
	}
	
	
	
	/**
     * Change the name of this filesystem to the given name.
     *
     * @param	name
     * 			The new name for this filesystem.
     * @effect  The name of this filesystem is set to the given name, 
     * 			if this is a valid name and the filesystem is writable, 
     * 			otherwise there is no change.
     * 			| if (isValidName(name) && isWritable())
     *          | then setName(name)
     * @effect  If the name is valid and the fileSystem is writable, the modification time 
     * 			of this filesystem is updated.
     *          | if (isValidName(name) && isWritable())
     *          | then setModificationTime()
     * @throws  FileNotWritableException(this)
     *          This filesystem is not writable
     *          | ! isWritable() 
     */
	
	public void changeName(String name) throws FileNotWritableException{
		if (isWritable()) {
			if(isValidName(name)) {
				setName(name);
				setModificationTime();
			}
			}
		else {
			throw new FileNotWritableException(this);
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
	 * 
	 * @param 	isWritable
	 * 			The new writability.
	 * @post	The new given writablity is registered as the new writability for 
	 * 			this filesystem.
	 * 			| new.isWritable() == isWritable
	 */
	public void setWritable(boolean isWritable) {
		this.isWritable = isWritable;
	}
	
	
	
	/**********************************************************
     * directory reference
     **********************************************************/
	
	/**
	 * An object of the class directory that refers to the directory where this file is 
	 * located.
	 */
	private Directory dir = null;
	
	
	/**
	 * 
	 * @param 	dir
	 * 			The new directory reference.
	 *  
	 */
	void setDir(Directory dir) {
		this.dir = dir;
	}
	
	public Directory getDir() {
		return dir;
	}
	
	/*
	public static boolean isValidFileSystem(FileSystem dir) {
		return ()
	}
	*/
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
     * at which the name or size was last changed. If this fileSysteem has
     * not yet been modified after construction, null is returned.
     */
    
    /* !!!!!!!!!!!! specificatie hierboven 'at which the name or size was last changed' naam is zowel voor File als Directory, size enkel voor File, en delete() en move() enkel voor Directory? specifiek bij File of Directory zetten of hierboven ook nog eens vermelden??!!!!!! */
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






public void makeRoot() {
	setDir(null);
	}



public int seekAlphabeticPosition(String string) {
	List<FileSystem> list = this.getDir().getList();
	int pos = 0;
	for(int i = 0; i < list.size(); i++) {
		if(string.charAt(0) < list.get(i).getName().charAt(0)) {
			pos = i; 
		}
		if(string.charAt(0) == list.get(i).getName().charAt(0)) {
			for(int j = 1; j < list.get(i).getName().length();j++) {
				if(string.charAt(j) < list.get(i).getName().charAt(j)) {
					pos = i; 
				}
			}
		}
	}
	
	return pos; 
}

public Directory insertNewIntoDirectory(int position, FileSystem file) {
	List<FileSystem> list = this.getDir().getList(); 
	list.add(position, file);
}
 
	
public void move(Directory dir) throws FileNotWritableException, AlreadyInListException{
		if (dir.isWritable()) {
			if (dir.exists(this.getName())) {
				throw new AlreadyInListException(this);
			}
			else {
				int pos = dir.seekAlphabeticPosition(this.getName());
				dir.insertNewIntoDirectory(pos, this.name);
				
			}
		}	
	

		else {
			throw new FileNotWritableException(dir);
			}
	}

		
/**********************************************************
 * delete
 **********************************************************/

public static List<FileSystem> bin = new ArrayList<FileSystem>();

public List<FileSystem> getBin(){
	return bin;
}

public void addToBin() {
	bin.add(this);
}



public void delete() {
	
}













}

