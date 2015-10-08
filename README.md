# Asssignment 4: Parsing and Fault Injection
##Max Zhou (mz282), Brian Guo (bg379)
****
###Abstract Syntax Tree
* contains a `Program` Node as the root.
* The AST will contain a `Program` Node as the root node. 
*  The `Program` Node stores an ArrayList of `RuleNodes`.
*  `RuleNode` represents the relationship between a condition and a command. 
	 * Contains a `ConditionNode` and a `CommandNode`.
*  `ConditionNode` represents a condition (either `True` or `False`). 
	* Implemented by `BinaryCondition` and `Relation`
		* A `BinaryCondition` holds 2 `ConditionNodes` as fields and links them with an operator of `&&` or `||`. Since `BinaryOp` is a condition, a `BinaryCondition` can store more `BinaryConditions` in order to store an arbitrary number of `BinaryConditions` (...`||`...`&&`...).
		* A `Relation` holds two expressions and links them with a relational operator (`<`, `>`, `==`, etc.).  
	* If `Condition` is True, it permits the `Rule` to evaluate and perform the `CommandNode`.
*  `CommandNode` holds a list of updates and an action, initially set to null. 
* `Update` is a Node that symbolizes the relationship `mem[expr] := expr`", and thus holds two nodes: a `MemoryNode` and an `Expression` Node. 
* `ActionNode` of a `CommandNode` is a `Node` that symbolizes one unique action (`wait`, `forward`, `backward`, etc.) and holds a String representing which `Action` it is, as well as a possible `Expression`, originally set to null, since some actions may contain expressions.
*  `Expression` is a sub-interface of Node which contains two classes: `NumberNode` and `BinaryOperation`. 
	* `NumberNode` is a Node representing one number. 
	* `BinaryOperation` is a node which contains two expressions and one operation (+,-,/,*) which it applies to them. Therefore, an expression will ALWAYS evaluate to a number, if the inputs are correct. 
 
***
###Parsing
While there is skeleton code for a recursive descent parser, which is fairly straightforward, a bottom-up parser may be simpler, once a parse table has been generated.  However, it may be too laborious to generate a parse table by hand for this grammar without any automated tools, so may not be such a good idea.
***
### Mutations
Mutation may be one of the more difficult processes to implement. Mutations themselves are described in detail in the project specification and are fairly straightforward. Deciding whether or not to mutate is simply a matter of calling Math.random(), as is deciding whether to mutate an attribute or a rule set. The most difficult task may be giving each node an equal probability to be mutated. I would like to find some way to do it without traversing through the tree and counting each individual Node, but I do not, as of yet, see a way to. Considering that there may be up to 999 rules, though, I imagine that counting a rule and its sub-Nodes may be rather slow. Still, perhaps it is the fact that the number of rules is arbitrary that makes it difficult to find a method that gives each node an equal opportunity, without counting each Node.

Perhaps we could store all Nodes in an ArrayList of some kind as well as in the standard tree structure.  This would make mutations in general easier, particularly finding a random Node to be mutated, but would make the removal and addition mutations slightly harder, due to having to remove every Node.
***
### Dividing work

Work will be split primarily through packages.  The entire AST package will be handled by Brian, while Parsing will be handled by Max.  By "the entire package," we mean that if a method needs to be implemented in a certain package, it will be implemented by that respective person. The main method, since it involves  parsing the input into tokens, will be handled by Max. The written problems will be coordinated by splitting the work and then checking and discussing the responses of the other person. This method of division of work will involve some gray-areas, which is something we are both aware of. For instance, parserImpl.java will need to call the appropriate constructors when using a top-down approach, which is something that will be discussed between the two of us in the midst of our project.  Since this is a small internal project, collaboration on the repo will be handled with branching.  This will also allow us with more flexibility in crossing over in work if necessary.
***
### Fault injector
The implementation of the fault injector will provide us with many sample critters that we can test to make sure our Pretty-Printer and Parser, at the very least, compiles. To test that it produces the right output, we believe Pretty-Printer and Parser can test each other. If we can keep track of what mutaton was made to which Node in each test, we can check where the process fails. The testing should involve mutating a critter, pretty-printing the output, parsing the print, and checking the resulting generated tree to see if it matches the mutated tree prior to pretty-printing. If they do not, we can check the pretty-printed document to see if the process failed in the pretty-printing or in the parsing.
***
###Pretty printing
One possible approach is to use the visitor pattern, which for a small upfront amount of work, should mostly trivialize pretty printing as it will make it independent of the code for each Node.  We could also use toString() helper functions or just recursively prettyPrint() everything instead.
