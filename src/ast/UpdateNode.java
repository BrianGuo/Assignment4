package ast;

public class UpdateNode implements Node {

	MemoryNode left;
	Expr right;
	int size;
	int leftsize;
	int rightsize;
	
	public UpdateNode(MemoryNode l, Expr r) {
		left = l;
		right = r;
		leftsize = l.size();
		rightsize = r.size();
		size = leftsize + rightsize;
	}
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public Node nodeAt(int index) {
		if (index == 0)
			return this;
		if (index >= size || index < 0)
			throw new IndexOutOfBoundsException();
		else if (index > leftsize){
			int n = index-(leftsize+1);
			return right.nodeAt(n);
		}
		else {
			return left.nodeAt(index-1);
		}
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		return sb.append(left.toString() + " := " + right.toString());
	}
	
	public String toString() {
		StringBuilder temp = new StringBuilder();
		return prettyPrint(temp).toString();
	}

}