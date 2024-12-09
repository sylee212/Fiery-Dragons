module com.fierydragons.game {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;

    opens com.fierydragons.game to javafx.fxml;
    exports com.fierydragons.game;
    exports com.fierydragons.game.animal;
    opens com.fierydragons.game.animal to javafx.fxml;
    exports com.fierydragons.game.dragoncard;
    opens com.fierydragons.game.dragoncard to javafx.fxml;
}