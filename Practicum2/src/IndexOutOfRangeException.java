
public class IndexOutOfRangeException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private final Directory Directory;
	
	public IndexOutOfRangeException(Directory Directory) {
		this.Directory = Directory;
	}
	
	public FileSystem getFileSystem() {
		return Directory;
	}
	
	
}

}
