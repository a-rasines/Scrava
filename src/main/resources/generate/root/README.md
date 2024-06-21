# Congratulations, you've exported your code
Welcome to the real world environment, in this folder you have everything that you'll need to give your project the next step in development.
## What's all these files and folders?
This might be your first time seeing the insides of a java application, so I'm gonna describe what each element included here is so you can navigate much more confortably.
* **/bin:** *(short for binary)* this folder is where the compiler outputs your code as binaries so then it creates the jar file.
* **/dist:** here you can find the jar file, which is the executable file of the project. You can run that file double clicking the file or writing `java -jar {filename}` in a command prompt only if you've moved the command prompt to that folder first (it does not show `C:/Windows/System32` but rather the folder on the left of the command).
* **/resources:** here's where all the textures are stored. To prevent duplicate names, the names have been changed, but you can change them back as long as you also change their name in the sprite classes that use those textures.
* **/src:** *(short for source)* here are stored the files you will need to modify in order to implement new features to the project.
* **.classpath:** This is a configuration normally used by IDEs like Eclipse that defines where do they have to search for source files, the compiler to use and where to output the code.
* **compile.bat:** This is a file for Windows systems that automatically generates the jar file and the bin and dist folders.
* **compile.sh:** This is a file for Linux and Mac systems that automatically generates the jar file and the bin and dist folders.
* **MANIFEST.MF:** This is metadata that Java needs inside the jar file in order to know certain details about how to run it

## How are the source files sorted?
The order it's pretty simple:
### Base folder
In this folder you can find the files that compose the graphic motor and the basic functionality of the sprites.
#### EventSystem
In this class you can find all the needed events to implement most of features. An event is a piece of code that runs whenever the user creates a certain type of input, that more or less is described in the name of the function that handles it. <br>
This class is mostly for your personal implementation, as you'll see if you enter in it that most functions are empty.

#### GraphicsPanel
Here you can find the functionality related to rendering and managing the sprites, also, here's the main function, which is the function that java runs when you start the program, this can be identified by it's declaration, which is always `public static void main(String[] args)`.

#### Sprite
This class, as I've mentioned before, has the basic functionality of sprites that is necesary for the GraphicsPanel or the EventSystem. If you want to add a function to all sprites at the same time or that interacts with the other classes in this folder, you have to add it here.

### Generated folder
In this other folder you can find all that you've made inside the application.
In here there are two kinds of files, your sprites and GlobalVariables.

Global variables has every variable that you've declared as global while the rest of files have their variables and the blocks associated with the sprite.