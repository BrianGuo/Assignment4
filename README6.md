Overview:

What we need to do:
  Display the state of the world: Essentially visually display hexagons, rocks, critters, food.
  
  Also # of critters and timesteps advanced

    * Current equivalent is the info function which means all this information is stored in simulator.
    * Just need graphical representation, do not use "-" but hexagons.
    
  Step 1 time per click, or step infinitely many times until all critters die:
  
    * Simulator equivalent is the step function.
    * Allow user to select the number of frames/second
    
  Display total Steps and critters alive
  
  Worlds
  
    * Create a new random world (newWorld() function in simulator)
    * Load a world: Display user hardDrive and allow user to select a file
  
  Critters
  
    * Load a critter
    * Place a critter onto the world
      - Toggle placeMode and click multiple times
      - Select a number to place randomly. Write as numberfield.
  
  Click a hex and view info in info panel. 
    
  
Schedule
  
  * 1/3 waypoint: WorldDisplay, StatusPanel, InfoPanel,
  * 2/3 waypoint: ActionsPanel + all functionality
      - 2.1 : Add Critters functionality
      - 2.2 : Load World functionality
      - 2.3 : Step world functionality
  * 3/3 waypoint(Finishing touches): Spillover cushion of two days. If not, testing and written problem lookover
  
