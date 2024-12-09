package com.fierydragons.game.board;


import com.fierydragons.game.GameComponentsGenerator;
import com.fierydragons.game.Location;
import com.fierydragons.game.player.Player;
import com.fierydragons.game.player.PlayerManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
    Author: Arvind Siva
    Co-authored: Mohamed Areeb Ilham Riluwan
 */
/*
    Board class holds the VolcanoCards used in the Board
 */
public class Board {
    private ArrayList<VolcanoCard> volcanoCards;    // stores Volcano Cards
    private int numOfVolcanoCards;  // number of volcano cards

    // constructor
    public Board() {
        this.volcanoCards = new ArrayList<>();
    }

    // getter for volcano cards
    public ArrayList<VolcanoCard> getVolcanoCards() {
        return volcanoCards;
    }

    // get VolcanoCard at index i
    public VolcanoCard getVolcanoCardByIndex(int i) {
        if (i >= this.numOfVolcanoCards)
            return null;
        return this.volcanoCards.get(i);
    }

    // get VolcanoTile at index j at VolcanoTile index i
    public VolcanoTile getVolcanoTileByIndex(int i, int j) {
        if (i >= this.numOfVolcanoCards)
            return null;
        return this.volcanoCards.get(i).getVolcanoTiles().get(j);
    }

    // get the cave by volcano card index and plot index (i, j)
    public Cave getCaveByIndex(int i, int j) {
        if (i >= this.numOfVolcanoCards)
            return null;
        return this.volcanoCards.get(i).getCaveAtIndex(j);
    }

    // initialize the board
    public void initialize() {
        this.volcanoCards = GameComponentsGenerator.generateVolcanoCards();
        this.numOfVolcanoCards = this.volcanoCards.size();
    }

    // getter for numOfVolcanoCards
    public int getNumOfVolcanoCards() {
        return numOfVolcanoCards;
    }
    public int getNumOfVolcanoTiles() {
        int numOfTiles = 0;
        for (VolcanoCard volcanoCard: this.volcanoCards) {
            numOfTiles += volcanoCard.getNumOfPlots();
        }
        return numOfTiles;
    }

    // generate json representation
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();

        ArrayList<JSONObject> jsonObjectList = new ArrayList<>();
        for (VolcanoCard volcanoCard: this.volcanoCards) {
            jsonObjectList.add(volcanoCard.toJson());
        }
        JSONArray jsonArray = new JSONArray(jsonObjectList);

        jsonObject.put("volcanoCards", jsonArray);
        jsonObject.put("numberOfVolcanoCards", numOfVolcanoCards);

        return jsonObject;
    }

    // rebuild Board object from json
    public static Board fromJson(JSONObject jsonObject) {
        Board board = new Board();
        board.numOfVolcanoCards = jsonObject.getInt("numberOfVolcanoCards");

        ArrayList<VolcanoCard> volcanoCards = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("volcanoCards");

        for (int i=0; i< board.getNumOfVolcanoCards(); i++) {
            volcanoCards.add(VolcanoCard.fromJson(jsonArray.getJSONObject(i)));
        }

        board.volcanoCards = volcanoCards;
        return board;
    }

    // used to set player at volcano tiles or cave based on location when rebuild from json
    public void reloadPlayerLocations() {
        ArrayList<Player> players = PlayerManager.getInstance().getPlayers();

        for (Player player: players) {
            Location location = player.getLocation();
            int i = location.getVolcanoCardIndex();
            int j = location.getPlotIndex();
            if (location.isCave()) {
                this.volcanoCards.get(i).getCave().setPlayer(player);
            } else {
                this.volcanoCards.get(i).getVolcanoTileAtIndex(j).setPlayer(player);
            }
        }
    }
}