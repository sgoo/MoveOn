package moveon.controllers;

import moveon.simulation.Tickable;

public interface Controller extends Tickable {
	public void init(int currentTick);
}
