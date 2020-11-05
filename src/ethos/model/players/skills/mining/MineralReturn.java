package ethos.model.players.skills.mining;

/**
 * The purpose of this interface is to be wrapped in a {@link Mineral} object and return some item as the return for mining the mineral.
 * 
 * This is necessary because not all minerals return a singular item as a result of mining the resource.
 * 
 * @author Jason MacKeigan
 * @date Jun 1, 2015
 */
public interface MineralReturn {

	/**
	 * Generates an item as the return for mining a mineral from a vein.
	 * 
	 * @return the item generated as the return.
	 */
	public int generate();

	/**
	 * Determines all of the possible item identification values that can be returned by {@link #generate()}.
	 * 
	 * @return an array of item identification values.
	 */
	public int[] inclusives();

}
