package Practicum2.src;

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

  Date timeBeforeConstructionNotWritable, timeAfterConstructionNotWritable;
  Date timeCreationBeforeConstruction, timeCreationAfterConstruction;


  @Before
  public void setUpFixture(){
	timeCreationBeforeConstruction = new Date();
    MyFileSystem = new FileSystem("MyFileSystem", null, true);
    Documents = new FileSystem("Documents", MyFileSystem.getDir(), true);
    timeCreationAfterConstruction = new Date();

    timeBeforeConstructionNotWritable = new Date();
    Downloads = new FileSystem("Downloads", MyFileSystem.getDir(), false);
    MyOS = new FileSystem("MyOS", null, false );
    timeAfterConstructionNotWritable = new Date();

  }

 @Test
 public void extendedConstructorTestDocuments_LegalCase(){
   assertEquals("Documents", Documents.getName());
   assertTrue(Documents.isWritable());
   assertEquals(MyFileSystem, Documents.getDir());
   assertTrue(Documents.isValidName("Documents"));
   assertFalse(timeCreationBeforeConstruction.after(Documents.getCreationTime()));
   assertTrue(timeCreationAfterConstruction.after(Documents.getCreationTime()));
   assertTrue(Documents.isValidCreationTime(Documents.getCreationTime()));
   assertNull(Documents.getModificationTime());
 }

@Test
public void extendedConstructorTestDocuments_IllegalCase(){
  timeCreationBeforeConstruction = new Date();
  Documents = new FileSystem("Documents!%#", MyFileSystem.getDir(), true);
  timeCreationAfterConstruction = new Date();
  assertFalse(Documents.isValidName(Documents.getName()));
  assertEquals(MyFileSystem, Documents.getDir());
  assertTrue(Documents.isWritable());
  assertTrue(Documents.isValidCreationTime(Documents.getCreationTime()));
  assertNull(Documents.getModificationTime());
  assertFalse(timeCreationBeforeConstruction.after(Documents.getCreationTime()));
}

@Test
public void extendedConstructorTestDownloads_LegalCase(){
  assertEquals("Downloads", Downloads.getName());
  assertFalse(Downloads.isWritable());
  assertTrue(Downloads.isValidName(Downloads.getName()));
  assertEquals(MyFileSystem, Downloads.getDir());
  assertNull(Downloads.getModificationTime());
  assertTrue(Downloads.isValidCreationTime(Downloads.getCreationTime()));
  assertFalse(timeBeforeConstructionNotWritable.after(Downloads.getCreationTime()));
  assertNull(Downloads.getModificationTime());
}

@Test
public void extendedConstructorTestDownloads_IllegalCase(){
  timeBeforeConstructionNotWritable = new Date(); 
  Downloads = new FileSystem("Downloads§@", MyFileSystem.getDir(), false);
  timeAfterConstructionNotWritable = new Date(); 
  assertFalse(Downloads.isValidName(Downloads.getName()));
  assertEquals("new_filesystem", Downloads.getName()); 
  assertEquals(MyFileSystem, Downloads.getDir()); 
  assertFalse(Downloads.isWritable()); 
  assertTrue(Downloads.isValidCreationTime(Downloads.getCreationTime()));
  assertFalse(timeAfterConstructionNotWritable.after(Downloads.getCreationTime()));
  
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
	assertEquals("new_filesystem", MyOS.getName()); 
	assertFalse(MyOS.isWritable());
	assertNull(MyOS.getModificationTime()); 
	assertFalse(timeBeforeConstructionNotWritable.after(MyOS.getCreationTime())); 
	assertFalse(MyOS.getCreationTime().after(timeAfterConstructionNotWritable)); 
}

@Test
public void extendedConstructorTestMyFileSystem_LegalCase() {
	assertEquals("MyFileSystem", MyFileSystem.getName()); 
	assertTrue(MyFileSystem.isWritable()); 
	assertTrue(MyFileSystem.isValidCreationTime(MyFileSystem.getCreationTime())); 
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
	assertEquals("new_filesystem", MyFileSystem.getName()); 
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
	assertNotEquals(Documents, Documents.getRoot()); 
}

@Test
public void testChangeName_LegalCase() {
	
	Date timeBeforeModify = new Date(); 
	Documents.changeName("Drive");
	Date timeAfterModify = new Date(); 
	assertEquals("Torrents", Documents.getName());
	assertNotEquals(timeBeforeModify, timeBeforeConstructionNotWritable);
	assertNotEquals(timeAfterModify, timeAfterConstructionNotWritable);
	assertNotNull(Documents.getModificationTime()); 
	assertFalse(timeBeforeModify.after(Documents.getModificationTime())); 
	assertFalse(Documents.getModificationTime().after(timeAfterModify)); 
}

@Test (expected = FileNotWritableException.class)
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
public void testBin() {
 

}

@Test
public void testForIsValidName_legalcase() {
	FileSystem validName = new FileSystem("validname", null, true); 
	String FileSystemName = validName.getName();
	assertTrue(validName.isValidName(FileSystemName)); 
}

@Test
public void testForIsValidName_IllegalCase() {
	FileSystem notAValidName = new FileSystem("notç&valid", null, true);
	String FileSystemName = notAValidName.getName(); 
	assertFalse(notAValidName.isValidName(FileSystemName)); 
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
	sleep(); 
	second = null; 
	assertFalse(first.hasOverlappingUsePeriod(second)); 
}

@Test
public void testHasOverlappingUsePeriod_ModifiedNameOverlap() {
	FileSystem first, second;
	first = new FileSystem("first", null, true); 
	sleep(); 
	second = new FileSystem("second", null, false); 
	
	first.changeName("numberone");
	sleep(); 
	second.changeName("numbertwo");
	assertTrue(first.hasOverlappingUsePeriod(second)); 
	
}

@Test
public void testOverLappingUsePeriod_Move() {
	FileSystem first, second;
	first = new FileSystem("first", null, true); 
	sleep();
	second = new FileSystem("second", null, true); 
	
	Directory Home = new Directory("Home", null, true);
	first.move(Home);
	sleep(); 
	second.move(Home);
	assertTrue(first.hasOverlappingUsePeriod(second)); 
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

@Test (expected = FileNotWritableException.class)
public void testForMove_NotWritable() {
	Directory Default = new Directory("Default", null, false);
	FileSystem MyPreferences, Users; 
	MyPreferences = new FileSystem("Preferences", null, true);
	Users = new FileSystem("Users", null, true); 
	MyPreferences.move(Default);
	Users.move(Default);
}

@Test (expected = AlreadyInListException.class)
public void testForMove_AlreadyExists() {
	Directory local = new Directory("local", null, true); 
	FileSystem settings; 
	settings = new FileSystem("settings", local, true); 
	settings.move(local);
}



private void sleep() {
    try {
        Thread.sleep(50);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}


}



