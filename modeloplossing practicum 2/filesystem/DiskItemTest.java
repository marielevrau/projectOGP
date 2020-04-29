package filesystem;
import filesystem.exception.*;
import static org.junit.Assert.*;
import java.util.Date;


import org.junit.*;

/**
 * A JUnit test class for testing the non-private methods of the DiskItem Class.
 * The class is abstract, so we can use either one of the subclasses to test the interface of the superclass.
 *   
 * @author  Tommy Messelis
 * @version 2.0
 */
public class DiskItemTest {

	private static DiskItem itemStringBoolean, itemDirStringBoolean;
	private static Directory terminatedDirectory;
	private static File terminatedFile;
	private static Date timeBefore, timeAfter;
	
	private static File root_file_writable, fileA_writable, fileB_unwritable, fileD_writable, fileInDirC_1_unwritable, fileInDirE_1_writable, fileInDirCDefaultName_writable, fileInDirEDefaultName_unwritable;
	private static Directory root_dir_empty_writable, root_dir_filled_writable, root_dir_empty_unwritable;
	private static Directory dirC_filled_writable, dirE_filled_unwritable, dirInDirC_2_empty_writable, dirInDirE_2_empty_unwritable;
	
	
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
	 */
	
	@Test
	public void testConstructorStringBoolean_Legal_ValidName() {
		timeBefore = new Date();
		sleep();
		itemStringBoolean = new File("new_file",Type.TEXT,100,true);
		sleep();
		timeAfter = new Date();
		
		//effect setName
		assertEquals("new_file",itemStringBoolean.getName());
		//effect setWritable
		assertTrue(itemStringBoolean.isWritable());
		//post condities
		assertTrue(itemStringBoolean.isRoot());
		assertFalse(timeBefore.after(itemStringBoolean.getCreationTime()));
		assertFalse(itemStringBoolean.getCreationTime().after(timeAfter));
		assertNull(itemStringBoolean.getModificationTime());
		assertFalse(itemStringBoolean.isTerminated());
		
	}
	
	@Test
	public void testConstructorStringBoolean_Legal_InvalidName() {
		timeBefore = new Date();
		sleep();
		itemStringBoolean = new File("new?file",Type.TEXT,100,true);
		sleep();
		timeAfter = new Date();
		
		//effect setname
		assertTrue(DiskItem.isValidName(itemStringBoolean.getName()));
		//effect setwritable
		assertTrue(itemStringBoolean.isWritable());
		//post
		assertTrue(itemStringBoolean.isRoot());
		assertFalse(timeBefore.after(itemStringBoolean.getCreationTime()));
		assertFalse(itemStringBoolean.getCreationTime().after(timeAfter));
		assertNull(itemStringBoolean.getModificationTime());
		assertFalse(itemStringBoolean.isTerminated());

	}
	
	/**
	 * Test the extended constructor
	 */
	
	@Test
	public void testConstructorDirStringBoolean_Legal_ValidName() {
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
		itemDirStringBoolean = new File(root_dir_filled_writable,"BZ_filename",Type.TEXT,100,true);
		sleep();
		timeAfter = new Date();
		
		int nbItemsAfter = root_dir_filled_writable.getNbItems();
		
		//effect setname
		assertEquals("BZ_filename",itemDirStringBoolean.getName());
		//effect setwritable
		assertTrue(itemDirStringBoolean.isWritable());
		//effect setParentDir
		assertEquals(root_dir_filled_writable,itemDirStringBoolean.getParentDirectory());
		//effect parent.addAsItem
			//posts
			assertTrue(root_dir_filled_writable.hasAsItem(itemDirStringBoolean));
			assertEquals(nbItemsBefore + 1, nbItemsAfter);		
			assertEquals(root_dir_filled_writable.getItemAt(1), fileA_writable);
			assertEquals(root_dir_filled_writable.getItemAt(2), fileB_unwritable);
			assertEquals(root_dir_filled_writable.getItemAt(3), itemDirStringBoolean);
			assertEquals(root_dir_filled_writable.getItemAt(4), dirC_filled_writable);
			assertEquals(root_dir_filled_writable.getItemAt(5), fileD_writable);
			assertEquals(root_dir_filled_writable.getItemAt(6), dirE_filled_unwritable);
			//effect setModTime
			assertNotNull(root_dir_filled_writable.getModificationTime());
			assertFalse(timeBefore.after(root_dir_filled_writable.getModificationTime()));
			assertFalse(root_dir_filled_writable.getModificationTime().after(timeAfter));
		//post
		assertFalse(timeBefore.after(itemDirStringBoolean.getCreationTime()));
		assertFalse(itemDirStringBoolean.getCreationTime().after(timeAfter));
		assertNull(itemDirStringBoolean.getModificationTime());
		assertFalse(itemDirStringBoolean.isTerminated());

	}
	
