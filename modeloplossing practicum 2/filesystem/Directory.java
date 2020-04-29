package filesystem;

import java.util.*;

import be.kuleuven.cs.som.annotate.*;

import filesystem.exception.*;


/**
 * A class of directories.
 * @invar 	Each directory must have proper items registered in it.
 *        	| hasProperItems()
 * 
 * @note	The class invariants are inherited from the superclass. This means that we must also have 
 * 			a proper parent directory. In this class, we overwrite this checker s.t. it includes the
 * 			restrictions on cycles in the file system structure.
 * 
 * @author 	Tommy Messelis
 * @version 3.0
 */
public class Directory extends DiskItem {

	/**********************************************************
	 * Constructors
	 **********************************************************/

	/**
	 * Initialize a new root directory with given name and writability.
	 * 
	 * @param  name
	 *         The name of the new directory.
	 * @param  writable
	 *         The writability of the new directory.
	 * @effect The new directory is a disk item with the given
	 *         name and writability.
	 *         | super(name,writable)
	 * @post   The new directory has no items.
	 *         | new.getNbItems() == 0
	 * 
	 */
	@Raw
	public Directory(String name, boolean writable) {
		super(name,writable);
	}

	/**
	 * Initialize a new writable root directory with given name.
	 * 
	 * @param  name
	 *         The name of the new directory.
	 * @effect The new root directory is initialized with the given name
	 *         and is writable.
	 *         | this(name,true)
	 */
	@Raw
	public Directory(String name) {
		this(name,true); 
	}


	/**
	 * Initialize a new directory with given parent directory, name and 
	 * writability.
	 * 
	 * @param  parent
	 *         The parent directory of the new directory.
	 * @param  name
	 *         The name of the new directory.
	 * @param  writable
	 *         The writability of the new directory.
	 * @effect The new directory is a disk item with the given
	 *         parent, name and writability.
	 *         | super(parent,name,writable)        
	 * @post   The new directory has no items.
	 *         | new.getNbItems() == 0
	 */
	@Raw
	public Directory(Directory parent, String name, boolean writable) 
			throws IllegalArgumentException, DiskItemNotWritableException {
		super(parent,name,writable);    
	}

	/**
	 * Initialize a new writable directory with given parent directory
	 * and name.
	 * 
	 * @param  parent
	 *         The parent directory of the new directory.
	 * @param  name
	 *         The name of the new directory.
	 * @effect The new directory is a disk item with the given
	 *         parent, name and writability.
	 *         | this(parent,name,true)       
	 */
	@Raw
	public Directory(Directory parent, String name) 
			throws IllegalArgumentException, DiskItemNotWritableException {
		this(parent,name,true);    
	}    

	
	
	
	/**********************************************************
	 * delete/termination
	 **********************************************************/

	/**
	 * Check whether this directory can be terminated.
	 * 
	 * @return	True if the directory is not yet terminated, is writable, contains 0 items
	 * 			and it is either a root or its parent directory is writable
	 * 			| result == getNbItems() == 0 && !isTerminated() && isWritable() 
	 * 			|            && (isRoot() || getParentDirectory().isWritable())
	 * @note	We have added a condition to the open spec of the superclass. Now the specification
	 * 			is in closed form.
	 */
	@Override
	public boolean canBeTerminated() {
		return getNbItems() == 0 && super.canBeTerminated();			
	}

	
	
	
	/**********************************************************
	 * Contents
	 **********************************************************/


