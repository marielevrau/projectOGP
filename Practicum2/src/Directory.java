
public class Directory extends FileSystem{
	
	
	public Directory(FileSystem dir, String name, Boolean writable) {
		super(dir,name,writable);
		
	}
	
	public Directory(String name) {
		this(null,name,true);
	}
	
	public Directory(FileSystem dir, String name) {
		this(dir,name,true);
	}
	
	
	public int getItemAt() {
		
	}
	
	public String getItem() {
		
	}
	
	public boolean exists() {
		
	}
	
	public int getIndexOf() {
		
	}
	
	public int getNbItems() {
		
	}
	
	public boolean hasAsItem() {
		
	}
	
	public boolean isDirectOrIndirectSubdirectoryOf() {
		
	}
	
}
