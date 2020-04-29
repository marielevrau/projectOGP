package filesystem;
import static org.junit.Assert.*;
import java.util.Date;


import org.junit.*;

/**
 * A JUnit test class for testing the non-private methods of the Directory Class.
 * Inherited methods are tested in the JUnit test for the superclass.
 *   
 * @author  Tommy Messelis
 * @version 2.0
 */
public class DirectoryTest {

	private static Directory dirStringBoolean, dirString, dirDirStringBoolean, dirDirString;
	
	private static Directory terminatedDirectory;
	private static File terminatedFile;
	private static Date timeBefore, timeAfter;
	
	@SuppressWarnings("unused")
	private static File root_file_writable, fileA_writable, fileB_unwritable, fileD_writable, fileInDirC_1_unwritable, fileInDirE_1_writable, fileInDirCDefaultName_writable, fileInDirEDefaultName_unwritable;
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
	 * Test the first constructor
	 * (invalid names have been covered in the superclass.)
	 */
	
	@Test
	public void testConstructorStringBoolean_Legal() {
		timeBefore = new Date();
		sleep();
		dirStringBoolean = new Directory("new_dir",false);
		sleep();
		timeAfter = new Date();
		
		//effect super
			assertEquals("new_dir",dirStringBoolean.getName());
			assertFalse(dirStringBoolean.isWritable());
			assertTrue(dirStringBoolean.isRoot());
			assertFalse(timeBefore.after(dirStringBoolean.getCreationTime()));
			assertFalse(dirStringBoolean.getCreationTime().after(timeAfter));
			assertNull(dirStringBoolean.getModificationTime());
			assertFalse(dirStringBoolean.isTerminated());
		//post 
		assertEquals(0, dirStringBoolean.getNbItems());
		
	}
	
	/**
	 * Test the second constructor
	 */
	
	@Test
	public void testConstructorString_Legal() {
		timeBefore = new Date();
		sleep();
		dirString = new Directory("new_dir");
		sleep();
		timeAfter = new Date();
		
		//effect super
			assertEquals("new_dir",dirString.getName());
			assertTrue(dirString.isWritable());
			assertTrue(dirString.isRoot());
			assertFalse(timeBefore.after(dirString.getCreationTime()));
			assertFalse(dirString.getCreationTime().after(timeAfter));
			assertNull(dirString.getModificationTime());
			assertFalse(dirString.isTerminated());
		//post 
		assertEquals(0, dirString.getNbItems());
		
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
		dirDirStringBoolean = new Directory(root_dir_filled_writable,"BZ_dirname",false);
		sleep();
		timeAfter = new Date();
		
		int nbItemsAfter = root_dir_filled_writable.getNbItems();
		
		//effect super
			//effect setname
			assertEquals("BZ_dirname",dirDirStringBoolean.getName());
			//effect setwritable
			assertFalse(dirDirStringBoolean.isWritable());
			//effect setParentDir
			assertEquals(root_dir_filled_writable,dirDirStringBoolean.getParentDirectory());
			//effect parent.addAsItem
				//posts
				assertTrue(root_dir_filled_writable.hasAsItem(dirDirStringBoolean));
				assertEquals(nbItemsBefore + 1, nbItemsAfter);		
				assertEquals(root_dir_filled_writable.getItemAt(1), fileA_writable);
				assertEquals(root_dir_filled_writable.getItemAt(2), fileB_unwritable);
				assertEquals(root_dir_filled_writable.getItemAt(3), dirDirStringBoolean);
				assertEquals(root_dir_filled_writable.getItemAt(4), dirC_filled_writable);
				assertEquals(root_dir_filled_writable.getItemAt(5), fileD_writable);
				assertEquals(root_dir_filled_writable.getItemAt(6), dirE_filled_unwritable);
				//effect setModTime
				assertNotNull(root_dir_filled_writable.getModificationTime());
				assertFalse(timeBefore.after(root_dir_filled_writable.getModificationTime()));
				assertFalse(root_dir_filled_writable.getModificationTime().after(timeAfter));
			//post
			assertFalse(timeBefore.after(dirDirStringBoolean.getCreationTime()));
			assertFalse(dirDirStringBoolean.getCreationTime().after(timeAfter));
			assertNull(dirDirStringBoolean.getModificationTime());
			assertFalse(dirDirStringBoolean.isTerminated());
		//post 
		assertEquals(0, dirDirStringBoolean.getNbItems());
		
	}
	
	/**
	 * Test the fourth (confined) constructor
	 */
	
