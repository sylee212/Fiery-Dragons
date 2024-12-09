package com.fierydragons.game.dragoncard;

import com.fierydragons.game.GameEngine;
import com.fierydragons.game.Location;
import com.fierydragons.game.animal.Animal;
import com.fierydragons.game.animal.AnimalFactory;
import com.fierydragons.game.board.Board;
import com.fierydragons.game.board.Cave;
import com.fierydragons.game.player.Player;
import org.json.JSONObject;
/*
    Author: Arvind Siva
    Co-authored:
 */
/**
 * DragonCard that causes Player to move backwards when flipped
 */
public class BackwardDragonCard extends DragonCard {

    // constructor
    public BackwardDragonCard(Animal animal, int moves) {
        super(animal, moves);
    }

    // move the player based on game rules
    @Override
    public void movePlayer(Player player, Board board) {
        // get current Player and VolcanoCard
        Location currentPlayerLoc = player.getLocation();

        // get index of plot and volcanoCard
        int plotIndex = currentPlayerLoc.getPlotIndex();
        int volcanoCardIndex = currentPlayerLoc.getVolcanoCardIndex();
        int amountOfMoves = this.getMoves();

        // dragon card animal is a dragon pirate
        if (player.getLastLocation() != null) {
            while (amountOfMoves < 0) { // move the Player backwards
                amountOfMoves++;
                plotIndex--;

                // if move into next VolcanoCard update VolcanoCardIndex
                if (plotIndex == -1) {
                    volcanoCardIndex--;
                    if (volcanoCardIndex == -1) {
                        volcanoCardIndex = board.getNumOfVolcanoCards() - 1;
                    }

                    plotIndex = board.getVolcanoCardByIndex(volcanoCardIndex).getNumOfPlots() - 1;
                }

                if (player.getMoves() <= 1) {
                    volcanoCardIndex = currentPlayerLoc.getVolcanoCardIndex();
                    plotIndex = currentPlayerLoc.getPlotIndex();
                    break;
                }

                if (board.getCaveByIndex(volcanoCardIndex, plotIndex) != null) {
                    Cave adjacentCave = board.getVolcanoCardByIndex(volcanoCardIndex).getCaveAtIndex(plotIndex);

                    if (adjacentCave.getPlayerID() == player.getId() && amountOfMoves < 0) {
                        volcanoCardIndex = currentPlayerLoc.getVolcanoCardIndex();
                        plotIndex = currentPlayerLoc.getPlotIndex();
                        break;
                    }
                }
            }

            if (board.getVolcanoTileByIndex(volcanoCardIndex, plotIndex).hasPlayer()) {
                // if location to move to has a player pass turn to next player
                GameEngine.getInstance().setWaitNextTurnState();
            } else { // allow Player to flip next dragon card or skip
                // move player to next location
                // remove player from current location
                board.getVolcanoTileByIndex(volcanoCardIndex, plotIndex).setPlayer(player);
                board.getVolcanoTileByIndex(currentPlayerLoc.getVolcanoCardIndex(), currentPlayerLoc.getPlotIndex()).setPlayer(null);
                player.setLocation(new Location(volcanoCardIndex, plotIndex, false), this.getMoves());
            }

        } else { // dragon card was not dragon pirate and animal didn't match
            // pass turn to next Player
            GameEngine.getInstance().setWaitNextTurnState();
        }
    }

    // rebuild dragon card from json
    public static BackwardDragonCard fromJson(JSONObject jsonObject) {
        BackwardDragonCard dragonCard = new BackwardDragonCard(
                AnimalFactory.getInstance().getAnimalFromJson(jsonObject.getJSONObject("animal")),
                jsonObject.getInt("moves")
        );
        dragonCard.setOpen(jsonObject.getBoolean("isOpen"));
        return dragonCard;
    }
}

