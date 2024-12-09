package com.fierydragons.game.board;

import com.fierydragons.game.animal.Animal;
import com.fierydragons.game.animal.AnimalFactory;
import org.json.JSONObject;

/*
    Author: Arvind Siva
    Co-authored: Mohamed Areeb Ilham Riluwan
 */
/*
    VolcanoTile class represents a normal non Cave plot in a Volcano Card
 */
public class VolcanoTile extends Plot{

    // constructor for the non cave volcano tile
    public VolcanoTile(Animal animal) {
        super(animal, null);    // Based on Plot super constructor
    }

    // rebuild VolcanoTile object from json
    public static VolcanoTile fromJson(JSONObject jsonObject) {
        Animal animal = AnimalFactory.getInstance().getAnimalFromJson(jsonObject.getJSONObject("animal"));
        VolcanoTile volcanoTile = new VolcanoTile(animal);
        return volcanoTile;
    }

}
