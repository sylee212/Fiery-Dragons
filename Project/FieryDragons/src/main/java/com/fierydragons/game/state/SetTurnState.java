package com.fierydragons.game.state;


import com.fierydragons.game.GameEngine;
import com.fierydragons.game.dragoncard.DragonCard;
/*
    Author: Arvind Siva
    Co-authored: Mohamed Areeb Ilham Riluwan
 */
/*
    SetTurnState is used when Players are flipping DragonCards to decide who starts first.
 */
public class SetTurnState extends State {
    // update the flipped DragonCard details in GameEngine
    @Override
    public void nextDragonCardClick(DragonCard dragonCard) {
        GameEngine.getInstance().setPlayerTurns(dragonCard);
    }

    // no button interaction in this state
    @Override
    public void nextActionBtn() {

    }
}