	@Test
	public void testConstructorDirString_Legal() {
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
		dirDirString = new Directory(root_dir_filled_writable,"BZ_dirname");
		sleep();
		timeAfter = new Date();
		
		int nbItemsAfter = root_dir_filled_writable.getNbItems();
		
		//effect super
			//effect setname
			assertEquals("BZ_dirname",dirDirString.getName());
			//effect setwritable
			assertTrue(dirDirString.isWritable());
			//effect setParentDir
			assertEquals(root_dir_filled_writable,dirDirString.getParentDirectory());
			//effect parent.addAsItem
				//posts
				assertTrue(root_dir_filled_writable.hasAsItem(dirDirString));
				assertEquals(nbItemsBefore + 1, nbItemsAfter);		
				assertEquals(root_dir_filled_writable.getItemAt(1), fileA_writable);
				assertEquals(root_dir_filled_writable.getItemAt(2), fileB_unwritable);
				assertEquals(root_dir_filled_writable.getItemAt(3), dirDirString);
				assertEquals(root_dir_filled_writable.getItemAt(4), dirC_filled_writable);
				assertEquals(root_dir_filled_writable.getItemAt(5), fileD_writable);
				assertEquals(root_dir_filled_writable.getItemAt(6), dirE_filled_unwritable);
				//effect setModTime
				assertNotNull(root_dir_filled_writable.getModificationTime());
				assertFalse(timeBefore.after(root_dir_filled_writable.getModificationTime()));
				assertFalse(root_dir_filled_writable.getModificationTime().after(timeAfter));
			//post
			assertFalse(timeBefore.after(dirDirString.getCreationTime()));
			assertFalse(dirDirString.getCreationTime().after(timeAfter));
			assertNull(dirDirString.getModificationTime());
			assertFalse(dirDirString.isTerminated());
		//post 
		assertEquals(0, dirDirString.getNbItems());
			
			
	}
	
	// All illegal cases have been tested in the superclass. Both size and type are programmed in a nominal way,
	// so the extra illegal cases are strictly forbidden and do not need to be tested.
	

	
	/**
	 * Test canBeTerminated
	 * closed specification now: test all cases!
	 */
	
	@Test
	public void testCanBeTerminated_Legal() {
		assertTrue(dirInDirC_2_empty_writable.canBeTerminated());
		assertTrue(root_dir_empty_writable.canBeTerminated());
	}	
	@Test
	public void testCanBeTerminated_IllegalNotEmpty() {
		assertFalse(dirC_filled_writable.canBeTerminated());
	}	
	@Test
	public void testCanBeTerminated_Illegal_alreadyTerminated() {
		assertFalse(terminatedDirectory.canBeTerminated());
	}
	@Test
	public void testCanBeTerminated_Illegal_notWritable() {
		assertFalse(root_dir_empty_unwritable.canBeTerminated());
	}
	@Test
	public void testCanBeTerminated_Illegal_parentNotWritable() {
		//we don't yet have such a directory, so make 
		//dirInDirE_2_empty_unwritable first writable
		dirInDirE_2_empty_unwritable.setWritable(true);
		assertFalse(dirInDirE_2_empty_unwritable.canBeTerminated());
	}
	
	
	/**
	 * Test hasAsItem
	 */
	@Test
	public void testHasAsItem_AllCases() {
		assertTrue(dirC_filled_writable.hasAsItem(fileInDirC_1_unwritable));
		assertTrue(dirC_filled_writable.hasAsItem(dirInDirC_2_empty_writable));
		assertTrue(dirC_filled_writable.hasAsItem(fileInDirCDefaultName_writable));
		assertFalse(dirC_filled_writable.hasAsItem(dirC_filled_writable));
		assertFalse(root_dir_filled_writable.hasAsItem(dirInDirC_2_empty_writable));
	}
	
	/**
	 * Test getInsertionIndexOf
	 */
	@Test
	public void testGetInsertionIndexOf_Legal() {
		//first, last, middle, empty dir
		assertEquals(1,root_dir_empty_writable.getInsertionIndexOf(fileB_unwritable));
		assertEquals(1,dirC_filled_writable.getInsertionIndexOf(fileB_unwritable));
		assertEquals(3,dirC_filled_writable.getInsertionIndexOf(fileD_writable));
		assertEquals(6,root_dir_filled_writable.getInsertionIndexOf(fileInDirCDefaultName_writable));	
	}
	@Test (expected = IllegalArgumentException.class)
	public void testGetInsertionIndexOf_Illegal_null() {
		root_dir_filled_writable.getInsertionIndexOf(null);	
	}
	@Test (expected = IllegalArgumentException.class)
	public void testGetInsertionIndexOf_Illegal_terminated() {
		root_dir_filled_writable.getInsertionIndexOf(terminatedFile);	
	}
	@Test (expected = IllegalArgumentException.class)
	public void testGetInsertionIndexOf_Illegal_alreadyAMember() {
		root_dir_filled_writable.getInsertionIndexOf(fileB_unwritable);	
	}
	
