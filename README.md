# Asssignment 4: Parsing and Fault Injection
##Max Zhou (mz282), Brian Guo (bg379)
#### Main method is found in src/main/Driver.java
****
##Summary
*	Parser was more challenging than expected, although that was partly because I decided to directly parse conditionals and expressions with the shunting-yard algorithm rather than just following the grammar.  This resulted in...a mess, but the two identical parsers for conditionals and expressions can be easily refactored now that both conditionals and expressions are subtypes of a BinaryChild class (only two children).
*	Our abstract syntax tree may have followed the grammar too closely, but it did make parsing quite easy and did not inconvenience mutations too much.
*	Mutations were definitely the hardest part, due to the huge number of cases to deal with.  An especially annoying aspect was the need to deep clone any copied objects.
	*	`nodeAt` and `getParent` produced many `ArrayIndexOutofBoundsException`s during testing; the code relied on many nested loops and was hard to trace.
	*	As a quick hack, we also made `TokenType` and `Token` public because their enums were quite useful in storing the type of operation for those nodes that required them in the AST.

***
###Abstract Syntax Tree
* contains a `Program` Node as the root.
*  `Program` stores an ArrayList of `Rule`s.
*  `RuleNode` represents the relationship between a condition and a command. 
	 * Contains a `Condition` and a `Command`.
*  `ConditionNode` represents a condition (either `True` or `False`). 
	* Implemented by `BinaryCondition` and `Relation`
		* A `BinaryCondition` holds 2 `Condition`s as fields and links them with  `&&` or `||`. Since `BinaryOp` is a condition, a `BinaryCondition` can store more `BinaryConditions` in order to store an arbitrary number of `BinaryConditions` (...`||`...`&&`...).
		* A `Relation` holds two expressions and links them with a relational operator (`<`, `>`, `==`, etc.).  
	* If `Condition` is True, it permits the `Rule` to evaluate and perform the `CommandNode`.
*  `CommandNode` holds a list of updates and an action, initially set to null. 
* `Update` is a Node that symbolizes the relationship `mem[expr] := expr`", and thus holds two nodes: a `MemoryNode` and an `Expression` Node. 
* `ActionNode` of a `CommandNode` is a `Node` that symbolizes one unique action (`wait`, `forward`, `backward`, etc.) and holds a String representing which `Action` it is, as well as a possible `Expression`, originally set to null, since some actions may contain expressions.
*  `Expr` is a sub-interface of Node which contains two classes: `NumberNode` and `BinaryOperation`. 
	* `NumberNode` is a Node that represents a single integer.
	* `BinaryOp` is a Node which contains two expressions and one operation (+,-,/,*,mod).
* `UnaryNode` is a Node that represents any unary operator, such as negation and memory access.
	* `NegationNode` represents the negation of a single expression. 
 
***
###Parsing
Standard recursive descent parser.

From inspection, it appears every operation is left-associative (except unary negation), and "and" has precedence over "or."  Conditionals and expressions are handled using the shunting yard algorithm, which means there is no need for separate parse methods for conjunction/relation and factor/term.  Unary negation can be handled in parseFactor() transparently.
This probably wasn't the best idea, because directly following the grammar rules would account for precedence and parentheses anyway, but it was a fun exercise and a relief from coding all the recursive calls, even if it took far longer than necessary.
####Shunting yard algorithm
Because I misread the grammar when first building the parser, I did not see the fact that *relation* could also map to *{condition}*.  Thus, after much googling, I came up with a stack-based algorithm that seemed like it would work for parsing conditionals (and also expressions) with operator precedence.
On further inspection, it turns out it was just a very crude version of the shunting-yard algorithm, so I switched to using the actual algorithm instead.  The basic logic is as follows:

 1. Read in an expression from the input stream and push onto the literals stack
 2. Read in an operator from the input stream and push onto the operators stack
 3. 
	* If the operator we just read has a higher precedence than the previous operator at the top of the stack, or if they have equal precedence and are left-associative, pop off 2 expressions from the literals stack and form a subtree using those and the operator.  Push the subtree back onto the literals stack.  Continue reducing if possible.
	* Otherwise, just keep the operator on the stack.
 4. To deal with parentheses, simply recursively call the algorithm on the expression inside the parentheses.
 5. Continue reading until out of valid tokens; then, reduce and clear the stack.

