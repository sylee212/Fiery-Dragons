package com.fierydragons.game.board;

import com.fierydragons.game.animal.Animal;
import com.fierydragons.game.animal.AnimalFactory;
import com.fierydragons.game.display.ColorOption;
import javafx.scene.paint.Color;
import org.json.JSONObject;

/*
    Author: Arvind Siva
    Co-authored: Mohamed Areeb Ilham Riluwan
 */
/*
    Cave class represents a Cave in a Volcano Tile
 */
public class Cave extends Plot {
    private int playerID;   // player ID of player that belongs to this Cave
    private ColorOption colorOption;    // color of Cave

    // constructor for cave which will save player id, the color chosen by the player for the cave and animal
    public Cave(int playerID, Animal animal, ColorOption colorOption) {
        super(animal, null);
        this.playerID = playerID;
        this.colorOption = colorOption;
    }

    // getter for ID of player that belongs to this Cave
    public int getPlayerID() {
        return playerID;
    }

    // get color of Cave
    public Color getColor() {
        return colorOption.getColor();
    }
    public ColorOption getColorOption() { return colorOption; }

    // generate json representation
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = super.toJson();
        jsonObject.put("color", colorOption);
        jsonObject.put("playerID", playerID);
        return jsonObject;
    }

    // rebuild Cave object from json
    public static Cave fromJson(JSONObject jsonObject) {
        String colorName = jsonObject.getString("color");

        Color color = null;
        switch(colorName) {
            case "Red":
                color = Color.RED;
                break;
            case "Yellow":
                color = Color.YELLOW;
                break;
            case "Green":
                color = Color.GREEN;
                break;
            case "Blue":
                color = Color.BLUE;
                break;
            default:
                color = null;
        }

        Cave cave = new Cave(
                jsonObject.getInt("playerID"),
                AnimalFactory.getInstance().getAnimalFromJson(jsonObject.getJSONObject("animal")),
                new ColorOption(colorName, color)
        );
        return cave;
    }
}