	/**
	 * Test hasProperItems (only bidirectionality
	 */
	@Test
	public void testHasProperItems_Legal() {
		assertTrue(root_dir_filled_writable.hasProperItems());
		for(int i=1; i<=root_dir_filled_writable.getNbItems(); i++ ) {
			assertTrue(root_dir_filled_writable.canHaveAsItem(root_dir_filled_writable.getItemAt(i)));
			assertTrue(root_dir_filled_writable.getItemAt(i).getParentDirectory() == root_dir_filled_writable);
		}
	}
	// Illegal case can not be tested, we cannot add an item that does not have this directory as parent
	
	/**
	 * Test canHaveAsItemAt (only ordering)
	 */
	@Test
	public void testcanHaveAsItemAt_Legal() {
		// for items already present in the directory:
		for(int i=1; i<=root_dir_filled_writable.getNbItems(); i++ ) {
			assertTrue(root_dir_filled_writable.canHaveAsItem(root_dir_filled_writable.getItemAt(i)));
			assertTrue(i == 1 || root_dir_filled_writable.getItemAt(i).isOrderedAfter(root_dir_filled_writable.getItemAt(i-1)));
			assertTrue(i == root_dir_filled_writable.getNbItems() || root_dir_filled_writable.getItemAt(i+1).isOrderedAfter(root_dir_filled_writable.getItemAt(i)));
		}
		// for items not yet in the directory:
		// begin, middle and end
		assertTrue(root_dir_filled_writable.canHaveAsItemAt(fileInDirCDefaultName_writable, 6));
		assertTrue(root_dir_filled_writable.canHaveAsItemAt(dirInDirC_2_empty_writable, 3));// C2_ comes before C_
		assertTrue(dirInDirC_2_empty_writable.canHaveAsItemAt(dirInDirC_2_empty_writable, 1));	
	}
	
