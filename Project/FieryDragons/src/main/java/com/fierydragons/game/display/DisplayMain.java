package com.fierydragons.game.display;

import com.fierydragons.game.GameEngine;
import com.fierydragons.game.player.PlayerManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/*
    Author: Arvind Siva
    Co-authored:
 */
/*
    DisplayMain holds all the inner displays representing the game components and render them
 */

public class DisplayMain {
    private static DisplayMain instance = null; // maintain single instance of DisplayMain

    private Scene scene; // scene used by JavaFX to render UI on screen
    private VBox layout;
    private int playerCount; // number of players playing
    private int currentPlayerIndex; // current Player being prompted for info
    private Stage primaryStage; // used by JavaFx to render the UI

    // inner displays for game components
    private InnerDisplay boardDisplay;
    private InnerDisplay dragonCardsDisplay;
    private InnerDisplay detailsDisplay;

    // list of colors to allow player to select
    private List<ColorOption> colorList = new ArrayList<>();

    // constructor made private to ensure a single instance
    private DisplayMain() {
    }

    // get instance of DisplayMain
    public static DisplayMain getInstance() {
        if (instance == null)
            instance = new DisplayMain();
        return instance;
    }

    // start used by JavaFX to start rendering UI
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        // initialize color list for player to select
        // adapted from ChatGPT, OpenAI, 2024
        colorList.add(new ColorOption("Red", Color.RED));
        colorList.add(new ColorOption("Green", Color.GREEN));
        colorList.add(new ColorOption("Blue", Color.BLUE));
        colorList.add(new ColorOption("Yellow", Color.YELLOW));

        // initialize scene
        scene = new Scene(layout, 400, 400);
        // add css
        scene.getStylesheets().add(getClass().getResource("/com/fierydragons/game/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Fiery Dragons");
        primaryStage.show();

        startGame();
    }

    public void restartGame() {
        layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        // initialize color list for player to select
        // adapted from ChatGPT, OpenAI, 2024
        colorList.clear();
        colorList.add(new ColorOption("Red", Color.RED));
        colorList.add(new ColorOption("Green", Color.GREEN));
        colorList.add(new ColorOption("Blue", Color.BLUE));
        colorList.add(new ColorOption("Yellow", Color.YELLOW));

        // initialize scene
        scene = new Scene(layout, 400, 400);
        // add css
        scene.getStylesheets().add(getClass().getResource("/com/fierydragons/game/styles.css").toExternalForm());


        primaryStage.setScene(scene);
        primaryStage.setTitle("Fiery Dragons");
        primaryStage.show();

        startGame();
    }

    /*
        displays Bootstrap style form asking how many Players are playing
        Acknowledgement: ChatGPT, OpenAI, 2024
                     [The code for displaying the form to ask user the number of players playing
                      the game]
     */
    public void startGame() {
        // ask user how many players playing, with input field and btn to submit
        Label promptLabel = new Label("How many players are there?");
        layout.getChildren().addAll(promptLabel, new TextField(), new Button("Let's Go"));

        Button loadBtn = new Button("Load Saved Game");
        loadBtn.setOnAction(event -> {
            JSONObject jsonObject = loadJsonFile();
            if (jsonObject != null) {
                System.out.println("Loaded JSON: " + jsonObject);
                GameEngine.getInstance().fromJson(jsonObject);
            }
        });

        layout.getChildren().add(loadBtn);

        // handle button action when Player submits
        ((Button) layout.getChildren().get(2)).setOnAction(e -> {
            try {
                playerCount = Integer.parseInt(((TextField) layout.getChildren().get(1)).getText());

                // start prompting playerCount times to get Player details like name, age
                if (playerCount > 1 && playerCount <= PlayerManager.MAX_PLAYERS) {
                    PlayerManager.getInstance().setNumberOfPlayers(playerCount);
                    currentPlayerIndex = 0;
                    promptForPlayerInfo();
                } else {    // handle errors (numOfPlayers is incorrect)
                    promptLabel.setText("Only 1 to " + PlayerManager.MAX_PLAYERS + " players are allowed!");
                }
            } catch (NumberFormatException ex) {
                promptLabel.setText("Only 1 to " + PlayerManager.MAX_PLAYERS + " players are allowed!");
            }
        });
    }

