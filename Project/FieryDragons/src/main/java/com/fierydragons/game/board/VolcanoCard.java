package com.fierydragons.game.board;
import com.fierydragons.game.dragoncard.DragonCard;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
/*
    Author: Arvind Siva
    Co-authored: Mohamed Areeb Ilham Riluwan
 */
/*
    VolcanoCard represents the Volcano tiles and cave if any for a Volcano Card
 */
public class VolcanoCard {
    private Cave cave; // stores cave
    private ArrayList<VolcanoTile> volcanoTiles;    // all the volcano tiles
    private int caveIndex;
    private int numOfPlots; // number of VolcanoTiles

    // constructor
    public VolcanoCard(int numOfPlots) {
        this.cave = null;
        this.volcanoTiles = new ArrayList<>(numOfPlots);
        this.numOfPlots = numOfPlots;
    }

    // add Cave
    public void addCave(int plotIndex, Cave cave) {
        this.cave = cave;
        this.caveIndex = plotIndex;
    }

    // getter for cave
    public Cave getCaveAtIndex(int i) {
        if (this.cave != null && this.caveIndex == i)
            return this.cave;
        return null;
    }
    public Cave getCave() {
        return this.cave;
    }

    public int getCaveIndex() {
        return this.caveIndex;
    }

    // has a cave or not?
    public boolean isHasCave() {
        return this.cave != null;
    }

    // getter for numOfPlots
    public int getNumOfPlots() {
        return numOfPlots;
    }

    // getter for VolcanoTiles
    public ArrayList<VolcanoTile> getVolcanoTiles() {
        return this.volcanoTiles;
    }
    public VolcanoTile getVolcanoTileAtIndex(int i) {
        return this.volcanoTiles.get(i);
    }

    // generate json representation
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();

        ArrayList<JSONObject> jsonObjectList = new ArrayList<>();
        for (VolcanoTile volcanoTile: this.volcanoTiles) {
            jsonObjectList.add(volcanoTile.toJson());
        }
        JSONArray jsonArray = new JSONArray(jsonObjectList);

        jsonObject.put("numOfPlots", numOfPlots);
        jsonObject.put("volcanoTiles", jsonArray);

        if (isHasCave()) {
            jsonObject.put("caveIndex", caveIndex);
            jsonObject.put("cave", getCave().toJson());
        }

        return jsonObject;
    }

    // rebuild VolcanoCard object from json
    public static VolcanoCard fromJson(JSONObject jsonObject) {
        VolcanoCard volcanoCard = new VolcanoCard(jsonObject.getInt("numOfPlots"));

        JSONArray jsonArray = jsonObject.getJSONArray("volcanoTiles");

        for (int i=0; i< volcanoCard.getNumOfPlots(); i++) {
            volcanoCard.getVolcanoTiles().add(VolcanoTile.fromJson(jsonArray.getJSONObject(i)));
        }

        if (jsonObject.has("cave")) {
            volcanoCard.addCave(
                    jsonObject.getInt("caveIndex"),
                    Cave.fromJson(jsonObject.getJSONObject("cave"))
            );
        }

        return volcanoCard;
    }

}


