package ast;

import java.util.ArrayList;
import java.util.Random;

import exceptions.IllegalCoordinateException;
import world.Coordinate;
import world.Critter;
import world.Entity;
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

	public static Coordinate coordAheadAt(Critter c, World w, int distance) throws IllegalCoordinateException{
		Coordinate newCoordinates = null;
		Coordinate coordinates = c.getLocation();
			switch (c.getDirection()){
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

	public int evaluateAhead(Critter c, World w, int distance){
		try {
			Coordinate newCoordinate = coordAheadAt(c, w, distance);
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
		if (r == null)
			return 0;
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