package filesystem;
import filesystem.exception.*;
import static org.junit.Assert.*;
import java.util.Date;


import org.junit.*;

/**
 * A JUnit test class for testing the non-private methods of the File Class.
 * Inherited methods are tested in the JUnit test for the superclass.
 *   
 * @author  Tommy Messelis
 * @version 2.0
 */
public class FileTest {

	private static File fileStringTypeIntBoolean, fileStringType, fileDirStringTypeIntBoolean, fileDirStringType;
	
	private static Directory terminatedDirectory;
	private static File terminatedFile;
	private static Date timeBefore, timeAfter;
	
	@SuppressWarnings("unused")
	private static File root_file_writable, fileA_writable, fileB_unwritable, fileD_writable, fileInDirC_1_unwritable, fileInDirE_1_writable, fileInDirCDefaultName_writable, fileInDirEDefaultName_unwritable;
	@SuppressWarnings("unused")
	private static Directory root_dir_empty_writable, root_dir_filled_writable, root_dir_empty_unwritable;
	private static Directory dirC_filled_writable, dirE_filled_unwritable, dirInDirC_2_empty_writable, dirInDirE_2_empty_unwritable;
	/*
	 * Note that we suppress the warnings for unused variables. In fact, the variables themselves are not unused.
	 * We do given them values (references to objects) and build up a file system structure with them.
	 * The warning comes because we never read out the values of these variables, and as such, they are 'unused'.
	 * 
	 * If you don't suppress the warnings, Eclipse will show yellow exclamation marks on the lines 
	 * where these variables are declared.
	 */
	
	
	@BeforeClass
	public static void setUpImmutableFixture() {
		terminatedDirectory = new Directory("terminatedDirectory",true);
		terminatedDirectory.terminate();
		terminatedFile = new File("terminatedFile", Type.JAVA, 500,true);
		terminatedFile.terminate();
	}
	
	@Before
	public void setUpFixture() {
		// set up a file system structure in some levels:
		// ROOT items: {root_file, root_dir_empty_writable, root_dir_filled_writable, root_dir_empty_unwritable} 
		// inside root_dir_filled_writable: {fileA, fileB, dirC, fileD, dirE}
		// dir C is filled and writable, dirE is filled and not writable
		// inside dirC: {fileInDirC_1, dirInDirC_2, fileInDirCDefaultName_writable}
		// inside dirE: {fileInDirE_1, dirInDirE_2, fileInDirEDefaultName_unwritable}
		
		root_file_writable = new File("root_file",Type.TEXT,100,true);
		root_dir_empty_writable = new Directory("root_dir_empty_writable", true);
		root_dir_filled_writable = new Directory("root_dir_filled_writable", true);
		root_dir_empty_unwritable = new Directory("root_dir_empty_unwritable", false);
		
		fileA_writable = new File(root_dir_filled_writable, "A_file", Type.PDF, 100, true);
		fileB_unwritable = new File(root_dir_filled_writable, "B_file", Type.TEXT, 50, false);
		fileD_writable = new File(root_dir_filled_writable, "D_file", Type.PDF, 200, true);
		dirC_filled_writable = new Directory(root_dir_filled_writable, "C_dir", true);
		dirE_filled_unwritable = new Directory(root_dir_filled_writable, "E_dir", true);
		
		fileInDirC_1_unwritable = new File(dirC_filled_writable, "C1_file", Type.TEXT, 500, false);
		dirInDirC_2_empty_writable = new Directory(dirC_filled_writable, "C2_dir", true);
		fileInDirCDefaultName_writable = new File(dirC_filled_writable, "new_diskitem", Type.TEXT, 900, true);
		
		fileInDirE_1_writable = new File(dirE_filled_unwritable, "E1_file", Type.JAVA, 2500, true);
		dirInDirE_2_empty_unwritable = new Directory(dirE_filled_unwritable, "E2_dir", false);
		fileInDirEDefaultName_unwritable = new File(dirE_filled_unwritable, "new_diskitem", Type.PDF, 700, false);
		
		dirE_filled_unwritable.setWritable(false);
		
	}
	
	/**
	 * Test the first (extended) constructor
	 * (invalid names have been covered in the superclass.)
	 */
	
