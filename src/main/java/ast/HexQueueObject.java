package ast;

import world.Coordinate;

public class HexQueueObject implements Comparable<HexQueueObject> {
	
	private int direction;
	private int distance;
	private Coordinate location;
	private int initialDirection;
	
	public HexQueueObject(int direction, int distance, Coordinate location, int initialDirection ) {
		this.direction = direction;
		this.distance = distance;
		this.location = location;
		this.initialDirection = initialDirection;
	}
	public int getDirection(){
		return direction;
	}
	public int getInitialDirection() {
		return initialDirection;
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