	/**
	 * Variable referencing a list collecting all items contained by this				
	 * directory. The class DiskItem is responsible for controlling the 
	 * bidirectional relationship. Files and directories can only be added or deleted
	 * through the constructors/destructors of File and Directory and through
	 * the move and makeRoot methods, hence the protected methods for adding
	 * and removing items from the directory
	 * 
	 * @invar items references an effective list. 
	 *        | items != null
	 * @invar Each element in the list references an effective item. 
	 *        | for each item in items:
	 *        |   item != null
	 * @invar Each element in the list references a non-terminated item.
	 *        | for each item in items:
	 *        | !item.isTerminated()
	 * @invar Each element in the list (except the first element)
	 *        references an item that has a name which (ignoring case)
	 *        comes after the name of the immediately preceding element,
	 *        in lexicographic order. 
	 *        | for each I in 1..items.size() - 1:
	 *        |   items.get(I).isOrderedAfter(items.get(I-1))
	 * @invar Each element in the list references an item that references
	 *        back to this directory.
	 *        | for each item in items:
	 *        |   item.getParentDirectory() == this
	 * 
	 * @note  This class is the non-controlling class in this relationship.
	 */	
	private final List<DiskItem> items = new ArrayList<DiskItem>();  

	/**
	 * Return the number of items of this directory.
	 */
	@Basic @Raw 
	public int getNbItems() {
		return items.size();
	}

	/**
	 * Return the item registered at the given position in this directory.
	 * 
	 * @param 	index
	 *        	The index of the item to be returned.
	 * @throws 	IllegalArgumentException
	 *         	The given index is not strictly positive or exceeds the number
	 *         	of items registered in this directory. 
	 *         	| (index < 1) || (index > getNbItems())
	 *         
	 * @note	We catch the exception that could be thrown when accessing the internal representation
	 * 			and formulate a better suited one for the user of this class.
	 * 			This adds to the encapsulation of the internal representation of this class.
	 */
	@Basic @Raw
	public DiskItem getItemAt(int index) throws IndexOutOfBoundsException {
		try{
			return items.get(index - 1);
		} catch (IndexOutOfBoundsException e) {
			//The exception e contains a message indicating that 'index-1' is out of bounds
			//Here, we throw a new Exception with the right information
			throw new IndexOutOfBoundsException("Index out of bounds: "+index);
		}
	}
	
	/**
	 * Check whether the given item is registered in this directory.
	 * 
	 * @param 	item
	 *        	The item to be checked.
	 * @return 	True if an item equal to the given item is registered at some
	 *         	position in this directory;
	 *         	false otherwise.
	 *         	| result == 
	 *         	|    for some I in 1..getNbItems() :
	 *         	| 	      (getItemAt(I) == item)
	 */
	@Raw
	public boolean hasAsItem(@Raw DiskItem item) { 
		for (int i=1; i<=getNbItems(); i++) {
			if (getItemAt(i) == item)
				return true;
		}
		return false;
	}
	
	/**
	 * Returns the potential index at which the given item would be inserted.
	 * 
	 * @param	item
	 * 			The disk item for which the index should be determined.
	 * @return	The index at which the item should be inserted, given the ordering of the directory items.
	 * 			| for each index in 1..result-1 : 
	 * 			| 		index == 1 || getItemAt(index).isOrderedBefore(item)
	 * 			| for each index in result..getNbItems()+1 : 
	 * 			| 		index == getNbItems()+1 || getItemAt(index).isOrderedAfter(item)
	 * @throws	IllegalArgumentException
	 * 			The given item is not effective or terminated
	 * 			| item == null || item.isTerminated()
	 * @throws	IllegalArgumentException
	 * 			The given effective item is already present in this directory
	 * 			| item != null && hasAsItem(item)
	 */
	public int getInsertionIndexOf(@Raw DiskItem item) {
		if (item == null || item.isTerminated())
			throw new IllegalArgumentException("The item is not effective or terminated.");
		if (this.hasAsItem(item))
			throw new IllegalArgumentException("The item is already present in this directory.");
		// Determine the index:
		int index = 1;
		while(index <= getNbItems() && getItemAt(index).isOrderedBefore(item)){
			index++;
		}
		return index;		
	}

	/**
	 * Check whether this directory has valid items.
	 *
	 * @return  True if and only if this directory can have all its items 
	 * 			at their respective indices
	 *          | result ==
	 *          |   for each I in 1..getNbItems() :
	 *          |     canHaveAsItemAt(getItemAt(I),I) && getItemAt(I).getParentDirectory() == this
	 * @note	This checker ensures the consistency of the bidirectional relationship
	 * 			and calls another checker to check all other requirements (except this consistency).
	 */
	@Raw 
	public boolean hasProperItems() {
		for (int i=1; i <= getNbItems(); i++) {
			if (!canHaveAsItemAt(getItemAt(i), i) || getItemAt(i).getParentDirectory() != this){
				return false;
			}
		}
		return true;
	}

