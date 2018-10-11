package com.example.schne.dotsnlines;

import java.util.ArrayList;

public class Dot {
	private ArrayList<Dot> neighbours = new ArrayList<>();
	
	public Dot() {}
	
	public void addNeighbor(Dot dot) {
		neighbours.add(dot);
	}
	
	public boolean isValidNeighbor(Dot dot) {
		if (neighbours.contains(dot)) {
			neighbours.remove(dot);
			return true;
		}
		return false;
	}
}
