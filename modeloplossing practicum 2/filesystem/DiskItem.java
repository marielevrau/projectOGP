package filesystem;

import java.util.Date;

import be.kuleuven.cs.som.annotate.*;
import filesystem.exception.*;

/**
 * An abstract class of disk items.
 *
 * @invar	Each disk item must have a properly spelled name.
 * 			| isValidName(getName())
 * @invar   Each disk item must have a valid creation time.
 *          | isValidCreationTime(getCreationTime())
 * @invar   Each disk item must have a valid modification time.
 *          | canHaveAsModificationTime(getModificationTime())
 * @invar   Each disk item must have a valid parent directory.
 *          | hasProperParentDirectory()
 * 
 * @author 	Tommy Messelis      
 * @version 3.0
 * 
 */
public abstract class DiskItem {

	/**
	 * Initialize a new root disk item with given name and writability.
	 * 
	 * @param  	name
	 *         	The name of the new disk item.
	 * @param  	writable
	 *         	The writability of the new disk item.
	 * 
	 * @effect  The name of the disk item is set to the given name.
	 * 			If the given name is not valid, a default name is set.
	 *          | setName(name) 
	 * @effect	The writability is set to the given flag
	 * 			| setWritable(writable)
	 * @post 	The disk item is a root item
	 * 			| new.isRoot()
	 * @post    The new creation time of this disk item is initialized to some time during
	 *          constructor execution.
	 *          | (new.getCreationTime().getTime() >= System.currentTimeMillis()) &&
	 *          | (new.getCreationTime().getTime() <= (new System).currentTimeMillis())
	 * @post    The new disk item has no time of last modification.
	 *          | new.getModificationTime() == null
	 * @post    The new disk item is not terminated.
	 *          | !new.isTerminated()          
	 */
	@Model @Raw
	protected DiskItem(String name, boolean writable) {
		setName(name);
		setWritable(writable);
	}

	/**
	 * Initialize a new disk item with given parent directory, name and 
	 * writability.
	 *   
	 * @param  	parent
	 *         	The parent directory of the new disk item.
	 * @param  	name
	 *         	The name of the new disk item.  
	 * @param  	writable
	 *         	The writability of the new disk item.
	 *         
	 * @effect  The name of the disk item is set to the given name.
	 * 			If the given name is not valid, a default name is set.
	 *          | setName(name) 
	 * @effect	The writability is set to the given flag
	 * 			| setWritable(writable)
	 * @effect 	The given directory is set as the parent 
	 *         	directory of this item.
	 *         	| setParentDirectory(parent)
	 * @effect 	This item is added to the items of the parent directory
	 *         	| parent.addAsItem(this)
	 * @post    The new creation time of this disk item is initialized to some time during
	 *          constructor execution.
	 *          | (new.getCreationTime().getTime() >= System.currentTimeMillis()) &&
	 *          | (new.getCreationTime().getTime() <= (new System).currentTimeMillis())
	 * @post    The new disk item has no time of last modification.
	 *          | new.getModificationTime() == null
	 * @post    The new disk item is not terminated.
	 *          | !new.isTerminated()
	 *          
	 * @throws 	IllegalArgumentException
	 *         	The given parent directory is not effective
	 *         	| parent == null
	 * @throws 	DiskItemNotWritableException(parent)
	 *         	The given parent directory is effective, but not writable.
	 *         	| parent != null && !parent.isWritable()
	 * @throws 	IllegalArgumentException
	 *         	The given valid name already exists in the effective and writable parent directory
	 *          | parent != null && parent.isWritable() && 
	 *         	|   isValidName(name) && parent.containsDiskItemWithName(name)
	 * @throws 	IllegalArgumentException
	 *         	The given name is not valid and the default name already exists in 
	 *         	the effective parent directory
	 *          | parent != null && parent.isWritable() && 
	 *         	|   !isValidName(name) && parent.containsDiskItemWithName(getDefaultName())
	 */
	@Model @Raw
	protected DiskItem(Directory parent, String name, boolean writable) 
			throws IllegalArgumentException, DiskItemNotWritableException {
		if (parent == null) 
			throw new IllegalArgumentException();
		if (!parent.isWritable()) 
			throw new DiskItemNotWritableException(parent);
		if (parent.isWritable() && isValidName(name) && parent.containsDiskItemWithName(name))
			throw new IllegalArgumentException();
		if (parent.isWritable() && !isValidName(name) && parent.containsDiskItemWithName(getDefaultName()))
			throw new IllegalArgumentException();
		
		setName(name);
		setWritable(writable);
		setParentDirectory(parent);
		try {
			parent.addAsItem(this);
		} catch (DiskItemNotWritableException e) {
			//cannot occur
			assert false;
		} catch (IllegalArgumentException e) {
			//cannot occur
			assert false;
		}
	}