	@Test
	public void testConstructorStringTypeIntBoolean_Legal() {
		timeBefore = new Date();
		sleep();
		fileStringTypeIntBoolean = new File("new_file",Type.TEXT,100,true);
		sleep();
		timeAfter = new Date();
		
		//effect super
			assertEquals("new_file",fileStringTypeIntBoolean.getName());
			assertTrue(fileStringTypeIntBoolean.isWritable());
			assertTrue(fileStringTypeIntBoolean.isRoot());
			assertFalse(timeBefore.after(fileStringTypeIntBoolean.getCreationTime()));
			assertFalse(fileStringTypeIntBoolean.getCreationTime().after(timeAfter));
			assertNull(fileStringTypeIntBoolean.getModificationTime());
			assertFalse(fileStringTypeIntBoolean.isTerminated());
		//effect setSize
		assertEquals(100, fileStringTypeIntBoolean.getSize());
		//post 
		assertEquals(Type.TEXT, fileStringTypeIntBoolean.getType());
		
	}
	
	/**
	 * Test the second (confined) constructor
	 */
	
	@Test
	public void testConstructorStringType_Legal() {
		timeBefore = new Date();
		sleep();
		fileStringType = new File("new_file",Type.TEXT);
		sleep();
		timeAfter = new Date();
		
		//effect super
			assertEquals("new_file",fileStringType.getName());
			assertTrue(fileStringType.isWritable());
			assertTrue(fileStringType.isRoot());
			assertFalse(timeBefore.after(fileStringType.getCreationTime()));
			assertFalse(fileStringType.getCreationTime().after(timeAfter));
			assertNull(fileStringType.getModificationTime());
			assertFalse(fileStringType.isTerminated());
		//effect setSize
		assertEquals(0, fileStringType.getSize());
		//post 
		assertEquals(Type.TEXT, fileStringType.getType());
		
	}
	
	/**
	 * Test the third (extended) constructor
	 */
	
	@Test
	public void testConstructorDirStringTypeIntBoolean_Legal() {
		// add to a filled writable root directory
		// make it come in between file B and dir C
		
		int nbItemsBefore = root_dir_filled_writable.getNbItems();
		
		// assert the before situation (just to be sure)
		assertEquals(nbItemsBefore,5);
		assertEquals(root_dir_filled_writable.getItemAt(1), fileA_writable);
		assertEquals(root_dir_filled_writable.getItemAt(2), fileB_unwritable);
		assertEquals(root_dir_filled_writable.getItemAt(3), dirC_filled_writable);
		assertEquals(root_dir_filled_writable.getItemAt(4), fileD_writable);
		assertEquals(root_dir_filled_writable.getItemAt(5), dirE_filled_unwritable);
		
		timeBefore = new Date();
		sleep();
		fileDirStringTypeIntBoolean = new File(root_dir_filled_writable,"BZ_filename",Type.TEXT,20,true);
		sleep();
		timeAfter = new Date();
		
		int nbItemsAfter = root_dir_filled_writable.getNbItems();
		
		//effect super
			//effect setname
			assertEquals("BZ_filename",fileDirStringTypeIntBoolean.getName());
			//effect setwritable
			assertTrue(fileDirStringTypeIntBoolean.isWritable());
			//effect setParentDir
			assertEquals(root_dir_filled_writable,fileDirStringTypeIntBoolean.getParentDirectory());
			//effect parent.addAsItem
				//posts
				assertTrue(root_dir_filled_writable.hasAsItem(fileDirStringTypeIntBoolean));
				assertEquals(nbItemsBefore + 1, nbItemsAfter);		
				assertEquals(root_dir_filled_writable.getItemAt(1), fileA_writable);
				assertEquals(root_dir_filled_writable.getItemAt(2), fileB_unwritable);
				assertEquals(root_dir_filled_writable.getItemAt(3), fileDirStringTypeIntBoolean);
				assertEquals(root_dir_filled_writable.getItemAt(4), dirC_filled_writable);
				assertEquals(root_dir_filled_writable.getItemAt(5), fileD_writable);
				assertEquals(root_dir_filled_writable.getItemAt(6), dirE_filled_unwritable);
				//effect setModTime
				assertNotNull(root_dir_filled_writable.getModificationTime());
				assertFalse(timeBefore.after(root_dir_filled_writable.getModificationTime()));
				assertFalse(root_dir_filled_writable.getModificationTime().after(timeAfter));
			//post
			assertFalse(timeBefore.after(fileDirStringTypeIntBoolean.getCreationTime()));
			assertFalse(fileDirStringTypeIntBoolean.getCreationTime().after(timeAfter));
			assertNull(fileDirStringTypeIntBoolean.getModificationTime());
			assertFalse(fileDirStringTypeIntBoolean.isTerminated());
		//effect setSize
		assertEquals(20, fileDirStringTypeIntBoolean.getSize());
		//post 
		assertEquals(Type.TEXT, fileDirStringTypeIntBoolean.getType());

	}
	
