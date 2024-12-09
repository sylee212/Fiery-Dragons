package com.fierydragons.game.animal;

import org.json.JSONObject;

import java.util.*;

/*
    Author: Arvind Siva
    Co-authored: Lee Sing Yuan, Tharani Prathaban
 */
/**
    Stores instance of Animals to be shared by DragonCard, VolcanoTilePlot and Cave
 */
public class AnimalFactory {
    // stores single instance of AnimalFactory during runtime
    private static AnimalFactory instance = null;

    // dictionary with key-value pair (AnimalType, Animal)
    private Map<AnimalType, Animal> animals;

    // constructor
    private AnimalFactory() {
        this.animals = new HashMap<>();
        // initialize all available Animals
        animals.put(AnimalType.BAT, new Bat());
        animals.put(AnimalType.BABY_DRAGON, new BabyDragon());
        animals.put(AnimalType.PIRATE_DRAGON, new PirateDragon());
        animals.put(AnimalType.SALAMANDER, new Salamander());
        animals.put(AnimalType.SPIDER, new Spider());
        animals.put(AnimalType.WILD, new Wild());
    }

    // returns instance of AnimalFactory
    public static AnimalFactory getInstance() {
        if (AnimalFactory.instance == null)
            AnimalFactory.instance = new AnimalFactory();
        return AnimalFactory.instance;
    }

    // get the Animal by AnimalType
    public Animal getAnimal(AnimalType animalType) {
        return this.animals.get(animalType);
    }

    // get the Animal using json representation
    public Animal getAnimalFromJson(JSONObject jsonObject) {
        String animalType = jsonObject.getString("type");
        Animal retAnimal = null;

        switch(animalType) {
            case "BabyDragon":
                retAnimal = this.getAnimal(AnimalType.BABY_DRAGON);
                break;
            case "Bat":
                retAnimal = this.getAnimal(AnimalType.BAT);
                break;
            case "PirateDragon":
                retAnimal = this.getAnimal(AnimalType.PIRATE_DRAGON);
                break;
            case "Salamander":
                retAnimal = this.getAnimal(AnimalType.SALAMANDER);
                break;
            case "Spider":
                retAnimal = this.getAnimal(AnimalType.SPIDER);
                break;
            case "Wild":
                retAnimal = this.getAnimal(AnimalType.WILD);
                break;
            default:
                retAnimal = null;
        }

        return retAnimal;
    }

    public Animal getRandomBoardAnimal() {
        // gets a random animal from Animal Factory
        Random random = new Random();
        int randomInt = random.nextInt(4);

        ArrayList<AnimalType> animalTypes = new ArrayList();
        animalTypes.add(AnimalType.BAT);
        animalTypes.add(AnimalType.SALAMANDER);
        animalTypes.add(AnimalType.BABY_DRAGON);
        animalTypes.add(AnimalType.SPIDER);
        Collections.shuffle(animalTypes);

        return this.getAnimal(animalTypes.get(randomInt));
    }

}

