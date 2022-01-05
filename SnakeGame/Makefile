#This make file builds the Java executable from the source
$(VERBOSE).SILENT:

.PHONY: default run build clean

#Main class package
main_pckg = com.game.snake.Launcher
#Main class java file location
main_file=src/com/game/snake/Launcher.java
#Main class file location
main_clss=com/game/snake/Launcher.class
#Libraries paths separate each lib by ";" character
lib=-cp bin

default:
#Compile 
	clear
	javac -d . -sourcepath src $(main_file)
	echo Compiling Java from Source [ O K ]
run:build
#Run Jar file	
	echo Running [ O K ]
	java -jar bin/snake.jar
build:
#Build Jar file attaching Manifest file
	jar -cfm ./bin/snake.jar META-INF/MANIFEST.MF com/*
	echo Build [ O K ]
clean:
#Clean Directory
	rm -r ./com
	rm -f ./bin/snake.jar
	echo Clean [ O K ]