	/**
	 * Check whether this directory can have the given item at the given index.
	 *
	 * @param   item
	 *          The item to be checked.
	 * @param   index
	 *          The index to be checked.
	 * @return	False if this directory cannot have the given item at any index or 
	 * 				  if the given index is not positive or exceeds the number of items with more than one
	 * 			otherwise,  if the item is in this directory 
	 * 					 	then true if and only if it is ordered after its predecessor 
	 * 							and before it successor (if those exist)
	 * 						else true if and only if inserting the item at the given index
	 * 							would not result in an unordered sequence
	 * 			| if (!canHaveAsItem(item)) then result == false
	 * 			| else if (index < 1 || index > getNbItems() +1) then result == false
	 * 			|	   else if (hasAsItem(item))
	 * 			|			then result == (index == 1 || getItemAt(index-1).isOrderedBefore(item))
	 * 			|							&& (index == getNbItems() || getItemAt(index+1).isOrderedAfter(item))
	 * 			|			else result == (index == 1 || getItemAt(index-1).isOrderedBefore(item))
	 * 			|							&& (index == getNbItems() + 1 || getItemAt(index).isOrderedAfter(item))
	 * 
	 * @note	This checker checks all conditions, except the consistency of the bidirectional relationship.
	 * 			Here, the ordering is checked. 
	 * 			Other restrictions on content are deferred to the canHaveAsItem checker.
	 */
	@Raw
	public boolean canHaveAsItemAt(@Raw DiskItem item, int index){ 
		if (!canHaveAsItem(item))
			return false;
		if ((index < 1) || (index > getNbItems()+1))
			return false;
		if(hasAsItem(item)){
			return (index == 1 ||  getItemAt(index-1).isOrderedBefore(item))
					&& (index == getNbItems() || getItemAt(index+1).isOrderedAfter(item));  
		}else{
			return (index == 1 ||  getItemAt(index-1).isOrderedBefore(item))
					&& (index == getNbItems() + 1 || getItemAt(index).isOrderedAfter(item));  

		}
	}

	/**
	 * Check whether this directory can have the given item as one of its items.
	 *
	 * @param   item
	 *          The item to be checked.
	 * @return 	If the given item is not effective or it is terminated or if this directory is terminated, 
	 * 			then false,
	 * 			else if the given item is an item of this directory
	 * 			 	 then true if and only if the given item has a unique name in this directory
	 * 				 else true if and only if the name of the given item does not yet exist in this directory 
	 * 			| if (item == null || item.isTerminated() || this.isTerminated()) 
	 * 			| then result == false
	 * 			| else if (this.hasAsItem(item))
	 * 			|	   then result == for one I in 1..getNbItems:
	 *          |      								item.getName().equalsIgnoreCase(getItemAt(I).getName())
	 * 			|	   else result == (!this.containsDiskItemWithName(item.getName()) 	
	 * 
	 * @note	This checker does not verify the consistency of the bidirectional relationship, 
	 * 			nor the ordering in this directory.
	 * @note	This checker can be used to verify existing items in this directory, as well as to verify whether
	 * 			a new item can be added to this directory. 
	 */
	@Raw
	public boolean canHaveAsItem(@Raw DiskItem item) {
		if (item == null || item.isTerminated() || this.isTerminated()) return false;
		if (this.hasAsItem(item)) {
			int count = 0;
			for (int position=1;position<=getNbItems();position++){
				 if (item.getName().equalsIgnoreCase(getItemAt(position).getName())) count++;
			}
			return count == 1;
		}else{
			return !this.containsDiskItemWithName(item.getName()); 
		}
	}

