package ast;

import world.Coordinate;

public class HexQueueObject implements Comparable<HexQueueObject> {
	
	private int direction;
	private int distance;
	private Coordinate location;
	
	public HexQueueObject(int direction, int distance, Coordinate location ) {
		this.direction = direction;
		this.distance = distance;
		this.location = location;
	}
	public int getDirection(){
		return direction;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public Coordinate getLocation() {
		return location;
	}
	@Override
	public int compareTo(HexQueueObject o) {
		return distance - o.distance;
	}
}