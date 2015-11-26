package ast;

public interface ParentSpecificMutation extends Mutation {
	
	/**
     * Mutates the given node
     * @param the node to be mutated
     * @return null if unsuccessful, Node as replacement in certain instances of mutate.
     * New Node if replacement unneeded
     */
    Node mutate(Node n);
}