package com.fierydragons.game.display;

import javafx.scene.layout.Pane;
/*
    Author: Arvind Siva
    Co-authored:
 */
/*
    InnerDisplay interface used by displays within MainDisplay that represents game components
 */
public interface InnerDisplay {
    void update();  // update UI after user interaction
    Pane getDisplay();  // get display holding all the inner UI elements
}
