package gui;

import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;

import javafx.scene.shape.Shape;

public class hexagon extends Shape {

	@Override
	public com.sun.javafx.geom.Shape impl_configShape() {
		
	}

	class hexagon2 extends com.sun.javafx.geom.Shape{
	
		float centerPoint;
		float Radium;
		
		@Override
		public RectBounds getBounds() {
			return new RectBounds(-0.5f, -0.5f ,0.5f,0.5f);
		}

		@Override
		public boolean contains(float x, float y) {
			if (Math.abs(x) > 0.25f){
				return (Math.abs(y) < (-4.0f * x) + 2);
			}
			return true;
		}

		@Override
		public boolean intersects(float x, float y, float w, float h) {
			if (contains(x,y))
				return true;
			else if (y > 0.5f)
				return (contains(x,y-h) || contains(x+w,y-h) || contains(x+w/2f,y-h));
			else if (x > 0.)
		}

		@Override
		public boolean contains(float x, float y, float w, float h) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public PathIterator getPathIterator(BaseTransform tx) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PathIterator getPathIterator(BaseTransform tx, float flatness) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public com.sun.javafx.geom.Shape copy() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	
	
	
}