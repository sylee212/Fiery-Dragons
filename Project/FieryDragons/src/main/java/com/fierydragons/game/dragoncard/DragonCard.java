package com.fierydragons.game.dragoncard;


import com.fierydragons.game.animal.Animal;
import com.fierydragons.game.animal.AnimalType;
import com.fierydragons.game.board.Board;
import com.fierydragons.game.player.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONObject;

/*
    Author: Arvind Siva
    Co-authored: Tharani Prathaban
 */
public abstract class DragonCard {
    private Animal animal;  // animal on the dragon card
    private int moves;  // number of moves

    // is the DragonCard flipped open
    private boolean isOpen;

    // cover image of DragonCard when not flipped
    public Image coverImage = new Image(getClass().getResourceAsStream("/assets/chitcard.png"));

    // constructor
    public DragonCard(Animal animal, int moves) {
        this.animal = animal;
        this.moves = moves;
        this.isOpen = false;
    }

    // getter for Animal
    public Animal getAnimal() {
        return animal;
    }

    // getter for moves
    public int getMoves() {
        return moves;
    }

    // is the DragonCard open?
    public boolean isOpen() {
        return isOpen;
    }

    // set teh DragonCard open or close
    public void setOpen(boolean open) {
        isOpen = open;
    }

    // move the player based on game rules
    public abstract void movePlayer(Player player, Board board);

    // get the ImageView
    public ImageView getImageView(double cardWidth, double cardHeight){
        // create imageView based on dragon card is open or not
        if (this.isOpen()) {
            return this.getAnimal().getImageView(cardWidth, cardHeight);
        } else {
            ImageView imageView = new ImageView(this.coverImage);
            imageView.setFitWidth(cardWidth);
            imageView.setFitHeight(cardHeight);
            return imageView;
        }
    }

    // generate json representation
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("moves", moves);
        jsonObject.put("isOpen", isOpen);
        jsonObject.put("animal", animal.toJson());
        jsonObject.put("type", getClass().getSimpleName());
        return jsonObject;
    }
}

