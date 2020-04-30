/**
 * 
 * @author Jérôme D'hulst
 *
 */
public class AlchemicIngredient {
	
	/**********************************************************
     * Constructors
     **********************************************************/
	public AlchemicIngredient(int quantity, IngredientType ingredientType, long hotness, long coldness, String state) {
		setIngredientType(ingredientType);
		setQuantity(quantity);
		setHotness(hotness);
		setColdness(coldness);
		setState(state);
		
	}
	
	public AlchemicIngredient(int quantity, IngredientType ingredientType) {
		
	}
	
	
	
	
	/**********************************************************
     * Name
     **********************************************************/
	
	
	
	
	/**********************************************************
     * Quantity
     **********************************************************/
	
	private int quantity = 0;
	
	private void setQuantity(int quantity) throws InvalidQuantityException {
		if (isCorrectQuantity(quantity)) {
			this.quantity = quantity;
		}
		else {
			throw new InvalidQuantityException(this);
		}
	}
	
	
	public boolean isCorrectQuantity(int quantity) {
		return (quantity >= 0);
	}
	
	
	public int getQuantity() {
		return this.quantity;
	}
	
	
	
	
	/**********************************************************
     * State
     **********************************************************/
	private String state = null;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**********************************************************
     * Temperature
     **********************************************************/
	private long coldness = 0;
	private long hotness = 0;
	public static long maxTemp = 10000;
	
	public static long getMaxTemp() {
		return maxTemp;
	}
	
	
	private void setColdness(long coldness) throws InvalidTemperatureException{
		if (isValidTemperature(coldness)) {
			this.coldness = coldness;
		}
		else {
			throw new InvalidTemperatureException(this);
		}
	}
	
	private void setHotness(long hotness) throws InvalidTemperatureException{
		if (isValidTemperature(hotness)) {
			this.hotness = hotness;
		}
		else {
			throw new InvalidTemperatureException(this);
		}
	}
	
	
	public boolean isValidTemperature(long temperature) {
		return (temperature > 0 && temperature <= getMaxTemp());
	}
	
	
	private void setTemperature(long hotness, long coldness) throws InvalidTempCombinationException {
		if (isValidTempCombination(hotness,coldness)) {
			setHotness(hotness);
			setColdness(coldness);
		}
		else {
			throw new InvalidTempCombinationException(this);
		}
	}
	
	public boolean isValidTempCombination(long hotness, long coldness) {
		return !(hotness > 0 && coldness > 0);
	}
	
	/*
	 * public getTemperature
	 */
	
	
	
	
	
	
	
	
		

			
	





	

}
