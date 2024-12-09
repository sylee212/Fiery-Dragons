package com.fierydragons.game;

import com.fierydragons.game.display.DisplayMain;
import javafx.application.Application;
import javafx.stage.Stage;
/*
    Author: Arvind Siva
    Co-authored:
 */
/*
    Main class to run the Fiery Dragons Game
 */
public class App extends Application {
    // the main Display for displaying all game UI
    private DisplayMain mainDisplay;

    // used by JavaFX to start up the game UI
    @Override
    public void start(Stage primaryStage) {
        this.mainDisplay = DisplayMain.getInstance();
        this.mainDisplay.start(primaryStage);
    }
}