import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Directory extends FileSystem{
	
	
	public Directory(String name, Directory dir,  Boolean writable) {
		super(name,dir,writable);
		
		
	}
	
	List<String> list = new ArrayList<String>();
	
	public Directory(String name) {
		this(name,null,true);
	}
	
	public Directory(FileSystem dir, String name) {
		this(name,dir,true);
	}
	
	public String getItemAt(int index) {
		return ;
	}
	
	public String getItem(String name) {
		return ;
	}
	
	public boolean exists(String name) {
		return this.list.contains(name.toLowerCase());
	}
	
	public int getIndexOf(String name) {
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == name) {
            		break;
            		
            }
            index = index + 1;
        }
		return index;
		
	}
	
	public int getNbItems() {
		return this.list.size();
	}
	
	public boolean hasAsItem(String name) {
		return this.list.contains(name);
	}
	
	public boolean isDirectOrIndirectSubdirectoryOf(Directory directory) {
		return directory.list.contains(this.getName());
		
	}
	
    /**********************************************************
     * modificationTime
     **********************************************************/
	
	public Date getModificationTime() {
		return super.getModificationTime();
	}
	

	
	public void makeRoot() {
		super.makeRoot();
		
	}
	
	public void move(FileSystem dir) {
		super.move(dir);
	}
	
	public FileSystem getRoot() {
		return super.getRoot();
		
	}
	
	
	
}
