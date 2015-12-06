package world;

import java.util.ArrayList;

public class Nothing extends Entity {

	public Nothing(int c, int r){
		location = new Coordinate(c,r);
	}
	@Override
	public String toString() {
		return "";
	}

	@Override
	public int appearance() {
		return -1;
	}

	@Override
	public ArrayList<String> properties() {
		return null;
	}
	
}