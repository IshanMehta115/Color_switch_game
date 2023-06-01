# Color_switch_game :joystick:


## Table of Contents

1. [About](#about)
2. [Screenshot](#screenshot)
3. [Features](#features)
4. [Implementation](#implementation)

## About 
Color Switch: A JavaFX College Project

Color Switch is a captivating project developed during my college journey using JavaFX. Inspired by the popular game of the same name, it showcases my passion for game development and programming skills. The project offers an immersive and challenging gaming experience, engaging players with its vibrant colors and addictive gameplay.

Using JavaFX, I created an interactive and visually appealing interface, allowing users to navigate through various obstacles and switch colors to progress further. The project demonstrates my proficiency in Java programming and GUI development, as well as my ability to implement complex game mechanics.

As a college project, Color Switch allowed me to apply the theoretical knowledge gained in my programming courses and showcase my creativity in designing a game with intuitive controls and smooth animations. It served as an excellent opportunity to refine my problem-solving skills and collaborate with team members.

Color Switch stands as a testament to my dedication, technical expertise, and enthusiasm for game development. It is a proud accomplishment that highlights my ability to transform ideas into functional and enjoyable experiences.


## Screenshot

<p align="center">
	<img height="600" src="https://github.com/IshanMehta115/Color_switch_game/blob/main/color_switch.png">
</p>

## Features

- It has a single endless mode in which you jump through same colored obstacles and gain stars.

- It has an option to change the color theme for the ball and the obstacles.

- It has an option to pause your game in between and then resume it anytime.

- You can also save your game after pausing it and then later load it from the main menu.

## Implementation

- The ball is an object of the player class which extends from in built Circle class.

- The obstacles are similarly made using the Rectangle and Arc class.

- The star is made using a Star class which extends from inbuilt class Polygon.

- The objects of these classes are used in the game class which stores all the score and simulates the game by connecting all the components together.

- The game class is written and read from saved_games.txt file while saving and loading respectively.

- All the above classes implements the Serializable interface so that they can be saved in a file.

- The public class has a hashMap containing all the Scenes used in the game.These Scene objects have objects of Buttons , ListView, Labels , TextField the functions which connects all the scenes together.

- More information is provided in the comments along with the functions , classes and variables.

		
