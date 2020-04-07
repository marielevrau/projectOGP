
public class IsRootDirectoryException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private final Directory Directory;
	
	public IsRootDirectoryException(Directory Directory) {
		this.Directory = Directory;
	}
	
	public Directory getDirectory() {
		return Directory;
	}

}