	/**********************************************************
	 * delete/termination
	 **********************************************************/

	/**
	 * Variable registering whether or not this disk item has been terminated.
	 */
	private boolean isTerminated = false;


	/**
	 * Check whether this disk item is already terminated.
	 */
	@Raw @Basic
	public boolean isTerminated() {
		return isTerminated;
	}


	/**
	 * Check whether this disk item can be terminated.
	 * 
	 * @return	True if the disk item is not yet terminated, is writable and it is either a root or
	 * 			its parent directory is writable
	 * 			| if (isTerminated() || !isWritable() || (!isRoot() && !getParentDirectory().isWritable()))
	 * 			| then result == false
	 * @note	This specification must be left open s.t. the subclasses can change it
	 */
	public boolean canBeTerminated(){
		return !isTerminated() && isWritable() && (isRoot() || getParentDirectory().isWritable());
	}

	/**
	 * Terminate this disk item.
	 * 
	 * @post 	This disk item is terminated.
	 *       	| new.isTerminated()
	 * @post	The disk item becomes a root item.
	 * 			| new.isRoot();
	 * @effect 	If this disk item is not terminated and it is not a root, it is made a root
	 * 			| if (!isTerminated() && !isRoot())  
	 * 			| then makeRoot()
	 * @throws 	IllegalStateException
	 * 		   	This disk item is not yet terminated and it can not be terminated.
	 * 		   	| !isTerminated() && !canBeTerminated()
	 */
	public void terminate() throws IllegalStateException {
		if(!isTerminated()){
			if (!canBeTerminated()) {
				throw new IllegalStateException("This item cannot be terminated");
			}
			if(!isRoot()){
				try{
					makeRoot();
				}catch(DiskItemNotWritableException e){
					//should not happen since this item and its parent are writable
					assert false;
				}
			}
			this.isTerminated = true;
		}
	}	




	/**********************************************************
	 * name - total programming
	 **********************************************************/

	/**
	 * Variable referencing the name of this disk item.
	 */
	private String name = null;

	/**
	 * Return the name of this disk item.
	 */
	@Raw @Basic 
	public String getName() {
		return name;
	}

	/**
	 * Check whether the given name is a legal name for a disk item.
	 * 
	 * @param  	name
	 *			The name to be checked
	 * @return	True if the given string is effective, not
	 * 			empty and consisting only of letters, digits, dots,
	 * 			hyphens and underscores; false otherwise.
	 * 			| result ==
	 * 			|	(name != null) && name.matches("[a-zA-Z_0-9.-]+")
	 */
	public static boolean isValidName(String name) {
		return (name != null && name.matches("[a-zA-Z_0-9.-]+"));
	}

	/**
	 * Set the name of this disk item to the given name.
	 *
	 * @param   name
	 * 			The new name for disk item file.
	 * @post    If the given name is valid, the name of
	 *          this disk item is set to the given name,
	 *          otherwise the name of the disk item is set to a valid name (the default).
	 *          | if (isValidName(name))
	 *          |      then new.getName().equals(name)
	 *          |      else new.getName().equals(getDefaultName())
	 */
	@Raw @Model 
	private void setName(String name) {
		if (isValidName(name)) {
			this.name = name;
		} else {
			this.name = getDefaultName();
		}
	}

	/**
	 * Return the name for a new disk item which is to be used when the
	 * given name is not valid.
	 *
	 * @return	A valid file name.
	 *         	| isValidName(result)
	 */
	@Model
	private static String getDefaultName() {
		return "new_diskitem";
	}