***
### Mutations
Mutations require a great number of helper methods.
We split the Nodes into three general categories: 

 * those with a variable number of children (list-like, extend `ListChildren`)
 * those with two children (tree-like, extend `BinaryChildren`)
 * those with one child (unary, extend `UnaryNode`)
 
With the use of Java exceptions and the use of a list of all of a Node's children, it may have been possible to simplify the code further and cut down on `instanceof` calls.  We did not do this, however.

Nodes are given number indices in the order they would be visited by a pre-order traversal, starting with 0 for the Program itself.  The `nodeAt` helper method returns the Node at a given index.  This allows us to find the parent of a given node without needing parent pointers, by traversing the syntax tree.  For nodes with a variable number of children, the traversal walks across the entire list of children in order.
####Miscellanea regarding mutations

*	A mutation is considered to have succeeded if it actually had an effect
*	Remove works on any node that is a child of a list-like node, any unary node with a child, and binaryOp. (or put another way: any binary node or any unary node with a child). Does not work on relations, although it probably should.
*	Swap works on any list-like node with two or more elements and any binary node, otherwise a different mutation should be chosen.
*	Duplicate works on any list-like node with at least one child.
*	Insert is messy, but should work on any unary node, expressions, and conditions.
*	Transform works on numbers, unary nodes, arithmetic expressions, and any node that implements `Tokenable`.
*	Copy works on any node except the root.

***
### Dividing work

Work will be split primarily through packages.  The entire AST package will be handled by Brian, while parsing, the main method, system testing, documentation, and miscellanea will be handled by Max.  By "the entire package," we mean that if a method needs to be implemented in a certain package, it will be implemented by that respective person. The main method, since it involves  parsing the input into tokens, will be handled by Max. The written problems will be coordinated by splitting the work and then checking and discussing the responses of the other person. This method of division of work will involve some gray-areas, which is something we are both aware of. For instance, parserImpl.java will need to call the appropriate constructors when using a top-down approach, which is something that will be discussed between the two of us in the midst of our project.  Since this is a small internal project, collaboration on the repo will be handled with branching.  This will also allow us with more flexibility in crossing over in work if necessary.
***
### Testing
The implementation of the fault injector will provide us with many sample critters that we can test to make sure our Pretty-Printer and Parser, at the very least, compiles. To test that it produces the right output, we believe Pretty-Printer and Parser can test each other. If we can keep track of what mutaton was made to which Node in each test, we can check where the process fails. The testing should involve mutating a critter, pretty-printing the output, parsing the print, and checking the resulting generated tree to see if it matches the mutated tree prior to pretty-printing. If they do not, we can check the pretty-printed document to see if the process failed in the pretty-printing or in the parsing.

Our general testing procedure is to read in a known valid program (such as from the given examples), and then run a battery of mutations on it--either a set number, or running one mutation per node.  The mutations may be randomly chosen or specified.  After each mutation, we pretty print the program and parse the printed result back in.  If the mutation is correct, it should both not throw an exception AND the parsed program's pretty print should be identical to the one it was constructed from.  This does leave the problem of leaving out any issues with the printing method itself (which would not be detected since the error is constant), or any silent failures.  To deal with that, a cursory manual inspection can reveal the problems (such as certain nodes seemingly not being affected by mutations). 
***
###Pretty printing
We opted to go for recursive  operations for pretty printing.  A node's `prettyPrint()` method formats itself and builds a string by recursively calling `prettyPrint()` on its children and concatenating them with a `StringBuilder`.  `toString()` simply calls `prettyPrint()`.  To deal with possible precedence issues during printing, grouping operators (parentheses and braces) are added to every expression that could require them, even if it may be redundant.
For example, `1 + 2 + 3` would be printed out as `((1 + 2) + 3)`.  This ensures that the parsed pretty-printed syntax tree will be identical to the original one.
***
###Miscellaneous
 - Each Node has a children() method that returns an ArrayList of all of its children.  Mutation methods can use this to provide a general interface for both list-like and tree-like nodes, and do other things (like check if the Node actually has children).
 - Currently, swap mutations only swap the first 2 children.
***
###Known problems
 - The remove mutation does not appear to fully work--certain nodes don't seem to be affected, when they should.
***
###Comments
 - Time was roughly evenly divided between coding and testing (debugging is particularly annoying when the problem is in another package's code!).  Spending time on designing an abstract syntax tree was definitely a great help.
 - Mutations were harder than expected.  Parsing was also harder than expected.  We had to change our spec a few times, but always in minor ways (such as forgetting a certain class of Node in the original design).