	/**
	 * Add the given item to the items registered in this directory.
	 *
	 * @param   item
	 *          The item to be added.
	 * @post    The given item is added to the items registered
	 *          in this directory.
	 *          | new.hasAsItem(item)
	 * @post    The number of items registered in this directory is
	 *          incremented with 1.
	 *          | new.getNbItems() == getNbItems() + 1   
	 * @post    All items registered in this directory, that are 
	 *          ordered after the given item are shifted one position
	 *          to the right.
	 *          | for each I in 1..getNbItems():
	 *          |   if (getItemAt(I).isOrderedAfter(item))
	 *          |     then new.getItemAt(I+1) == getItemAt(I)
	 * @effect 	The new modification time of this directory is updated.
	 *         	| setModificationTime()
	 *         
	 *          
	 * @throws	IllegalArgumentException
	 * 			The given disk item is non-effective
	 * 			| item == null                 
	 * @throws  IllegalArgumentException
	 *          The item already exists in this directory
	 *          | hasAsItem(item)
	 * @throws  IllegalArgumentException
	 *          This directory can not have the given item as a member.
	 *          | !canHaveAsItem(item)
	 * @throws	IllegalArgumentException
	 * 			The given (effective) item does not yet refer to this directory as its parent directory
	 * 			| item != null && item.getParentDirectory() != this
	 *          
	 * @note	This is an auxiliary method that helps set up a bidirectional relationship. 
	 * 			It should only be called from within the controlling class. 
	 * 			It is therefore important to restrict the use of this method as much as possible.
	 * 			We thus require the provided item to have set up one way of the relationship already 
	 * 			from within the controlling class.
	 *			(This means that the given item is in a raw state!)
	 */ 
	@Model
	protected void addAsItem(@Raw DiskItem item) throws IllegalArgumentException{
		if(item == null)
			throw new IllegalArgumentException("The given item is non-effective.");
		if(hasAsItem(item))
			throw new IllegalArgumentException("The given item already exists in this directory.");
		if(!canHaveAsItem(item))
			throw new IllegalArgumentException("The given item is not allowed in this directory.");
		if(item.getParentDirectory() != this)
			throw new IllegalArgumentException("The given item does not yet refer to this parent directory.");
		
		addItemAt(item, getInsertionIndexOf(item));
		setModificationTime();
	}
	
	/**
	 * Insert the given item at the given index.
	 * 
	 * @param   item
	 *          The item to be added.
	 * @param   index
	 *          The index where the given item must be inserted.
	 * @post    The number of items registered in this directory is
	 *          incremented with 1.
	 *          | new.getNbItems() == getNbItems() + 1   
	 * @post    The given item is inserted at the given index.
	 *          | new.getItemAt(index) == item
	 * @post    All items after the given index are shifted
	 *          one position to the right.
	 *          | for each I in index..getNbItems():
	 *          |   new.getItemAt(I+1) == getItemAt(I)
	 * @throws  IllegalArgumentException
	 *          This directory already contains the given item or cannot have it at the given index.
	 *          | hasAsItem(item) || !canHaveAsItemAt(item,index)
	 */
	@Model
	private void addItemAt(@Raw DiskItem item, int index) throws IllegalArgumentException {
		if (hasAsItem(item) || !canHaveAsItemAt(item,index))
			throw new IllegalArgumentException("cannot add the given item at the given index to this directory");
		items.add(index-1,item);
	}
	
