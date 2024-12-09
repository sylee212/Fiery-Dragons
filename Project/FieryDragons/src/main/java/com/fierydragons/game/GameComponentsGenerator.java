package com.fierydragons.game;

import com.fierydragons.game.animal.Animal;
import com.fierydragons.game.animal.AnimalFactory;
import com.fierydragons.game.animal.AnimalType;
import com.fierydragons.game.board.Cave;
import com.fierydragons.game.board.VolcanoCard;
import com.fierydragons.game.board.VolcanoTile;
import com.fierydragons.game.dragoncard.*;
import com.fierydragons.game.player.Player;
import com.fierydragons.game.player.PlayerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
/*
    Author: Tharani Prathaban
    Co-authored: Arvind Siva
 */
/**
 * Manager class responsible for generating combinations of volcano cards and dragon cards
 */
public class GameComponentsGenerator {

    // generate the volcano cards with caves and shuffle them
    public static ArrayList<VolcanoCard> generateVolcanoCards() {
        // List to store all combinations of land pieces
        ArrayList<VolcanoCard> volcanoCards = new ArrayList<>();

        AnimalFactory animalFactory = AnimalFactory.getInstance();

        VolcanoCard volcanoCard =  new VolcanoCard(1);
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCards.add(volcanoCard);

        volcanoCard =  new VolcanoCard(1);
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCards.add(volcanoCard);

        volcanoCard =  new VolcanoCard(2);
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCards.add(volcanoCard);

        volcanoCard =  new VolcanoCard(2);
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCards.add(volcanoCard);

        volcanoCard =  new VolcanoCard(2);
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCards.add(volcanoCard);

        volcanoCard =  new VolcanoCard(3);
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCards.add(volcanoCard);

        volcanoCard =  new VolcanoCard(3);
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCards.add(volcanoCard);

        volcanoCard =  new VolcanoCard(3);
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCards.add(volcanoCard);

        volcanoCard = new VolcanoCard(3);
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCards.add(volcanoCard);

        volcanoCard = new VolcanoCard(4);
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCard.getVolcanoTiles().add(new VolcanoTile(animalFactory.getRandomBoardAnimal()));
        volcanoCards.add(volcanoCard);

        Collections.shuffle(volcanoCards);  // randomly shuffle the volcano cards

        // get players and number of players
        ArrayList<Player> players = PlayerManager.getInstance().getPlayers();
        int numOfPlayers = PlayerManager.getInstance().getNumberOfPlayers();

        //  assigns caves to VolcanoTile randomly
        ArrayList<AnimalType> animalTypes = new ArrayList();
        animalTypes.add(AnimalType.BAT);
        animalTypes.add(AnimalType.SALAMANDER);
        animalTypes.add(AnimalType.BABY_DRAGON);
        animalTypes.add(AnimalType.SPIDER);
        Collections.shuffle(animalTypes);
        ArrayList<Integer> randomPositions = generateUniqueIntegersRandom(0, volcanoCards.size()-1, numOfPlayers);

        for (int i = 0; i < numOfPlayers; i++) {
            // create Cave based on Player chosen color
            Cave cave = new Cave(players.get(i).getId(), animalFactory.getAnimal(animalTypes.get(i)), players.get(i).getColorOption());
            cave.setPlayer(players.get(i));
            // randomly select cave position and volcano card
            int volcanoCardIndex = randomPositions.get(0);
            randomPositions.remove(0);
            int caveIndex = (int) ((Math.random() * (volcanoCards.get(volcanoCardIndex).getNumOfPlots() - 0)) + 0);;
            // assign cave to VolcanoTile
            volcanoCards.get(volcanoCardIndex).addCave(caveIndex, cave);
            // set Player current location to Cave
            players.get(i).setLocation(new Location(volcanoCardIndex, caveIndex,true), 0);
        }

        return volcanoCards;
    }

    private static ArrayList<Integer> generateUniqueIntegersRandom(int start, int end, int count) {
        // generates a list of random numbers used to randomly assign caves to volcano cards
        ArrayList<Integer> uniqueNumbers = new ArrayList<>();
        Random random = new Random();

        while (uniqueNumbers.size() < count) {
            int number = random.nextInt(end - start + 1) + start;
            if (!uniqueNumbers.contains(number)) {
                uniqueNumbers.add(number);
            }
        }

        return uniqueNumbers;
    }

    // generate dragon cards and shuffle them
    public static ArrayList<DragonCard> generateDragonCards() {
        // List to store all chit cards
        ArrayList<DragonCard> dragonCards = new ArrayList<>();

        AnimalFactory animalFactory = AnimalFactory.getInstance();

        // load all the default dragon cards
        for(int i = 1; i <= 3; i++) {
            dragonCards.add(new ForwardDragonCard(animalFactory.getAnimal(AnimalType.SALAMANDER), i));
        }

        for(int i = 1; i <= 3; i++) {
            dragonCards.add(new ForwardDragonCard(animalFactory.getAnimal(AnimalType.BAT), i));
        }

        for(int i = 1; i <= 3; i++) {
            dragonCards.add(new ForwardDragonCard(animalFactory.getAnimal(AnimalType.BABY_DRAGON), i));
        }

        for(int i = 1; i <= 3; i++) {
            dragonCards.add(new ForwardDragonCard(animalFactory.getAnimal(AnimalType.SPIDER), i));
        }

        for(int i=1; i <= 2; i++) {
            dragonCards.add(new BackwardDragonCard(animalFactory.getAnimal(AnimalType.PIRATE_DRAGON), -i));
            dragonCards.add(new BackwardDragonCard(animalFactory.getAnimal(AnimalType.PIRATE_DRAGON), -i));
        }

        // add dragon cards that swap with the nearest player
        dragonCards.add(new SwapDragonCard(animalFactory.getAnimal(AnimalType.SPIDER)));
        dragonCards.add(new SwapDragonCard(animalFactory.getAnimal(AnimalType.SPIDER)));

        // add wild cards to the collection
        dragonCards.add(new WildDragonCard(animalFactory.getAnimal(AnimalType.WILD), 1));
        dragonCards.add(new WildDragonCard(animalFactory.getAnimal(AnimalType.WILD), 2));

        Collections.shuffle(dragonCards);   // randomly shuffle the dragon cards
        return dragonCards;
    }
}
