package com.fierydragons.game.animal;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONObject;

/*
    Author: Lee Sing Yuan
    Co-authored: Arvind Siva
 */
/*
    Represents the Animal on the DragonCard, in the Volcano Tile Plot and in Cave
 */
public abstract class Animal {
    private Image image;    // stores image of the animal
    private String name;    // stores name of the animal

    public Animal(String imagePath, String name) {
        this.image = new Image(getClass().getResourceAsStream(imagePath));
        this.name = name;
    }

    // getter for image
    public Image getImage() {
        return image;
    }

    // getter for name
    public String getName() {
        return name;
    }

    // returns an ImageView containing the image at specified width and height
    public ImageView getImageView(double width, double height) {
        ImageView imageView = new ImageView(this.image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    // generate json representation
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", this.getClass().getSimpleName());
        return jsonObject;
    }
}


