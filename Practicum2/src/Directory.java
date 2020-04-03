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
	
	public List<FileSystem> getList(){
		return list;
	}
	
	
	public FileSystem getItemAt(int index) {
		return list.get(index-1);
	}
	
	
	
	
	public FileSystem getItem(String name) throws NotInListException {
		if (this.exists(name)) {
		for (int i = 0;i<list.size();i++) {
			FileSystem FileDir = list.get(i);
			if (FileDir.getName()== name) {
				return  FileDir;
			}
		}
		}
		else {
			throw new NotInListException(this);
		}
	}
	
	
	
	
	
	
	public boolean exists(String name) {
		for (int i = 0; i < list.size();i++) {
			if (list.get(i).getName()==name) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	
	public int getIndexOf(String name) {
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName() == name) {
            		index = i;
            	break;
            }
        }
		return index + 1;
		
	}
	
	
	
	
	
	
	
	
	public int getNbItems() {
		return this.list.size();
	}
	
	
	
	
	
	public boolean hasAsItem(FileSystem FileDir) {
		return this.list.contains(FileDir);
	}
	
	
	
	
	
	/* exception voor als het een root directory is of een directory die geen subdirectory is*/
	public boolean isDirectOrIndirectSubdirectoryOf(Directory directory) {
		return directory.list.contains(this);
		
	}
	
	/**********************************************************
     * remove
     **********************************************************/
	
	public void remove(FileSystem fileS) {
		list.remove(fileS);
		fileS.setDir(null);
	}
	
	/**********************************************************
     * delete
     **********************************************************/
	@Override
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
	
    /**********************************************************
     * modificationTime
     **********************************************************/
	
	public Date getModificationTime() {
		return super.getModificationTime();
	}
	

	
	
	
	
	
}
