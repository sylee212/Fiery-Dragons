package com.fierydragons.game.display;

import com.fierydragons.game.GameEngine;
import com.fierydragons.game.board.Board;
import com.fierydragons.game.player.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;

import java.util.HashMap;
/*
    Author: Arvind Siva
    Co-authored: Tharani Prathaban
 */
/*
    DisplayBoard is used to display the UI of the board containing Volcano Tiles, Caves and Players
 */
public class DisplayBoard implements InnerDisplay{
    // center coordinates to center the board on screen
    private static final double CENTER_X = Screen.getPrimary().getVisualBounds().getWidth()/2;
    private static final double CENTER_Y = Screen.getPrimary().getVisualBounds().getHeight()/2;
    // distance of player in cave from screen centre
    private static final double PLAYER_CAVE_RADIUS = 295;
    // distance of cave from screen centre
    private static final double CAVE_RADIUS = 255;
    // distance of volcano tile from screen centre
    private static final double VOLCANO_TILE_RADIUS = 210;
    // distance of player on volcano tile plot from screen centre
    private static final double PLAYER_VOLCANO_TILE_RADIUS = 160;
    private static final double IMAGE_SIZE = 35; // Size of each image view
    private HashMap<Integer, Circle> playerCircles; // Circle represents Player tokens
    private Pane pane;  // Pane object holds VolcanoTiles and Players

    // constructor
    public DisplayBoard() {
        this.pane = new Pane();
        this.playerCircles = new HashMap<>();
        this.setUp();
    }

    /*
     set up the board at the beginning
     Acknowledgement: ChatGPT, OpenAI, 2024
                     [The code for displaying images in a circle was adapted from ChatGPT for
                      use in FieryDragons to display the VolcanoTiles in a circle]
     */
    private void setUp() {
        GameEngine gameEngine = GameEngine.getInstance();
        Board board = gameEngine.getBoard();

        // calculate angle step to move for each VolcanoTilePlot on the Board
        int numOfVolcanoCards = board.getNumOfVolcanoCards();
        double angleStep = 360.0 / (board.getNumOfVolcanoTiles());
        int angleCounter = 0;

        // loop through all plots in the board
        for (int i = 0; i < numOfVolcanoCards; i++) {
            for (int j = 0; j < board.getVolcanoCardByIndex(i).getNumOfPlots(); j++) {
                // calculate the angle for each VolcanoTilePlot, Cave
                double angle = Math.toRadians(angleCounter * angleStep);
                angleCounter++;

                // calculate the position of the current VolcanoTilePlot
                double x = CENTER_X + VOLCANO_TILE_RADIUS * Math.cos(angle);
                double y = CENTER_Y + VOLCANO_TILE_RADIUS * Math.sin(angle);

                // create an ImageView for each volcanoTilePlot
                ImageView imageView = board.getVolcanoTileByIndex(i, j).getAnimal().getImageView(IMAGE_SIZE, IMAGE_SIZE);
                // set the position of the ImageView
                imageView.setX(x - imageView.getFitWidth() / 2);
                imageView.setY(y - imageView.getFitHeight() / 2);

                Image image2 = new Image(getClass().getResourceAsStream("/assets/landpiece2.png"));
                ImageView imageView2 = new ImageView(image2);
                imageView2.setFitWidth(IMAGE_SIZE);
                imageView2.setFitHeight(IMAGE_SIZE);
                imageView2.setX(x - imageView2.getFitWidth() / 2);
                imageView2.setY(y - imageView2.getFitHeight() / 2);

                //cCreate a StackPane to contain the ImageView
                StackPane stackPane = new StackPane();
                // use color border css to differentiate VolcanoTiles
                if (i % 2 == 0)
                    stackPane.getStyleClass().add("volcano-tile-even");
                else
                    stackPane.getStyleClass().add("volcano-tile-odd");

                // add the ImageView to the StackPane
                stackPane.getChildren().addAll(imageView2, imageView);
                stackPane.setLayoutX(imageView.getX());
                stackPane.setLayoutY(imageView.getY());

                // add the ImageView to the pane
                pane.getChildren().add(stackPane);

                // display the Circle of the Player if there is a Player here
                if (board.getVolcanoTileByIndex(i, j).hasPlayer()) {
                    Player player = board.getVolcanoTileByIndex(i, j).getPlayer();

                    // calculate the position of Player at the VolcanoTilePlot
                    x = CENTER_X + PLAYER_VOLCANO_TILE_RADIUS * Math.cos(angle);
                    y = CENTER_Y + PLAYER_VOLCANO_TILE_RADIUS * Math.sin(angle);

                    // create a circle with a specified radius
                    Circle circle = new Circle(10); // set the desired radius

                    // set the color of the circle representing Player
                    circle.setFill(player.getColor());
                    circle.setCenterX(x);
                    circle.setCenterY(y);
                    // add circle to pane
                    pane.getChildren().add(circle);
                    // keep track of player positions
                    playerCircles.put(player.getId(), circle);
                }

                // check if volcanoTile has cave
                if (board.getVolcanoCardByIndex(i).isHasCave()) {
                    if (board.getCaveByIndex(i, j) != null) {
                        // calculate the position of the Cave
                        x = CENTER_X + CAVE_RADIUS * Math.cos(angle);
                        y = CENTER_Y + CAVE_RADIUS * Math.sin(angle);

                        // create ImageView for Cave
                        imageView = board.getCaveByIndex(i, j).getAnimal().getImageView(IMAGE_SIZE, IMAGE_SIZE);
                        // set the position of the ImageView
                        imageView.setX(x - imageView.getFitWidth() / 2);
                        imageView.setY(y - imageView.getFitHeight() / 2);

                        stackPane = new StackPane();
                        // set the background color of the StackPane
                        String caveColor = board.getCaveByIndex(i, j).getColor().toString();
                        System.out.println(caveColor);
                        stackPane.setStyle("-fx-border-color: #" + caveColor.substring(2) + "; -fx-border-width: 5; -fx-border-radius:90px;");

                        stackPane.setLayoutX(imageView.getX());
                        stackPane.setLayoutY(imageView.getY());
                        // add the ImageView to the StackPane
                        stackPane.getChildren().add(imageView);

                        // add the ImageView to the pane
                        pane.getChildren().add(stackPane);

                        // check if Cave has a Player
                        if (board.getCaveByIndex(i, j).hasPlayer()) {
                            Player player = board.getCaveByIndex(i, j).getPlayer();

                            // calculate the position of Player at the Cave
                            x = CENTER_X + PLAYER_CAVE_RADIUS * Math.cos(angle);
                            y = CENTER_Y + PLAYER_CAVE_RADIUS * Math.sin(angle);

                            // create a circle with a specified radius
                            Circle circle = new Circle(10); // set the desired radius

                            // set the color of the circle representing Player
                            circle.setFill(player.getColor());
                            circle.setCenterX(x);
                            circle.setCenterY(y);
                            // add circle to pane
                            pane.getChildren().add(circle);
                            // keep track of player positions
                            playerCircles.put(player.getId(), circle);
                        }
                    }
                }
            }
        }
    }

