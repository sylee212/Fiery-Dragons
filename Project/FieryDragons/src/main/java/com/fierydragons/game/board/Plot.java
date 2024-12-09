package com.fierydragons.game.board;


import com.fierydragons.game.animal.Animal;
import com.fierydragons.game.animal.AnimalType;
import com.fierydragons.game.player.Player;
import org.json.JSONObject;

/*
    Author: Arvind Siva
    Co-authored: Mohamed Areeb Ilham Riluwan
 */
/*
    Abstract class Plot which represents a cave or a plot in a volcano card
 */
public abstract class Plot {
    private Animal animal;  // animal on the plot
    private Player player;  // current standing Player

    // constructor
    public Plot(Animal animal, Player player) {
        this.animal = animal;
        this.player = player;
    }

    // getter for Animal
    public Animal getAnimal() {
        return this.animal;
    }

    // has Player or not?
    public boolean hasPlayer() {
        return this.player != null;
    }

    // get standing Player
    public Player getPlayer() {
        return player;
    }

    // set new Standing Player
    public void setPlayer(Player player) {
        this.player = player;
    }

    // creates json representation
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("animal", animal.toJson());
        if (player != null)
            jsonObject.put("playerID", player.getId());
        jsonObject.put("hasPlayer", hasPlayer());
        return jsonObject;
    }
}
