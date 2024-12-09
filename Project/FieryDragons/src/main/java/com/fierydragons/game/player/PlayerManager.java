package com.fierydragons.game.player;

import com.fierydragons.game.animal.Animal;
import com.fierydragons.game.animal.AnimalFactory;
import com.fierydragons.game.animal.AnimalType;
import com.fierydragons.game.display.ColorOption;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
/*
    Author: Lee Sing Yuan
    Co-authored: Arvind Siva
 */
/**
 * This is the class responsible for handling the creation and management of player
 */

public class PlayerManager {
    public static int MAX_PLAYERS = 4;  // max num of players
    private int numberOfPlayers;    // number of players
    private int currentNumberOfPlayers; // current number of dded players
    private int currentPlayerIndex; // current turn player index

    // stores the players
    private ArrayList<Player> players;

    // singleton class
    private static PlayerManager instance = null;

    // constructor
    private PlayerManager(){}

    /**
     * The function to get the playerManager because it is a singleton class
     * @return PlayerManager
     */
    public static PlayerManager getInstance(){
        // singleton class
        if ( instance == null ){
            instance = new PlayerManager();
        }
        return instance;
    }

    // getter for number of players
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    // set the number of players
    public void setNumberOfPlayers(int numberOfPlayers) {
        // sets the number of players
        this.numberOfPlayers = numberOfPlayers;
        this.currentNumberOfPlayers = 0;
        this.currentPlayerIndex = 0;
        // initializes the array of player
        this.players = new ArrayList<>();
    }

    // getter for players
    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    // setter for players (used to set the turns)
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    // get player by index
    public Player getPlayerByIndex(int i) {
        return this.players.get(i);
    }

    // add a player
    public void addPlayer(String name, int age, ColorOption colorOption){
        // only create a new player if the current number of players is below the max amount
        // its an additional protection
        if (currentNumberOfPlayers < numberOfPlayers) {
            Player player = new Player(name, age, colorOption);
            this.players.add(player);

            // add the current number of players
            this.currentNumberOfPlayers += 1;
        }
    }

    // get the player who is in current turn
    public Player getCurrentPlayer() {
        return this.players.get(this.currentPlayerIndex);
    }


    // update currentPlayerIndex to get Player for next turn
    public void updateNextPlayerIndex() {
        // update currentPlayerIndex
        this.currentPlayerIndex++;
        this.currentPlayerIndex %= this.numberOfPlayers;

    }

    // use the animals on flipped dragon cards to determine player turns
    public void setTurns(ArrayList<Animal> drawnCardsSetTurns) {
        // used to arrange Players in order of turns
        ArrayList<Player> playersTurns = new ArrayList<>();
        AnimalFactory animalFactory = AnimalFactory.getInstance();

        // find most recent Player to flip Dragon Pirate and make him first
        // then clockwise from him for other Players turn
        for (int i = getNumberOfPlayers()-1; i >= 0 ; i--) {
            if (drawnCardsSetTurns.get(i) == animalFactory.getAnimal(AnimalType.PIRATE_DRAGON)) {
                playersTurns.add(getPlayerByIndex(i));

                int j = (i+1) % getNumberOfPlayers();
                while (j != i) {
                    playersTurns.add(getPlayerByIndex(j));
                    j++;
                    j %= getNumberOfPlayers();
                }
                break;
            }
        }

        // find youngest Player if nobody flipped a Pirate Dragon
        // then clockwise from him for other Players turn
        if (playersTurns.size() == 0) {
            int youngestPlayerIndex = 0;
            int youngestPlayerAge =Integer.MAX_VALUE;
            for (int i = 0; i < getNumberOfPlayers(); i++) {
                if (getPlayerByIndex(i).getAge() < youngestPlayerAge) {
                    youngestPlayerIndex = i;
                    youngestPlayerAge = getPlayerByIndex(i).getAge();
                }
            }

            int j = youngestPlayerIndex;
            playersTurns.add(getPlayerByIndex(j));
            j++;
            j %= getNumberOfPlayers();

            while (j != youngestPlayerIndex) {
                playersTurns.add(getPlayerByIndex(j));
                j++;
                j %= getNumberOfPlayers();
            }
        }

        setPlayers(playersTurns);
    }

    // generate json representation
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        ArrayList<JSONObject> jsonObjectList = new ArrayList<>();
        for (Player player: this.players) {
            jsonObjectList.add(player.toJson());
        }
        JSONArray jsonArray = new JSONArray(jsonObjectList);

        jsonObject.put("numberOfPlayers", numberOfPlayers);
        jsonObject.put("currentPlayerIndex", currentPlayerIndex);
        jsonObject.put("players", jsonArray);

        return jsonObject;
    }

    // set the index of current player
    private void setCurrentPlayerIndex(int i) {
        this.currentPlayerIndex = i;
    }

    // rebuild PlayerManager instance from json
    public static PlayerManager fromJson(JSONObject jsonObject) {
        instance = new PlayerManager();

        instance.setNumberOfPlayers(jsonObject.getInt("numberOfPlayers"));
        instance.setCurrentPlayerIndex(jsonObject.getInt("currentPlayerIndex"));

        ArrayList<Player> loadedPlayers = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("players");

        for (int i=0; i< getInstance().numberOfPlayers; i++) {
            loadedPlayers.add(Player.fromJSON(jsonArray.getJSONObject(i)));
        }

        instance.setPlayers(loadedPlayers);
        return instance;
    }
}
