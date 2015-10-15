package ast;

/**
 * A factory that produces the public static Mutation objects corresponding to each
 * mutation
 */
public class MutationFactory {
    public static Mutation getRemove() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public static Mutation getSwap() {
    	return new SwapMutation();
    }

    public static Mutation getReplace() {
        return new CopyMutation();
    }

    public static Mutation getTransform() {
        return new TransformMutation();
    }

    public static Mutation getInsert() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public static Mutation getDuplicate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }
}
