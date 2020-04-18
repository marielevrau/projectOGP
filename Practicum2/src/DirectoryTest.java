/**
 *  A class with test function for public methods of the class Directory
 */
import Practicum2.src.*; 
import java.util.*;
import org.junit.*; 
import static org.junit.Assert.*; 


/**
 *
 * @author willemsart, Jérome D'hulst, Marie Levrau
 *
 */
public class DirectoryTest {
	
	
	Directory DirJustName, DirWritable, DirFull; 
	Directory OuterDir, InnerDir; 
	Date timeBeforeCreationWritable, timeAfterCreationWritable;
	Date timeAfterCreationNotWritable, timeBeforeCreationNotWritable; 
	
	@Before
	public void setUpFixture() {
		OuterDir = new Directory("OuterDir"); 
		InnerDir = new Directory("InnerDir"); 
		
		timeBeforeCreationWritable = new Date();
		DirJustName = new Directory("names");
		DirWritable = new Directory("writables", InnerDir, true); 
		timeAfterCreationWritable = new Date(); 
		
		timeBeforeCreationNotWritable = new Date(); 
		DirFull = new Directory("directory", OuterDir, false);
		timeAfterCreationNotWritable = new Date(); 
		
	}
	
	@Test
	public void extendedConstructorTest_DirJustName_LegalCase() {
		assertEquals("names", DirJustName.getName());
		assertTrue(DirJustName.isWritable()); 
		assertEquals(null, DirJustName.getDir()); 
		assertFalse(timeBeforeCreationWritable.after(DirJustName.getCreationTime())); 
		assertFalse(timeAfterCreationWritable.before(DirJustName.getCreationTime())); 
		assertNull(DirJustName.getModificationTime()); 
	}
	
	@Test
	public void extendedConstructorTest_DirJustName_IllegalCase() {
		timeBeforeCreationWritable = new Date(); 
		DirJustName = new Directory("whatsinaname?"); 
		timeAfterCreationWritable = new Date(); 
		assertEquals("new_fileSystem", DirJustName.getName()); 
		assertTrue(DirJustName.isWritable()); 
		assertEquals(null, DirJustName.getDir()); 
		assertFalse(timeBeforeCreationWritable.after(DirJustName.getCreationTime())); 
		assertFalse(timeAfterCreationWritable.before(DirJustName.getCreationTime())); 
		assertNull(DirJustName.getModificationTime()); 
	}
	
	@Test
	public void extendedConstructorTest_DirWritable_LegalCase() {
		assertEquals("writables", DirWritable.getName()); 
		assertEquals(InnerDir, DirWritable.getDir()); 
		assertTrue(DirWritable.isWritable()); 
		assertFalse(timeBeforeCreationWritable.after(DirWritable.getCreationTime())); 
		assertFalse(timeAfterCreationWritable.before(DirWritable.getCreationTime())); 
		assertNull(DirWritable.getModificationTime()); 
	}
	
	@Test
	public void extendedConstructorTest_DirWritable_IllegalCase() {
		timeBeforeCreationWritable = new Date();
		DirWritable = new Directory(InnerDir, "%writables%"); 
		timeAfterCreationWritable = new Date(); 
		assertTrue(FileSystem.isValidName(DirWritable.getName())); 
		assertEquals(InnerDir, DirWritable.getDir()); 
		assertTrue(DirWritable.isWritable()); 
		assertEquals("new_fileSystem", DirWritable.getName()); 
		assertNull(DirWritable.getModificationTime()); 
		assertFalse(timeBeforeCreationWritable.after(DirWritable.getCreationTime())); 
		assertFalse(DirWritable.getCreationTime().after(timeAfterCreationWritable)); 
	}
	
	@Test
	public void extendedConstructor_DirFull_LegalCase() {
		assertEquals("directory", DirFull.getName()); 
		assertFalse(DirFull.isWritable()); 
		assertEquals(OuterDir, DirFull.getDir()); 
		assertFalse(timeBeforeCreationNotWritable.after(DirFull.getCreationTime())); 
		assertFalse(DirFull.getCreationTime().after(timeAfterCreationNotWritable)); 
		assertNull(DirFull.getModificationTime()); 
		
	}
	
