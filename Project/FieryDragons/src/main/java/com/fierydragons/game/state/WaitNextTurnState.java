package com.fierydragons.game.state;

import com.fierydragons.game.GameEngine;
import com.fierydragons.game.dragoncard.DragonCard;
import org.json.JSONObject;
/*
    Author: Arvind Siva
    Co-authored: Mohamed Areeb Ilham Riluwan
 */

/*
    WaitTurnState is used for UI to freeze the current game state and allow Player to click
    Next Player button for next Player's turn
 */
public class WaitNextTurnState extends State {
    private State lastState; // the last state before this state

    // constructor
    public WaitNextTurnState(State state) {
        super();
        this.lastState = state;
        this.lastState.setAllowSkip(false);
    }

    // tells UI to not allow any dragon cards to be flipped
    @Override
    public boolean isDragonCardsFreeze() {
        return true;
    }

    // DragonCards are not allowed to be flipped in this state
    @Override
    public void nextDragonCardClick(DragonCard dragonCard) {

    }

    // updates next Player index and allow next Player to play their turn by resetting teh state in GameEngine
    @Override
    public void nextActionBtn() {
        GameEngine.getInstance().nextTurn();
        GameEngine.getInstance().setState(this.lastState);
    }

    // wait for the Player to pass the turn to next Player
    @Override
    public boolean isWait() {
        return true;
    }

    // generate json representation
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = super.toJson();
        jsonObject.put("allowSkip", this.isAllowSkip());
        jsonObject.put("type", this.getClass().getSimpleName());
        jsonObject.put("lastState", this.lastState.toJson());

        return  jsonObject;
    }

    // rebuild WaitNextTurnState from json
    public static WaitNextTurnState fromJson(JSONObject jsonObject) {
        String stateType = jsonObject.getJSONObject("lastState").getString("type");
        State lastState;

        switch (stateType) {
            case "SetTurnState":
                lastState = new SetTurnState();
                break;
            case "StartPlayState":
                lastState = new StartPlayState();
                break;
            case "PlayState":
                lastState = new PlayState();
                break;
            case "EndState":
                lastState = new EndState();
                break;
            default:
                lastState = null;
        }
        lastState.setAllowSkip(jsonObject.getJSONObject("lastState").getBoolean("allowSkip"));

        WaitNextTurnState newState = new WaitNextTurnState(lastState);
        newState.setAllowSkip(jsonObject.getBoolean("allowSkip"));
        return newState;
    }
}
