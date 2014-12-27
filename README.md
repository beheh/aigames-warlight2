# Warlight 2 AI

This repository contains a java bot for a virtual version of the board game "Risk" (called WarLight).

These packages have not been licensed yet.


## Bot implementations

The bot implementations are contained in the `impl` package. They share the common backend and actually do the decision making.

### Quickstep

Quickstep is the first implementation of the bot. It roughly estimates attacks and randomly reinforces owned regions.

### Foxtrot

Foxtrot is a big step up from the Quickstep bot. It employs breadth-first search to move armies towards the border.
Unfortunately the scoring system forces Foxtrot to pick a single best region to reinforce, which leads to a disadvantage when fighting on multiple fronts.

### Jive

Jive is the latest bot generation and can handle multiple fronts, detecting borders and simulating attacks in advance.


## Backend

The common backend handles the game engine communications and provides uncoupled functionalities.

### Launcher

The `Launcher` class initializes the latest bot and communication classes.

### Generic bots

The `bot` package contains the basic commands and abstract bot class for bots to express their decisions.

### Statistics

The `stats` package contains various ranking and simulation classes.

### Game state

The `game` package contains the `GameState` class, which keeps track of the known game environment.
The contained `Map` class keeps track of the known map, consisting of `Region`s and `SuperRegion`s.

### Input/Output

The `io` package handles the game engine communications.