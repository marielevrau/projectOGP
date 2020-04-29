import Practicum2.src.FileSystem; 
import Practicum2.src.Directory; 
import Practicum2.src.File;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.List;

import org.junit.*;
import java.util.ArrayList;

/**
* A JUnit testclass for testing the public methods of the
* superclass "FileSystem"
*
* @author Art Willems
* @author Jérôme D'hulst
* @author Marie Levrau
*/


public class FileSystemTest{
	


  FileSystem MyFileSystem;
  FileSystem Documents;
  FileSystem Downloads; 
  FileSystem MyOS;
  Directory MyDirectory; 

  Date timeBeforeConstructionNotWritable, timeAfterConstructionNotWritable;
  Date timeCreationBeforeConstruction, timeCreationAfterConstruction;


  @Before
  public void setUpFixture(){
	MyDirectory = new Directory("MyDirectory"); 
	timeCreationBeforeConstruction = new Date();
	sleep(); 
    MyFileSystem = new FileSystem("MyFileSystem", null, true);
    Documents = new FileSystem("Documents", MyDirectory, true);
    sleep();
    timeCreationAfterConstruction = new Date();

    timeBeforeConstructionNotWritable = new Date();
    Downloads = new FileSystem("Downloads", MyDirectory, false);
    MyOS = new FileSystem("MyOS", null, false );
    timeAfterConstructionNotWritable = new Date();

  }