	/**
	 * Test the fourth (confined) constructor
	 */
	
	@Test
	public void testConstructorDirStringType_Legal() {
		// add to a filled writable root directory
		// make it come in between file B and dir C
		
		int nbItemsBefore = root_dir_filled_writable.getNbItems();
		
		// assert the before situation (just to be sure)
		assertEquals(nbItemsBefore,5);
		assertEquals(root_dir_filled_writable.getItemAt(1), fileA_writable);
		assertEquals(root_dir_filled_writable.getItemAt(2), fileB_unwritable);
		assertEquals(root_dir_filled_writable.getItemAt(3), dirC_filled_writable);
		assertEquals(root_dir_filled_writable.getItemAt(4), fileD_writable);
		assertEquals(root_dir_filled_writable.getItemAt(5), dirE_filled_unwritable);
		
		timeBefore = new Date();
		sleep();
		fileDirStringType = new File(root_dir_filled_writable,"BZ_filename",Type.TEXT,20,true);
		sleep();
		timeAfter = new Date();
		
		int nbItemsAfter = root_dir_filled_writable.getNbItems();
		
		//effect super
			//effect setname
			assertEquals("BZ_filename",fileDirStringType.getName());
			//effect setwritable
			assertTrue(fileDirStringType.isWritable());
			//effect setParentDir
			assertEquals(root_dir_filled_writable,fileDirStringType.getParentDirectory());
			//effect parent.addAsItem
				//posts
				assertTrue(root_dir_filled_writable.hasAsItem(fileDirStringType));
				assertEquals(nbItemsBefore + 1, nbItemsAfter);		
				assertEquals(root_dir_filled_writable.getItemAt(1), fileA_writable);
				assertEquals(root_dir_filled_writable.getItemAt(2), fileB_unwritable);
				assertEquals(root_dir_filled_writable.getItemAt(3), fileDirStringType);
				assertEquals(root_dir_filled_writable.getItemAt(4), dirC_filled_writable);
				assertEquals(root_dir_filled_writable.getItemAt(5), fileD_writable);
				assertEquals(root_dir_filled_writable.getItemAt(6), dirE_filled_unwritable);
				//effect setModTime
				assertNotNull(root_dir_filled_writable.getModificationTime());
				assertFalse(timeBefore.after(root_dir_filled_writable.getModificationTime()));
				assertFalse(root_dir_filled_writable.getModificationTime().after(timeAfter));
			//post
			assertFalse(timeBefore.after(fileDirStringType.getCreationTime()));
			assertFalse(fileDirStringType.getCreationTime().after(timeAfter));
			assertNull(fileDirStringType.getModificationTime());
			assertFalse(fileDirStringType.isTerminated());
		//effect setSize
		assertEquals(20, fileDirStringType.getSize());
		//post 
		assertEquals(Type.TEXT, fileDirStringType.getType());

	}
	
	// All illegal cases have been tested in the superclass. Both size and type are programmed in a nominal way,
	// so the extra illegal cases are strictly forbidden and do not need to be tested.
	

	/**
	 * Test toString
	 */
	@Test
	public void testToString() {
		assertEquals(fileA_writable.toString(),"A_file.pdf");
		assertEquals(fileB_unwritable.toString(),"B_file.txt");
		assertEquals(fileInDirE_1_writable.toString(),"E1_file.java");
	}
	
	
	/**
	 * Test canBeTerminated
	 * closed specification now: test all cases!
	 */
	
