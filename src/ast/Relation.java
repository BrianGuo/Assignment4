package ast;

public class Relation implements Condition {

	Expr left;
	Expr right;
	Relationship R;
	int leftsize;
	int rightsize;
	
	public Relation (Expr l, Expr r, Relationship rel) {
		left = l;
		right = r;
		R = rel;
		leftsize = l.size();
		rightsize = r.size();
	}
	@Override
	public int size() {
		return leftsize + rightsize + 1;
	}

	@Override
	public Node nodeAt(int index) {
		if (index == 0)
			return this;
		else if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		else if (index > leftsize)
			return right.nodeAt(index - (leftsize+1));
		else
			return left.nodeAt(index -1 );
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		String relation = "";
		switch (R) {
		case LESS:
			relation = "<";
			break;
		case GREATER:
			relation = ">";
			break;
		case EQUAL:
			relation = "=";
			break;
		case NOTEQUAL:
			relation = "!=";
			break;
		case GREATOREQUAL:
			relation = ">=";
			break;
		case LESSOREQUAL:
			relation = "<=";
			break;
		default:
			break;
		}
		return sb.append(left.toString() + " " + relation + " " + right.toString());
	}
	
	public enum Relationship {
		LESS,GREATER,EQUAL,NOTEQUAL,GREATOREQUAL,LESSOREQUAL;
	}
}