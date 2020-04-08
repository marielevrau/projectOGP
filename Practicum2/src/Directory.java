import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Directory extends FileSystem{
	
	
	/**********************************************************
     * constructor
     **********************************************************/
	
	public Directory(String name, Directory dir,  Boolean writable) {
		super(name,dir,writable);
		
		
	}
	
	
	
	public Directory(String name) {
		this(name,null,true);
	}
	
	
	
	
	
	
	
	
	public Directory(Directory dir, String name) {
		this(name,dir,true);
	}
	
	
	/**********************************************************
     * list of subfiles and sub-directories
     **********************************************************/
	public List<FileSystem> list = new ArrayList<FileSystem>();
	
	
	
	
	public FileSystem sortListLexicograhpic(List<FileSystem> list, int l, int r){
		if(l < r) {
			int middle = (l + r) / 2;
			FileSystem leftList = sortListLexicograhpic(list, l, middle);
			FileSystem rightList = sortListLexicograhpic(list, middle + 1, r); 
		}
	}
	
	
	 
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
