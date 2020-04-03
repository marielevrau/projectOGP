
public class NotInListException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final FileSystem FileSystem;
	
	public NotInListException(FileSystem FileSystem) {
		this.FileSystem = FileSystem;
	}
	
	public FileSystem getDirectory() {
		return FileSystem;
	}
	
}
