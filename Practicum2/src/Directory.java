import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A class of directories.
 * 
 * @invar	Each directory must have a valid list.
 * 			| isValidList(getList())
 * 
* @author Jérôme D'hulst, Marie Levrau, Art Willems
*/
public class Directory extends FileSystem{
	
	
	/**********************************************************
     * constructor
     **********************************************************/
	/**
	 * Initialize a new directory as a  with a name, directory reference and writability.
	 * @param 	name
	 * 			The name of the new directory.
	 * @param 	dir
	 * 			The directory reference of the new directory with a null reference if the new directory
	 * 			is a root 
	 * @param	writable
	 * 			The writability of the new directory.
	 * @effect	This new directory is initialized as a fileSystem with the given name,
	 * 			the given directory reference and the given writability
	 * 			| super(name,dir,writable)
	 */
	public Directory(String name, Directory dir,  Boolean writable) {
		super(name,dir,writable);
		
		
	}
	
	
	/**
	 * Initialize a new directory with a given name.
	 * 
	 * @param 	name
	 * 			The name of the new directory
	 * @effect	This new directory is initialized with the given name, null directory reference and a true 
	 * 			writability.
	 * 			| this(name,null,true)
	 */
	public Directory(String name) {
		this(name,null,true);
	}
	
	
	
	/**
	 * Initialize a new directory with a given directory reference and a given name.
	 * 
	 * @param 	dir
	 * 			The directory reference of the new directory with a null reference if the	
	 * 			the new directory is a root file.
	 * @param 	name
	 * 			The name of the new directory.
	 * @effect	This new directory is initialized with the given directory reference,
	 * 			the given name and a true writability.
	 * 			| this(name,dir,true)
	 */
	public Directory(Directory dir, String name) {
		this(name,dir,true);
	}
	
	
	/**********************************************************
     * list of subfiles and subdirectories
     **********************************************************/
	
	/**
	 * A list that contains the objects from fileSystem as its subfiles/subdirectories.
	 */
	public List<FileSystem> list = new ArrayList<FileSystem>();
	
	
	 /**
	  * Return the list of this directory.
	  */
	@Basic @Raw
	public List<FileSystem> getList(){
		return list;
	}
	
	
	/**
	 * Return the subdirectory or subfile with given index in the directory list.
	 * 
	 * @param	index
	 * 			The index in the directory list.
	 * @return	the subdirectory or subfile with given index in the directory list.
	 * @throws	IndexOutOfRangeException(this)
	 *			The given index is out of range.
	 *			| (index >= list.size()) || (list.size() == 0 )
	 */
	public FileSystem getItemAt(int index) throws IndexOutOfRangeException {
		if ((index >= list.size()) || (list.size() == 0 )) {
			throw new IndexOutOfRangeException(this);
		}
		else {
			return list.get(index-1);
		}
		
	}
	
	
	
	/**
	 * Return the subdirectory or subfile with the given name.
	 * 
	 * @param	name
	 * 			The name in the directory list. 
	 * @return	the subdirectory or subfile with given name.
	 * @throws	NotInListException(this)
	 *			The given name is not in the list of this directory.
	 *			| ! this.exists(name)
	 */
	public FileSystem getItem(String name) throws NotInListException {
		if (this.exists(name)) {
			int index = 0;
		for (int i = 0;i<list.size();i++) {
			FileSystem FileDir = list.get(i);
			if (FileDir.getName()== name) {
				  break;
			}
			index = index +1;
			
		}
			return list.get(index);
		}
		else {
			throw new NotInListException(this);
		}
		
	}
	
	
	
	
	
