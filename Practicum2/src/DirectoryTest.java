/**
 *  A class with test function for public methods of the class Directory
 */
package Practicum2.src;

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
		timeBeforeCreationWritable = new Date();
		DirJustName = new Directory("names");
		DirWritable = new Directory(InnerDir, "writables"); 
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
		assertTrue(timeBeforeCreationWritable.before(DirJustName.getCreationTime())); 
		assertTrue(timeAfterCreationWritable.after(DirJustName.getCreationTime())); 
		assertNull(DirJustName.getModificationTime()); 
	}
	
	@Test
	public void extendedConstructorTest_DirJustName_IllegalCase() {
		timeBeforeCreationWritable = new Date(); 
		DirJustName = new Directory("whatsinaname?"); 
		timeAfterCreationWritable = new Date(); 
		//if the directory name is invalid, will there be a new default name chosen? 
		assertTrue(DirJustName.isWritable()); 
		assertEquals(null, DirJustName.getDir()); 
		assertTrue(timeBeforeCreationWritable.before(DirJustName.getCreationTime())); 
		assertTrue(timeAfterCreationWritable.after(DirJustName.getCreationTime())); 
		assertNull(DirJustName.getModificationTime()); 
	}
	
	@Test
	public void extendedConstructorTest_DirWritable_LegalCase() {
		assertEquals("writables", DirWritable.getName()); 
		assertEquals(InnerDir, DirWritable.getDir()); 
		assertTrue(DirWritable.isWritable()); 
		assertTrue(timeBeforeCreationWritable.before(DirWritable.getCreationTime())); 
		assertTrue(timeAfterCreationWritable.after(DirWritable.getCreationTime())); 
		assertNull(DirWritable.getModificationTime()); 
	}
	
	@Test
	public void extendedConstructorTest_DirWritable_IllegalCase() {
		timeBeforeCreationWritable = new Date();
		DirWritable = new Directory(InnerDir, "%writables%"); 
		timeAfterCreationWritable = new Date(); 
		assertFalse(DirWritable.isValidName(DirWritable.getName())); 
		assertEquals(InnerDir, DirWritable.getDir()); 
		assertTrue(DirWritable.isWritable()); 
		// is this the desired implementation? 
		assertEquals("new_filesystem", DirWritable.getName()); 
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
		assertFalse(DirFull.isValidName(DirFull.getName())); 
		assertEquals(OuterDir, DirFull.getDir()); 
		assertFalse(timeBeforeCreationNotWritable.after(DirFull.getCreationTime())); 
		assertFalse(DirFull.getCreationTime().after(timeAfterCreationNotWritable)); 
		assertNull(DirFull.getModificationTime());
		
	}
	
	@Test
	public void testForIsDirectOrInDirectSubdirectoryOf_LegalCase() {
		InnerDir = new Directory(OuterDir, "InnerDir"); 
		assertTrue(InnerDir.isDirectOrIndirectSubdirectoryOf(OuterDir)); 
	}
	
	@Test(expected = IsRootDirectoryException.class)
	public void testForIsDirectOrIndirectSubDirectoryOf_IllegalCaseNull() {
		InnerDir = new Directory(null, "InnerDir"); 
		assertFalse(InnerDir.isDirectOrIndirectSubdirectoryOf(OuterDir)); 
	}
	
	@Test (expected = NotDirectOrIndirectSubdirectoryException.class)
	public void testForIsDirectOrIndirectSubDirectoryOf_IllegalCaseNoSub() {
		assertFalse(DirJustName.isDirectOrIndirectSubdirectoryOf(DirFull)); 
	}
	@Test
	public void testForGetList(){
		// initialise some files and directories and add them to an empty Arraylist
		OuterDir = new Directory("OuterDir", null, true); 
		InnerDir = new Directory("InnerDir",OuterDir, false); 
		File SomeFile = new File(InnerDir, "ogp", 20, false, "java"); 
		File AnotherFile = new File(DirFull, "p_and_o",45, false, "pdf"); 
		List<FileSystem> ToCompare = new ArrayList<FileSystem>();
		ToCompare.add(OuterDir); 
		ToCompare.add(DirFull);
		ToCompare.add(AnotherFile);
		ToCompare.add(InnerDir);
		ToCompare.add(SomeFile); 
		assertEquals(ToCompare, OuterDir.getList()); 
	}
	
	@Test
	public void testForMove_LegalCase() {
		DirJustName.move(DirWritable);
		Directory dir = DirJustName.getDir(); 
		assertEquals(DirWritable, dir); 
		
	}
	
	@Test (expected = FileNotWritableException.class)
	public void testForMove_NotWritableException() {
		DirJustName.move(DirFull);
		assertEquals(DirFull, DirJustName.getDir()); 
	}
	
	@Test (expected = AlreadyInListException.class)
	public void testForMove_AlreadyInListException() {
		DirJustName.move(null);
		assertEquals(null, DirJustName.getDir()); 
		
	}
	
	@Test
	public void testListEmpty_trueCase() {
		Directory NullDirectory = new Directory("empty_directory");
		assertTrue(NullDirectory.isListEmpty()); 
		
	}
	
	//normally, following test should be applicable for FileSystem, but only for Directories
	@Test
	public void testListEmpty_falseCase() {
		Directory NotNullDirectory = new Directory("justanotherdirectory"); 
		File File = new File(NotNullDirectory, "file", 35, true, "txt"); 
		assertFalse(NotNullDirectory.isListEmpty()); 
	}
	
	@Test
	public void testForRemove_LegalCase() {
		InnerDir.remove(DirWritable);
		assertTrue(InnerDir.isListEmpty()); 
		assertNotNull(InnerDir.getModificationTime()); 
	}
	
	@Test (expected = NotInListException.class)
	public void testForRemove_IllegalCase() {
		File dummy = new File("dummy"); 
		InnerDir.remove(dummy);
		assertNull(InnerDir.getModificationTime()); 
	}
	
	@Test
	public void testCanHaveAsItem_LegalCase() {
		InnerDir.setDir(OuterDir);
		assertTrue(OuterDir.hasAsItem(DirWritable)); 
		
	}
	
	@Test (expected = NotInListException.class)
	public void testCanHaveAsItem_Illegalcase() {
		assertFalse(OuterDir.hasAsItem(DirJustName)); 
	}
	
	@Test
	public void testgetNbItems() {
		InnerDir.setDir(OuterDir); 
		assertEquals(3,OuterDir.getNbItems()); 
	}
	
	
	
}

