package com.fierydragons.game.display;

import com.fierydragons.game.GameEngine;
import com.fierydragons.game.dragoncard.DragonCard;
import com.fierydragons.game.player.Player;
import com.fierydragons.game.player.PlayerManager;
import com.fierydragons.game.state.State;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
/*
    Author: Arvind Siva
    Co-authored:
 */
/*
    DisplayDetails is used to display current player turn, animal on dragon card, number of moves and
    button for triggering next action
 */
public class DisplayDetails implements InnerDisplay {
    private Label currentPlayerLabel;   // current Player turn
    private Label currentDragonCardLabel; // animal on last dragon card flipped
    private Label dragonCardMovesLabel; // moves on last flipped dragon card
    private Button actionButton;    // button for triggering next action in game
    private Button resetButton; // reset button
    private Button saveButton;  // save gameplay
    private Button peekButton;  // peek dragon cards button
    private VBox detailsBox;    // box to contain all the Label and Button

    // constructor
    public DisplayDetails() {
        this.detailsBox = new VBox();
        detailsBox.setSpacing(10); // Vertical spacing between components
        this.setUp();
    }

    /*
     set up initial data and set action button visibility to false
     Acknowledgement: ChatGPT, OpenAI, 2024
                      [The code for displaying the data using labels and the button were adapted from
                       ChatGPT]
     */
    private void setUp() {
        // used to display the name of current player turn
        this.currentPlayerLabel = new Label("Who's Turn:");
        this.currentPlayerLabel.getStyleClass().add("custom-label");
        this.detailsBox.getChildren().add(currentPlayerLabel);

        // display name of animal on last flipped dragon card
        this.currentDragonCardLabel = new Label("Dragon Card Animal:");
        this.currentDragonCardLabel.getStyleClass().add("custom-label");
        this.detailsBox.getChildren().add(currentDragonCardLabel);

        // display moves on last flipped dragon card
        this.dragonCardMovesLabel = new Label("Dragon Card Moves:");
        this.dragonCardMovesLabel.getStyleClass().add("custom-label");
        this.detailsBox.getChildren().add(dragonCardMovesLabel);

        // action button set false visibility at start
        this.actionButton = new Button("Button");
        this.actionButton.setVisible(false);
        this.detailsBox.getChildren().add(actionButton);
        // use a vertical grow policy so the button stays at the bottom
        VBox.setVgrow(actionButton, Priority.ALWAYS);

        // button to reset (restart) the game
        this.resetButton = new Button("Reset");
        this.resetButton.setVisible(true);
        this.detailsBox.getChildren().add(resetButton);
        VBox.setVgrow(resetButton, Priority.ALWAYS);
        resetButton.setOnAction(event -> {
            GameEngine.getInstance().restartGame();
            actionButton.setVisible(false);
        });

        // button to save the current progress of the game
        this.saveButton = new Button("Save progress");
        this.saveButton.setVisible(true);
        this.detailsBox.getChildren().add(saveButton);
        VBox.setVgrow(saveButton, Priority.ALWAYS);
        saveButton.setOnAction(event -> {
            DisplayMain.getInstance().saveJsonFile(
                    GameEngine.getInstance().toJson()
            );
        });

        // button to peek all the dragon cards
        this.peekButton = new Button("Peek");
        this.peekButton.setVisible(false);
        this.detailsBox.getChildren().add(peekButton);
    }

    /*
     update the board after every user interaction
     */
    public void update() {
        GameEngine gameEngine = GameEngine.getInstance();
        State state = gameEngine.getState();
        Player currentPlayer = PlayerManager.getInstance().getCurrentPlayer();

        if (state.isGameOver()) {   // when a player has won the game
            this.detailsBox.getChildren().clear();
            // display winner Player number
            Label winLabel = new Label("Game Winner: Player " + currentPlayer.getId());
            winLabel.getStyleClass().add("custom-label");
            this.detailsBox.getChildren().add(winLabel);
            // display name of winner
            Label winNameLabel = new Label("Name: " + currentPlayer.getName());
            winNameLabel.getStyleClass().add("custom-label");
            this.detailsBox.getChildren().add(winNameLabel);
            this.detailsBox.getChildren().add(this.resetButton);
            this.detailsBox.getChildren().add(this.saveButton);

        } else {
            actionButton.setVisible(false);
            DragonCard lastDragonCard = gameEngine.getDragonCardManager().getLastDragonCard();

            // if Player is playing or setting turns display current player name
            if (!state.startPlay()) {
                this.currentPlayerLabel.setText("Who's Turn?: Player " + currentPlayer.getId() + " Name: " + currentPlayer.getName());
                this.currentPlayerLabel.setStyle("-fx-background-color: " + currentPlayer.getColorName() + ";");
            }

            // allow player to peek all dragon cards before flip dragon card
            // only if the player can still peek
            if (state.isAllowPeek() && PlayerManager.getInstance().getCurrentPlayer().getPeekAllowed()) {
                // press peek button to peek all dragon cards
                this.peekButton.setVisible(true);
                this.peekButton.setOnAction(event -> {
                    state.nextPeekBtn();
                    DisplayMain.getInstance().update();
                    event.consume();
                });
            } else if (state.isPeek()) {
                // player is peeking the dragon cards
                // create button to allow player to stop peeking and return to the game
                this.peekButton.setVisible(true);
                this.peekButton.setText("Stop Peeking");
                this.peekButton.setOnAction(event -> {
                    state.nextPeekBtn();
                    this.peekButton.setText("Peek");
                    this.peekButton.setVisible(false);
                    DisplayMain.getInstance().update();
                    event.consume();
                });
            }
            else {
                // not in a peeking state or player not allowed to peek
                this.peekButton.setVisible(false);
            }



            // if there is a last flipped DragonCard display its details
            if (lastDragonCard != null) {
                currentDragonCardLabel.setText("Card animal: " + lastDragonCard.getAnimal().getName());
                dragonCardMovesLabel.setText("Moves: " + lastDragonCard.getMoves());

                if (lastDragonCard.getMoves() == 0) {
                    dragonCardMovesLabel.setText("Moves: Swap");
                }
            } else {
                currentDragonCardLabel.setText("Card animal: ");
                dragonCardMovesLabel.setText("Moves: ");
            }

            if (state.startPlay()) { // activate button to allow Player to start he game
                actionButton.setVisible(true);
                actionButton.setText("Start Game");
                actionButton.setOnAction(event -> {
                    state.nextActionBtn();
                    actionButton.setVisible(false);
                    DisplayMain.getInstance().update();
                    event.consume();
                });
            }  else if (state.isWait()) {   // activate button to allow Player to pass to next player turn
                actionButton.setVisible(true);
                actionButton.setText("Next Player");
                actionButton.setOnAction(event -> {
                    state.nextActionBtn();
                    actionButton.setVisible(false);
                    DisplayMain.getInstance().update();
                });
            } else if (state.isAllowSkip()) {   // activate button to allow player to skip their turn
                actionButton.setVisible(true);
                actionButton.setText("Skip");
                actionButton.setOnAction(event -> {
                    state.nextActionBtn();
                    actionButton.setVisible(false);
                    DisplayMain.getInstance().update();
                });
            }
        }
    }

    // return detailsBox containing all the UI elements
    public Pane getDisplay() {
        return this.detailsBox;
    }
}