	@Test
	public void testConstructorDirStringBoolean_Legal_InvalidName() {
		// very similar to the valid filename, but ordering in the directory will be different
		
		// add to a filled writable root directory
		
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
		itemDirStringBoolean = new File(root_dir_filled_writable,"2_112?X..5",Type.TEXT,100,true);
		sleep();
		timeAfter = new Date();
		
		int nbItemsAfter = root_dir_filled_writable.getNbItems();
		
		//effect setname
		assertTrue(DiskItem.isValidName(itemDirStringBoolean.getName()));
		//effect setwritable
		assertTrue(itemDirStringBoolean.isWritable());
		//effect setParentDir
		assertEquals(root_dir_filled_writable,itemDirStringBoolean.getParentDirectory());
		//effect parent.addAsItem
			//posts
			assertTrue(root_dir_filled_writable.hasAsItem(itemDirStringBoolean));
			assertEquals(nbItemsBefore + 1, nbItemsAfter);		
			assertEquals(root_dir_filled_writable.getItemAt(1), fileA_writable);
			assertEquals(root_dir_filled_writable.getItemAt(2), fileB_unwritable);
			assertEquals(root_dir_filled_writable.getItemAt(3), dirC_filled_writable);
			assertEquals(root_dir_filled_writable.getItemAt(4), fileD_writable);
			assertEquals(root_dir_filled_writable.getItemAt(5), dirE_filled_unwritable);
			assertEquals(root_dir_filled_writable.getItemAt(6), itemDirStringBoolean);
			//effect setModTime
			assertNotNull(root_dir_filled_writable.getModificationTime());
			assertFalse(timeBefore.after(root_dir_filled_writable.getModificationTime()));
			assertFalse(root_dir_filled_writable.getModificationTime().after(timeAfter));
		//post
		assertFalse(timeBefore.after(itemDirStringBoolean.getCreationTime()));
		assertFalse(itemDirStringBoolean.getCreationTime().after(timeAfter));
		assertNull(itemDirStringBoolean.getModificationTime());
		assertFalse(itemDirStringBoolean.isTerminated());
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorDirStringBoolean_Illegal_nullParent() {
		itemDirStringBoolean = new File(null,"BZ_filename",Type.TEXT,100,true);
		
	}
	
	@Test (expected = DiskItemNotWritableException.class)
	public void testConstructorDirStringBoolean_Illegal_unwritableParent() {
		itemDirStringBoolean = new File(root_dir_empty_unwritable,"BZ_filename",Type.TEXT,100,true);
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorDirStringBoolean_Illegal_parentContainsItemWithSameName() {
		itemDirStringBoolean = new File(root_dir_filled_writable,"A_file",Type.TEXT,100,true);
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorDirStringBoolean_Illegal_parentContainsItemWithSameDefaultName() {
		itemDirStringBoolean = new File(dirC_filled_writable,"X.?",Type.TEXT,100,true);
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorDirStringBoolean_Illegal_terminatedParent() {
		itemDirStringBoolean = new File(terminatedDirectory,"a_filename",Type.TEXT,100,true);
		
	}
	
	// the other exceptions thrown by effect clauses of the constructor cannot happen.
	// This is because e.g. addAsItem(.) will never be called with a null parameter from this constructor.
	// We will test those cases when we test the addAsItem in more detail.
	
	
	/**
	 * Test canBeTerminated
	 * open specification: only the illegal cases specified in the superclass can be tested here!
	 */
	
	
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
	 * Test terminate
	 */
	
	@Test
	public void testTerminate_LegalCase_rootItem() {
		root_file_writable.terminate();
		assertTrue(root_file_writable.isTerminated());
		assertTrue(root_file_writable.isRoot());
	}
	
	@Test
	public void testTerminate_LegalCase_rootItemAlreadyTerminated() {
		terminatedFile.terminate();
		assertTrue(terminatedFile.isTerminated());
		assertTrue(terminatedFile.isRoot());
	}
	
	@Test
	public void testTerminate_LegalCase_NonRootItem() {
		timeBefore = new Date();
		sleep();
		int nbItemsBefore = root_dir_filled_writable.getNbItems();
		fileA_writable.terminate();
		int nbItemsAfter = root_dir_filled_writable.getNbItems();
		sleep();
		timeAfter = new Date();
		
		// postconditions
		assertTrue(fileA_writable.isTerminated());
		assertTrue(fileA_writable.isRoot());
		
		//effect makeroot
			//effect getParent().remove(.)
			assertFalse(root_dir_filled_writable.hasAsItem(fileA_writable));
			assertEquals(root_dir_filled_writable.getIndexOf(fileB_unwritable),1);
			assertEquals(root_dir_filled_writable.getIndexOf(dirC_filled_writable),2);
			assertEquals(root_dir_filled_writable.getIndexOf(fileD_writable),3);
			assertEquals(root_dir_filled_writable.getIndexOf(dirE_filled_unwritable),4);
			assertEquals(nbItemsBefore-1, nbItemsAfter);
			assertNotNull(root_dir_filled_writable.getModificationTime());
			assertFalse(timeBefore.after(root_dir_filled_writable.getModificationTime()));
			assertFalse(root_dir_filled_writable.getModificationTime().after(timeAfter));
			
			//effect setModificationTime()
			assertNotNull(fileA_writable.getModificationTime());
			assertFalse(timeBefore.after(fileA_writable.getModificationTime()));
			assertFalse(fileA_writable.getModificationTime().after(timeAfter));
			
			//effect setParent(null)
			assertEquals(fileA_writable.getParentDirectory(), null);
	}
	
	// non-root items can not be already terminated, so no 4th legal case.
	
	@Test (expected = IllegalStateException.class)
	public void testTerminate_IllegalCase_NotTerminatable() {
		assertFalse(fileB_unwritable.canBeTerminated());
		assertFalse(fileB_unwritable.isTerminated());
		fileB_unwritable.terminate();
	}
	
	/**
	 * Test isValidName
	 */
	
	@Test
	public void testIsValidName_LegalCase() {
		assertTrue(DiskItem.isValidName("abcDEF123-_."));
	}

	@Test
	public void testIsValidName_IllegalCase() {
		assertFalse(DiskItem.isValidName(null));
		assertFalse(DiskItem.isValidName(""));
		assertFalse(DiskItem.isValidName("%illegalSymbol"));
		
	}
	
	/**
	 * Test changeName
	 */
	
	@Test
	public void testChangeName_LegalCase_rootItem() {
		timeBefore = new Date();
		sleep();
		root_file_writable.changeName("new_valid_filename");
		sleep();
		timeAfter = new Date();
		
		//effect setName
		assertEquals("new_valid_filename",root_file_writable.getName());
		//effect setModificationTime
		assertNotNull(root_file_writable.getModificationTime());
		assertFalse(root_file_writable.getModificationTime().before(timeBefore));
		assertFalse(timeAfter.before(root_file_writable.getModificationTime()));
		// no other effects for root items
	}
	@Test
	public void testChangeName_LegalCase_rootItem_InvalidName() {
		timeBefore = new Date();
		sleep();
		root_file_writable.changeName("445new_invalid_filename?X112");
		sleep();
		timeAfter = new Date();
		
		//effect setName
		assertEquals("new_diskitem",root_file_writable.getName());
		//effect setModificationTime
		assertNotNull(root_file_writable.getModificationTime());
		assertFalse(root_file_writable.getModificationTime().before(timeBefore));
		assertFalse(timeAfter.before(root_file_writable.getModificationTime()));
		// no other effects for root items
	}
	@Test
	public void testChangeName_LegalCase_nonRootItem() {
		timeBefore = new Date();
		sleep();
		fileD_writable.changeName("new_valid_filename");
		sleep();
		timeAfter = new Date();
		
		//effect setName
		assertEquals("new_valid_filename",fileD_writable.getName());
		//effect setModificationTime
		assertNotNull(fileD_writable.getModificationTime());
		assertFalse(fileD_writable.getModificationTime().before(timeBefore));
		assertFalse(timeAfter.before(fileD_writable.getModificationTime()));
		//restore order after update
		assertTrue(root_dir_filled_writable.hasProperItems());
		assertEquals(root_dir_filled_writable.getIndexOf(fileA_writable),1);
		assertEquals(root_dir_filled_writable.getIndexOf(fileB_unwritable),2);
		assertEquals(root_dir_filled_writable.getIndexOf(dirC_filled_writable),3);
		assertEquals(root_dir_filled_writable.getIndexOf(fileD_writable),5);
		assertEquals(root_dir_filled_writable.getIndexOf(dirE_filled_unwritable),4);
		//effect modification time parent
		assertNotNull(root_dir_filled_writable.getModificationTime());
		assertFalse(root_dir_filled_writable.getModificationTime().before(timeBefore));
		assertFalse(timeAfter.before(root_dir_filled_writable.getModificationTime()));
		
	}
	
	@Test (expected = IllegalStateException.class)
	public void testChangeName_IllegalCase_terminatedItem() {
		terminatedFile.changeName("legalname");
	}
	@Test (expected = DiskItemNotWritableException.class)
	public void testChangeName_IllegalCase_unwritableItem() {
		fileB_unwritable.changeName("legalname");
	}
	@Test (expected = IllegalArgumentException.class)
	public void testChangeName_IllegalCase_nameExistsInParent() {
		dirInDirC_2_empty_writable.changeName("C1_file");
	}
	@Test (expected = IllegalArgumentException.class)
	public void testChangeName_IllegalCase_defaultNameExistsInParent() {
		dirInDirC_2_empty_writable.changeName("InvalidX.?/Name");
	}
	
	/**
	 * Test ordering methods
	 */
	
	@Test
	public void testIsOrderedAfterString() {
		assertTrue(fileB_unwritable.isOrderedAfter("a"));
		assertFalse(fileB_unwritable.isOrderedAfter("z"));
		assertFalse(fileB_unwritable.isOrderedAfter(fileB_unwritable.getName()));
		assertFalse(fileB_unwritable.isOrderedAfter((String)null));
	}
	@Test
	public void testIsOrderedBeforeString() {
		assertTrue(fileB_unwritable.isOrderedBefore("z"));
		assertFalse(fileB_unwritable.isOrderedBefore("a"));
		assertFalse(fileB_unwritable.isOrderedBefore(fileB_unwritable.getName()));
		assertFalse(fileB_unwritable.isOrderedBefore((String)null));
	}
	@Test
	public void testIsOrderedAfterDiskItem() {
		assertTrue(fileB_unwritable.isOrderedAfter(fileA_writable));
		assertFalse(fileB_unwritable.isOrderedAfter(dirE_filled_unwritable));
		assertFalse(fileB_unwritable.isOrderedAfter(fileB_unwritable));
		assertFalse(fileB_unwritable.isOrderedAfter((DiskItem)null));
	}
	
	@Test
	public void testIsOrderedBeforeDiskItem() {
		assertTrue(fileB_unwritable.isOrderedBefore(dirE_filled_unwritable));
		assertFalse(fileB_unwritable.isOrderedBefore(fileA_writable));
		assertFalse(fileB_unwritable.isOrderedBefore(fileB_unwritable));
		assertFalse(fileB_unwritable.isOrderedBefore((DiskItem)null));
	}
	
	/**
	 * Test isValidCreationTime
	 */
	
	@Test
	public void testIsValidCreationTime_AllCases() {
		Date now = new Date();
		assertTrue(DiskItem.isValidCreationTime(now));
		assertFalse(DiskItem.isValidCreationTime(null));
		Date inFuture = new Date(System.currentTimeMillis() + 1000*60*60);
		assertFalse(DiskItem.isValidCreationTime(inFuture));
	}
	
	
	/**
	 * Test canHaveAsModificationTime
	 */
	
	@Test
	public void testcanHaveAsModificationTime_AllCases() {
		timeBefore = new Date();
		sleep();
		itemStringBoolean = new File("new_file",Type.TEXT,100,true);
		sleep();
		timeAfter = new Date();
		sleep();
		Date inFuture = new Date(System.currentTimeMillis() + 1000*60*60);
		
		assertTrue(itemStringBoolean.canHaveAsModificationTime(null));
		assertTrue(itemStringBoolean.canHaveAsModificationTime(timeAfter));
		assertFalse(itemStringBoolean.canHaveAsModificationTime(timeBefore));
		assertFalse(itemStringBoolean.canHaveAsModificationTime(inFuture));
		
	}
	
	/**
	 *  Test setModification time
	 */
	
	@Test
	public void testSetModificationTime_AllCases() {
		timeBefore = new Date();
		sleep();
		fileD_writable.setModificationTime();
		sleep();
		timeAfter = new Date();
		
		assertNotNull(fileD_writable.getModificationTime());
		assertTrue(fileD_writable.getModificationTime().before(timeAfter));
		assertTrue(timeBefore.before(fileD_writable.getModificationTime()));
	}
	
	
	/**
	 * Test overlapping use period
	 */
	
	@Test
	public void testHasOverlappingUsePeriod_UnmodifiedItems() {
		// One case
		DiskItem one = new File(dirInDirC_2_empty_writable,"one",Type.TEXT);
		sleep(); // sleep() to be sure that one.getCreationTime() < other.getCreationTime()
		DiskItem other = new File(dirInDirC_2_empty_writable,"other",Type.TEXT);
		
		//check both ways
		assertFalse(one.hasOverlappingUsePeriod(other));
		assertFalse(other.hasOverlappingUsePeriod(one));
	}
	@Test
	public void testHasOverlappingUsePeriod_OneModifiedItem() {
		// Three cases: one modified item A, other created before, during or after use period of A
		DiskItem before = new File(dirInDirC_2_empty_writable,"before",Type.TEXT);
		sleep(); 
		DiskItem a = new File(dirInDirC_2_empty_writable,"a",Type.TEXT);
		sleep(); 
		DiskItem during = new File(dirInDirC_2_empty_writable,"during",Type.TEXT);
		sleep(); 
		a.setModificationTime();
		sleep();
		DiskItem after = new File(dirInDirC_2_empty_writable,"after",Type.TEXT);

		//check both ways
		assertFalse(before.hasOverlappingUsePeriod(a));
		assertFalse(a.hasOverlappingUsePeriod(before));
		assertFalse(during.hasOverlappingUsePeriod(a));
		assertFalse(a.hasOverlappingUsePeriod(during));
		assertFalse(after.hasOverlappingUsePeriod(a));
		assertFalse(a.hasOverlappingUsePeriod(after));
	}
	@Test
	public void testHasOverlappingUsePeriod_TwoModifiedItems() {
		// Three cases: 
		// no overlapping use periods: A and B
		// partially overlapping: A and C
		// totally overlapping: A and D
		
		DiskItem a = new File(dirInDirC_2_empty_writable,"a",Type.TEXT);
		sleep(); 
		DiskItem c = new File(dirInDirC_2_empty_writable,"c",Type.TEXT);
		sleep(); 
		DiskItem d = new File(dirInDirC_2_empty_writable,"d",Type.TEXT);
		sleep(); 
		d.setModificationTime();
		sleep();
		a.setModificationTime();
		sleep();
		c.setModificationTime();
		sleep();
		DiskItem b = new File(dirInDirC_2_empty_writable,"b",Type.TEXT);
		sleep();
		b.setModificationTime();

		//check both ways
		assertFalse(a.hasOverlappingUsePeriod(b));
		assertFalse(b.hasOverlappingUsePeriod(a));
		assertTrue(a.hasOverlappingUsePeriod(c));
		assertTrue(c.hasOverlappingUsePeriod(a));
		assertTrue(a.hasOverlappingUsePeriod(d));
		assertTrue(d.hasOverlappingUsePeriod(a));
		
	}
	
	/**
	 * Test setWritable
	 */
	@Test
	public void testSetWritable() {
		assertFalse(fileInDirC_1_unwritable.isWritable());
		fileInDirC_1_unwritable.setWritable(true);
		assertTrue(fileInDirC_1_unwritable.isWritable());
		
		assertTrue(root_dir_empty_writable.isWritable());
		root_dir_empty_writable.setWritable(false);
		assertFalse(root_dir_empty_writable.isWritable());
		
	}
	
	
	/**
	 * Test getRoot
	 */
	@Test
	public void testGetRoot_Legal_rootItems() {
		assertEquals(root_file_writable.getRoot(), root_file_writable);
		assertEquals(root_dir_empty_unwritable.getRoot(), root_dir_empty_unwritable);
	}
	
	@Test
	public void testGetRoot_Legal_nonRootItems() {
		assertEquals(fileInDirCDefaultName_writable.getRoot(), root_dir_filled_writable);
		assertEquals(dirInDirE_2_empty_unwritable.getRoot(), root_dir_filled_writable);
	}
	
	
	/**
	 * Test makeRoot
	 */
	
	@Test
	public void testMakeRoot_Legal_alreadyRoot() {
		root_file_writable.setModificationTime();
		timeBefore = root_file_writable.getModificationTime();
		sleep();
		root_file_writable.makeRoot();
		sleep();
		timeAfter = root_file_writable.getModificationTime();
		
		// modification time not changed
		assertEquals(timeBefore, timeAfter);
		// effect setParent
		assertNull(root_file_writable.getParentDirectory());
		
	}
	
	@Test
	public void testMakeRoot_Legal_notYetRoot() {
		int nbItemsParentBefore = root_dir_filled_writable.getNbItems();
		timeBefore = new Date();
		sleep();
		dirC_filled_writable.makeRoot();
		sleep();
		timeAfter = new Date();
		
		// effect parent.removeAsItem
			//removeItemAt
			assertFalse(root_dir_filled_writable.hasAsItem(dirC_filled_writable));
			assertEquals(root_dir_filled_writable.getIndexOf(fileA_writable),1);
			assertEquals(root_dir_filled_writable.getIndexOf(fileB_unwritable),2);
			assertEquals(root_dir_filled_writable.getIndexOf(fileD_writable),3);
			assertEquals(root_dir_filled_writable.getIndexOf(dirE_filled_unwritable),4);
			assertEquals(nbItemsParentBefore-1, root_dir_filled_writable.getNbItems());
			//setModificationtime
			assertNotNull(root_dir_filled_writable.getModificationTime());
			assertTrue(timeBefore.before(root_dir_filled_writable.getModificationTime()));
			assertTrue(root_dir_filled_writable.getModificationTime().before(timeAfter));
		// effect modification time	
		assertNotNull(dirC_filled_writable.getModificationTime());
		assertTrue(timeBefore.before(dirC_filled_writable.getModificationTime()));
		assertTrue(dirC_filled_writable.getModificationTime().before(timeAfter));	
		// effect set Parent
		assertNull(dirC_filled_writable.getParentDirectory());
		
	}
	
	@Test (expected = IllegalStateException.class)
	public void testMakeRoot_Illegal_terminatedItem() {
		terminatedDirectory.makeRoot();
	}
	@Test (expected = DiskItemNotWritableException.class)
	public void testMakeRoot_Illegal_notWritableItem() {
		fileInDirC_1_unwritable.makeRoot();
	}
	@Test (expected = DiskItemNotWritableException.class)
	public void testMakeRoot_Illegal_notWritableParent() {
		fileInDirE_1_writable.makeRoot();
	}
	
	//Illegal cases for the effect clauses are not possible here
	
	
	/**
	 * Test move
	 */
	@Test
	public void testMove_Legal_RootItem() {
		
		int nbItemsNewParentBefore = dirInDirC_2_empty_writable.getNbItems();
		timeBefore = new Date();
		sleep();
		root_dir_empty_writable.move(dirInDirC_2_empty_writable);
		sleep();
		timeAfter = new Date();
		
		
		//effect newparent.addAsItem
			//posts
			assertTrue(dirInDirC_2_empty_writable.hasAsItem(root_dir_empty_writable));
			assertEquals(nbItemsNewParentBefore + 1, dirInDirC_2_empty_writable.getNbItems());		
			assertEquals(dirInDirC_2_empty_writable.getItemAt(1), root_dir_empty_writable);
			//effect setModTime
			assertNotNull(dirInDirC_2_empty_writable.getModificationTime());
			assertFalse(timeBefore.after(dirInDirC_2_empty_writable.getModificationTime()));
			assertFalse(dirInDirC_2_empty_writable.getModificationTime().after(timeAfter));
		
		
		// effect setModTime
		assertNotNull(root_dir_empty_writable.getModificationTime());
		assertFalse(timeBefore.after(root_dir_empty_writable.getModificationTime()));
		assertFalse(root_dir_empty_writable.getModificationTime().after(timeAfter));
	
		// effect setParent
		assertEquals(root_dir_empty_writable.getParentDirectory(), dirInDirC_2_empty_writable);
		
	}
	
	@Test
	public void testMove_Legal_NonRootItem() {
		
		int nbItemsCurrentParentBefore = root_dir_filled_writable.getNbItems();
		int nbItemsNewParentBefore = root_dir_empty_writable.getNbItems();
		timeBefore = new Date();
		sleep();
		dirC_filled_writable.move(root_dir_empty_writable);
		sleep();
		timeAfter = new Date();
		
		
		//effect parent.removeAsItem
			//posts
			assertFalse(root_dir_filled_writable.hasAsItem(dirC_filled_writable));
			assertEquals(nbItemsCurrentParentBefore, root_dir_filled_writable.getNbItems()+1);		
			assertEquals(root_dir_filled_writable.getItemAt(1), fileA_writable);
			assertEquals(root_dir_filled_writable.getItemAt(2), fileB_unwritable);
			assertEquals(root_dir_filled_writable.getItemAt(3), fileD_writable);
			assertEquals(root_dir_filled_writable.getItemAt(4), dirE_filled_unwritable);
			//effect setModTime
			assertNotNull(root_dir_filled_writable.getModificationTime());
			assertFalse(timeBefore.after(root_dir_filled_writable.getModificationTime()));
			assertFalse(root_dir_filled_writable.getModificationTime().after(timeAfter));
		
		//effect newparent.addAsItem
			//posts
			assertTrue(root_dir_empty_writable.hasAsItem(dirC_filled_writable));
			assertEquals(nbItemsNewParentBefore + 1, root_dir_empty_writable.getNbItems());		
			assertEquals(root_dir_empty_writable.getItemAt(1), dirC_filled_writable);
			//effect setModTime
			assertNotNull(root_dir_empty_writable.getModificationTime());
			assertFalse(timeBefore.after(root_dir_empty_writable.getModificationTime()));
			assertFalse(root_dir_empty_writable.getModificationTime().after(timeAfter));
		
		
		// effect setModTime
		assertNotNull(dirC_filled_writable.getModificationTime());
		assertFalse(timeBefore.after(dirC_filled_writable.getModificationTime()));
		assertFalse(dirC_filled_writable.getModificationTime().after(timeAfter));
	
		// effect setParent
		assertEquals(dirC_filled_writable.getParentDirectory(), root_dir_empty_writable);
		
	}
	
	@Test (expected = IllegalStateException.class)
	public void testMove_Illegal_terminatedItem() {
		terminatedDirectory.move(dirInDirC_2_empty_writable);
	}
	@Test (expected = DiskItemNotWritableException.class)
	public void testMove_Illegal_notWritableItem() {
		fileInDirC_1_unwritable.move(dirInDirC_2_empty_writable);
	}
	@Test (expected = DiskItemNotWritableException.class)
	public void testMove_Illegal_notWritableParent() {
		fileInDirE_1_writable.move(dirInDirC_2_empty_writable);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testMove_Illegal_nullTarget() {
		fileInDirCDefaultName_writable.move(null);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testMove_Illegal_alreadyParentTarget() {
		fileInDirCDefaultName_writable.move(dirC_filled_writable);
	}
	@Test (expected = DiskItemNotWritableException.class)
	public void testMove_Illegal_targetNotWritable() {
		fileInDirC_1_unwritable.move(dirInDirE_2_empty_unwritable);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testMove_Illegal_TargetCantHaveThisItem() {
		//give the default name to fileA
		fileA_writable.changeName("-.?X");
		fileA_writable.move(dirC_filled_writable);
	}
	@Test (expected = IllegalArgumentException.class)
	public void testMove_Illegal_ItemCantHaveThisParent() {
		// Here we test the cycles in the file system structure!
		// we need to test all possible versions of the canHaveAsParent() checker, for all subclasses.
		root_dir_filled_writable.move(dirInDirC_2_empty_writable);
	}
	
	//other illegal cases for the effect clauses are not possible here
	
	
	/**
	 * Test isRoot
	 */
	@Test
	public void testIsRoot_AllCases() {
		assertTrue(root_dir_empty_unwritable.isRoot());
		assertFalse(dirE_filled_unwritable.isRoot());
		assertFalse(fileInDirEDefaultName_unwritable.isRoot());
	}
	
	
	/**
	 * Test hasProperParentDirectory
	 */
	@Test
	public void testHasProperParentDir_LegalCases() {
		//canHaveAs is tested separately. Here, we only look at bidirectionality
		// root items
		assertTrue(root_dir_empty_unwritable.hasProperParentDirectory());
		// nonroot items
		assertTrue(fileD_writable.hasProperParentDirectory());
		assertTrue(dirInDirE_2_empty_unwritable.hasProperParentDirectory());
	}
	
	@Test
	public void testHasProperParentDir_IllegalCases() {
		// a nonroot item for which the parent does not point back
		// It is impossible to test, because the setParentDirectory is private
		// there is no way of constructing an object that points to a parant without that parent pointing back.
	}
	
	/**
	 * Test canHaveAsParentDirectory
	 * 
	 * Only the specification defined at this level can be tested.
	 */
	@Test
	public void testCanHaveAsParentDirectory() {
		assertTrue(terminatedFile.canHaveAsParentDirectory(null));
		assertFalse(terminatedFile.canHaveAsParentDirectory(dirInDirC_2_empty_writable));
		assertTrue(fileInDirCDefaultName_writable.canHaveAsParentDirectory(null));
		assertFalse(fileInDirCDefaultName_writable.canHaveAsParentDirectory(terminatedDirectory));
			
	}
	
	/** 
	 * Test isDirectOrIndirectChildOf
	 */
	@Test
	public void testIsDirectOrIndirectChildOf() {
		assertFalse(root_dir_empty_writable.isDirectOrIndirectChildOf(dirE_filled_unwritable));
		assertFalse(fileD_writable.isDirectOrIndirectChildOf(null));
		assertFalse(root_file_writable.isDirectOrIndirectChildOf(null));
		
		assertTrue(fileD_writable.isDirectOrIndirectChildOf(root_dir_filled_writable));
		assertTrue(fileInDirCDefaultName_writable.isDirectOrIndirectChildOf(dirC_filled_writable));
		assertTrue(fileInDirCDefaultName_writable.isDirectOrIndirectChildOf(root_dir_filled_writable));
		
		
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
