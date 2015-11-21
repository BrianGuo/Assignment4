package ast;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import exceptions.IllegalCoordinateException;
import world.Coordinate;
import world.Critter;
import world.Entity;
import world.Food;
import parse.Token;
import parse.TokenType;
import world.World;

/**
 * A representation of a sensor--nearby, ahead, random, and smell.
 * {@code sense}: Represents the type of sensor.
 * {@code r}: For use with nearby, ahead, and random.  Meaningless for smell.
 */
public class Sensor extends UnaryNode implements Expr {

	private Token sense;
	private Expr r;
	//private int size;
	
	public Sensor(Token s){
		this.sense= s;
		r = null;
		//size = 1;
	}
	
	public Sensor (Sensor sensor){
		sense = sensor.getSense();
		//size = 1;
		if(sensor.getExpr()!= null){
			r = sensor.getExpr();
			//size += r.size();
		}
	}
	public Token getSense(){
		return sense;
	}
	public Expr getExpr() {
		return r;
	}
	public Sensor(Token s, Expr r){
		if(s.isSensor()){
			this.sense = s;
			this.r = r;
		}
		//size = r.size() + 1;
	}
	
	@Override
	public int size() {
		int size = 1;
		if (r!= null)
			size += r.size();
		return size;
	}

	@Override
	public Node nodeAt(int index) {
		if (index ==0)
			return this;
		else if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		else{
			return r.nodeAt(index -1);
		}
		
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(sense.toString());
		if (r != null)
			sb.append("[" + r.toString() + "]");
		return sb;
	}

	
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		return prettyPrint(sb).toString();
	}
	
	

	@Override
	public ArrayList<Node> children() {
		ArrayList<Node> temp = new ArrayList<Node>();
		if(r != null)
			temp.add(r);
		return temp;
	}
	
	@Override
	public boolean hasChild() {
		return (sense.getType().equals(TokenType.NEARBY) || sense.getType().equals(TokenType.AHEAD) || sense.getType().equals(TokenType.RANDOM));
	}
	
	@Override
	public void setChild(Node n) {
		if (n instanceof Expr){
			r = (Expr) n;
			//size = 1 + n.size();
		}
	}

	@Override
	public Node getChild(){
		return r;
	}

	


	public void setToken(Token t) {
		if (t.isSensor())
			sense = t;
	}

	public static Coordinate coordAheadAt(Coordinate coordinates, int direction, World w, int distance) throws IllegalCoordinateException{
		Coordinate newCoordinates = null;
			switch (direction){
				case 0:
					newCoordinates = new Coordinate(coordinates.getCol(),coordinates.getRow()+1*distance);
					break;
				case 1:
					newCoordinates = new Coordinate(coordinates.getCol()+1*distance,coordinates.getRow()+1*distance);
					break;
				case 2:
					newCoordinates = new Coordinate(coordinates.getCol()+1*distance,coordinates.getRow());
					break;
				case 3:
					newCoordinates = new Coordinate(coordinates.getCol(),coordinates.getRow()-1*distance);
					break;
				case 4:
					newCoordinates = new Coordinate(coordinates.getCol()-1*distance,coordinates.getRow()-1*distance);
					break;
				case 5:
					newCoordinates = new Coordinate(coordinates.getCol()-1*distance,coordinates.getRow());
					break;
				default:
					break;
			}

		return newCoordinates;

	}
	public int evaluateSmell(Critter c, World w) {
		PriorityQueue<HexQueueObject> p = new PriorityQueue<HexQueueObject>();
		Coordinate ahead = coordAheadAt(c.getLocation(), c.getDirection(), w, 1);
		if (w.hexAt(ahead) instanceof Food){
			return 0;
		}
		else {
			if(w.hexAt(ahead) == null)
				p.add(new HexQueueObject(c.getDirection(), 1, ahead ));
			p.add(new HexQueueObject(c.getDirection()+1, 1, c.getLocation()));
			p.add(new HexQueueObject(c.getDirection()-1, 1, c.getLocation()));
			HexQueueObject food =  evaluateQueue(p, w);
			if (food == null)
				return 1000000;
			Coordinate foodLocation = food.getLocation();
			double deltaY = foodLocation.getRow() - c.getRow();
			double deltaX = foodLocation.getCol() - c.getCol();
			System.out.println("This is delta Y: " + deltaY);
			System.out.println("This is delta X: " + deltaX);
			int trueDirection = 0;
			if (deltaX == 0) {
				if (deltaY < 0) {
					trueDirection = 3;
				}
			}
			else if (deltaX > 0) {
				double slope = deltaY/deltaX;
				if (slope > 1/2){
					if (slope < 2)
						trueDirection = 1;
				}
				else{
					if (slope > 0)
						trueDirection = 2;
					else
						trueDirection = 3;
				}
			}
			else{
				double slope = deltaY/deltaX;
				if (slope > 1/2) {
					if (slope > 1)
						trueDirection = 3;
					else
						trueDirection = 4;
				}
				else{
					if (slope > 0) {
						trueDirection = 5;
					}
					else
						trueDirection = 0;
				}
			}
			int relativeDirection = (trueDirection - c.getDirection())%6;
			System.out.println(trueDirection);
			System.out.println(c.getDirection());
			if (relativeDirection <0)
				relativeDirection += 6;
			return 1000*food.getDistance() + relativeDirection;
		}
	}
	
	public HexQueueObject evaluateQueue(PriorityQueue<HexQueueObject> p, World w) {
		HexQueueObject h = p.remove();
		try{
			Coordinate ahead = coordAheadAt(h.getLocation(), h.getDirection(), w, 1);
			if (w.hexAt(ahead) instanceof Food){
				return h;
			}
			if (h.getDistance() > 10)
				return null;
			else{
				int direction = h.getDirection();
				int distance = h.getDistance() + 1;
				if (w.hexAt(ahead) == null)
					p.add(new HexQueueObject(direction, distance, ahead));
				p.add(new HexQueueObject((direction-1)%6, distance, h.getLocation()));
				p.add(new HexQueueObject((direction+1)%6, distance, h.getLocation()));
				return evaluateQueue(p,w);
			}
		}
		catch(IllegalCoordinateException e) {
			return evaluateQueue(p,w);
		}
	}
	public int evaluateAhead(Critter c, World w, int distance){
		try {
			Coordinate newCoordinate = coordAheadAt(c.getLocation(), c.getDirection(), w, distance);
			if (!w.inBounds(newCoordinate)) {
				return w.constants.ROCK_VALUE;
			}
			if (w.hexAt(newCoordinate) == null)
				return 0;
			else {
				return w.hexAt(newCoordinate).appearance();
			}
		}
		catch(IllegalCoordinateException e){
			return w.constants.ROCK_VALUE;
		}
	}
	
	public int evaluateRandom(Critter c, World w) {
		Random r = new Random();
		int endpoint = this.r.evaluate(c, w);
		return r.nextInt(Math.abs(endpoint));
	}
	
	public int evaluateNearby(Critter c, World w){
		int direction = r.evaluate(c, w);
		if (direction > 5 || direction < 0) {
			direction = Math.abs(direction%6);
		}
		int originalDirection = c.getDirection();
		c.setDirection(direction);
		int result = evaluateAhead(c,w,1);
		c.setDirection(originalDirection);
		return result;
	}

	@Override
	public int evaluate(Critter c, World w) {
		if (sense.getType() == TokenType.SMELL)
			return evaluateSmell(c,w);
		else if (sense.getType() == TokenType.AHEAD) {
			int coefficient = r.evaluate(c, w);
			return evaluateAhead(c,w,coefficient);
		}
		else if (sense.getType() == TokenType.NEARBY){
			return evaluateNearby(c,w);
		}
		else {
			assert(sense.getType() == TokenType.RANDOM);
			return evaluateRandom(c,w);
		}
	}
}