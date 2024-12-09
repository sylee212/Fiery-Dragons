package com.fierydragons.game;

import com.fierydragons.game.animal.Animal;
import com.fierydragons.game.animal.AnimalFactory;
import com.fierydragons.game.board.Board;
import com.fierydragons.game.display.DisplayMain;
import com.fierydragons.game.dragoncard.DragonCard;
import com.fierydragons.game.dragoncard.DragonCardManager;
import com.fierydragons.game.player.PlayerManager;
import com.fierydragons.game.state.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
/*
    Author: Arvind Siva
    Co-authored:
 */
/*
    GameEngine class is used to control the logic of the game actions
    Tracks teh current state of the game
    Only a single instance is used to ensure that there are no multiple game engine having multiple game states
 */
public class GameEngine {
    private static GameEngine instance = null;  // single GameEngine instance
    private PlayerManager playerManager; // player manager
    private ArrayList<Animal> drawnCardsSetTurns;   // drawn dragon card animal types for setting turns
    private DragonCardManager dragonCardManager;  // dragon card manager
    private Board board;    // board containing volcano tiles, player tokens and caves
    private State state;    // state tells UI what to do based on state of GameEngine

    // private constructor ensures only one instance exists
    private GameEngine() {
        this.playerManager = PlayerManager.getInstance();
        this.dragonCardManager = new DragonCardManager();
        this.board = new Board();
        this.drawnCardsSetTurns = new ArrayList<>();
        this.state = new SetTurnState();
    }

    // get the single instance of GameEngine
    public static GameEngine getInstance() {
        if (GameEngine.instance == null)
            GameEngine.instance = new GameEngine();
        return GameEngine.instance;
    }

    // getter for State
    public State getState() {
        return state;
    }
    // setter for state
    public void setState(State state) {this.state = state;}
    // state to end the game (someone won)
    public void endGame() {
        this.state = new EndState();
    }
    // state when waiting for the next turn
    public void setWaitNextTurnState() {
        this.state = new WaitNextTurnState(this.getState());
    }

    // load the board and dragon cards and player tokens
    public void initialize() {
        // initialize board and load it
        // load dragon cards
        this.dragonCardManager.initialize();
        this.board.initialize();
    }

    // set the turns of the Players
    public void setPlayerTurns(DragonCard dragoncard) {
        this.dragonCardManager.setLastDragonCard(dragoncard);

        Animal animal = dragoncard.getAnimal();
        this.drawnCardsSetTurns.add(animal);    // add to drawnCardsSetTurns

        // if all players have flipped dragon cards to select turn, set their turns
        if (this.drawnCardsSetTurns.size() == playerManager.getNumberOfPlayers()) {
            this.playerManager.setTurns(drawnCardsSetTurns);
            this.drawnCardsSetTurns.clear();
            this.state = new StartPlayState();
        } else {
            // trigger button to allow Player to pass to next Player to flip dragon card to decide their turn
            this.state = new WaitNextTurnState(this.getState());
        }
    }

    // start the game after turns have been set
    public void startGame() {
        this.dragonCardManager.setLastDragonCard(null);
        this.dragonCardManager.closeDragonCards();    // close the dragon cards at start of a turn
        this.state = new PlayState();   // allows players to play the game
    }

    // player flips a dragon card when playing the game
    public void playTurn(DragonCard dragonCard) {
        // set the last flipped dragon card
        this.dragonCardManager.setLastDragonCard(dragonCard);
        // allow player to skip if he can flip more dragon cards
        this.state.setAllowSkip(true);

        dragonCard.movePlayer(playerManager.getCurrentPlayer(), this.getBoard());

        // all dragon cards were flipped in a turn and pass turn to next player
        if (this.dragonCardManager.isAllDragonCardsOpen()) {
            this.setWaitNextTurnState();
        }
    }

    // update current player for the next turn
    public void nextTurn() {
        this.dragonCardManager.setLastDragonCard(null);
        this.dragonCardManager.closeDragonCards(); // close all dragon cards for next turn
        // update currentPlayerIndex
        playerManager.updateNextPlayerIndex();
    }

    // getter for board
    public Board getBoard() {
        return board;
    }

    // getter for dragonCardManager
    public DragonCardManager getDragonCardManager() {
        return this.dragonCardManager;
    }

    // generate json representation
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dragonManager", dragonCardManager.toJson());
        jsonObject.put("board", board.toJson());
        jsonObject.put("playerManager", playerManager.toJson());
        jsonObject.put("state", state.toJson());

        if (drawnCardsSetTurns.size() > 0) {
            ArrayList<JSONObject> jsonObjectList = new ArrayList<>();
            for (Animal animal: this.drawnCardsSetTurns) {
                jsonObjectList.add(animal.toJson());
            }
            JSONArray jsonArray = new JSONArray(jsonObjectList);
            jsonObject.put("drawnCardsSetTurns", jsonArray);
        }

        return jsonObject;
    }

    // rebuild GameEngine instance from json
    public void fromJson(JSONObject jsonObject) {
        DragonCardManager newDragonCardManager = DragonCardManager.fromJson(jsonObject.getJSONObject("dragonManager"));
        Board newBoard = Board.fromJson(jsonObject.getJSONObject("board"));
        PlayerManager newPlayerManager = PlayerManager.fromJson(jsonObject.getJSONObject("playerManager"));
        State newState = State.fromJson(jsonObject.getJSONObject("state"));

        newBoard.reloadPlayerLocations();

        this.dragonCardManager = newDragonCardManager;
        this.board = newBoard;
        this.playerManager = newPlayerManager;
        this.state = newState;

        if (jsonObject.has("drawnCardsSetTurns")) {
            ArrayList<Animal> drawnCardsSetTurns = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("drawnCardsSetTurns");

            for (int i=0; i < jsonArray.length(); i++) {
                Animal drawnAnimal = AnimalFactory.getInstance().getAnimalFromJson(jsonArray.getJSONObject(i));
                drawnCardsSetTurns.add(drawnAnimal);
            }

            this.drawnCardsSetTurns = drawnCardsSetTurns;
        }

        System.out.println(this.state.getClass());

        DisplayMain.getInstance().setUp();
    }

    // restart the game
    public void restartGame() {
        this.playerManager = PlayerManager.getInstance();
        this.dragonCardManager = new DragonCardManager();
        this.board = new Board();
        this.drawnCardsSetTurns = new ArrayList<>();
        this.state = new SetTurnState();

        DisplayMain.getInstance().restartGame();
    }
}