	@Test
	public void extendedConstructor_DirFull_IllegalCase() {
		timeBeforeCreationNotWritable = new Date(); 
		DirFull = new Directory("directory#!", OuterDir, false);
		timeAfterCreationNotWritable = new Date(); 
		assertTrue(Directory.isValidName(DirFull.getName())); 
		assertEquals(OuterDir, DirFull.getDir()); 
		assertFalse(timeBeforeCreationNotWritable.after(DirFull.getCreationTime())); 
		assertFalse(DirFull.getCreationTime().after(timeAfterCreationNotWritable)); 
		assertNull(DirFull.getModificationTime());
		
	}
	@Test
	public void testSetDir() {
		InnerDir.setDir(OuterDir);
		assertEquals(OuterDir, InnerDir.getDir());
	}
	
	@Test
	public void testForIsDirectOrInDirectSubdirectoryOf_LegalCase() {
		InnerDir = new Directory(OuterDir, "InnerDir"); 
		assertTrue(InnerDir.isDirectOrIndirectSubdirectoryOf(OuterDir)); 
	}
	
	@Test (expected = IsRootDirectoryException.class)
	public void testForIsDirectOrIndirectSubDirectoryOf_IllegalCaseNull() {
		InnerDir = new Directory(null, "InnerDir"); 
		assertFalse(InnerDir.isDirectOrIndirectSubdirectoryOf(OuterDir)); 
	}
	
	@Test (expected = NotDirectOrIndirectSubdirectoryException.class)
	public void testForIsDirectOrIndirectSubDirectoryOf_IllegalCaseNoSub() {
		assertFalse(DirFull.isDirectOrIndirectSubdirectoryOf(DirJustName)); 
	}
	
	@Test
	public void testForGetList(){
		// initialise some files and directories and add them to an empty Arraylist
		// the exact same files are added in the right way, so the two lists are exact copies of eachother
		OuterDir = new Directory("OuterDir", null, true); 
		InnerDir = new Directory("InnerDir",OuterDir, false); 
		File SomeFile = new File(InnerDir, "ogp", 20, false, "java"); 
		File AnotherFile = new File(DirFull, "p_and_o",45, false, "pdf");
		List<FileSystem> ToCompare = new ArrayList<FileSystem>();
		ToCompare.add(InnerDir);
		ToCompare.add(DirFull);
		ToCompare.add(AnotherFile);
		ToCompare.add(SomeFile); 
		assertEquals(ToCompare, OuterDir.getList()); 
	}
	
	@Test
	public void testForMove_LegalCase() {
		DirJustName.move(DirWritable);
		Directory dir = DirJustName.getDir(); 
		assertEquals(DirWritable, dir); 
		assertNotNull(DirJustName.getModificationTime()); 
		
	}
	
	@Test (expected = FileSystemNotWritableException.class)
	public void testForMove_NotWritableException() {
		DirJustName.move(DirFull);
		assertEquals(DirFull, DirJustName.getDir()); 
		assertNull(DirJustName.getModificationTime()); 
	}
	
	@Test (expected = AlreadyInListException.class)
	public void testForMove_AlreadyInListException() {
		DirJustName.move(null);
		assertEquals(null, DirJustName.getDir()); 
		assertNull(DirJustName.getModificationTime()); 
	}
	
	@Test
	public void testListEmpty_trueCase() {
		Directory NullDirectory = new Directory("empty_directory");
		assertTrue(NullDirectory.isListEmpty()); 
		
	}
	
	@Test
	public void testListEmpty_falseCase() {
		Directory NotNullDirectory = new Directory("justanotherdirectory"); 
		File File = new File(NotNullDirectory, "file", 35, true, "txt"); 
		NotNullDirectory.getList().add(File); 
		assertFalse(NotNullDirectory.isListEmpty()); 
	}
	@Test
	public void testIsValidList_legalCase() {
		assertTrue(OuterDir.isValidList()); 
	}
	
	@Test 
	public void testIsvalidList_IllegalCase() {
		OuterDir = new Directory("OuterDir", OuterDir, true); 
		assertFalse(OuterDir.isValidList()); 
	}
	
	@Test
	public void testIsValidFileSystem_LegalCase() {
		FileSystem valid = new FileSystem("valid", DirWritable, true); 
		assertTrue(OuterDir.isValidFileSystem(valid)); 
	}
	
	@Test
	public void testIsValidFileSystem_IllegalCaseA() { 
		FileSystem invalid = new FileSystem("invalid", DirWritable, true); 
		assertFalse(DirWritable.isValidFileSystem(invalid)); 
	}
	
	@Test
	public void testIsValidFileSystem_IllegalCaseB() {
		FileSystem invalid = null; 
		assertFalse(OuterDir.isValidFileSystem(invalid)); 
	}
	@Test
	public void testForRemove_LegalCase() {
		InnerDir.remove(DirWritable);
		assertTrue(InnerDir.isListEmpty()); 
		assertNotNull(InnerDir.getModificationTime()); 
	}
	
