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

}

