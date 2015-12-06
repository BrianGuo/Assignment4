package gui;

import world.Coordinate;
import world.Critter;

public class NewCritterPositions {
	
	private Critter critter;
	private Coordinate[] positions;
	private int num = 0;
	
	public NewCritterPositions(Critter c) {
		this.critter = c;
	}
	
	public void setPositions(Coordinate[] positions){
		this.positions = positions;
	}
	
	public void setNum(int num) {
		this.num = num;
	}
	
	public Critter getCritter() {
		return critter;
	}
	
	public Coordinate[] getPositions() {
		return positions;
	}
	public int getNum() {
		return num;
	}
}