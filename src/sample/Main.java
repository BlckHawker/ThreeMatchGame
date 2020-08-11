package sample;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    static final int TILE_SIZE = 40; //width and height of the tile
    static final int WIDTH = 800; //the width of the widow
    static final int HEIGHT = 600; //the height of the widow

    static final int Y_TILES =  HEIGHT/ TILE_SIZE; //the number of rows
    static final int X_TILES =  WIDTH / TILE_SIZE; //the number of columns
    static int score;

    static boolean tileSelected;
    static Tile[][] grid;

    static Pane board;
    static BorderPane game;
    static Stage window;

    @Override
    public void start(Stage stage)
    {
        window = stage;
        window.setScene(new Scene(Game.createGame()));
        window.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
