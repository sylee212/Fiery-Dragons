package com.fierydragons.game;

import org.json.JSONObject;

/*
    Author: Arvind Siva
    Co-authored:
 */
/*
    Location class represents current location of Player in Board
 */
public class Location {
    private int volcanoCardIndex;    // index of current VolcanoCard
    private int plotIndex;   // index of current Plot
    private boolean isCave; // is the location a Cave?

    // constructor
    public Location(int volcanoCardIndex, int plotIndex, boolean isCave) {
        this.volcanoCardIndex = volcanoCardIndex;
        this.plotIndex = plotIndex;
        this.isCave = isCave;
    }

    // getter for volcanoTile index
    public int getVolcanoCardIndex() {
        return volcanoCardIndex;
    }

    // getter for Plot index
    public int getPlotIndex() {
        return this.plotIndex;
    }

    // getter for isCave
    public boolean isCave() {
        return isCave;
    }

    // generate json representation
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("volcanoCardIndex", volcanoCardIndex);
        jsonObject.put("plotIndex", plotIndex);
        jsonObject.put("isCave", isCave);
        return jsonObject;
    }

    // rebuild Location object from json
    public static Location fromJson(JSONObject jsonObject) {
        return new Location(
                jsonObject.getInt("volcanoCardIndex"),
                jsonObject.getInt("plotIndex"),
                jsonObject.getBoolean("isCave"));
    }
}
