
public class Directory extends FileSystem{
	
	
	public Directory(String name, FileSystem dir,  Boolean writable) {
		super(name,dir,writable);
		
	}
	
	public Directory(String name) {
		this(name,null,true);
	}
	
	public Directory(FileSystem dir, String name) {
		this(name,dir,true);
	}
	
	public boolean isSubMap() {
		return (this.getDir() != null);
	}
	
	public String getItemAt(int index) {
		return ;
	}
	
	public String getItem(String name) {
		return ;
	}
	
	public boolean exists() {
		return
	}
	
	public int getIndexOf(String name) {
		return
	}
	
	public int getNbItems() {
		return
	}
	
	public boolean hasAsItem(String name) {
		return
	}
	
	public boolean isDirectOrIndirectSubdirectoryOf(String name) {
		return
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
