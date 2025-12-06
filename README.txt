=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: 19054912
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays - This concept is essential to my game as it helps store the Tile objects that make up each level world.
  A 2D array is ideal for this data as the tiles naturally fall into an x,y grid pattern. The array's unique row-column
  structure also enables Player objects to access the Tiles around their specific x,y coordinates, instead of having to
  loop through every tile in the world, significantly speeding up collision computation. The array is the only container
  of level data, so level information is not redundant, and it is fully encapsulated to the World class.
  Finally, the Tile type was chosen as it contains all variables and methods required to implement graphics and
  collisions for each tile in a level.

  2. Collections - Collections, specifically a HashSet are used in my game to store the current PowerUps each player
  has. A collection of this nature is necessary as Player objects need to be able to keep track of what PowerUps they
  are currently "holding" so that they can trigger the appropriate logic continuously until that PowerUp expires. This
  is accomplished through looping through the HashSet with an Iterator every game tick.

  A HashSet was chosen to store the PowerUps as the order of the PowerUps in the collection do not matter, and no
  duplicate PowerUps are permitted (a player can only have one of each type of PowerUp at a time).

  Each player only stores one HashSet for their PowerUps so no information is stored redundantly, and the PowerUps
  HashSet can only be accessed through specific methods, so its state is encapsulated.

  3. File I/O - Players can store the current level and game state at any time by pressing a save button in a level.
  This uses file writing to store all relevant game state in a custom save file, which is then read when a player wants
  to load their previous game state. This includes, but is not limited to, current level, player positions,
  time elapsed, PowerUps obtained, etc. In case the file does not exist, an IOException is handled appropriately by
  notifying the user that a save file does not exist and continuing the game operations as normal.

  4. Inheritance and Subtyping - This concept is most prominently used in the Tile class and its subclasses to share
  a base framework on which specific tiles can add functionality to. Specifically, I used dynamic subtyping by building
  subclasses that override .collidesWith() and .onCollide() functions (ex. Spike, DashTile, Flag), allowing those tiles
  to implement their own custom collision logic and collision side-effects while still maintaining base features like
  displaying pixel art and defining a region of the level. Dynamic dispatch is then used to route the collision-check
  logic of each player to the appropriate Tile type for which they've collided with.

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  From top --> down in the file structure:

  LevelsScreen.java:
    This class is an extension of JPanel that defines the user interface for the Level Select screen. It incorporates
    text and buttons that the user can click on with the help of MouseListeners. Objects of this class are created and
    displayed to the screen from ScreenManager.java

  TitleScreen.java:
    This class is an extension of JPanel that defines the user interface for the Title Select screen. It incorporates
    text and buttons that the user can click on with the help of MouseListeners. It also includes a a button
    for an "Instructions" pop up that teaches the player how to play the game. Objects of this class are created and
    displayed to the screen from ScreenManager.java

  BackgroundPanel.java:
    This class is an extension of JPanel that defines the background image for all screens. It simply loads an image
    and sits at the back of the panel Z-stack. Objects of this class are created and displayed from ScreenManager.java

  StatusPanel.java:
    This class is an extension of JPanel that simply adds a status notifier to the bottom of the game window. A
    function of the class allows the status label to updated. Objects of this class are created and displayed from
    RunTether.java

  UIButton.java:
    This class is an extension of JPanel that defines a custom expanding button. It loads button images and contains
    functions to resize the button based on whether a user is hovering over it. Objects of this class are created and
    displayed from UIView.java

  UICard.java:
    This class is an extension of JPanel that defines a UI Card (a rectangular container) that holds text and
    UIButtons. It appears when a player wins or loses a level. Objects of this class are created and displayed from
    UIView.java

  UIView.java:
    This class is an extension of JPanel that handles all UI operations for the game while it is running. An object of
    this class is created from ScreenManager.java when a new level starts and the class' timer keeps the UI reactive
    throughout gameplay.

  Collision.java:
    This class is a custom data holder for all information about a particular Collision event between a Player object
    and a Tile object. It is returned as the result of a .collidesWith() function in any Tile object.

  PhysicsObject.java:
    This abstract class defines the primary functions for implementing physics (motion, gravity, friction, etc.) for
    any object in the game. It stores important information like position, velocity, and net force. Its most important
    functions are .impulse() which applies force to the PhysicsObject and .update() which computes a new position for
    the PhysicsObject. This class is extended by Player.java, which implements the abstract .draw() method.

  Sprites.java:
    This static class is the primary data loader for all Art Sprites used in the game. It loads all sprites into
    memory and holds them in static variables for access by any object throughout the game.

  SpriteSheetLoader.java:
    This static utility class holds a single method to perform a file read operation on a specific image, handling
    any errors that may arise.

  ButtonTile.java:
    This class is an extension of Tile that is linked to a collection of Door tiles. When a player collides with this
    tile it "unlocks" the door objects (makes them invisible).

  DashTile.java:
    This class is an extension of Tile that adds a Dash PowerUp to a Player object when it collides with it.

  Door.java:
    This class is an extension of Tile that has a .unlockDoor() method that hides the tile and turns off collision.

  DoubleJumpTile.java:
    This class is an extension of Tile that adds a Double Jump PowerUp to a Player object when it collides with it.

  Flag.java:
    This class is an extension of Tile that has a .onCollide() method that ends the current level with a win.

  Spike.java:
    This class is an extension of Tile that has a .onCollide() method that ends the current level with a loss.

  Tile.java:
    This class defines the framework for a basic tile object, including fields that store the position, dimension,
    image, color, and properties (ex. passableFromBelow) for each tile. It is extended by other Tile objects with
    custom methods.

  UntetherTile.java:
    This class is an extension of Tile that adds an Untether Jump PowerUp to a Player object when it collides with it.

  World.java:
    This class is an extension of JPanel that handles the creation, display, and running each level. It also creates
    the Player objects and runs the .tick() function to update all game state.

  Direction.java:
    This enum stores the Direction components that represent the current motion direction of the player. It is used for
    specific collision logic.

  Lava.java:
    This class is an extension of PhysicsObject that slowly encroaches from the left side of a level, ending the game
    if any players collide with it.

  Player.java:
    This class is an extension of PhysicsObject that represents the primary playable character. It implements various
    motion functions, along with collision checking and PowerUp logic.

  PowerUp.java:
    This class represents a PowerUp. It stores information about the PowerUp type and duration. It also has a function
    to compute the time remaining before the PowerUp expires.

  RunTether.java:
    This class is the entry point of the game. It creates the JFrame and ScreenManager object.

  ScreenManager.java:
    This class is an extension of JPanel that controls the current screen displayed to the player.
    Using the .changeScreen() function, it assigns new JPanel objects to the current screen,
    changing the visuals currently displayed.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  Yes, the collision logic for the players was particularly challenging, as I had never implemented collisions from
  scratch before. Additionally, the dynamic physics calculations made the implementation even more difficult,
  as players could often clip through collision boxes entirely after gaining enough speed.

  Another stumbling box I faced was the UI components, as making them switch screens,
  have custom graphics, react to hovering, and absolutely positionable required making my own custom
  UI display system, which was very time-consuming.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  My design has clear separation of functionality between systems like Physics, UI, and the World. However, many of
  these pieces do have to work together and trigger functionality in other systems. Due to this, there is a lot of
  shared resources between various systems; although, core pieces of game state, like the level layout and current
  PowerUps are privately encapsulated.

  If I had the chance to refactor, I would explore possibly moving collision logic to the Player.java class instead
  of the Tile.java class. Although this would make overriding collision functionality more difficult, it would enable
  me to make more of the game state private and have less sharing of references throughout the various systems.


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

All sprites for the game were from the free-to-use Asset Pack found here:
https://pixelfrog-assets.itch.io/pixel-adventure-1

I also referenced many StackOverflow posts to learn about additional features of Java Swing as I built the game;
however, no specific tutorials were referenced.