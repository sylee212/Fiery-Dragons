package com.fierydragons.game.dragoncard;

import com.fierydragons.game.GameEngine;
import com.fierydragons.game.Location;
import com.fierydragons.game.animal.Animal;
import com.fierydragons.game.animal.AnimalFactory;
import com.fierydragons.game.board.Board;
import com.fierydragons.game.board.VolcanoCard;
import com.fierydragons.game.player.Player;
import com.fierydragons.game.player.PlayerManager;
import org.json.JSONObject;

import java.util.ArrayList;
/*
    Author: Lee Sing Yuan
    Co-authored: Arvind Siva
 */
public class SwapDragonCard extends DragonCard{

    public SwapDragonCard(Animal animal) {
        super(animal, 0);
    }

    /**
     * This function will dictate the movement of the player
     *
     * Idea:
     *      1) get the current player location or the player that called this dragon card
     *      2) this player must not be in a cave to teleport
     *      3) loop through all the players and calculate all the distance between each of them to the current player
     *      4) keep track of the player with the smallest difference in distance
     *      5) if there is a player with the smallest difference, swap the current player and the closest player positions
     *      6) inform state
     *
     * @param player the player instance
     * @param board the board itself
     */
    @Override
    public void movePlayer(Player player, Board board) {
        ArrayList<Player> players = PlayerManager.getInstance().getPlayers();

        Player nearestPlayer = null;
        int shortestDist = Integer.MAX_VALUE;
        boolean isCurrentPlayerForward = false;

        Location currentPlayerLoc = player.getLocation();
        VolcanoCard currentVolcanoCard = board.getVolcanoCardByIndex(currentPlayerLoc.getVolcanoCardIndex());

        if (!currentPlayerLoc.isCave()) {
            for (Player nearbyPlayer: players) {
                Location nearbyPlayerLoc = nearbyPlayer.getLocation();

                if (nearbyPlayer.getId() != player.getId() && !nearbyPlayerLoc.isCave()) {

                    if (nearbyPlayerLoc.getVolcanoCardIndex() == currentPlayerLoc.getVolcanoCardIndex()) {
                        int dist = Math.abs(nearbyPlayerLoc.getPlotIndex() - currentPlayerLoc.getPlotIndex());
                        if (dist < shortestDist) {
                            shortestDist = dist;
                            nearestPlayer = nearbyPlayer;
                        }
                    } else {
                        int numOfVolcanoCards = board.getNumOfVolcanoCards();
                        int clockwiseDist = 0;
                        int counterClockWiseDist = 0;

                        int i = currentPlayerLoc.getVolcanoCardIndex();
                        int j = currentPlayerLoc.getPlotIndex();
                        clockwiseDist += currentVolcanoCard.getNumOfPlots() - j - 1;

                        while (i != nearbyPlayerLoc.getVolcanoCardIndex()) {
                            i += 1;
                            i %= numOfVolcanoCards;

                            if (i == nearbyPlayerLoc.getVolcanoCardIndex()){
                                clockwiseDist += nearbyPlayerLoc.getPlotIndex()+1;
                            } else {
                                clockwiseDist += board.getVolcanoCardByIndex(i).getNumOfPlots();
                            }
                        }

                        i = nearbyPlayerLoc.getVolcanoCardIndex();
                        j = nearbyPlayerLoc.getPlotIndex();
                        counterClockWiseDist += board.getVolcanoCardByIndex(i).getNumOfPlots() - j - 1;

                        while (i != currentPlayerLoc.getVolcanoCardIndex()) {
                            i += 1;
                            i %= numOfVolcanoCards;

                            if (i == currentPlayerLoc.getVolcanoCardIndex()){
                                counterClockWiseDist += currentPlayerLoc.getPlotIndex()+1;
                            } else {
                                counterClockWiseDist += board.getVolcanoCardByIndex(i).getNumOfPlots();
                            }
                        }

                        int dist = Math.min(clockwiseDist, counterClockWiseDist);
                        if (dist < shortestDist) {
                            shortestDist = dist;
                            nearestPlayer = nearbyPlayer;

                            isCurrentPlayerForward = clockwiseDist <= counterClockWiseDist;
                        }
                    }
                }
            }
        }

        if (nearestPlayer != null) {
            Location nearestPlayerLoc = nearestPlayer.getLocation();
            board.getVolcanoTileByIndex(currentPlayerLoc.getVolcanoCardIndex(),
                    currentPlayerLoc.getPlotIndex()).setPlayer(nearestPlayer);
            board.getVolcanoTileByIndex(nearestPlayerLoc.getVolcanoCardIndex(),
                    nearestPlayerLoc.getPlotIndex()).setPlayer(player);

            int numOfVolcanoTiles = board.getNumOfVolcanoTiles();
            if (isCurrentPlayerForward) {
                player.setLocationSwap(nearestPlayerLoc, shortestDist, numOfVolcanoTiles);
                nearestPlayer.setLocationSwap(currentPlayerLoc, shortestDist * -1, numOfVolcanoTiles);
            } else {
                player.setLocationSwap(nearestPlayerLoc, shortestDist * -1, numOfVolcanoTiles);
                nearestPlayer.setLocationSwap(currentPlayerLoc, shortestDist, numOfVolcanoTiles);
            }
        }

        // current player in cave pass to next player
        GameEngine.getInstance().setWaitNextTurnState();
    }

    // rebuild dragon card from json
    public static SwapDragonCard fromJson(JSONObject jsonObject) {
        SwapDragonCard dragonCard = new SwapDragonCard(
                AnimalFactory.getInstance().getAnimalFromJson(jsonObject.getJSONObject("animal"))
        );
        dragonCard.setOpen(jsonObject.getBoolean("isOpen"));
        return dragonCard;
    }
}
