import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

public class FileSystem {
	
	
	
	public FileSystem(String name, Directory dir, Boolean writable) {
		setName(name);
		setWritable(writable);
		setDir(dir);
	}
	
	
	
	
	/**********************************************************
     * name 
     **********************************************************/
	
	private String name = null;
	
	
	
	public static boolean isValidName(String name) {
		return (name != null && name.matches("[a-zA-Z_0-9.-]+"));
	}
	
	
	
	
	private void setName(String name) {
		if (isValidName(name)) {
			this.name = name;
		}
		else {
			this.name = getDefaultName();
		}
	}
	
	
	
	
	private static String getDefaultName() {
		return "new_fileSystem";
	}
	
	
	
	
	public String getName() {
		return name;
	}
	
	
	
	
	
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
	
	private boolean isWritable = true;
	
	public boolean isWritable() {
		return isWritable;
	}

	
	public void setWritable(boolean isWritable) {
		this.isWritable = isWritable;
	}
	
	
	
	/**********************************************************
     * directory reference
     **********************************************************/
	private Directory dir = null;
	
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
     * Return the time at which this file was created.
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
     * Return the time at which this file was last modified, that is
     * at which the name or size was last changed. If this file has
     * not yet been modified after construction, null is returned.
     */
    @Raw @Basic
    public Date getModificationTime() {
        return modificationTime;
    }
	
	
    /**
     * Set the modification time of this file to the current time.
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
    private void setModificationTime() {
        modificationTime = new Date();
    }

	
	
	
	/**********************************************************
     * Root and move
     **********************************************************/

public FileSystem getRoot() {
	if (dir == null) {
		return this;
	}
	else {
		return dir;
	}
}






public void makeRoot() {
	setDir(null);
	}
	

public int seekAlphabeticPosition(String string) {

}

public Directory insertNewIntoDirectory(int position, String s) {
	
}

	
public void move(Directory dir) throws FileNotWritableException, AlreadyInListException{
		if (dir.isWritable()) {
			if (dir.exists(this.getName())) {
				throw new AlreadyInListException(this);
			}
			else {
				int pos = dir.seekAlphabeticPosition(this.getName());
				dir.insertNewIntoDirectory(pos, this.getName());
				setModificationTime();
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