	/**
	 * Set the name of this disk item to the given name.
	 *
	 * @param	name
	 * 			The new name for this disk item.
	 * @effect  The name of this disk item is set to the given name.
	 *          | setName(name)
	 * @effect  The modification time is updated.
	 *          | setModificationTime()
	 * @effect  If this disk item is not a root item, the order of the items in the parent
	 * 			directory is restored given the updated name of this item.
	 * 			| if (!isRoot()) then 
	 * 			|	getParentDirectory().restoreOrderAfterNameChangeAt(getParentDirectory().getIndexOf(this))
	 * @effect	If this item is not a root item, then the modification time of the parent directory is updated.
	 * 			| if (!isRoot()) then 
	 * 			|	getParentDirectory().setModificationTime()
	 *          
	 * @throws 	IllegalStateException
	 * 			This disk item is already terminated
	 * 			| isTerminated()
	 * @throws  DiskItemNotWritableException(this)
	 *          This disk item is not writable.
	 *          | !isWritable()
	 * @throws  DiskItemNotWritableException(getParentDirectory())
	 *          This disk item is not a root item and the parent directory is not writable.
	 *          | !isRoot() && !getParentDirectory().isWritable()
	 * @throws 	IllegalArgumentException
	 * 			The given name is valid, but this item is not a root item and 
	 * 			its parent directory already contains an item with the given name.
	 * 			| !isRoot() && isValidName(name) && getParentDirectory().containsDiskItemWithName(name)
	 * @throws 	IllegalArgumentException
	 * 			The given name is not valid, and this item is not a root item and 
	 * 			its parent directory already contains an item with the default name.
	 * 			| !isRoot() && !isValidName(name) && getParentDirectory().containsDiskItemWithName(getDefaultName())
	 */
	public void changeName(String name) throws DiskItemNotWritableException, IllegalStateException {
		if (isTerminated()) 
			throw new IllegalStateException("Disk item terminated!");
		if (!isWritable()) 
			throw new DiskItemNotWritableException(this);
		if (!isRoot() && !getParentDirectory().isWritable()) 
			throw new DiskItemNotWritableException(getParentDirectory());
		if (!isRoot() && isValidName(name) && getParentDirectory().containsDiskItemWithName(name))
			throw new IllegalArgumentException("Parent directory already contains this name.");
		if (!isRoot() && !isValidName(name) && getParentDirectory().containsDiskItemWithName(getDefaultName()))
			throw new IllegalArgumentException("Invalid name and parent directory already contains the default name.");
		
		//setName will take any name and still change the name of this item to a valid name.
		setName(name);
		if(!isRoot()){
			int currentIndexInParent = getParentDirectory().getIndexOf(this);
			getParentDirectory().restoreOrderAfterNameChangeAt(currentIndexInParent);
			getParentDirectory().setModificationTime();
		}
		setModificationTime();
	}

	/**
	 * Checks whether the name of this item is lexicographically 
	 * ordered after the given name, ignoring case.
	 * 
	 * @param 	name
	 *       	The name to compare with
	 * @return 	True if the given name is effective and the name of this item 
	 * 			comes strictly after the given name (ignoring case), 
	 *         	false otherwise.
	 *       	| result == (name != null) && (getName().compareToIgnoreCase(name) > 0)
	 */
	public boolean isOrderedAfter(String name) {
		return (name != null) && (getName().compareToIgnoreCase(name) > 0);
	}

	/**
	 * Checks whether the name of this item is lexicographically 
	 * ordered before the given name, ignoring case.
	 * 
	 * @param 	name
	 *       	The name to compare with
	 * @return 	True if the given name is effective and the name of this item 
	 * 			comes strictly before the given name (ignoring case), 
	 *         	false otherwise.
	 *       	| result == (name != null) && (getName().compareToIgnoreCase(name) < 0)
	 */
	public boolean isOrderedBefore(String name) {
		return (name != null) && (getName().compareToIgnoreCase(name) < 0);
	}

	/**
	 * Checks whether this item is ordered after the given other item
	 * according to the lexicographic ordering of their names,
	 * ignoring case.
	 * 
	 * @param 	other
	 *        	The item to compare with
	 * @return 	True if the given other item is effective, and the name
	 *         	of this item is lexicographically ordered after the name
	 *         	of the given other item (ignoring case),
	 *         	false otherwise.
	 *       	| result == (other != null) && 
	 *       	|           isOrderedAfter(other.getName())
	 */
	public boolean isOrderedAfter(@Raw DiskItem other) {
		return (other != null) && isOrderedAfter(other.getName());
	}

