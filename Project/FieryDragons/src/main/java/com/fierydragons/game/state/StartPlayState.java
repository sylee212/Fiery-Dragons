package com.fierydragons.game.state;

import com.fierydragons.game.GameEngine;
import com.fierydragons.game.dragoncard.DragonCard;

/*
    Author: Arvind Siva
    Co-authored: Mohamed Areeb Ilham Riluwan
 */
/*
    StartPlayState is used when all Player turns have been set and the game is ready to be started in UI
 */
public class StartPlayState extends State{

    // tells UI to not allow any dragon cards to be flipped
    @Override
    public boolean isDragonCardsFreeze() {
        return true;
    }

    // DragonCards are not allowed to be flipped in this state
    @Override
    public void nextDragonCardClick(DragonCard dragonCard) {
    }

    // starts the game when Player presses Start Game button
    @Override
    public void nextActionBtn() {
        GameEngine.getInstance().nextTurn();
        GameEngine.getInstance().startGame();
    }

    // ready to start playing the game
    @Override
    public boolean startPlay() {
        return true;
    }
}