	@Test (expected = NotInListException.class)
	public void testForRemove_IllegalCaseA() {
		File dummy = new File("dummy"); 
		InnerDir.remove(dummy);
		assertNull(InnerDir.getModificationTime()); 
	}
	
	@Test (expected = FileSystemNotWritableException.class)
	public void testForRemove_IllegalCaseB(){
		OuterDir = new Directory("OuterDir", null, false); 
		OuterDir.remove(InnerDir); 
	}
	
	@Test
	public void testCanHaveAsItem_LegalCase() {
		// set the directory of InnerDir to OuterDir. Objects within InnerDir will be placed in OuterDir.getList()
		InnerDir.setDir(OuterDir);
		assertTrue(OuterDir.hasAsItem(DirWritable)); 
		
	}

	
	@Test (expected = NotInListException.class)
	public void testCanHaveAsItem_Illegalcase() {
		assertFalse(OuterDir.hasAsItem(DirJustName)); 
	}
	
	@Test
	public void testgetNbItems() {
		// set the directory of InnerDir to OuterDir. Objects within InnerDir will be placed in OuterDir.getList()
		InnerDir.setDir(OuterDir); 
		assertEquals(3, OuterDir.getNbItems()); 
	}

	
	@Test
	public void testGetNbItems_null() {
		Directory Empty = new Directory("empty"); 
		assertEquals(0, Empty.getNbItems()); 
	}

	@Test
	public void testGetItemAt_LegalCaseA() {
		//testing the accessing of files within one directory 
		File one, two, three; 
		one = new File(DirWritable, "one", 56, true, "pdf"); 
		two = new File(DirWritable, "two", 789, true, "java"); 
		three = new File(DirWritable, "three", 9, true, "txt"); 
		assertEquals(one, DirWritable.getItemAt(1)); 
		assertEquals(two, DirWritable.getItemAt(2)); 
		assertEquals(three, DirWritable.getItemAt(3)); 
	}

	
	@Test (expected = IndexOutOfRangeException.class)
	public void testGetItemAt_IllegalCaseA() {
		// make three new files for DirWritable
		File one, two, three; 
		one = new File(DirWritable, "one", 56, true, "pdf"); 
		two = new File(DirWritable, "two", 789, true, "java"); 
		three = new File(DirWritable, "three", 9, true, "txt"); 
		DirWritable.getItemAt(5); 
	}
	
	@Test
	public void testGetItemAt_LegalCaseB() {
		//testing the accessing of directories within directories
		InnerDir.setDir(OuterDir);
		assertEquals(DirFull, OuterDir.getItemAt(1)); 
	}
	
	@Test (expected = IndexOutOfRangeException.class)
	public void testGetItemAt_IllegalCaseB() {
		OuterDir.getItemAt(5); 
	}
	
	@Test
	public void testGetIndexOf_legalCaseA() {
		//testing the accessing of files within directories
		InnerDir.setDir(OuterDir);
		//make a new file located in InnerDir. Lexicographically, the new file is placed first in InnerDir.getList()
		File BeforeDirJustName;
		BeforeDirJustName = new File(InnerDir, "before", 10, true,"pdf"); 
		assertEquals(1, OuterDir.getIndexOf(BeforeDirJustName.getName())); 
	}

	
	@Test(expected = NotInListException.class)
	public void testGetIndexOf_IllegalCaseA() {
		// make a new root file
		File file = new File("file"); 
		InnerDir.getIndexOf(file.getName()); 
		
	}
	
	@Test
	public void testGetIndexOf_LegalCaseB() {
		//now we test to find the index of directory within a directory
		InnerDir = new Directory("InnerDir", OuterDir, true);
		assertEquals(2, OuterDir.getIndexOf("InnerDir")); 
	}
	
	@Test (expected = NotInListException.class)
	public void testGetIndexOf_IllegalCaseB() {
		// make a directory that isn't a subdirectory of OuterDir, trying to get index will result in NotInListException
		Directory DirNotInOuterDir = new Directory("dir", null, true ); 
		OuterDir.getIndexOf(DirNotInOuterDir.getName()); 
	}
	
	@Test
	public void testDeleteDirectory_LegalCase() {
		DirWritable.delete();
		assertFalse(InnerDir.getList().contains(DirWritable)); 
	}
	
	@Test (expected = AlreadyDeletedException.class)
	public void testDeleteDirectory_IllegalCase() {
		DirWritable.setDelete(true);
		DirWritable.delete();
	}
	
	
}