	/**
	 * Checks whether this item is ordered before the given other item
	 * according to the lexicographic ordering of their names,
	 * ignoring case.
	 * 
	 * @param 	other
	 *        	The item to compare with
	 * @return 	True if the given other item is effective, and the name
	 *         	of this item is lexicographically ordered before the name
	 *         	of the given other item (ignoring case),
	 *         	false otherwise.
	 *       	| result == (other != null) && 
	 *       	|           isOrderedBefore(other.getName())
	 */
	public boolean isOrderedBefore(@Raw DiskItem other) {
		return (other != null) && isOrderedBefore(other.getName());
	}


	/**********************************************************
	 * creationTime
	 **********************************************************/

	/**
	 * Variable referencing the time of creation.
	 */
	private final Date creationTime = new Date();

	/**
	 * Return the time at which this disk item was created.
	 */
	@Raw @Basic @Immutable 
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Check whether the given date is a valid creation time.
	 *
	 * @param  	date
	 *         	The date to check.
	 * @return 	True if and only if the given date is effective and not
	 * 			in the future.
	 *         	| result == 
	 *         	| 	(date != null) &&
	 *         	| 	(date.getTime() <= System.currentTimeMillis())
	 */
	public static boolean isValidCreationTime(Date date) {
		return 	(date!=null) &&
				(date.getTime()<=System.currentTimeMillis());
	}




	/**********************************************************
	 * modificationTime
	 **********************************************************/

	/**
	 * Variable referencing the time of the last modification,
	 * possibly null.
	 */
	private Date modificationTime = null;

	/**
	 * Return the time at which this disk item was last modified, that is
	 * at which the name or content was last changed. If this disk item has
	 * not yet been modified after construction, null is returned.
	 */
	@Raw @Basic
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * Check whether this disk item can have the given date as modification time.
	 *
	 * @param	date
	 * 			The date to check.
	 * @return 	True if and only if the given date is either not effective
	 * 			or if the given date lies between the creation time and the
	 * 			current time.
	 *         | result == (date == null) ||
	 *         | ( (date.getTime() >= getCreationTime().getTime()) &&
	 *         |   (date.getTime() <= System.currentTimeMillis())     )
	 */
	@Raw
	public boolean canHaveAsModificationTime(Date date) {
		return (date == null) ||
				( (date.getTime() >= getCreationTime().getTime()) &&
						(date.getTime() <= System.currentTimeMillis()) );
	}

	/**
	 * Set the modification time of this disk item to the current time.
	 *
	 * @post   The new modification time is effective.
	 *         | new.getModificationTime() != null
	 * @post   The new modification time lies between the system
	 *         time at the beginning of this method execution and
	 *         the system time at the end of method execution.
	 *         | (new.getModificationTime().getTime() >=
	 *         |                    System.currentTimeMillis()) &&
	 *         | (new.getModificationTime().getTime() <=
	 *         |                    (new System).currentTimeMillis())
	 */
	@Model @Raw
	protected void setModificationTime() {
		modificationTime = new Date();
	}

	/**
	 * Return whether this disk item and the given other disk item have an
	 * overlapping use period.
	 *
	 * @param 	other
	 *        	The other disk item to compare with.
	 * @return 	False if the other disk item is not effective
	 * 			False if the prime object does not have a modification time
	 * 			False if the other disk item is effective, but does not have a modification time
	 * 			otherwise, true if and only if the open time intervals of this disk item and
	 * 			the other disk item overlap
	 *        	| if (other == null) then result == false else
	 *        	| if ((getModificationTime() == null)||
	 *        	|       other.getModificationTime() == null)
	 *        	|    then result == false
	 *        	|    else 
	 *        	| result ==
	 *        	| ! (getCreationTime().before(other.getCreationTime()) && 
	 *        	|	 getModificationTime().before(other.getCreationTime()) ) &&
	 *        	| ! (other.getCreationTime().before(getCreationTime()) && 
	 *        	|	 other.getModificationTime().before(getCreationTime()) )
	 */
	public boolean hasOverlappingUsePeriod(DiskItem other) {
		if (other == null) return false;
		if(getModificationTime() == null || other.getModificationTime() == null) return false;
		return ! (getCreationTime().before(other.getCreationTime()) && 
				getModificationTime().before(other.getCreationTime()) ) &&
				! (other.getCreationTime().before(getCreationTime()) && 
						other.getModificationTime().before(getCreationTime()) );
	}


