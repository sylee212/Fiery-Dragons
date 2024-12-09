package com.fierydragons.game.dragoncard;

import com.fierydragons.game.GameComponentsGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
/*
    Author: Arvind Siva
    Co-authored: Tharani Prathaban
 */
/**
 * Manager to assist the GameEngine to store the dragon cards and metadata related to dragon cards
 * to run the game
 */
public class DragonCardManager {
    // list of dragon cards
    private ArrayList<DragonCard> dragonCards;

    // the last flipped dragon card
    private DragonCard lastDragonCard;

    // number of dragon cards
    private int numOfDragonCards;

    // constructor
    public DragonCardManager() {

    }

    // initialize the manager
    public void initialize(){
        this.dragonCards = GameComponentsGenerator.generateDragonCards();
        this.numOfDragonCards = this.dragonCards.size();
//        this.shuffleDragonCards();
    }

    // getter for dragon cards
    public ArrayList<DragonCard> getDragonCards() {
        return dragonCards;
    }

    // close all the dragon cards
    public void closeDragonCards() {
        for (int i = 0; i < this.dragonCards.size(); i++) {
            this.dragonCards.get(i).setOpen(false);
        }
    }

    // is all the dragon cards flip open?
    public boolean isAllDragonCardsOpen() {
        for(int i = 0; i < this.dragonCards.size(); i++) {
            if (!dragonCards.get(i).isOpen())
                return false;
        }
        return true;
    }

    // getter for last flipped dragon card
    public DragonCard getLastDragonCard() {
        return lastDragonCard;
    }

    // setter for last flipped dragon card
    public void setLastDragonCard(DragonCard lastDragonCard) {
        this.lastDragonCard = lastDragonCard;
    }

    // shuffle the dragon cards
    private void shuffleDragonCards() {
        Collections.shuffle(this.dragonCards);
    }

    // getter for number of dragon cards
    public int getNumOfDragonCards() {
        return this.numOfDragonCards;
    }

    // get a dragon card by index
    public DragonCard getDragonCardByIndex(int i) {
        return this.dragonCards.get(i);
    }

    // generate json representation
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        ArrayList<JSONObject> jsonObjectList = new ArrayList<>();
        for (DragonCard dragonCard: this.dragonCards) {
            jsonObjectList.add(dragonCard.toJson());
        }
        JSONArray jsonArray = new JSONArray(jsonObjectList);

        jsonObject.put("numOfDragonCards", numOfDragonCards);

        if (lastDragonCard != null)
            jsonObject.put("lastDragonCard", dragonCards.indexOf(lastDragonCard));
        jsonObject.put("dragonCards", jsonArray);

        return jsonObject;
    }

    // rebuild DragonCardManager from json
    public static DragonCardManager fromJson(JSONObject jsonObject) {
        DragonCardManager dragonCardManager = new DragonCardManager();
        dragonCardManager.numOfDragonCards = jsonObject.getInt("numOfDragonCards");

        ArrayList<DragonCard> dragonCards = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("dragonCards");

        for (int i=0; i< dragonCardManager.numOfDragonCards; i++) {
            String dragonCardType = jsonArray.getJSONObject(i).getString("type");
            DragonCard dragonCard;

            switch (dragonCardType) {
                case "ForwardDragonCard":
                    dragonCard = ForwardDragonCard.fromJson(jsonArray.getJSONObject(i));
                    break;
                case "BackwardDragonCard":
                    dragonCard = BackwardDragonCard.fromJson(jsonArray.getJSONObject(i));
                    break;
                case "SwapDragonCard":
                    dragonCard = SwapDragonCard.fromJson(jsonArray.getJSONObject(i));
                    break;
                case "WildDragonCard":
                    dragonCard = WildDragonCard.fromJson(jsonArray.getJSONObject(i));
                    break;
                default:
                    dragonCard = null;
            }

            dragonCards.add(dragonCard);
        }

        if (jsonObject.has("lastDragonCard")) {
            dragonCardManager.lastDragonCard = dragonCards.get(jsonObject.getInt("lastDragonCard"));
        }
        dragonCardManager.dragonCards = dragonCards;

        return dragonCardManager;
    }
}
