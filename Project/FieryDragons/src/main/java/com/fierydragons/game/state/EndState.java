package com.fierydragons.game.state;


import com.fierydragons.game.dragoncard.DragonCard;
/*
    Author: Arvind Siva
    Co-authored: Mohamed Areeb Ilham Riluwan
 */
/*
    EndState is used when the game has ended (a Player won by returning to cave)
    Freezes the UI and announce the winner
 */
public class EndState extends State{

    // DragonCards are not allowed to be flipped in this state
    @Override
    public void nextDragonCardClick(DragonCard dragonCard) {

    }

    // no button interaction in this state
    @Override
    public void nextActionBtn() {

    }

    // Checks if game is over
    @Override
    public boolean isGameOver() {
        return true;
    }
}
