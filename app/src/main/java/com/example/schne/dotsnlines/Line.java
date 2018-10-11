package com.example.schne.dotsnlines;


import java.util.ArrayList;
import java.util.Arrays;

public class Line {
	private int[] point1;
	private int[] point2;
	private int boardSize;
	private ArrayList<Line> square1Partners = new ArrayList<>();
	private ArrayList<Line> square2Partners = new ArrayList<>();
	
	public Line(int[] point1, int[] point2, int boardSize) {
		this.point1 = point1;
		this.point2 = point2;
		this.boardSize = boardSize;
		
		generateSquarePartners();
	}
	
	private Line(int[] point1, int[] point2) {
		this.point1 = point1;
		this.point2 = point2;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(point1) + " " + Arrays.toString(point2);
	}
	
	public int getCompleteSquare(ArrayList<Line> drawnLines) {
		int square1LinesDrawn = 0;
		int square2LinesDrawn = 0;
		
		for (Line line : drawnLines) {
			if (square1Partners.contains(line) && square1LinesDrawn < 3) {
				square1LinesDrawn++;
			}
			if (square2Partners.contains(line) && square2LinesDrawn < 3) {
				square2LinesDrawn++;
			}
			if (square1LinesDrawn == 3 && square2LinesDrawn == 3) {
				return 3;
			}
		}
		
		if (square1LinesDrawn < 3 && square2LinesDrawn < 3) {
			return 0;
		}
		return square1LinesDrawn == 3 ? 1 : 2;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Line) {
			Line other = (Line) o;
			
			return (Arrays.equals(other.point1, point1) && Arrays.equals(other.point2, point2))
					|| (Arrays.equals(other.point2, point1) && Arrays.equals(other.point1, point2));
		}
		return false;
	}
	
	private void generateSquarePartners() {
		int[] point1Shift1 = Arrays.copyOf(point1, 2);
		int[] point1Shift2 = Arrays.copyOf(point1, 2);
		int[] point2Shift1 = Arrays.copyOf(point2, 2);
		int[] point2Shift2 = Arrays.copyOf(point2, 2);
		int index;
		
		if (point1[0] == point2[0]) {
			index = 0;
		} else {
			index = 1;
		}
		
		if (point1[index] - 1 > 0) {
			point1Shift1[index] = point1[index] - 1;
			point2Shift1[index] = point2[index] - 1;
			
			square1Partners.add(new Line(point1, point1Shift1));
			square1Partners.add(new Line(point1Shift1, point2Shift1));
			square1Partners.add(new Line(point2Shift1, point2));
		}
		if (point1[index] + 1 < boardSize) {
			point1Shift2[index] = point1[index] + 1;
			point2Shift2[index] = point2[index] + 1;
			
			square2Partners.add(new Line(point1, point1Shift2));
			square2Partners.add(new Line(point1Shift2, point2Shift2));
			square2Partners.add(new Line(point2Shift2, point2));
		}
	}
}