	/**
	 * Remove the given item from this directory.
	 *
	 * @param 	item
	 *        	The item to remove
	 * @effect 	If the item was in this directory, it is removed at the position it was registered
	 *         	| if (hasAsItem(item) then removeItemAt(getIndexOf(item))
	 * @effect 	The new modification time of this directory is updated.
	 *         	| setModificationTime()
	 * @throws 	IllegalArgumentException
	 * 			The given item is non-effective
	 * 			| item == null        
	 * @throws 	IllegalArgumentException
	 *         	The given item is not in the directory
	 *         	| !hasAsItem(item)
	 * @throws	IllegalArgumentException
	 * 			The given (effective) item still refers to an effective directory as its parent directory
	 * 			| item != null && item.getParentDirectory() != null
	 * 
	 * @note	This is an auxiliary method that helps break off a bidirectional relationship. 
	 * 			It should only be called from within the controlling class. 
	 * 			It is therefore important to restrict the use of this method as much as possible.
	 * 			We thus require the provided item to have broken off one way of the relationship already 
	 * 			from within the controlling class.
	 * @note	Alternatively, we could use an IllegalStateException in case the given item is not present 
	 * 			in this directory. This depends on how you interpret the error. 
	 * 			Is it the argument that is illegal, or is this directory in an Illegal state for the operation?
	 *  
	 */
	@Raw @Model
	protected void removeAsItem(DiskItem item) throws IllegalArgumentException{
		if(item == null)
			throw new IllegalArgumentException("The given item is non-effective.");
		if(!hasAsItem(item))
			throw new IllegalArgumentException("This item is not present in this directory.");
		if(item.getParentDirectory() != null)
			throw new IllegalArgumentException("The given item still refers to an effective parent directory.");
		
		try{
			removeItemAt(getIndexOf(item));
		}catch(IndexOutOfBoundsException e){
			//this will not happen
			assert(false);
		}
		setModificationTime();
	}

	/**
	 * Remove the given item at the given index from this directory.
	 *
	 * @param 	index
	 *        	The index from the item to remove.
	 * @post	This directory no longer has the item at the given index as an item
	 * 			| !new.hasAsItem(getItemAt(index))
	 * @post  	All elements to the right of the removed item
	 *        	are shifted left by 1 position.
	 *        	| for each I in index+1..getNbItems():
	 *        	|   new.getItemAt(I-1) == getItemAt(I)
	 * @post  	The number of items has decreased by one
	 *        	| new.getNbItems() == getNbItems() - 1
	 * @throws	IndexOutOfBoundsException
	 *        	The given position is not positive or exceeds the number
	 *        	of items registered in this directory. 
	 *        	| (index < 1) || (index > getNbItems())
	 */
	@Raw @Model 
	private void removeItemAt(int index) throws IndexOutOfBoundsException{
		if(index < 1 || index > getNbItems())
			throw new IndexOutOfBoundsException("Index out of bounds: "+index);
		items.remove(index-1);
	}
	
