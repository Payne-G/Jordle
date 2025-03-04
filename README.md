**Jordle - A JavaFX Wordle Clone**
Welcome to Jordle, a JavaFX-based recreation of the popular word puzzle game Wordle! This project aims to provide a sleek and interactive experience while showcasing the power of JavaFX.

With Jordle, players must guess a hidden five-letter word within six attempts, receiving color-coded feedback for each guess to indicate letter accuracy—just like the original game.

This project is designed for:

- Java and JavaFX enthusiasts looking for a fun coding challenge
- Developers interested in game development with JavaFX
- Anyone who loves word puzzles!

**Installation**
Prerequisites
Before running Jordle, ensure you have the following installed:

- Java Development Kit (JDK) 17+
- JavaFX SDK (if using a standalone JDK)

Simply clone the repository to have all you need to get started.
I recommend changing the "words.txt" file by adding words sepereated by lines to include all of the words you want (you can find lists of all wordle words by simply searching for them, there is a github repository that you can copy and paste into this file).
You can also change the confetti.GIF and jordleImage.jpg (not really recommended but can be fun) to whatever you want as long as they have the same file name and format.

**Running the Program**
While you can run the program through something like IntelliJ or VSCode, I am just going to outline the Command Prompt method.

With command prompt open, direct your command prompt to the folder where Jordle is installed using cd "Path To your Jordle folder", then you want to run the following lines seperately:
javac --module-path "Path to your javafx lib folder" --add-modules javafx.controls,javafx.fxml Jordle.java

java --module-path "Path to your javafx lib folder" --add-modules javafx.controls,javafx.fxml Jordle
The program should open and you can play the game!