	/**********************************************************
	 * writable
	 **********************************************************/

	/**
	 * Variable registering whether or not this disk item is writable.
	 */
	private boolean isWritable = true;

	/**
	 * Check whether this disk item is writable.
	 */
	@Raw @Basic
	public boolean isWritable() {
		return isWritable;
	}

	/**
	 * Set the writability of this disk item to the given writability.
	 *
	 * @param isWritable
	 *        The new writability
	 * @post  The given writability is registered as the new writability
	 *        for this disk item.
	 *        | new.isWritable() == isWritable
	 */
	@Raw 
	public void setWritable(boolean isWritable) {
		this.isWritable = isWritable;
	}




	/**********************************************************
	 * parent directory
	 **********************************************************/	

	/**
	 * Variable referencing the directory (if any) to which this 
	 * disk item belongs.
	 * 
	 * @note This class is the controlling class for the bidirectional relationship.
	 * 
	 */
	private Directory parentDirectory = null;


	/**
	 * Return the root item to which this item directly or indirectly
	 * belongs. In case this item is a root item, the item itself is 
	 * the result.
	 * 
	 * @return If this item is a root item, this item is returned;
	 *         Otherwise the root to which the parent item of this 
	 *         item belongs is returned.
	 *         | if (isRoot())
	 *         | then result == this
	 *         | else result == getParentDirectory().getRoot()
	 */
	public DiskItem getRoot() {
		if (isRoot()) {
			return this;
		} else {
			return getParentDirectory().getRoot();
		}
	}
	
	/**
	 * Turns this disk item into a root disk item. (Nothing happens if this item is already a root item.)
	 * 
	 * @effect  If this disk item is not a root, this disk item is
	 *          removed from its parent directory.
	 *          | if (!isRoot())
	 *          | then getParentDirectory().removeAsItem(this)
	 * @effect  If this disk item is not a root, its modification time changed
	 * 			| if (!isRoot())
	 *          | then setModificationTime()    
	 * @effect	The parent directory of this item is set to null.
	 * 			| setParentDirectory(null)     
	 * 
	 * @throws 	IllegalStateException
	 * 			This disk item is terminated
	 * 			| isTerminated()
	 * @throws	DiskItemNotWritableException(this)
	 * 			This disk item is not a root and it is not writable
	 * 			| !isRoot() && !isWritable()
	 * @throws	DiskItemNotWritable(getParentDirectory())	
	 * 			This disk item is not a root and its parent directory is not writable
	 * 			| !isRoot() && !getParentDirectory().isWritable()
	 * 
	 */ 
	public void makeRoot() throws DiskItemNotWritableException {
		if (isTerminated()) 
			throw new IllegalStateException("Diskitem is terminated!");
		if (!isRoot()) {
			if (!isWritable()) 
				throw new DiskItemNotWritableException(this);
			if(!getParentDirectory().isWritable())
				throw new DiskItemNotWritableException(getParentDirectory());
			
			// First, break off the bidirectional relationship from this side:
			Directory dir = getParentDirectory();
			setParentDirectory(null); 
			// Please note that the parent directory object is now in a raw state:
			// it refers to this item, which does not point back to that directory anymore!
			
			// Then, break off the relationship from the other side:
			try{
				dir.removeAsItem(this); // The removeAsItem method should be annotated raw because it is currently in a raw state.
			} catch(IllegalArgumentException e) {
				// This should not happen.
				assert(false);
			}
			// Don't forget to change the modification time now.
			setModificationTime();
		} 
		// If the item was already a root item, nothing happens.
	}

