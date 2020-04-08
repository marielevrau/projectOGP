package Practicum2.src;

import static org.junit.Assert.*;
import java.util.Date;
import org.junit.*;

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
  FileSystem NotAValidFolder;
  FileSystem Downloads; /*will make not writable*/
  FileSystem MyOS;

  Date timeBeforeConstructionNotWritable, timeAfterConstructionNotWritable;
  Date timeCreationBeforeConstruction, timeCreationAfterConstruction;


  @Before
  public void setUpFixture(){
    MyFileSystem = new FileSystem("MyFileSystem", null, true);

    timeCreationBeforeConstruction = new Date();
    Documents = new FileSystem("Documents", MyFileSystem.getDir(), true);
    timeCreationAfterConstruction = new Date();

    NotAValidFolder = new FileSystem("not#a#valid#folder", null, false);

    timeBeforeConstructionNotWritable = new Date();
    Downloads = new FileSystem("Downloads", MyFileSystem.getDir(), false);
    timeAfterConstructionNotWritable = new Date();

    MyOS = new FileSystem("MyOS", null, false );

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
}








}

