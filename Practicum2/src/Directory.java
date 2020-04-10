import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	 * @effect	This new directory is initialized as a filesystem with the given name,
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
     * list of subfiles and sub-directories
     **********************************************************/
	
	/**
	 * A list that contains the objects from filesystem as its subfiles/subdirectories.
	 */
	public List<FileSystem> list = new ArrayList<FileSystem>();
	
	
	
	
	public FileSystem sortListLexicograhpic(List<FileSystem> list, int l, int r){
		if(l < r) {
			int middle = (l + r) / 2;
			FileSystem leftList = sortListLexicograhpic(list, l, middle);
			FileSystem rightList = sortListLexicograhpic(list, middle + 1, r); 
		}
	}
	
	
	 /**
	  * Return the list of this directory.
	  */
	public List<FileSystem> getList(){
		return list;
	}
	
	
	public FileSystem getItemAt(int index) throws IndexOutOfRangeException {
		if ((index >= list.size()) || (list.size() == 0 )) {
			throw new IndexOutOfRangeException(this);
		}
		else {
			return list.get(index-1);
		}
		
	}
	
	
	
	
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
	
	
	
	
	
	
	public boolean exists(String name) {
		for (int i = 0; i < list.size();i++) {
			if ((list.get(i).getName()).toLowerCase() == name.toLowerCase()) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	
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
	
	
	
	
	public int getNbItems() {
		return this.list.size();
	}
	
	
	
	
	
	public boolean hasAsItem(FileSystem FileDir) throws NotInListException {
		if (this.list.contains(FileDir)) {
			return true;
		}
		else {
			throw new NotInListException(this);
		}
	}
	
	
	
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
     * Root and move
     **********************************************************/
	
	public void move(Directory dir) {
		setModificationTime();
		super.move(dir);
		
	}
	
	/**********************************************************
     * remove
     **********************************************************/
	
	public void remove(FileSystem fileS) throws NotInListException{
		if (this.list.contains(fileS)) {
			list.remove(fileS);
			fileS.setDir(null);
		}
		else {
			throw new NotInListException(this);
		}

	}
	
	/**********************************************************
     * delete
     **********************************************************/
	@Override
	/*!!! setModificationTime() nog toevoegen aan functie delete(), heb ik nu nog niet gedaan want delete() ging jérome nog aanpassen!!!*/
	public void delete() throws DirListNotEmptyException {
		if (this.isListEmpty()) {
			Directory ref = this.getDir();
			ref.remove(this);
			this.addToBin();
		}
		else{ 
			throw new DirListNotEmptyException(this);
		}
	}
	
	
	public boolean isListEmpty() {
		return this.getList().isEmpty();
	}
	
   

	
	
	
	
	
}