 @Test
 public void extendedConstructorTestDocuments_LegalCase(){
   assertEquals("Documents", Documents.getName());
   assertTrue(Documents.isWritable());
   assertEquals(MyDirectory, Documents.getDir());
   assertTrue(FileSystem.isValidName("Documents"));
   assertFalse(timeCreationBeforeConstruction.after(Documents.getCreationTime()));
   assertTrue(timeCreationAfterConstruction.after(Documents.getCreationTime()));
   assertTrue(FileSystem.isValidCreationTime(Documents.getCreationTime()));
   assertNull(Documents.getModificationTime());
 }

@Test
public void extendedConstructorTestDocuments_IllegalCase(){
  timeCreationBeforeConstruction = new Date();
  Documents = new FileSystem("Documents!%#", MyDirectory, true);
  timeCreationAfterConstruction = new Date();
  assertTrue(FileSystem.isValidName(Documents.getName()));
  assertEquals(MyDirectory, Documents.getDir());
  assertTrue(Documents.isWritable());
  assertTrue(FileSystem.isValidCreationTime(Documents.getCreationTime()));
  assertNull(Documents.getModificationTime());
  assertFalse(timeCreationBeforeConstruction.after(Documents.getCreationTime()));
}

@Test
public void extendedConstructorTestDownloads_LegalCase(){
  assertEquals("Downloads", Downloads.getName());
  assertFalse(Downloads.isWritable());
  assertTrue(FileSystem.isValidName(Downloads.getName()));
  assertEquals(MyDirectory, Downloads.getDir());
  assertNull(Downloads.getModificationTime());
  assertTrue(FileSystem.isValidCreationTime(Downloads.getCreationTime()));
  assertFalse(timeBeforeConstructionNotWritable.after(Downloads.getCreationTime()));
  assertNull(Downloads.getModificationTime());
}

@Test
public void extendedConstructorTestDownloads_IllegalCase(){
  timeBeforeConstructionNotWritable = new Date(); 
  Downloads = new FileSystem("Downloads§@", MyDirectory, false);
  timeAfterConstructionNotWritable = new Date(); 
  assertTrue(FileSystem.isValidName(Downloads.getName()));
  assertEquals("new_fileSystem", Downloads.getName()); 
  assertEquals(MyDirectory, Downloads.getDir()); 
  assertFalse(Downloads.isWritable()); 
  assertTrue(FileSystem.isValidCreationTime(Downloads.getCreationTime()));
  assertFalse(timeAfterConstructionNotWritable.before(Downloads.getCreationTime()));
  
}

@Test 
public void extendedConstructorTestMyOS_LegalCase() {
	assertEquals("MyOS", MyOS.getName()); 
	assertEquals(null, MyOS.getDir() ); 
	assertFalse(MyOS.isWritable()); 
	assertNull(MyOS.getModificationTime()); 
	assertFalse(timeBeforeConstructionNotWritable.after(MyOS.getCreationTime())); 
	assertFalse(MyOS.getCreationTime().after(timeAfterConstructionNotWritable)); 
	
}

@Test
public void extendedConstructorTestMyOS_IllegalCase() {
	timeBeforeConstructionNotWritable = new Date();
	MyOS = new FileSystem("TEmple!", null, false); 
	timeAfterConstructionNotWritable = new Date(); 
	assertEquals("new_fileSystem", MyOS.getName()); 
	assertEquals(null, MyOS.getDir()); 
	assertFalse(MyOS.isWritable());
	assertNull(MyOS.getModificationTime()); 
	assertFalse(timeBeforeConstructionNotWritable.after(MyOS.getCreationTime())); 
	assertFalse(MyOS.getCreationTime().after(timeAfterConstructionNotWritable)); 
}

@Test
public void extendedConstructorTestMyFileSystem_LegalCase() {
	assertEquals("MyFileSystem", MyFileSystem.getName()); 
	assertTrue(MyFileSystem.isWritable()); 
	assertTrue(FileSystem.isValidCreationTime(MyFileSystem.getCreationTime())); 
	assertNull(MyFileSystem.getModificationTime());
	assertEquals(null, MyFileSystem.getDir()); 
	assertFalse(timeCreationBeforeConstruction.after(MyFileSystem.getCreationTime())); 
	assertFalse(MyFileSystem.getCreationTime().after(timeCreationAfterConstruction)); 
	
}

@Test
public void extendedConstructorTestMyFilesystem_IllegalCase() {
	timeCreationBeforeConstruction = new Date();
	MyFileSystem = new FileSystem("Hard-drive", null, true);
	timeCreationAfterConstruction = new Date(); 
	assertEquals("Hard-drive", MyFileSystem.getName()); 
	assertTrue(MyFileSystem.isWritable());
	assertEquals(null, MyFileSystem.getDir()); 
	assertFalse(timeCreationBeforeConstruction.after(MyFileSystem.getCreationTime())); 
	assertFalse(MyFileSystem.getCreationTime().after(timeCreationAfterConstruction)); 
	
}

@Test
public void testForGetRoot_rootFile() {
	assertEquals(MyOS, MyOS.getRoot()); 
}

@Test
public void testForGetRoot_notRootFile() {
	assertNotEquals(MyFileSystem, Documents.getRoot()); 
}

@Test
public void testChangeName_LegalCase() {
	
	Date timeBeforeModify = new Date(); 
	Documents.changeName("Drive");
	Date timeAfterModify = new Date(); 
	assertEquals("Drive", Documents.getName());
	assertFalse(timeBeforeModify.after(timeBeforeConstructionNotWritable));
	assertFalse(timeAfterModify.before(timeAfterConstructionNotWritable));
	assertNotNull(Documents.getModificationTime()); 
	assertFalse(timeBeforeModify.after(Documents.getModificationTime())); 
	assertFalse(Documents.getModificationTime().after(timeAfterModify)); 
}

@Test (expected = FileSystemNotWritableException.class)
public void testChangeName_IllegalCase(){
	Downloads.changeName("Torrents");
}

@Test
public void testSetWritable() {
	Downloads.setWritable(true);
	assertTrue(Downloads.isWritable()); 
	
}

@Test
public void testMakeRoot() {
	Documents.makeRoot();
	assertEquals(null, Documents.getDir());
}

@Test
public void testForIsValidName_legalcase() {
	FileSystem validName = new FileSystem("validname", null, true); 
	String FileSystemName = validName.getName();
	assertTrue(FileSystem.isValidName(FileSystemName)); 
}

@Test
public void testForIsValidName_IllegalCase() {
	FileSystem notAValidName = new FileSystem("notç&valid", null, true);
	assertFalse(FileSystem.isValidName("notç&valid")); 
}

@Test
public void testCanHasAsModificationTime_legalCase() {
	assertTrue(Documents.canHaveAsModificationTime(null)); 
	assertTrue(Documents.canHaveAsModificationTime(new Date())); 
}

@Test
public void testCanHasAsModificationTime_IllegalCase() {
	assertFalse(Documents.canHaveAsModificationTime(new Date(timeCreationAfterConstruction.getTime() - 1000*60*60))); 
}

@Test
public void testHasOverlappingUsePeriod_NoOverlap() {
	FileSystem first;  
	first = new FileSystem("first", null, true); 
	sleep(); //to make sure first.getCreationTime() != second.getCreationTime()
	FileSystem second; 
	second = new FileSystem("second", null, true); 
	
	assertFalse(first.hasOverlappingUsePeriod(second)); 
	// modify only one of the FileSystems. 
	second = new FileSystem("second", null, true); 
	second.setWritable(false);
	assertFalse(second.hasOverlappingUsePeriod(first)); 
}

@Test
public void testHasOverlappingUsePeriod_nullCase() {
	FileSystem first, second; 
	first = new FileSystem("first", null, true); 
	sleep(); // make sure that first.getCreationTime != second.getCreationTime()
	second = null; 
	assertFalse(first.hasOverlappingUsePeriod(second)); 
}

@Test
public void testHasOverlappingUsePeriod_ModifiedNameOverlap() {
	// make two new file systems "first" and "second"
	FileSystem first, second;
	first = new FileSystem("first", null, true); 
	sleep(); // make sure that first.getCreationTime()!= second.getCreationTime()
	second = new FileSystem("second", null, true); 
	
	first.changeName("numberone");
	sleep(); 
	second.changeName("numbertwo");
	assertTrue(first.hasOverlappingUsePeriod(second)); 
	
}

@Test
public void testOverLappingUsePeriod_Move() {
	// make two new file systems first and second
	FileSystem first, second;
	first = new FileSystem("first", null, true); 
	sleep();
	second = new FileSystem("second", null, true); 
	// now move the file systems, but in a way that there is overlap between use periods. 
	Directory Home = new Directory("Home", null, true);
	first.move(Home);
	sleep(); 
	second.move(Home);
	assertTrue(first.hasOverlappingUsePeriod(second)); 
	
}

@Test
public void testSeekAlphabeticPosition() {
	// make a new FileSystem with MyDirectory as directory. 
	FileSystem SomeOtherFiles = new FileSystem("varia", MyDirectory, true); 
	assertEquals(3, SomeOtherFiles.seekAlphabeticPosition(SomeOtherFiles.getName())); 
}

@Test
public void testForMove_LegalCase() {
	List<FileSystem> ExactCopy = new ArrayList<FileSystem>(); 
	Directory Drive = new Directory("Drive", null, true);
	FileSystem Music, Photos; 
	
	Music = new FileSystem("Music", null, true); 
	Photos = new FileSystem("Photos", null, true); 
	// move the filesystems to the drive directory
	Music.move(Drive);
	Photos.move(Drive); 
	// now add the objects 'Music' and 'Photos' to the ExactCopy
	ExactCopy.add(Music);
	ExactCopy.add(Photos); 
	//
	assertEquals(ExactCopy, Drive.getList()); 
	
}

@Test (expected = FileSystemNotWritableException.class)
public void testForMove_NotWritable() {
	// make a directory that is not writable for file systems called "Users" and "MyPreferences", trying to move these
	//file systems will result in a FileSystemNotWritableException. 
	Directory Default = new Directory("Default", null, false);
	FileSystem MyPreferences, Users; 
	MyPreferences = new FileSystem("Preferences", null, true);
	Users = new FileSystem("Users", null, true); 
	MyPreferences.move(Default);
	Users.move(Default);
}

@Test (expected = AlreadyInListException.class)
public void testForMove_AlreadyExists() {
	// make a new directory local and set the directory of a file system called settings to it. Try to move settings to local
	// will result in AlreadyInListException. 
	Directory local = new Directory("local", MyDirectory, true); 
	FileSystem settings; 
	settings = new FileSystem("settings", local, true); 
	settings.move(local);
}

@Test
public void testForIsDeleted_LegalCaseA() {
	assertFalse(MyOS.isDeleted()); 
}

@Test
public void testForIsDeleted_LegalCaseB() {
	//make a new filesystem and set its status to delete. Check if it is indeed deleted. 
	FileSystem MyOSPrevious = new FileSystem("MyOSVersion0", null, false); 
	MyOSPrevious.setDelete(true);
	assertTrue(MyOSPrevious.isDeleted()); 
	
}

@Test
public void testDelete_Legalcase() {
	// make a new filesystem that is not empty, add a directory to it and delete the directory. 
	FileSystem Obsolete = new FileSystem("obsolete", MyDirectory, false);
	Directory root = Obsolete.getDir(); 
	Obsolete.delete();
	assertFalse(root.getList().contains(Obsolete)); 
}

@Test (expected= AlreadyDeletedException.class)
public void testDelete_IllegalCase() {
	FileSystem Obsolete = new FileSystem("obsolete", MyDirectory, false);
	Obsolete.delete();
	sleep(); //make sure to wait after the filesystem has been deleted
	Obsolete.delete();	
	
}

private void sleep() {
    try {
        Thread.sleep(50);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}


}



