import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;


/**
 * An abstract class of devices involving an alchemic ingredient and a laboratorium that owns this device.
 * 
 * @invar	Each device must have a proper laboratorium. 
 * 			| hasProperLaboratorium()
 * 
* @author Jérôme D'hulst, Marie Levrau, Art Willems
*/

public class Device {
	
	/**********************************************************
     * constructor
     **********************************************************/
	
	/**
	 * Initialize a new device with given alchemic ingredient and laboratorium that owns this device.
	 * @param 	alchemicIngredient
	 * 			The alchemicIngredient to put in this device.
	 * @param 	laboratorium
	 * 			The laboratorium that has this device.
	 *
	 * @effect  The alchemic ingredient of the device is set to the given alchemic ingredient.
	 * 			If the given alchemic ingredient is not valid, a default alchemic ingredient is set.
	 *          | setAlchemicIngredient(alchemicIngredient) 
	 * @effect  This new device is moved to the given laboratorium. 
	 * 			If the given laboratorium is not valid, a default laboratorium is set.
	 *          | moveTo(laboratorium)          
	 */
	@Model @Raw
	protected Device(AlchemicIngredient alchemicIngredient, Laboratorium laboratorium) {
		setAlchemicIngredient(alchemicIngredient);
		moveTo(laboratorium);
	}
	
	/**********************************************************
     * alchemicIngredient
     **********************************************************/
	
	/**********************************************************
     * laboratorium
     **********************************************************/
	
	/**********************************************************
     * termination
     **********************************************************/
	
	/**
	 * Variable registering whether or not this device has been terminated.
	 */
	private boolean isTerminated = false;


	/**
	 * Check whether this device is already terminated.
	 */
	@Raw @Basic
	public boolean isTerminated() {
		return isTerminated;
	}
	
	/**
	 * Check whether this device can be terminated.
	 * 
	 * @return	
	 */
	public boolean canBeTerminated(){
		return (!isTerminated() DINGEN TOEVOEGEN );
	}
	
	/**
	 * Terminate this device.
	 * 
	 * @post 	This device is terminated.
	 *       	| new.isTerminated()
	 * @throws 	IllegalStateException
	 * 		   	This device is not yet terminated and it can not be terminated.
	 * 		   	| !isTerminated() && !canBeTerminated()
	 */
	public void terminate() throws IllegalStateException {
		if(!isTerminated()){
			if (!canBeTerminated()) {
				throw new IllegalStateException("This item cannot be terminated");
			}
			this.isTerminated = true;
		}
	}	
	
	/**********************************************************
     * bidirection relation with laboratorium
     **********************************************************/
	
	/**
	 * Return the laboratorium that has this device.
	 *
	 */
	
	@Basic @Raw
	public Laboratorium getLaboratiorium() {
		return laboratorium;
	}
	
	/** 
	 * Check whether this device can have the given laboratorium as
	 * its laboratorium.
	 * 
	 * @param  	laboratorium
	 *          The laboratorium to check.
	 * @return  
	 */
	@Raw 
	public boolean canHaveAsLaboratorium(Laboratorium laboratorium) {
		if (this.isTerminated())
			return (laboratorium == null);
		else 
			return ( (laboratorium != null) && (!laboratorium.isTerminated()));
	}
	
	/** 
	 * Check whether this device has a proper laboratorium. 
	 * 
	 * @return  
	 */
	@Raw 
	public boolean hasProperLaboratorium() {
		return (canHaveAsLaboratorium(getLaboratorium()) NOG DINGEN TOEVOEGEN);
	}
	
	/**
	 * Move this device into the given laboratorium.
	 *
	 * @param   laboratorium
	 *          The laboratorium where this device is moved to.
	 * @post    This device is moved into the given laboratorium
	 *          | new.getLaboratorium() == laboratorium
	 * @throws  IllegalLaboratoriumException
	 *          This device cannot have the given laboratorium as the laboratorium where this device can move into.
	 *          | !canHaveAsLaboratorium(laboratorium)
	 * @throws 	IllegalStateException
	 * 			This device is already terminated
	 * 			| isTerminated()
	 * 
	 */
	@Raw @Model
	private void moveTo(Laboratorium laboratorium)
			throws IllegalLaboratoriumException, IllegalStateException {
		if (isTerminated()) 
			throw new IllegalStateException("Device is terminated!");
		if (!canHaveAsLaboratorium(laboratorium)) {
			throw new IllegalLaboratoriumException("Inappropriate laboratorium!");
		}
		this.laboratorium = laboratorium;
	}
	
	/**********************************************************
     * methodes
     **********************************************************/
	public void addQuantity(IngredientContainer container) {
		
	}
	
	public IngredientContainer removeResult() {
		return container;
	}
	
	public void executeOperation() {
		
	}
	
}