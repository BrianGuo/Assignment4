Smell Algorithm: - Brian Guo

The smell algorithm will be implemented using a version of Djikstra's algorithm. The first move will check if the hex ahead has
food, and if not, it will add the forward, right, and left hexes. The queue takes a new queue item that contains
2 variables: the hex and the distance.
The distance would logically be the sum of all the turns and forward movements. 
The at each step, a hex (specifically, the hex ahead of the critter)will be examined. 
If the hex contains food, the distance of that hex is already contained within the 
queue item. The direction is calculated by taking the critter's direction and location (remember that sensor nodes are passed
a critter and the world), and using an inverse sin function to calculate a degree of rotation, and subsequently using that 
degree of rotation and dividiing it by 60 to determine how many rotations one must make to be in the right direction.
If the hex does not contain food, the algorithm will add to the queue the nodes immediately ahead after a forward step,
a left turn, and a right turn. This process is repeated until the nearest food is found

Ring Buffer - Brian Guo

Our ring buffer will follow the project definition of a ring buffer
add will add to the end of the array and increment the tail index
contains will check between head and tail indices to see if the item is in the array
etc.. The rest of the collections methods should be easy to implement. isempty checks if head and tail are the same index.
Iterator creates a new iterator instance, size will return the number of items between head and tail
Some of queues methods intersect with Collection, but otherwise, peek and offer are add and remove wihtout the incrementation.

Testing Ring buffer
Our testing will involve running methods with threads concurrently connecting to the ring buffer. We will take the suggestion
of using random delays in order to attempt to crash our program and find out where we need to fix our methods.
