# Assignment5
###World
* In addition to what's in the class diagram, world should have methods to move, add, and remove objects.
* World also will contain all constants.  They could have gone in either Simulator or World, but we are putting most methods in World.
* World will be fairly "heavy": a lot of interpreter logic will be handled here.

###Simulator
 - Simulator holds the World and advances time steps, and not much else.

###Console
 - Console just interfaces with the Simulator.

###Critters and WorldObjects
 - WorldObjects are stored in the World.  WorldObjects do not know what World they are in, so anything that deals with a separate object (mate, ahead[], etc) requires a call to the World "supervisor" which has the necessary information.  This is also why a great deal of interpreter logic is at the world level.
 - Critters store the last rule executed for use with the console and have a helper variable for use with mating.

###Updates to AST:
 - Every `Expr` and `Condition` has an evaluate function; this allows for evaluation to be handled in a similar manner as pretty-printing (recursive calls).
 - Mutations must be updated to have 1/4 chance of them actually occurring.
 - Sensor and memory now evaluate to numbers
 - Fix errors in mutations

###Updates to parser:
 - Fix edge cases with `-(something)` and chained negatives (`--`).

###User interface
 * Fairly straightforward with regards to design.  Just a slog to write the methods.

###Written problems:
* Evenly split.  Probably should collaborate on the loop invariant problem.


###Testing. 
* Unit testing--first, bootstrap in some Critters and a World, and then run some constant tests (to check evaluation works correctly, to check interpreting + interpreting Outcomes works correctly, etc).
* Make sure that Actions with no Exprs actually don't have any.
* World testing mainly revolves around making sure object interaction works correctly (walking into a rock, eating food, **mating** especially).  Likely to be done with a specially-constructed set of rules.
* After proper mating and mutation code is in, we can test with a "seed stock" of rules, randomly create critters based off them, and then mate and ensure it works.
* In addition, ensure proper use of energy!!
* It may be possible to write a program that randomly generates ASTs to test `eval()`.
* Test ASCII printing with a single critter that does a known thing--the spiral critter would be very useful here.
* Finally, read in a bunch of random Critters and set them free!  And hope nothing crashes.

###Division of work:
1. Creation of the Interpreter as well as adjusting all of the AST nodes to adhere to evaluations                        (sensors, memory, etc), creating the outcome class. This also includes creation of the critter class.                 Critter class means fixing mutations and creating new mutations. Work Level approx: 5/10. Brian
2. Creation of world. Food class. Work level approx: 4/10. Everything                 mostly straightforward.  Max.
3. Creation of the simulator, loading from the simulator, advancing time steps. Console
                Work level approx: 4/10.  Brian.
4. Creation of Test cases and a standard test suite to run for all classes. Work level approx: 5-7/10.  Mostly Max.
5. Finishing written problems: Depending how hard they are, anywhere from 3-5/10. 
6. Anything that involves parsing a file: Max
