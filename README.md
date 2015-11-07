# Assignment 5: Graphical User Interface
##Max Zhou (mz282), Brian Guo (bg379)

##Summary and design

####State of the world

* The map of the world will be displayed by drawing on a canvas.
* Hexagons will be manually drawn onto the world and centered and sized to fit the world.  No scrolling.
* Critters/rocks/etc will be manually drawn on top of the hexagon.
	* This shouldn't really be a problem--a separate method handles the drawing of each hexagon, so it should be trivial to draw another image in the same location.
* Users can click on the world in order to select an object to focus on.
* Critter count and steps can either be placed in the general information tab or in a small box in the corner of the world.

####User interface
 * Users interact with the world using the buttons on the right side.
 * For loading a critter, the user will first navigate to a file and select a critter file.
	 * A new tab will open in the "General information" panel that displays the attributes of the critter when loaded (as specified in the critter file)
	 * Users can then either push a button to add critters randomly, or another one to place them.
		 * The random critter button will also have some other control to allow the user to control how many to be placed.
		 * The place critter button is a toggle button, which then lets the user click on the map to place critters.
			 * Direction can perhaps be handled with a separate control or some sort of click-and-drag functionality.
 * Selecting a world will load in a world file and open its attributes in the "General information" panel as well.
	 * Another button erases the current world and replaces it with the new one.  Can probably be extended if needed to allow for multiple worlds in the same program.
 * The user can choose to either advance one step or toggle constant stepping.  Constant stepping stops when there are no critters left in the world, although this can probably be handled either way.  The user can select how often to step using either a slider or selector.
 * When the user clicks on a location in the world, detailed information about it pops up in the "detailed information" tab, which displays all the critter's attributes.  Idea is for it to be similar to inspecting a turtle in NetLogo.
 * We can also display the coordinates of the hex the user's pointer is currently over.

----
##Implementation
###Architecture
 * Because we are drawing hexes directly on the canvas, we will need to add event listeners to the canvas and catch clicks.  Then, we have to use math in order to turn the click's coordinates into a hex coordinate that the rest of the program can use.
 * It may be hard to check for exactly which hexes get modified during any given time step, although it could be possible to iterate over them and manually check.  Our current plan is to redraw the entire canvas every time step (as long as we don't exceed the maximum frame per second limit), but if that is too expensive, it could be optimized by only redrawing changed hexes.
 * In order to draw the world, the view can simply get the entire state of the world from the model.  The observer pattern may not be necessary, since updates to the world only occur at known times (when the user interacts with the world and during a time step).  When the user adds a critter, we can just redraw the entire world.
 * Ideally, we will use ObservableValues to propagate updates in the model to the view without manually registering observers (Meteor's reactive variables spoiled me).  However this may require refactoring the fields in the model (not sure if that is allowed), so it may be better to make the entire Critter object observable (or wrap it) for real-time updates.
	 * For example, in order to display the number of current critters, the simulator will have code to keep track of that number and then expose an ObservableValue (either method or field, not sure which) with the current value.
		 * Then the label in the world simply binds to that value.
			 * Possible problem with this is that string formatting will happen in simulator, not the world (unless there's an easy way to bind a Label's data to something and then format it...?)
	 * This design decision was mostly just due to familiarity with working with reactive variables over manually registering observers (and it makes cleaner code, as well).
 * Button events are passed on to dedicated methods in simulator that perform the necessary functions in the world.
###Programming
 * We are planning on a bottom-up approach.
 * Buttons and views are added incrementally to the program.  Because the backend is essentially already done, we can just add event handlers and data binding one section at a time, moving on to another one when the backend for one section is complete.
 * In general, Brian will work on displaying the world and its information (step number and number of critters).
 * Max will handle the buttons to interface with the simulation and the backend.

####Tentative schedule
  
  * 1/3 waypoint: WorldDisplay, StatusPanel, InfoPanel,
  * 2/3 waypoint: ActionsPanel + all functionality
      - 2.1 : Add Critters functionality
      - 2.2 : Load World functionality
      - 2.3 : Step world functionality
  * 3/3 waypoint(Finishing touches): Spillover cushion of two days. If not, testing and written problem lookover
  
  ---
##Testing
 * Testing is weird with a GUI.
	 * The underlying methods called by events, in the controller, can be unit tested.  Sending the actual click events probably cannot (writing a script to click in certain locations is impractical, fragile, and not very portable).
	 * Our console will still work as an alternative view, and we can use that as a known reference for what the display -should- be like in order to test that the world display works correctly.
	 * For edge cases, we will most likely unit test the backend to make sure it is returning the correct value, then manually test the frontend to make sure the current information is returned to the user, in case of an error, for example.
	 * And, of course, all the previous unit tests on the model should still pass regardless of any changes to this code, unless the model itself gets refactored (since those tests are fairly fragile, oops). 
