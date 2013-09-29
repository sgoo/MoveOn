public interface Tickable {

	/**
	 * 
	 * @param ticks
	 * @return priority of their ticking 0 = highest
	 */
	public void tick(int ticks);

}
