# Flappy Bird Java

This project is a Java version of **Flappy Bird**.

In Flappy Bird, you control a bird that constantly falls due to gravity. Press a key to flap upward and pass through gaps between incoming pipes. Every pipe you pass increases your score, and the game ends if you hit a pipe or the ground.

## How to run a Java application (quick tutorial)

If your main class is `Main` (for example: `Main.java`), you can compile and run it like this:

```bash
# 1) Check Java is installed
java -version
javac -version

# 2) Compile
javac Main.java

# 3) Run
java Main
```

If your file is inside a folder (example: `src/Main.java`):

```bash
# Compile into an output folder
javac -d out src/Main.java

# Run from compiled classes
java -cp out Main
```