    /*
        displays Bootstrap style form asking player details like name, age & color
        Acknowledgement: ChatGPT, OpenAI, 2024
                     [The code for displaying the form to ask for player details
                      the game]
     */
    private void promptForPlayerInfo() {
        GameEngine gameEngine = GameEngine.getInstance();
        // remove previous form UI components
        layout.getChildren().clear();

        // Ask player to enter name, enter age & select color from dropdown menu
        Label nameLabel = new Label("Enter Player " + (currentPlayerIndex + 1) + "'s name:");
        TextField nameField = new TextField();
        Label ageLabel = new Label("Enter Player " + (currentPlayerIndex + 1) + "'s age:");
        TextField ageField = new TextField();
        Label colorLabel = new Label("Preferred Color:");
        ComboBox<ColorOption> colorComboBox = new ComboBox<>();
        colorComboBox.setPromptText("Choose a color");
        colorComboBox.getItems().addAll(colorList);

        // player press submit to submit details
        Button submitButton = new Button("Submit");

        // add all UI components to layout
        layout.getChildren().addAll(nameLabel, nameField, ageLabel, ageField, colorLabel, colorComboBox, submitButton);

        // submit Player details to gameEngine when submit is pressed
        submitButton.setOnAction(e -> {
            try {
                String playerName = nameField.getText();
                int playerAge = Integer.parseInt(ageField.getText());
                ColorOption preferredColor = colorComboBox.getSelectionModel().getSelectedItem();

                if (playerAge >= 5 && playerAge <= 99) {    // add player to gameEngine if correct age
                    PlayerManager.getInstance().addPlayer(playerName, playerAge, preferredColor);
                    colorList.remove(preferredColor);

                    currentPlayerIndex++;
                }

                if (currentPlayerIndex < playerCount) { // prompt remaining Players for details
                    promptForPlayerInfo();
                } else {    // once done prompting load the Game and allow player to set turns
                    gameEngine.initialize();
                    setUp();
                }

            } catch (NumberFormatException ex) {
                System.out.println("Please enter valid information.");
            } catch (NullPointerException np) {
                System.out.println("Please provide all information.");
            }
        });
    }

    /*
        initial set up of the board and dragon cards to allow player to flip dragon cards to decide the turns
     */
    public void setUp() {
        // create an HBox as the main layout
        HBox mainLayout = new HBox();
        mainLayout.setSpacing(20);

        AnchorPane anchorPane = new AnchorPane();

        // initialize inner displays
        this.detailsDisplay = new DisplayDetails();
        this.dragonCardsDisplay = new DisplayDragonCards();
        this.boardDisplay = new DisplayBoard();

        // add dragon cards display into board display (display dragon cards in centre of board)
        this.boardDisplay.getDisplay().getChildren().add(this.dragonCardsDisplay.getDisplay());

        // add inner displays to mainLayout
        mainLayout.getChildren().add(this.boardDisplay.getDisplay());
        mainLayout.getChildren().add(this.detailsDisplay.getDisplay());

        // ADDING background
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/fierydragons.png")));
        ImageView backgroundImageView = new ImageView(backgroundImage);

        // values are fixed here, need to find out how to get dynamic data
        backgroundImageView.setFitWidth(2000);
        backgroundImageView.setFitHeight(1000);

        backgroundImageView.setEffect(new BoxBlur(10, 10, 3));
        anchorPane.getChildren().add(backgroundImageView);

        anchorPane.getChildren().add(mainLayout);

        // create a scene and set it on the stage
        // use mainLayout to display all game components
        scene = new Scene(anchorPane, 900, 600);
        primaryStage.setScene(scene);
        // use css
        scene.getStylesheets().add(getClass().getResource("/com/fierydragons/game/styles.css").toExternalForm());
        primaryStage.setTitle("Fiery Dragons");
        primaryStage.show();

        // perform update to load game components UI
        this.update();
    }

    /*
        update the game UI upon r interaction
     */
    public void update() {
        // update UI of the inner displays
        this.dragonCardsDisplay.update();
        this.boardDisplay.update();
        this.detailsDisplay.update();
    }

    // reload game from json file
    public JSONObject loadJsonFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open JSON File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );

        File file = fileChooser.showOpenDialog(this.primaryStage);
        if (file != null) {
            try (FileReader fileReader = new FileReader(file)) {
                StringBuilder sb = new StringBuilder();
                int i;
                while ((i = fileReader.read()) != -1) {
                    sb.append((char) i);
                }
                return new JSONObject(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // save game into a json file
    public void saveJsonFile(JSONObject jsonObject) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save JSON File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );

        File file = fileChooser.showSaveDialog(this.primaryStage);
        if (file != null) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
