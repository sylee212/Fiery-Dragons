package com.fierydragons.game.display;

import com.fierydragons.game.GameEngine;
import com.fierydragons.game.state.State;
import com.fierydragons.game.dragoncard.DragonCard;
import com.fierydragons.game.dragoncard.DragonCardManager;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
/*
    Author: Arvind Siva
    Co-authored: Tharani Prathaban
 */
/**
    DisplayDragonCards displays UI for the DragonCards in teh center of the Board
 */
public class DisplayDragonCards implements InnerDisplay{
    private final static double CARD_WIDTH = 30;    // width of dragon card
    private final static double CARD_HEIGHT = 35;   // height of dragon card

    // center coordinates to center the dragon cards on screen
    private static final double CENTER_X = Screen.getPrimary().getVisualBounds().getWidth()/2;
    private static final double CENTER_Y = Screen.getPrimary().getVisualBounds().getHeight()/2;
    private int numOfDragonCards;   // number of DragonCards
    private GridPane gridPane;   // gridPane holds all ImageView of dragon cards and displays it


    // constructor
    public DisplayDragonCards() {
        GameEngine gameEngine = GameEngine.getInstance();
        this.numOfDragonCards = gameEngine.getDragonCardManager().getNumOfDragonCards();
        this.gridPane = new GridPane();
        this.setup();
    }

    /*
     set up the dragon cards at the beginning
     Acknowledgement: ChatGPT, OpenAI, 2024
                     [The code for displaying dragon cards in a square grid was adapted from
                      ChatGPT]
     */
    private void setup() {
        // size of grid
        int gridRows = 4;
        int gridCols = 5;

        gridPane.setHgap(10); // Horizontal gap between columns
        gridPane.setVgap(10); // Vertical gap between rows

        // get dragonCardManager that holds dragon cards
        DragonCardManager dragonCardManager = GameEngine.getInstance().getDragonCardManager();

        // add ImageViews of dragon cards to the gridPane
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                // calculate the index of the current dragon card
                int index = row * gridCols + col;
                DragonCard currentDragonCard = dragonCardManager.getDragonCardByIndex(index);

                // get the image views
                ImageView imageView = currentDragonCard.getImageView(CARD_WIDTH,CARD_HEIGHT);

                // add the ImageView to the GridPane at the specified row and column
                gridPane.add(imageView, col, row);
            }
        }

        // centre the gridPane on screen
        double gridPaneWidth = gridCols * (CARD_WIDTH + gridPane.getHgap()) - gridPane.getHgap();
        double gridPaneHeight = gridRows * (CARD_HEIGHT + gridPane.getVgap()) - gridPane.getVgap();
        gridPane.setLayoutX(CENTER_X - gridPaneWidth / 2);
        gridPane.setLayoutY(CENTER_Y - gridPaneHeight / 2);
        // add css styling to gridPane
        gridPane.getStyleClass().add("grid-pane");
    }

    /*
     update the dragon cards after every user interaction
     Acknowledgement: ChatGPT, OpenAI, 2024
                     [The code for displaying dragon cards in a square grid was adapted from
                      ChatGPT]
     */
    public void update() {
        GameEngine gameEngine = GameEngine.getInstance();
        State state = gameEngine.getState();

        // size of grid
        int gridRows = 4;
        int gridCols = 5;


        gridPane.setHgap(10); // Horizontal gap between columns
        gridPane.setVgap(10); // Vertical gap between rows
        // clear imageViews to reset them with new ones to update current dragon card status
        gridPane.getChildren().clear();

        // get dragonCardManager that holds dragon cards
        DragonCardManager dragonCardManager = GameEngine.getInstance().getDragonCardManager();

        // add ImageViews of dragon cards to the gridPane
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                // calculate the index of the current dragon card
                int index = row * gridCols + col;
                DragonCard currentDragonCard = dragonCardManager.getDragonCardByIndex(index);

                // get the image views
                ImageView imageView = state.isDragonCardsPeek() ?
                        currentDragonCard.getAnimal().getImageView(CARD_WIDTH,CARD_HEIGHT) :
                        currentDragonCard.getImageView(CARD_WIDTH,CARD_HEIGHT);

                // don't allow any more flips on dragon cards
                if (state.isDragonCardsFreeze()) {
                    imageView.setOnMouseClicked(null);
                } else if (!currentDragonCard.isOpen()) {
                    // update GameEngine with currently flipped DragonCard and flip the DragonCard open
                    imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        currentDragonCard.setOpen(true);
                        state.nextDragonCardClick(currentDragonCard);
                        imageView.setImage(currentDragonCard.getAnimal().getImage());
                        DisplayMain.getInstance().update();

                        event.consume();
                    });
                }

                // add imageView to stackPane
                StackPane stackPane = new StackPane();
                // add css to stackPane
                stackPane.getStyleClass().add("dragon-card-border");
                stackPane.setLayoutX(imageView.getX());
                stackPane.setLayoutY(imageView.getY());
                stackPane.getChildren().add(imageView);

                if (currentDragonCard == dragonCardManager.getLastDragonCard()) {
                    // stackPane is used for last flipped dragon card to better visualize with a pink border
                    gridPane.add(stackPane, col, row);
                } else {
                    // not last flipped dragon card render as ImageView
                    gridPane.add(imageView, col, row);
                }
            }
        }
    }

    // getter for gridPane
    public Pane getDisplay() {
        return this.gridPane;
    }

}
