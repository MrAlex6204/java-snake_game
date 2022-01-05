echo off
cls
setlocal
set main_file=src/com/game/snake/Launcher.java
set PATH=%PATH%;C:\Temp\OpenJDK_1.8\bin;

echo Main file : %main_file%
echo Compiling source code..
javac.exe -d . -sourcepath src %main_file%
echo Building jar file..
jar.exe cfm bin/snake.jar META-INF/MANIFEST.MF com/*


pause