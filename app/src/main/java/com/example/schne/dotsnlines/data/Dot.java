package com.example.schne.dotsnlines.data;

import java.util.ArrayList;

/**
 * A Dot, associated with a Button, that contains its valid neighbors.
 */
public class Dot {
	// valid neighbours are either directly above, below, to the left or the right of *this* dot
	private ArrayList<Dot> validNeighbours = new ArrayList<>();
	
	public Dot() {
	}
	
	/**
	 * Record dot as a valid neighbor
	 *
	 * @param dot a valid neighbour
	 */
	public void addNeighbor(Dot dot) {
		validNeighbours.add(dot);
	}
	
	/**
	 * returns whether or not dot a valid neighbour.
	 * If so, dot gets removed from *this'* validNeighbours list and
	 * *this* dot gets removed from dot's validNeighbours list
	 *
	 * @param dot potential neighbour
	 * @return true iff dot is a valid neighbour
	 */
	public boolean isValidNeighbor(Dot dot) {
		if (validNeighbours.contains(dot)) {
			
			// both validNeighbors lists must be updated since the line between
			// them cannot be placed once more
			validNeighbours.remove(dot);
			dot.validNeighbours.remove(this);
			return true;
		}
		return false;
	}
}