	/**
	 * Move this disk item to a given directory.
	 * 
	 * @param   target
	 *          The target directory.
	 * @effect  If this disk item is not a root, this disk item is
	 *          removed from its parent directory.
	 *          | if (!isRoot())
	 *          | then getParentDirectory().removeAsItem(this) 
	 * @effect  This disk item is added to the target directory.
	 *          | target.addAsItem(this)
	 * @effect  The modification time is updated.
	 *          | setModificationTime()
	 * @effect  The given directory is registered as the parent directory 
	 *          of this item.
	 *          | setParentDirectory(target)
	 * 
	 * @throws 	IllegalStateException
	 * 			This disk item is terminated
	 * 			| isTerminated()
	 * @throws	DiskItemNotWritableException(this)
	 * 			This disk item is not writable
	 * 			| !isWritable()
	 * @throws	DiskItemNotWritable(getParentDirectory())	
	 * 			This disk item is not a root and its parent directory is not writable
	 * 			| !isRoot() && !getParentDirectory().isWritable()
	 * 
	 * @throws	IllegalArgumentException
	 * 			The given target directory is non-effective or is already registered as the parent directory.
	 * 			| (target == null) || (target == this.getParentDirectory())
	 * @throws	DiskItemNotWritableException(target)
	 * 			This given target directory is effective but not writable
	 * 			| (target != null) && !target.isWritable()
	 * @throws	IllegalArgumentException
	 * 			The given directory is effective, but can not have this item as a member.
	 * 			| target != null && !target.canHaveAsItem(this)
	 * @throws  IllegalArgumentException
	 * 			The item cannot have the given target directory as a parent.
	 * 			(e.g. Moving the item to the given (effective) target directory would result in a cycle in the structure.)
	 * 			|target != null && !canHaveAsParentDirectory(target)
	 * 
	 * @note	Part of the job is to make this item a root item before adding it to the new parent directory.
	 * 			While we can use the implementation of that method, we can NOT use its specification in an effect clause!
	 * 			Doing so would include post condition that the item has null as a parent, which will not be the case here.
	 * 
	 */
	public void move(Directory target) 
			throws IllegalArgumentException, DiskItemNotWritableException, IllegalStateException {
		
		// is this item ok?
		if (isTerminated()) 
			throw new IllegalStateException("Diskitem is terminated!");
		if (!isWritable())
			throw new DiskItemNotWritableException(this);
		if (!isRoot() && !getParentDirectory().isWritable())
			throw new DiskItemNotWritableException(this.getParentDirectory());
				
		// is the target ok?
		if (target == null)
			throw new IllegalArgumentException("The target directory is non-effective.");
		if (target == this.getParentDirectory())
			throw new IllegalArgumentException("The target directory is already the parent directory.");
		if (!target.isWritable())
			throw new DiskItemNotWritableException(target); 
		if (!target.canHaveAsItem(this))
			throw new IllegalArgumentException("The target directory can not have this item as a member.");
		  	// Note that this if clause is only executed if the target is effective (and even writable).
		if (!canHaveAsParentDirectory(target))
			throw new IllegalArgumentException("The target is not allowed as a parent directory.");
					
		// then move!
		
		// If this item is not a root item, first remove it from its parent
		if (!isRoot()) {
			try{
				// First, break off one direction before we can remove
				Directory dir = getParentDirectory();
				setParentDirectory(null); 
				// Then, break off the other direction
				dir.removeAsItem(this);
			}catch(IllegalArgumentException e){
				//this cannot happen
				assert false;
			}
			// Note: if we were to use the makeRoot() method, we would already have set the modification time.
			// That is not really a problem here, so this would also be allows.
		}
		
		// Then, set up the new relationship
		// First, set up one direction
		try {
			setParentDirectory(target); 
		}catch(IllegalArgumentException e){
			//this should not happen, because the parent is allowed
			assert false;
		}catch(IllegalStateException e){
			//this should not happen, because we checked termination state
			assert false;
		}
		// Please note this item is now in a raw state! (i.e. points to a parent that doesn't refer back)
		try{
			// Second, set up the other direction using an auxiliary method in the non-controlling class
			target.addAsItem(this); // the formal argument of addAsItem should thus be annotated @Raw
		}catch(IllegalArgumentException e){
			//this should not happen, because it can have this item
			assert false;
		}catch(DiskItemNotWritableException e){
			//this should not happen, because we checked it
			assert false;
		}
		
		// Don't forget to change the modification time now.
		setModificationTime();
	}

	/**
	 * Check whether this item is a root item.
	 * 
	 * @return  True if this item has a non-effective parent directory;
	 *          false otherwise.
	 *        	| result == (getParentDirectory() == null)
	 */
	@Raw
	public boolean isRoot() {
		return getParentDirectory() == null;
	}