	@Test
	public void testCanBeTerminated_Legal() {
		assertTrue(root_file_writable.canBeTerminated());
		assertTrue(fileA_writable.canBeTerminated());
	}	
	@Test
	public void testCanBeTerminated_Illegal_alreadyTerminated() {
		assertFalse(terminatedFile.canBeTerminated());
	}
	@Test
	public void testCanBeTerminated_Illegal_notWritable() {
		assertFalse(root_dir_empty_unwritable.canBeTerminated());
	}
	@Test
	public void testCanBeTerminated_Illegal_parentNotWritable() {
		assertFalse(fileInDirE_1_writable.canBeTerminated());
	}
	
	/**
	 * Test isValidType
	 */
	@Test
	public void testIsValidType() {
		assertFalse(File.isValidType(null));
		assertTrue(File.isValidType(Type.JAVA));
		assertTrue(File.isValidType(Type.TEXT));
		assertTrue(File.isValidType(Type.PDF));
	}
	
	/**
	 * Test isValidSize
	 */
	@Test
	public void testIsValidSize_LegalCase() {
		assertTrue(File.isValidSize(0));
		assertTrue(File.isValidSize(File.getMaximumSize()/2));
		assertTrue(File.isValidSize(File.getMaximumSize()));
	}
	@Test
	public void testIsValidSize_IllegalCase() {
		assertFalse(File.isValidSize(-1));
		if (File.getMaximumSize() < Integer.MAX_VALUE) {
			assertFalse(File.isValidSize(File.getMaximumSize()+1));
		}
	}

	/**
	 * Test enlarge
	 */
	@Test
	public void testEnlarge_LegalCase() {
		timeBefore = new Date();
		sleep();
		fileD_writable.enlarge(300);
		sleep();
		timeAfter = new Date();		
		
		//effect changeSize:
		//effect setSize
		assertEquals(500,fileD_writable.getSize());
		//effect setModTime
		assertNotNull(fileD_writable.getModificationTime());
		assertFalse(fileD_writable.getModificationTime().before(timeBefore));
		assertFalse(timeAfter.before(fileD_writable.getModificationTime()));
	
	}
	@Test (expected = DiskItemNotWritableException.class)
	public void testEnlarge_IllegalCase_NotWritable() {
		fileB_unwritable.enlarge(300);
	}
	// precondition blocks negative or too large increments in size, we don't need to test this.
	
	/**
	 * Test shorten
	 */
	@Test
	public void testshorten_LegalCase() {
		timeBefore = new Date();
		sleep();
		fileD_writable.shorten(50);
		sleep();
		timeAfter = new Date();		
		
		//effect changeSize:
		//effect setSize
		assertEquals(150,fileD_writable.getSize());
		//effect setModTime
		assertNotNull(fileD_writable.getModificationTime());
		assertFalse(fileD_writable.getModificationTime().before(timeBefore));
		assertFalse(timeAfter.before(fileD_writable.getModificationTime()));
	
	}
	@Test (expected = DiskItemNotWritableException.class)
	public void testshorten_IllegalCase_NotWritable() {
		fileB_unwritable.shorten(10);
	}
	// precondition blocks negative or too large decrements in size, we don't need to test this.
	

	/**
	 * Test canHaveAsParentDirectory
	 * 
	 * The complete spec should be tested now that it is in closed form!
	 */
	@Test
	public void testCanHaveAsParentDirectory() {
		// copy from superclass
		assertTrue(terminatedFile.canHaveAsParentDirectory(null));
		assertFalse(terminatedFile.canHaveAsParentDirectory(dirInDirC_2_empty_writable));
		assertTrue(fileInDirCDefaultName_writable.canHaveAsParentDirectory(null));
		assertFalse(fileInDirCDefaultName_writable.canHaveAsParentDirectory(terminatedDirectory));
		// additional: (any non terminated directory can be the parent of a file)
		assertTrue(root_file_writable.canHaveAsParentDirectory(dirInDirE_2_empty_unwritable));
			
	}
	
	
	/**
	 * Auxiliary method for testing purposes
	 */
	
	private void sleep() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
