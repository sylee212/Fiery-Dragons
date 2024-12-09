package com.fierydragons.game.dragoncard;

import com.fierydragons.game.GameEngine;
import com.fierydragons.game.Location;
import com.fierydragons.game.animal.Animal;
import com.fierydragons.game.animal.AnimalFactory;
import com.fierydragons.game.board.Board;
import com.fierydragons.game.board.Cave;
import com.fierydragons.game.board.VolcanoCard;
import com.fierydragons.game.player.Player;
import org.json.JSONObject;
/*
    Author: Tharani Prathaban
    Co-authored: Arvind Siva
 */
/**
 * WildDragonCard that allows a Player to move forward without checking for matching animals on the dragon card and the land
 */

public class WildDragonCard extends DragonCard {

    public WildDragonCard(Animal animal, int moves) {
        super(animal, moves);
    }

    @Override
    public void movePlayer(Player player, Board board) {
        // get current Player and VolcanoCard
        Location currentPlayerLoc = player.getLocation();
        VolcanoCard currentVolcanoCard = board.getVolcanoCards().get(currentPlayerLoc.getVolcanoCardIndex());

        // get index of plot and volcanoCard
        int plotIndex = currentPlayerLoc.getPlotIndex();
        int volcanoCardIndex = currentPlayerLoc.getVolcanoCardIndex();
        int amountOfMoves = this.getMoves();

        // the Player is still in their cave
        if (currentVolcanoCard.isHasCave() && currentPlayerLoc.isCave()) {
            Cave currentCave = currentVolcanoCard.getCaveAtIndex(plotIndex);
            if (amountOfMoves > 0) {    // move the Player out of the cave in clockwise
                amountOfMoves--;

                // move the Player
                while (amountOfMoves > 0) {
                    amountOfMoves--;
                    plotIndex++;

                    // if move into next VolcanoCard update VolcanoCardIndex
                    if (plotIndex == board.getVolcanoCardByIndex(volcanoCardIndex).getNumOfPlots()) {
                        plotIndex = 0;
                        volcanoCardIndex++;
                        volcanoCardIndex %= board.getNumOfVolcanoCards();
                    }
                }

                if (board.getVolcanoTileByIndex(volcanoCardIndex, plotIndex).hasPlayer()) {
                    // allow Player to pass to next Player because the location to move to has a Player
                    GameEngine.getInstance().setWaitNextTurnState();
                } else { // allow Player to flip next dragon card or skip
                    // move player to next location
                    // remove player from current location
                    board.getVolcanoTileByIndex(volcanoCardIndex, plotIndex).setPlayer(player);
                    currentCave.setPlayer(null);
                    player.setLocation(new Location(volcanoCardIndex, plotIndex, false), this.getMoves());
                }

            } else {
                // do not allow Player to move backwards and pass turn to next Player
                GameEngine.getInstance().setWaitNextTurnState();
            }
        } else { // player is not in their cave
            // dragon card animal matches volcano tile plot animal
            boolean enterCave = false; // enter cave or not

            // move the Player by number of moves clockwise
            while (amountOfMoves > 0) {
                // check if Player's own cave can be reached
                if (board.getVolcanoCardByIndex(volcanoCardIndex).isHasCave()) {
                    // get Cave and cave index
                    Cave nearbyCave = board.getVolcanoCardByIndex(volcanoCardIndex).getCave();
                    int caveIndex =  board.getVolcanoCardByIndex(volcanoCardIndex).getCaveIndex();

                    // the cave belongs to the player
                    if (caveIndex == plotIndex && nearbyCave.getPlayerID() == player.getId()) {
                        Location lastPlayerLoc = player.getLastLocation();

                        if(!lastPlayerLoc.isCave() && player.getMoves() > 1) {
                            // player can enter the cave (But not exact moves maybe have extra moves)
                            amountOfMoves--;
                            enterCave = true;
                        }
                    }
                }

                // block Player from entering cave if there is no exact moves and break
                if (enterCave && amountOfMoves > 0) {
                    plotIndex = currentPlayerLoc.getPlotIndex();
                    volcanoCardIndex = currentPlayerLoc.getVolcanoCardIndex();
                    enterCave = false;
                    break;
                }

                // if not entering cave continue to move Player along volcano tile plos
                if (!enterCave) {
                    amountOfMoves--;
                    plotIndex++;

                    // if move into next VolcanoCard update VolcanoCardIndex
                    if (plotIndex == board.getVolcanoCardByIndex(volcanoCardIndex).getNumOfPlots()) {
                        plotIndex = 0;
                        volcanoCardIndex++;
                        volcanoCardIndex %= board.getNumOfVolcanoCards();
                    }
                }
            }

            if (enterCave) {    // end the game if player returns to their cave
                // update the Player's location to cave
                // remove player from current location
                board.getVolcanoCardByIndex(volcanoCardIndex).getCave().setPlayer(player);
                board.getVolcanoTileByIndex(currentPlayerLoc.getVolcanoCardIndex(), currentPlayerLoc.getPlotIndex()).setPlayer(null);
                player.setLocation(new Location(volcanoCardIndex, plotIndex, true), this.getMoves());

                GameEngine.getInstance().endGame();

            } else if (board.getVolcanoTileByIndex(volcanoCardIndex, plotIndex).hasPlayer()) {
                // pass to next player if there is a player at the location to move too
                GameEngine.getInstance().setWaitNextTurnState();
            } else { // allow player to flip next dragon card or skip
                // move player to new location
                // remove player from current location
                board.getVolcanoTileByIndex(volcanoCardIndex, plotIndex).setPlayer(player);
                board.getVolcanoTileByIndex(currentPlayerLoc.getVolcanoCardIndex(), currentPlayerLoc.getPlotIndex()).setPlayer(null);
                player.setLocation(new Location(volcanoCardIndex, plotIndex, false), this.getMoves());
            }
        }
    }

    // rebuild dragon card from json
    public static WildDragonCard fromJson(JSONObject jsonObject) {
        WildDragonCard dragonCard = new WildDragonCard(AnimalFactory.getInstance().getAnimalFromJson(jsonObject.getJSONObject("animal")),
                jsonObject.getInt("moves")
        );
        dragonCard.setOpen(jsonObject.getBoolean("isOpen"));
        return dragonCard;
    }
}