	/** 
	 * Check whether this disk item has a proper parent directory as
	 * its parent directory.
	 * 
	 * @return  true if this disk item can have its registered parent directory 
	 * 			as its parent directory and it is either a root, or 
	 * 			its registered parent directory has this item as a registered item.
	 *          | result == canHaveAsParentDirectory(getParentDirectory()) &&
	 *			|            (isRoot() || getParentDirectory().hasAsItem(this))
	 *	@note	This checker is split up in two parts, the consistency of the 
	 *			bidirectional relationship is added to the functionality of 
	 *			the internal state checker (canHaveAsParentDirectory())
	 */
	@Raw 
	public boolean hasProperParentDirectory() {
		return canHaveAsParentDirectory(getParentDirectory()) &&
				(isRoot() || getParentDirectory().hasAsItem(this));
	}
	
	/** 
	 * Check whether this disk item can have the given directory as
	 * its parent directory.
	 * 
	 * @param  	directory
	 *          The directory to check.
	 * @return  If this disk item is terminated, 
	 * 			then true if the given directory is not effective, 
	 * 			     false otherwise.
	 *          | if (this.isTerminated()) then result == (directory == null)
	 * 
	 * @return  If the given directory is non-effective, then true
	 * 			| if (directory == null) then result == true
	 * 
	 * @return  If the given directory is effective and terminated, then false
	 * 			| if (directory != null && directory.isTerminated()) then result == false
	 * 
	 * 
	 * @note	The remaining checks are dependent on the type of disk item we are considering, 
	 * 			so we include them at the level of the subclass. We must therefore leave the
	 * 			specification of this checker open at this level. (It does not specify an answer
	 * 			in all other cases, so no contradictions can occur when combined with the specification
	 * 			at the level of the subclasses.)
	 * 
	 * @note	This checker considers only the content restrictions of the parent directory variable.
	 * 			It does not check the consistency of the bidirectional relationship.
	 * 			It can be used to check if the current parent directory is valid, 
	 * 			or if a given directory can be accepted as a (new) parent directory.
	 */
	@Raw 
	public boolean canHaveAsParentDirectory(Directory directory) {
		if (this.isTerminated())
			return (directory == null);
		if (directory == null)
			return true;
		if (directory.isTerminated())
			return false;
		// Note that the implementation requires an answer in all cases.
		return true;
	}
	
	/**
	 * Checks whether this item is a direct or indirect child item of the given directory.
	 * 
	 * @param 	directory
	 * 			The directory to check.
	 * @return  If this is a root item, then false
	 * 			| if this.isRoot() then result == false
	 * @return	If the given directory is non-effective, then false
	 * 			| if directory == null then result == false
	 * @return	If this is not a root item and the given directory is effective,
	 * 			then true if this item is a direct or indirect child of the given directory,
	 * 				 false otherwise
	 * 			| if (!this.isRoot() && directory != null)
	 * 			| then result == ( this.getParentDirectory() == directory ||
	 * 			|					this.getParentDirectory().isDirectOrIndirectChildOf(directory) )
	 * 
	 */
	public boolean isDirectOrIndirectChildOf(Directory directory) {
		if (this.isRoot() || directory == null)
			return false;
		else
			return this.getParentDirectory() == directory || this.getParentDirectory().isDirectOrIndirectChildOf(directory);
	}

	/**
	 * Set the parent directory of this item to the given directory.
	 *
	 * @param   directory
	 *          The new parent directory for this item.
	 * @post    The parent directory of this item is set to the given 
	 *          directory.
	 *          | new.getParentDirectory() == directory
	 * @throws  IllegalArgumentException
	 *          This item cannot have the given directory as its
	 *          parent directory.
	 *          | ! canHaveAsParentDirectory(parentDirectory)
	 * @throws 	IllegalStateException
	 * 			This disk item is terminated
	 * 			| isTerminated()
	 * 
	 * @note	The setter is only responsible to satisfy the invariants of this class, 
	 * 			not the bidirectionality of the relationship, 
	 * 			nor the extra constraints associated with moving or deleting items.
	 */
	@Raw @Model
	private void setParentDirectory(Directory directory)
			throws IllegalArgumentException, IllegalStateException {
		if (isTerminated()) 
			throw new IllegalStateException("Diskitem is terminated!");
		if (!canHaveAsParentDirectory(directory)) {
			throw new IllegalArgumentException("Inappropriate item!");
		}
		this.parentDirectory = directory;
	}

	/**
	 * Return the parent directory (if any) to which this item
	 * applies.
	 */
	@Raw @Basic
	public Directory getParentDirectory() {
		return parentDirectory;
	}


}