    /*
    update the board after every user interaction
    Acknowledgement: ChatGPT, OpenAI, 2024
                     [The code for displaying images in a circle  was adapted from ChatGPT for
                      use in FieryDragons to display the VolcanoTiles in a circle]
     */
    public void update() {
        // get GameEngine and Board
        GameEngine gameEngine = GameEngine.getInstance();
        Board board = gameEngine.getBoard();

        // calculate angle step to move for each Player on the Board
        int numOfVolcanoCards = board.getNumOfVolcanoCards();
        double angleStep = 360.0 / (board.getNumOfVolcanoTiles());
        int angleCounter = 0;

        // loop through all plots in the board
        for (int i = 0; i < numOfVolcanoCards; i++) {
            for (int j = 0; j < board.getVolcanoCardByIndex(i).getNumOfPlots(); j++) {
                // calculate the angle to position a Player at this location (i,j)
                double angle = Math.toRadians(angleCounter * angleStep);
                angleCounter++;
                double x;
                double y;

                // display the Circle of the Player if there is a Player here
                if (board.getVolcanoTileByIndex(i, j).hasPlayer()) {
                    Player player = board.getVolcanoTileByIndex(i, j).getPlayer();

                    // calculate the position of Player at the VolcanoTilePlot
                    x = CENTER_X + PLAYER_VOLCANO_TILE_RADIUS * Math.cos(angle);
                    y = CENTER_Y + PLAYER_VOLCANO_TILE_RADIUS * Math.sin(angle);

                    // get the Circle representing the Player
                    Circle circle = this.playerCircles.get(player.getId());

                    // set to current position
                    circle.setCenterX(x);
                    circle.setCenterY(y);
                }

                // check if VolcanoTile has cave
                if (board.getVolcanoCardByIndex(i).isHasCave()) {
                    if (board.getCaveByIndex(i, j) != null) {
                        if (board.getCaveByIndex(i, j).hasPlayer()) {
                            // display the Circle of the Player if there is a Player here
                            Player player = board.getCaveByIndex(i, j).getPlayer();

                            // calculate the position of Player at the Cave
                            x = CENTER_X + PLAYER_CAVE_RADIUS * Math.cos(angle);
                            y = CENTER_Y + PLAYER_CAVE_RADIUS * Math.sin(angle);

                            // get the Circle representing the Player
                            Circle circle = this.playerCircles.get(player.getId());

                            // set to current position at Cave
                            circle.setCenterX(x);
                            circle.setCenterY(y);
                        }
                    }
                }
            }
        }
    }

    // getter for Pane
    public Pane getDisplay() {
        return pane;
    }
}

