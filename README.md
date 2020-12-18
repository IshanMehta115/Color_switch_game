# Color_switch_game

This game is a replica of the famous color switch game. It is made using javafx.

Features:

		It has a single endless mode in which you jump through same colored obstacles and gain stars.

		It has an option to change the color theme for the ball and the obstacles.

		It has an option to pause your game in between and then resume it anytime.

		You can also save your game after pausing it and then later load it from the main menu.

Implementation:

		The ball is an object of the player class which extends from in built Circle class.

		The obstacles are similarly made using the Rectangle and Arc class.

		The star is made using a Star class which extends from inbuilt class Polygon.

		The objects of these classes are used in the game class which stores all the score and simulates the game by connecting all the components together.

		The game class is written and read from saved_games.txt file while saving and loading respectively.

		All the above classes implements the Serializable interface so that they can be saved in a file.

		The public class has a hashMap containing all the Scenes used in the game.These Scene objects have objects of Buttons , ListView, Labels , TextField the functions which connects all the scenes together.

		More information is provided in the comments along with the functions , classes and variables.

		