	/**
	 * Test canHaveAsItem (only content)
	 */
	@Test
	public void testcanHaveAsItem_AllCases() {
		// for items already in the directory:
		assertTrue(root_dir_filled_writable.canHaveAsItem(fileA_writable));
		int count = 0;
		for(int i=1; i<=root_dir_filled_writable.getNbItems(); i++) {
			if (root_dir_filled_writable.getItemAt(i).getName().equals("A_file")) count++;
		}
		assertEquals(count, 1);
		// for items not in the directory: name should not exist yet
		assertTrue(root_dir_filled_writable.canHaveAsItem(fileInDirE_1_writable));
		assertFalse(dirC_filled_writable.canHaveAsItem(fileInDirEDefaultName_unwritable));
	}
	
	
	/**
	 * Test addAsItem
	 * 
	 * legal case is impossible to test from here, must be done from within the controlling class.
	 * Illegal cases are tested here. 
	 */
	@Test
	public void testAddAsItem_Legal() {
		// The legal case is only callable from within the controlling class.
		// We are not capable of building an item here that is allowed to be added.
	}
	@Test (expected = IllegalArgumentException.class)
	public void testAddAsItem_Illegal_null() {
		root_dir_filled_writable.addAsItem(null);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testAddAsItem_Illegal_alreadyPresent() {
		root_dir_filled_writable.addAsItem(fileA_writable);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testAddAsItem_Illegal_cantHaveItem() {
		dirC_filled_writable.addAsItem(terminatedFile);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testAddAsItem_Illegal_parentNotRight() {
		dirC_filled_writable.addAsItem(fileInDirE_1_writable);
	}
		
	/**
	 * Test removeAsItem
	 * 
	 * again, auxiliary method, only illegal cases to test.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testRemoveAsItem_Illegal_null() {
		root_dir_filled_writable.removeAsItem(null);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testRemoveAsItem_Illegal_notPresent() {
		root_dir_filled_writable.removeAsItem(fileInDirE_1_writable);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testRemoveAsItem_Illegal_parentNotRight() {
		dirC_filled_writable.removeAsItem(fileInDirCDefaultName_writable);
	}

	
	/**
	 * Test containsDiskItemWithName (idem exists)
	 */
	@Test
	public void testContainsDiskItemWithName_allCases() {
		assertTrue(root_dir_filled_writable.containsDiskItemWithName(fileA_writable.getName()));
		assertFalse(root_dir_filled_writable.containsDiskItemWithName(fileInDirE_1_writable.getName()));	
	}
	
	
	/**
	 * Test getItem
	 */
	@Test
	public void testGetItem_Legal() {
		//important: the name of the resulting item should match, that is all, the item may be different if they have the same name
		assertTrue(root_dir_filled_writable.getItem(fileA_writable.getName()).getName().equalsIgnoreCase(fileA_writable.getName()));
		assertTrue(dirC_filled_writable.getItem(fileInDirEDefaultName_unwritable.getName()).getName().equalsIgnoreCase(fileInDirEDefaultName_unwritable.getName()));
		assertNull(root_dir_filled_writable.getItem("nonamehere"));
	}
	
	
	/**
	 * Test getIndexOf
	 */
	@Test
	public void testGetIndexOf_legal() {
		assertEquals(1,root_dir_filled_writable.getIndexOf(fileA_writable));
		assertEquals(2,root_dir_filled_writable.getIndexOf(fileB_unwritable));
		assertEquals(3,root_dir_filled_writable.getIndexOf(dirC_filled_writable));
		assertEquals(4,root_dir_filled_writable.getIndexOf(fileD_writable));
		assertEquals(5,root_dir_filled_writable.getIndexOf(dirE_filled_unwritable));	
	}
	@Test (expected = IllegalArgumentException.class)
	public void testGetIndexOf_illegal() {
		root_dir_filled_writable.getIndexOf(fileInDirC_1_unwritable);
	}
	
	/**
	 * Test restoreOrderAfterNameChangeAt
	 * 
	 * auxiliary method, legal case has been tested in the 'controlling' class
	 * test only illegal cases here
	 */
	@Test (expected = IndexOutOfBoundsException.class)
	public void testRestoreOrderAfterNameChange_illegal() {
		root_dir_filled_writable.restoreOrderAfterNameChangeAt(-1);
	}
	@Test (expected = IndexOutOfBoundsException.class)
	public void testRestoreOrderAfterNameChange_illegal2() {
		root_dir_filled_writable.restoreOrderAfterNameChangeAt(0);
	}
	@Test (expected = IndexOutOfBoundsException.class)
	public void testRestoreOrderAfterNameChange_illegal3() {
		root_dir_filled_writable.restoreOrderAfterNameChangeAt(10);
	}
	
	
	/**
	 * Test canHaveAsParentDirectory
	 * 
	 * The complete spec should be tested now that it is in closed form!
	 * 
	 */
	@Test
	public void testCanHaveAsParentDirectory() {
		// copy from superclass
		assertTrue(terminatedFile.canHaveAsParentDirectory(null));
		assertFalse(terminatedFile.canHaveAsParentDirectory(dirInDirC_2_empty_writable));
		assertTrue(fileInDirCDefaultName_writable.canHaveAsParentDirectory(null));
		assertFalse(fileInDirCDefaultName_writable.canHaveAsParentDirectory(terminatedDirectory));
		// additional: (a non terminated directory X can only be the parent of a directory Y if X is not 
		//				a direct or indirect child of this directory Y (and if X is not the same as Y))
		assertTrue(dirC_filled_writable.canHaveAsParentDirectory(dirInDirE_2_empty_unwritable));
		assertFalse(root_dir_filled_writable.canHaveAsParentDirectory(dirInDirC_2_empty_writable));
		assertFalse(root_dir_filled_writable.canHaveAsParentDirectory(root_dir_filled_writable));		
	}
	
	
	/**
	 * Test isDirectOrIndirectSubDirectoryOf
	 */
	@Test
	public void testIsDirectOrIndirectSubdirectoryOf_allcases() {
		assertEquals(dirInDirC_2_empty_writable.isDirectOrIndirectChildOf(root_dir_filled_writable), dirInDirC_2_empty_writable.isDirectOrIndirectSubdirectoryOf(root_dir_filled_writable));
		assertEquals(dirInDirC_2_empty_writable.isDirectOrIndirectChildOf(dirC_filled_writable), dirInDirC_2_empty_writable.isDirectOrIndirectSubdirectoryOf(dirC_filled_writable));
		assertEquals(dirInDirE_2_empty_unwritable.isDirectOrIndirectChildOf(dirC_filled_writable), dirInDirE_2_empty_unwritable.isDirectOrIndirectSubdirectoryOf(dirC_filled_writable));
		
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
