import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class of ingredientTypes
 * 
 * @invar	Each ingredientType must have a properly spelled name.
 * 			| isValidName(getName())
 * @invar	Each ingredientType must have a valid state.
 * 			| isValidState(getState())
 * @invar	Each ingredientType must have a valid standard temperature.
 * 			| isValidStandardTemperature(getStandardTemperature())
 * 
* @author Jérôme D'hulst, Marie Levrau, Art Willems
*/

public class IngredientType {
	
	/**********************************************************
     * constructor
     **********************************************************/
	
	/**
	 * Initialize a new ingredientType with given name, state and standard temperature.
	 * @param 	name
	 * 			The name of the new ingredientType.
	 * @param 	state
	 * 			The state of the new ingredientType.
	 * @param	standardTemperature
	 * 			The standard temperature of the new ingredientType.
	 */
	public IngredientType(String name, String state, int[] standardTemperature) {
		setName(name);
		setState(state);
		setStandardTemperature(standardTemperature);
	}
	
	/**********************************************************
     * name 
     **********************************************************/
	
	/**
	 * Variable referencing the name of this ingredientType.
	 */
	private String name = null;
	
    /**
     * Return the name of this ingredientType.
     */
    @Raw @Basic 
    public String getName() {
        return name;
    }
    
    /**
     * Check whether the given name is a legal name for an ingredientType.
     * 
     * @param  	name
     *			The name to be checked
     * @return	True if 
     */
    public static boolean isValidName(String name) {
        return (name != null && name.matches("[a-zA-Z^()]+") && (name.length() >= 2) );
    }
    
    /**
     * Set the name of this ingredientType to the given name.
     *
     * @param   name
     * 			The new name for this ingredientType.
     * @post    If the given name is valid, the name of
     *          this ingredientType is set to the given name,
     *          otherwise the name of the ingredientType is set to a valid name (the default).
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
     * Return the name for a new ingredientType which is to be used when the
     * given name is not valid.
     *
     * @return   A valid name.
     *         | isValidName(result)
     */
    @Model
    private static String getDefaultName() {
        return "new_name";
    }
    
    

	/**********************************************************
     * state
     **********************************************************/
	
	/**
	 * Variable referencing the name of this ingredientType.
	 */
	private String state = null;
	
	 /**
     * Return the state of this ingredientType.
     */
    @Raw @Basic 
    public String getState() {
        return state;
    }
    
    /**
     * Check whether the given state is a legal state for an ingredientType.
     * 
     * @param  	state
     *			The state to be checked
     * @return	True if the given state is either a Liquid or a Powder
     * 			| result ==
     * 			|	(state == "Liquid") || (state == "Powder")		
     */
    public static boolean isValidState(String state) {
        return ((state == "Liquid") || (state == "Powder"));
    }
	
    /**
     * Set the state of this ingredientType to the given state.
     *
     * @param   state
     * 			The new state for this ingredientType.
     * @post    If the given state is valid, the state of
     *          this ingredientType is set to the given state,
     *          otherwise the state of the ingredientType is set to a valid state (the default).
     *          | if (isValidState(name))
     *          |      then new.getState().equals(state)
     *          |      else new.getState().equals(getDefaultState())
     */
    @Raw @Model 
    private void setState(String state) {
        if (isValidName(state)) {
        		this.state = state;
        } else {
        		this.state = getDefaultState();
        }
    }
    
    /**
     * Return the state for a new ingredientType which is to be used when the
     * given state is not valid.
     *
     * @return   A valid state.
     *         | isValidState(result)
     */
    @Model
    private static String getDefaultState() {
        return "new_state";
    }
    
	/**********************************************************
     * standardTemperature
     **********************************************************/
	
    /**
	 * Variable referencing the standard temperature of this ingredientType.
	 */
	private int[] standardTemperature = new int[2];
	
    /**
     * Return the standard temperature of this ingredientType.
     */
    @Raw @Basic 
    public int[] getStandardTemperature() {
        return standardTemperature;
    }
    
    @Raw @Model 
    private void setStandardTemperature(int[] standardTemperature) {
        if (isValidStandardTemperature(standardTemperature)) {
        		this.standardTemperature = standardTemperature;
        } else {
        		this.name = getDefaultStandardTemperature();
        }
    }
    
    /**
     * Check whether the given standard temperature is a legal standard temperature for a ingredientType.
     * 
     * @param  	standardTemperature
     *			The standard temperature to be checked
     * @return	True if the given standard temperature is strictly higher than [0,0] and 
     * 			the coldness and hotness are not both different from 0. 
     */
    public static boolean isValidStandardTemperature(int[] standardTemperature) {
        if (standardTemperature[0] != 0  && standardTemperature[1] != 0) {
        	return false;
        }
        else if (standardTemperature[0] == 0  && standardTemperature[1] == 0){
        	return false;
        }
        else {
        	return true;
        }
    }
    
    /**
     * Return the standard temperature for a new ingredientType which is to be used when the
     * given standard temperature is not valid.
     *
     * @return   A valid standard temperature.
     *         | isValidStandardTemperature(result)
     */
    @Model
    private static int[2] getDefaultStandardTemperature() {
        return [0,20];
    }
}
