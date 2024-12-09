package com.fierydragons.game.state;

import com.fierydragons.game.GameEngine;
import com.fierydragons.game.dragoncard.DragonCard;
import com.fierydragons.game.player.PlayerManager;
import org.json.JSONObject;

/*
    Author: Mohamed Areeb Ilham Riluwan
    Co-authored: Arvind Siva
 */

/*
    PeekState is used when the Player is peeking all the dragon cards
 */
public class PeekState extends State {

    private State lastState; // the last state before this state

    // constructor
    public PeekState(State state) {
        super();
        this.lastState = state;
    }

    // tells UI to not allow any dragon cards to be flipped
    @Override
    public boolean isDragonCardsFreeze() {
        return true;
    }

    // is peeking mode allowed for dragon cards
    @Override
    public boolean isDragonCardsPeek() { return true; }

    // is in peek state?
    @Override
    public boolean isPeek() { return true; }

    // DragonCards are already flipped to peek
    @Override
    public void nextDragonCardClick(DragonCard dragonCard) {

    }

    // no action button
    @Override
    public void nextActionBtn() {

    }

    /// action in peek button to perform
    @Override
    public void nextPeekBtn() {
        GameEngine.getInstance().setState(this.lastState);
        PlayerManager.getInstance().getCurrentPlayer().setPeekAllowed(false);
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

    // rebuild PeekState from json
    public static PeekState fromJson(JSONObject jsonObject) {
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

        PeekState newState = new PeekState(lastState);
        newState.setAllowSkip(jsonObject.getBoolean("allowSkip"));
        return newState;
    }
}
