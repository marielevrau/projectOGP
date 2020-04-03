
public class AlreadyInListException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final FileSystem FileSystem;
	
	public AlreadyInListException(FileSystem FileSystem) {
		this.FileSystem = FileSystem;
	}
	
	public FileSystem getFileSystem() {
		return FileSystem;
	}
	
}