	/**
	 * Check whether the given name exists in the directory list.
	 * 
	 * @param	name
	 * 			The name in the directory list.
	 * @return	True if and only if the name in the directory list is equal to the given name.
	 * 			|if ((list.get(i).getName()).toLowerCase() == name.toLowerCase())
	 *
	 */
	public boolean exists(String name) {
		for (int i = 0; i < list.size();i++) {
			if ((list.get(i).getName()).toLowerCase() == name.toLowerCase()) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * Return the index of the given name in the directory list.
	 * 
	 * @param	name
	 * 			The name in the directory list.
	 * @return	The index of the given name in the directory list.
	 * @throws	NotInListException(this)
	 *			The given name is not in the list of this directory.
	 *			| ! this.exists(name)
	 */
	public int getIndexOf(String name) throws NotInListException{
		if (this.exists(name)) {
			int index = 0;
			for (int i = 0; i < list.size(); i++) {
		        if (list.get(i).getName() == name) {
		        		index = i;
		        	break;
		        }
		    }
			return index + 1;}
		else {
			throw new NotInListException(this);
		}
		
	}
	
	
	
	/**
	 * Return the amount of subfiles and subdirectories in this directory.
	*/
	public int getNbItems() {
		return this.list.size();
	}
	
	
	
	
	/**
	 * Check whether this directory contains the given file or directory.
	 * 
	 * @param	fileDir
	 * 			The given file or directory.
	 * @return	True if and only if the directory list contains this file or directory.
	 * 			| if (this.list.contains(FileDir)) 
	 * @throws	NotInListException(this)
	 *			The given name is not in the list of this directory.
	 *			| ! (this.list.contains(FileDir))
	 */
	public boolean hasAsItem(FileSystem FileDir) throws NotInListException {
		if (this.list.contains(FileDir)) {
			return true;
		}
		else {
			throw new NotInListException(this);
		}
	}
	
	
	/**
	 * Check whether this directory is a direct or indirect subdirectory of the given directory.
	 * 
	 * @param	directory
	 * 			The given directory.
	 * @return	True if and only if this directory is a direct or indirect subdirectory of the given directory.
	 * @throws	IsRootDirectoryException(this)
	 *			This directory is a root directory. 
	 *			| (super.getRoot() == this)
	 * @throws	NotDirectOrIndirectSubdirectoryException(this)
	 *			This directory is not a direct or indirect subdirectory of the given directory. 
	 *			| !(super.getRoot() == this) && !(super.getRoot() == directory)
	 *
	 */
	public boolean isDirectOrIndirectSubdirectoryOf(Directory directory) throws IsRootDirectoryException, NotDirectOrIndirectSubdirectoryException {
		if(super.getRoot() == this) {
			throw new IsRootDirectoryException(this);
		} 
		else if (super.getRoot() == directory){
			return directory.list.contains(this);
		}
		else {
			throw new NotDirectOrIndirectSubdirectoryException(this);  
		}
		
	}
	
	
	/**********************************************************
     * delete
     **********************************************************/
	
	/**
	 * Delete this directory from the system.
	 * 
	 * @effect	If the directory list is empty, this directory will be deleted.
	 * 			| if (this.isListEmpty())
	 * 			| then (super.delete()) 
	 *@throws	DirListNotEmptyException(this)
	 *			The list of this directory is not empty.
	 *			| ! this.isListEmpty()
	 */
	@Override
	public void delete() throws DirListNotEmptyException {
		if (this.isListEmpty()) {
			super.delete();
			
		}
		else{ 
			throw new DirListNotEmptyException(this);
		}
	}
	
	/**
	 * Check whether the list of this directory is empty.
	 * 
	 * @return	True if the list is empty.
	 * 			| this.getList().isEmpty()
	 */
	public boolean isListEmpty() {
		return this.getList().isEmpty();
	}
	
	
	
	/**
	 * Remove a fileSystem from the list of this directory.
	 * 
	 * @param 	fileS
	 * 			The fileSystem to be removed.
	 * @post	The given fileSystem has been removed from the the list of this directory.
	 * @effect 	The modification time is updated.
     *         	| setModificationTime()
	 * @throws 	NotInListException(this)
	 * 			The list of this directory does not contain the given fileSystem.
	 * @throws 	FileSystemNotWritableException(this)
	 * 			This directory is not writable.
	 * 			| !(this.isWritable())
	 * @throws 	FileSystemInvalidException(this)
	 * 			The given filesystem is invalid.
	 * 			| !(this.isValidFileSystem(fileS))		
	 */
	public void remove(FileSystem fileS) throws NotInListException, FileSystemNotWritableException,FileSystemInvalidException{
		if (this.isWritable()) {
			if (this.list.contains(fileS)) {
				if (this.isValidFileSystem(fileS)){
					list.remove(fileS);
					fileS.setDir(null);
					this.setModificationTime();
				}
				else {
					throw new FileSystemInvalidException(this);
				}
				
			}
	
			else {
				throw new NotInListException(this);
			}
		}
		else {
			throw new FileSystemNotWritableException(this);
		}

	}
	
  
	
	/**
	 * Check whether the list of this directory is a valid list.
	 * 
	 *@return	True if the the list does not contain any null objects
	 *			or the object itself.
	 *			| result == 
	 *			|	(this.getList().contains(this) || this.getList().contains(null))
	 */
	public boolean isValidList() throws FileSystemInvalidException {
		if(this.exists(this.getName()) || this.getList().contains(null)) {
			return true; 
		}
		throw new FileSystemInvalidException(this); 
	}
	
	/**
	 * Check whether an object can be inserted into a directory list.
	 * @param 	fileSystem
	 * 			The fileSystem to be checked.
	 * @return	True if the fileSystem is not a null object or the fileSystem
	 * 			is not the directory itself.
	 * 			| fileSystem != null || fileSystem != this)
	 */
	public boolean isValidFileSystem(FileSystem fileSystem) throws FileSystemInvalidException {
		if(fileSystem != null || fileSystem.getName() != this.getName()) {
			return true; 
		}
		throw new FileSystemInvalidException(this); 
	}
	
}
