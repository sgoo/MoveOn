package MoveOn;

import java.util.ArrayList;

public class Simulator {

	private ArrayList<Tickable> tickables;

	public void simulate(){
		tickables = new ArrayList<Tickable>();
		for (int i = 0;; i++) {
			for(Tickable tickable : tickables){
				tickable.tick(i);
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Simulator simulator = new Simulator();
		simulator.simulate();
	}
}
