package com.fierydragons.game.player;

import com.fierydragons.game.Location;
import com.fierydragons.game.display.ColorOption;
import javafx.scene.paint.Color;
import org.json.JSONObject;

/*
    Author: Arvind Siva
    Co-authored: Lee Sing Yuan
 */
/*
    Player class to store metadata of Players
 */
public class Player {
    private String name;    // name of Player
    private int age;    // Player's age
    private int id; // Player ID
    private static int ID_GENERATOR = 1;    // generate Player IDs
    private ColorOption colorOption;    // Color used by Player token and Cave
    private Location location;  // Current location of Player on Board
    private Location lastLocation;  // Last location of Player on Board
    private int moves;  // Number of steps moved from Cave in clockwise direction
    private boolean peekAllowed; // is player allowed to peek

    // constructor
    public Player(String name, int age, ColorOption colorOption) {
        this.name = name;
        this.age = age;
        this.id = Player.ID_GENERATOR;
        this.colorOption = colorOption;
        this.location = null;
        this.lastLocation = null;
        this.moves = 0;
        this.peekAllowed = true;
        Player.ID_GENERATOR++;
    }

    // constructor for rebuilding from json
    public Player(String name, int age, ColorOption colorOption, int id) {
        this.name = name;
        this.age = age;
        this.colorOption = colorOption;
        this.id = id;
        this.location = null;
        this.lastLocation = null;
        this.peekAllowed = true;
        this.moves = 0;
    }

    // getter for name
    public String getName() {
        return name;
    }

    // getter for age
    public int getAge() {
        return age;
    }

    // getter for ID
    public int getId() {
        return id;
    }

    // getter for Color
    public Color getColor() {
        return colorOption.getColor();
    }

    public ColorOption getColorOption() { return this.colorOption; }

    // get name of color

    public String getColorName() { return colorOption.toString(); }

    // getter for Location
    public Location getLocation() {
        return location;
    }

    /*
        set current Location and update last Location
        update moves based on current moved number of steps
     */
    public void setLocation(Location location, int moves) {
        this.lastLocation = this.location;
        this.location = location;
        this.moves += moves;
    }

    // is player allowed to peek
    public boolean getPeekAllowed(){
        return this.peekAllowed;
    }

    // set player allowed to peek
    public void setPeekAllowed(boolean peekAllowed){
        this.peekAllowed = peekAllowed;
    }

    // set the location when swapping

    public void setLocationSwap(Location location, int moves, int numOfPlots) {
        this.lastLocation = this.location;
        this.location = location;

        if (this.moves + moves > numOfPlots + 1) {
            this.moves = (this.moves + moves) % numOfPlots;
        } else {
            this.moves += moves;
        }
    }

    // getter for last Location
    public Location getLastLocation() {
        return lastLocation;
    }


    // get moves
    public int getMoves() {
        return this.moves;
    }

    // create json representation
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("age", age);
        jsonObject.put("id", id);
        jsonObject.put("color", colorOption);
        jsonObject.put("moves", moves);
        jsonObject.put("location", location.toJson());
        jsonObject.put("lastLocation", lastLocation == null ? null:lastLocation.toJson());
        jsonObject.put("peekAllowed", peekAllowed);
        return jsonObject;
    }

    // rebuild Player object from json
    public static Player fromJSON(JSONObject jsonObject) {
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

        Player player = new Player(
                jsonObject.getString("name"),
                jsonObject.getInt("age"),
                new ColorOption(colorName, color),
                jsonObject.getInt("id"));

        if (jsonObject.has("lastLocation")) {
            player.setLocation(
                    Location.fromJson(jsonObject.getJSONObject("lastLocation")),
                    0
            );
        }

        player.setPeekAllowed(jsonObject.getBoolean("peekAllowed"));

        player.setLocation(
                Location.fromJson(jsonObject.getJSONObject("location")),
                jsonObject.getInt("moves")
        );

        System.out.println(jsonObject);
        return player;
    }
}


