# Assignment5


1. The Interpreter

    In order to interpret the AST more quickly with fewer lines of code, 
    I will add an evaluate() method to each Expr, ( int evaluate() )
    and an evaluate method to each condition. (boolean evaluate())
    
    Interpreter.java is a file given to us in a5-release. 
    It has boolean eval(Condition c) which I think should just return c.evaluate
    same with int eval(Expr e)
    
    /*By the way, just something of note. I don't know whether you want to make a Critter.java which has a program and all of its memory pieces or you just want to store the memory data as an array inside program. Something to consider. Because we do need to store the values of memory somewhere.*/
    Edit: you need your own critter class. Or you should have one. It should store its own location. It should have a program. It should have an array. It does NOT need to have an interpreter inside it.
    
    So interpreter will pass Outcome over to simulator by interpreting a Critter's program.
    Outcome, as of right now, is an interface containing absolutely nothing.
    So we shuold invent the methods to put in there.
    Outcome should definitely have a slot for an action,
    Quick note: A critter should store the world it is in so that sensors can be evaluated. Ahead[5] could not return
    anything unless the critter knows what world it is in. This might mean that Sensor nodes themselves need to store the world that the critter is in. The idea here is to have every Eval function pass the world to its children, so that the sensor nodes at the very base will have the world. Confirmed with TAs. Also, theoretically, it should store a critter?? Maybe? because it needs to pass the critter. 
    Edit: Interpreter stores the critter and condition evaluates using the critter and the world as parameters. 
    Outcome should return an action. The action should be one of a set of enums, and if the action is either tag or serve, read from the optional expression. /*If it is "updated" all it means is that no actual action was taken and the simulator should advance to the next critter.*/ Edit: a program that only updates will return wait. 
    

2. Simulator
    Simulator will definitely store a world
    I don't know if the simulator should store the critters or the world should store the critters. I think it should be the latter. Correction: it should be the latter. World files can spawn critters so it makes sense that they should be stored after spawned.
    Simulator MUST have method "advanceTime()." This should go to every critter and update it.
    Simulator should have an interpreter. Interpreters should be passed critters and evaluate them to an outcome. 

3. World
    World file might store everything in a 2-D array / 2D arraylist of objects (subject to change. By all means offer better suggestions). should definitely be able to adjust critter position( method 1) remove objects( method 2) place objects (method 3) check objects (method 4)

4. updates to the previous ast package
  1. every expr and condition has an evaluate function eval (condition, World) or (expr, world)
  2. mutations need to be able to include the possibility of attribute increase/reduction and a 3/4 chance of failing along with a 3/4 chance of failing every successive time.
  3. sensors now give stuff, memory nodes now give stuff. Remember. 
  4. I will finish my incorrect mutations.

5. User interface
    Fairly straightforward. I don't know of anything in there that would be hard to implement.

6. Written problems:
    If the division of work is fair and right this time, I think the written problems should be split much more easily.


6. Testing. 
    Can you handle testing? create tests ahead of time. Make sure they're good. He writes what we have to test for.

7. Division of work:
        To reiterate, the tasks:
            1. Creation of the Interpreter as well as adjusting all of the AST nodes to adhere to evaluations                        (sensors, memory, etc), creating the outcome class. This also includes creation of the critter class.                 Critter class means fixing mutations and creating new mutaitons. Work Level approx: 7/10
            
            2. Creation of world and all of its methods, rock class, Food class. Work level approx: 4/10. Everything                 mostly straightforward.
            3. Creation of the simulator, loading from the simulator, advancing time steps. Work level approx: 4/10
            4. Creation of Test cases and a standard test suite to run for all classes. Work level approx: 5-7/10
            5. Finishing written problems: Depending how hard they are, anywhere from 3-5/10. There are only four                    problems and two of them are questions about asymtotic complexity.
            6. Other: List here. Current Difficulty: 0/10
    
    I propose separating work in this manner: I will create the interpreter as well as adjust the AST Nodes (Brian). Will create the critter class and the outcomes.
    Creation of the world and its methods will be left to Max.
    Creation of the Simulator will be handled by Brian
    Creation of Test cases will be handled by Max.
    Written problems will be split by the two of us. NOTE: WE MUST DISCUSS THE ONE CONCERNING LOOP INVARIANTS BEFORE WE SUBMIT. #3.
    If you agree to this, I will write down all the methods you will need from me and all of the methods I will be calling to the world from the simulator. 
    As of right now, I see no problem with the interpreter evaluating its own outcomes btw and making the critter act accordingly.
