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
	
	
	public FileSystem getItemAt(int index) {
		return list.get(index-1);
	}
	
	
	
	
	public FileSystem getItem(String name) {
		for (int i = 0;i<list.size();i++) {
			FileSystem FileDir = list.get(i);
			if (FileDir.getName()== name) {
				return  FileDir;
			}
		}
		return null;
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
		return index;
		
	}
	
	
	
	
	
	
	
	
	public int getNbItems() {
		return this.list.size();
	}
	
	
	
	
	
	public boolean hasAsItem(FileSystem FileDir) {
		return this.list.contains(FileDir);
	}
	
	
	
	
	
	
	public boolean isDirectOrIndirectSubdirectoryOf(Directory directory) {
		return directory.list.contains(this);
		
	}
	
	
	
    /**********************************************************
     * modificationTime
     **********************************************************/
	
	public Date getModificationTime() {
		return super.getModificationTime();
	}
	

	
	
	
	
	
}