	/**
	 * Check whether this directory contains an item with the given name
	 * @param	name
	 * 			The name to check.
	 * @return  True if an item with the given name (ignoring case)
	 *          is registered at some position in this directory; 
	 *          false otherwise.
	 *        | result ==
	 *        |   (for some I in 1..getNbItems():
	 *        |      getItemAt(I).getName().equalsIgnoreCase(name))
	 */
	@Raw
	public boolean containsDiskItemWithName(String name){
		for (int i=1;i<=getNbItems();i++) {
			if (getItemAt(i).getName().equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check whether this directory contains an item with the given name
	 * @param	name
	 * 			The name to check.
	 * @return  True if an item with the given name (ignoring case)
	 *          is registered in this directory; 
	 *          false otherwise.
	 *        | result == containsDiskItemWithName(name)
	 */
	public boolean exists(String name){
		return containsDiskItemWithName(name);
	}

	/**
	 * Return the item in this directory with the given name.
	 * 
	 * @param 	name
	 *        	The name of the item to be looked up.
	 * @return 	the item in this directory with the given name, if such an item exists
	 * 			null otherwise
	 * 			| if (containsDiskItemWithName(name)) 
	 * 			| then (hasAsItem(result) && 
	 *         	| 		result.getName().equalsIgnoreCase(name))
	 *         	| else result == null
	 *         
	 * @note	This operation should complete in O(log(n)) time
	 */
	public DiskItem getItem(String name) {
		//do a binary search!
		int low = 1;
		int high = getNbItems();
		while (low <= high) {
			int middle = (low+high)/2;
			DiskItem middleItem = getItemAt(middle);
			if(middleItem.getName().equalsIgnoreCase(name)) 
				return middleItem;
			if (middleItem.isOrderedAfter(name)) {
				high = middle-1;
			} else {
				low = middle+1;
			}
		}
		//if not found, return null
		return null;
	}
	
	/**
	 * Return the position at which the given item is registered.
	 *
	 * @param   item
	 *          The item to be searched.
	 * @return  The given item is registered in this directory at the
	 *          resulting position.
	 *          | getItemAt(result)==item
	 * @throws  IllegalArgumentException
	 *          The given item is not in the directory
	 *          | ! hasAsItem(item)
	 */
	public int getIndexOf(@Raw DiskItem item) throws IllegalArgumentException {
		if(!hasAsItem(item))
			throw new IllegalArgumentException("This item is not present in this directory");
		else{
			for(int i=1; i<=getNbItems(); i++){
				if(getItemAt(i) == item) return i;
			}
			//this will never happen!
			assert false;
			return -1;
		}
	}
	
	/**
	 * Restore the order of the items in this directory, 
	 * given only one of them may be in the wrong position due to a name change
	 * 
	 * @param	index
	 * 			the index of the item with a new name
	 * @post	this directory has proper items	
	 * 			| hasProperItems()
	 * @post	The index of a certain number of items has changed
	 * 			| for each I in 1..getNbItems() :
	 *        	|   for some J in 1..getNbItems() :
	 *        	|      new.getItemAt(J) == getItemAt(I)
	 * @effect 	The new modification time of this directory is updated.
	 *         	| setModificationTime()
	 * @throws	IndexOutOfBoundsException
	 * 			The given index is not valid
	 * 			| (index < 1) || (index > getNbItems())
	 */
	@Raw @Model
	protected void restoreOrderAfterNameChangeAt(int index) {
		if(index < 1 || index > getNbItems())
			throw new IndexOutOfBoundsException("The index is not valid");
		try{
			DiskItem item = getItemAt(index);
			removeItemAt(index);
			addItemAt(item,getInsertionIndexOf(item));
			// Note that we did not change modifiction time of this directory, only because we use the base
			// mutators and not the RemoveAsItem and AddAsItem mutators (which do change the modification time)!
		}catch(IllegalArgumentException e){
			//this should not happen
			assert false;
		}catch(DiskItemNotWritableException e){
			//this should not happen
			assert false;
		}catch(IndexOutOfBoundsException e){
			//this should not happen
			assert false;
		}
	}
	
	/** 
	 * Check whether this disk directory can have the given directory as
	 * its parent directory.
	 * 
	 * @param  	directory
	 *          The directory to check.
	 * 
	 * @return 	If this directory is not terminated, and the given directory is effective and not terminated,
	 * 			then if the given directory is the same as this directory, then false
	 * 				 if the given directory is a direct or indirect child of this directory, then false
	 * 				 true otherwise
	 * 			| if (!this.isTerminated() && directory != null && !directory.isTerminated())
	 * 			|	then result == ( this != directory && !directory.isDirectOrIndirectChildOf(this) )
	 * 
	 * @note	This checker now closes the specification of the inherited checker by specifying the result in all
	 * 			other cases than described at the level of the superclass.
	 * 			In case the disk item is a directory, it is important that cycles in the file system structure are prohibited.
	 */
	@Raw @Override
	public boolean canHaveAsParentDirectory(Directory directory) {
		if (!this.isTerminated() && directory != null && !directory.isTerminated()) {
			return this != directory && !directory.isDirectOrIndirectChildOf(this);
		} else {
			// In the other cases, we can rely on the implementation in the superclass.
			return super.canHaveAsParentDirectory(directory);
		}
	}
	
	/**
	 * Checks whether this directory is a direct or indirect subdirectory of the given directory.
	 * 
	 * @param 	directory
	 * 			The directory to check.
	 * @return	True if and only if this directory is a direct or indirect child of the given directory
	 * 			| result == super.isDirectOrIndirectChildOf(directory)
	 */
	public boolean isDirectOrIndirectSubdirectoryOf(Directory directory) {
		return isDirectOrIndirectChildOf(directory);
	}
	
}
