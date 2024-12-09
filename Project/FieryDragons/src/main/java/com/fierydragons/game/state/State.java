package com.fierydragons.game.state;

import com.fierydragons.game.dragoncard.DragonCard;
import org.json.JSONObject;

/*
    Author: Arvind Siva
    Co-authored: Mohamed Areeb Ilham Riluwan
 */
/*
    State class is used by GameEngine to inform Display (UI) on what to render
    based on current game state and what actions to perform on user interaction
 */
public abstract class State {
    private boolean allowSkip;  // allow player to skip turn or not

    // constructor
    public State() {
        this.allowSkip = false;
    }

    // tells UI to allow player to flip more dragon cards or not
    public boolean isDragonCardsFreeze() {
        return isGameOver() || isWait();
    };

    public boolean isDragonCardsPeek() { return false; }

    // is the game over (has someone won)?
    public boolean isGameOver() {
        return false;
    }

    // ready or not to start playing the game?
    public boolean startPlay() { return false; }

    // wait for the Player to pass the turn to next Player?
    public boolean isWait() { return false; }

    // is the currentPlayer allowed to skip his turn?
    public boolean isAllowSkip() {return this.allowSkip;}

    // setter for allowSkip
    public void setAllowSkip(boolean allowSkip) {
        this.allowSkip = allowSkip;
    }

    // is allow to display peek button ?
    public boolean isAllowPeek() { return false; }

    // is in peek mode?
    public boolean isPeek() { return false; }

    // next action to perform by GameEngine when DragonCard is flipped
    public abstract void nextDragonCardClick(DragonCard dragonCard);

    // next action to perform by GameEngine when a button is pressed
    public abstract void nextActionBtn();

    // next action to trigger when peek btn is pressed
    public void nextPeekBtn() {};

    // generate json representation
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", this.getClass().getSimpleName());
        jsonObject.put("allowSkip", allowSkip);
        return jsonObject;
    }

    // rebuild State object from json
    public static State fromJson(JSONObject jsonObject) {
        String stateType = jsonObject.getString("type");
        State state;

        switch (stateType) {
            case "SetTurnState":
                state = new SetTurnState();
                break;
            case "WaitNextTurnState":
                state = WaitNextTurnState.fromJson(jsonObject);
                break;
            case "StartPlayState":
                state = new StartPlayState();
                break;
            case "PlayState":
                state = new PlayState();
                break;
            case "PeekState":
                state = PeekState.fromJson(jsonObject);
                break;
            case "EndState":
                state = new EndState();
                break;
            default:
                state = null;
        }

        state.setAllowSkip(jsonObject.getBoolean("allowSkip"));
        return state;
    }
}
