package com.fierydragons.game.state;


import com.fierydragons.game.GameEngine;
import com.fierydragons.game.dragoncard.DragonCard;
import com.fierydragons.game.player.PlayerManager;

/*
    Author: Arvind Siva
    Co-authored: Mohamed Areeb Ilham Riluwan
 */
/*
    PlayState is used when the Player is playing their turn
 */
public class PlayState extends State {
    // update GameEngine with flipped dragonCard to decide on the Player move
    @Override
    public void nextDragonCardClick(DragonCard dragonCard) {
        GameEngine.getInstance().playTurn(dragonCard);
    }

    // skip player button gets activated from the 2nd DragonCard onwards in a single turn
    // skip player will be used to skip to next player turn if current player doesn't want to flip DragonCard
    @Override
    public void nextActionBtn() {
        this.setAllowSkip(false);
        GameEngine.getInstance().nextTurn();
        GameEngine.getInstance().setState(this);
    }

    // allow player to peek if they can
    @Override
    public void nextPeekBtn() {
        GameEngine.getInstance().setState(new PeekState(GameEngine.getInstance().getState()));
    }

    // allow to display peek button
    @Override
    public boolean isAllowPeek() {
        return true;
    }
}
