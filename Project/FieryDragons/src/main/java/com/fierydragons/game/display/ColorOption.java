package com.fierydragons.game.display;

import javafx.scene.paint.Color;
/*
    Author: Arvind Siva
    Co-authored:
 */
/*
    ColorOption class is used to display list of colors to be chosen by Players when
    inputting their details such as name and age
    Acknowledgement: ChatGPT, OpenAI, 2024
 */
public class ColorOption {
    private final String name;  // name of the Color
    private final Color color;  // Color object

    // constructor
    public ColorOption(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    // getter for Color
    public Color getColor() {
        return color;
    }

    // returns name of Color to be used in dropdown menu for Player to select their color when entering their details
    @Override
    public String toString() {
        // Return the name of the color instead of the default hex value
        return name;
    }
}